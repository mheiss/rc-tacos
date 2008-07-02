package at.rc.tacos.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Helo;

public class HeloEncoder implements MessageEncoder
{
    @Override
    public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException
    {
        //Cast the object to a helo
    	Helo helo = (Helo)message;
    	
        //assert valid
        if(helo ==  null)
            throw new IllegalArgumentException("The encode object cannot be null");
      
        //write the start element
        writer.writeStartElement(Helo.ID);
       
        //write the id
        writer.writeStartElement("id");
        writer.writeCharacters(String.valueOf(helo.getId()));
        writer.writeEndElement();
        //write the server ip
        writer.writeStartElement("serverIp");
        writer.writeCharacters(helo.getServerIp());
        writer.writeEndElement();
        //write the server port
        writer.writeStartElement("serverPort");
        writer.writeCharacters(String.valueOf(helo.getServerPort()));
        writer.writeEndElement();
        //write the primary server tag
        writer.writeStartElement("serverPrimary");
        writer.writeCharacters(String.valueOf(helo.isServerPrimary()));
        writer.writeEndElement();
        //write the redirect tag
        writer.writeStartElement("redirect");
        writer.writeCharacters(String.valueOf(helo.isRedirect()));
        writer.writeEndElement();
        
        //end of the helo
        writer.writeEndElement();
    }
}
