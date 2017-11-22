package com.example.android.bubbles;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SensorManager initialVerifyingManager;
    TextView titleText;
    TextView preAlphaText;
    Button gameStartButton;
    private boolean containsAccelerometer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        titleText = (TextView) findViewById(R.id.titleTextView);
        preAlphaText = (TextView) findViewById(R.id.preAlphaTextView);
        gameStartButton = (Button) findViewById(R.id.gameStartButton);

        initialVerifyingManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if(initialVerifyingManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
            containsAccelerometer = true;
        }

        gameStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View currentView) {
                if(containsAccelerometer) {
                    Context context = currentView.getContext();
                    Intent intent = new Intent(context, LevelSelect.class);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Accelerometer not detected, please plug one in.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
