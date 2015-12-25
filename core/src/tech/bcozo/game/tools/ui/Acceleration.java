/**
 * 
 */
package tech.bcozo.game.tools.ui;

/**
 * <p>
 * Javadoc description
 * </p>
 * 
 * @ClassName: Acceleration
 * @author Jayden Liang
 * @version 1.0
 * @date Dec 19, 2015 11:36:57 PM
 */
public class Acceleration {
    private boolean accelComplete;
    private boolean inconstant;
    private float toSpeed;
    private float speed;
    private float acceleration;
    private float accelDeduction;

    /**
     * <p>
     * This is the constructor of Acceleration
     * </p>
     */
    public Acceleration() {
        accelComplete = true;
        inconstant = false;
        toSpeed = 0;
        speed = 0;
        acceleration = 0;
        accelDeduction = 0;
    }

    public static Acceleration constantAcceleration(float fromSpeed,
            float toSpeed, float acceleration) {
        Acceleration accel = new Acceleration();
        accel.inconstant = false;
        accel.toSpeed = toSpeed;
        accel.speed = fromSpeed;
        accel.acceleration = acceleration;
        // make sure acceleration is effective
        if (acceleration > 0 && fromSpeed < toSpeed
                || acceleration < 0 && fromSpeed > toSpeed) {
            accel.accelComplete = false;
        } else {
            accel.accelComplete = true;
        }
        accel.accelDeduction = 0;
        return accel;
    }

    public static Acceleration inconstantAcceleration(float fromSpeed,
            float toSpeed, float acceleration, float deduction) {
        Acceleration accel = new Acceleration();
        accel.inconstant = true;
        accel.inconstant = false;
        accel.toSpeed = toSpeed;
        accel.speed = fromSpeed;
        accel.acceleration = acceleration;
        accel.acceleration = acceleration;
        // make sure acceleration is effective
        if (acceleration > 0 && fromSpeed < toSpeed) {
            accel.accelComplete = false;
            // make sure acceleration narrowing to 0;
            accel.accelDeduction = deduction > 0 ? -deduction : deduction;
        } else if (acceleration < 0 && fromSpeed > toSpeed) {
            accel.accelComplete = false;
            // make sure acceleration narrowing to 0;
            accel.accelDeduction = deduction > 0 ? deduction : -deduction;
        } else {
            accel.accelComplete = true;
            accel.accelDeduction = 0;
        }
        return accel;
    }

    public boolean accelerationComplete() {
        return accelComplete;
    }

    public float getSpeed() {
        return speed;
    }

    public void update() {
        if (!accelComplete) {
            speed += acceleration;
            if (!inconstant) {
                if (acceleration > 0 && speed >= toSpeed) {
                    accelComplete = true;
                } else if (acceleration < 0 && speed <= toSpeed) {
                    accelComplete = true;
                }
            } else {
                acceleration += accelDeduction;
                if (acceleration == 0
                        || Math.abs(acceleration) < Math.abs(accelDeduction)) {
                    accelComplete = true;
                }
            }
        }
    }

}
