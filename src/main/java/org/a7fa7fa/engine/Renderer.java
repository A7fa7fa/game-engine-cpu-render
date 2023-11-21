package org.a7fa7fa.engine;

import org.a7fa7fa.engine.gfx.Font;
import org.a7fa7fa.engine.gfx.Image;
import org.a7fa7fa.engine.gfx.ImageTile;

public class Renderer {


    private final int pixelHeight, pixelWidth;
    private final int[] pixels;

    private Font font = Font.STANDARD;


    public Renderer(GameContainer gameContainer) {
        pixelWidth = gameContainer.getWidth();
        pixelHeight = gameContainer.getHeight();
        pixels = gameContainer.getWindow().getImagePixelData();
    }

    public void setPixel(int x, int y, int value) {
        if( (x < 0 || x >= pixelWidth || y < 0 || y >= pixelHeight) || ((value >> 24) & 0xff) == 0) { // bit shift right by 24 bit AND with 255 return alpha value
//        if( (x < 0 || x >= pixelWidth || y < 0 || y >= pixelHeight) || value == Color.DONT_RENDER_COLOR.getHexValue() ) {
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

    public void drawImageTile(ImageTile imageTile, int offX, int offY, int tileX, int tileY) {
        // don't render if whole image is offscreen
        if (offX < -imageTile.getTileWidth()) return;
        if (offY < -imageTile.getTileHeight()) return;
        if (offX >= pixelWidth) return;
        if (offY >= pixelHeight) return;

        int newX = 0;
        int newY = 0;
        int newWidth = imageTile.getTileWidth();
        int newHeight = imageTile.getTileHeight();


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
                this.setPixel(x + offX, y + offY , imageTile.getPixelValue(x + (tileX * imageTile.getTileWidth()), y + (tileY * imageTile.getTileHeight())));
            }
        }
    }

    public void drawText(String text, int offX, int offY, int color) {
        this.drawText(text, offX, offY, color, 1);
    }

    public void drawText(String text, int offX, int offY, int color, int size) {
        text = text.toUpperCase();

        int offset = 0;

        for (int i = 0; i < text.length(); i++) {
            int unicode = text.codePointAt(i) - font.getFIRST_UNICODE_CHAR();

            for (int y = 0; y < font.getFontImage().getHeight() * size; y++) {
                for (int x = 0; x < font.getWidths()[unicode] * size; x++) {
                    if (font.getFontImage().getPixelValue((x/size) + font.getOffsets()[unicode], (y/size)) == Color.WHITE.getHexValue()) {
                        setPixel(x + offX + offset, y + offY, color);
                    }
                }
            }

            offset += (font.getWidths()[unicode] * size);
        }
    }

    public void drawRect(int offX, int offY, int width, int height, int color) {

        // TODO clipping and out of screen

        for (int y = 0; y <= height; y++) {
            setPixel(offX, y + offY, color);
            setPixel(offX + width, y + offY, color);
        }

        for (int x = 0; x <= width; x++) {
            setPixel(offX + x, offY, color);
            setPixel(offX + x, offY + height, color);
        }
    }

    public void drawRect(int offX, int offY, int width, int height, int color, boolean fill) {

        if (!fill) {
            this.drawRect(offX, offY, width, height, color);
            return;
        }
        // TODO cliping and out of screen

        for (int y = 0; y <= height; y++) {
            for (int x = 0; x <= width; x++) {
                setPixel(offX +x, offY + y, color);
            }
        }

    }


    public void clear() {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = 0;
        }
    }
}
