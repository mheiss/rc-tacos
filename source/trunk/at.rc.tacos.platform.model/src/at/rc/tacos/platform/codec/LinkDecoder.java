package at.rc.tacos.platform.codec;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import at.rc.tacos.platform.model.AbstractMessage;
import at.rc.tacos.platform.model.Link;

/**
 * Link Decoder
 * @author Payer Martin
 * @version 1.0
 */
public class LinkDecoder implements MessageDecoder {

    public AbstractMessage doDecode(XMLEventReader reader) throws XMLStreamException {
        //The item to decode
        Link link = new Link();
            
        //parse and set up the object
        while(reader.hasNext())
        {
            //the type of the event
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) 
            {
                String startName = event.asStartElement().getName().getLocalPart();
                //create a new item 
                if(Link.ID.equalsIgnoreCase(startName))
                	link = new Link();
                
                //get the type of the element and set the corresponding value
                if("id".equalsIgnoreCase(startName))
                    link.setId(Integer.parseInt(reader.getElementText()));
                if("innerText".equalsIgnoreCase(startName))
                    link.setInnerText(reader.getElementText());
                if("href".equalsIgnoreCase(startName))
                    link.setHref(reader.getElementText());
                if("title".equalsIgnoreCase(startName))
                	link.setTitle(reader.getElementText());
                if("username".equalsIgnoreCase(startName))
                	link.setUsername(reader.getElementText());
            }
            //check for the end element, and return the object
            if(event.isEndElement())
            {
                //get the name
                String endElement = event.asEndElement().getName().getLocalPart();
                //check if we have reached the end
                if (Link.ID.equalsIgnoreCase(endElement))
                    return link;
            }
        }
        return null;
    }
}
