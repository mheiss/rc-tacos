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

        //write the elements and attributes
        writer.writeStartElement("rosterId");
        writer.writeCharacters(String.valueOf(entry.getRosterId()));
        writer.writeEndElement();
        //get the encoder for the staff member
        MessageEncoder encoder = ProtocolCodecFactory.getDefault().getEncoder(StaffMember.ID);
        encoder.doEncode(entry.getStaffMember(), writer);
//        //write the elements and attributes
//        writer.writeStartElement("dateOfRosterEntry");
//        writer.writeCharacters(String.valueOf(entry.getDateOfRosterEntry()));
//        writer.writeEndElement();
//        //write the elements and attributes
//        writer.writeStartElement("plannedStartofWork");
//        writer.writeCharacters(String.valueOf(entry.getPlannedStartofWork()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("plannedEndOfWork");
        writer.writeCharacters(String.valueOf(entry.getPlannedEndOfWork()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("realStartOfWork");
        writer.writeCharacters(String.valueOf(entry.getRealStartOfWork()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("realEndOfWork");
        writer.writeCharacters(String.valueOf(entry.getRealEndOfWork()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("station");
        writer.writeCharacters(entry.getStation());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("competence");
        writer.writeCharacters(entry.getCompetence());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("servicetype");
        writer.writeCharacters(entry.getServicetype());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("rosterNotes");
        writer.writeCharacters(entry.getRosterNotes());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("standby");
        writer.writeCharacters(String.valueOf(entry.getStandby()));
        writer.writeEndElement();

        //end
        writer.writeEndElement();
    }
}
