package br.mackenzie;

public interface ArduinoInput {
    void conectar();
    boolean verificarPulo();
    void desconectar();
}