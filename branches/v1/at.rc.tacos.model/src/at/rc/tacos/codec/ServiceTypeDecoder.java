package at.rc.tacos.codec;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.ServiceType;

public class ServiceTypeDecoder implements MessageDecoder
{
    @Override
    public AbstractMessage doDecode(XMLEventReader reader) throws XMLStreamException
    {
        //create a new serviceType
        ServiceType serviceType = new ServiceType();
        
        //parse and set up the object
        while(reader.hasNext())
        {
            //the type of the event
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) 
            {
                String startName = event.asStartElement().getName().getLocalPart();
                //create a new item 
                if(ServiceType.ID.equalsIgnoreCase(startName))
                    serviceType = new ServiceType();
                
                //get the type of the element and set the corresponding value
                if("id".equalsIgnoreCase(startName))
                    serviceType.setId(Integer.valueOf(reader.getElementText()));
                if("serviceName".equalsIgnoreCase(startName))
                    serviceType.setServiceName(reader.getElementText());
            }
            //check for the end element, and return the object
            if(event.isEndElement())
            {
                //get the name
                String endElement = event.asEndElement().getName().getLocalPart();
                //check if we have reached the end
                if (ServiceType.ID.equalsIgnoreCase(endElement))
                    return serviceType;
            }
        }
        return null;
    }
}
