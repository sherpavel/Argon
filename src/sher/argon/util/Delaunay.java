package sher.argon.util;

import sher.argon.math.Vec2;

import java.util.*;

/**
 * Delaunay triangulation algorithm.
 * @see <a href="https://en.wikipedia.org/wiki/Delaunay_triangulation">Delaunay rtiangulation on Wikipedia</a>
 */
public class Delaunay {
    /**
     * Generates the delaunay triangulation from set of {@link Vec2 vector} point.
     * @param vertices set of points
     * @return {@link Triangle triangles} array
     * @see Vec2
     * @see Triangle
     */
    public static ArrayList<Triangle> generate(ArrayList<Vec2> vertices) {
        ArrayList<Triangle> triangulation = new ArrayList<>();

        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
        for (Vec2 v : vertices) {
            if (v.x < minX) minX = (int) v.x;
            if (v.x > maxX) maxX = (int) v.x;
            if (v.y < minY) minY = (int) v.y;
            if (v.y > maxY) maxY = (int) v.y;
        }
        minX--; maxX++; minY--; maxY++;

        // Super triangle
        Vec2 a = new Vec2(minX, 3*maxY);
        Vec2 b = new Vec2(3*maxX, minY);
        Vec2 c = new Vec2(-3*maxX, -3*maxY);

        triangulation.add(new Triangle(a, b, c));
        compute(vertices, triangulation);
        triangulation.removeIf(triangle ->
                triangle.p1 == a || triangle.p1 == b || triangle.p1 == c
                        || triangle.p2 == a || triangle.p2 == b || triangle.p2 == c
                        || triangle.p3 == a || triangle.p3 == b || triangle.p3 == c);

        return triangulation;
    }

    /**
     * Generates the delaunay triangulation from set of {@link Vec2 vector} point.
     * This method ensures that the resulting triangulation is always convex.
     * @param vertices set of points
     * @return {@link Triangle triangles} array
     * @see Vec2
     * @see Triangle
     */
    public static ArrayList<Triangle> genConvex(ArrayList<Vec2> vertices) {
        ArrayList<Triangle> triangulation = new ArrayList<>();

        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
        for (Vec2 v : vertices) {
            if (v.x < minX) minX = (int) v.x;
            if (v.x > maxX) maxX = (int) v.x;
            if (v.y < minY) minY = (int) v.y;
            if (v.y > maxY) maxY = (int) v.y;
        }
        minX--; maxX++; minY--; maxY++;

        // Super triangle
        Vec2 a = new Vec2(minX, 3*maxY);
        Vec2 b = new Vec2(3*maxX, minY);
        Vec2 c = new Vec2(-3*maxX, -3*maxY);

        triangulation.add(new Triangle(a, b, c));
        Vec2[] convexHull = ConvexHull.generate(vertices);
        compute(new ArrayList<>(Arrays.asList(convexHull)), triangulation);
        triangulation.removeIf(triangle ->
                triangle.p1 == a || triangle.p1 == b || triangle.p1 == c
                        || triangle.p2 == a || triangle.p2 == b || triangle.p2 == c
                        || triangle.p3 == a || triangle.p3 == b || triangle.p3 == c);
        compute(vertices, triangulation);

        return triangulation;
    }

