package sher.argon.core.renderer;

import sher.argon.Layer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.concurrent.CopyOnWriteArrayList;

public class Renderer {
    int width, height;
    BufferedImage renderImage;
    Graphics2D renderGraphics;
    int[] rendererRaster;

    CopyOnWriteArrayList<Layer> layers;

    public Renderer(int width, int height) {
        this.width = width;
        this.height = height;

        renderImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        renderGraphics = renderImage.createGraphics();
        renderGraphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        renderGraphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
//        renderGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
        renderGraphics.setBackground(new Color(0, 0, 0, 0));

        rendererRaster = ((DataBufferInt) renderImage.getRaster().getDataBuffer()).getData();

        layers = new CopyOnWriteArrayList<>();
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    public void addLayer(Layer layer) {
        layers.add(layer);
    }
    public void removeLayer(Layer layer) {
        layers.remove(layer);
    }
    public int getLayersCount() {
        return layers.size();
    }

    public void combineLayers() {
        for (int i = 0; i < rendererRaster.length; i++)
            rendererRaster[i] = 0;
        for (Layer layer : layers) {
            if (layer.render())
                renderGraphics.drawImage(layer.image, 0, 0, null);
        }

    }
    public BufferedImage getRenderImage() {
        return renderImage;
    }
}
