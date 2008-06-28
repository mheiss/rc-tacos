package at.rc.tacos.codec;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.SystemMessage;

public class SystemMessageDecoder implements MessageDecoder
{
    @Override
    public AbstractMessage doDecode(XMLEventReader reader) throws XMLStreamException
    { 
        //The message to decode
        SystemMessage message = new SystemMessage();
            
        //parse and set up the object
        while(reader.hasNext())
        {
            //the type of the event
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) 
            {
                String startName = event.asStartElement().getName().getLocalPart();
                //create a new item 
                if(SystemMessage.ID.equalsIgnoreCase(startName))
                    message = new SystemMessage();
                
                //get the type of the element and set the corresponding value
                if("message".equalsIgnoreCase(startName))
                    message.setMessage(reader.getElementText());
                
                //the type of the message
                if("type".equalsIgnoreCase(startName))
                	message.setType(Integer.valueOf(reader.getElementText()));
            }
            //check for the end element, and return the object
            if(event.isEndElement())
            {
                //get the name
                String endElement = event.asEndElement().getName().getLocalPart();
                //check if we have reached the end
                if (SystemMessage.ID.equalsIgnoreCase(endElement))
                    return message;
            }
        }
        return null;
    }
}
