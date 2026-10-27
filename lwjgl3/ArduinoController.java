
package br.mackenzie;

import com.badlogic.gdx.Gdx;
import com.fazecast.jSerialComm.SerialPort;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ArduinoController implements AutoCloseable {

    private final String portName;
    private final Runnable onJump;

    private final AtomicBoolean running = new AtomicBoolean(false);

    private SerialPort serialPort;
    private Thread readerThread;

    public ArduinoController(String portName, Runnable onJump) {
        this.portName = portName;
        this.onJump = onJump;
    }

    public boolean start() {
        if (running.get()) {
            return true;
        }

        serialPort = SerialPort.getCommPort(portName);

        serialPort.setComPortParameters(
                9600,
                8,
                SerialPort.ONE_STOP_BIT,
                SerialPort.NO_PARITY
        );

        serialPort.setComPortTimeouts(
                SerialPort.TIMEOUT_READ_SEMI_BLOCKING,
                1000,
                0
        );

        if (!serialPort.openPort()) {
            Gdx.app.error(
                    "ArduinoController",
                    "Nao foi possivel abrir " + portName
                            + ". Feche o Monitor Serial do Arduino IDE."
            );

            serialPort = null;
            return false;
        }

        running.set(true);

        readerThread = new Thread(
                this::readLoop,
                "arduino-serial-reader"
        );

        readerThread.setDaemon(true);
        readerThread.start();

        Gdx.app.log(
                "ArduinoController",
                "Arduino conectado em " + portName
        );

        return true;
    }

    private void readLoop() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        serialPort.getInputStream(),
                        StandardCharsets.US_ASCII
                ))) {

            String line;

            while (running.get() && (line = reader.readLine()) != null) {
                if ("JUMP".equalsIgnoreCase(line.trim())) {
                    Gdx.app.postRunnable(onJump);
                }
            }

        } catch (Exception e) {
            if (running.get()) {
                Gdx.app.error(
                        "ArduinoController",
                        "Erro na comunicacao serial: " + e.getMessage()
                );
            }
        }
    }

    @Override
    public void close() {
        running.set(false);

        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
        }

        if (readerThread != null) {
            readerThread.interrupt();
        }

        serialPort = null;
    }
}
