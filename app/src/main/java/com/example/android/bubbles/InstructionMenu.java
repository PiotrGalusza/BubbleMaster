package com.example.android.bubbles;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InstructionMenu extends AppCompatActivity {

    TextView helpTitle;
    TextView goalTopText;
    TextView warningMiddleText;
    TextView bubbleColorsBottomText;
    Button comprehensionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructions_menu);

        helpTitle = (TextView) findViewById(R.id.helpMenuTitleText);
        goalTopText = (TextView) findViewById(R.id.goalDescriptionTopText);
        warningMiddleText = (TextView) findViewById(R.id.CollisionWarningMiddleText);
        bubbleColorsBottomText = (TextView) findViewById(R.id.BubbleColorsBottomText);
        comprehensionButton = (Button) findViewById(R.id.toLevelSelectFromHelpButton);

        helpTitle.setText(R.string.help_title);
        goalTopText.setText(R.string.help_goal_desc);
        warningMiddleText.setText(R.string.help_warning_desc);
        bubbleColorsBottomText.setText(R.string.help_bubble_info_desc);
        comprehensionButton.setText(R.string.help_confirmation);

        comprehensionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, LevelSelect.class);
                context.startActivity(intent);
            }
        });
    }

}
