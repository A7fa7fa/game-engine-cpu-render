package org.a7fa7fa.engine;

public abstract class AbstractGame {

    public abstract void update(GameContainer gameContainer, float deltaTime);
    public abstract void render(GameContainer gameContainer, Renderer renderer);
}
