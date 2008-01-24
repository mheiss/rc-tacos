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

        //write the id
        writer.writeStartElement("id");
        writer.writeCharacters(String.valueOf(dayInfo.getId()));
        writer.writeEndElement();
        //timestamp of the last change
        writer.writeStartElement("timestamp");
        writer.writeCharacters(String.valueOf(dayInfo.getTimestamp()));
        writer.writeEndElement();
        //the message
        writer.writeStartElement("message");
        writer.writeCharacters(dayInfo.getMessage());
        writer.writeEndElement();
        //last change username
        writer.writeStartElement("lastChangedBy");
        writer.writeCharacters(dayInfo.getLastChangedBy());
        writer.writeEndElement();
        //indicate a change
        writer.writeStartElement("dirty");
        writer.writeCharacters(String.valueOf(dayInfo.isDirty()));
        writer.writeEndElement();
        
        //end
        writer.writeEndElement();
    }
}