/**
 * 
 */
package tech.bcozo.game.flappeebee;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * <p>
 * Javadoc description
 * </p>
 * 
 * @ClassName: Flower
 * @author Jayden Liang
 * @version 1.0
 * @date Dec 18, 2015 11:13:07 PM
 */
public class Flower extends Actor {
    private static final float COLLISION_RECTANGLE_WIDTH = 13f;
    private static final float COLLISION_RECTANGLE_HEIGHT = 447f;
    private static final float COLLISION_CIRCLE_RADIUS = 35f;
    public static final float WIDTH = COLLISION_CIRCLE_RADIUS * 2;
    private static final float HEIGHT_OFFSET = -400f;
    private Circle floorCollisionCircle;
    private Rectangle floorCollisionRectangle;
    private Circle ceilingCollisionCircle;
    private Rectangle ceilingCollisionRectangle;
    private Texture floorTexture;
    private Texture ceilingTexture;

    private float x;
    private float y;
    private float textureOffsetX;
    private float textureOffsetY;
    private float moveSpeed;

    /**
     * <p>
     * This is the constructor of Flower
     * </p>
     */
    public Flower(float crossingZoneHeight, Texture floorTexture,
            Texture ceilingTexture) {
        float zoneHeight = crossingZoneHeight;
        x = 0;
        y = 0;
        moveSpeed = 0;
        this.floorTexture = floorTexture;
        this.ceilingTexture = ceilingTexture;
        this.y = MathUtils.random(HEIGHT_OFFSET);
        this.floorCollisionRectangle = new Rectangle(x, y,
                COLLISION_RECTANGLE_WIDTH, COLLISION_RECTANGLE_HEIGHT);
        this.floorCollisionCircle = new Circle(
                x + floorCollisionRectangle.width / 2,
                y + floorCollisionRectangle.height, COLLISION_CIRCLE_RADIUS);

        this.ceilingCollisionRectangle = new Rectangle(x,
                floorCollisionCircle.y + zoneHeight, COLLISION_RECTANGLE_WIDTH,
                COLLISION_RECTANGLE_HEIGHT);
        this.ceilingCollisionCircle = new Circle(
                x + ceilingCollisionRectangle.width / 2,
                ceilingCollisionRectangle.y, COLLISION_CIRCLE_RADIUS);
        textureOffsetX = floorTexture.getWidth() / 2 - 6;
        textureOffsetY = COLLISION_CIRCLE_RADIUS;
    }

    @Override
    public void clear() {
        floorTexture.dispose();
        ceilingTexture.dispose();
        floorCollisionCircle = null;
        floorCollisionRectangle = null;
        ceilingCollisionCircle = null;
        ceilingCollisionRectangle = null;
        floorTexture = null;
        ceilingTexture = null;
        super.clear();
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    public void setPosition(float x) {
        this.x = x;
        updateCollisionCircle();
        updateCollisionRectangle();
    }

    private void updateCollisionCircle() {
        floorCollisionCircle.setX(x + floorCollisionRectangle.width / 2);
        ceilingCollisionCircle.setX(x + ceilingCollisionRectangle.width / 2);
    }

    private void updateCollisionRectangle() {
        floorCollisionRectangle.setX(x);
        ceilingCollisionRectangle.setX(x);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(floorTexture,
                floorCollisionRectangle.getX() - textureOffsetX,
                floorCollisionRectangle.getY() + textureOffsetY);
        batch.draw(ceilingTexture,
                ceilingCollisionRectangle.getX() - textureOffsetX,
                ceilingCollisionRectangle.getY() - textureOffsetY);
        super.draw(batch, parentAlpha);
    }

    @Override
    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(floorCollisionCircle.x, floorCollisionCircle.y,
                floorCollisionCircle.radius);
        shapeRenderer.rect(floorCollisionRectangle.x, floorCollisionRectangle.y,
                floorCollisionRectangle.width, floorCollisionRectangle.height);
        shapeRenderer.circle(ceilingCollisionCircle.x, ceilingCollisionCircle.y,
                ceilingCollisionCircle.radius);
        shapeRenderer.rect(ceilingCollisionRectangle.x,
                ceilingCollisionRectangle.y, ceilingCollisionRectangle.width,
                ceilingCollisionRectangle.height);
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    @Override
    public void act(float delta) {
        setPosition(x - moveSpeed * delta);
        super.act(delta);
    }

    public boolean isFlappeeColliding(Bee flappee) {
        Circle flappeeCollisionCircle = flappee.getCollisionCircle();
        return Intersector.overlaps(flappeeCollisionCircle,
                ceilingCollisionCircle)
                || Intersector.overlaps(flappeeCollisionCircle,
                        floorCollisionCircle)
                || Intersector.overlaps(flappeeCollisionCircle,
                        ceilingCollisionRectangle)
                || Intersector.overlaps(flappeeCollisionCircle,
                        floorCollisionRectangle);
    }

}
