/**
 * 
 */
package tech.bcozo.game.flappeebee;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

/**
 * <p>
 * Javadoc description
 * </p>
 * 
 * @ClassName: Flappee
 * @author Jayden Liang
 * @version 1.0
 * @date Dec 16, 2015 12:01:59 AM
 */
public class Flappee {
    private static final float COLLISION_RADIUS = 24f;
    private static final float DIVE_ACCELERATION = 0.30F;
    private static final float FLAP_ACCELERATION = 5F;
    private final Circle collisionCircle;
    private float x;
    private float y;
    private float speedY;

    /**
     * <p>
     * This is the constructor of Flappee
     * </p>
     */
    public Flappee() {
        x = 0;
        y = 0;
        speedY = 0;
        collisionCircle = new Circle(x, y, COLLISION_RADIUS);
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

    public void update() {
        speedY -= DIVE_ACCELERATION;
        setPosition(x, y + speedY);
    }

    public void flap() {
        speedY = FLAP_ACCELERATION;
        setPosition(x, y + speedY);
    }
}
