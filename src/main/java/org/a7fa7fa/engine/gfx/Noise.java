package org.a7fa7fa.engine.gfx;

import org.j3d.texture.procedural.PerlinNoiseGenerator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Noise extends Image {

    private PerlinNoiseGenerator noiseGen = new PerlinNoiseGenerator();

    private float resolution = 0.01f;
    private float off = 0.0f;
    private int tileSize = 1;


    public Noise(int width, int height, int tileSize) {
        super(width * tileSize, height * tileSize);
        this.tileSize = tileSize;
        this.update(0.35f);
        setAlpha(true);
    }

    private int createColor(float power) {
        return this.createColor(power, power);
    }

    private int createColor(float power, float alphaPower) {
        return (((int)(255*(1-alphaPower)) & 0xFF) << 24) |
                (((int)(255*power) & 0xFF) << 16) |
                (((int)(255*power) & 0xFF) << 8)  |
                (((int)(255*power) & 0xFF) << 0);
    }

    public void update(float deltaTime) {
        this.off += deltaTime;
        for (int y = 0; y < this.getHeight(); y += this.tileSize) {
            for (int x= 0; x < this.getWidth(); x += this.tileSize) {
                float power = ((noiseGen.noise3((float)(x) * resolution, (float) (y) * resolution, this.off)) + 1.0f) / 2.0f;

                int value;

                if (power < 0.4) { // dark -> make black
                    value =  this.createColor(power, power * 1.7f);
                } else if (power > 0.48 && power < 0.52) { // color circle around dark
//                    value =  this.createColor(power, power * 1.9f);
                    value =  (((int)(255) & 0xFF) << 24) |
                            (((int)(0) & 0xFF) << 16) |
                            (((int)(0) & 0xFF) << 8)  |
                            (((int)(255*power) & 0xFF) << 0);
                } else if (power > 0.58f && power < 0.62f) { // color circle around dark
//                    value =  this.createColor(power, power * 1.9f);
                    value =  (((int)(255) & 0xFF) << 24) |
                            (((int)(0) & 0xFF) << 16) |
                            (((int)(255*power) & 0xFF) << 8)  |
                            (((int)(0) & 0xFF) << 0);
                }else if (power > 0.68f && power < 0.72f) { // color circle around dark
//                    value =  this.createColor(power, power * 1.9f);
                    value =  (((int)(255) & 0xFF) << 24) |
                            (((int)(0) & 0xFF) << 16) |
                            (((int)(255*power) & 0xFF) << 8)  |
                            (((int)(255*power) & 0xFF) << 0);
                } else if (power > 0.85f) { // color circle around dark
//                    value =  this.createColor(power, power * 1.9f);
                    value =  (((int)(255) & 0xFF) << 24) |
                            (((int)(0) & 0xFF) << 16) |
                            (((int)(255*power) & 0xFF) << 8)  |
                            (((int)(255*power) & 0xFF) << 0);
                } else {
                    value =  this.createColor(1.0f);
                }

                for (int xi = 0; xi < this.tileSize; xi++) {
                    for (int yi = 0; yi < this.tileSize; yi++) {
                        int pixelIndex = (x+xi) + (y+yi) * this.getWidth();
                        this.getPixels()[pixelIndex] = value;
                    }
                }


            }
        }
    }
}
