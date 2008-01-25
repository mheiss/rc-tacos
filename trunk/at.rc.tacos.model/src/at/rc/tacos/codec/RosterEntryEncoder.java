package at.rc.tacos.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.factory.ProtocolCodecFactory;
import at.rc.tacos.model.Job;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.ServiceType;
import at.rc.tacos.model.StaffMember;

public class RosterEntryEncoder  implements MessageEncoder
{
    @Override
    public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException
    {
        //Cast the object to a item
        RosterEntry entry = (RosterEntry)message;
        
        //assert valid
        if(entry ==  null)
        {
            System.out.println("WARNING: Object roster entry is null and cannot be encoded");
            return;
        }

        //start
        writer.writeStartElement(RosterEntry.ID);

        //id for this entry
        writer.writeStartElement("rosterId");
        writer.writeCharacters(String.valueOf(entry.getRosterId()));
        writer.writeEndElement();
        //get the encoder for the location
        MessageEncoder encoder = ProtocolCodecFactory.getDefault().getEncoder(Location.ID);
        encoder.doEncode(entry.getStation(), writer);
        //get the encoder for the staff member
        encoder = ProtocolCodecFactory.getDefault().getEncoder(StaffMember.ID);
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
        //get the encoder for the service type
        encoder = ProtocolCodecFactory.getDefault().getEncoder(Job.ID);
        encoder.doEncode(entry.getJob(), writer);
        //get the encoder for the staff job
        encoder = ProtocolCodecFactory.getDefault().getEncoder(ServiceType.ID);
        encoder.doEncode(entry.getServicetype(), writer);
        //notes are mandatory
        if(entry.getRosterNotes() != null)
        {
            writer.writeStartElement("rosterNotes");
            writer.writeCharacters(entry.getRosterNotes());
            writer.writeEndElement();
        }
        //write the elements and attributes
        writer.writeStartElement("createdByUser");
        writer.writeCharacters(entry.getCreatedByUsername());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("standby");
        System.out.println("Bereitschaft:" +entry.getStandby());
        writer.writeCharacters(String.valueOf(entry.getStandby()));
        writer.writeEndElement();

        //end
        writer.writeEndElement();
    }
}
