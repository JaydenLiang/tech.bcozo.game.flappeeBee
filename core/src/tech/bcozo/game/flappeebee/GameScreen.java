/**
 * 
 */
package tech.bcozo.game.flappeebee;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import tech.bcozo.game.tools.storage.GameStorage;
import tech.bcozo.game.tools.ui.DigitDisplay;

/**
 * <p>
 * Javadoc description
 * </p>
 * 
 * @ClassName: GameScreen
 * @author Jayden Liang
 * @version 1.0
 * @date Dec 15, 2015 11:58:42 PM
 */
public class GameScreen extends ScreenAdapter {
    private static final float GAP_BETWEEN_FLOWERS = 200f;
    private static final float GAME_SPEED = 100f;
    private static final float CROSSING_ZONE_HEIGHT = 225f;
    private Viewport viewport;
    private Camera camera;
    private Stage stage;
    private Bee flappee;
    private ArrayList<Flower> flowers = new ArrayList<Flower>();
    private int score;
    private int highScore;
    private GAME_STATE state;
    private Texture background;
    private Texture flowerBottom;
    private Texture flowerTop;
    private Texture beeTexture;
    private Texture gameOverTexture;
    private DigitDisplay scoreText;
    private DigitDisplay highScoreText;

    private Image backgroundImage;
    private Image gameOverImage;

    private Group backgroundGroup;
    private Group flowerGroup;
    private Group scoreGroup;
    private Group gameOverGroup;

