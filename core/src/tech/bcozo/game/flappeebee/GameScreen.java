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
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

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
    private static final float WORLD_WIDTH = 480;
    private static final float WORLD_HEIGHT = 640;
    private static final float GAP_BETWEEN_FLOWERS = 200f;
    private static final float GAME_SPEED = 100f;
    private static final float CROSSING_ZONE_HEIGHT = 225f;
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Camera camera;
    private SpriteBatch batch;

    private Bee flappee;
    private ArrayList<Flower> flowers = new ArrayList<Flower>();
    private int score;
    private GAME_STATE state;
    private Texture background;
    private Texture flowerBottom;
    private Texture flowerTop;
    private Texture beeTexture;

    /**
     * <p>
     * This is the constructor of GameScreen
     * </p>
     */
    public GameScreen() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        flowers = new ArrayList<>();
        score = 0;
        state = GAME_STATE.PLAYING;
        background = new Texture(Gdx.files.internal("background01.png"));
        flowerBottom = new Texture(Gdx.files.internal("flower01.png"));
        flowerTop = new Texture(Gdx.files.internal("flower02.png"));
        beeTexture = new Texture(Gdx.files.internal("bee.fly.serial02.png"));
        flappee = new Bee(beeTexture);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        flappee.setPosition(WORLD_WIDTH / 4, WORLD_HEIGHT / 2);
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
            update(delta);
            break;
        case GAME_OVER:
            onFlappeeDie(delta);
            break;
        default:
            break;
        }
        clearScreen();
        draw();
    }

    private void draw() {
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        batch.draw(background, 0, 0);
        drawFlowers();
        flappee.draw(batch);
        batch.end();
    }

    private void drawDebug() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        flappee.drawDebug(shapeRenderer);
        for (Flower flower : flowers) {
            flower.drawDebug(shapeRenderer);
        }
        shapeRenderer.end();
    }

    private void update(float delta) {
        updateFlappee(delta);
        updateFlowers(delta);
        if (checkForCollision()) {
            gameOver();
        }
    }

    private void updateFlowers(float delta) {
        float flyDistance = GAME_SPEED * delta;
        for (Flower flower : flowers) {
            flower.update(flyDistance);
        }
        checkIfNewFlowerIsNeeded();
        removeFlowersIfPassed();
    }

    private void updateFlappee(float delta) {
        flappee.update(delta);
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            flappee.flap();
        blockFlappeeLeavingTheWorld();
    }

    private void blockFlappeeLeavingTheWorld() {
        flappee.setPosition(flappee.getX(), MathUtils.clamp(flappee.getY(),
                0 + flappee.getRadius(), WORLD_HEIGHT - flappee.getRadius()));
    }

    private void createNewFlower() {
        Flower newFlower = new Flower(CROSSING_ZONE_HEIGHT, flowerBottom,
                flowerTop);
        newFlower.setPosition(WORLD_WIDTH + Flower.WIDTH);
        flowers.add(newFlower);
    }

    private void drawFlowers() {
        for (Flower flower : flowers) {
            flower.draw(batch);
        }
    }

    private void checkIfNewFlowerIsNeeded() {
        if (flowers.size() == 0) {
            createNewFlower();
        } else {
            Flower flower = flowers.get(flowers.size() - 1);// get the last
                                                            // flower
            if (flower.getX() < WORLD_WIDTH - GAP_BETWEEN_FLOWERS) {
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

    private void gameOver() {
        state = GAME_STATE.GAME_OVER;
        flappee.die();
    }

    private void restart() {
        flappee.setPosition(WORLD_WIDTH / 4, WORLD_HEIGHT / 2);
        flowers.clear();
        score = 0;
        state = GAME_STATE.PLAYING;
    }

    private enum GAME_STATE {
        STOP, PLAYING, PAUSE, GAME_OVER
    }

    private void onFlappeeDie(float delta) {
        if (flappee.getY() - flappee.getRadius() > 0) {
            flappee.update(delta);
        }
    }
}
