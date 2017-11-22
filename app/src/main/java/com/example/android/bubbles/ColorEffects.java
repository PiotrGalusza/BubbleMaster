package com.example.android.bubbles;

public class ColorEffects {
    private String selectedColor;
    private final String GREEN_COLOR = "GreenBubble";
    private final String BLUE_COLOR = "BlueBubble";
    private final String RED_COLOR = "RedBubble";
    private final int GREEN_ACCEL_DIVIDER = 25;
    private final int BLUE_ACCEL_DIVIDER = 10;
    private final int RED_ACCEL_DIVIDER = 5;
    private int currAccelDivider;
    private final float GREEN_BUB_SIZE = 0.004f;
    private final float BLUE_BUB_SIZE = 0.006f;
    private final float RED_BUB_SIZE = 0.008f;
    private float bubbleDiameter;
    private final double GREEN_FRICTION = 0.75;
    private final double BLUE_FRICTION = 0.9;
    private final double RED_FRICTION = 1.5;
    private double frictionMultiplier;
    private int bubbleColorResource;


    public ColorEffects(String desiredColor) {
        selectedColor = desiredColor;

        switch (desiredColor) {
            case(GREEN_COLOR):
                currAccelDivider = GREEN_ACCEL_DIVIDER;
                bubbleDiameter = GREEN_BUB_SIZE;
                frictionMultiplier = GREEN_FRICTION;
                bubbleColorResource = R.drawable.greenball;
                break;
            case(BLUE_COLOR):
                currAccelDivider = BLUE_ACCEL_DIVIDER;
                bubbleDiameter = BLUE_BUB_SIZE;
                frictionMultiplier = BLUE_FRICTION;
                bubbleColorResource = R.drawable.blueball;
                break;
            case(RED_COLOR):
                currAccelDivider = RED_ACCEL_DIVIDER;
                bubbleDiameter = RED_BUB_SIZE;
                frictionMultiplier = RED_FRICTION;
                bubbleColorResource = R.drawable.redball;
                break;
            default:
                break;
        }
    }

    public String getSelectedColor() {
        return selectedColor;
    }

    public int getCurrAccelDivider() {
        return currAccelDivider;
    }

    public float getBubbleDiameter() {
        return bubbleDiameter;
    }

    public double getFrictionMultiplier() {
        return frictionMultiplier;
    }

    public int getBubbleColorResource() {
        return bubbleColorResource;
    }
}
