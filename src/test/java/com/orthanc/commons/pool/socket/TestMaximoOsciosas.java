package com.orthanc.commons.pool.socket;

import com.orthanc.commons.pool.socket.SocketHelper;
import com.orthanc.commons.pool.socket.SocketClient;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Adam M. Gamboa G
 */
public class TestMaximoOsciosas extends AbstractTestBase{
    
    private final int CANTIDAD_MINIMA = 3; //Inicio del POOL
    private final int CANTIDAD_OSCIOSA_MAX = 4; //Maximo oscioso
    private final int CANTIDAD_SOLICITAR = 6;
    
    @Before
    public void setUp(){
        helper.logStatus();
        
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMinIdle(CANTIDAD_MINIMA);
        config.setMaxIdle(CANTIDAD_OSCIOSA_MAX);
        helper.setConfiguration(config);
        
        helper.logStatus();
        Assert.assertEquals(CANTIDAD_MINIMA, helper.getFeature(SocketHelper.StatusPoolFeature.NUM_IDLE));
    }
    
    @Test
    public void test(){
        Assert.assertEquals(CANTIDAD_MINIMA, helper.getFeature(SocketHelper.StatusPoolFeature.NUM_IDLE));
        
        //Solicitar la cantidad minima de sockets en pila
        List<SocketClient> clientes = new ArrayList<>();
        for(int i=0;i<CANTIDAD_SOLICITAR; i++){
            clientes.add(helper.getSocket());
        }
        
        helper.logStatus();
        Assert.assertEquals(CANTIDAD_SOLICITAR, helper.getFeature(SocketHelper.StatusPoolFeature.NUM_ACTIVE));
        Assert.assertEquals(0, helper.getFeature(SocketHelper.StatusPoolFeature.NUM_IDLE));
        
        //Empezar a regresar al socket, sin embargo se van a devolver mas de la 
        //cantidad osciosa permitida, por lo cual la cantidad osciosa no debe
        //pasar ese umbral y el pool va a tener menos instancias.
        int cantidad = clientes.size();
        for(int i=0; i<cantidad; i++){
            helper.returnSocket(clientes.remove(0));
            Assert.assertTrue(helper.getFeature(SocketHelper.StatusPoolFeature.NUM_IDLE) <= CANTIDAD_OSCIOSA_MAX);
        }
        helper.logStatus();
        Assert.assertEquals(0, helper.getFeature(SocketHelper.StatusPoolFeature.NUM_ACTIVE));
        Assert.assertEquals(CANTIDAD_OSCIOSA_MAX, helper.getFeature(SocketHelper.StatusPoolFeature.NUM_IDLE));
    }
}
