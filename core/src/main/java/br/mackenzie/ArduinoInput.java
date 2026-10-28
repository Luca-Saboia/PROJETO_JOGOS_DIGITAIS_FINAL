package br.mackenzie;

/**
 * Abstração do controle externo que pode solicitar um pulo.
 * A implementação que acessa a porta serial fica no módulo desktop (lwjgl3).
 */
public interface ArduinoInput extends AutoCloseable {

    /** Inicia a leitura do dispositivo. Retorna false se não conseguir conectar. */
    boolean start();

    /** Libera a conexão e os recursos associados. */
    @Override
    void close();
}
