/**
 * 
 */
package tech.bcozo.game.flappeebee;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import tech.bcozo.game.tools.storage.GameStorage;
import tech.bcozo.game.tools.ui.Acceleration;
import tech.bcozo.game.tools.ui.DigitDisplay;
import tech.bcozo.game.tools.ui.TextureDisposer;

/**
 * <p>
 * Javadoc description
 * </p>
 * 
 * @ClassName: StartScreen
 * @author Jayden Liang
 * @version 1.0
 * @date Dec 19, 2015 8:10:08 PM
 */
public class StartScreen extends ScreenAdapter {
    // disposable variables
    private Stage stage;
    private Texture backgroundTexture;
    private Texture buttonPlayUpTexture;
    private Texture buttonPlayDownTexture;
    private Texture beeTexture;
    private DigitDisplay scoreText;
    // clearable variables
    ImageButton buttonPlay;
    StartScreenButtonListener buttonPlayListener;
    private Bee bee;
    private float renderTimer;
    // parent
    private Game game;

    /**
     * <p>
     * This is the constructor of StartScreen
     * </p>
     */
    public StartScreen(Game game) {
        this.game = game;
        renderTimer = 0;
        stage = new Stage(new FitViewport(GameConfig.WORLD_WIDTH,
                GameConfig.WORLD_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        backgroundTexture = new Texture(Gdx.files.internal("background01.png"));
        stage.addActor(new Image(backgroundTexture));
        buttonPlayUpTexture = new Texture(
                Gdx.files.internal("buttonPlayUp.png"));
        buttonPlayDownTexture = new Texture(
                Gdx.files.internal("buttonPlayDown.png"));
        buttonPlay = new ImageButton(
                new TextureRegionDrawable(
                        new TextureRegion(buttonPlayUpTexture)),
                new TextureRegionDrawable(
                        new TextureRegion(buttonPlayDownTexture)));
        stage.addActor(buttonPlay);
        buttonPlay.setPosition(
                (GameConfig.WORLD_WIDTH - buttonPlay.getWidth()) / 2,
                GameConfig.WORLD_HEIGHT / 2 - 30);
        buttonPlayListener = new StartScreenButtonListener();
        buttonPlay.addListener(buttonPlayListener);
        beeTexture = new Texture(Gdx.files.internal("bee.fly.serial02.png"));
        bee = new Bee(50, beeTexture);
        bee.setPosition(30, buttonPlay.getY() - 60);
        stage.addActor(bee);
        scoreText = new DigitDisplay("digits02.png", "digits02.txt");
        scoreText.setPosition(200, 395);
        String highScore = GameStorage.getInstanceOf("FlappeeBee")
                .getValue("highScore");
        if (highScore.equals("")) {
            scoreText.setNumber(0, 10, 0);
        } else {
            scoreText.setNumber(Integer.parseInt(highScore), 9, 0);
        }
        stage.addActor(scoreText);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        renderTimer += delta;
        if (renderTimer > 1) {
            renderTimer = 0;
        }
        stage.draw();
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        // clear every clearable variables
        buttonPlay.clear();
        bee.clear();
        // dispose every disposable variables
        stage.dispose();
        backgroundTexture.dispose();
        buttonPlayUpTexture.dispose();
        buttonPlayDownTexture.dispose();
        beeTexture.dispose();
        scoreText.dispose();
        // unlink all reference
        stage = null;
        buttonPlay = null;
        bee = null;
        backgroundTexture = null;
        buttonPlayUpTexture = null;
        buttonPlayDownTexture = null;
        beeTexture = null;
        scoreText = null;
        buttonPlayListener = null;
        game = null;
        super.dispose();
    }

    private void startNewGame() {
        game.setScreen(new GameScreen());
        dispose();
    }

    private class Bee extends Actor {
        private static final int TILE_WIDTH = 60;
        private static final int TILE_HEIGHT = 60;
        private static final float FLY_ACCELERATION = 0.25f;
        private static final float FLY_SPEED = 5f;
        private static final float FLY_MAX_TIME = 2f;

        private TextureRegion[][] flappeeTextureRegion;
        private Animation flappeeAnimation;

        private int nextFrame;
        private int totalFrame;
        private int flyDirection;
        private float flySpeed;
        private float flyTime;
        private float flyTimer;
        private boolean textureMirror;

        private Acceleration accel;

        public Bee(float speed, Texture beeTexture) {
            flySpeed = speed;
            flyDirection = 1;
            flyTimer = 0;
            textureMirror = false;
            flyTime = MathUtils.random(FLY_MAX_TIME);
            flappeeTextureRegion = TextureRegion.split(beeTexture, TILE_WIDTH,
                    TILE_HEIGHT);
            totalFrame = flappeeTextureRegion[0].length;
            flappeeAnimation = new Animation(GameConfig.FRAME_DURATION,
                    flappeeTextureRegion[0]);
            flappeeAnimation.setPlayMode(Animation.PlayMode.LOOP);
            accel = Acceleration.constantAcceleration(0, FLY_SPEED,
                    FLY_ACCELERATION);
        }

        @Override
        public void setX(float x) {
            super.setX(x);
        }

        @Override
        public void setY(float y) {
            super.setY(y);
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            float newX = getX();
            float newY = getY();
            // move logic
            if (newX < 0) {
                flyDirection = 1;
                textureMirror = false;
            } else if (newX > GameConfig.WORLD_WIDTH - TILE_WIDTH) {
                flyDirection = -1;
                textureMirror = true;
            }
            // if accelerate done
            if (flyTimer > flyTime) {
                flyTime = 0;
                if (newX < 0 || newX > GameConfig.WORLD_WIDTH - TILE_WIDTH
                        || MathUtils.randomBoolean(0.2f)
                                && accel.accelerationComplete()) {
                    accel = Acceleration.constantAcceleration(0, FLY_SPEED,
                            FLY_ACCELERATION);
                    flyTime = MathUtils.random(FLY_MAX_TIME);
                    flyTimer = 0;
                }
            }
            if (flyTime > 0) {
                flyTimer += delta;
            }
            accel.update();
            if (flyTime > 0) {
                flySpeed = accel.getSpeed();
            } else
                flySpeed = 0;
            newX += flySpeed * flyDirection;
            setPosition(newX, newY);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            super.draw(batch, parentAlpha);
            batch.draw(flappeeAnimation.getKeyFrame(nextFrame),
                    textureMirror ? getX() + TILE_WIDTH : getX(), getY(),
                    textureMirror ? -TILE_WIDTH : TILE_WIDTH, TILE_HEIGHT);
            nextFrame = nextFrame == totalFrame ? 0 : nextFrame + 1;
        }

        @Override
        public void clear() {
            // unlink from its parent
            this.setParent(null);
            TextureDisposer.clearTextureRegion(flappeeTextureRegion);
            flappeeTextureRegion = null;
            flappeeAnimation = null;
            accel = null;
            super.clear();
        }
    }

    private class StartScreenButtonListener extends ActorGestureListener {
        public StartScreenButtonListener() {
        }

        @Override
        public void tap(InputEvent event, float x, float y, int count,
                int button) {
            super.tap(event, x, y, count, button);
            startNewGame();
        }
    }

}
