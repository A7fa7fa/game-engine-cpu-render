package org.a7fa7fa.engine.gfx;

import org.j3d.texture.procedural.PerlinNoiseGenerator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Image {

    private int height, width;
    private int[] pixels;
    private boolean alpha = false;
    private int lightBlock = Light.NONE;

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

    public Image(int[] pixels, int width, int height) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
    }

    public Image(int width, int height) {
        this.pixels = new int[width * height];
        this.width = width;
        this.height = height;
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

    public boolean isAlpha() {
        return alpha;
    }

    public void setAlpha(boolean alpha) {
        this.alpha = alpha;
    }

    public int getLightBlock() {
        return lightBlock;
    }

    public void setLightBlock(int lightBlock) {
        this.lightBlock = lightBlock;
    }
}
