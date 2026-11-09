
package br.mackenzie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class InputHandler {

    private final Player player;
    private final GameWorld world;

    public InputHandler(Player player, GameWorld world) {
        this.player = player;
        this.world = world;
    }

    public void handleInput() {
        if (world.isGameOver()) {
            return;
        }

        boolean touched = Gdx.input.justTouched();
        boolean spacePressed =
            Gdx.input.isKeyJustPressed(Input.Keys.SPACE);

        if (touched || spacePressed) {
            player.jump();
        }
    }
}
