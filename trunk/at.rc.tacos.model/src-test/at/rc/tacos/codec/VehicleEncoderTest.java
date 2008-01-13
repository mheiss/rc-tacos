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
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.TestDataSource;
import at.rc.tacos.model.VehicleDetail;

public class VehicleEncoderTest
{
    private VehicleEncoder encoder;
    private static XMLStreamWriter writer;
    
    @BeforeClass
    public static void oneTimeSetUp() throws XMLStreamException
    {
        XMLOutputFactory xmlof = XMLOutputFactory.newInstance();
        writer = xmlof.createXMLStreamWriter(new StringWriter());
        //register needed encoders
        ProtocolCodecFactory.getDefault().registerEncoder(StaffMember.ID, new StaffMemberEncoder());
        ProtocolCodecFactory.getDefault().registerEncoder(MobilePhoneDetail.ID, new MobilePhoneEncoder());
    }
    
    @Before
    public void setUp()
    {
        encoder = new VehicleEncoder();
    }
    
    @Test
    public void testTransportEncode1() throws XMLStreamException
    {
        VehicleDetail detail = TestDataSource.getInstance().vehicleList.get(0);
        encoder.doEncode(detail, writer);
    }
    
    @Test
    public void testTransportEncode2() throws XMLStreamException
    {
        VehicleDetail detail = TestDataSource.getInstance().vehicleList.get(1);
        encoder.doEncode(detail, writer);
    }
    
    @Test
    public void testTransportEncode3() throws XMLStreamException
    {
        VehicleDetail detail = TestDataSource.getInstance().vehicleList.get(2);
        encoder.doEncode(detail, writer);
    }
    
    
    @AfterClass
    public static void oneTimeTearDown() throws XMLStreamException
    {
        writer.close();
    }

}
