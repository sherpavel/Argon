package sher.argon.math;

import sher.argon.variable.DoubleVar;
import sher.argon.variable.IntVar;

/**
 * Vec3 is 3-dimensional vector.
 * All of the methods are immutable, i.e. do not affect the current object.
 */
public class Vec3 {
    /**
     * Vector component
     */
    public double x, y, z;

    /**
     * Creates vector object.
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     */
    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Creates a copy of the vector.
     * If {@code null} is provided - 0 vector is returned.
     * @param v vector to copy
     */
    public Vec3(Vec3 v) {
        this((v == null)?0:v.x, (v == null)?0:v.y, (v == null)?0:v.z);
    }

    /**
     * Creates an empty vector
     */
    public Vec3() {
        this(0,0,0);
    }

    /**
     * Creates a vector from {@link IntVar integer variables}.
     * @see IntVar
     * @param v1 x variable
     * @param v2 y variable
     * @param v3 z variable
     */
    public Vec3(IntVar v1, IntVar v2, IntVar v3) {
        this(v1.value(), v2.value(), v3.value());
    }

    /**
     * Creates a vector from {@link DoubleVar double variables}.
     * @see DoubleVar
     * @param v1 x variable
     * @param v2 y variable
     * @param v3 z variable
     */
    public Vec3(DoubleVar v1, DoubleVar v2, DoubleVar v3) {
        this(v1.value(), v2.value(), v3.value());
    }

    /**
     * Sets vector's x, y and z components
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     */
    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Copies the coordinates of the vector
     * @param v vector to copy
     */
    public void set(Vec3 v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    /**
     * Adds corresponding values to the vector components
     * @param dx dX value
     * @param dy dY value
     * @return updated vector
     */
    public Vec3 add(double dx, double dy, double dz) {
        return new Vec3(x + dx, y + dy, z + dz);
    }

    /**
     * Adds vector
     * @param v vector to add
     * @return updated vector
     */
    public Vec3 add(Vec3 v) {
        return new Vec3(x + v.x, y + v.y, z + v.z);
    }

    /**
     * Subtracts corresponding values from the vector components
     * @param dx dX value
     * @param dy dY value
     * @return updated vector
     */
    public Vec3 sub(double dx, double dy, double dz) {
        return new Vec3(x - dx, y - dy, z - dz);
    }

    /**
     * Subtracts vector
     * @param v vector to subtract
     * @return updated vector
     */
    public Vec3 sub(Vec3 v) {
        return new Vec3(x - v.x, y - v.y, z - v.z);
    }

    /**
     * Multiplies the vector components
     * @param a multiplier
     * @return updated vector
     */
    public Vec3 mlp(double a) {
        return new Vec3(x * a, y * a, z * a);
    }

    /**
     * Divides the vector components
     * @param a divider
     * @return updated vector
     */
    public Vec3 div(double a) {
        return new Vec3(x / a, y / a, z / a);
    }

    /**
     * Scalar multiplication of two vectors' components.
     * @param v multiplier vector
     * @return updated vector
     */
    public Vec3 scalar_mlp(Vec3 v) {
        return new Vec3(x * v.x, y * v.y, z * v.z);
    }

    /**
     * Scalar division of two vectors' components.
     * @param v divider vector
     * @return updated vector
     */
    public Vec3 scalar_div(Vec3 v) {
        return new Vec3(x / v.x, y / v.y, z / v.z);
    }

    /**
     * Returns vector's length
     * @return vector's length
     */
    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Returns a unit vector.
     * @return unit vector
     */
    public Vec3 norm() {
        return div(length());
    }

    /**
     * Returns dot product of two vectors.
     * The order of computation is: {@code current dot v}
     * @param v vector
     * @return dot product
     */
    public double dot(Vec3 v) {
        return x * v.x + y * v.y + z * v.z;
    }

    /**
     * Returns cross product of two vectors.
     * The order of computation is: {@code current cross v}
     * @param v vector
     * @return cross product vector
     * @see Vec3
     */
    public Vec3 cross(Vec3 v) {
        return new Vec3(
                y*v.z - z*v.y,
                z*v.x - x*v.z,
                x*v.y - y*v.x);
    }

    /**
     * Linear interpolation of two vectors.
     * @see <a href="https://en.wikipedia.org/wiki/Linear_interpolation">Linear interpolation on Wikipedia</a>
     * @param v target vector
     * @param t linear interpolation constant
     * @return interpolated vector
     */
    public Vec3 lerp(Vec3 v, double t) {
        return add(v.sub(this).mlp(t));
    }

    /**
     * Returns a copy of the current vector
     * @return vector copy
     */
    public Vec3 copy() {
        return new Vec3(x, y, z);
    }

    /**
     * Returns {@link Vec2 2-dimensional vector} with current x and y components
     * @return 2-dimensional vector
     * @see Vec2
     */
    public Vec2 toVec2() {
        return new Vec2(x, y);
    }

    /**
     * Compares the two vectors with precision {@code delta}
     * @param v vector to compare against
     * @param delta precision
     * @return boolean value
     */
    public boolean equals(Vec3 v, double delta) {
        return Math.abs(v.x - x) <= delta &&
                Math.abs(v.y - y) <= delta &&
                Math.abs(v.z - z) <= delta;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ")";
    }
}
