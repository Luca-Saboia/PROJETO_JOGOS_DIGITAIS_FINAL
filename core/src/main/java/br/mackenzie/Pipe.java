package br.mackenzie;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Pipe {
    private float x, y;
    private int width, height;
    
    // Textura do cano
    private Texture pipeTexture;

    public Pipe(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        
        // Carrega a imagem do cano da pasta assets
        this.pipeTexture = new Texture("toppipe.png");
    }

    public void update(float delta) {
        // Lógica de movimento do cano para a esquerda
    }

    public void render(SpriteBatch batch) {
        // Desenha o cano na tela
        batch.draw(pipeTexture, x, y, width, height);
    }

    public void dispose() {
        // Libera a memória da textura do cano
        if (pipeTexture != null) {
            pipeTexture.dispose();
        }
    }
}