package at.rc.tacos.platform.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.platform.model.AbstractMessage;
import at.rc.tacos.platform.model.Address;

public class AddressEncoder implements MessageEncoder
{
	@Override
	public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException
	{
		//Cast the object to a item
		Address address = (Address)message;

		//assert valid
		if(address ==  null)
		{
			System.out.println("WARNING: Object "+ getClass().getName()+" is null and cannot be encoded");
			return;
		}

		writer.writeStartElement(Address.ID);

		//write the elements and attributes
        writer.writeStartElement("addressId");
        writer.writeCharacters(String.valueOf(address.getAddressId()));
        writer.writeEndElement();
        
		//write the zip
		writer.writeStartElement("zip");
		writer.writeCharacters(String.valueOf(address.getZip()));
		writer.writeEndElement();

		//write the city name
		if(address.getCity() != null)
		{
			writer.writeStartElement("city");
			writer.writeCharacters(address.getCity());
			writer.writeEndElement();
		}
		//the number is not mandatory
		if(address.getStreet() != null)
		{
			writer.writeStartElement("street");
			writer.writeCharacters(address.getStreet());
			writer.writeEndElement();
		}
		if(address.getStreetNumber() != null)
		{
			writer.writeStartElement("streetNumber");
			writer.writeCharacters(address.getStreetNumber());
			writer.writeEndElement();
		}

		//end
		writer.writeEndElement();
	}
}
