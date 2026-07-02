package com.firstime.libgdx.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.firstime.libgdx.assets.GameAssets;
import com.firstime.libgdx.util.GameConfig;

import java.util.ArrayList;
import java.util.List;

public class ObstacleSpawner {

    private final List<Obstacle> obstacles = new ArrayList<>();
    private final List<TextureRegion> variants;
    private float timeUntilNextSpawn;

    public ObstacleSpawner(GameAssets assets) {
        variants = assets.cactusVariants;
        scheduleNextSpawn();
    }

    public void update(float delta, float speed) {
        timeUntilNextSpawn -= delta;
        if (timeUntilNextSpawn <= 0f) {
            spawn();
            scheduleNextSpawn();
        }
        for (Obstacle obstacle : obstacles) {
            obstacle.update(delta, speed);
        }
        obstacles.removeIf(Obstacle::isOffscreen);
    }

    private void spawn() {
        float height = MathUtils.random(45f, 85f);
        float width = height * 0.6f;
        TextureRegion region = variants.isEmpty() ? null : variants.get(MathUtils.random(variants.size() - 1));
        obstacles.add(new Obstacle(GameConfig.WORLD_WIDTH + width, GameConfig.GROUND_Y, width, height, region));
    }

    private void scheduleNextSpawn() {
        timeUntilNextSpawn = MathUtils.random(GameConfig.MIN_OBSTACLE_GAP, GameConfig.MAX_OBSTACLE_GAP);
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public void reset() {
        obstacles.clear();
        scheduleNextSpawn();
    }
}
