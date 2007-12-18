package at.rc.tacos.codec;

import java.io.StringWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.rc.tacos.model.Login;

public class LoginEncoderTest
{
    private LoginEncoder encoder;
    private static XMLStreamWriter writer;
    
    @BeforeClass
    public static void oneTimeSetUp() throws XMLStreamException
    {
        XMLOutputFactory xmlof = XMLOutputFactory.newInstance();
        writer = xmlof.createXMLStreamWriter(new StringWriter());
    }
    
    @Before
    public void setUp()
    {
        encoder = new LoginEncoder();
    }
    
    @Test
    public void testLoginRequest() throws XMLStreamException
    {
        Login login = new Login("testuser","pwd",true);
        encoder.doEncode(login, writer);
    }
    
    @Test
    public void testLoginResponseSuccessfully() throws XMLStreamException
    {
        Login login = new Login("testuser","15",true);
        login.setPassword("");
        login.setLoggedIn(true);
        encoder.doEncode(login, writer);
    }
    
    @Test
    public void testLoginResponseFailed() throws XMLStreamException
    {
        Login login = new Login("testuser","pwd",true);
        login.setPassword("");
        login.setLoggedIn(false);
        login.setErrorMessage("test");
        encoder.doEncode(login, writer);
    }
    
    @AfterClass
    public static void oneTimeTearDown() throws XMLStreamException
    {
        writer.close();
    }
}
