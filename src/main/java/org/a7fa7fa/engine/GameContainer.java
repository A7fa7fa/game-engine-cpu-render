package org.a7fa7fa.engine;

public class GameContainer implements Runnable {

    private Thread thread;
    private Window window;
    private Fps fps;
    private Renderer renderer;
    private Input input;
    private AbstractGame game;

    private boolean running = false;
    private final double FRAMES_PER_SECOND = 60.0;
    private final double UPDATE_CAP = 1.0 / FRAMES_PER_SECOND;

//    private int width = 1600, height = 900;
    private int width = 640, height = 480;
    private float scale = 1f;

    private String title = "my engine v1.0";

    public GameContainer(AbstractGame game){
        this.game = game;
    }

    public void start() {
        System.out.println("Starting game container thread...");
        window = new Window(this);
        renderer = new Renderer(this);
        input = new Input(this);
        fps = new Fps();
        thread = new Thread(this);
        thread.run(); // makes this to main thread
    }

    public void stop() {
        running = false;
    }

    public void run() {
        System.out.println("Thread running.");
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

                game.update(this, (float)UPDATE_CAP);

                // input update should be the last part of update loop
                input.update();
                if (fps.getFrameTime() >= 1.0) {
                    fps.updateFps();
                }

            }

            if (render) {
                renderer.clear();
                game.render(this, renderer);
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

    public Input getInput() {
        return input;
    }

    public double getFps() {
        return fps.getFps();
    }
}
