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
        // don't render if whole image is offscreen
        if (offX < -image.getWidth()) return;
        if (offY < -image.getHeight()) return;
        if (offX >= pixelWidth) return;
        if (offY >= pixelHeight) return;

        int newX = 0;
        int newY = 0;
        int newWidth = image.getWidth();
        int newHeight = image.getHeight();


        // if image clips outside of screen. image width is set to the with that is still inside of screen
        if (offX < 0) {
            newX -= offX;
        }

        if (offY < 0) {
            newY -= offY;
        }

        if (newWidth + offX > this.pixelWidth) {
            newWidth -= (newWidth + offX - pixelWidth);
        }

        if (newHeight + offY > this.pixelHeight) {
            newHeight -= (newHeight + offY - this.pixelHeight);
        }

        for (int y = newY; y < newHeight; y++ ) {
            for (int x = newX; x < newWidth; x++ ) {
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
