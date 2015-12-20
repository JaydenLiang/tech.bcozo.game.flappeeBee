/**
 * 
 */
package tech.bcozo.game.flappeebee;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

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
public class Bee {
    private static final float COLLISION_RADIUS = 24f;
    private static final float DIVE_ACCELERATION = 0.30F;
    private static final float FLAP_ACCELERATION = 5F;
    private static final int TILE_WIDTH = 60;
    private static final int TILE_HEIGHT = 60;
    private static final float FRAME_DURATION = 0.01F;
    private final Circle collisionCircle;
    private float x;
    private float y;
    private float speedY;
    private float textureOffsetX;
    private float textureOffsetY;

    private final Animation animation;
    private float animationTimer;

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
        TextureRegion[][] flappeeTextures = new TextureRegion(texture)
                .split(TILE_WIDTH, TILE_HEIGHT);
        animation = new Animation(FRAME_DURATION, flappeeTextures[0]);
        animation.setPlayMode(Animation.PlayMode.LOOP);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getRadius() {
        return COLLISION_RADIUS;
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(collisionCircle.x, collisionCircle.y,
                collisionCircle.radius);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateCollisionCircle();
    }

    private void updateCollisionCircle() {
        collisionCircle.setX(x);
        collisionCircle.setY(y);
    }

    public void update(float delta) {
        animationTimer += delta;
        speedY -= DIVE_ACCELERATION;
        setPosition(x, y + speedY);
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

    public void draw(SpriteBatch batch) {
        TextureRegion flappeeTexture = animation.getKeyFrame(animationTimer);
        batch.draw(flappeeTexture,
                collisionCircle.x - flappeeTexture.getRegionWidth() / 2,
                collisionCircle.y - flappeeTexture.getRegionHeight() / 2);
    }
}
