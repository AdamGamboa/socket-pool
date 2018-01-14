
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
 *
 * @author Adam M. Gamboa G
 */
public class TestUsoMultiple extends AbstractTestBase{
    
    private final int TAMANO_MAXIMO = 5;
    private final int TAMANO_MINIMO = 3;
    
    @Before
    public void setUp(){
        helper.logStatus();
        
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(TAMANO_MAXIMO);
        config.setMinIdle(TAMANO_MINIMO);
        helper.setConfiguration(config);
        
        helper.logStatus();
        Assert.assertEquals(TAMANO_MAXIMO, helper.getFeature(SocketHelper.StatusPoolFeature.MAX_TOTAL));
        Assert.assertEquals(TAMANO_MINIMO, helper.getFeature(SocketHelper.StatusPoolFeature.NUM_IDLE));
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
                SocketClient client = helper.getSocket();
                clientes.add(client);//Debe quedar a la espera
                client.execute(TestUsoMultiple.super.getDummyMessage());
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                SocketClient client = helper.getSocket();
                clientes.add(client);//Debe quedar a la espera
                client.execute(TestUsoMultiple.super.getDummyMessage());
            }
        });
        t1.start();
        t2.start();
        
        //Uso los clientes para enviar mensajes
        for(SocketClient client: clientes){
            client.execute(super.getDummyMessage());
        }
        
        //Devuelvo varios para dar chance a los que estan esperando en los hilos
        for(int i=0; i<2;i++){
            helper.returnSocket(clientes.remove(0));
        }
        
        //Espero a que se ejecuten los hilos
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException ex) {
        }
        
        //Uso los clientes para enviar mensajes
        for(SocketClient client: clientes){
            client.execute(super.getDummyMessage());
        }
        
        //Devuelvo todos al pool y vuelvo a solicitar
        int temp = clientes.size();
        for(int i=0; i<temp;i++){
            helper.returnSocket(clientes.remove(0));
        }
        for(int i=0;i<TAMANO_MAXIMO; i++){
            SocketClient client = helper.getSocket();
            clientes.add(client);
            
            client.execute(super.getDummyMessage());
        }
        
        int temp2 = clientes.size();
        for(int i=0; i<temp2;i++){
            helper.returnSocket(clientes.remove(0));
        }
        
    }
    
}
