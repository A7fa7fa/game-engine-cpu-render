package org.a7fa7fa.engine;

public class Fps {

    private double frameTime = 0;
    private int frames = 0;

    public Fps() {}

    void updateFrameTime(double passedTime) {
        this.frameTime += passedTime;
    }

    void incrementFrame() {
        this.frames++;
    }

    double getFps() {
        double fps = Math.round(frames/frameTime*100.0)/100.0;
        frameTime = 0;
        frames = 0;
        return fps;
    }

    double getFrameTime() {
        return frameTime;
    }
}
