package sher.argon.math;

import sher.argon.variable.DoubleVar;

/**
 * Vec4 is primarily used for storing 4-component color information.
 */
public class Vec4 {
    public double x, y, z, w;

    public Vec4(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    public Vec4() {
        this(0, 0, 0, 0);
    }
    public Vec4(DoubleVar v1, DoubleVar v2, DoubleVar v3, DoubleVar v4) {
        this(v1.value(), v2.value(), v3.value(), v4.value());
    }

    // Getters
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getZ() {
        return z;
    }
    public double getW() {
        return w;
    }

    // Setters
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public void setZ(double z) {
        this.z = z;
    }
    public void setW(double w) {
        this.w = w;
    }
    public void set(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    public void set(Vec4 v) {
        x = v.x;
        y = v.y;
        z = v.z;
        w = v.w;
    }

    // Add
    public Vec4 add(double dx, double dy, double dz, double dw) {
        return new Vec4(x + dx, y + dy, z + dz, w + dw);
    }
    public Vec4 add(Vec4 v) {
        return new Vec4(x + v.x, y + v.y, z + v.z, w + v.w);
    }
    // Subtract
    public Vec4 sub(double dx, double dy, double dz, double dw) {
        return new Vec4(x - dx, y - dy, z - dz, w - dw);
    }
    public Vec4 sub(Vec4 v) {
        return new Vec4(x - v.x, y - v.y, z - v.z, w - v.w);
    }
    // Multiply and divide
    public Vec4 mlp(double a) {
        return new Vec4(x * a, y * a, z * a, w * a);
    }
    public Vec4 div(double a) {
        return new Vec4(x / a, y / a, z / a, w / a);
    }
    public Vec4 scalar_mlp(Vec4 v) {
        return new Vec4(x * v.x, y * v.y, z * v.z, w * v.w);
    }
    public Vec4 scalar_div(Vec4 v) {
        return new Vec4(x / v.x, y / v.y, z / v.z, w / v.w);
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public Vec4 norm() {
        return div(length());
    }

    public double dot(Vec4 v) {
        return x * v.x + y * v.y + z * v.z + w * v.w;
    }

    public Vec4 lerp(Vec4 v, double t) {
        return add(v.sub(this).mlp(t));
    }
    public static Vec4 lerp(Vec4 v1, Vec4 v2, double t) {
        return v1.add(v2.sub(v1).mlp(t));
    }

    public Vec4 clone() {
        return new Vec4(x, y, z, w);
    }

    public boolean equals(Vec4 v, double delta) {
        return Math.abs(v.x - x) <= delta &&
                Math.abs(v.y - y) <= delta &&
                Math.abs(v.z - z) <= delta &&
                Math.abs(v.w - w) <= delta;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + "," + w + ")";
    }
}

