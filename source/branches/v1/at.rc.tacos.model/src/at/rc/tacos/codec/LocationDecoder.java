package at.rc.tacos.codec;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.factory.ProtocolCodecFactory;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.MobilePhoneDetail;

public class LocationDecoder implements MessageDecoder
{
    @Override
    public AbstractMessage doDecode(XMLEventReader reader) throws XMLStreamException
    {
        //create a new location
        Location location = new Location();

        //parse and set up the object
        while(reader.hasNext())
        {
            //the type of the event
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) 
            {
                String startName = event.asStartElement().getName().getLocalPart();
                //create a new item 
                if(Location.ID.equalsIgnoreCase(startName))
                    location = new Location();
                
                //get the type of the element and set the corresponding value
                if("id".equalsIgnoreCase(startName))
                    location.setId(Integer.valueOf(reader.getElementText()));
                if("locationName".equalsIgnoreCase(startName))
                    location.setLocationName(reader.getElementText());
                if("street".equalsIgnoreCase(startName))
                    location.setStreet(reader.getElementText());
                if("streetnumber".equalsIgnoreCase(startName))
                    location.setStreetNumber(reader.getElementText());
                if("zipcode".equalsIgnoreCase(startName))
                    location.setZipcode(Integer.valueOf(reader.getElementText()));
                if("city".equalsIgnoreCase(startName))
                    location.setCity(reader.getElementText());
                if("notes".equalsIgnoreCase(startName))
                    location.setNotes(reader.getElementText());
                if(MobilePhoneDetail.ID.equalsIgnoreCase(startName))
                {
                    //get the decoder for the staff
                    MessageDecoder decoder = ProtocolCodecFactory.getDefault().getDecoder(MobilePhoneDetail.ID);
                    location.setPhone((MobilePhoneDetail)decoder.doDecode(reader));
                }
            }
            //check for the end element, and return the object
            if(event.isEndElement())
            {
                //get the name
                String endElement = event.asEndElement().getName().getLocalPart();
                //check if we have reached the end
                if (Location.ID.equalsIgnoreCase(endElement))
                    return location;
            }
        }
        return null;
    }
}
