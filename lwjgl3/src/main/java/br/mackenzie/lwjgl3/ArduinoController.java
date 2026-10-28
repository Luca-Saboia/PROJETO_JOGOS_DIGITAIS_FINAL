package br.mackenzie.lwjgl3;

import br.mackenzie.ArduinoInput;
import com.badlogic.gdx.Gdx;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.util.Locale;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Recebe comandos do Arduino usando eventos da serial em vez de readLine com timeout.
 * O callback de pulo sempre e encaminhado para a thread principal do libGDX.
 */
public final class ArduinoController implements ArduinoInput {

    private final String portName;
    private final Runnable onJump;
    private final StringBuilder lineBuffer = new StringBuilder();
    private final Object bufferLock = new Object();

    private volatile boolean running;
    private SerialPort serialPort;
    private SerialPortDataListener dataListener;

    public ArduinoController(String portName, Runnable onJump) {
        this.portName = portName;
        this.onJump = onJump;
    }

    @Override
    public synchronized boolean start() {
        if (running && serialPort != null && serialPort.isOpen()) {
            return true;
        }

        // Registra o que a biblioteca realmente enxerga. COM4 pode estar correto
        // no Gerenciador de Dispositivos, mas indisponivel para este processo.
        String detectedPorts = listDetectedPorts();
        Gdx.app.log("ArduinoController", "Porta solicitada: " + portName);
        Gdx.app.log("ArduinoController", "Portas seriais detectadas: " + detectedPorts);

        try {
            serialPort = SerialPort.getCommPort(portName);
            serialPort.setComPortParameters(
                    9600,
                    8,
                    SerialPort.ONE_STOP_BIT,
                    SerialPort.NO_PARITY
            );
            // Recepcao por eventos em modo nao bloqueante.
            serialPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);

            if (!serialPort.openPort()) {
                // Captura os codigos imediatamente, antes de qualquer outra chamada nativa.
                int errorCode = serialPort.getLastErrorCode();
                int errorLocation = serialPort.getLastErrorLocation();
                Gdx.app.error("ArduinoController",
                        describeOpenFailure(portName, errorCode, errorLocation, detectedPorts));
                serialPort = null;
                return false;
            }
        } catch (Exception e) {
            Gdx.app.error("ArduinoController",
                    "Excecao ao abrir " + portName + ": " + e.getClass().getSimpleName()
                            + ": " + e.getMessage()
                            + ". Portas detectadas: " + detectedPorts, e);
            serialPort = null;
            return false;
        }

        synchronized (bufferLock) {
            lineBuffer.setLength(0);
        }

        dataListener = new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_RECEIVED) {
                    return;
                }
                byte[] bytes = event.getReceivedData();
                if (bytes != null && bytes.length > 0 && running) {
                    acceptBytes(bytes);
                }
            }
        };

        running = true;
        serialPort.addDataListener(dataListener);

        Gdx.app.log("ArduinoController", "Arduino conectado em " + portName
                + " a 9600 baud. Aguardando linhas READY/FSR/JUMP.");
        return true;
    }

    private String listDetectedPorts() {
        try {
            SerialPort[] ports = SerialPort.getCommPorts();
            if (ports == null || ports.length == 0) {
                return "nenhuma porta detectada pela jSerialComm";
            }
            return Arrays.stream(ports)
                    .map(p -> p.getSystemPortName() + " (" + p.getDescriptivePortName() + ")")
                    .collect(Collectors.joining(", "));
        } catch (Exception e) {
            return "falha ao enumerar portas: " + e.getMessage();
        }
    }

    private String describeOpenFailure(String port, int code, int location, String detectedPorts) {
        String cause;
        switch (code) {
            case 2:
                cause = "Windows erro 2 (arquivo/dispositivo nao encontrado): a COM nao esta acessivel "
                        + "ao processo, apesar do nome configurado.";
                break;
            case 5:
                cause = "Windows erro 5 (acesso negado): a porta pode estar aberta por outro processo "
                        + "ou bloqueada pelo driver/permissao. O numero COM pode estar correto.";
                break;
            case 31:
                cause = "Windows erro 31 (falha geral do dispositivo): confira cabo USB, conexao fisica "
                        + "e driver USB-serial do Arduino.";
                break;
            default:
                cause = "A biblioteca/Windows recusou a abertura; o codigo nativo precisa ser analisado.";
                break;
        }
        return "Falha ao abrir " + port + ". Codigo nativo=" + code
                + ", local nativo=" + location + ". " + cause
                + " Portas detectadas pela biblioteca: " + detectedPorts;
    }

    private void acceptBytes(byte[] bytes) {
        synchronized (bufferLock) {
            for (byte b : bytes) {
                char c = (char) (b & 0xFF);
                if (c == '\n') {
                    String line = lineBuffer.toString().trim();
                    lineBuffer.setLength(0);
                    if (!line.isEmpty()) {
                        handleLine(line);
                    }
                } else if (c != '\r') {
                    if (lineBuffer.length() < 256) {
                        lineBuffer.append(c);
                    } else {
                        // Descarta linha corrompida/muito longa, sem travar o receptor.
                        lineBuffer.setLength(0);
                    }
                }
            }
        }
    }

    private void handleLine(String line) {
        String command = line.trim();
        String normalized = command.toUpperCase(Locale.ROOT);

        if (normalized.equals("READY") || normalized.startsWith("FSR:")
                || normalized.startsWith("BASELINE:")
                || normalized.startsWith("DELTA:")) {
            Gdx.app.log("ArduinoController", "Recebido: " + command);
            return;
        }

        // Aceita o comando esperado e aliases simples para sketches antigos.
        if (normalized.equals("JUMP") || normalized.equals("PULO") || normalized.equals("1")) {
            Gdx.app.log("ArduinoController", "COMANDO DE PULO RECEBIDO pela serial.");
            Gdx.app.postRunnable(onJump);
            return;
        }

        Gdx.app.log("ArduinoController", "Linha serial nao reconhecida: " + command);
    }

    @Override
    public synchronized void close() {
        running = false;
        if (serialPort != null) {
            try {
                if (dataListener != null) {
                    serialPort.removeDataListener();
                    dataListener = null;
                }
                if (serialPort.isOpen()) {
                    serialPort.closePort();
                }
            } catch (Exception e) {
                if (Gdx.app != null) {
                    Gdx.app.error("ArduinoController", "Erro fechando a porta serial: " + e.getMessage());
                }
            }
            serialPort = null;
        }
    }
}
