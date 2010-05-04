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
import at.rc.tacos.model.CallerDetail;

public class CallerEncoder implements MessageEncoder {

	@Override
	public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException {
		// Cast the object to a item
		CallerDetail notifier = (CallerDetail) message;

		// assert valid
		if (notifier == null) {
			System.out.println("WARNING: Object " + getClass().getName() + " is null and cannot be encoded");
			return;
		}

		writer.writeStartElement(CallerDetail.ID);

		// write the id
		writer.writeStartElement("callerId");
		writer.writeCharacters(String.valueOf(notifier.getCallerId()));
		writer.writeEndElement();

		// write the notifier name
		if (notifier.getCallerName() != null) {
			writer.writeStartElement("callerName");
			writer.writeCharacters(notifier.getCallerName());
			writer.writeEndElement();
		}
		// the number is not mandatory
		if (notifier.getCallerTelephoneNumber() != null) {
			writer.writeStartElement("callerTelephoneNumber");
			writer.writeCharacters(notifier.getCallerTelephoneNumber());
			writer.writeEndElement();
		}

		// end
		writer.writeEndElement();
	}

}
