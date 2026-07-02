package com.firstime.libgdx.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.firstime.libgdx.util.GameConfig;

import java.util.ArrayList;
import java.util.List;

public class GameAssets implements Disposable {

    public final List<TextureRegion> dinoRunFrames = new ArrayList<>();
    public final List<TextureRegion> dinoJumpFrames = new ArrayList<>();
    public final List<TextureRegion> dinoHurtFrames = new ArrayList<>();
    public final List<TextureRegion> cactusVariants = new ArrayList<>();

    public Sound jumpSound;
    public Sound hitSound;
    public Music music;

    private final List<Texture> ownedTextures = new ArrayList<>();

    public GameAssets() {
        loadStrip("dino/run.png", dinoRunFrames);
        loadStrip("dino/jump.png", dinoJumpFrames);
        loadStrip("dino/hurt.png", dinoHurtFrames);
        loadGrid("obstacles/cactus.png", GameConfig.CACTUS_SHEET_COLUMNS, GameConfig.CACTUS_SHEET_ROWS, cactusVariants);

        jumpSound = loadSoundIfPresent("sfx/jump.wav");
        hitSound = loadSoundIfPresent("sfx/hit.wav");
        music = loadMusicIfPresent("music/theme.ogg");
    }

    private void loadStrip(String path, List<TextureRegion> target) {
        FileHandle handle = Gdx.files.internal(path);
        if (!handle.exists()) {
            return;
        }
        Texture texture = new Texture(handle);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        ownedTextures.add(texture);

        int frameSize = texture.getHeight();
        int columns = Math.max(1, texture.getWidth() / frameSize);
        TextureRegion[][] split = TextureRegion.split(texture, frameSize, frameSize);
        for (int i = 0; i < columns; i++) {
            target.add(split[0][i]);
        }
    }

    private void loadGrid(String path, int columns, int rows, List<TextureRegion> target) {
        FileHandle handle = Gdx.files.internal(path);
        if (!handle.exists()) {
            return;
        }
        Texture texture = new Texture(handle);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        ownedTextures.add(texture);

        int frameWidth = texture.getWidth() / columns;
        int frameHeight = texture.getHeight() / rows;
        TextureRegion[][] split = TextureRegion.split(texture, frameWidth, frameHeight);
        for (TextureRegion[] row : split) {
            for (TextureRegion region : row) {
                target.add(region);
            }
        }
    }

    private Sound loadSoundIfPresent(String path) {
        FileHandle handle = Gdx.files.internal(path);
        return handle.exists() ? Gdx.audio.newSound(handle) : null;
    }

    private Music loadMusicIfPresent(String path) {
        FileHandle handle = Gdx.files.internal(path);
        if (!handle.exists()) {
            return null;
        }
        Music result = Gdx.audio.newMusic(handle);
        result.setLooping(true);
        result.setVolume(0.4f);
        return result;
    }

    public boolean hasDinoFrames() {
        return !dinoRunFrames.isEmpty();
    }

    public boolean hasCactusFrames() {
        return !cactusVariants.isEmpty();
    }

    @Override
    public void dispose() {
        for (Texture texture : ownedTextures) {
            texture.dispose();
        }
        if (jumpSound != null) {
            jumpSound.dispose();
        }
        if (hitSound != null) {
            hitSound.dispose();
        }
        if (music != null) {
            music.dispose();
        }
    }
}