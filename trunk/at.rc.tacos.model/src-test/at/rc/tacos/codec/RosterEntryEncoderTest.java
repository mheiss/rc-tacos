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
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.TestDataSource;

public class RosterEntryEncoderTest
{
    private RosterEntryEncoder encoder;
    private static XMLStreamWriter writer;
    
    @BeforeClass
    public static void oneTimeSetUp() throws XMLStreamException
    {
        XMLOutputFactory xmlof = XMLOutputFactory.newInstance();
        writer = xmlof.createXMLStreamWriter(new StringWriter());
        //register the encoder for the staff member
        ProtocolCodecFactory.getDefault().registerEncoder(StaffMember.ID,new StaffMemberEncoder());
    }
    
    @Before
    public void setUp()
    {
        encoder = new RosterEntryEncoder();
    }
    
    @Test
    public void testRosterEntryEncode() throws XMLStreamException
    {
        RosterEntry entry = new RosterEntry();
        entry.setRosterId(1);
        encoder.doEncode(entry, writer);
    }
    
    
    @AfterClass
    public static void oneTimeTearDown() throws XMLStreamException
    {
        writer.close();
    }
}
