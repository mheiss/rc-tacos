package at.rc.tacos.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.factory.ProtocolCodecFactory;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.StaffMember;

public class LoginEncoder implements MessageEncoder
{
    @Override
    public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException
    {
        //Cast the object to a item
        Login login = (Login)message;
        
        //assert valid
        if(login ==  null)
        {
            System.out.println("WARNING: Object login is null and cannot be encoded");
            return;
        }
        
        //write the start element
        writer.writeStartElement(Login.ID);
       
        //write the elements and attributes
        writer.writeStartElement("username");
        writer.writeCharacters(login.getUsername());
        writer.writeEndElement();
        
        if(login.getPassword() != null && !login.getPassword().isEmpty())
        {
            writer.writeStartElement("password");
            writer.writeCharacters(login.getPassword());
            writer.writeEndElement();
        }
        
        //write the elements and attributes
        writer.writeStartElement("loggedIn");
        writer.writeCharacters(String.valueOf(login.isLoggedIn()));
        writer.writeEndElement();
        
        //write the staff member for this user login
        if(login.getUserInformation() != null)
        {
            //get the encoder for a staff member
            MessageEncoder encoder = ProtocolCodecFactory.getDefault().getEncoder(StaffMember.ID);
            encoder.doEncode(login.getUserInformation(), writer);
        }
        
        //write the element
        writer.writeStartElement("webClient");
        writer.writeCharacters(String.valueOf(login.isWebClient()));
        writer.writeEndElement();
        
        //write the elements and attributes
        if(login.getErrorMessage() != null && !login.getErrorMessage().isEmpty())
        {
            writer.writeStartElement("errorMessage");
            writer.writeCharacters(login.getErrorMessage());
            writer.writeEndElement();
        }
        //write the elements and attributes
        if(login.getAuthorization() != null && !login.getAuthorization().isEmpty())
        {
            writer.writeStartElement("authorization");
            writer.writeCharacters(login.getAuthorization());
            writer.writeEndElement();
        }
        writer.writeStartElement("islocked");
        writer.writeCharacters(String.valueOf(login.isIslocked()));
        writer.writeEndElement();
        
        //end of the item
        writer.writeEndElement();
    }
}
