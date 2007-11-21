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
        //write the elements and attributes
        writer.writeStartElement("name");
        writer.writeCharacters(notifier.getNotifierName());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("telephoneNumer");
        writer.writeCharacters(notifier.getNotifierTelephoneNumber());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("notifierNotes");
        writer.writeCharacters(notifier.getNotifierNotes());
        writer.writeEndElement();
        //end
        writer.writeEndElement();
    }

}
