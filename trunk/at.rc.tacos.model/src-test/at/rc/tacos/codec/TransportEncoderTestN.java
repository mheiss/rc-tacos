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

public class TransportEncoderTestN
{
    private TransportEncoder encoder;
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
        encoder = new TransportEncoder();
    }
    
    @Test
    public void testAddNewTransport() throws XMLStreamException
    {
    	String fromStreet = "von Straﬂe";
    	String fromCommunity = "von Ort";
    	String theStation = "Bruck an der Mur";
    	Calendar cal = Calendar.getInstance();
    	long transportDate = cal.getTimeInMillis();
    	long startLong = cal.getTimeInMillis();
    	String priority = "A";
    	int directness = 1;
    	Transport transport = new Transport(fromStreet,fromCommunity,theStation,transportDate,startLong,priority,directness);
    	transport.setBackTransport(true);
    	Patient patient = new Patient("derNachname", "derVorname");
//    	transport.setPatient(patient);
    	
    	encoder.doEncode(transport, writer);
    }

    
    @AfterClass
    public static void oneTimeTearDown() throws XMLStreamException
    {
        writer.close();
    }
}
