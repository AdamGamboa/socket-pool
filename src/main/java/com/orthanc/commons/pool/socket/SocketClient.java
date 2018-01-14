package com.orthanc.commons.pool.socket;

import com.orthanc.commons.pool.socket.exceptions.SocketClientException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adam M. Gamboa G
 */
public class SocketClient {

    private static final Logger LOGGER = Logger.getLogger(SocketClient.class.getName());

    private Socket client;
    private BufferedReader input;
    private BufferedWriter output;
    
    private static final int TIMEOUT_IN_MS = 30 * 1000;   // 1 segundo;        

    public SocketClient(String host, int port) {
        this.create(host, port);
    }

    private void create(String host, int port) {
        // create a socket with a timeout
        try {
            SocketAddress socketAddress = new InetSocketAddress(host, port);
            // create a socket
            this.client = new Socket();

            // this method will block no more than timeout ms.
            this.client.connect(socketAddress, TIMEOUT_IN_MS);
            this.client.setSoTimeout(TIMEOUT_IN_MS);
            this.client.setTcpNoDelay(true);
            this.client.setKeepAlive(true);
            
            this.input = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
            this.output = new BufferedWriter(new OutputStreamWriter(this.client.getOutputStream()));
            this.output.flush();
        } catch (IOException ex) {
           LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
           throw new SocketClientException("No se ha podido crear una nueva conexión al Socket. Host:"+host+", puerto:"+port,
                   ex);
        }
    }

    /**
     * 
     * @param message Mensaje a enviar en el socket
     * @return Resultado obtenido. NULL si hay un error de conexión
     */
    public byte[] execute(byte[] message) {
        try{
            //Enviar el mensaje
            this.output.write(new String(message));
            this.output.newLine();
            this.output.flush();
            
            //Obtener la respuesta del inputStream del client socket
            //Debido a que es una llamada continua sin cerrar el socket, se debe 
            //tener una instruccion de cierre para un envio en este ejemplo es 
            //cambio de linea (el servidor socket mandará un cambio de linea para
            //indicar el mensaje a terminado)
            String line = input.readLine();
            return line.getBytes();
        }catch(IOException ioex){
            LOGGER.log(Level.SEVERE, "Error enviando mensaje al socket", ioex);
            throw new SocketClientException("Error enviando mensaje al socket",ioex);
        }
    }

    /**
     *
     */
    public void close() {
        if (this.client != null) {
            try {
                this.client.close();
            } catch (IOException ioex) {
                LOGGER.log(Level.WARNING, "El client de socket no se ha podido cerrar.", ioex);
            } finally {
                this.client = null;
            }
        }
    }

    /**
     *
     * @return
     */
    public boolean isValid() {
        if (this.client != null) {
            return this.client.isClosed();
        }
        return false;
    }

    public void activate() {
        LOGGER.log(Level.FINE, "... Activando socket ...");
    }

    public void desactivate() {
        LOGGER.log(Level.FINE, "... Desactivando socket ...");
    }

}
