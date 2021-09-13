package sher.argon.util;

import sher.argon.math.Calc;

import java.awt.Color;

/**
 * Gradient class is a collection of methods to simplify the work with colors and gradients.
 */
public class Gradient {
    public static final int[][] Argon = {
            {253, 252, 255},
            {201, 32, 176},
            {130, 60, 250},
            {28, 12, 54}
    };
    public static final int[][] Mild = {
            {254, 172, 94},
            {199, 121, 208},
            {75, 192, 200}
    };
    public static final int[][] Shine = {
            {18, 194, 233},
            {196, 113, 237},
            {246, 79, 89}
    };
    public static final int[][] Maroon = {
            {195, 20, 50},
            {36, 11, 54}
    };
    public static final int[][] Timber = {
            {252, 0, 255},
            {0, 219, 222}
    };

    /**
     * Computes the color from the gradient array.
     * <br>
     * The variable {@code p} is the percentage of the gradient in range [0-1] and is independent from the gradient size (0 being first color in the array and 1 - last).
     * <br>
     * Gradient is 2-dimensional array of colors. Each color is an rgb array of size 3. For an example see {@link #Argon}, {@link #Maroon}, etc.
     * @param gradient array of colors
     * @param p gradient percentage
     * @param alpha alpha component value
     * @return rgb integer
     */
    public static int gradient(int[][] gradient, double p, int alpha) {
        if (gradient.length == 1) return Util.rgbArrayToInt(gradient[0]);
        p = Calc.clamp(p, 0, 1);
        double pScaled = p * (gradient.length-1.00001d);
        int c = (int)Math.floor(pScaled);
        p = pScaled - c;
        double r = gradient[c][0] * (1-p) + gradient[c+1][0] * p;
        double g = gradient[c][1] * (1-p) + gradient[c+1][1] * p;
        double b = gradient[c][2] * (1-p) + gradient[c+1][2] * p;
        return Util.rgbToInt((int)r, (int)g, (int)b, Calc.clamp(alpha, 0, 255));
    }

    /**
     * Computes the color from the gradient array.
     * <br>
     * The variable {@code p} is the percentage of the gradient in range [0-1] and is independent from the gradient size (0 being first color in the array and 1 - last).
     * <br>
     * Gradient is 2-dimensional array of colors. Each color is an rgb array of size 3. For an example see {@link #Argon}, {@link #Maroon}, etc.
     * @param gradient array of colors
     * @param p gradient offset
     * @return rgb integer
     */
    public static int gradient(int[][] gradient, double p) {
        return gradient(gradient, p, 255);
    }

    /**
     * Computes the color from two gradient arrays and blends them proportionally to {@code mergeP} value.
     * <br>
     * The variable {@code mergeP} is the proportion of the two gradients in range [0-1].
     * The variable {@code colorP} is the percentage within each gradient in range [0-1] and is independent from the gradients' sizes (0 being first color in the array and 1 - last).
     * Gradient is 2-dimensional array of colors. Each color is an rgb array of size 3. For an example see {@link #Argon}, {@link #Maroon}, etc.
     * @param gradient1 array of colors
     * @param gradient2 array of colors
     * @param mergeP gradient blend
     * @param colorP gradient offset
     * @param alpha alpha component
     * @return rgb integer
     */
    public static int gradientMerge(int[][] gradient1, int[][] gradient2, double mergeP, double colorP, int alpha) {
        return blend(gradient(gradient1, colorP, alpha), gradient(gradient2, colorP, alpha), Calc.clamp(mergeP, 0, 1));
    }

    /**
     * Computes the color from two gradient arrays and blends them proportionally to {@code mergeP} value.
     * <br>
     * The variable {@code mergeP} is the proportion of the two gradients in range [0-1].
     * The variable {@code colorP} is the percentage within each gradient in range [0-1] and is independent from the gradients' sizes (0 being first color in the array and 1 - last).
     * Gradient is 2-dimensional array of colors. Each color is an rgb array of size 3. For an example see {@link #Argon}, {@link #Maroon}, etc.
     * @param gradient1 array of colors
     * @param gradient2 array of colors
     * @param mergeP gradient blend
     * @param colorP gradient offset
     * @return rgb integer
     */
    public static int gradientMerge(int[][] gradient1, int[][] gradient2, double mergeP, double colorP) {
        return gradientMerge(gradient1, gradient2, mergeP, colorP, 255);
    }

