package com.example.android.bubbles;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class LevelSelect extends AppCompatActivity{

    TextView levelSelect;
    Button helpButton;
    TextView levelOneText;
    TextView levelTwoText;
    TextView levelThreeText;
    ImageButton levelOneButton;
    ImageButton levelTwoButton;
    ImageButton levelThreeButton;
    Button levelOneTimesButton;
    Button levelTwoTimesButton;
    Button levelThreeTimesButton;

    private final String CURRENT_LEVEL = "CURRENT_LEVEL";
    private final String LEVEL_ONE = "One";
    private final String LEVEL_TWO = "Two";
    private final String LEVEL_THREE = "Three";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_select_menu);

        levelSelect = (TextView) findViewById(R.id.levelSelectTextView);
        helpButton = (Button) findViewById(R.id.helpButton);
        levelOneText = (TextView) findViewById(R.id.levelOneText);
        levelTwoText = (TextView) findViewById(R.id.levelTwoText);
        levelThreeText = (TextView) findViewById(R.id.levelThreeText);
        levelOneButton = (ImageButton) findViewById(R.id.levelOneImageButton);
        levelTwoButton = (ImageButton) findViewById(R.id.levelTwoImageButton);
        levelThreeButton = (ImageButton) findViewById(R.id.levelThreeImageButton);
        levelOneTimesButton = (Button) findViewById(R.id.levelOneBestTimesButton);
        levelTwoTimesButton = (Button) findViewById(R.id.levelTwoBestTimesButton);
        levelThreeTimesButton = (Button) findViewById(R.id.levelThreeBestTimesButton);

        levelSelect.setText(R.string.level_select_title);
        helpButton.setText(R.string.level_select_help);
        levelOneText.setText("1");
        levelTwoText.setText("2");
        levelThreeText.setText("3");
        Picasso.with(levelOneButton.getContext()).load(R.drawable.leveloneimage).into(levelOneButton);
        Picasso.with(levelTwoButton.getContext()).load(R.drawable.leveltwoimage).into(levelTwoButton);
        Picasso.with(levelThreeButton.getContext()).load(R.drawable.levelthreeimage).into(levelThreeButton);
        levelOneTimesButton.setText(R.string.level_select_one_times);
        levelTwoTimesButton.setText(R.string.level_select_two_times);
        levelThreeTimesButton.setText(R.string.level_select_three_times);

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View currentView) {
                Context context = currentView.getContext();
                Intent intent = new Intent(context, InstructionMenu.class);
                context.startActivity(intent);
            }
        });

        levelOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View currentView) {
                Context context = currentView.getContext();
                Intent intent = new Intent(context, ColorSelector.class);
                intent.putExtra(CURRENT_LEVEL, LEVEL_ONE);
                context.startActivity(intent);
            }
        });

        levelTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View currentView) {
                Context context = currentView.getContext();
                Intent intent = new Intent(context, ColorSelector.class);
                intent.putExtra(CURRENT_LEVEL, LEVEL_TWO);
                context.startActivity(intent);
            }
        });

        levelThreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View currentView) {
                Context context = currentView.getContext();
                Intent intent = new Intent(context, ColorSelector.class);
                intent.putExtra(CURRENT_LEVEL, LEVEL_THREE);
                context.startActivity(intent);
            }
        });

        levelOneTimesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View currentView) {
                Context context = currentView.getContext();
                Intent intent = new Intent(context, BestTimes.class);
                intent.putExtra(CURRENT_LEVEL, LEVEL_ONE);
                context.startActivity(intent);
            }
        });

        levelTwoTimesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View currentView) {
                Context context = currentView.getContext();
                Intent intent = new Intent(context, BestTimes.class);
                intent.putExtra(CURRENT_LEVEL, LEVEL_TWO);
                context.startActivity(intent);
            }
        });

        levelThreeTimesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View currentView) {
                Context context = currentView.getContext();
                Intent intent = new Intent(context, BestTimes.class);
                intent.putExtra(CURRENT_LEVEL, LEVEL_THREE);
                context.startActivity(intent);
            }
        });


    }


}
