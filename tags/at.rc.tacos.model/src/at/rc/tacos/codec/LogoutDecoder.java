package at.rc.tacos.codec;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Logout;

public class LogoutDecoder implements MessageDecoder
{
    @Override
    public AbstractMessage doDecode(XMLEventReader reader) throws XMLStreamException
    {
        Logout logout = new Logout();
        
        //parse and set up the object
        while(reader.hasNext())
        {
            //the type of the event
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) 
            {
                String startName = event.asStartElement().getName().getLocalPart();
                //create a new item 
                if(Logout.ID.equalsIgnoreCase(startName))
                    logout = new Logout();

                //get the type of the element and set the corresponding value
                if("username".equalsIgnoreCase(startName))
                    logout.setUsername(reader.getElementText());
                if("loggedOut".equalsIgnoreCase(startName))
                    logout.setLoggedOut(Boolean.valueOf(reader.getElementText()));
                if("errorMessage".equalsIgnoreCase(startName))
                    logout.setErrorMessage(reader.getElementText());
            }
            //check for the end element, and return the object
            if(event.isEndElement())
            {
                //get the name
                String endElement = event.asEndElement().getName().getLocalPart();
                //check if we have reached the end
                if (Logout.ID.equalsIgnoreCase(endElement))
                    return logout;
            }
        }
        return null;
    }
}
