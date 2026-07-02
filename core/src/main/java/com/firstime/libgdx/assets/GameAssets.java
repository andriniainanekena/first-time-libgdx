package com.firstime.libgdx.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

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
        loadSequence("dino/run", "run", dinoRunFrames);
        loadSequence("dino/jump", "jump", dinoJumpFrames);
        loadSequence("dino/hurt", "hurt", dinoHurtFrames);
        loadSequence("obstacles", "cactus", cactusVariants);

        jumpSound = loadSoundIfPresent("sfx/jump.wav");
        hitSound = loadSoundIfPresent("sfx/hit.wav");
        music = loadMusicIfPresent("music/theme.ogg");
    }

    private void loadSequence(String directory, String prefix, List<TextureRegion> target) {
        int index = 0;
        while (true) {
            FileHandle handle = Gdx.files.internal(directory + "/" + prefix + index + ".png");
            if (!handle.exists()) {
                break;
            }
            Texture texture = new Texture(handle);
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            ownedTextures.add(texture);
            target.add(new TextureRegion(texture));
            index++;
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