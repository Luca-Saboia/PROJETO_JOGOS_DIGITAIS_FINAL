package br.mackenzie;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private BitmapFont font;

    private Texture passaroTexture;
    private GameWorld gameWorld;
    private InputHandler inputHandler;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        font = new BitmapFont();

        // Carrega passaro.png ou usa bucket.png como alternativa se passaro.png não existir
        if (Gdx.files.internal("passaro.png").exists()) {
            passaroTexture = new Texture(Gdx.files.internal("passaro.png"));
        } else {
            passaroTexture = new Texture(Gdx.files.internal("bucket.png"));
        }

        gameWorld = new GameWorld(passaroTexture);
        inputHandler = new InputHandler(gameWorld.getPlayer(), gameWorld);
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
    }
}