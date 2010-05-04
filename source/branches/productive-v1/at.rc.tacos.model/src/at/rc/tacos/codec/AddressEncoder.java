/*******************************************************************************
 * Copyright (c) 2008, 2009 Internettechnik, FH JOANNEUM
 * http://www.fh-joanneum.at/itm
 * 
 * 	Licenced under the GNU GENERAL PUBLIC LICENSE Version 2;
 * 	You may obtain a copy of the License at
 * 	http://www.gnu.org/licenses/gpl-2.0.txt
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *******************************************************************************/
package at.rc.tacos.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Address;

public class AddressEncoder implements MessageEncoder {

	@Override
	public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException {
		// Cast the object to a item
		Address address = (Address) message;

		// assert valid
		if (address == null) {
			System.out.println("WARNING: Object " + getClass().getName() + " is null and cannot be encoded");
			return;
		}

		writer.writeStartElement(Address.ID);

		// write the elements and attributes
		writer.writeStartElement("addressId");
		writer.writeCharacters(String.valueOf(address.getAddressId()));
		writer.writeEndElement();

		// write the zip
		writer.writeStartElement("zip");
		writer.writeCharacters(String.valueOf(address.getZip()));
		writer.writeEndElement();

		// write the city name
		if (address.getCity() != null) {
			writer.writeStartElement("city");
			writer.writeCharacters(address.getCity());
			writer.writeEndElement();
		}
		// the number is not mandatory
		if (address.getStreet() != null) {
			writer.writeStartElement("street");
			writer.writeCharacters(address.getStreet());
			writer.writeEndElement();
		}
		if (address.getStreetNumber() != null) {
			writer.writeStartElement("streetNumber");
			writer.writeCharacters(address.getStreetNumber());
			writer.writeEndElement();
		}

		// end
		writer.writeEndElement();
	}

}
