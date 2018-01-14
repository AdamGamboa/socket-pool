package com.orthanc.commons.pool.socket;

import com.orthanc.commons.pool.socket.SocketClient;
import com.orthanc.commons.pool.socket.mock.Parametros;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Chequea que se pueda realizar una conexión sencilla y enviar un solo mensaje.
 * 
 * @author Adam M. Gamboa G.
 */
public class TestConexion extends AbstractTestBase{
    
    
    @Test
    public void testSendMessage(){
        logHeaderTestMethod("testSendMessage()");
        
        SocketClient socketTask = helper.getSocket();
        helper.logStatus();
        assertNotNull(socketTask);
        byte[] result = socketTask.execute(super.getDummyMessage());
        assertNotNull(result);
        String resultStr = new String(result);
        assertTrue(resultStr.startsWith(Parametros.INICIO_TRAMA));
        helper.returnSocket(socketTask);
        helper.logStatus();
    }
}
