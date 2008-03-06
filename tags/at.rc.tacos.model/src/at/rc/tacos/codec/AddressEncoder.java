package at.rc.tacos.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Address;

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

		//write the id
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

		//end
		writer.writeEndElement();
	}

}
