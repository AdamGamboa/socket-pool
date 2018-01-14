package com.orthanc.commons.pool.socket;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Adam M. Gamboa G
 */
@RunWith(Suite.class)

@Suite.SuiteClasses({
   TestServerOn.class,
   TestConexion.class,
   TestBorrowUseReturn.class,
   TestBorrowManyThenUse.class,
   TestMinimoConexiones.class,
   TestMaximoOsciosas.class,
   TestMaximoConexiones.class,
   TestUsoMultiple.class
})
public class MockSuiteTest {
    
}
