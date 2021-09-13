package sher.argon.util;

import sher.argon.math.Calc;
import sher.argon.math.Vec2;
import sher.argon.math.Vec3;
import sher.argon.parallel.Parallel;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Contains helpful color and graphical functions.
 */
public class Util {
    /**
     * Converts r, g, b and alpha color components into integer rgb.
     * The values' range is {@code [0-255]}
     * @param r red
     * @param g green
     * @param b blue
     * @param alpha alpha
     * @return rgb integer
     */
    public static int rgbToInt(int r, int g, int b, int alpha) {
        r = Calc.clamp(r, 0, 255);
        g = Calc.clamp(g, 0, 255);
        b = Calc.clamp(b, 0, 255);
        alpha = Calc.clamp(alpha, 0, 255);
        int rgb = alpha << 24;
        rgb |= r << 16;
        rgb |= g << 8;
        rgb |= b;
        return rgb;
    }
    /**
     * Converts r, g and b color components into integer rgb.
     * The values' range is {@code [0-255]}.
     * @param r red
     * @param g green
     * @param b blue
     * @return rgb integer
     */
    public static int rgbToInt(int r, int g, int b) {
        return rgbToInt(r, g, b, 255);
    }

    /**
     * Converts 3 or 4 component rgb array into integer rgb. Each value must be in range {@code [0-255]}
     * @param rgb 3 or 4 component rgb array
     * @return rgb integer
     * @throws IllegalArgumentException if array's length is not 3 or 4
     */
    public static int rgbArrayToInt(int[] rgb) {
        if (rgb.length < 3 || rgb.length > 4) throw new IllegalArgumentException("Array of length " + rgb.length + " instead of 3 or 4");
        return (rgb.length == 3) ? rgbToInt(rgb[0], rgb[1], rgb[2]) : rgbToInt(rgb[0], rgb[1], rgb[2], rgb[3]);
    }

    /**
     * Converts rgb integer to rgba 4-component array.
     * @param rgb rgb integer
     * @return rgba array
     */
    public static int[] rgbToArray(int rgb) {
        return new int[]{
                (rgb >> 16) & 0xFF,
                (rgb >> 8) & 0xFF,
                rgb & 0xFF,
                (rgb >> 24) & 0xFF,
        };
    }

    /**
     * Converts {@link Color color} object to {@link Vec3 vector}.
     * Return vector's components' value range is {@code [0-1]}
     * @param color {@link Color color} object
     * @return color {@link Vec3 vector}
     */
    public static Vec3 colorToVec3(Color color) {
        return new Vec3(color.getRed(), color.getGreen(), color.getBlue()).div(255d);
    }

    /**
     * Converts color {@link Vec3 vector} to {@link Color color} object.
     * Vectors' components must be normalized (i.e. [0-1] each)
     * @param v {@link Vec3 vector}
     * @return {@link Color color}
     */
    public static Color vec3ToColor(Vec3 v) {
        return new Color(
                (float) Calc.clamp(v.x, 0, 1),
                (float) Calc.clamp(v.y, 0, 1),
                (float) Calc.clamp(v.z, 0, 1));
    }

    /**
     * Draws a circle of given radius at {@link Vec2 vector} coordinates.
     * @param p position {@link Vec2 vector}
     * @param r radius
     * @param g {@link Graphics2D Graphics2D} object. Provided by {@link sher.argon.Layer Layer}.
     */
    public static void drawPoint(Vec2 p, double r, Graphics2D g) {
        g.fillOval((int) (p.x - r), (int) (p.y - r), (int) (2*r), (int) (2*r));
    }

    /**
     * Draws {@link Vec2 vector} line starting at from center point.
     * @param v {@link Vec2 vector} to draw
     * @param center center point form which the vector is drawn
     * @param g {@link Graphics2D Graphics2D} object. Provided by {@link sher.argon.Layer Layer}.
     */
    public static void drawVecCenter(Vec2 v, Vec2 center, Graphics2D g) {
        g.drawLine((int) (center.x), (int) (center.y), (int) (v.x + center.x), (int) (v.y + center.y));
    }

    /**
     * Draws line between two points.
     * @param p1 position 1
     * @param p2 position 2
     * @param g {@link Graphics2D Graphics2D} object. Provided by {@link sher.argon.Layer Layer}.
     */
    public static void drawVec(Vec2 p1, Vec2 p2, Graphics2D g) {
        g.drawLine((int) (p1.x), (int) (p1.y), (int) (p2.x), (int) (p2.y));
    }

    @Deprecated
    public static BufferedImage rotate90(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage newImage = new BufferedImage(
                height, width,
                BufferedImage.TYPE_INT_ARGB);
        Parallel parallel = new Parallel(x -> {
            for (int y = 0; y < image.getHeight(); y++) {
                newImage.setRGB(y, x, image.getRGB(x, y));
            }
        });
        parallel.start(image.getWidth(), 8);
        return newImage;
    }
    @Deprecated
    public static BufferedImage rotate180(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage newImage = new BufferedImage(
                width, height,
                BufferedImage.TYPE_INT_ARGB);
        Parallel parallel = new Parallel(x -> {
            for (int y = 0; y < image.getHeight(); y++) {
                newImage.setRGB(width-1-x, height-1-y, image.getRGB(x, y));
            }
        });
        parallel.start(width, 8);
        return newImage;
    }
}
