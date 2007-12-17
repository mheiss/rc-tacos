package at.rc.tacos.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.CallerDetail;

public class CallerEncoder  implements MessageEncoder
{
    @Override
    public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException
    {
        //Cast the object to a item
        CallerDetail notifier = (CallerDetail)message;
        
        writer.writeStartElement(CallerDetail.ID);
        //write the notifier name
        writer.writeStartElement("name");
        writer.writeCharacters(notifier.getCallerName());
        writer.writeEndElement();
        //the number is not mandatory
        if(notifier.getCallerTelephoneNumber() != null)
        {
            writer.writeStartElement("telephoneNumer");
            writer.writeCharacters(notifier.getCallerTelephoneNumber());
            writer.writeEndElement();
        }
        //the notes are not mandatory
        if(notifier.getCallerNotes() != null)
        {
            writer.writeStartElement("notifierNotes");
            writer.writeCharacters(notifier.getCallerNotes());
            writer.writeEndElement();
        }
        //end
        writer.writeEndElement();
    }

}
