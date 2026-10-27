package br.mackenzie;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Player {
    private Vector2 position;
    private Vector2 velocity;
    private int width, height;
    
    // Textura do pássaro
    private Texture texture;

    public Player(float x, float y, int width, int height) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2();
        this.width = width;
        this.height = height;
        
        // Carrega a imagem do passarinho da pasta assets
        this.texture = new Texture("flappybird.png");
    }

    public void update(float delta) {
        // Lógica de movimentação e física do jogador
    }

    public void render(SpriteBatch batch) {
        // Desenha a textura na posição atual do jogador
        batch.draw(texture, position.x, position.y, width, height);
    }

    public void dispose() {
        // Libera a memória da textura ao fechar a tela/jogo
        if (texture != null) {
            texture.dispose();
        }
    }

    public Vector2 getPosition() {
        return position;
    }
}