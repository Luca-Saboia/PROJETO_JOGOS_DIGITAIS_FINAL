package br.mackenzie;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private BitmapFont font;

    private Texture passaroTexture;
    private Texture pipeTexture;
    private GameWorld gameWorld;
    private InputHandler inputHandler;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        font = new BitmapFont();

        // Tenta carregar imagem do pássaro ou gera bloco amarelo se não existir
        if (Gdx.files.internal("passaro.png").exists()) {
            passaroTexture = new Texture(Gdx.files.internal("passaro.png"));
        } else if (Gdx.files.internal("bucket.png").exists()) {
            passaroTexture = new Texture(Gdx.files.internal("bucket.png"));
        } else {
            passaroTexture = criarTexturaSolida(32, 24, Color.YELLOW);
        }

        // Tenta carregar imagem do cano ou gera bloco verde se não existir
        if (Gdx.files.internal("pipe.png").exists()) {
            pipeTexture = new Texture(Gdx.files.internal("pipe.png"));
        } else {
            pipeTexture = criarTexturaSolida(1, 1, Color.GREEN);
        }

        gameWorld = new GameWorld(passaroTexture, pipeTexture);
        inputHandler = new InputHandler(gameWorld.getPlayer(), gameWorld);
    }

    private Texture criarTexturaSolida(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        inputHandler.handleInput();
        gameWorld.update(delta);

        camera.update();
        ScreenUtils.clear(0.3f, 0.6f, 0.9f, 1f);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        gameWorld.render(batch);

        font.draw(batch, "Pontos: " + (gameWorld.getScore() / 2), 20, 460);

        if (gameWorld.isGameOver()) {
            font.draw(batch, "GAME OVER!", 360, 260);
            font.draw(batch, "Pressione ESPACO ou clique para reiniciar", 260, 230);
        }

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        passaroTexture.dispose();
        pipeTexture.dispose();
    }
}