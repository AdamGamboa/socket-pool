package com.orthanc.commons.pool.socket.mock;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adam M. Gamboa G.
 */
public class SocketRequest implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(SocketRequest.class.getName());
    /**
     * It stores the socket with the request to process
     */
    private Socket socket;
    private BufferedReader input;
    private BufferedWriter output;

    /**
     * Private to hide it
     */
    private SocketRequest() {
    }

    /**
     * Constructor
     *
     * @param socket Socket with the request
     */
    public SocketRequest(Socket socket) {
        this.socket = socket;
        
        try {
            if(socket != null){
                this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                this.output = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "SOCKET SERVER: Error.",ex);
        }
    }

    /**
     * Processes the request.
     */
    @Override
    public void run() {
        try {
            while(this.socket.isConnected()){
                //Recibir mensaje
                LOGGER.log(Level.FINEST, "SOCKET SERVER: recibiendo mensaje");
                String line = this.input.readLine();

                //Procesar mensaje
                LOGGER.log(Level.INFO, "SOCKET SERVER: Mensaje recibido. {0}", line);

                //Retornar respuesta
                String mensajeRespuesta = Parametros.INICIO_TRAMA+"11010101Prueba de trama       "+
                        "01/01/20170001000348539MasPrueba "+new Date().toString()+"holamundo";
                this.output.write(mensajeRespuesta);
                this.output.newLine();//Cambio de linea para indicar mensaje ha terminado
                this.output.flush();
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "SOCKET SERVER: Error.",ex);
        } catch(Exception ex){
            LOGGER.log(Level.SEVERE, "SOCKET SERVER: Error.",ex);
        } finally {
            this.closeSocket();
        }
    }

    /**
     * Closes the Socket connection
     */
    private void closeSocket() {
        try {
            this.socket.close();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "SOCKET SERVER: Error cerrando el socket server", ex);
        }
    }

}

