package br.mackenzie.lwjgl3;

import br.mackenzie.ArduinoInput; // Importa a interface do core
import com.fazecast.jSerialComm.SerialPort;

public class ArduinoController implements ArduinoInput {
    private SerialPort port;
    private String portaConfigurada;

    public ArduinoController(String portaConfigurada) {
        this.portaConfigurada = portaConfigurada;
    }

    @Override
    public void conectar() {
        port = SerialPort.getCommPort(portaConfigurada);
        port.setBaudRate(9600);
        port.setNumDataBits(8);
        port.setNumStopBits(SerialPort.ONE_STOP_BIT);
        port.setParity(SerialPort.NO_PARITY);
        port.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
        
        // A mágica não-bloqueante
        port.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);

        if (port.openPort()) {
            System.out.println("Porta " + portaConfigurada + " aberta com sucesso!");
            try { Thread.sleep(2000); } catch (Exception e) {}
        } else {
            System.err.println("ERRO: Não abriu a porta " + portaConfigurada);
        }
    }

    @Override
    public boolean verificarPulo() {
        if (port != null && port.isOpen() && port.bytesAvailable() > 0) {
            byte[] readBuffer = new byte[port.bytesAvailable()];
            port.readBytes(readBuffer, readBuffer.length);
            String entrada = new String(readBuffer).trim();

            if (!entrada.isEmpty()) {
                // System.out.println("Sinal: " + entrada);
                return true; 
            }
        }
        return false;
    }

    @Override
    public void desconectar() {
        if (port != null && port.isOpen()) {
            port.closePort();
            System.out.println("Porta fechada.");
        }
    }
}