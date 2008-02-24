package at.rc.tacos.codec;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Address;

public class AddressDecoder implements MessageDecoder
{
    @Override
    public AbstractMessage doDecode(XMLEventReader reader) throws XMLStreamException
    {
        //Create a new notifier
        Address address = new Address();
            
        //parse and set up the object
        while(reader.hasNext())
        {
            //the type of the event
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) 
            {
                String startName = event.asStartElement().getName().getLocalPart();
                //create a new item 
                if(Address.ID.equalsIgnoreCase(startName))
                    address = new Address();
                
                //get the type of the element and set the corresponding value
                if("zip".equalsIgnoreCase(startName))
                    address.setZip(Integer.valueOf(reader.getElementText()));
                if("city".equalsIgnoreCase(startName))
                    address.setCity(reader.getElementText());
                if("street".equalsIgnoreCase(startName))
                    address.setStreet(reader.getElementText());
            }
            //check for the end element, and return the object
            if(event.isEndElement())
            {
                //get the name
                String endElement = event.asEndElement().getName().getLocalPart();
                //check if we have reached the end
                if (Address.ID.equalsIgnoreCase(endElement))
                    return address;
            }
        }
        return null;
    }
}
