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
import at.rc.tacos.model.Logout;

public class LogoutEncoder implements MessageEncoder {

	@Override
	public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException {
		// Cast the object to a item
		Logout logout = (Logout) message;

		// assert valid
		if (logout == null) {
			System.out.println("WARNING: Object logout is null and cannot be encoded");
			return;
		}

		// write the start element
		writer.writeStartElement(Logout.ID);

		// write the elements and attributes
		writer.writeStartElement("username");
		writer.writeCharacters(logout.getUsername());
		writer.writeEndElement();

		// write the elements and attributes
		writer.writeStartElement("loggedOut");
		writer.writeCharacters(String.valueOf(logout.isLoggedOut()));
		writer.writeEndElement();

		// write the elements and attributes
		if (logout.getErrorMessage() != null) {
			writer.writeStartElement("errorMessage");
			writer.writeCharacters(logout.getErrorMessage());
			writer.writeEndElement();
		}

		// end of the item
		writer.writeEndElement();
	}
}
