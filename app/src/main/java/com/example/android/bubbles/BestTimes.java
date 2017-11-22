package com.example.android.bubbles;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BestTimes extends AppCompatActivity {

    TextView bestTimesTitle;
    TextView blueBubText;
    TextView greenBubText;
    TextView redBubText;
    TextView blueBubDatabaseTime;
    TextView greenBubDatabaseTime;
    TextView redBubDatabaseTime;
    Button backButton;

    private String titleText = "Current Best Times For Level ";
    private String blueBubbleTime;
    private String greenBubbleTime;
    private String redBubbleTime;
    private String currentLevelToCheck;
    private final String BLUE_DB_CHILD = "BlueBubble";
    private final String GREEN_DB_CHILD = "GreenBubble";
    private final String RED_DB_CHILD = "RedBubble";
    private final String CURRENT_LEVEL = "CURRENT_LEVEL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.best_times_menu);

        bestTimesTitle = (TextView) findViewById(R.id.bestTimesTitleText);
        blueBubText = (TextView) findViewById(R.id.blueBubbleBestTimeText);
        greenBubText = (TextView) findViewById(R.id.greenBubbleBestTimesText);
        redBubText = (TextView) findViewById(R.id.redBubbleBestTimesText);
        blueBubDatabaseTime = (TextView) findViewById(R.id.blueBubbleDatabaseTime);
        greenBubDatabaseTime = (TextView) findViewById(R.id.greenBubbleDatabaseTime);
        redBubDatabaseTime = (TextView) findViewById(R.id.redBubbleDatabaseTime);
        backButton = (Button) findViewById(R.id.returnToLevelSelectButton);

        Intent currentLevelIntent = getIntent();
        currentLevelToCheck = currentLevelIntent.getStringExtra(CURRENT_LEVEL);
        verifyLevel(currentLevelToCheck);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference referenceToDatabase = database.getReference("Level" + currentLevelToCheck);

        bestTimesTitle.setText(titleText);
        backButton.setText(R.string.times_back);
        blueBubText.setText(R.string.times_blue_bub);
        greenBubText.setText(R.string.times_green_bub);
        redBubText.setText(R.string.times_red_bub);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View currentView) {
                Context context = currentView.getContext();
                Intent intent = new Intent(context, LevelSelect.class);
                context.startActivity(intent);
            }
        });

        referenceToDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                blueBubbleTime = dataSnapshot.child(BLUE_DB_CHILD).getValue(Long.class).toString();
                greenBubbleTime = dataSnapshot.child(GREEN_DB_CHILD).getValue(Long.class).toString();
                redBubbleTime = dataSnapshot.child(RED_DB_CHILD).getValue(Long.class).toString();
                blueBubDatabaseTime.setText(blueBubbleTime);
                greenBubDatabaseTime.setText(greenBubbleTime);
                redBubDatabaseTime.setText(redBubbleTime);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Failed to access Firebase - please reload", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Verifies that the user requested level is valid and nothing went wrong
     *
     * @param potentialLevel User requested level
     */
    private void verifyLevel(String potentialLevel) {
        if(!potentialLevel.equals("One") && !potentialLevel.equals("Two") && !potentialLevel.equals("Three")) {
            Context context = getBaseContext();
            Toast.makeText(context, "Something has gone terribly wrong, please try again", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, LevelSelect.class);
            context.startActivity(intent);
        }

        titleText += potentialLevel;
    }

}
