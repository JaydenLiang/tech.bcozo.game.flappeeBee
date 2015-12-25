package tech.bcozo.game.tools.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureDisposer {

    public TextureDisposer() {
    }

    public static void clearTextureRegion(TextureRegion[][] regions) {
        for (int i = 0; i < regions.length; i++) {
            for (int j = 0; j < regions[i].length; j++) {
                regions[i][j] = null;
            }
            regions[i] = null;
        }
    }
}
