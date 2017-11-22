package com.example.android.bubbles;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.Toast;

import java.util.ArrayList;

public class DrawLevel extends AppCompatActivity {

    private LevelView currentLevelView;
    private SensorManager sensorManager;
    private WindowManager windowManager;
    private Display phoneDisplay;
    Button acceptFailureButton;

    private final String LEVEL_RESULT = "PASS_OR_FAIL";
    private final String PASSED = "pass";
    private final String FAILED = "fail";
    private final String BUBBLE_COLOR = "BUBBLE_COLOR";
    private final String CURRENT_LEVEL = "CURRENT_LEVEL";
    private final String LEVEL_COMPLETION_TIME = "COMPLETION_TIME";
    private ColorEffects chosenColor;
    private LevelEffects chosenLevel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        phoneDisplay = windowManager.getDefaultDisplay();

        String requestedColor = getIntent().getStringExtra(BUBBLE_COLOR);
        chosenColor = new ColorEffects(requestedColor);

        String requestedLevel = getIntent().getStringExtra(CURRENT_LEVEL);
        chosenLevel = new LevelEffects(requestedLevel);
        currentLevelView = new LevelView(this);
        currentLevelView.setBackgroundResource(chosenLevel.getChosenBackResource());


        acceptFailureButton = new Button(this);
        acceptFailureButton.setText(R.string.level_accept_defeat);
        acceptFailureButton.setLayoutParams(new ViewGroup.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));
        acceptFailureButton.setGravity(Gravity.CENTER);

        currentLevelView.addView(acceptFailureButton);
        setContentView(currentLevelView);

        acceptFailureButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, PostLevelScreen.class);
                intent.putExtra(LEVEL_RESULT, FAILED);
                context.startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentLevelView.startSimulation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        currentLevelView.stopSimulation();
    }

    /** A FrameLayout which contains all the data pertaining to level, obstacle, and Bubbble creation
     */
    class LevelView extends FrameLayout implements SensorEventListener {
        private Sensor accelerometerSensor;
        private Bubble coloredBub;
        private ArrayList<RectF> obstacles = new ArrayList<>(chosenLevel.getObstacles());
        private RectF goal = new RectF();
        private Paint redPaint;
        private Paint goalPaint;

        private long startingTime;
        private long previousTime;
        private int bubbleWidth;
        private int bubbleHeight;

        private float distanceToXPixel;
        private float distanceToYPixel;
        private float originX;
        private float originY;
        private float sensorX;
        private float sensorY;
        private float maxXBound;
        private float maxYBound;

        class Bubble extends View {
            private float currentXPos = -0.5f;//--> Starts on the left side of the screen
            private float currentYPos = 0.0f;//--> Starts centered vertically
            private float currentXVel = 0;
            private float currentYVel = 0;
            private int numOfCollisions = 0;

            public Bubble(Context context) {
                super(context);
                this.setLayerType(LAYER_TYPE_HARDWARE, null);

                this.setBackgroundResource(chosenColor.getBubbleColorResource());
                addView(this, new ViewGroup.LayoutParams(bubbleWidth, bubbleHeight));
            }

            /** Uses current position, time since last update, current velocity, and assigned friction multiplier to update the bubble's
             * new position and directional velocities/
             *
             * @param xPos Current x position
             * @param yPos Current y position
             * @param timeDiff Time since last update
             */
            public void computeNewPhysics(float xPos, float yPos, float timeDiff) {
                float xAcceleration = -xPos/chosenColor.getCurrAccelDivider();
                float yAcceleration = -yPos/chosenColor.getCurrAccelDivider();

                currentXPos += currentXVel * timeDiff + xAcceleration * timeDiff * timeDiff / 2;
                currentYPos += currentYVel * timeDiff + yAcceleration * timeDiff * timeDiff / 2;
                currentXVel += (xAcceleration * timeDiff) * chosenColor.getFrictionMultiplier();
                currentYVel += (yAcceleration * timeDiff) * chosenColor.getFrictionMultiplier();
            }

            /** Checks and punishes the user if their bubble has collided with or is in the 'gravity' effect of an obstacle
             */
            public void checkObstacleCollisions() {
                float leftSide = this.getX();
                float rightSide = this.getX() + bubbleWidth;
                float topSide = this.getY();
                float bottomSide = this.getY() + bubbleHeight;

                for(RectF obstacle : obstacles) {
                    if(rightSide-obstacle.left<0.000001 && (Math.abs(leftSide-obstacle.left)<=bubbleWidth+2) && (topSide>=obstacle.top) && (bottomSide<=obstacle.bottom)) {
                        currentXVel = -0.01f;
                        numOfCollisions++;
                    } else if(leftSide-obstacle.right<0.000001 && (Math.abs(rightSide-obstacle.right)<=bubbleWidth+2) && (topSide>=obstacle.top) && (bottomSide<=obstacle.bottom)) {
                        currentXVel = 0.01f;
                        numOfCollisions++;
                    } else if(topSide-obstacle.bottom<0.000001 && (Math.abs(bottomSide-obstacle.bottom)<=bubbleHeight+2) && (leftSide>=obstacle.left) && (rightSide<=obstacle.right)) {
                        currentYVel = -0.01f;
                        numOfCollisions++;
                    } else if(bottomSide-obstacle.top<0.000001 && (Math.abs(topSide-obstacle.top)<=bubbleHeight+2) && (leftSide>=obstacle.left) && (rightSide<=obstacle.right)) {
                        currentYVel = 0.01f;
                        numOfCollisions++;
                    }
                }

            }

            /** Checks if the Bubble has reached the goal yet
             */
            public void checkIfGoalReached() {
                if(goal.contains(getX(), getY())) {
                    long levelCompletionTime = System.currentTimeMillis() - startingTime;

                    stopSimulation();
                    Intent intent = new Intent(getContext(), PostLevelScreen.class);
                    intent.putExtra(LEVEL_RESULT, PASSED);
                    intent.putExtra(CURRENT_LEVEL, chosenLevel.getCurrentLevel());
                    intent.putExtra(BUBBLE_COLOR, chosenColor.getSelectedColor());
                    intent.putExtra(LEVEL_COMPLETION_TIME, levelCompletionTime);
                    getContext().startActivity(intent);
                }
            }

            /** Checks and rectifies any collisions the bubble has with the phone screen edges
             */
            public void checkEdgeCollisions() {
                if(currentXPos > maxXBound) {
                    currentXPos = maxXBound;
                    currentXVel = -0.02f;
                    numOfCollisions++;
                } else if(currentXPos < -maxXBound) {
                    currentXPos = -maxXBound;
                    currentXVel = 0.02f;
                    numOfCollisions++;
                }
                if(currentYPos > maxYBound) {
                    currentYPos = maxYBound;
                    currentYVel = -0.02f;
                    numOfCollisions++;
                } else if(currentYPos < -maxYBound) {
                    currentYPos = -maxYBound;
                    currentYVel = 0.02f;
                    numOfCollisions++;
                }
            }

            /** Updates the bubble's position via Verlet integration (more or less...) and rectifies any collisions or failure
             *
             * @param sx
             * @param sy
             * @param now Current time in milliseconds
             */
            public void updateBubble(float sx, float sy, long now) {
                if (previousTime != 0) {
                    float timeDifference = (float) (now - previousTime) / 1000.f;
                    this.computeNewPhysics(sx, sy, timeDifference);
                } else {
                    startingTime = now;
                }
                previousTime = now;

                this.checkObstacleCollisions();
                this.checkEdgeCollisions();
                this.checkIfGoalReached();

                if(numOfCollisions == 5) {
                    stopSimulation();
                    Intent intent = new Intent(getContext(), PostLevelScreen.class);
                    intent.putExtra(LEVEL_RESULT, FAILED);
                    getContext().startActivity(intent);
                }
            }

            public float getPosX() {
                return this.currentXPos;
            }

            public float getPosY() {
                return this.currentYPos;
            }
        }

        /** Retrieves accelerometer data ata  slower rate to add an illusion of realism
         */
        public void startSimulation() {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        }

        /** Unregisters the sensor once the level is completed or paused
         */
        public void stopSimulation() {
            sensorManager.unregisterListener(this);
        }

        /** Creates the level with all necessary obstacles and Bubbles
         *
         * @param context Application's Base Context
         */
        public LevelView(Context context) {
            super(context);
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            DisplayMetrics levelMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(levelMetrics);
            distanceToXPixel = levelMetrics.xdpi / 0.0254f;
            distanceToYPixel = levelMetrics.ydpi / 0.0254f;

            // bubble length/width set to change depending on difficulty
            bubbleWidth = (int) (chosenColor.getBubbleDiameter() * distanceToXPixel + 0.75f);
            bubbleHeight = (int) (chosenColor.getBubbleDiameter() * distanceToYPixel + 0.75f);
            coloredBub = new Bubble(getContext());

            goal = new RectF(700, 180, 850, 250);

            redPaint = new Paint();
            redPaint.setColor(Color.RED);
            goalPaint = new Paint();
            goalPaint.setColor(Color.GREEN);
        }

        /** Determines the farthest distance the Bubble may move
         *
         * @param width Current screen width
         * @param height Current screen height
         * @param oldWidth Previous screen width
         * @param oldHeight Previous screen height
         */
        @Override
        protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
            originX = (width - bubbleWidth) * 0.5f;
            originY = (height - bubbleHeight) * 0.5f;
            maxXBound = ((width / distanceToXPixel - chosenColor.getBubbleDiameter()) * 0.5f);
            maxYBound = ((height / distanceToYPixel - chosenColor.getBubbleDiameter()) * 0.5f);
        }

        /** Records accelerometer data relative to one of four tilted directions
         *
         * @param event data sent from accelerometer
         */
        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (phoneDisplay.getRotation()) {
                case Surface.ROTATION_0:
                    sensorX = event.values[0];
                    sensorY = event.values[1];
                    break;
                case Surface.ROTATION_90:
                    sensorX = -event.values[1];
                    sensorY = event.values[0];
                    break;
                case Surface.ROTATION_180:
                    sensorX = -event.values[0];
                    sensorY = -event.values[1];
                    break;
                case Surface.ROTATION_270:
                    sensorX = event.values[1];
                    sensorY = -event.values[0];
                    break;
                default:
                    Toast.makeText(getContext(), "Something terrible has happened to your sensor...", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        /** Draws the current level with everything it needs to contain
         *
         * @param canvas Activity screen
         */
        @Override
        protected void onDraw(Canvas canvas) {
            Bubble currentBubble = coloredBub;
            for(RectF obstacle : obstacles) {
                canvas.drawRect(obstacle, redPaint);
            }
            canvas.drawRect(goal, goalPaint);

            currentBubble.updateBubble(sensorX, sensorY, System.currentTimeMillis());
            float startingX = originX + currentBubble.getPosX() * distanceToXPixel;
            float startingY = originY - currentBubble.getPosY() * distanceToYPixel;
            currentBubble.setTranslationX(startingX);
            currentBubble.setTranslationY(startingY);

            // get that bubble moving!
            invalidate();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Nothing to do in here c:
        }
    }
}
