package sher.argon.util;

import sher.argon.math.Vec2;
import sher.argon.math.Vec3;

/**
 * Quasirandom number generator with equal distribution.
 * @see <a href="http://extremelearning.com.au/unreasonable-effectiveness-of-quasirandom-sequences/">The quasirandom sequences</a>
 */
public class PseudoRandom {
    private static final double PHI1 = 1.6180339887498948482;
    private static final double PHI2 = 1.32471795724474602596;
    private static final double PHI3 = 1.22074408460575947536;
    private static final double PHI1a1 = 1.0 / PHI1;
    private static final double PHI2a1 = 1.0 / PHI2;
    private static final double PHI2a2 = 1.0 / (PHI2*PHI2);
    private static final double PHI3a1 = 1.0 / PHI3;
    private static final double PHI3a2 = 1.0 / (PHI3*PHI3);
    private static final double PHI3a3 = 1.0 / (PHI3*PHI3*PHI3);

    /**
     * Calculates quasirandom number
     * @param n seed value
     * @return quasirandom number
     */
    public static double value(double n) {
        return (0.5 + PHI1a1*n) % 1;
    }

    /**
     * Calculates quasirandom {@link Vec2 2d vector}.
     * @param n seed value
     * @return quasirandom vector
     * @see Vec2
     */
    public static Vec2 vec2(double n) {
        return new Vec2(
                (0.5 + PHI2a1*n) % 1,
                (0.5 + PHI2a2*n) % 1);
    }

    /**
     * Calculates quasirandom {@link Vec3 3d vector}.
     * @param n seed value
     * @return quasirandom vector
     * @see Vec3
     */
    public static Vec3 vec3(double n) {
        return new Vec3(
                (0.5 + PHI3a1*n) % 1,
                (0.5 + PHI3a2*n) % 1,
                (0.5 + PHI3a3*n) % 1);
    }
}