    private static void compute(ArrayList<Vec2> vertices, ArrayList<Triangle> triangulation) {
        for (Vec2 vertex : vertices) {
            HashSet<Edge> edges = new HashSet<>();

            for (int i = 0; i < triangulation.size(); i++) {
                Triangle triangle = triangulation.get(i);
                if (triangle.insideCircumcircle(vertex)) {
                    if (!edges.add(triangle.a)) edges.remove(triangle.a);
                    if (!edges.add(triangle.b)) edges.remove(triangle.b);
                    if (!edges.add(triangle.c)) edges.remove(triangle.c);
                    triangulation.remove(i);
                    i--;
                }
            }

            for (Edge edge : edges)
                triangulation.add(new Triangle(edge.a, edge.b, vertex));
        }
    }

//    private void computeParallel(ArrayList<Vec2> vertices, int minX, int maxX, Layer layer) {
//        int threads = 2;
//        ArrayList<Vec2>[] vertexSection = new ArrayList[threads];
//        ArrayList<Triangle>[] triSection = new ArrayList[threads];
//        Vec2[][] convexHulls = new Vec2[threads][];
//
//        for (int i = 0; i < threads; i++) {
//            vertexSection[i] = new ArrayList<>();
//            triSection[i] = new ArrayList<>();
//            triSection[i].add(new Triangle(a, b, c));
//        }
//
//        for (Vec2 vertex : vertices) {
//            int index = (int) Math.floor((vertex.x()-minX)*threads / (maxX-minX));
//            vertexSection[index].add(vertex);
//        }
//
//        new Parallel(s -> {
//            convexHulls[s] = ConvexHull.generate(vertexSection[s]);
////            for (Vec2 p : convexHulls[s])
////                vertexSection[s].remove(p);
////            compute(new ArrayList<>(Arrays.asList(convexHulls[s])), triSection[s]);
//            compute(vertexSection[s], triSection[s]);
//            triSection[s].removeIf(triangle ->
//                    triangle.p1 == a || triangle.p1 == b || triangle.p1 == c
//                            || triangle.p2 == a || triangle.p2 == b || triangle.p2 == c
//                            || triangle.p3 == a || triangle.p3 == b || triangle.p3 == c);
//        }, threads).start(threads);
//
//        //=== Draw ===//
//        for (int s = 0; s < threads; s++) {
//            for (Triangle triangle : triSection[s]) {
//                double xc = (triangle.p1.x() + triangle.p2.x() + triangle.p3.x()) / 3;
//                double yc = (triangle.p1.y() + triangle.p2.y() + triangle.p3.y()) / 3;
//                layer.g.setColor(Gradient.blend(
//                        Gradient.blend(
//                                new Color(38, 255, 223),
//                                new Color(242, 106, 27),
//                                xc / maxX),
//                        Gradient.blend(
//                                new Color(1, 28, 38),
//                                new Color(225, 44, 16),
//                                xc / maxX),
//                        yc / maxX
//                ));
//                triangle.drawPolygon(layer);
//            }
//        }
//        //=== Draw ===//
//
//        /** MERGE **/
//        layer.g.setStroke(new BasicStroke(4));
//
//        for (int s = 0; s < threads-1; s++) {
//            ArrayList<Vec2> leftHull = new ArrayList<>(Arrays.asList(convexHulls[s]));
//            ArrayList<Vec2> rightHull = new ArrayList<>(Arrays.asList(convexHulls[s+1]));
//            ArrayList<Vec2> combined = new ArrayList<>();
//            combined.addAll(leftHull);
//            combined.addAll(rightHull);
//            Vec2[] LRHull = ConvexHull.generate(combined);
//
//            //=== Draw ===//
//            layer.g.setColor(Color.CYAN);
//            for (int p = 0; p < LRHull.length-1; p++) {
//                Vec2 p1 = LRHull[p], p2 = LRHull[p+1];
//                layer.g.drawLine((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y());
//            }
//            Vec2 p1 = LRHull[0], p2 = LRHull[LRHull.length-1];
//            layer.g.drawLine((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y());
//            //=== Draw ===//
//
//            Edge topLREdge = new Edge(null, null);
//            Edge btmLREdge = new Edge(null, null);
//            for (int i = 0; i < LRHull.length-1; i++) {
//                if (leftHull.contains(LRHull[i]) && rightHull.contains(LRHull[i+1])) {
//                    topLREdge.a = LRHull[i];
//                    topLREdge.b = LRHull[i+1];
//                }
//                if (rightHull.contains(LRHull[i]) && leftHull.contains(LRHull[i+1])) {
//                    btmLREdge.b = LRHull[i];
//                    btmLREdge.a = LRHull[i+1];
//                }
//            }
//            if (btmLREdge.a == null || btmLREdge.b == null) {
//                btmLREdge.a = LRHull[0];
//                btmLREdge.b = LRHull[LRHull.length-1];
//            }
//
//            // LR iteration
//            Edge LREdge = topLREdge;
//
//            // Right side
//            ArrayList<Triangle> candTris = findCandidateTris(LREdge.b, triSection[s+1]);
//            HashSet<Vec2> hashSetPoints = new HashSet<>();
//            for (Triangle tris : candTris)
//                hashSetPoints.addAll(findCandidatePoints(LREdge.b, tris));
//            ArrayList<Vec2> candPoints = new ArrayList<>(hashSetPoints);
//            candPoints.sort(Comparator.comparingDouble(v -> v.sub(LREdge.b).angleTo(LREdge.b.sub(LREdge.a))));
//
//            Vec2 rightCandidate = null;
//            for (int i = 0; i < candPoints.size()-1; i++) {
//                if (candPoints.get(i).sub(LREdge.b).angleTo(LREdge.b.sub(LREdge.a)) > Math.PI)
//                    break;
//                if (new Triangle(LREdge.a, LREdge.b, candPoints.get(i)).contains(candPoints.get(i+1))) {
//                    triSection[s+1].remove(findTriangle(
//                            LREdge.b,
//                            candPoints.get(i),
//                            candPoints.get(i+1),
//                            triSection[s+1]));
//                } else {
//                    rightCandidate = candPoints.get(i);
//                    break;
//                }
//            }

