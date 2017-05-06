

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class ReceiveDataTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class ReceiveDataTest
{
    /**
     * Default constructor for test class ReceiveDataTest
     */
    public ReceiveDataTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }
    
    @Test
    public void testRun(){
        Router r = new Router("./Routers/Test.txt",true);
        ReceiveData rd = new ReceiveData(r);
        rd.run();
    }
}
