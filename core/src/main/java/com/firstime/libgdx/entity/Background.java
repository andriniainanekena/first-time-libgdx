package com.firstime.libgdx.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.firstime.libgdx.util.GameConfig;

public class Background {

    private static final float DASH_WIDTH = 26f;
    private static final float DASH_GAP = 18f;

    private static final Color SKY_TOP = new Color(0.53f, 0.81f, 0.92f, 1f);
    private static final Color SKY_BOTTOM = new Color(0.86f, 0.95f, 0.98f, 1f);
    private static final Color GROUND_COLOR = new Color(0.29f, 0.33f, 0.41f, 1f);
    private static final Color DASH_COLOR = new Color(0.75f, 0.55f, 0.35f, 1f);

    private float groundScroll = 0f;

    public void update(float delta, float speed) {
        groundScroll = (groundScroll + speed * delta) % (DASH_WIDTH + DASH_GAP);
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.rect(0, 0, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT,
            SKY_BOTTOM, SKY_BOTTOM, SKY_TOP, SKY_TOP);

        shapeRenderer.setColor(GROUND_COLOR);
        shapeRenderer.rect(0, GameConfig.GROUND_Y - 4f, GameConfig.WORLD_WIDTH, 4f);

        shapeRenderer.setColor(DASH_COLOR);
        float x = -groundScroll;
        while (x < GameConfig.WORLD_WIDTH) {
            shapeRenderer.rect(x, GameConfig.GROUND_Y - 10f, DASH_WIDTH, 4f);
            x += DASH_WIDTH + DASH_GAP;
        }

        shapeRenderer.end();
    }
}