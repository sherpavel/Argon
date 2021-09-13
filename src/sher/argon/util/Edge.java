package sher.argon.util;

import sher.argon.math.Vec2;

/**
 * Edge is a collection of two {@link Vec2 2d vector} points.
 */
public class Edge {
    public Vec2 a, b;

    /**
     * Creates an edge
     * @see Vec2
     * @param a point
     * @param b point
     */
    public Edge(Vec2 a, Vec2 b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public int hashCode() {
        return a.hashCode() * b.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return this.hashCode() == o.hashCode();
    }
}
