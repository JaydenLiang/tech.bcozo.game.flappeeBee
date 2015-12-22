/**
 * 
 */
package tech.bcozo.game.flappeebee;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import tech.bcozo.game.tools.ui.TextureDisposer;

/**
 * <p>
 * Javadoc description
 * </p>
 * 
 * @ClassName: Bee
 * @author Jayden Liang
 * @version 1.0
 * @date Dec 16, 2015 12:01:59 AM
 */
public class Bee extends Actor {
    private static final float COLLISION_RADIUS = 24f;
    private static final float DIVE_ACCELERATION = 0.30F;
    private static final float FLAP_ACCELERATION = 5F;
    private static final int TILE_WIDTH = 60;
    private static final int TILE_HEIGHT = 60;
    private final Circle collisionCircle;
    private float x;
    private float y;
    private float speedY;

    private Animation animation;
    private float animationTimer;

    private TextureRegion[][] flappeeTextures;

    /**
     * <p>
     * This is the constructor of Bee
     * </p>
     */
    public Bee(Texture texture) {
        x = 0;
        y = 0;
        speedY = 0;
        animationTimer = 0;
        collisionCircle = new Circle(x, y, COLLISION_RADIUS);
        flappeeTextures = new TextureRegion(texture).split(TILE_WIDTH,
                TILE_HEIGHT);
        animation = new Animation(GameConfig.FRAME_DURATION,
                flappeeTextures[0]);
        animation.setPlayMode(Animation.PlayMode.LOOP);
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    public float getRadius() {
        return COLLISION_RADIUS;
    }

    @Override
    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(collisionCircle.x, collisionCircle.y,
                collisionCircle.radius);
    }

    @Override
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateCollisionCircle();
    }

    private void updateCollisionCircle() {
        collisionCircle.setX(x);
        collisionCircle.setY(y);
    }

    public void flap() {
        speedY = FLAP_ACCELERATION;
        setPosition(x, y + speedY);
    }

    public Circle getCollisionCircle() {
        return collisionCircle;
    }

    public void die() {
        speedY = 0;
    }

    @Override
    public void act(float delta) {
        animationTimer += delta;
        speedY -= DIVE_ACCELERATION;
        setPosition(x, y + speedY);
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion flappeeTexture = animation.getKeyFrame(animationTimer);
        batch.draw(flappeeTexture,
                collisionCircle.x - flappeeTexture.getRegionWidth() / 2,
                collisionCircle.y - flappeeTexture.getRegionHeight() / 2);
        super.draw(batch, parentAlpha);
    }

    @Override
    public void clear() {
        // unlink from its parent
        this.setParent(null);
        TextureDisposer.clearTextureRegion(flappeeTextures);
        flappeeTextures = null;
        animation = null;
        super.clear();
    }
}
