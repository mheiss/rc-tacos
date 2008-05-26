package at.rc.tacos.codec;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.SickPerson;

public class SickPersonDecoder implements MessageDecoder
{
    @Override
    public AbstractMessage doDecode(XMLEventReader reader) throws XMLStreamException
    {
        //Create a new sick person
        SickPerson person = new SickPerson();
            
        //parse and set up the object
        while(reader.hasNext())
        {
            //the type of the event
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) 
            {
                String startName = event.asStartElement().getName().getLocalPart();
                //create a new item 
                if(SickPerson.ID.equalsIgnoreCase(startName))
                    person = new SickPerson();
                
                //get the type of the element and set the corresponding value
                if("sickPersonId".equalsIgnoreCase(startName))
                    person.setSickPersonId(Integer.valueOf(reader.getElementText()));
             
                if("lastName".equalsIgnoreCase(startName))
                    person.setLastName(reader.getElementText());
                if("firstName".equalsIgnoreCase(startName))
                    person.setFirstName(reader.getElementText());
                if("streetname".equalsIgnoreCase(startName))
                    person.setStreetname(reader.getElementText());
                if("cityname".equalsIgnoreCase(startName))
                    person.setCityname(reader.getElementText());
                if("sex".equalsIgnoreCase(startName))
                	person.setMale(Boolean.valueOf(reader.getElementText()));
                if("SVNR".equalsIgnoreCase(startName))
                	person.setSVNR(reader.getElementText());
                if("kindOfTransport".equalsIgnoreCase(startName))
                	person.setKindOfTransport(reader.getElementText());
                if("notes".equalsIgnoreCase(startName))
                	person.setNotes(reader.getElementText());
            }
            //check for the end element, and return the object
            if(event.isEndElement())
            {
                //get the name
                String endElement = event.asEndElement().getName().getLocalPart();
                //check if we have reached the end
                if (SickPerson.ID.equalsIgnoreCase(endElement))
                    return person;
            }
        }
        return null;
    }
}
