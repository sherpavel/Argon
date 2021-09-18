package sher.argon;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * Layer provides drawing canvas and methods to interact with it.
 * It is based on Java's {@link BufferedImage} and {@link Graphics2D} for more complex shapes.
 * The image's pixel raster is exposed and can be accessed directly.
 * Note: The layer's alpha component is 0 by default.
 */
public class Layer {
    int width, height;

    /**
     * Layer {@link BufferedImage image}
     */
    public final BufferedImage image;

    /**
     * Layer's image {@link Graphics2D graphics}
     */
    public final Graphics2D g;

    /**
     * Direct image raster access
     */
    public final int[] raster;

    boolean render;

    Layer(int width, int height, boolean render) {
        this.width = width;
        this.height = height;
        this.render = render;

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
//        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
        g.setBackground(new Color(0, 0, 0, 0));

        // Accessing raster directly is by far the fastest way of interacting with image on pixel-by-pixel basis
        raster = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    }

    /**
     * Returns layer width.
     * @return layer width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns layer height.
     * @return layer height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns if layer should be drawn.
     * @return If layer should be drawn
     */
    public boolean render() {
        return render;
    }

    // Pixel access
    /**
     * Returns rgb integer from the raster.
     * The out-of-bound coordinate will return 0.
     * @param x coordinate
     * @param y coordinate
     * @return integer rgb value
     */
    public int getRGB(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return 0;
        return raster[x + width*y];
    }
    /**
     * Returns red color component.
     * The out-of-bound coordinate will return 0.
     * @param x coordinate
     * @param y coordinate
     * @return red component value
     */
    public int getRed(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return 0;
        return (raster[x + width*y] >> 16) & 0xFF;
    }
    /**
     * Returns green color component.
     * The out-of-bound coordinate will return 0.
     * @param x coordinate
     * @param y coordinate
     * @return green component value
     */
    public int getGreen(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return 0;
        return (raster[x + width*y] >> 8) & 0xFF;
    }
    /**
     * Returns blue color component.
     * The out-of-bound coordinate will return 0.
     * @param x coordinate
     * @param y coordinate
     * @return blue component value
     */
    public int getBlue(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return 0;
        return raster[x + width*y] & 0xFF;
    }
    /**
     * Returns alpha color component.
     * The out-of-bound coordinate will return 0.
     * @param x coordinate
     * @param y coordinate
     * @return alpha component value
     */
    public int getAlpha(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return 0;
        return (raster[x + width*y] >> 24) & 0xFF;
    }
    /**
     * Computes the true monochromatic value at given coordinate
     * {@code (0.299*r + 0.587*g + 0.114*b)}.
     * The out-of-bound coordinate will return 0.
     * @param x coordinate
     * @param y coordinate
     * @return monochromatic value
     */
    public int getValue(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return 0;
        int p = raster[x + width*y];
        int r = (p>>16)&0xFF;
        int g = (p>>8)&0xFF;
        int b = p&0xFF;
        return (int) (0.299*r + 0.587*g + 0.114*b);
    }

    /**
     * Returns {@link Color}.
     * The out-of-bound coordinate will return empty color
     * {@code (r=0, g=0, b=0, alpha=0)}
     * @param x coordinate
     * @param y coordinate
     * @return {@link Color color} object
     */
    public Color getColor(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return new Color(0, 0, 0, 0);
        return new Color(raster[x + width*y], true);
    }

    /**
     * Sets rgb integer at given coordinate.
     * Alpha component is required to display color.
     * No error will be thrown for out-of-bound coordinate.
     * @param x coordinate
     * @param y coordinate
     * @param rgb rgb color
     */
    public void setRGB(int x, int y, int rgb) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return;
        raster[x+width*y] = rgb;
    }
    /**
     * Sets red color component at given coordinate.
     * No error will be thrown for out-of-bound coordinate.
     * @param x coordinate
     * @param y coordinate
     * @param r red color component
     */
    public void setRed(int x, int y, int r) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return;
        int i = x+width*y;
        int p = raster[i];
        p &= ~(255 << 16);
        p |= r << 16;
        raster[i] = p;
    }
    /**
     * Sets green color component at given coordinate.
     * No error will be thrown for out-of-bound coordinate.
     * @param x coordinate
     * @param y coordinate
     * @param g green color component
     */
    public void setGreen(int x, int y, int g) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return;
        int i = x+width*y;
        int p = raster[i];
        p &= ~(255 << 8);
        p |= g << 8;
        raster[i] = p;
    }
    /**
     * Sets blue color component at given coordinate.
     * No error will be thrown for out-of-bound coordinate.
     * @param x coordinate
     * @param y coordinate
     * @param b blue color component
     */
    public void setBlue(int x, int y, int b) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return;
        int i = x+width*y;
        int p = raster[i];
        p &= ~(255);
        p |= b;
        raster[i] = p;
    }
    /**
     * Sets alpha component at given coordinate.
     * No error will be thrown for out-of-bound coordinate.
     * @param x coordinate
     * @param y coordinate
     * @param alpha alpha component
     */
    public void setAlpha(int x, int y, int alpha) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return;
        int i = x+width*y;
        int p = raster[i];
        p &= ~(255 << 24);
        p |= alpha << 24;
        raster[i] = p;
    }
    /**
     * Sets monochromatic value as all 3 color components at given coordinate.
     * No error will be thrown for out-of-bound coordinate.
     * @param x coordinate
     * @param y coordinate
     * @param value monochromatic value
     * @param alpha alpha component
     */
    public void setValue(int x, int y, int value, int alpha) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return;
        int rgb = 0;
        rgb |= alpha << 24;
        rgb |= value << 16;
        rgb |= value << 8;
        rgb |= value;
        raster[x+width*y] = rgb;
    }
    /**
     * Sets monochromatic value as all 3 color components at given coordinate.
     * The alpha component remains untouched.
     * No error will be thrown for out-of-bound coordinate.
     * @param x coordinate
     * @param y coordinate
     * @param value monochromatic value
     */
    public void setValue(int x, int y, int value) {
        setValue(x, y, value, getAlpha(x, y));
    }

    /**
     * Sets color at given coordinate.
     * No error will be thrown for out-of-bound coordinate.
     * @param x coordinate
     * @param y coordinate
     * @param color {@link Color}
     * @see Color
     */
    public void setColor(int x, int y, Color color) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return;
        int rgb = 0;
        rgb |= color.getAlpha() << 24;
        rgb |= color.getRed() << 16;
        rgb |= color.getGreen() << 8;
        rgb |= color.getBlue();
        raster[x+width*y] = rgb;
    }

    // Misc
    /**
     * Clears the layer including the alpha channel.
     */
    public void clear() {
        // Just to be clear (pun intended): g.clearRect() function is for some reason slower then iterating through the raster, thus this method is preferred.
        for (int i = 0; i < raster.length; i++)
            raster[i] = 0;
    }

    /**
     * Fills the layer with the given rgb color.
     * @param rgb integer rgb
     */
    public void fill(int rgb) {
        // Why not g.fillRect? Same reason as clear() method above.
        for (int i = 0; i < raster.length; i++)
            raster[i] = rgb;
    }

    /**
     * Fills the layer with the given color.
     * @param color {@link Color color} object
     */
    public void fill(Color color) {
        // Why not g.fillRect? Same reason as clear() method above.
        int rgb = color.getRGB();
        for (int i = 0; i < raster.length; i++)
            raster[i] = rgb;
    }
}
