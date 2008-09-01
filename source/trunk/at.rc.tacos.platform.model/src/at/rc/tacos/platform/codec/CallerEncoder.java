package at.rc.tacos.platform.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.platform.model.AbstractMessage;
import at.rc.tacos.platform.model.CallerDetail;

public class CallerEncoder implements MessageEncoder
{
    @Override
    public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException
    {
        //Cast the object to a item
        CallerDetail notifier = (CallerDetail)message;
        
        //assert valid
        if(notifier ==  null)
        {
            System.out.println("WARNING: Object "+ getClass().getName()+" is null and cannot be encoded");
            return;
        }
        
        writer.writeStartElement(CallerDetail.ID);
        
        //write the id
        writer.writeStartElement("callerId");
        writer.writeCharacters(String.valueOf(notifier.getCallerId()));
        writer.writeEndElement();
        
        //write the notifier name
        if(notifier.getCallerName() != null)
        {
	        writer.writeStartElement("callerName");
	        writer.writeCharacters(notifier.getCallerName());
	        writer.writeEndElement();
        }
        //the number is not mandatory
        if(notifier.getCallerTelephoneNumber() != null)
        {
            writer.writeStartElement("callerTelephoneNumber");
            writer.writeCharacters(notifier.getCallerTelephoneNumber());
            writer.writeEndElement();
        }

        //end
        writer.writeEndElement();
    }

}
