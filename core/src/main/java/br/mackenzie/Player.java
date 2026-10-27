package br.mackenzie;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends GameObject {
    private static final float GRAVITY = -1000f;
    private static final float JUMP_FORCE = 350f;

    private TextureRegion[] frames;
    private float animTime;

    public Player(float x, float y, Texture texture) {
        super(x, y, 32, 24);

        // O arquivo flappybird.png fornecido no projeto e uma imagem unica,
        // nao uma spritesheet de tres quadros de 32x24.
        this.frames = new TextureRegion[] {
                new TextureRegion(texture)
        };
    }

    @Override
    public void update(float delta) {
        animTime += delta;

        velocity.add(0, GRAVITY * delta);
        position.add(0, velocity.y * delta);

        bounds.setPosition(position.x, position.y);
    }

    public void jump() {
        velocity.y = JUMP_FORCE;
    }

    @Override
    public void render(SpriteBatch batch) {
        int frameIndex = (int) (animTime * 10) % frames.length;
        batch.draw(frames[frameIndex], position.x, position.y, width, height);
    }

    public void reset(float x, float y) {
        position.set(x, y);
        velocity.set(0, 0);
        bounds.setPosition(x, y);
    }
}