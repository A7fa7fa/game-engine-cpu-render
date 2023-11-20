package org.a7fa7fa.game;

import org.a7fa7fa.engine.AbstractGame;
import org.a7fa7fa.engine.GameContainer;
import org.a7fa7fa.engine.Renderer;

import java.awt.event.KeyEvent;

public class GameManager extends AbstractGame {

    public GameManager() {}

    @Override
    public void update(GameContainer gameContainer, float deltaTime) {
        if (gameContainer.getInput().isKeyDown(KeyEvent.VK_A)) {
            System.out.println("Key A was pressed");
        }

    }

    @Override
    public void render(GameContainer gameContainer, Renderer renderer) {

    }

    public static void main( String[] args ) {

        System.out.println( "Starting..." );


        GameContainer gc = new GameContainer(new GameManager());
        gc.start();
    }
}
