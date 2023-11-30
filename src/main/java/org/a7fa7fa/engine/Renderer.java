package org.a7fa7fa.engine;

import org.a7fa7fa.engine.gfx.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Renderer {

    private Font font = Font.STANDARD;
    private ArrayList<ImageRequest> imageRequests = new ArrayList<ImageRequest>();
    private ArrayList<LightRequest> lightRequests = new ArrayList<LightRequest>();
    private boolean processing = false;

    private final int pixelHeight, pixelWidth;
    private final int[] pixels;
    private int[] lightMap;
    private int[] lightBlock;

    private int[] zBuffer;
    private int zDepth;



    public Renderer(GameContainer gameContainer) {
        pixelWidth = gameContainer.getWidth();
        pixelHeight = gameContainer.getHeight();
        pixels = gameContainer.getWindow().getImagePixelData();
        zBuffer = new int[pixels.length];
        lightMap = new int[pixels.length];
        lightBlock = new int[pixels.length];
    }

    public void process() {
        processing = true;

        Collections.sort(imageRequests, new Comparator<ImageRequest>() {
            @Override
            public int compare(ImageRequest i1, ImageRequest i2) {
                if (i1.zDepth < i2.zDepth) {
                    return -1;
                }
                if (i1.zDepth > i2.zDepth) {
                    return 1;
                }
                return 0;
            }
        });
        for (int i = 0; i < imageRequests.size(); i++) {
            ImageRequest imgRes = imageRequests.get(i);
            setzDepth(imgRes.zDepth);
            drawImage(imgRes.image, imgRes.offX, imgRes.offY);
        }

        for (int i = 0; i < lightRequests.size(); i++) {
            LightRequest l = lightRequests.get(i);
            this.drawLightRequest(l.light, l.x, l.y);
        }

        for (int i = 0; i < pixels.length; i++) {
            float r = ((lightMap[i] >> 16) & 0xff) / 255f;
            float g = ((lightMap[i] >> 8) & 0xff) / 255f;
            float b = ((lightMap[i]) & 0xff) / 255f;
            pixels[i] = ((int)(((pixels[i] >> 16) & 0xff) * r)) << 16 | ((int)(((pixels[i] >> 8) & 0xff) * g)) << 8 | ((int)((pixels[i] & 0xff) * b));
        }

        processing = false;
        imageRequests.clear();
        lightRequests.clear();
    }

    public void clear() {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = 0;
            zBuffer[i] = 0;
            lightMap[i] = Color.AMBIENT_COLOR.getHexValue();
            lightBlock[i] = 0;
        }
    }

    public void setPixel(int x, int y, int value) {
        int alpha = ((value >> 24) & 0xff);  // bit shift right by 24 bit AND mask last 8 bit
        if( (x < 0 || x >= pixelWidth || y < 0 || y >= pixelHeight) || alpha  == 0) {
            return;
        }

        int pixelIndex = x + y * pixelWidth;

        if (zBuffer[pixelIndex] > zDepth) {
            return;
        }

        zBuffer[pixelIndex] = zDepth;

        if (alpha == 255) {
            pixels[pixelIndex] = value;
        } else {
            int pixelColor = pixels[pixelIndex];

            // blend red -> get the difference in red between old and new and scale it by the factor of alpha (how transparent it is)
            int newRed = ((pixelColor >> 16) & 0xff) - (int)((((pixelColor >> 16) & 0xff) - ((value >> 16) & 0xff)) * (alpha / 255f));
            int newGreen = ((pixelColor >> 8) & 0xff) - (int)((((pixelColor >> 8) & 0xff) - ((value >> 8) & 0xff)) * (alpha / 255f));
            int newBlue = ((pixelColor) & 0xff) - (int)((((pixelColor) & 0xff) - ((value) & 0xff)) * (alpha / 255f));

            pixels[pixelIndex] = (255 << 24 | newRed << 16 | newGreen << 8 | newBlue );

        }
    }

    public void setLightMap(int x, int y, int value) {
        if (x < 0 || x >= pixelWidth || y < 0 || y >= pixelHeight) {
            return;
        }
        int pixelIndex = x + y * pixelWidth;

        int baseColor = lightMap[pixelIndex];
        int maxRed = Math.max((baseColor >> 16) & 0xff, (value >> 16) & 0xff);
        int maxGreen = Math.max((baseColor >> 8) & 0xff, (value >> 8) & 0xff);
        int maxBlue = Math.max((baseColor) & 0xff, (value) & 0xff);

        lightMap[pixelIndex] = (maxRed << 16 | maxGreen << 8 | maxBlue );

    }

    public void setLightBlock(int x, int y, int value) {
        if (x < 0 || x >= pixelWidth || y < 0 || y >= pixelHeight) {
            return;
        }
        int pixelIndex = x + y * pixelWidth;

        if (zBuffer[pixelIndex] > zDepth) {
            return;
        }
        lightBlock[pixelIndex] = value;
    }

    public void drawImage(Image image, int offX, int offY) {

        if (image.isAlpha() && !processing) {
            imageRequests.add(new ImageRequest(image, zDepth, offX, offY));
            return;
        }

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
                this.setLightBlock(x + offX, y + offY , image.getLightBlock());
            }
        }
    }

    public void drawImageTile(ImageTile imageTile, int offX, int offY, int tileX, int tileY) {
        if (imageTile.isAlpha() && !processing) {
            imageRequests.add(new ImageRequest(imageTile.getTileImage(tileX, tileY), zDepth, offX, offY));
            return;
        }

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
                this.setLightBlock(x + offX, y + offY , imageTile.getLightBlock());
            }
        }
    }


    public void drawLight(Light light, int offX, int offY) {
        lightRequests.add(new LightRequest(light, offX, offY));
    }

    private void drawLightRequest(Light light, int offX, int offY) {
        for (int i = 0; i <= light.getDiameter(); i++) {
            this.drawLightLine(light, light.getRadius(), light.getRadius(), i, 0, offX, offY);
            this.drawLightLine(light, light.getRadius(), light.getRadius(), i, light.getDiameter(), offX, offY);
            this.drawLightLine(light, light.getRadius(), light.getRadius(), 0, i, offX, offY);
            this.drawLightLine(light, light.getRadius(), light.getRadius(), light.getDiameter(), i, offX, offY);
        }
    }

    // Bresenham's line algorithm
    private void drawLightLine(Light light, int x0, int y0, int x1, int y1, int offX, int offY) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);

        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;

        int err = dx - dy;
        int e2;

        while (true){

            int lightColor = light.getLightValue(x0, y0);
            if (lightColor == 0) { // position is outside of light
                break;
            }

            int screenX = x0 - light.getRadius()+offX;
            int screenY = y0 -light.getRadius()+offY;

            if (screenX < 0 || screenX >= pixelWidth || screenY < 0 || screenY >= pixelHeight) {
                break;
            }

            if (lightBlock[screenX + screenY * pixelWidth] == Light.FULL) {
                break;
            }

            setLightMap(screenX, screenY, lightColor);

            if (x0 == x1 && y0 == y1) {
                break;
            }

            e2 = 2 * err;

            if (e2 > -1 * dy) {
                err -= dy;
                x0 += sx;
            }

            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
    }

    private void plotLineLow(int x0, int y0, int x1, int y1, int color) {

        int dx = x1 - x0;
        int dy = y1 - y0;
        int yi = 1;

        if (dy < 0) {
            yi = -1;
            dy = -dy;
        }

        int D = (2 * dy) - dx;
        int y = y0;

        for (int x = x0; x < x1; x++ ) {
            this.setPixel(x, y, color);
            if (D > 0) {
                y = y + yi;
                D = D + (2 * (dy - dx));
            } else {
                D = D + 2*dy;
            }
        }
    }

    private void plotLineHigh(int x0, int y0, int x1, int y1, int color) {

        int dx = x1 - x0;
        int dy = y1 - y0;
        int xi  = 1;

        if (dx  < 0) {
            xi  = -1;
            dx = -dx;
        }

        int D = (2 * dx) - dy;
        int x = x0;

        for (int y = y0; y < y1; y++ ) {
            this.setPixel(x, y, color);
            if (D > 0) {
                x = x + xi;
                D = D + (2 * (dx - dy));
            } else {
                D = D + 2*dx;
            }
        }
    }

    public void drawLine(Line line, int offX, int offY, int color) {
        if (Math.abs(line.getEndY() - line.getStartY()) <= Math.abs(line.getEndX() - line.getStartX())) {
            if (line.getStartX() > line.getEndX()) {
                this.plotLineLow((int)line.getEndX(), (int)line.getEndY(), (int)line.getStartX(), (int)line.getStartY(), color);
            } else {
                this.plotLineLow((int)line.getStartX(), (int)line.getStartY(), (int)line.getEndX(),(int) line.getEndY(), color);
            }
        } else {
            if (line.getStartY() > line.getEndY()) {
                this.plotLineHigh((int)line.getEndX(), (int)line.getEndY(),(int) line.getStartX(), (int)line.getStartY(), color);
            } else {
                this.plotLineHigh((int)line.getStartX(), (int)line.getStartY(),(int) line.getEndX(), (int)line.getEndY(), color);
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
                        setLightMap(x + offX + offset, y + offY, color);
                    }
                }
            }

            offset += (font.getWidths()[unicode] * size);
        }
    }

    public void drawRect(int offX, int offY, int width, int height, int color) {

        // TODO clipping and out of screen

        for (int y = 0; y < height; y++) {
            setPixel(offX, y + offY, color);
            setPixel(offX + width, y + offY, color);
        }

        for (int x = 0; x < width; x++) {
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

    public void setzDepth(int zDepth) {
        this.zDepth = zDepth;
    }
}
