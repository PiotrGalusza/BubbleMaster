package com.example.android.bubbles;

import android.graphics.RectF;

import java.util.ArrayList;

public class LevelEffects {
    private String currentLevel;
    private final String LEVEL_ONE = "One";
    private final String LEVEL_TWO = "Two";
    private final String LEVEL_THREE = "Three";
    private int chosenBackResource;
    private ArrayList<RectF> obstacles;

    public LevelEffects(String chosenLevel) {
        currentLevel = chosenLevel;
        obstacles = new ArrayList<>();

        switch (chosenLevel) {
            case(LEVEL_ONE):
                chosenBackResource = R.drawable.levelonebackground;
                obstacles.add(new RectF(60, 40, 185, 100));
                obstacles.add(new RectF(550, 40, 675, 100));
                obstacles.add(new RectF(60, 340, 185, 400));
                obstacles.add(new RectF(550, 340, 675, 400));
                obstacles.add(new RectF(350, 190, 475, 250));
                break;
            case(LEVEL_TWO):
                chosenBackResource = R.drawable.leveltwobackground;
                obstacles.add(new RectF(60, 40, 185, 100));
                obstacles.add(new RectF(550, 40, 675, 100));
                obstacles.add(new RectF(90, 340, 185, 380));
                obstacles.add(new RectF(500, 240, 625, 400));
                obstacles.add(new RectF(350, 50, 375, 250));
                break;
            case(LEVEL_THREE):
                chosenBackResource = R.drawable.levelthreebackground;
                obstacles.add(new RectF(70, 30, 185, 100));
                obstacles.add(new RectF(450, 40, 675, 220));
                obstacles.add(new RectF(120, 340, 185, 400));
                obstacles.add(new RectF(550, 270, 575, 400));
                obstacles.add(new RectF(350, 190, 500, 270));
                break;
            default:
                break;
        }
    }

    public String getCurrentLevel() {
        return currentLevel;
    }

    public int getChosenBackResource() {
        return chosenBackResource;
    }

    public ArrayList<RectF> getObstacles() {
        return obstacles;
    }
}
