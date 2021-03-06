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
import at.rc.tacos.model.SickPerson;

public class SickPersonEncoder implements MessageEncoder {

	@Override
	public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException {
		// Cast the object to a item
		SickPerson person = (SickPerson) message;

		// assert valid
		if (person == null) {
			System.out.println("WARNING: Object sickPerson is null and cannot be encoded");
			return;
		}

		// start
		writer.writeStartElement(SickPerson.ID);

		// write the elements and attributes
		writer.writeStartElement("sickPersonId");
		writer.writeCharacters(String.valueOf(person.getSickPersonId()));
		writer.writeEndElement();

		// write the elements and attributes
		writer.writeStartElement("lastName");
		writer.writeCharacters(person.getLastName());
		writer.writeEndElement();
		// write the elements and attributes
		writer.writeStartElement("firstName");
		writer.writeCharacters(person.getFirstName());
		writer.writeEndElement();

		// write the elements and attributes
		if (person.getStreetname() != null) {
			writer.writeStartElement("streetname");
			writer.writeCharacters(person.getStreetname());
			writer.writeEndElement();
		}
		// write the elements and attributes
		if (person.getCityname() != null) {
			writer.writeStartElement("cityname");
			writer.writeCharacters(person.getCityname());
			writer.writeEndElement();
		}

		if (person.getSVNR() != null) {
			writer.writeStartElement("SVNR");
			writer.writeCharacters(person.getSVNR());
			writer.writeEndElement();
		}

		if (person.getKindOfTransport() != null) {
			writer.writeStartElement("kindOfTransport");
			writer.writeCharacters(person.getKindOfTransport());
			writer.writeEndElement();
		}

		if (person.getNotes() != null) {
			writer.writeStartElement("notes");
			writer.writeCharacters(person.getNotes());
			writer.writeEndElement();
		}

		// write the elements and attributes
		writer.writeStartElement("sex");
		writer.writeCharacters(String.valueOf(person.isMale()));
		writer.writeEndElement();

		// end
		writer.writeEndElement();
	}

}
