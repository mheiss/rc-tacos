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
import at.rc.tacos.factory.ProtocolCodecFactory;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.MobilePhoneDetail;

/**
 * Class to encode a location to xml
 * 
 * @author Michael
 */
public class LocationEncoder implements MessageEncoder {

	@Override
	public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException {
		// Cast the object to a Location
		Location location = (Location) message;

		// assert valid
		if (location == null) {
			System.out.println("WARNING: Object location is null and cannot be encoded");
			return;
		}

		// write the start element
		writer.writeStartElement(Location.ID);
		// do we have a funtion, then write it as attribute
		if (location.type != null)
			writer.writeAttribute("type", location.type);

		// write the elements and attributes
		writer.writeStartElement("id");
		writer.writeCharacters(String.valueOf(location.getId()));
		writer.writeEndElement();
		// write the elements and attributes
		if (location.getLocationName() != null) {
			writer.writeStartElement("locationName");
			writer.writeCharacters(location.getLocationName());
			writer.writeEndElement();
		}
		// write the elements and attributes
		if (location.getStreet() != null) {
			writer.writeStartElement("street");
			writer.writeCharacters(location.getStreet());
			writer.writeEndElement();
		}
		// write the elements and attributes
		if (location.getStreetNumber() != null) {
			writer.writeStartElement("streetNumber");
			writer.writeCharacters(location.getStreetNumber());
			writer.writeEndElement();
		}
		// write the elements and attributes
		if (location.getZipcode() > 0) {
			writer.writeStartElement("zipcode");
			writer.writeCharacters(String.valueOf(location.getZipcode()));
			writer.writeEndElement();
		}
		// write the elements and attributes
		if (location.getCity() != null) {
			writer.writeStartElement("city");
			writer.writeCharacters(location.getCity());
			writer.writeEndElement();
		}
		// write the elements and attributes
		if (location.getNotes() != null) {
			writer.writeStartElement("notes");
			writer.writeCharacters(location.getNotes());
			writer.writeEndElement();
		}
		// get the encoder for the mobile phone
		if (location.getPhone() != null) {
			MessageEncoder encoder = ProtocolCodecFactory.getDefault().getEncoder(MobilePhoneDetail.ID);
			encoder.doEncode(location.getPhone(), writer);
		}

		// end of the item
		writer.writeEndElement();
	}
}
