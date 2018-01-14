package com.orthanc.commons.pool.socket;

import com.orthanc.commons.pool.socket.SocketHelper;
import com.orthanc.commons.pool.socket.SocketClient;
import static com.orthanc.commons.pool.socket.AbstractTestBase.helper;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Testear el comportamiento del pool al alcanzar el maximo de conexiones disponibles
 * @author Adam M. Gamboa G
 */
public class TestMaximoConexiones extends AbstractTestBase{
    
    private final int TAMANO_MAXIMO = 10;
    
    @Before
    public void setUp(){
        helper.logStatus();
        
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(TAMANO_MAXIMO);
        helper.setConfiguration(config);
        
        helper.logStatus();
        Assert.assertEquals(TAMANO_MAXIMO, helper.getFeature(SocketHelper.StatusPoolFeature.MAX_TOTAL));
    }
    
    @Test
    public void test(){
        List<SocketClient> clientes = new ArrayList<>();
        for(int i=0;i<TAMANO_MAXIMO; i++){
            clientes.add(helper.getSocket());
        }
        
        helper.logStatus();
        Assert.assertEquals(TAMANO_MAXIMO, helper.getFeature(SocketHelper.StatusPoolFeature.MAX_TOTAL));
        Assert.assertEquals(TAMANO_MAXIMO, helper.getFeature(SocketHelper.StatusPoolFeature.NUM_ACTIVE));
        Assert.assertEquals(0, helper.getFeature(SocketHelper.StatusPoolFeature.NUM_IDLE));
        Assert.assertEquals(0, helper.getFeature(SocketHelper.StatusPoolFeature.NUM_WAITERS));
        
        //Agregar algunas peticiones extra
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                clientes.add(helper.getSocket());//Debe quedar a la espera
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                clientes.add(helper.getSocket());//Debe quedar a la espera
            }
        });
        t1.start();
        t2.start();
        
        try {
            Thread.sleep(100);//Esperando unos milisegundos para que los hilos corran
        } catch (InterruptedException ex) {}
        
        helper.logStatus();
        Assert.assertEquals(TAMANO_MAXIMO, helper.getFeature(SocketHelper.StatusPoolFeature.MAX_TOTAL));
        Assert.assertEquals(TAMANO_MAXIMO, helper.getFeature(SocketHelper.StatusPoolFeature.NUM_ACTIVE));
        Assert.assertEquals(0, helper.getFeature(SocketHelper.StatusPoolFeature.NUM_IDLE));
        Assert.assertTrue(helper.getFeature(SocketHelper.StatusPoolFeature.NUM_WAITERS)>0);
        
        
        //Devolver algunos elementos, para que procedan los que estan a la espera
        for(int i=0; i<3;i++){
            helper.returnSocket(clientes.remove(0));
        }
        //Ahora no deberian de haber elementos esperando
        helper.logStatus();
        Assert.assertEquals(TAMANO_MAXIMO, helper.getFeature(SocketHelper.StatusPoolFeature.MAX_TOTAL));
        Assert.assertEquals(0,helper.getFeature(SocketHelper.StatusPoolFeature.NUM_WAITERS));
    }
}