    /**
     * Blends two colors proportionally.
     * @param rgb1 first color
     * @param rgb2 second color
     * @param p percentage
     * @return rgb integer
     */
    public static int blend(int rgb1, int rgb2, double p) {
        p = Calc.clamp(p, 0, 1);
        int alpha = (int) (((rgb1>>24)&0xFF) * (1-p) + ((rgb2>>24)&0xFF) * (p));
        int r = (int) (((rgb1>>16)&0xFF) * (1-p) + ((rgb2>>16)&0xFF) * (p));
        int g = (int) (((rgb1>>8)&0xFF) * (1-p) + ((rgb2>>8)&0xFF) * (p));
        int b = (int) ((rgb1&0xFF) * (1-p) + (rgb2&0xFF) * (p));
        int rgb = 0;
        rgb |= (alpha) << 24;
        rgb |= (r) << 16;
        rgb |= (g) << 8;
        rgb |= b;
        return rgb;
    }

    /**
     * Blends two colors proportionally.
     * @param color1 first color
     * @param color2 second color
     * @param p percentage
     * @param alpha alpha component
     * @return {@link Color}
     * @see Color
     */
    public static Color blend(Color color1, Color color2, double p, int alpha) {
        p = Calc.clamp(p, 0, 1);
        return new Color(
                (int) ((1d-p) * color1.getRed() + (p) * color2.getRed()),
                (int) ((1d-p) * color1.getGreen() + (p) * color2.getGreen()),
                (int) ((1d-p) * color1.getBlue() + (p) * color2.getBlue()),
                alpha);
    }

    /**
     * Blends two colors proportionally.
     * @param color1 first color
     * @param color2 second color
     * @param p percentage
     * @return {@link Color}
     * @see Color
     */
    public static Color blend(Color color1, Color color2, double p) {
        return blend(color1, color2, p, 255);
    }

    /**
     * Factors the color's brightness proportionally.
     * @param color color
     * @param p percentage
     * @return {@link Color}
     * @see Color
     */
    public static Color factor(Color color, double p) {
        p = Calc.clamp(p, 0, 1);
        return new Color(
                (int) (color.getRed() * p),
                (int) (color.getGreen() * p),
                (int) (color.getBlue() * p));
    }

    /**
     * Adds two colors.
     * @param c1 color
     * @param c2 color
     * @return {@link Color}
     * @see Color
     */
    public static Color add(Color c1, Color c2) {
        return new Color(
                Math.min(c1.getRed() + c2.getRed(), 255),
                Math.min(c1.getGreen() + c2.getGreen(), 255),
                Math.min(c1.getBlue() + c2.getBlue(), 255));
    }

    /**
     * Adds two colors and scales the resulting color to fit in range [0-255].
     * @param c1 color
     * @param c2 color
     * @return {@link Color}
     * @see Color
     */
    public static Color addScaled(Color c1, Color c2) {
        int r = c1.getRed() + c2.getRed();
        int g = c1.getGreen() + c2.getGreen();
        int b = c1.getBlue() + c2.getBlue();
        int max = Math.max(Math.max(r, g), b);
        return new Color(
                (255 * r) / max,
                (255 * g) / max,
                (255 * b) / max);
    }

    /**
     * Subtracts two colors.
     * @param c1 color
     * @param c2 color
     * @return {@link Color}
     * @see Color
     */
    public static Color sub(Color c1, Color c2) {
        return new Color(
                Math.min(c1.getRed(), c2.getRed()),
                Math.min(c1.getGreen(), c2.getGreen()),
                Math.min(c1.getBlue(), c2.getBlue()));
    }

    @Deprecated
    public static Color illuminate(Color object, Color light) {
        return sub(light, object);
    }
}
