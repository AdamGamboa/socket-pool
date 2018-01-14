package com.orthanc.commons.pool.socket;

import com.orthanc.commons.pool.socket.SocketHelper;
import com.orthanc.commons.pool.socket.SocketClient;
import static com.orthanc.commons.pool.socket.AbstractTestBase.helper;
import com.orthanc.commons.pool.socket.mock.Parametros;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Adam M. Gamboa G
 */
public class TestBorrowUseReturn extends AbstractTestBase {
    
    @Test
    public void test(){
        logHeaderTestMethod("test()");
        
        SocketClient socketTask = helper.getSocket();
        helper.logStatus();
        assertNotNull(socketTask);
        assertEquals(1, helper.getFeature(SocketHelper.StatusPoolFeature.NUM_ACTIVE));
        
        byte[] result = socketTask.execute(super.getDummyMessage());
        assertNotNull(result);
        String resultStr = new String(result);
        assertTrue(resultStr.startsWith(Parametros.INICIO_TRAMA));
        helper.returnSocket(socketTask);
        helper.logStatus();
        assertEquals(0, helper.getFeature(SocketHelper.StatusPoolFeature.NUM_ACTIVE));
        
        
        SocketClient socketTask2 = helper.getSocket();
        helper.logStatus();
        assertNotNull(socketTask2);
        assertEquals(1, helper.getFeature(SocketHelper.StatusPoolFeature.NUM_ACTIVE));
        
        byte[] result2 = socketTask2.execute(super.getDummyMessage());
        assertNotNull(result2);
        String resultStr2 = new String(result2);
        assertTrue(resultStr2.startsWith(Parametros.INICIO_TRAMA));
        
        helper.returnSocket(socketTask2);
        helper.logStatus();
        assertEquals(0, helper.getFeature(SocketHelper.StatusPoolFeature.NUM_ACTIVE));
        
        SocketClient socketTask3 = helper.getSocket();
        helper.logStatus();
        assertNotNull(socketTask3);
        assertEquals(1, helper.getFeature(SocketHelper.StatusPoolFeature.NUM_ACTIVE));
        
        byte[] result3 = socketTask3.execute(super.getDummyMessage());
        assertNotNull(result3);
        String resultStr3 = new String(result3);
        assertTrue(resultStr3.startsWith(Parametros.INICIO_TRAMA));
        helper.returnSocket(socketTask3);
        helper.logStatus();
        assertEquals(0, helper.getFeature(SocketHelper.StatusPoolFeature.NUM_ACTIVE));
    }
}
