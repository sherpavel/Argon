package sher.argon.util;

import sher.argon.Layer;
import sher.argon.math.Vec2;

/**
 * Flat triangle class with helper methods.
 * @see Delaunay
 */
public class Triangle {
    /**
     * Triangle vertex
     */
    public Vec2 p1, p2, p3;

    /**
     * Triangle edge
     */
    public Edge a, b, c;

    /**
     * Creates a triangle from 3 vertices
     * @param p1 vertex
     * @param p2 vertex
     * @param p3 vertex
     */
    public Triangle(Vec2 p1, Vec2 p2, Vec2 p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        a = new Edge(p1, p2);
        b = new Edge(p2, p3);
        c = new Edge(p1, p3);
    }

    /**
     * Checks if the point is inside the triangle.
     * @param v point to test
     * @return boolean
     */
    public boolean contains(Vec2 v) {
        double d1, d2, d3;
        boolean has_neg, has_pos;

        d1 = sign(v, p1, p2);
        d2 = sign(v, p2, p3);
        d3 = sign(v, p3, p1);

        has_neg = (d1 < 0) || (d2 < 0) || (d3 < 0);
        has_pos = (d1 > 0) || (d2 > 0) || (d3 > 0);

        return !(has_neg && has_pos);
    }
    private double sign(Vec2 p1, Vec2 p2, Vec2 p3) {
        return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
    }

    /**
     * Checks if the point is inside the triangle's circumcirle
     * @param point point to test
     * @return boolean
     * @see <a href="https://en.wikipedia.org/wiki/Circumscribed_circle">Circumscribed circle on Wikipedia</a>
     */
    public boolean insideCircumcircle(Vec2 point) {
        Vec2 circumcircleCenter = circumcircle();
        return point.sub(circumcircleCenter).length() < p1.sub(circumcircleCenter).length();
    }
    private Vec2 circumcircle() {
        double x1 = p1.x;
        double y1 = p1.y;
        double x2 = p2.x;
        double y2 = p2.y;
        double x3 = p3.x;
        double y3 = p3.y;
        double x12 = x1*x1;
        double y12 = y1*y1;
        double x22 = x2*x2;
        double y22 = y2*y2;
        double x32 = x3*x3;
        double y32 = y3*y3;
        double x = (x12+y12)*(y2-y3) + (x22+y22)*(y3-y1) + (x32+y32)*(y1-y2);
        double y = (x12+y12)*(x3-x2) + (x22+y22)*(x1-x3) + (x32+y32)*(x2-x1);
        double d = 2*(x1*(y2-y3) - y1*(x2-x3) + x2*y3 - x3*y2);
        return new Vec2(x/d, y/d);
    }

    /**
     * Calculates the area of the triangle
     * @return triangle's area
     */
    public double getArea() {
        return Math.abs(p1.x*(p2.y-p3.y)+p2.x*(p3.y-p1.y)+p3.x*(p1.y-p2.y)) / 2;
    }

    /**
     * Calculates the midpoint (center point) of the triangle
     * @return triangle's midpoint
     */
    public Vec2 getMidPoint() {
        return new Vec2(
                p1.x + p2.x + p3.x,
                p1.y + p2.y + p3.y).div(3);
    }

    /**
     * Draws triangle's outline.
     * @param layer layer to draw on
     * @see Layer
     */
    public void drawOutline(Layer layer) {
        layer.g.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
        layer.g.drawLine((int) p1.x, (int) p1.y, (int) p3.x, (int) p3.y);
        layer.g.drawLine((int) p2.x, (int) p2.y, (int) p3.x, (int) p3.y);
//        layer.g.drawPolygon(new int[]{
//                (int) p1.x, (int) p2.x, (int) p3.x
//        }, new int[]{
//                (int) p1.y, (int) p2.y, (int) p3.y
//        }, 3);
    }

    /**
     * Draws triangle.
     * @param layer layer to draw on
     * @see Layer
     */
    public void drawPolygon(Layer layer) {
        layer.g.fillPolygon(new int[]{
                (int) p1.x, (int) p2.x, (int) p3.x
        }, new int[]{
                (int) p1.y, (int) p2.y, (int) p3.y
        }, 3);
    }
}
