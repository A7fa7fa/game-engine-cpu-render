package org.a7fa7fa.core;

public class Renderer {


    private final int pixelHeight, pixelWidth;
    private final int[] pixels;

    public Renderer(GameContainer gameContainer) {
        pixelWidth = gameContainer.getWidth();
        pixelHeight = gameContainer.getHeight();

        pixels = gameContainer.getWindow().getImagePixelData();
    }

    public void clear() {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = 0;
        }
    }
}
