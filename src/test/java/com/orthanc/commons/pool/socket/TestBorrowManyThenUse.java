package com.orthanc.commons.pool.socket;

import com.orthanc.commons.pool.socket.SocketHelper;
import com.orthanc.commons.pool.socket.SocketClient;
import com.orthanc.commons.pool.socket.mock.Parametros;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Escenario donde se solicitan varias conexiones de socket al pool, y luego se
 * utilizan. Finalmente se regresan las conexiones al pool. 
 * El pool debe de incrementarse cada vez que se pide una conexion.
 * 
 * @author Adam M. Gamboa G.
 */
public class TestBorrowManyThenUse extends AbstractTestBase{
    
    @Test
    public void testPedirSocketsLuegoEnviar(){
        logHeaderTestMethod("testPedirSocketsLuegoEnviar()");
        //Solicito primero los sockets
        SocketClient socketTask = helper.getSocket();
        helper.logStatus();
        assertNotNull(socketTask);
        assertEquals(1, helper.getFeature(SocketHelper.StatusPoolFeature.NUM_ACTIVE));
        
        SocketClient socketTask2 = helper.getSocket();
        helper.logStatus();
        assertNotNull(socketTask2);
        assertEquals(2, helper.getFeature(SocketHelper.StatusPoolFeature.NUM_ACTIVE));
        
        SocketClient socketTask3 = helper.getSocket();
        helper.logStatus();
        assertNotNull(socketTask3);
        assertEquals(3, helper.getFeature(SocketHelper.StatusPoolFeature.NUM_ACTIVE));
        
        //Luego los ejecuto
        byte[] result = socketTask.execute(super.getDummyMessage());
        assertNotNull(result);
        String resultStr = new String(result);
        assertTrue(resultStr.startsWith(Parametros.INICIO_TRAMA));
        
        
        byte[] result2 = socketTask2.execute(super.getDummyMessage());
        assertNotNull(result2);
        String resultStr2 = new String(result2);
        assertTrue(resultStr2.startsWith(Parametros.INICIO_TRAMA));
        
        
        byte[] result3 = socketTask3.execute(super.getDummyMessage());
        assertNotNull(result3);
        String resultStr3 = new String(result3);
        assertTrue(resultStr3.startsWith(Parametros.INICIO_TRAMA));
        
        helper.logStatus();
        
        //Luego los regreso
        helper.returnSocket(socketTask);
        helper.logStatus();
        helper.returnSocket(socketTask2);
        helper.logStatus();
        helper.returnSocket(socketTask3);
        helper.logStatus();
        assertEquals(0, helper.getFeature(SocketHelper.StatusPoolFeature.NUM_ACTIVE));
        assertEquals(3, helper.getFeature(SocketHelper.StatusPoolFeature.NUM_IDLE));
    }
}
