package com.firstime.libgdx.util;

public final class GameConfig {

    private GameConfig() {
    }

    public static final float WORLD_WIDTH = 800f;
    public static final float WORLD_HEIGHT = 400f;
    public static final float GROUND_Y = 70f;

    public static final float GRAVITY = -2200f;
    public static final float JUMP_VELOCITY = 780f;

    public static final float BASE_SPEED = 300f;
    public static final float SPEED_GROWTH = 6f;
    public static final float MAX_SPEED = 650f;

    public static final float MIN_OBSTACLE_GAP = 1.1f;
    public static final float MAX_OBSTACLE_GAP = 2.2f;

    public static final float RUN_FRAME_DURATION = 0.09f;
    public static final float SCORE_PER_SECOND = 10f;

    public static final String PREFS_NAME = "dino-runner-prefs";
    public static final String HIGH_SCORE_KEY = "high-score";
}