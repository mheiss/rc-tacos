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
import at.rc.tacos.model.Period;

public class PeriodEncoder implements MessageEncoder {

	@Override
	public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException {
		// Cast the object to a item
		Period period = (Period) message;

		// assert valid
		if (period == null) {
			System.out.println("WARNING: Object " + getClass().getName() + " is null and cannot be encoded");
			return;
		}

		writer.writeStartElement(Period.ID);

		// write the elements and attributes
		writer.writeStartElement("periodId");
		writer.writeCharacters(String.valueOf(period.getPeriodId()));
		writer.writeEndElement();

		// write the period
		if (period.getPeriod() != null) {
			writer.writeStartElement("period");
			writer.writeCharacters(period.getPeriod());
			writer.writeEndElement();
		}
		// the serviceType competence (_HA, _ZD, _LSD)
		if (period.getServiceTypeCompetence() != null) {
			writer.writeStartElement("serviceTypeCompetence");
			writer.writeCharacters(period.getServiceTypeCompetence());
			writer.writeEndElement();
		}

		// end
		writer.writeEndElement();
	}

}
