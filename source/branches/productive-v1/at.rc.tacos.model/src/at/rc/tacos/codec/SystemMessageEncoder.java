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
import at.rc.tacos.model.SystemMessage;

public class SystemMessageEncoder implements MessageEncoder {

	@Override
	public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException {
		// Cast the object to a item
		SystemMessage sysMessage = (SystemMessage) message;

		// assert valid
		if (sysMessage == null) {
			System.out.println("WARNING: Object sysMessage is null and cannot be encoded");
			return;
		}

		// write the start element
		writer.writeStartElement(SystemMessage.ID);

		// write the elements and attributes
		writer.writeStartElement("message");
		writer.writeCharacters(sysMessage.getMessage());
		writer.writeEndElement();

		// write the error type
		writer.writeStartElement("type");
		writer.writeCharacters(String.valueOf(sysMessage.getType()));
		writer.writeEndElement();

		// end of the item
		writer.writeEndElement();
	}
}
