package br.mackenzie;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Pipe extends GameObject {

    private static final float DEFAULT_SPEED = 150f;

    private final float speed;
    private boolean scored;
    private Texture texture;

    public Pipe(
            float x,
            float y,
            float width,
            float height,
            Texture texture) {

        this(
                x,
                y,
                width,
                height,
                texture,
                DEFAULT_SPEED
        );
    }

    public Pipe(
            float x,
            float y,
            float width,
            float height,
            Texture texture,
            float speed) {

        super(x, y, width, height);

        this.texture = texture;
        this.speed = speed;
        this.scored = false;
    }

    @Override
    public void update(float delta) {

        position.x -= speed * delta;

        bounds.setPosition(
                position.x,
                position.y
        );
    }

    @Override
    public void render(SpriteBatch batch) {

        if (texture != null) {

            batch.draw(
                    texture,
                    position.x,
                    position.y,
                    width,
                    height
            );
        }
    }

    public boolean isOffscreen() {
        return position.x + width < 0;
    }

    public boolean isScored() {
        return scored;
    }

    public void setScored(boolean scored) {
        this.scored = scored;
    }
}