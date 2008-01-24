package at.rc.tacos.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.factory.ProtocolCodecFactory;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.MobilePhoneDetail;

/**
 * Class to encode a location to xml
 * @author Michael
 */
public class LocationEncoder implements MessageEncoder
{
    @Override
    public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException
    {
        //Cast the object to a Location
        Location location = (Location)message;
        
        //write the start element
        writer.writeStartElement(Location.ID);
        //do we have a funtion, then write it as attribute
        if(location.type != null)
            writer.writeAttribute("type", location.type);
       
        //write the elements and attributes
        writer.writeStartElement("id");
        writer.writeCharacters(String.valueOf(location.getId()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("locationName");
        writer.writeCharacters(location.getLocationName());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("street");
        writer.writeCharacters(location.getStreet());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("streetNumber");
        writer.writeCharacters(location.getStreetNumber());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("zipcode");
        writer.writeCharacters(String.valueOf(location.getZipcode()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("city");
        writer.writeCharacters(location.getCity());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("notes");
        writer.writeCharacters(location.getNotes());
        writer.writeEndElement();
        //get the encoder for the mobile phone
        MessageEncoder encoder = ProtocolCodecFactory.getDefault().getEncoder(MobilePhoneDetail.ID);
        encoder.doEncode(location.getPhone(), writer);
        
        //end of the item
        writer.writeEndElement();
    }
}
