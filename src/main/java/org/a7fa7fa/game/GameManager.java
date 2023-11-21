package org.a7fa7fa.game;

import org.a7fa7fa.engine.AbstractGame;
import org.a7fa7fa.engine.GameContainer;
import org.a7fa7fa.engine.Renderer;
import org.a7fa7fa.engine.gfx.Image;

import java.awt.event.KeyEvent;

public class GameManager extends AbstractGame {

    private Image image;
    private Image background;

    public GameManager() {
        background = new Image("/background.png");
        image = new Image("/Untitled.png");
    }

    @Override
    public void update(GameContainer gameContainer, float deltaTime) {
        if (gameContainer.getInput().isKeyDown(KeyEvent.VK_A)) {
            System.out.println("Key A was pressed");
        }

    }

    @Override
    public void render(GameContainer gameContainer, Renderer renderer) {

        renderer.drawImage(background, 0, 0);
        renderer.drawImage(image, gameContainer.getInput().getMouseX() - (image.getWidth()/2), gameContainer.getInput().getMouseY() - (image.getHeight() / 2));

    }

    public static void main( String[] args ) {

        System.out.println( "Starting..." );
        GameContainer gc = new GameContainer(new GameManager());
        gc.start();
    }
}
