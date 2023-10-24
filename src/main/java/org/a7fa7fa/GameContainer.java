package org.a7fa7fa;

public class GameContainer implements Runnable {

    private Thread thread;
    private boolean running = false;
    private final double UPDATE_CAP = 1.0/60.0;


    public GameContainer(){}

    public void start() {
        System.out.println("Starting game container thread...");
        thread = new Thread(this);
        thread.run(); // makes this to main thread
    }

    public void stop() {}

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

                if (fps.getFrameTime() >= 0.1) {
                    System.out.println("Fps : " + fps.getFps());
                }

            }

            if (render) {

                // TODO render game
                fps.incrementFrame();

            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // TOOD error handling
                    throw new RuntimeException(e);
                }
            }
        }

        dispose();

    }

    private void dispose() {}


}
