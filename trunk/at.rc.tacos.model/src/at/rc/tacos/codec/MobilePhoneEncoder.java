package at.rc.tacos.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.MobilePhoneDetail;

public class MobilePhoneEncoder  implements MessageEncoder
{
    @Override
    public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException
    {
        //Cast the object to a item
        MobilePhoneDetail phone = (MobilePhoneDetail)message;
        
        writer.writeStartElement(MobilePhoneDetail.ID);
       
        //write the elements and attributes
        writer.writeStartElement("mobilePhoneId");
        writer.writeCharacters(phone.getMobilePhoneId());
        writer.writeEndElement();
        
        writer.writeStartElement("mobilePhoneNumer");
        writer.writeCharacters(phone.getMobilePhoneNumber());
        writer.writeEndElement();
        
        writer.writeEndElement();
    }
}
