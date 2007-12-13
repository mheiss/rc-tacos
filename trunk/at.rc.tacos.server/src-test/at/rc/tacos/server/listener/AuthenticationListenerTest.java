package at.rc.tacos.server.listener;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import at.rc.tacos.core.db.dao.UserLoginDAO;
import at.rc.tacos.model.Login;
import at.rc.tacos.server.dao.DaoService;

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
        //add login data
        UserLoginDAO userDao = DaoService.getInstance().getFactory().createUserDAO();
        userDao.addUserLogin("user1", "P@ssw0rd");
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
}
