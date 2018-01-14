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
public class TestMinimoConexiones extends AbstractTestBase{
    
    private final int CANTIDAD_MINIMA = 3;
    
    @Before
    public void setUp(){
        helper.logStatus();
        
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMinIdle(CANTIDAD_MINIMA);
        helper.setConfiguration(config);
        
        helper.logStatus();
        Assert.assertEquals(CANTIDAD_MINIMA, helper.getFeature(SocketHelper.StatusPoolFeature.NUM_IDLE));
    }
    
    @Test
    public void test(){
        Assert.assertEquals(CANTIDAD_MINIMA, helper.getFeature(SocketHelper.StatusPoolFeature.NUM_IDLE));
        
        //Solicitar la cantidad minima de sockets en pila
        List<SocketClient> clientes = new ArrayList<>();
        for(int i=0;i<CANTIDAD_MINIMA; i++){
            clientes.add(helper.getSocket());
        }
        
        helper.logStatus();
        Assert.assertEquals(clientes.size(), helper.getFeature(SocketHelper.StatusPoolFeature.NUM_ACTIVE));
        Assert.assertEquals(0, helper.getFeature(SocketHelper.StatusPoolFeature.NUM_IDLE));
        
        //Solicitar un socket mas
        clientes.add(helper.getSocket());
        helper.logStatus();
        Assert.assertEquals(clientes.size(), helper.getFeature(SocketHelper.StatusPoolFeature.NUM_ACTIVE));
        Assert.assertEquals(0, helper.getFeature(SocketHelper.StatusPoolFeature.NUM_IDLE));
        
        clientes.add(helper.getSocket());
        helper.logStatus();
        Assert.assertEquals(clientes.size(), helper.getFeature(SocketHelper.StatusPoolFeature.NUM_ACTIVE));
        Assert.assertEquals(0, helper.getFeature(SocketHelper.StatusPoolFeature.NUM_IDLE));
        
        //Empezar a regresar al socket
        //Solicitar la cantidad minima de sockets en pila
        int cantidad = clientes.size();
        for(int i=0; i<cantidad; i++){
            helper.returnSocket(clientes.remove(0));
        }
        helper.logStatus();
        Assert.assertEquals(0, helper.getFeature(SocketHelper.StatusPoolFeature.NUM_ACTIVE));
        Assert.assertEquals(cantidad, helper.getFeature(SocketHelper.StatusPoolFeature.NUM_IDLE));
    }
}
