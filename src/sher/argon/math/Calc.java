package sher.argon.math;

/**
 * Contains helpful mathematical functions.
 */
public class Calc {
    /**
     * Constrains value to the given boundaries.
     * @param value input value
     * @param min lower boundary
     * @param max upper boundary
     * @return constrained value
     */
    public static double clamp(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Constrains value to the given boundaries.
     * @param value input value
     * @param min lower boundary
     * @param max upper boundary
     * @return constrained value
     */
    public static int clamp(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Maps the value to new range.
     * The returned value is proportionally mapped from it's initial range to a new one.
     * @param value input value
     * @param from initial range lower boundary
     * @param to initial range upper boundary
     * @param scaleFrom new range lower boundary
     * @param scaleTo new range upper boundary
     * @return mapped value
     */
    public static double map(double value, double from, double to, double scaleFrom, double scaleTo) {
        double delta = (value - from) / (to - from);
        return scaleFrom + (scaleTo - scaleFrom) * delta;
    }

    /**
     * Constrains the value to its initial range and maps the result to a new one.
     * The returned value is proportionally mapped from it's initial range to a new one.
     * @param value input value
     * @param from initial range lower boundary
     * @param to initial range upper boundary
     * @param scaleFrom new range lower boundary
     * @param scaleTo new range upper boundary
     * @return mapped value
     */
    public static double mapClamp(double value, double from, double to, double scaleFrom, double scaleTo) {
        return map(clamp(value, from, to), from, to, scaleFrom, scaleTo);
    }

    /**
     * Linear interpolation of two values.
     * @see <a href="https://en.wikipedia.org/wiki/Linear_interpolation">Linear interpolation on Wikipedia</a>
     * @param value initial value
     * @param target target value
     * @param t linear interpolation constant
     * @return interpolated value
     */
    public static double lerp(double value, double target, double t) {
        return (1 - t) * value + t * target;
    }
}
