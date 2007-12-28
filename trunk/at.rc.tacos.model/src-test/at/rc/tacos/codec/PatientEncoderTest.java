package at.rc.tacos.codec;

import java.io.StringWriter;
import java.util.Calendar;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.rc.tacos.model.Patient;
import at.rc.tacos.model.Transport;

public class PatientEncoderTest
{
    private PatientEncoder encoder;
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
        encoder = new PatientEncoder();
    }
    
    @Test
    public void testAddNewPatient() throws XMLStreamException
    {
    	Patient patient = new Patient("derNachname", "derVorname");
    	encoder.doEncode(patient, writer);
    }

    
    @AfterClass
    public static void oneTimeTearDown() throws XMLStreamException
    {
        writer.close();
    }
}
