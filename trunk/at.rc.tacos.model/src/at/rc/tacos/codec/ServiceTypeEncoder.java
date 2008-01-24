package at.rc.tacos.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.ServiceType;

/**
 * Class to encode a serviceType to xml
 * @author Michael
 */
public class ServiceTypeEncoder implements MessageEncoder
{
    @Override
    public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException
    {
        //Cast the object to a ServiceType
        ServiceType serviceType = (ServiceType)message;
        
        //write the start element
        writer.writeStartElement(ServiceType.ID);
       
        //write the elements and attributes
        writer.writeStartElement("id");
        writer.writeCharacters(String.valueOf(serviceType.getId()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("serviceName");
        writer.writeCharacters(serviceType.getServiceName());
        writer.writeEndElement();
        
        //end of the service type
        writer.writeEndElement();
    }
}
