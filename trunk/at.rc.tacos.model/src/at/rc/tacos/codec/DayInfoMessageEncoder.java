package at.rc.tacos.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.DayInfoMessage;

public class DayInfoMessageEncoder  implements MessageEncoder
{
    @Override
    public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException
    {
        //Cast the object to a item
        DayInfoMessage dayInfo = (DayInfoMessage)message;

        //start
        writer.writeStartElement(DayInfoMessage.ID);

        //id for this entry
        writer.writeStartElement("timestamp");
        writer.writeCharacters(Long.toString(dayInfo.getTimestamp()));
        writer.writeEndElement();
        //station id
        writer.writeStartElement("message");
        writer.writeCharacters(dayInfo.getMessage());
        writer.writeEndElement();
        //staff member id
        writer.writeStartElement("lastChangedBy");
        writer.writeCharacters(dayInfo.getLastChangedBy());
        writer.writeEndElement();
        
        //end
        writer.writeEndElement();
    }
}