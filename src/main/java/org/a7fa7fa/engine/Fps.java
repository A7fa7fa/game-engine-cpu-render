package org.a7fa7fa.engine;

public class Fps {

    private double frameTime = 0;
    private int frames = 0;
    private double fps = 0.0;

    public Fps() {}

    void updateFrameTime(double passedTime) {
        this.frameTime += passedTime;
    }

    void incrementFrame() {
        this.frames++;
    }

    void updateFps() {
        double fps = Math.round(frames/frameTime*100.0)/100.0;
        frameTime = 0;
        frames = 0;
        this.fps = fps;
    }

    double getFps() {
        return this.fps;
    }

    double getFrameTime() {
        return frameTime;
    }
}
