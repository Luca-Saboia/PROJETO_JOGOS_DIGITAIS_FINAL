package br.mackenzie.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import br.mackenzie.Main;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new Main(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
    Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
    configuration.setTitle("Jogo de Reabilitação");
    configuration.setWindowedMode(800, 480);
    configuration.useVsync(true);
    configuration.setForegroundFPS(60); // Corrigido de setFPSLimit para setForegroundFPS
    return configuration;
}
}