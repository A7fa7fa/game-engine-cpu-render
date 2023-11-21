package org.a7fa7fa.engine.gfx;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Image {

    private int height, width;
    private int[] pixels;

    public Image(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(Image.class.getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        width = image.getWidth();
        height = image.getHeight();
        pixels = image.getRGB(0,0, width, height, null, 0, width);

        image.flush();
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int[] getPixels() {
        return pixels;
    }

    public int getPixelValue(int x, int y) {
        try {
            return this.pixels[x + y * this.width];
        } catch (Exception e) {
            System.out.println(x + " - " + y);
            return 0;
        }
    }
}