            // Left side
//            candTris = findCandidateTris(LREdge.a, triSection[s]);
//            hashSetPoints = new HashSet<>();
//            for (Triangle tris : candTris)
//                hashSetPoints.addAll(findCandidatePoints(LREdge.a, tris));
//            candPoints = new ArrayList<>(hashSetPoints);
//            candPoints.sort(Comparator.comparingDouble(v -> v.sub(LREdge.a).angleTo(LREdge.a.sub(LREdge.b))));
//
//            Vec2 leftCandidate = null;
//            for (int i = 0; i < candPoints.size()-1; i++) {
//                if (candPoints.get(i).sub(LREdge.a).angleTo(LREdge.a.sub(LREdge.b)) > Math.PI)
//                    break;
//                if (new Triangle(LREdge.a, LREdge.b, candPoints.get(i)).contains(candPoints.get(i+1))) {
//                    triSection[s].remove(findTriangle(
//                            LREdge.a,
//                            candPoints.get(i),
//                            candPoints.get(i+1),
//                            triSection[s]));
//                } else {
//                    leftCandidate = candPoints.get(i);
//                    break;
//                }
//            }

//            while (!(LREdge.a == topLREdge.a && LREdge.b == topLREdge.b)) {
//                // Find right-side vertices
//                ArrayList<Triangle> candTris = findCandidateTris(LREdge.b, triSection[s+1]);
//                HashSet<Vec2> hashSetPoints = new HashSet<>();
//                for (Triangle tris : candTris)
//                    hashSetPoints.addAll(findCandidatePoints(LREdge.b, tris));
//                ArrayList<Vec2> candPoints = new ArrayList<>(hashSetPoints);
//
//
//                candPoints.sort(Comparator.comparingDouble(v -> v.sub(LREdge.b).angleTo(LREdge.a.sub(LREdge.b))));
//            }

            //=== Draw ===//
//            layer.g.setColor(Color.MAGENTA);
////            layer.g.fillOval((int) leftCandidate.x() - 10, (int) leftCandidate.y() - 10, 20, 20);
//            for (int i = 0; i < candPoints.size(); i++) {
//                System.out.println(candPoints.get(i) + " > "
//                        + Math.toDegrees(
//                                candPoints.get(i).sub(LREdge.b).angleTo(LREdge.a.sub(LREdge.b))
//                ));
//                int r = 12 - 10*i/candPoints.size();
//                Vec2 p = candPoints.get(i);
//                layer.g.fillOval((int) p.x() - r, (int) p.y() - r, 2*r, 2*r);
//            }
//
//            layer.g.setColor(Color.GREEN);
////            layer.g.fillOval((int) rightCandidate.x() - 10, (int) rightCandidate.y() - 10, 20, 20);
//
//            layer.g.setColor(Color.GREEN);
//            p1 = topLREdge.a; p2 = topLREdge.b;
//            layer.g.drawLine((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y());
//            p1 = btmLREdge.a; p2 = btmLREdge.b;
//            layer.g.drawLine((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y());
            //=== Draw ===//
//        }

//        /** Combine **/
//        for (ArrayList<Triangle> secTri : triSection)
//            triangulation.addAll(secTri);
//    }
//    private int section(Vec2 p, int minX, int maxX, int sections) {
//        return (int) Math.floor((p.x()-minX)*sections / (maxX-minX));
//    }
//    private ArrayList<Triangle> findCandidateTris(Vec2 vertex, ArrayList<Triangle> triangulation) {
//        ArrayList<Triangle> candidates = new ArrayList<>();
//        for (Triangle triangle : triangulation) {
//            if (triangle.p1 == vertex || triangle.p2 == vertex || triangle.p3 == vertex)
//                candidates.add(triangle);
//        }
//        return candidates;
//    }
//    private ArrayList<Vec2> findCandidatePoints(Vec2 vertex, Triangle triangle) {
//        ArrayList<Vec2> candidates = new ArrayList<>();
//        if (triangle.p1 == vertex) {
//            candidates.add(triangle.p2);
//            candidates.add(triangle.p3);
//        } else if (triangle.p2 == vertex) {
//            candidates.add(triangle.p1);
//            candidates.add(triangle.p3);
//        } else if (triangle.p3 == vertex) {
//            candidates.add(triangle.p1);
//            candidates.add(triangle.p2);
//        }
//        return candidates;
//    }
//    private Triangle findTriangle(Vec2 p1, Vec2 p2, Vec2 p3, ArrayList<Triangle> triangulation) {
//        for (Triangle tri : triangulation) {
//            if ((tri.p1 == p1 && tri.p2 == p2 && tri.p3 == p3)
//                    || (tri.p1 == p1 && tri.p2 == p3 && tri.p3 == p2)
//                    || (tri.p1 == p2 && tri.p2 == p1 && tri.p3 == p3)
//                    || (tri.p1 == p2 && tri.p2 == p3 && tri.p3 == p1)
//                    || (tri.p1 == p3 && tri.p2 == p2 && tri.p3 == p1)
//                    || (tri.p1 == p3 && tri.p2 == p1 && tri.p3 == p2))
//                return tri;
//        }
//        return null;
//    }
}
