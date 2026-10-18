package br.mackenzie;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class GameWorld {

    public static final float WORLD_WIDTH = 800f;
    public static final float WORLD_HEIGHT = 480f;

    private static final float PIPE_WIDTH = 50f;
    private static final float MIN_PIPE_HEIGHT = 40f;

    private final Player player;
    private final Array<Pipe> pipes;
    private final Texture pipeTexture;

    private float spawnTimer;
    private int score;
    private boolean gameOver;
    private Difficulty difficulty;

    private float previousGapCenter = Float.NaN;

    public GameWorld(Texture passaroTexture, Texture pipeTexture) {
        this(passaroTexture, pipeTexture, Difficulty.FACIL);
    }

    public GameWorld(
            Texture passaroTexture,
            Texture pipeTexture,
            Difficulty difficulty) {

        this.player = new Player(100, 240, passaroTexture);
        this.pipeTexture = pipeTexture;
        this.pipes = new Array<>();

        startNewGame(difficulty);
    }

    public void update(float delta) {

        if (gameOver) {
            return;
        }

        player.update(delta);

        spawnTimer += delta;

        if (spawnTimer >= difficulty.getSpawnInterval()) {
            spawnTimer -= difficulty.getSpawnInterval();
            spawnPipes();
        }

        for (int i = pipes.size - 1; i >= 0; i--) {

            Pipe pipe = pipes.get(i);

            pipe.update(delta);

            // Verifica colisão entre o pássaro e os canos.
            if (player.getBounds().overlaps(pipe.getBounds())) {
                gameOver = true;
            }

            // A pontuação conta os dois canos de cada par.
            // O Main divide o total por dois para exibir um ponto por par.
            if (!pipe.isScored()
                    && pipe.getPosition().x + pipe.getWidth()
                    < player.getPosition().x) {

                pipe.setScored(true);
                score++;
            }

            // Remove os canos que saíram da tela.
            if (pipe.isOffscreen()) {
                pipes.removeIndex(i);
            }
        }

        // Verifica se o pássaro saiu dos limites verticais.
        if (player.getPosition().y <= 0
                || player.getPosition().y >=
                WORLD_HEIGHT - player.getHeight()) {

            gameOver = true;
        }
    }

    private void spawnPipes() {

        float gapSize = difficulty.getGapSize();

        float minCenter =
                MIN_PIPE_HEIGHT + gapSize / 2f;

        float maxCenter =
                WORLD_HEIGHT - MIN_PIPE_HEIGHT - gapSize / 2f;

        float gapCenter;

        if (Float.isNaN(previousGapCenter)) {

            // A primeira abertura começa em uma altura aleatória.
            gapCenter = MathUtils.random(minCenter, maxCenter);

        } else {

            // A variação aumenta conforme a dificuldade.
            float maxVariation =
                    difficulty.getMaxGapCenterChange();

            float minVariation = maxVariation * 0.45f;

            float upRoom = maxCenter - previousGapCenter;
            float downRoom = previousGapCenter - minCenter;

            boolean canMoveUp = upRoom >= minVariation;
            boolean canMoveDown = downRoom >= minVariation;

            float direction;

            if (canMoveUp && canMoveDown) {
                direction =
                        MathUtils.randomBoolean() ? 1f : -1f;

            } else if (canMoveUp) {
                direction = 1f;

            } else if (canMoveDown) {
                direction = -1f;

            } else {
                direction =
                        upRoom >= downRoom ? 1f : -1f;
            }

            float availableRoom =
                    direction > 0 ? upRoom : downRoom;

            float lowestStep =
                    Math.min(minVariation, availableRoom);

            float largestStep =
                    Math.min(maxVariation, availableRoom);

            float step =
                    MathUtils.random(lowestStep, largestStep);

            gapCenter =
                    previousGapCenter + direction * step;
        }

        previousGapCenter = gapCenter;

        // Calcula as alturas dos canos superior e inferior.
        float bottomHeight =
                gapCenter - gapSize / 2f;

        float topY =
                gapCenter + gapSize / 2f;

        float topHeight =
                WORLD_HEIGHT - topY;

        float speed = difficulty.getPipeSpeed();

        Pipe bottomPipe = new Pipe(
                WORLD_WIDTH,
                0,
                PIPE_WIDTH,
                bottomHeight,
                pipeTexture,
                speed
        );

        Pipe topPipe = new Pipe(
                WORLD_WIDTH,
                topY,
                PIPE_WIDTH,
                topHeight,
                pipeTexture,
                speed
        );

        pipes.add(bottomPipe);
        pipes.add(topPipe);
    }

    public void render(SpriteBatch batch) {

        for (Pipe pipe : pipes) {
            pipe.render(batch);
        }

        player.render(batch);
    }

    /** Reinicia a partida mantendo a dificuldade selecionada. */
    public void restart() {
        startNewGame(difficulty);
    }

    /** Inicia uma nova partida na dificuldade escolhida. */
    public final void startNewGame(Difficulty difficulty) {

        this.difficulty =
                difficulty == null ? Difficulty.FACIL : difficulty;

        player.reset(100, 240);

        pipes.clear();

        score = 0;
        gameOver = false;
        spawnTimer = 0;

        // Permite sortear novamente a altura do primeiro par.
        previousGapCenter = Float.NaN;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getScore() {
        return score;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }
}