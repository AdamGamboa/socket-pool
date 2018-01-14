package com.orthanc.commons.pool.socket;

import com.orthanc.commons.pool.socket.SocketHelper;
import com.orthanc.commons.pool.socket.mock.Parametros;
import com.orthanc.commons.pool.socket.mock.SocketServerHelper;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 *
 * @author Adam M. Gamboa G
 */
public abstract class AbstractTestBase {
    protected static final Logger LOGGER = Logger.getLogger(AbstractTestBase.class.getName());
    
    protected static SocketHelper helper;
    protected static SocketServerHelper serverHelper;
    
    @BeforeClass
    public static void inicializar(){
        serverHelper = SocketServerHelper.getInstance();
        serverHelper.start();
        
        helper = SocketHelper.getInstance(Parametros.HOST, Parametros.PUERTO);
    }
    
    @AfterClass
    public static void finalizar(){
        helper.shutDown();
        serverHelper.shutdown();
    }
    
    
    
    /**
     * Metodo utilizado para imprimir un encabezado
     * @param name 
     */
    protected void logHeaderTestMethod(String name){
        LOGGER.log(Level.INFO, "\n<<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>>"
                + "\n"+getClass().getName()+"."+name
                + "\n<<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>>");
    }
    
    /**
     * Método generico para crear un mensaje Dummy a enviar en las tramas
     * @return Mensaje generado
     */
    protected byte[] getDummyMessage(){
        return ("Mensaje de Prueba enviado:"+new Date().toString()).getBytes();
    }
}
