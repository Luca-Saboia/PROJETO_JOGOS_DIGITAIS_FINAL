
package br.mackenzie.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import br.mackenzie.Main;

public class Lwjgl3Launcher {

    public static void main(String[] args) {
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        Main game = new Main();

        // Porta padrao do Arduino. Altere se o Arduino IDE mostrar outra porta.
        String arduinoPort = System.getProperty("arduino.port", "COM4");
        ArduinoController arduinoController = new ArduinoController(
            arduinoPort,
            game::pularPorArduino
        );
        game.setArduinoController(arduinoController);

        return new Lwjgl3Application(
            game,
            getDefaultConfiguration()
        );
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration =
            new Lwjgl3ApplicationConfiguration();

        configuration.setTitle("Flappy Game");
        configuration.setWindowedMode(800, 480);
        configuration.useVsync(true);
        configuration.setForegroundFPS(60);

        return configuration;
    }
}
