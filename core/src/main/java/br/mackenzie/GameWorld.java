package br.mackenzie;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class GameWorld {
    private Player player;
    private Array<Pipe> pipes;
    private float spawnTimer;
    private int score;
    private boolean gameOver;

    private static final float SPAWN_INTERVAL = 1.6f;
    private static final float GAP_SIZE = 130f;

    public GameWorld(Texture passaroTexture) {
        this.player = new Player(100, 240, passaroTexture);
        this.pipes = new Array<>();
        this.score = 0;
        this.gameOver = false;
    }

    public void update(float delta) {
        if (gameOver) return;

        player.update(delta);

        // Geração contínua de canos
        spawnTimer += delta;
        if (spawnTimer >= SPAWN_INTERVAL) {
            spawnTimer = 0;
            spawnPipes();
        }

        // Atualização dos canos e verificação de colisões
        for (int i = pipes.size - 1; i >= 0; i--) {
            Pipe pipe = pipes.get(i);
            pipe.update(delta);

            if (player.getBounds().overlaps(pipe.getBounds())) {
                gameOver = true;
            }

            if (!pipe.isScored() && pipe.getPosition().x + pipe.getWidth() < player.getPosition().x) {
                pipe.setScored(true);
                score++;
            }

            if (pipe.isOffscreen()) {
                pipes.removeIndex(i);
            }
        }

        // Colisão com o chão ou limite superior da tela (480px de altura)
        if (player.getPosition().y <= 0 || player.getPosition().y >= 480 - player.getHeight()) {
            gameOver = true;
        }
    }

    private void spawnPipes() {
        float pipeWidth = 50f;
        float minHeight = 40f;
        float maxHeight = 480f - GAP_SIZE - minHeight;
        float bottomHeight = MathUtils.random(minHeight, maxHeight);

        Pipe bottomPipe = new Pipe(800, 0, pipeWidth, bottomHeight);
        Pipe topPipe = new Pipe(800, bottomHeight + GAP_SIZE, pipeWidth, 480f - (bottomHeight + GAP_SIZE));

        pipes.add(bottomPipe);
        pipes.add(topPipe);
    }

    public void render(SpriteBatch batch) {
        player.render(batch);
    }

    public void restart() {
        player.reset(100, 240);
        pipes.clear();
        score = 0;
        gameOver = false;
        spawnTimer = 0;
    }

    public Player getPlayer() { return player; }
    public boolean isGameOver() { return gameOver; }
    public int getScore() { return score; }
}