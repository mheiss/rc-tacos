package at.rc.tacos.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.factory.ProtocolCodecFactory;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.StaffMember;

public class RosterEntryEncoder  implements MessageEncoder
{
    @Override
    public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException
    {
        //Cast the object to a item
        RosterEntry entry = (RosterEntry)message;

        //start
        writer.writeStartElement(RosterEntry.ID);

        //id for this entry
        writer.writeStartElement("rosterId");
        writer.writeCharacters(String.valueOf(entry.getRosterId()));
        writer.writeEndElement();
        //station id
        writer.writeStartElement("stationId");
        writer.writeCharacters(String.valueOf(entry.getStationId()));
        writer.writeEndElement();
        //staff member id
        writer.writeStartElement("stationId");
        writer.writeCharacters(String.valueOf(entry.getStationId()));
        writer.writeEndElement();
        //servicetypeId
        writer.writeStartElement("servicetypeId");
        writer.writeCharacters(String.valueOf(entry.getServicetypeId()));
        writer.writeEndElement();
        //jobId
        writer.writeStartElement("jobId");
        writer.writeCharacters(String.valueOf(entry.getJobId()));
        writer.writeEndElement();
        //get the encoder for the staff member
        MessageEncoder encoder = ProtocolCodecFactory.getDefault().getEncoder(StaffMember.ID);
        encoder.doEncode(entry.getStaffMember(), writer);
        //planned start of work
        writer.writeStartElement("plannedStartOfWork");
        writer.writeCharacters(Long.toString(entry.getPlannedStartOfWork()));
        writer.writeEndElement();
        //planned end of work
        writer.writeStartElement("plannedEndOfWork");
        writer.writeCharacters(Long.toString(entry.getPlannedEndOfWork()));
        writer.writeEndElement();
        //real time is mandatory
        if(entry.getRealStartOfWork() > 0)
        {
            writer.writeStartElement("realStartOfWork");
            writer.writeCharacters(Long.toString(entry.getRealStartOfWork()));
            writer.writeEndElement();
        }
        //real time is mandatory
        if(entry.getRealEndOfWork() > 0)
        {
            writer.writeStartElement("realEndOfWork");
            writer.writeCharacters(Long.toString(entry.getRealEndOfWork()));
            writer.writeEndElement();
        }
        //the station for the service
        writer.writeStartElement("station");
        writer.writeCharacters(entry.getStation());
        writer.writeEndElement();
        
        
        //the competence for this service
        writer.writeStartElement("job");
        writer.writeCharacters(entry.getJob());
        writer.writeEndElement();
        //the type of the service
        writer.writeStartElement("servicetype");
        writer.writeCharacters(entry.getServicetype());
        writer.writeEndElement();
        //notes are mandatory
        if(entry.getRosterNotes() != null)
        {
            writer.writeStartElement("rosterNotes");
            writer.writeCharacters(entry.getRosterNotes());
            writer.writeEndElement();
        }
        //write the elements and attributes
        writer.writeStartElement("standby");
        writer.writeCharacters(String.valueOf(entry.getStandby()));
        writer.writeEndElement();
        

        //end
        writer.writeEndElement();
    }
}
