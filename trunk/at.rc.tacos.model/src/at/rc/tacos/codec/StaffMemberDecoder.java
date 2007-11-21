package at.rc.tacos.codec;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.StaffMember;

public class StaffMemberDecoder implements MessageDecoder
{
    @Override
    public AbstractMessage doDecode(XMLEventReader reader) throws XMLStreamException
    {
        //Create a new staff member
        StaffMember member = new StaffMember();
            
        //parse and set up the object
        while(reader.hasNext())
        {
            //the type of the event
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) 
            {
                String startName = event.asStartElement().getName().getLocalPart();
                //create a new item 
                if(StaffMember.ID.equalsIgnoreCase(startName))
                    member = new StaffMember();
                
                //get the type of the element and set the corresponding value
                if("personId".equalsIgnoreCase(startName))
                    member.setPersonId(Integer.valueOf(reader.getElementText()));
                if("lastName".equalsIgnoreCase(startName))
                    member.setLastName(reader.getElementText());
                if("firstName".equalsIgnoreCase(startName))
                    member.setFirstName(reader.getElementText());
                if("userName".equalsIgnoreCase(startName))
                    member.setUserName(reader.getElementText());
            }
            //check for the end element, and return the object
            if(event.isEndElement())
            {
                //get the name
                String endElement = event.asEndElement().getName().getLocalPart();
                //check if we have reached the end
                if (StaffMember.ID.equalsIgnoreCase(endElement))
                    return member;
            }
        }
        return null;
    }
}
