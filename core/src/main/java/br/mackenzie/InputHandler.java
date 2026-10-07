package br.mackenzie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class InputHandler {
    private Player player;
    private GameWorld world;

    public InputHandler(Player player, GameWorld world) {
        this.player = player;
        this.world = world;
    }

    public void handleInput() {
        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (world.isGameOver()) {
                world.restart();
            } else {
                player.jump();
            }
        }
    }
}