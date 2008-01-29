package at.rc.tacos.codec;

import java.io.StringWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import at.rc.tacos.factory.ProtocolCodecFactory;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.CallerDetail;
import at.rc.tacos.model.Patient;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.TestDataSource;
import at.rc.tacos.model.Transport;
import at.rc.tacos.model.VehicleDetail;


public class TransportEncoderTest
{
    private TransportEncoder encoder;
    private static XMLStreamWriter writer;
    
    @BeforeClass
    public static void oneTimeSetUp() throws XMLStreamException
    {
        XMLOutputFactory xmlof = XMLOutputFactory.newInstance();
        writer = xmlof.createXMLStreamWriter(new StringWriter());
        //register needed encoders
        ProtocolCodecFactory.getDefault().registerEncoder(Patient.ID,new PatientEncoder());
        ProtocolCodecFactory.getDefault().registerEncoder(CallerDetail.ID,new CallerEncoder());
        ProtocolCodecFactory.getDefault().registerEncoder(VehicleDetail.ID,new VehicleEncoder());
        ProtocolCodecFactory.getDefault().registerEncoder(StaffMember.ID, new StaffMemberEncoder());
        ProtocolCodecFactory.getDefault().registerEncoder(MobilePhoneDetail.ID, new MobilePhoneEncoder());
    }
    
    @Before
    public void setUp()
    {
        encoder = new TransportEncoder();
    }
    
    @Test
    public void testTransportEncode1() throws XMLStreamException
    {
        Transport transport = TestDataSource.getInstance().transportList.get(0);
        transport.getVehicleDetail().getDriver();
        encoder.doEncode(transport, writer);
    }
    
    @Test
    public void testTransportEncode2() throws XMLStreamException
    {
        Transport transport = TestDataSource.getInstance().transportList.get(1);
        encoder.doEncode(transport, writer);
    }
    
    @Test
    public void testTransportEncode3() throws XMLStreamException
    {
        Transport transport = TestDataSource.getInstance().transportList.get(2);
        encoder.doEncode(transport, writer);
    }
    
    
    @AfterClass
    public static void oneTimeTearDown() throws XMLStreamException
    {
        writer.close();
    }
}
