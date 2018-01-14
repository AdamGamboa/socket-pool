package com.orthanc.commons.pool.socket;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;



/**
 * Clase encargada de inicializar, destruir o validar una conexión de Socket 
 * en el pool.
 * 
 * @author Adam M. Gamboa G
 */
public class SocketFactory implements PooledObjectFactory <SocketClient> {
    
    private final String host;
    private final int port;
    
    //<<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>>
    //<<>><<>><<>><<>><<>>  Constructor   <<>><<>><<>><<>><<>><<>><<>><<>><<>>
    //<<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>>
    
    /**
     * Constructor del factory 
     * @param host 
     * @param port 
     */
    public SocketFactory(String host, int port){
        this.host = host;
        this.port = port;
    }
    
    //<<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>>
    //<<>><<>><<>><<>><<>><<>>  METODOS   <<>><<>><<>><<>><<>><<>><<>><<>><<>>
    //<<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>>
    
    private SocketClient create() throws Exception {
        SocketClient socket = new SocketClient(this.host, this.port);
        return socket;
    }

    private PooledObject<SocketClient> wrap(SocketClient t) {
        return new DefaultPooledObject<>(t);
    }

    @Override
    public PooledObject<SocketClient> makeObject() throws Exception {
        return this.wrap(this.create());
    }

    @Override
    public void destroyObject(PooledObject<SocketClient> p) throws Exception {
        p.getObject().close();
    }

    @Override
    public boolean validateObject(PooledObject<SocketClient> p) {
        return p.getObject().isValid();
    }

    @Override
    public void activateObject(PooledObject<SocketClient> p) throws Exception {
        p.getObject().activate();
    }

    @Override
    public void passivateObject(PooledObject<SocketClient> p) throws Exception {
        p.getObject().desactivate();
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
    
    

}
