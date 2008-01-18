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
        writer.writeCharacters(member.getLastName());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("firstName");
        writer.writeCharacters(member.getFirstName());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("userName");
        writer.writeCharacters(member.getUserName());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("islocked");
        writer.writeCharacters(String.valueOf(member.getIslocked()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("authorization");
        writer.writeCharacters(member.getAuthorization());
        writer.writeEndElement();
        //write the elements and attributes
        if(member.getEMail() != null)
        {
	        writer.writeStartElement("eMail");
	        writer.writeCharacters(member.getEMail());
	        writer.writeEndElement();
        }
        //write the elements and attributes
        if(member.getCityname() != null)
        {
	        writer.writeStartElement("cityname");
	        writer.writeCharacters(member.getCityname());
	        writer.writeEndElement();
        }
     	//write the elements and attributes
        if(member.getStreetname() != null)
        {
	        writer.writeStartElement("streetname");
	        writer.writeCharacters(member.getStreetname());
	        writer.writeEndElement();
        }
        //write the elements and attributes
        writer.writeStartElement("primaryLocation");
        writer.writeCharacters(String.valueOf(member.getPrimaryLocation()));
        writer.writeEndElement();
        
        //write the elements and attributes
        if(member.getBirthday() > 0)
        {
            writer.writeStartElement("birthday");
            writer.writeCharacters(Long.toString(member.getBirthday()));
            writer.writeEndElement();
        }
        
        //write the elements and attributes
        writer.writeStartElement("sex");
        writer.writeCharacters(String.valueOf(member.isSex()));
        writer.writeEndElement();
        
        //encode the status messages
        for(String phoneNumber:member.getPhonenumber())
        {
            writer.writeStartElement("phonenumber");
            writer.writeCharacters(String.valueOf(phoneNumber));
            writer.writeEndElement();
        }
        
        //end
        writer.writeEndElement();
    }

}
