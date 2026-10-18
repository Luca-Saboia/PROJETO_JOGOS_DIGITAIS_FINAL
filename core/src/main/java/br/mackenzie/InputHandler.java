package br.mackenzie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/** Entrada durante a partida. */
public class InputHandler {

    private final Player player;
    private final GameWorld world;

    public InputHandler(Player player, GameWorld world) {
        this.player = player;
        this.world = world;
    }

    public void handleInput() {

        if (!world.isGameOver()
                && (Gdx.input.justTouched()
                || Gdx.input.isKeyJustPressed(Input.Keys.SPACE))) {

            player.jump();
        }
    }
}