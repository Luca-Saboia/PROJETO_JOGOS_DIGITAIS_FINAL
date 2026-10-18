package br.mackenzie;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {

    private enum ScreenState {
        MENU,
        COUNTDOWN,
        PLAYING,
        GAME_OVER
    }

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;

    private BitmapFont font;
    private BitmapFont largeFont;
    private GlyphLayout layout;

    private Texture passaroTexture;
    private Texture pipeTexture;

    private GameWorld gameWorld;
    private InputHandler inputHandler;

    private ScreenState screenState = ScreenState.MENU;

    private Difficulty selectedDifficulty = Difficulty.FACIL;

    private float countdownElapsed;

    private final Vector3 touchPoint = new Vector3();

    private final Rectangle easyButton =
            new Rectangle(290, 267, 220, 52);

    private final Rectangle mediumButton =
            new Rectangle(290, 196, 220, 52);

    private final Rectangle hardButton =
            new Rectangle(290, 125, 220, 52);

    private final Rectangle replayButton =
            new Rectangle(240, 157, 320, 54);

    private final Rectangle menuButton =
            new Rectangle(300, 87, 200, 54);

    @Override
    public void create() {

        // O jogo sempre começa no menu.
        screenState = ScreenState.MENU;

        Gdx.app.log(
                "Main",
                "Iniciando na tela de selecao de dificuldade"
        );

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        camera = new OrthographicCamera();

        camera.setToOrtho(
                false,
                GameWorld.WORLD_WIDTH,
                GameWorld.WORLD_HEIGHT
        );

        font = new BitmapFont();

        largeFont = new BitmapFont();
        largeFont.getData().setScale(2f);

        layout = new GlyphLayout();

        // Carrega o pássaro pelos nomes utilizados no projeto.
        if (Gdx.files.internal("passaro.png").exists()) {

            passaroTexture =
                    new Texture(Gdx.files.internal("passaro.png"));

        } else if (Gdx.files.internal("bucket.png").exists()) {

            passaroTexture =
                    new Texture(Gdx.files.internal("bucket.png"));

        } else {

            passaroTexture =
                    criarTexturaSolida(32, 24, Color.YELLOW);
        }

        // Carrega a textura dos obstáculos.
        if (Gdx.files.internal("pipe.png").exists()) {

            pipeTexture =
                    new Texture(Gdx.files.internal("pipe.png"));

        } else {

            pipeTexture =
                    criarTexturaSolida(1, 1, Color.GREEN);
        }

        gameWorld = new GameWorld(
                passaroTexture,
                pipeTexture,
                selectedDifficulty
        );

        inputHandler = new InputHandler(
                gameWorld.getPlayer(),
                gameWorld
        );
    }

    private Texture criarTexturaSolida(
            int width,
            int height,
            Color color) {

        Pixmap pixmap = new Pixmap(
                width,
                height,
                Pixmap.Format.RGBA8888
        );

        pixmap.setColor(color);
        pixmap.fill();

        Texture texture = new Texture(pixmap);

        pixmap.dispose();

        return texture;
    }

    @Override
    public void render() {

        float delta = Gdx.graphics.getDeltaTime();

        handleScreenInput();

        // Desenha o menu antes de atualizar qualquer partida.
        if (screenState == ScreenState.MENU) {

            camera.update();

            ScreenUtils.clear(
                    0.08f, 0.10f, 0.16f, 1f
            );

            batch.setProjectionMatrix(camera.combined);

            shapeRenderer.setProjectionMatrix(camera.combined);

            renderMenu();

            return;
        }

        // Controla a contagem regressiva.
        if (screenState == ScreenState.COUNTDOWN) {

            countdownElapsed += delta;

            if (countdownElapsed >= 4f) {
                screenState = ScreenState.PLAYING;
            }

        } else if (screenState == ScreenState.PLAYING) {

            inputHandler.handleInput();

            gameWorld.update(delta);

            if (gameWorld.isGameOver()) {
                screenState = ScreenState.GAME_OVER;
            }
        }

        camera.update();

        ScreenUtils.clear(
                0.08f, 0.10f, 0.16f, 1f
        );

        batch.setProjectionMatrix(camera.combined);

        shapeRenderer.setProjectionMatrix(camera.combined);

        batch.begin();

        gameWorld.render(batch);

        if (screenState == ScreenState.PLAYING
                || screenState == ScreenState.COUNTDOWN) {

            font.draw(
                    batch,
                    "Pontos: " + (gameWorld.getScore() / 2),
                    20,
                    460
            );

            font.draw(
                    batch,
                    "Nivel: " + selectedDifficulty.getDisplayName(),
                    650,
                    460
            );
        }

        batch.end();

        switch (screenState) {

            case COUNTDOWN:
                renderCountdown();
                break;

            case GAME_OVER:
                renderGameOver();
                break;

            case MENU:
                break;

            case PLAYING:
                break;
        }
    }

    private void handleScreenInput() {

        // Escolha de dificuldade no menu inicial.
        if (screenState == ScreenState.MENU
                && Gdx.input.justTouched()) {

            Vector3 point = getTouchPoint();

            if (easyButton.contains(point.x, point.y)) {

                beginCountdown(Difficulty.FACIL);

            } else if (mediumButton.contains(point.x, point.y)) {

                beginCountdown(Difficulty.MEDIO);

            } else if (hardButton.contains(point.x, point.y)) {

                beginCountdown(Difficulty.DIFICIL);
            }

        } else if (screenState == ScreenState.GAME_OVER) {

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {

                beginCountdown(selectedDifficulty);

            } else if (Gdx.input.justTouched()) {

                Vector3 point = getTouchPoint();

                if (replayButton.contains(point.x, point.y)) {

                    beginCountdown(selectedDifficulty);

                } else if (menuButton.contains(point.x, point.y)) {

                    screenState = ScreenState.MENU;
                }
            }
        }
    }

    private Vector3 getTouchPoint() {

        touchPoint.set(
                Gdx.input.getX(),
                Gdx.input.getY(),
                0
        );

        camera.unproject(touchPoint);

        return touchPoint;
    }

    private void beginCountdown(Difficulty difficulty) {

        selectedDifficulty = difficulty;

        gameWorld.startNewGame(selectedDifficulty);

        countdownElapsed = 0f;

        screenState = ScreenState.COUNTDOWN;
    }

    private void renderMenu() {

        shapeRenderer.begin(
                ShapeRenderer.ShapeType.Filled
        );

        drawButton(
                easyButton,
                new Color(0.16f, 0.62f, 0.27f, 1f)
        );

        drawButton(
                mediumButton,
                new Color(0.91f, 0.58f, 0.13f, 1f)
        );

        drawButton(
                hardButton,
                new Color(0.78f, 0.20f, 0.18f, 1f)
        );

        shapeRenderer.end();

        batch.begin();

        drawCentered(
                largeFont,
                "FLAPPY GAME",
                400,
                405
        );

        drawCentered(
                font,
                "ESCOLHA A DIFICULDADE",
                400,
                350
        );

        drawButtonLabel(easyButton, "FACIL");

        drawButtonLabel(mediumButton, "MEDIO");

        drawButtonLabel(hardButton, "DIFICIL");

        batch.end();
    }

    private void renderCountdown() {

        String countdownText;

        if (countdownElapsed < 1f) {

            countdownText = "3";

        } else if (countdownElapsed < 2f) {

            countdownText = "2";

        } else if (countdownElapsed < 3f) {

            countdownText = "1";

        } else {

            countdownText = "GO!";
        }

        batch.begin();

        drawCentered(
                largeFont,
                countdownText,
                400,
                270
        );

        batch.end();
    }

    private void renderGameOver() {

        shapeRenderer.begin(
                ShapeRenderer.ShapeType.Filled
        );

        drawButton(
                replayButton,
                new Color(0.16f, 0.48f, 0.75f, 1f)
        );

        drawButton(
                menuButton,
                new Color(0.30f, 0.34f, 0.40f, 1f)
        );

        shapeRenderer.end();

        batch.begin();

        drawCentered(
                largeFont,
                "GAME OVER",
                400,
                330
        );

        drawCentered(
                font,
                "Pontos: " + (gameWorld.getScore() / 2),
                400,
                285
        );

        drawCentered(
                font,
                "Nivel: " + selectedDifficulty.getDisplayName(),
                400,
                255
        );

        drawButtonLabel(
                replayButton,
                "JOGAR NOVAMENTE"
        );

        drawButtonLabel(
                menuButton,
                "MENU"
        );

        batch.end();
    }

    private void drawButton(
            Rectangle rectangle,
            Color color) {

        shapeRenderer.setColor(color);

        shapeRenderer.rect(
                rectangle.x,
                rectangle.y,
                rectangle.width,
                rectangle.height
        );
    }

    private void drawButtonLabel(
            Rectangle rectangle,
            String label) {

        layout.setText(font, label);

        float x = rectangle.x
                + (rectangle.width - layout.width) / 2f;

        float y = rectangle.y
                + (rectangle.height + layout.height) / 2f;

        font.draw(batch, layout, x, y);
    }

    private void drawCentered(
            BitmapFont drawFont,
            String text,
            float centerX,
            float y) {

        layout.setText(drawFont, text);

        drawFont.draw(
                batch,
                layout,
                centerX - layout.width / 2f,
                y
        );
    }

    @Override
    public void dispose() {

        if (batch != null) batch.dispose();

        if (shapeRenderer != null) shapeRenderer.dispose();

        if (font != null) font.dispose();

        if (largeFont != null) largeFont.dispose();

        if (passaroTexture != null) passaroTexture.dispose();

        if (pipeTexture != null) pipeTexture.dispose();
    }
}