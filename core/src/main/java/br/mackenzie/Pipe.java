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

        // Se a textura tiver tamanho do spritesheet (96x24), fatiamos em 3 quadros
        if (texture.getWidth() >= 96) {
            this.frames = new TextureRegion[3];
            this.frames[0] = new TextureRegion(texture, 0, 0, 32, 24);
            this.frames[1] = new TextureRegion(texture, 32, 0, 32, 24);
            this.frames[2] = new TextureRegion(texture, 64, 0, 32, 24);
        } else {
            this.frames = new TextureRegion[1];
            this.frames[0] = new TextureRegion(texture);
        }
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