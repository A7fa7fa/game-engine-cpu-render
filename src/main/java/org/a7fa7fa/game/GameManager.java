package org.a7fa7fa.game;

import org.a7fa7fa.engine.AbstractGame;
import org.a7fa7fa.engine.Color;
import org.a7fa7fa.engine.GameContainer;
import org.a7fa7fa.engine.Renderer;
import org.a7fa7fa.engine.gfx.Image;
import org.a7fa7fa.engine.gfx.ImageTile;

import java.awt.event.KeyEvent;
import java.util.concurrent.ThreadLocalRandom;

public class GameManager extends AbstractGame {

    private Image cursor;
    private ImageTile backgroundTile;
    private ImageTile sprite;

    public GameManager() {
        backgroundTile = new ImageTile("/grasstile_16_16_7.png", 16, 16);
        cursor = new Image("/cursor.png");
        sprite = new ImageTile("/weed.png", 16, 16);
    }

    @Override
    public void update(GameContainer gameContainer, float deltaTime) {
        if (gameContainer.getInput().isKeyDown(KeyEvent.VK_A)) {
            System.out.println("Key A was pressed");
        }
        spritePos += deltaTime * 12;
        if (spritePos >= 9){
            spritePos = 0;
        }

    }

    float spritePos = 0;
    int[] world;

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }


    @Override
    public void render(GameContainer gameContainer, Renderer renderer) {

        if (world == null) {
            world = new int[45*200];
            for (int j = 0; j < gameContainer.getHeight(); j += 16) {
                for (int i = 0; i < gameContainer.getWidth(); i += 16) {
                    int rand = ThreadLocalRandom.current().nextInt(0, 7);
                    world[(j/16) * (gameContainer.getWidth() / 16) + (i/16)] = rand;
                }
            }
        }

        for (int j = 0; j < gameContainer.getHeight(); j += 16) {
            for (int i = 0; i < gameContainer.getWidth(); i += 16) {
                renderer.drawImageTile(backgroundTile, i, j, world[(j/16) * (gameContainer.getWidth() / 16) + (i/16)] , 0);
            }
        }

        renderer.drawImageTile(sprite, 50, 50, (int)spritePos, 0);

        renderer.drawImageTile(sprite, 100, 60, (int)spritePos, 0);

        renderer.drawImage(cursor, gameContainer.getInput().getMouseX() - 8, gameContainer.getInput().getMouseY() - 8);
        renderer.drawText("Fps: " + String.valueOf(gameContainer.getFps()), 1,1, Color.WHITE.getHexValue());
        renderer.drawText("Mousepos x:" + gameContainer.getInput().getMouseX() + " y:"+ gameContainer.getInput().getMouseY(), 1,11, Color.WHITE.getHexValue());
    }

    public static void main( String[] args ) {

        System.out.println( "Starting..." );
        GameContainer gc = new GameContainer(new GameManager());
        gc.start();
    }
}
