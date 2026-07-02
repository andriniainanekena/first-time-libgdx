package com.firstime.libgdx;

import com.badlogic.gdx.Game;
import com.firstime.libgdx.screen.GameScreen;

public class Main extends Game {

    @Override
    public void create() {
        setScreen(new GameScreen());
    }
}