    /**
     * <p>
     * This is the constructor of GameScreen
     * </p>
     */
    public GameScreen() {
        viewport = new FitViewport(GameConfig.WORLD_WIDTH,
                GameConfig.WORLD_HEIGHT);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        flowers = new ArrayList<Flower>();
        score = 0;
        highScore = 0;
        state = GAME_STATE.PLAYING;
        backgroundGroup = new Group();
        flowerGroup = new Group();
        scoreGroup = new Group();
        gameOverGroup = new Group();
        background = new Texture(Gdx.files.internal("background02.png"));
        backgroundImage = new Image(background);
        stage.addActor(backgroundGroup);
        stage.addActor(flowerGroup);
        stage.addActor(scoreGroup);

        backgroundGroup.addActor(backgroundImage);

        flowerBottom = new Texture(Gdx.files.internal("flower01.png"));
        flowerTop = new Texture(Gdx.files.internal("flower02.png"));
        beeTexture = new Texture(Gdx.files.internal("bee.fly.serial02.png"));
        gameOverTexture = new Texture(Gdx.files.internal("textGameOver.png"));
        gameOverImage = new Image(gameOverTexture);

        flappee = new Bee(beeTexture);

        stage.addActor(flappee);
        scoreText = new DigitDisplay("digits02.png", "digits02.txt");
        highScoreText = new DigitDisplay("digits02.png", "digits02.txt");
        scoreGroup.addActor(scoreText);
        scoreGroup.addActor(highScoreText);

        stage.addActor(gameOverGroup);
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        // clear every clearable variables
        flappee.clear();
        flowers.clear();
        backgroundGroup.clear();
        flowerGroup.clear();
        scoreGroup.clear();
        gameOverGroup.clear();
        backgroundImage.clear();
        gameOverImage.clear();
        // dispose every disposable variables
        stage.dispose();
        background.dispose();
        flowerBottom.dispose();
        flowerTop.dispose();
        gameOverTexture.dispose();
        beeTexture.dispose();
        scoreText.dispose();
        highScoreText.dispose();
        // unlink all reference
        stage = null;
        camera = null;
        viewport = null;
        flappee = null;
        flowers = null;
        backgroundGroup = null;
        flowerGroup = null;
        scoreGroup = null;
        gameOverGroup = null;
        background = null;
        backgroundImage = null;
        gameOverImage = null;
        flowerBottom = null;
        flowerTop = null;
        gameOverTexture = null;
        beeTexture = null;
        scoreText = null;
        highScoreText = null;
        super.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        flappee.setPosition(GameConfig.WORLD_WIDTH / 4,
                GameConfig.WORLD_HEIGHT / 2);
        scoreText.setPosition(5,
                GameConfig.WORLD_HEIGHT - scoreText.getHeight() - 5);
        scoreText.setDisplayFormat(0, 10, 0, 0);
        scoreText.setNumber(0);

        String highScoreString = GameStorage.getInstanceOf("FlappeeBee")
                .getValue("highScore");
        if (highScoreString != "")
            highScore = Integer.parseInt(highScoreString);

        highScoreText.setDisplayFormat(10, 10, 0, 0);
        highScoreText.setNumber(highScore);
        highScoreText.setPosition(
                (GameConfig.WORLD_WIDTH - highScoreText.getWidth()) / 2,
                GameConfig.WORLD_HEIGHT - scoreText.getHeight() - 5);
        gameOverGroup.addActor(gameOverImage);
        gameOverImage.setPosition(
                GameConfig.WORLD_WIDTH / 2 - gameOverImage.getWidth() / 2,
                GameConfig.WORLD_HEIGHT / 2);
        gameOverGroup.setVisible(false);
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b,
                Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void render(float delta) {
        switch (state) {
        case PLAYING:
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                flappee.flap();
            }
            stage.act(delta);
            checkIfNewFlowerIsNeeded();
            removeFlowersIfPassed();
            if (checkForCollision()) {
                gameOver();
            }
            updateScore(MathUtils.roundPositive(delta * 100));
            break;
        case GAME_OVER:
            stage.act(delta);
            if (flappee.getY() <= flappee.getHeight()) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    restart();
                }
            }
            break;
        case RESTARTING:
            restarting();
            break;
        default:
            break;
        }
        blockFlappeeLeavingTheWorld();
        clearScreen();
        stage.draw();
    }

    private void blockFlappeeLeavingTheWorld() {
        flappee.setPosition(flappee.getX(),
                MathUtils.clamp(flappee.getY(), 0 + flappee.getRadius(),
                        GameConfig.WORLD_HEIGHT - flappee.getRadius()));
    }

    private void createNewFlower() {
        Flower newFlower = new Flower(CROSSING_ZONE_HEIGHT, flowerBottom,
                flowerTop);
        newFlower.setPosition(GameConfig.WORLD_WIDTH + Flower.WIDTH);
        newFlower.setMoveSpeed(GAME_SPEED);
        flowers.add(newFlower);
        flowerGroup.addActor(newFlower);
    }

    private void updateFlowerMoveSpeed(float moveSpeed) {
        for (Flower flower : flowers) {
            flower.setMoveSpeed(moveSpeed);
        }
    }

    private void checkIfNewFlowerIsNeeded() {
        if (flowers.size() == 0) {
            createNewFlower();
        } else {
            Flower flower = flowers.get(flowers.size() - 1);// get the last
                                                            // flower
            if (flower.getX() < GameConfig.WORLD_WIDTH - GAP_BETWEEN_FLOWERS) {
                createNewFlower();
            }
        }
    }

    private void removeFlowersIfPassed() {
        if (flowers.size() > 0) {
            Flower firstFlower = flowers.get(0);// get the first flower
            if (firstFlower.getX() < 0 - Flower.WIDTH) {
                flowers.remove(0);
            }
        }
    }

    private boolean checkForCollision() {
        for (Flower flower : flowers) {
            if (flower.isFlappeeColliding(flappee)) {
                return true;
            }
        }
        return false;
    }

    private void updateScore(int addScore) {
        score += addScore;
        scoreText.setNumber(score);
        if (score > highScore) {
            highScore = score;
            highScoreText.setNumber(highScore);
        }
    }

    private void gameOver() {
        state = GAME_STATE.GAME_OVER;
        updateFlowerMoveSpeed(0);
        gameOverGroup.setVisible(true);
        flappee.die();
    }

    private void restart() {
        flowers.clear();
        flowerGroup.clearChildren();
        gameOverGroup.setVisible(false);
        score = 0;
        state = GAME_STATE.RESTARTING;
    }

    private void restarting() {
        if (flappee.getY() < GameConfig.WORLD_HEIGHT / 2) {
            flappee.flap();
        } else {
            GameStorage.getInstanceOf("FlappeeBee").setValue("highScore",
                    String.valueOf(highScore));
            show();
            state = GAME_STATE.PLAYING;
        }
    }

    private enum GAME_STATE {
        STOP, PLAYING, PAUSE, GAME_OVER, RESTARTING
    }
}
