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
import at.rc.tacos.model.DayInfoMessage;

public class DayInfoMessageDecoder implements MessageDecoder {

	@Override
	public AbstractMessage doDecode(XMLEventReader reader) throws XMLStreamException {
		// The item to decode
		DayInfoMessage dayInfo = new DayInfoMessage();

		// parse and set up the object
		while (reader.hasNext()) {
			// the type of the event
			XMLEvent event = reader.nextEvent();
			if (event.isStartElement()) {
				String startName = event.asStartElement().getName().getLocalPart();
				// create a new item
				if (DayInfoMessage.ID.equalsIgnoreCase(startName))
					dayInfo = new DayInfoMessage();

				// get the type of the element and set the corresponding value
				if ("timestamp".equalsIgnoreCase(startName))
					dayInfo.setTimestamp(Long.valueOf(reader.getElementText()));
				if ("message".equalsIgnoreCase(startName))
					dayInfo.setMessage(reader.getElementText());
				if ("lastChangedBy".equalsIgnoreCase(startName))
					dayInfo.setLastChangedBy(reader.getElementText());
				if ("dirty".equalsIgnoreCase(startName))
					dayInfo.setDirty(Boolean.valueOf(reader.getElementText()));
			}
			// check for the end element, and return the object
			if (event.isEndElement()) {
				// get the name
				String endElement = event.asEndElement().getName().getLocalPart();
				// check if we have reached the end
				if (DayInfoMessage.ID.equalsIgnoreCase(endElement))
					return dayInfo;
			}
		}
		return null;
	}

}
