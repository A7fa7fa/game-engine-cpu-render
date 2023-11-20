package org.a7fa7fa.core;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Window {

    private JFrame frame;
    private BufferedImage image;
    private Canvas canvas;
    private BufferStrategy bufferStrategy;
    private Graphics graphics;

    public Window(GameContainer gc) {
        image = new BufferedImage(gc.getWidth(), gc.getHeight(), BufferedImage.TYPE_INT_RGB); // image that is stored in RAM
        canvas = new Canvas();
        Dimension s = new Dimension((int)(gc.getWidth() * gc.getScale()), (int)(gc.getHeight() * gc.getScale()));
        canvas.setPreferredSize(s);
        canvas.setMaximumSize(s);
        canvas.setMaximumSize(s);

        frame = new JFrame(gc.getTitle());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // x on window ends the program
        frame.setLayout(new BorderLayout());
        frame.add(canvas, BorderLayout.CENTER); // adds canvas to center of frame
        frame.pack(); // sets size of frame to size of canvas
        frame.setLocationRelativeTo(null); // opens in the middle of screen
        frame.setResizable(false);
        frame.setVisible(true);

        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        graphics = bufferStrategy.getDrawGraphics();
    }

    public void update() {
        // draws image to BufferStrategy, that is attached to canvas
        graphics.drawImage(image,0, 0, canvas.getWidth(), canvas.getHeight(),null);
        // to draw from BufferStrategy to canvas
        bufferStrategy.show();
    }
}
