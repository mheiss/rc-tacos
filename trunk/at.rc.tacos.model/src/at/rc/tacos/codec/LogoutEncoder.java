package at.rc.tacos.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Logout;

public class LogoutEncoder implements MessageEncoder
{
    @Override
    public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException
    {
        //Cast the object to a item
        Logout logout = (Logout)message;
        
        //write the start element
        writer.writeStartElement(Logout.ID);
       
        //write the elements and attributes
        writer.writeStartElement("username");
        writer.writeCharacters(logout.getUsername());
        writer.writeEndElement();
                
        //write the elements and attributes
        writer.writeStartElement("loggedOut");
        writer.writeCharacters(String.valueOf(logout.isLoggedOut()));
        writer.writeEndElement();
        
        //write the elements and attributes
        if(logout.getErrorMessage() != null)
        {
            writer.writeStartElement("errorMessage");
            writer.writeCharacters(logout.getErrorMessage());
            writer.writeEndElement();
        }
        
        //end of the item
        writer.writeEndElement();
    }
}
