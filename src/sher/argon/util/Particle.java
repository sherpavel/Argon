package sher.argon.util;

import sher.argon.Layer;
import sher.argon.math.Vec2;
import sher.argon.math.Vec4;

/**
 * Particle class that contains position and velocity {@link Vec2 vectors} and checks for several types of boundaries.
 */
public class Particle {
    /**
     * Particle's position
     */
    public Vec2 pos;

    /**
     * Particle's velocity
     */
    public Vec2 vel;

    /**
     * Creates particles with position.
     * @param x coordinate
     * @param y coordinate
     */
    public Particle(double x, double y) {
        pos = new Vec2(x, y);
        vel = new Vec2();
    }

    /**
     * Creates particles with position.
     * @param pos position
     */
    public Particle(Vec2 pos) {
        this.pos = pos;
        vel = new Vec2();
    }

    /**
     * Creates particles with position 0.
     */
    public Particle() {
        this(0, 0);
    }

    /**
     * Adds velocity to the position.
     */
    public void update() {
        pos = pos.add(vel);
    }

    /**
     * Adds velocity to the position.
     * The particle's velocity will be reflected upon collision.
     */
    public void updateBoxBounds(Vec4 box) {
        update();
        if (pos.x < box.x) {
            pos.x = box.x;
            vel.x = -1*vel.x;
        } else if (pos.x > box.x+box.z) {
            pos.x = box.x+box.z;
            vel.x = -1*vel.x;
        }
        if (pos.y < box.y) {
            pos.y = box.y;
            vel.y = -1*vel.y;
        } else if (pos.y > box.y+box.w) {
            pos.y = box.y+box.w;
            vel.y = -1*vel.y;
        }
    }

    /**
     * Adds velocity to the position.
     * The particle's position will be reflected to the opposite side of the boundary upon collision.
     */
    public void updateTorusBounds(Vec4 box) {
        update();
        if (pos.x < box.x)
            pos.x = box.x + box.z;
        else if (pos.x > box.x + box.z)
            pos.x = box.x;
        if (pos.y < box.y)
            pos.y = box.y + box.w;
        else if (pos.y > box.y + box.w)
            pos.y = box.y;
    }

    /**
     * Adds velocity to the position.
     * The particle's velocity will be reflected upon collision.
     */
    public void updateCircleBounds(Vec2 center, double r) {
        update();
        if (pos.sub(center).length() > r) {
            Vec2 normal = center.sub(pos).norm();
            vel = vel.sub(normal.mlp(vel.dot(normal)).mlp(2)).norm().mlp(vel.length());
        }
    }

    /**
     * Draws particle as circle.
     * @param layer layer to draw on
     * @param r circle radius
     * @see Layer
     */
    public void draw(Layer layer, int r) {
        layer.g.fillOval((int) pos.x - r, (int) pos.y - r, 2*r, 2*r);
    }
}
