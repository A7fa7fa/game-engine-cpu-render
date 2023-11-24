package org.a7fa7fa.game;

import org.a7fa7fa.engine.AbstractGame;
import org.a7fa7fa.engine.Color;
import org.a7fa7fa.engine.GameContainer;
import org.a7fa7fa.engine.Renderer;
import org.a7fa7fa.engine.audio.SoundClip;
import org.a7fa7fa.engine.gfx.Image;
import org.a7fa7fa.engine.gfx.ImageTile;
import org.a7fa7fa.engine.gfx.Light;

import java.awt.event.KeyEvent;
import java.util.concurrent.ThreadLocalRandom;

public class GameManager extends AbstractGame {

    private Image cursor;
    private Image tras;
    private ImageTile trasTile;
    private ImageTile backgroundTile;
    private ImageTile sprite;
    private ImageTile character;
    private ImageTile idle;
    private SoundClip clip;
    private Light light2;

    public GameManager() {
        backgroundTile = new ImageTile("/grasstile_16_16_7.png", 16, 16);
        cursor = new Image("/cursor.png");
        tras = new Image("/test-transparent.png");
        tras.setAlpha(true);
        trasTile = new ImageTile("/test-transparent.png", 64,64);
        trasTile.setAlpha(true);
        sprite = new ImageTile("/weed_16_16_9.png", 16, 16);
        character = new ImageTile("/char_16_16_4.png", 16, 16);
        character.setLightBlock(Light.FULL);
        idle = new ImageTile("/character/Swordsman/Idle.png", 128, 128);
        clip = new SoundClip("/audio/clap.wav");
        light2 = new Light(200, 0xff00ffff);
//        clip.setVolume(-20);
    }

    int charX = -1;
    int charY = 0;
    int charTile = 0;


    @Override
    public void update(GameContainer gameContainer, float deltaTime) {

        if (charX == -1) {
            charX = gameContainer.getWidth() / 2;
            charY = gameContainer.getHeight() / 2;
        }

        if (gameContainer.getInput().isKeyDown(KeyEvent.VK_SPACE)) {
            clip.play();
        }

        if (gameContainer.getInput().isKey(KeyEvent.VK_A)) {
            charTile = 2;
            charX -= 1;
        }

        if (gameContainer.getInput().isKey(KeyEvent.VK_D)) {
            charTile = 3;
            charX += 1;
        }
        if (gameContainer.getInput().isKey(KeyEvent.VK_W)) {
            charTile = 1;
            charY -= 1;
        }
        if (gameContainer.getInput().isKey(KeyEvent.VK_S)) {
            charTile = 0;
            charY += 1;
        }



        spritePos += deltaTime * 12;
        if (spritePos >= 9){
            spritePos = 0;
        }

        idlePos += deltaTime * 12;
        if (idlePos >= 8){
            idlePos = 0;
        }

    }

    float spritePos = 0;
    float idlePos = 0;
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



        renderer.setzDepth(Integer.MAX_VALUE);
        renderer.drawImage(tras, gameContainer.getInput().getMouseX() +50, gameContainer.getInput().getMouseY() +50);
        renderer.setzDepth(0);

        for (int j = 0; j < gameContainer.getHeight(); j += 16) {
            for (int i = 0; i < gameContainer.getWidth(); i += 16) {
                renderer.drawImageTile(backgroundTile, i, j, world[(j/16) * (gameContainer.getWidth() / 16) + (i/16)] , 0);
            }
        }

        renderer.drawRect(50,50,32,32, Color.BLACK.getHexValue());
        renderer.drawRect(100,100,32,32, Color.BLUEISH.getHexValue(), true);

        renderer.drawImageTile(sprite, 350, 150, (int)spritePos, 0);

        renderer.drawImageTile(sprite, 600, 260, (int)spritePos, 0);

        renderer.drawImageTile(character, charX, charY, charTile, 0);
        renderer.drawImageTile(idle, charX + 50, charY + 50, (int)idlePos, 0);
        renderer.drawImageTile(trasTile, 200, charY + 200, 0, 0);

        renderer.drawImage(cursor, gameContainer.getInput().getMouseX() - 8, gameContainer.getInput().getMouseY() - 8);

        renderer.drawLight(light2, gameContainer.getInput().getMouseX(),gameContainer.getInput().getMouseY());
        renderer.drawLight(light2, 200, 200);
        renderer.drawLight(light2, 400, 400);
        renderer.drawLight(light2, 800, 400);
        renderer.drawLight(light2, 600, 400);

        int textSize = 3;

        renderer.drawText("Fps: " + String.valueOf(gameContainer.getFps()), 1,1, Color.WHITE.getHexValue(), textSize);
        renderer.drawText("Mouse x:" + gameContainer.getInput().getMouseX() + " y:"+ gameContainer.getInput().getMouseY(), 1,21, Color.WHITE.getHexValue(), textSize);
        renderer.drawText("Mouse Tile x:" + gameContainer.getInput().getMouseX()/16 + " y:"+ gameContainer.getInput().getMouseY()/16, 1,41, Color.WHITE.getHexValue(), textSize);
        renderer.drawText("Char Tile x:" + charX/16 + " y:"+ charY/16, 1,61, Color.WHITE.getHexValue(), textSize);

    }

    public static void main( String[] args ) {

        System.out.println( "Starting..." );
        GameContainer gc = new GameContainer(new GameManager());
        gc.start();
    }
}
