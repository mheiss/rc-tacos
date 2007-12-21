package at.rc.tacos.codec;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.CallerDetail;

public class CallerDecoder implements MessageDecoder
{
    @Override
    public AbstractMessage doDecode(XMLEventReader reader) throws XMLStreamException
    {
        //Create a new notifier
        CallerDetail notifier = new CallerDetail();
            
        //parse and set up the object
        while(reader.hasNext())
        {
            //the type of the event
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) 
            {
                String startName = event.asStartElement().getName().getLocalPart();
                //create a new item 
                if(CallerDetail.ID.equalsIgnoreCase(startName))
                    notifier = new CallerDetail();
                
                //get the type of the element and set the corresponding value
                if("name".equalsIgnoreCase(startName))
                    notifier.setCallerName(reader.getElementText());
                if("telephoneNumer".equalsIgnoreCase(startName))
                    notifier.setCallerTelephoneNumber(reader.getElementText());
            }
            //check for the end element, and return the object
            if(event.isEndElement())
            {
                //get the name
                String endElement = event.asEndElement().getName().getLocalPart();
                //check if we have reached the end
                if (CallerDetail.ID.equalsIgnoreCase(endElement))
                    return notifier;
            }
        }
        return null;
    }
}
