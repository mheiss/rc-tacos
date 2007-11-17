package at.rc.tacos.core.xml.codec;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.NotifierDetail;

public class NotifierDecoder implements MessageDecoder
{
    @Override
    public AbstractMessage doDecode(XMLEventReader reader) throws XMLStreamException
    {
        //Create a new notifier
        NotifierDetail notifier = new NotifierDetail();
            
        //parse and set up the object
        while(reader.hasNext())
        {
            //the type of the event
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) 
            {
                String startName = event.asStartElement().getName().getLocalPart();
                //create a new item 
                if(NotifierDetail.ID.equalsIgnoreCase(startName))
                    notifier = new NotifierDetail();
                
                //get the type of the element and set the corresponding value
                if("name".equalsIgnoreCase(startName))
                    notifier.setNotifierName(reader.getElementText());
                if("telephoneNumer".equalsIgnoreCase(startName))
                    notifier.setNotifierTelephoneNumber(reader.getElementText());
                if("notifierNotes".equalsIgnoreCase(startName))
                    notifier.setNotifierNotes(reader.getElementText());
            }
            //check for the end element, and return the object
            if(event.isEndElement())
            {
                //get the name
                String endElement = event.asEndElement().getName().getLocalPart();
                //check if we have reached the end
                if (NotifierDetail.ID.equalsIgnoreCase(endElement))
                    return notifier;
            }
        }
        return null;
    }
}
