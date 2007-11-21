package at.rc.tacos.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.StaffMember;

public class StaffMemberEncoder  implements MessageEncoder
{
    @Override
    public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException
    {
        //Cast the object to a item
        StaffMember member = (StaffMember)message;
       
        //start
        writer.writeStartElement(StaffMember.ID);
        //do we have a funtion
        if(member.getFunction() != null)
            writer.writeAttribute("function", member.getFunction());
        
        //write the elements and attributes
        writer.writeStartElement("personId");
        writer.writeCharacters(String.valueOf(member.getPersonId()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("lastName");
        writer.writeCharacters(member.getLastname());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("firstName");
        writer.writeCharacters(member.getFirstName());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("userName");
        writer.writeCharacters(member.getUserName());
        writer.writeEndElement();
        
        //end
        writer.writeEndElement();
    }

}
