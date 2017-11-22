package com.example.android.bubbles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ColorSelector extends Activity {

    TextView colorSelectorTitle;
    Button greenSelectorButton;
    Button blueSelectorButton;
    Button redSelectorButton;
    TextView GreenEffectDescription;
    TextView blueEffectDescription;
    TextView redEffectDescription;

    private final String BUBBLE_COLOR = "BUBBLE_COLOR";
    private final String GREEN_COLOR = "GreenBubble";
    private final String BLUE_COLOR = "BlueBubble";
    private final String RED_COLOR = "RedBubble";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bubble_selector_menu);
        final Intent selectedLevelIntent = getIntent();

        colorSelectorTitle = (TextView) findViewById(R.id.bubbleColorSelectorTitleText);
        greenSelectorButton = (Button) findViewById(R.id.greenBubbleSelectorButton);
        blueSelectorButton = (Button) findViewById(R.id.blueBubbleSelectorButton);
        redSelectorButton = (Button) findViewById(R.id.redBubbleSelectorButton);
        GreenEffectDescription = (TextView) findViewById(R.id.greenBubbleEffectDescriptionTextView);
        blueEffectDescription = (TextView) findViewById(R.id.blueBubbleEffectDescriptionTextView);
        redEffectDescription = (TextView) findViewById(R.id.redBubbleEffectDescriptionTextView);

        colorSelectorTitle.setText(R.string.colors_select_prompt);
        greenSelectorButton.setText(R.string.colors_green);
        blueSelectorButton.setText(R.string.colors_blue);
        redSelectorButton.setText(R.string.colors_red);
        GreenEffectDescription.setText(R.string.colors_green_desc);
        blueEffectDescription.setText(R.string.colors_blue_desc);
        redEffectDescription.setText(R.string.colors_red_desc);

        greenSelectorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View currentView) {
                Context context = currentView.getContext();
                Intent intent = new Intent(context, DrawLevel.class);
                intent.putExtras(selectedLevelIntent);
                intent.putExtra(BUBBLE_COLOR, GREEN_COLOR);
                context.startActivity(intent);
            }
        });

        blueSelectorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View currentView) {
                Context context = currentView.getContext();
                Intent intent = new Intent(context, DrawLevel.class);
                intent.putExtras(selectedLevelIntent);
                intent.putExtra(BUBBLE_COLOR, BLUE_COLOR);
                context.startActivity(intent);
            }
        });

        redSelectorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View currentView) {
                Context context = currentView.getContext();
                Intent intent = new Intent(context, DrawLevel.class);
                intent.putExtras(selectedLevelIntent);
                intent.putExtra(BUBBLE_COLOR, RED_COLOR);
                context.startActivity(intent);
            }
        });
    }



}
