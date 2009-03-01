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
import at.rc.tacos.model.Lock;

public class LockEncoder implements MessageEncoder {

	@Override
	public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException {
		// cast the object to encode
		Lock lock = (Lock) message;

		// assert valid
		if (lock == null) {
			System.out.println("WARNING: Object lock is null and cannot be encoded");
			return;
		}

		// write the start element
		writer.writeStartElement(Lock.ID);

		// write the elements and attributes
		writer.writeStartElement("contentId");
		writer.writeCharacters(lock.getContentId());
		writer.writeEndElement();
		// write the elements and attributes
		writer.writeStartElement("lockedId");
		writer.writeCharacters(lock.getLockedId());
		writer.writeEndElement();
		// write the elements and attributes
		writer.writeStartElement("lockedBy");
		writer.writeCharacters(lock.getLockedBy());
		writer.writeEndElement();
		// write the elements and attributes
		writer.writeStartElement("hasLock");
		writer.writeCharacters(String.valueOf(lock.isHasLock()));
		writer.writeEndElement();

		// end of the item
		writer.writeEndElement();
	}
}
