package at.rc.tacos.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.NotifierDetail;

public class NotifierEncoder  implements MessageEncoder
{
    @Override
    public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException
    {
        //Cast the object to a item
        NotifierDetail notifier = (NotifierDetail)message;
        
        writer.writeStartElement(NotifierDetail.ID);
        //write the notifier name
        writer.writeStartElement("name");
        writer.writeCharacters(notifier.getNotifierName());
        writer.writeEndElement();
        //the number is not mandatory
        if(notifier.getNotifierTelephoneNumber() != null)
        {
            writer.writeStartElement("telephoneNumer");
            writer.writeCharacters(notifier.getNotifierTelephoneNumber());
            writer.writeEndElement();
        }
        //the notes are not mandatory
        if(notifier.getNotifierNotes() != null)
        {
            writer.writeStartElement("notifierNotes");
            writer.writeCharacters(notifier.getNotifierNotes());
            writer.writeEndElement();
        }
        //end
        writer.writeEndElement();
    }

}
