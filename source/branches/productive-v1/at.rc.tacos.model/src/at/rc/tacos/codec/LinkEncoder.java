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
import at.rc.tacos.model.Link;

/**
 * Link Encoder
 * 
 * @author Payer Martin
 * @version 1.o
 */
public class LinkEncoder implements MessageEncoder {

	@Override
	public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException {
		// Cast the object to a item
		at.rc.tacos.model.Link link = (Link) message;

		// assert valid
		if (link == null) {
			System.out.println("WARNING: Object link is null and cannot be encoded");
			return;
		}

		// start
		writer.writeStartElement(Link.ID);
		// id
		writer.writeStartElement("id");
		writer.writeCharacters(Integer.toString(link.getId()));
		writer.writeEndElement();
		// innerText
		writer.writeStartElement("innerText");
		writer.writeCharacters(link.getInnerText());
		writer.writeEndElement();
		// href
		writer.writeStartElement("href");
		writer.writeCharacters(link.getHref());
		writer.writeEndElement();
		// title
		if (link.getTitle() != null) {
			writer.writeStartElement("title");
			writer.writeCharacters(link.getTitle());
			writer.writeEndElement();
		}
		// username
		writer.writeStartElement("username");
		writer.writeCharacters(link.getUsername());
		writer.writeEndElement();

		// end
		writer.writeEndElement();

	}

}
