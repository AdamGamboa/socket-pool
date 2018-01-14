package com.orthanc.commons.pool.socket;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 *
 * @author Adam M. Gamboa G
 */
public class SocketHelper {
    
    private static final Logger LOGGER = Logger.getLogger(SocketHelper.class.getName());
    
    private final static Map<String,SocketHelper> socketPools = new HashMap<>();
    
    
    private final SocketPool socketPool;
    
    /**
     * Constructor privado para ocultarlo
     */
    private SocketHelper(SocketPool socketPool){
        this.socketPool = socketPool;
    }
    
    //<<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>>
    //<<>><<>><<>><<>><<>> METODOS SINGLETON  <<>><<>><<>><<>><<>><<>><<>><<>>
    //<<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>>
    
    /**
     * Método encargado de obtener una instancia unica en la cual se encuentra
     * el pool de clients sockets.
     * 
     * @param host Host del socket a conectarse
     * @param port Puerto del socket a conectarse
     * @return Instancia unica del helper 
     */
    public static SocketHelper getInstance(String host, int port){
        String key = host+":"+port;
        if(!socketPools.containsKey(key)){
            synchronized(SocketHelper.class){
                SocketFactory factory = new SocketFactory(host, port);
                GenericObjectPoolConfig config = getDefaultConfig();
                SocketPool newPool = new SocketPool(factory, config);
                try {
                    newPool.preparePool();
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, "Error tratanto de inicializar el pool con el minimo establecido", ex);
                }
                SocketHelper newInstance = new SocketHelper(newPool);
                socketPools.put(key, newInstance);
            }
        }
        return socketPools.get(key);
    }
    
    public void shutDown(){
        String key = ((SocketFactory)socketPool.getFactory()).getHost()+":"+
                ((SocketFactory)socketPool.getFactory()).getPort();
        socketPools.remove(key);
    }
    
    /**
     * Genera un objeto con la configuración por defecto que se desea para los 
     * pools de sockets generados
     * @return Configuración
     */
    private static GenericObjectPoolConfig getDefaultConfig(){
        GenericObjectPoolConfig defaultConfig = new GenericObjectPoolConfig();
        //Setear aqui la configuración por defecto que se desea
        return defaultConfig;
    }
    
    
    //<<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>>
    //<<>><<>><<>><<>><<>> METODOS HELPER  <<>><<>><<>><<>><<>><<>><<>><<>>
    //<<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>>
    
    /**
     * Obtiene una instancia de un cliente de socket.
     * @return Instancia del cliente de socket.
     */
    public SocketClient getSocket(){
        try {
            return this.socketPool.borrowObject();
        } catch (Exception ex) {
            Logger.getLogger(SocketHelper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /**
     * Regresa la instancia de cliente de socket al pool.
     * 
     * @param socketClient Instancia a ser devuelta.
     */
    public void returnSocket(SocketClient socketClient){
        this.socketPool.returnObject(socketClient); 
    }
    
    /**
     * Setea valores de configuración en el pool.
     * @param config Propiedades de configuración
     */
    public void setConfiguration(GenericObjectPoolConfig config){
        this.socketPool.setConfig(config);
        try{
            this.socketPool.preparePool();
        }catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error tratanto de resetear el pool", ex);
        }
    }
    
    /**
     * Registra en el log el estado actual del pool.
     */
    public void logStatus(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n-------------------------------------").append("\n")
            .append("[Tamano Máximo:").append(this.socketPool.getMaxTotal()).append("]")
            .append("[Tamano Minimo:").append(this.socketPool.getMinIdle()).append("]")
            .append("[Instancias utilizadas:").append(this.socketPool.getNumActive()).append("]")
            .append("[Instancias no utilizadas: ").append(this.socketPool.getNumIdle()).append("]")
            .append("[Total Instancias: ").append(this.socketPool.getNumIdle()+this.socketPool.getNumActive()).append("]")
            .append("[Peticiones en cola: ").append(this.socketPool.getNumWaiters()).append("]")
            .append("\n-------------------------------------").append("\n");
        LOGGER.log(Level.INFO, sb.toString());
    }
    
    
    /**
     * Las caracteristicas son configuraciones o estados actuales del pool.
     * 
     * @param feature Caracteristica del pool a consultar
     * @return Valor encontrado
     */
    public int getFeature(StatusPoolFeature feature){
        switch(feature){
            case NUM_ACTIVE:
                return this.socketPool.getNumActive();
            case NUM_IDLE:
                return this.socketPool.getNumIdle();
            case NUM_WAITERS:
                return this.socketPool.getNumWaiters();
            case MAX_TOTAL:
                return this.socketPool.getMaxTotal();
        }
        return -1;
    }
    
    /**
     * Enumerado con las carateristicas del Pool que se permiten consultar
     */
    public enum StatusPoolFeature{
        NUM_ACTIVE,
        NUM_IDLE,
        NUM_WAITERS,
        MAX_TOTAL;
    }
    
}
