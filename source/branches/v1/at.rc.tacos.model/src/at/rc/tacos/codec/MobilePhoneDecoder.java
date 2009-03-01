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

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.MobilePhoneDetail;

public class MobilePhoneDecoder implements MessageDecoder {

	@Override
	public AbstractMessage doDecode(XMLEventReader reader) throws XMLStreamException {
		// The mobile phone to decode
		MobilePhoneDetail mobilePhone = new MobilePhoneDetail();

		// parse and set up the object
		while (reader.hasNext()) {
			// the type of the event
			XMLEvent event = reader.nextEvent();
			if (event.isStartElement()) {
				String startName = event.asStartElement().getName().getLocalPart();
				// create a new item
				if (MobilePhoneDetail.ID.equalsIgnoreCase(startName))
					mobilePhone = new MobilePhoneDetail();

				// get the type of the element and set the corresponding value
				if ("id".equalsIgnoreCase(startName))
					mobilePhone.setId(Integer.valueOf(reader.getElementText()));
				if ("mobilePhoneName".equalsIgnoreCase(startName))
					mobilePhone.setMobilePhoneName(reader.getElementText());
				if ("mobilePhoneNumber".equalsIgnoreCase(startName))
					mobilePhone.setMobilePhoneNumber(reader.getElementText());
			}
			// check for the end element, and return the object
			if (event.isEndElement()) {
				// get the name
				String endElement = event.asEndElement().getName().getLocalPart();
				// check if we have reached the end
				if (MobilePhoneDetail.ID.equalsIgnoreCase(endElement))
					return mobilePhone;
			}
		}
		return null;
	}
}
