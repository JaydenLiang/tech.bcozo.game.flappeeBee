package tech.bcozo.game.flappeebee;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FlappeeBee extends Game {
    SpriteBatch batch;
    Texture img;

    public FlappeeBee() {
    }

    @Override
    public void create() {
        setScreen(new StartScreen(this));
        // setScreen(new GameScreen());
    }
}
