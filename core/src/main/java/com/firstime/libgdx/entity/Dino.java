package com.firstime.libgdx.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.firstime.libgdx.assets.GameAssets;
import com.firstime.libgdx.util.GameConfig;

import java.util.List;

public class Dino {

    public enum State { RUNNING, JUMPING, HURT }

    private final float x = 90f;
    private float y = GameConfig.GROUND_Y;
    private float velocityY = 0f;
    private final float width;
    private final float height;

    private State state = State.RUNNING;
    private float stateTime = 0f;

    private final Animation<TextureRegion> runAnimation;
    private final TextureRegion jumpFrame;
    private final TextureRegion hurtFrame;
    private final boolean hasAssets;

    public Dino(GameAssets assets) {
        hasAssets = assets.hasDinoFrames();
        if (hasAssets) {
            runAnimation = new Animation<>(GameConfig.RUN_FRAME_DURATION,
                toArray(assets.dinoRunFrames), Animation.PlayMode.LOOP);
            jumpFrame = pickFrame(assets.dinoJumpFrames, assets.dinoRunFrames.get(0));
            hurtFrame = pickFrame(assets.dinoHurtFrames, assets.dinoRunFrames.get(0));
            TextureRegion sample = assets.dinoRunFrames.get(0);
            height = 110f;
            width = height * sample.getRegionWidth() / (float) sample.getRegionHeight();
        } else {
            runAnimation = null;
            jumpFrame = null;
            hurtFrame = null;
            width = 60f;
            height = 70f;
        }
    }

    private TextureRegion pickFrame(List<TextureRegion> frames, TextureRegion fallback) {
        return frames.isEmpty() ? fallback : frames.get(0);
    }

    private Array<TextureRegion> toArray(List<TextureRegion> frames) {
        Array<TextureRegion> array = new Array<>();
        for (TextureRegion frame : frames) {
            array.add(frame);
        }
        return array;
    }

    public void jump() {
        if (state == State.RUNNING) {
            velocityY = GameConfig.JUMP_VELOCITY;
            state = State.JUMPING;
        }
    }

    public void hurt() {
        state = State.HURT;
    }

    public void update(float delta) {
        stateTime += delta;
        if (state == State.JUMPING) {
            velocityY += GameConfig.GRAVITY * delta;
            y += velocityY * delta;
            if (y <= GameConfig.GROUND_Y) {
                y = GameConfig.GROUND_Y;
                velocityY = 0f;
                state = State.RUNNING;
            }
        }
    }

    public void render(SpriteBatch batch) {
        if (!hasAssets) {
            return;
        }
        TextureRegion frame;
        switch (state) {
            case JUMPING:
                frame = jumpFrame;
                break;
            case HURT:
                frame = hurtFrame;
                break;
            default:
                frame = runAnimation.getKeyFrame(stateTime);
        }
        batch.draw(frame, x, y, width, height);
    }

    public boolean hasAssets() {
        return hasAssets;
    }

    public Rectangle getBounds() {
        float pad = width * 0.15f;
        return new Rectangle(x + pad, y + pad * 0.5f, width - pad * 2f, height - pad);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}