package at.rc.tacos.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.SystemMessage;

public class SystemMessageEncoder implements MessageEncoder
{
    @Override
    public void doEncode(AbstractMessage message, XMLStreamWriter writer)  throws XMLStreamException
    {   
        //Cast the object to a item
        SystemMessage sysMessage = (SystemMessage)message;
        
        //write the start element
        writer.writeStartElement(SystemMessage.ID);
       
        //write the elements and attributes
        writer.writeStartElement("message");
        writer.writeCharacters(sysMessage.getMessage());
        writer.writeEndElement();
        
        //end of the item
        writer.writeEndElement();
        
    }
}
