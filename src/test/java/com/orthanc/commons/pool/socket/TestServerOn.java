package com.orthanc.commons.pool.socket;

import static com.orthanc.commons.pool.socket.AbstractTestBase.serverHelper;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Chequea que el servidor de sockets para las pruebas se encuentre arriba.
 * 
 * @author Adam M. Gamboa G.
 */
public class TestServerOn extends AbstractTestBase{
    
    @Test
    public void testServerOn() throws InterruptedException{
        logHeaderTestMethod("testServerOn");
        
        boolean serverOn = serverHelper.isServerOn();
        assertTrue(serverOn);
        Thread.sleep(1000);
    }
}
