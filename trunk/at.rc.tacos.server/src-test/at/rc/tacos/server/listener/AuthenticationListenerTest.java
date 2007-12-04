package at.rc.tacos.server.listener;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import at.rc.tacos.model.Login;

/**
 * Test class for the authentication listener
 * @author Michael
 */
public class AuthenticationListenerTest
{
    private AuthenticationListener listener;

    @Before
    public void setUp()
    {
        listener = new AuthenticationListener();
    }

    @Test
    public void testLoginSuccessfully()
    {
        Login login = listener.handleLoginRequest(new Login("user1","P@ssw0rd"));
        Assert.assertTrue(login.isLoggedIn());
    }
    
    @Test
    public void testLoginFailed()
    {
        Login login = listener.handleLoginRequest(new Login("user12","test"));
        Assert.assertFalse(login.isLoggedIn());
    }
    
    @After
    public void tearDown()
    {
        listener = null;
    }
}
