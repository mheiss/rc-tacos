package at.rc.tacos.platform.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.platform.model.AbstractMessage;
import at.rc.tacos.platform.model.Logout;

public class LogoutEncoder implements MessageEncoder
{
    @Override
    public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException
    {
        //Cast the object to a item
        Logout logout = (Logout)message;
        
        //assert valid
        if(logout ==  null)
        {
            System.out.println("WARNING: Object logout is null and cannot be encoded");
            return;
        }
        
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
