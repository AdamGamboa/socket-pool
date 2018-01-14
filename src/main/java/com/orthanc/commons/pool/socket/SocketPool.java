package com.orthanc.commons.pool.socket;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;



/**
 *
 * @author Adam M. Gamboa 
 */
public class SocketPool extends GenericObjectPool<SocketClient> {
    
    /**
     * Constructor. Usa la configuración por defecto proporcionada
     * por apache-commons-pool
     * 
     * @param factory 
     */
    public SocketPool(PooledObjectFactory<SocketClient> factory) {
        super(factory);
    }
    
    /**
     * Constructor Usa la configuración especificada para la creación
     * del pool.
     * 
     * @param factory
     * @param config 
     */
    public SocketPool(PooledObjectFactory<SocketClient> factory,
            GenericObjectPoolConfig config) {
        super(factory, config);
    }
    
}
