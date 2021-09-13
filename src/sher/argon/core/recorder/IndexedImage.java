package sher.argon.core.recorder;

import java.awt.image.BufferedImage;

class IndexedImage {
    BufferedImage image;
    int index;

    IndexedImage(BufferedImage image, int index) {
        this.image = image;
        this.index = index;
    }
}
