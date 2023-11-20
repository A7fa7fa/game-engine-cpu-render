package org.a7fa7fa.engine;

import org.a7fa7fa.engine.gfx.Image;

public class Renderer {


    private final int pixelHeight, pixelWidth;
    private final int[] pixels;

    private final int DONT_RENDER_COLOR = 0xffff00ff;

    public Renderer(GameContainer gameContainer) {
        pixelWidth = gameContainer.getWidth();
        pixelHeight = gameContainer.getHeight();

        pixels = gameContainer.getWindow().getImagePixelData();
    }

    public void setPixel(int x, int y, int value) {
        if( (x < 0 || x >= pixelWidth || y < 0 || y >= pixelHeight) || value == DONT_RENDER_COLOR ) {
            return;
        }

        pixels[x + y * pixelWidth] = value;
    }

    public void drawImage(Image image, int offX, int offY) {
        for (int y = 0; y < image.getHeight(); y++ ) {
            for (int x = 0; x < image.getWidth(); x++ ) {
                this.setPixel(x + offX, y + offY , image.getPixelValue(x, y));
            }
        }
    }

    public void clear() {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = 0;
        }
    }
}
