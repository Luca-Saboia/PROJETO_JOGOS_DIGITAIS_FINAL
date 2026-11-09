package br.mackenzie.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import br.mackenzie.Main;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Flappy Fisio");
        config.setWindowedMode(800, 480);
        
        Main game = new Main();
        
        // Cria o controlador com a porta correta (COM5) e injeta no jogo!
        ArduinoController arduino = new ArduinoController("COM5");
        game.setArduinoController(arduino);
        
        new Lwjgl3Application(game, config);
    }
}