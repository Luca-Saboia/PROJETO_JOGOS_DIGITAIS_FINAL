package br.mackenzie;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameWorld {
    private Player player;
    
    // Textura do plano de fundo
    private Texture backgroundTexture;

    public GameWorld() {
        player = new Player(33, 200, 34, 24);
        
        // Carrega a imagem de fundo da pasta assets
        this.backgroundTexture = new Texture("flappybirdbg.png");
    }

    public void update(float delta) {
        player.update(delta);
        // Atualizações gerais do mundo do jogo
    }

    public void render(SpriteBatch batch) {
        // Desenha o fundo cobrindo a tela do jogo (ajuste as dimensões conforme a resolução desejada)
        batch.draw(backgroundTexture, 0, 0, 800, 480);
        
        // Desenha o jogador por cima do fundo
        player.render(batch);
    }

    public void dispose() {
        // Libera os recursos de textura
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
        player.dispose();
    }

    public Player getPlayer() {
        return player;
    }
}