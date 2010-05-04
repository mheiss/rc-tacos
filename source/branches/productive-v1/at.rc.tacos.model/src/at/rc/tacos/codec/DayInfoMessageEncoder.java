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
import at.rc.tacos.model.DayInfoMessage;

public class DayInfoMessageEncoder implements MessageEncoder {

	@Override
	public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException {
		// Cast the object to a item
		DayInfoMessage dayInfo = (DayInfoMessage) message;

		// assert valid
		if (dayInfo == null) {
			System.out.println("WARNING: Object dayInfo is null and cannot be encoded");
			return;
		}

		// start
		writer.writeStartElement(DayInfoMessage.ID);
		// timestamp of the last change
		writer.writeStartElement("timestamp");
		writer.writeCharacters(String.valueOf(dayInfo.getTimestamp()));
		writer.writeEndElement();
		// the message
		if (dayInfo.getMessage() != null) {
			writer.writeStartElement("message");
			writer.writeCharacters(dayInfo.getMessage());
			writer.writeEndElement();
		}
		// last change username
		writer.writeStartElement("lastChangedBy");
		writer.writeCharacters(dayInfo.getLastChangedBy());
		writer.writeEndElement();
		// indicate a change
		writer.writeStartElement("dirty");
		writer.writeCharacters(String.valueOf(dayInfo.isDirty()));
		writer.writeEndElement();

		// end
		writer.writeEndElement();
	}
}
