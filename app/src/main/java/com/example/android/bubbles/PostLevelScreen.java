package com.example.android.bubbles;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostLevelScreen extends AppCompatActivity {

    TextView postLevelTitle;
    Button postLevelToLevelSelectButton;

    private final String LEVEL_RESULT = "PASS_OR_FAIL";
    private final String PASSED = "pass";
    private final String CURRENT_LEVEL = "CURRENT_LEVEL";
    private final String BUBBLE_COLOR = "BUBBLE_COLOR";
    private final String LEVEL_COMPLETION_TIME = "COMPLETION_TIME";
    private long oldBubbleTime = -1;
    private String level;
    private String bubbleColor;
    private double newTime;
    private int displayCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent levelResult = getIntent();
        String choosePostLevelScreen = levelResult.getStringExtra(LEVEL_RESULT);

        // Chooses which menu screen to display - pass or fail
        if (choosePostLevelScreen.equals(PASSED)) {
            setContentView(R.layout.level_passed_menu);
            postLevelTitle = (TextView) findViewById(R.id.levelPassedTitleTextView);
            postLevelToLevelSelectButton = (Button) findViewById(R.id.successToLevelSelectButton);
            postLevelTitle.setText(R.string.post_level_passed_title);
            postLevelToLevelSelectButton.setText(R.string.post_level_encouragement);
        } else {
            setContentView(R.layout.failure_menu);
            postLevelTitle = (TextView) findViewById(R.id.failureTitleTextView);
            postLevelToLevelSelectButton = (Button) findViewById(R.id.failureToLevelSelectButton);
            postLevelTitle.setText(R.string.post_level_failed_title);
            postLevelToLevelSelectButton.setText(R.string.post_level_discouraged);
        }

        // Checks if there's a new best time for Firebase
        if (choosePostLevelScreen.equals(PASSED)) {
            level = levelResult.getStringExtra(CURRENT_LEVEL);
            bubbleColor = levelResult.getStringExtra(BUBBLE_COLOR);
            newTime = (double) (levelResult.getLongExtra(LEVEL_COMPLETION_TIME, 0) / 1000);

            updateBestTimes();
        }

        postLevelToLevelSelectButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, LevelSelect.class);
                context.startActivity(intent);
            }
        });
    }

    /** Compares newest completion time with the best time stored in Firebase and replaces it if a new high score has been set
     */
    private void updateBestTimes() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference referenceToDatabase = database.getReference("Level" + level);

        if((oldBubbleTime!=-1) && (newTime<oldBubbleTime)) {
            referenceToDatabase.child(bubbleColor).setValue(newTime);
            if(displayCounter==0) {
                Toast.makeText(getBaseContext(), "New High Score!", Toast.LENGTH_SHORT).show();
                displayCounter++;
            }
            postLevelToLevelSelectButton.callOnClick();
        }

        referenceToDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                oldBubbleTime = dataSnapshot.child(bubbleColor).getValue(Long.class);
                updateBestTimes();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Failed to update Firebase - please reload", Toast.LENGTH_SHORT).show();
            }
        });
    }
}