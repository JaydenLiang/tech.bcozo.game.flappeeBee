package tech.bcozo.game.flappeebee.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import tech.bcozo.game.flappeebee.FlappeeBee;
import tech.bcozo.game.flappeebee.GameConfig;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = (int) GameConfig.WORLD_WIDTH;
        config.height = (int) GameConfig.WORLD_HEIGHT;
        new LwjglApplication(new FlappeeBee(), config);
    }
}
