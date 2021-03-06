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
import at.rc.tacos.model.MobilePhoneDetail;

public class MobilePhoneEncoder implements MessageEncoder {

	@Override
	public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException {
		// Cast the object to a item
		MobilePhoneDetail phone = (MobilePhoneDetail) message;

		// assert valid
		if (phone == null) {
			System.out.println("WARNING: Object mobile phone is null and cannot be encoded");
			return;
		}

		writer.writeStartElement(MobilePhoneDetail.ID);
		// write the id
		writer.writeStartElement("id");
		writer.writeCharacters(String.valueOf(phone.getId()));
		writer.writeEndElement();
		// write the name of the phone
		if (phone.getMobilePhoneName() != null) {
			writer.writeStartElement("mobilePhoneName");
			writer.writeCharacters(phone.getMobilePhoneName());
			writer.writeEndElement();
		}
		// the phone number is not a required field
		if (phone.getMobilePhoneNumber() != null) {
			writer.writeStartElement("mobilePhoneNumber");
			writer.writeCharacters(phone.getMobilePhoneNumber());
			writer.writeEndElement();
		}
		writer.writeEndElement();
	}
}
