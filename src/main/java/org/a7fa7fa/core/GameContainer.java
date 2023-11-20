package org.a7fa7fa.core;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class GameContainer implements Runnable {

    private Thread thread;
    private Window window;
    private Renderer renderer;
    private Input input;

    private boolean running = false;
    private final double FRAMES_PER_SECOND = 60.0;
    private final double UPDATE_CAP = 1.0 / FRAMES_PER_SECOND;

//    private int width = 1600, height = 900;
    private int width = 320, height = 240;
    private float scale = 4f;

    private String title = "my engine v1.0";

    public GameContainer(){}

    public void start() {
        System.out.println("Starting game container thread...");
        window = new Window(this);
        renderer = new Renderer(this);
        input = new Input(this);
        thread = new Thread(this);
        thread.run(); // makes this to main thread
    }

    public void stop() {
        running = false;
    }

    public void run() {
        System.out.println("Thread running.");
        Fps fps = new Fps();
        running = true;

        boolean render = false;
        double firstTime = 0;
        double lastTime = System.nanoTime() / 1_000_000_000.0; // makes to millisecond
        double passedTime = 0;
        double unprocessedTime = 0;

        while (running) {
            render = false;
            firstTime = System.nanoTime() / 1_000_000_000.0;
            passedTime = firstTime - lastTime;
            lastTime = firstTime;

            unprocessedTime += passedTime;
            fps.updateFrameTime(passedTime);

            while (unprocessedTime >= UPDATE_CAP){
                unprocessedTime -= UPDATE_CAP;

                render = true; // only render if you have an update -> there is no point to render if there is no change

                // TODO update game
                if (input.isButtonDown(MouseEvent.BUTTON1)) {
                    System.out.println("key is pressed");
                }
                System.out.println("x:" + input.getMouseX() + " y:" + input.getMouseY());

                // input update should be the last part of update loop
                input.update();
                if (fps.getFrameTime() >= 1.1) {
                    System.out.println("Fps : " + fps.getFps());
                }

            }

            if (render) {
                renderer.clear();

                // TODO render game
                window.update();
                fps.incrementFrame();

            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // TODO error handling
                }
            }
        }

        dispose();
    }

    private void dispose()  {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            // TODO error handling
        }
    }



    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Window getWindow() {
        return window;
    }

}
