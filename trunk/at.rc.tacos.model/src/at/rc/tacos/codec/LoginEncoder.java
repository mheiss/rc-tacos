package at.rc.tacos.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Login;

public class LoginEncoder implements MessageEncoder
{
    @Override
    public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException
    {
        //Cast the object to a item
        Login login = (Login)message;
        
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
        
        //end of the item
        writer.writeEndElement();
    }
}
