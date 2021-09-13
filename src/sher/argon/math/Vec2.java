package sher.argon.math;

import sher.argon.variable.DoubleVar;
import sher.argon.variable.IntVar;

/**
 * Vec2 is 2-dimensional vector.
 * All of the methods are immutable, i.e. do not affect the current object.
 */
public class Vec2 {
    /**
     * Vector component
     */
    public double x, y;

    /**
     * Creates vector.
     * @param x coordinate
     * @param y coordinate
     */
    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a copy of the vector.
     * If {@code null} is provided - 0 vector is returned.
     * @param v vector to copy
     */
    public Vec2(Vec2 v) {
        this((v == null)?0:v.x, (v == null)?0:v.y);
    }

    /**
     * Creates an empty vector
     */
    public Vec2() {
        this(0,0);
    }

    /**
     * Creates a unit vector from a radian angle.
     * @param angle radian angle
     */
    public Vec2(double angle) {
        this(Math.cos(angle), Math.sin(angle));
    }

    /**
     * Creates a vector from {@link IntVar integer variables}.
     * @see IntVar
     * @param v1 x variable
     * @param v2 y variable
     */
    public Vec2(IntVar v1, IntVar v2) {
        this(v1.value(), v2.value());
    }

    /**
     * Creates a vector from {@link DoubleVar double variables}.
     * @see DoubleVar
     * @param v1 x variable
     * @param v2 y variable
     */
    public Vec2(DoubleVar v1, DoubleVar v2) {
        this(v1.value(), v2.value());
    }

    /**
     * Sets vector's x and y components
     * @param x coordinate
     * @param y coordinate
     */
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Copies the coordinates of the vector
     * @param v vector to copy
     */
    public void set(Vec2 v) {
        x = v.x;
        y = v.y;
    }

    /**
     * Adds corresponding values to the vector components
     * @param dx dX value
     * @param dy dY value
     * @return updated vector
     */
    public Vec2 add(double dx, double dy) {
        return new Vec2(x + dx, y + dy);
    }

    /**
     * Adds vector
     * @param v vector to add
     * @return updated vector
     */
    public Vec2 add(Vec2 v) {
        return new Vec2(x + v.x, y + v.y);
    }

    /**
     * Rotates vector by an angle, preserving it's length
     * @param angle radian angle
     * @return updated vector
     */
    public Vec2 add(double angle) {
        return new Vec2(toAngle() + angle).mlp(length());
    }

    /**
     * Subtracts corresponding values from the vector components
     * @param dx dX value
     * @param dy dY value
     * @return updated vector
     */
    public Vec2 sub(double dx, double dy) {
        return new Vec2(x - dx, y - dy);
    }

    /**
     * Subtracts vector
     * @param v vector to subtract
     * @return updated vector
     */
    public Vec2 sub(Vec2 v) {
        return new Vec2(x - v.x, y - v.y);
    }

    /**
     * Multiplies the vector components
     * @param a multiplier
     * @return updated vector
     */
    public Vec2 mlp(double a) {
        return new Vec2(x * a, y * a);
    }

    /**
     * Divides the vector components
     * @param a divider
     * @return updated vector
     */
    public Vec2 div(double a) {
        return new Vec2(x / a, y / a);
    }

    /**
     * Scalar multiplication of two vectors' components.
     * @param v multiplier vector
     * @return updated vector
     */
    public Vec2 scalar_mlp(Vec2 v) {
        return new Vec2(x * v.x, y * v.y);
    }

    /**
     * Scalar division of two vectors' components.
     * @param v divider vector
     * @return updated vector
     */
    public Vec2 scalar_div(Vec2 v) {
        return new Vec2(x / v.x, y / v.y);
    }

    /**
     * Returns vector's length
     * @return vector's length
     */
    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Returns the radian angle of the vector
     * @return radian angle
     */
    public double toAngle() {
        return Math.atan2(y, x);
    }

    /**
     * Returns a unit vector.
     * @return unit vector
     */
    public Vec2 norm() {
        return div(length());
    }

    /**
     * Returns dot product of two vectors.
     * The order of computation is: {@code current dot v}
     * @param v vector
     * @return dot product
     */
    public double dot(Vec2 v) {
        return x * v.x + y * v.y;
    }

    /**
     * Returns cross product of two vectors.
     * The order of computation is: {@code current cross v}
     * @param v vector
     * @return cross product vector
     * @see Vec3
     */
    public Vec3 cross(Vec2 v) {
        return new Vec3(0, 0, x*v.y - y*v.x);
    }

    /**
     * Linear interpolation of two vectors.
     * @see <a href="https://en.wikipedia.org/wiki/Linear_interpolation">Linear interpolation on Wikipedia</a>
     * @param v target vector
     * @param t linear interpolation constant
     * @return interpolated vector
     */
    public Vec2 lerp(Vec2 v, double t) {
        return add(v.sub(this).mlp(t));
    }

    /**
     * Returns a copy of the current vector
     * @return vector copy
     */
    public Vec2 copy() {
        return new Vec2(x, y);
    }

    /**
     * Returns {@link Vec3 3-dimensional vector} with current x and y components
     * @return 3-dimensional vector
     * @see Vec3
     */
    public Vec3 toVec3() {
        return new Vec3(x, y, 0);
    }

    /**
     * Compares the two vectors with precision {@code delta}
     * @param v vector to compare against
     * @param delta precision
     * @return boolean value
     */
    public boolean equals(Vec2 v, double delta) {
        return Math.abs(v.x - x) <= delta &&
                Math.abs(v.y - y) <= delta;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
