package com.firstime.libgdx.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Obstacle {

    private float x;
    private final float y;
    private final float width;
    private final float height;
    private final TextureRegion region;

    public Obstacle(float x, float y, float width, float height, TextureRegion region) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.region = region;
    }

    public void update(float delta, float speed) {
        x -= speed * delta;
    }

    public void render(SpriteBatch batch) {
        if (region != null) {
            batch.draw(region, x, y, width, height);
        }
    }

    public boolean isOffscreen() {
        return x + width < 0f;
    }

    public Rectangle getBounds() {
        float pad = width * 0.1f;
        return new Rectangle(x + pad, y, width - pad * 2f, height);
    }
}