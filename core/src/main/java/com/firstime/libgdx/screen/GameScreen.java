package com.firstime.libgdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.firstime.libgdx.assets.GameAssets;
import com.firstime.libgdx.entity.Background;
import com.firstime.libgdx.entity.Dino;
import com.firstime.libgdx.entity.Obstacle;
import com.firstime.libgdx.entity.ObstacleSpawner;
import com.firstime.libgdx.util.GameConfig;

public class GameScreen implements Screen {

    private enum State { RUNNING, GAME_OVER }

    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private GlyphLayout glyphLayout;

    private GameAssets assets;
    private Dino dino;
    private ObstacleSpawner spawner;
    private Background background;
    private Preferences preferences;

    private State state = State.RUNNING;
    private float speed = GameConfig.BASE_SPEED;
    private float score = 0f;
    private int highScore = 0;

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(1.4f);
        glyphLayout = new GlyphLayout();

        assets = new GameAssets();
        dino = new Dino(assets);
        spawner = new ObstacleSpawner(assets);
        background = new Background();

        preferences = Gdx.app.getPreferences(GameConfig.PREFS_NAME);
        highScore = preferences.getInteger(GameConfig.HIGH_SCORE_KEY, 0);

        if (assets.music != null) {
            assets.music.play();
        }
    }

    @Override
    public void render(float delta) {
        handleInput();
        if (state == State.RUNNING) {
            update(delta);
        }
        draw();
    }

    private void handleInput() {
        boolean jumpPressed = Gdx.input.isKeyJustPressed(Input.Keys.SPACE)
            || Gdx.input.isKeyJustPressed(Input.Keys.UP)
            || Gdx.input.justTouched();

        if (state == State.RUNNING && jumpPressed) {
            dino.jump();
            if (assets.jumpSound != null) {
                assets.jumpSound.play();
            }
        } else if (state == State.GAME_OVER && jumpPressed) {
            restart();
        }
    }

    private void update(float delta) {
        speed = Math.min(GameConfig.MAX_SPEED, speed + GameConfig.SPEED_GROWTH * delta);
        score += GameConfig.SCORE_PER_SECOND * delta;

        dino.update(delta);
        spawner.update(delta, speed);
        background.update(delta, speed);

        Rectangle dinoBounds = dino.getBounds();
        for (Obstacle obstacle : spawner.getObstacles()) {
            if (obstacle.getBounds().overlaps(dinoBounds)) {
                triggerGameOver();
                break;
            }
        }
    }

    private void triggerGameOver() {
        state = State.GAME_OVER;
        dino.hurt();
        if (assets.hitSound != null) {
            assets.hitSound.play();
        }
        int finalScore = (int) score;
        if (finalScore > highScore) {
            highScore = finalScore;
            preferences.putInteger(GameConfig.HIGH_SCORE_KEY, highScore);
            preferences.flush();
        }
    }

    private void restart() {
        state = State.RUNNING;
        speed = GameConfig.BASE_SPEED;
        score = 0f;
        spawner.reset();
        dino = new Dino(assets);
    }

    private void draw() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        background.render(shapeRenderer);

        if (!dino.hasAssets() || !assets.hasCactusFrames()) {
            renderFallbackShapes();
        }

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        dino.render(batch);
        for (Obstacle obstacle : spawner.getObstacles()) {
            obstacle.render(batch);
        }
        drawUi();
        batch.end();
    }

    private void renderFallbackShapes() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if (!dino.hasAssets()) {
            shapeRenderer.setColor(Color.FOREST);
            shapeRenderer.rect(dino.getX(), dino.getY(), dino.getWidth(), dino.getHeight());
        }
        if (!assets.hasCactusFrames()) {
            shapeRenderer.setColor(Color.OLIVE);
            for (Obstacle obstacle : spawner.getObstacles()) {
                Rectangle bounds = obstacle.getBounds();
                shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
            }
        }
        shapeRenderer.end();
    }

    private void drawUi() {
        glyphLayout.setText(font, "Score " + (int) score);
        font.setColor(Color.DARK_GRAY);
        font.draw(batch, glyphLayout, GameConfig.WORLD_WIDTH - glyphLayout.width - 20f, GameConfig.WORLD_HEIGHT - 20f);

        glyphLayout.setText(font, "Best " + highScore);
        font.draw(batch, glyphLayout, GameConfig.WORLD_WIDTH - glyphLayout.width - 20f, GameConfig.WORLD_HEIGHT - 45f);

        if (state == State.GAME_OVER) {
            String message = "Game Over - tap to restart";
            glyphLayout.setText(font, message);
            font.setColor(Color.FIREBRICK);
            font.draw(batch, glyphLayout,
                (GameConfig.WORLD_WIDTH - glyphLayout.width) / 2f,
                GameConfig.WORLD_HEIGHT / 2f + 60f);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
        assets.dispose();
    }
}