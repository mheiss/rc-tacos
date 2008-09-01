package at.rc.tacos.platform.codec;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import at.rc.tacos.platform.factory.ProtocolCodecFactory;
import at.rc.tacos.platform.model.AbstractMessage;
import at.rc.tacos.platform.model.Job;
import at.rc.tacos.platform.model.Location;
import at.rc.tacos.platform.model.RosterEntry;
import at.rc.tacos.platform.model.ServiceType;
import at.rc.tacos.platform.model.StaffMember;

public class RosterEntryDecoder implements MessageDecoder
{
    @Override
    public AbstractMessage doDecode(XMLEventReader reader)  throws XMLStreamException
    {
        //Create a new roster entry
        RosterEntry entry = new RosterEntry();

        //parse and set up the object
        while(reader.hasNext())
        {
            //the type of the event
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) 
            {
                String startName = event.asStartElement().getName().getLocalPart();
                //create a new item 
                if(RosterEntry.ID.equalsIgnoreCase(startName))
                    entry = new RosterEntry();
                //get the type of the element and set the corresponding value
                if(Location.ID.equalsIgnoreCase(startName))
                {
                    //get the decoder for the staff
                    MessageDecoder decoder = ProtocolCodecFactory.getDefault().getDecoder(Location.ID);
                    entry.setStation((Location)decoder.doDecode(reader));
                }
                if(StaffMember.ID.equalsIgnoreCase(startName))
                {
                    //get the decoder for the staff
                    MessageDecoder decoder = ProtocolCodecFactory.getDefault().getDecoder(StaffMember.ID);
                    entry.setStaffMember((StaffMember)decoder.doDecode(reader));
                }
                if(Job.ID.equalsIgnoreCase(startName))
                {
                    //get the decoder for the staff
                    MessageDecoder decoder = ProtocolCodecFactory.getDefault().getDecoder(Job.ID);
                    entry.setJob((Job)decoder.doDecode(reader));
                }
                if(ServiceType.ID.equalsIgnoreCase(startName))
                {
                    //get the decoder for the staff
                    MessageDecoder decoder = ProtocolCodecFactory.getDefault().getDecoder(ServiceType.ID);
                    entry.setServicetype((ServiceType)decoder.doDecode(reader));
                }
                //get the type of the element and set the corresponding value
                if("rosterId".equalsIgnoreCase(startName))
                    entry.setRosterId(Integer.valueOf(reader.getElementText()));
                //get the type of the element and set the corresponding value
                if("plannedStartofWork".equalsIgnoreCase(startName))
                    entry.setPlannedStartOfWork(Long.valueOf(reader.getElementText()));
                //get the type of the element and set the corresponding value
                if("plannedEndOfWork".equalsIgnoreCase(startName))
                    entry.setPlannedEndOfWork(Long.valueOf(reader.getElementText()));
                //get the type of the element and set the corresponding value
                if("realStartOfWork".equalsIgnoreCase(startName))
                    entry.setRealStartOfWork(Long.valueOf(reader.getElementText()));
                //get the type of the element and set the corresponding value
                if("realEndOfWork".equalsIgnoreCase(startName))
                    entry.setRealEndOfWork(Long.valueOf(reader.getElementText()));
                //get the type of the element and set the corresponding value
                if("rosterNotes".equalsIgnoreCase(startName))
                    entry.setRosterNotes(reader.getElementText());
                //get the type of the element and set the corresponding value
                if("standby".equalsIgnoreCase(startName))
                    entry.setStandby(Boolean.valueOf(reader.getElementText()));
                if("createdByUser".equalsIgnoreCase(startName))
                    entry.setCreatedByUsername(reader.getElementText());
            }
            //check for the end element, and return the object
            if(event.isEndElement())
            {
                //get the name
                String endElement = event.asEndElement().getName().getLocalPart();
                //check if we have reached the end
                if (RosterEntry.ID.equalsIgnoreCase(endElement))
                    return entry;
            }
        }
        return null;
    }
}
