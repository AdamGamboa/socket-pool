
package com.orthanc.commons.pool.socket.mock;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adam M. Gamboa G
 */
public class SocketServerHelper {
    
    private static final Logger LOGGER = Logger.getLogger(SocketServerHelper.class.getName());

    private static SocketServerHelper instance;
    
    private ServerSocket serverSocket;
    private ExecutorService executor;
    private boolean serverOn;
    
    /**
     * Private to hide it
     */
    private SocketServerHelper() {
    }

    /**
     * Gets the only instance in the application for this class.
     * @return Instance of DSignListener
     */
    public static SocketServerHelper getInstance() {
        if (instance == null) {
            initialize();
        }else if(instance != null && !instance.serverOn){
            initialize();
        }
        return instance;
    }

    /**
     * Initializes the listener, it tries to create a new socket server for listen to.
     * throws ServerSocketException if the socket server is not created
     */
    private synchronized static void initialize() {
        try {
            instance = new SocketServerHelper();
            int port = Parametros.PUERTO;
            instance.serverSocket = new ServerSocket(port);
            instance.executor = Executors.newSingleThreadExecutor();
            instance.serverOn = true;
        } catch (IOException ioex) {
            LOGGER.log(Level.SEVERE, "No se ha iniciado el socket server", ioex);
            throw new RuntimeException("No se ha iniciado el socket server");
        }
    }
    
    /**
     * It starts the listener. Put the server socket to listen in a new Thread for
     * incoming requests.
     */
    public void start() {
        LOGGER.log(Level.INFO, "Iniciando el socket server");
        
        executor.submit(() -> {
            // create an open ended thread-pool
            ExecutorService threadPool = Executors.newCachedThreadPool();
            try {
                LOGGER.log(Level.INFO, "Socket server iniciado....");
                while (serverOn) {
                    // wait for a client to connect
                    Socket clientSocket = serverSocket.accept();
                    // create a new Service Request object for that socket, and fork it in a background thread
                    threadPool.submit(new SocketRequest(clientSocket));
                }
            } catch (IOException ex) {
                LOGGER.log(Level.INFO, "Socket Server Detenido");
            }finally {
                // we _have_ to shutdown the thread-pool when we are done
                threadPool.shutdown();
            }
        });
    }
    
    /**
     * Shutdowns the Listener. Also the socket server in the listener is closed.
     */
    public void shutdown() {
        LOGGER.log(Level.INFO, "Deteniendo el socket server ...");
        try {
            serverSocket.close();
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Error terminando el multihilo del socket server. Procesos no terminados", e);
        } catch (IOException ex) {
             LOGGER.log(Level.INFO, "Error terminando el multihilo del socket server.", ex);
        } finally {
            executor.shutdownNow();
            serverOn = false;
        }
        LOGGER.log(Level.INFO, "Socket server Detenido...");
    }
    
    public boolean isServerOn(){
        return this.serverOn;
    }
}
