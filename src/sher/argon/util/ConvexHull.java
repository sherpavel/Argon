package sher.argon.util;

import sher.argon.math.Vec2;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Convex hull algorithm.
 * @see <a href="https://en.wikipedia.org/wiki/Convex_hull">Convex hull on Wikipedia</a>
 */
public class ConvexHull {
    /**
     * Generates convex hull from the set of {@link Vec2 vector} points.
     * The output array start from left-most point and going clockwise.
     * @param vertices set of point
     * @return convex hull array
     * @see Vec2
     */
    public static Vec2[] generate(ArrayList<Vec2> vertices) {
        vertices.sort((v1, v2) -> {
            int xComp = Double.compare(v1.x, v2.x);
            return (xComp == 0) ? Double.compare(v1.y, v2.y) : xComp;
        });

        Vec2[] hull = new Vec2[2*vertices.size()];
        int k = 0;

        for (Vec2 vertex : vertices) {
            while (k >= 2 && cross(hull[k - 2], hull[k - 1], vertex) <= 0)
                k--;
            hull[k++] = vertex;
        }
        for (int i = vertices.size() - 2, t = k + 1; i >= 0; i--) {
            while (k >= t && cross(hull[k - 2], hull[k - 1], vertices.get(i)) <= 0)
                k--;
            hull[k++] = vertices.get(i);
        }

        hull = Arrays.copyOfRange(hull, 0, k-1);

        return hull;
    }

    public static double cross(Vec2 O, Vec2 A, Vec2 B) {
        return (A.x - O.x) * (B.y - O.y) - (A.y - O.y) * (B.x - O.x);
    }
}
