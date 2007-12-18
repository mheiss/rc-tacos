package at.rc.tacos.codec;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Login;

public class LoginDecoder implements MessageDecoder
{
    @Override
    public AbstractMessage doDecode(XMLEventReader reader) throws XMLStreamException
    {
        //The message to decode
        Login login = new Login();
            
        //parse and set up the object
        while(reader.hasNext())
        {
            //the type of the event
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) 
            {
                String startName = event.asStartElement().getName().getLocalPart();
                //create a new item 
                if(Login.ID.equalsIgnoreCase(startName))
                    login = new Login();
                
                //get the type of the element and set the corresponding value
                if("username".equalsIgnoreCase(startName))
                    login.setUsername(reader.getElementText());
                if("password".equalsIgnoreCase(startName))
                    login.setPassword(reader.getElementText());
                if("loggedIn".equalsIgnoreCase(startName))
                    login.setLoggedIn(Boolean.valueOf(reader.getElementText()));
                if("webClient".equalsIgnoreCase(startName))
                    login.setWebClient(Boolean.valueOf(reader.getElementText()));
                if("errorMessage".equalsIgnoreCase(startName))
                    login.setErrorMessage(reader.getElementText());
            }
            //check for the end element, and return the object
            if(event.isEndElement())
            {
                //get the name
                String endElement = event.asEndElement().getName().getLocalPart();
                //check if we have reached the end
                if (Login.ID.equalsIgnoreCase(endElement))
                    return login;
            }
        }
        return null;
    }
}
