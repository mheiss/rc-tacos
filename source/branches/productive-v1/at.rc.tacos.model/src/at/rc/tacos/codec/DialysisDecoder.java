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
import at.rc.tacos.factory.ProtocolCodecFactory;
import at.rc.tacos.model.DialysisPatient;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Patient;

public class DialysisDecoder implements MessageDecoder {

	@Override
	public AbstractMessage doDecode(XMLEventReader reader) throws XMLStreamException {
		// Create a new transport
		DialysisPatient dia = new DialysisPatient();

		// parse and set up the object
		while (reader.hasNext()) {
			// the type of the event
			XMLEvent event = reader.nextEvent();
			if (event.isStartElement()) {
				String startName = event.asStartElement().getName().getLocalPart();
				// create a new item
				if (DialysisPatient.ID.equalsIgnoreCase(startName))
					dia = new DialysisPatient();

				// get the patient details
				if (Patient.ID.equalsIgnoreCase(startName)) {
					// get the decoder for the staff
					MessageDecoder decoder = ProtocolCodecFactory.getDefault().getDecoder(Patient.ID);
					dia.setPatient((Patient) decoder.doDecode(reader));
				}
				if (Location.ID.equalsIgnoreCase(startName)) {
					// get the decoder for the staff
					MessageDecoder decoder = ProtocolCodecFactory.getDefault().getDecoder(Location.ID);
					dia.setLocation((Location) decoder.doDecode(reader));
				}
				// get the type of the element and set the corresponding value
				if ("id".equalsIgnoreCase(startName))
					dia.setId(Integer.valueOf(reader.getElementText()));
				if ("fromStreet".equalsIgnoreCase(startName))
					dia.setFromStreet(reader.getElementText());
				if ("fromCity".equalsIgnoreCase(startName))
					dia.setFromCity(reader.getElementText());
				if ("toStreet".equalsIgnoreCase(startName))
					dia.setToStreet(reader.getElementText());
				if ("toCity".equalsIgnoreCase(startName))
					dia.setToCity(reader.getElementText());
				if ("kindOfTransport".equalsIgnoreCase(startName))
					dia.setKindOfTransport(reader.getElementText());
				if ("assistantPerson".equalsIgnoreCase(startName))
					dia.setAssistantPerson(Boolean.valueOf(reader.getElementText()));
				if ("insurance".equalsIgnoreCase(startName))
					dia.setInsurance(reader.getElementText());
				if ("plannedStartOfTransport".equalsIgnoreCase(startName))
					dia.setPlannedStartOfTransport(Long.valueOf(reader.getElementText()));
				if ("plannedTimeAtPatient".equalsIgnoreCase(startName))
					dia.setPlannedTimeAtPatient(Long.valueOf(reader.getElementText()));
				if ("appointmentTimeAtDialysis".equalsIgnoreCase(startName))
					dia.setAppointmentTimeAtDialysis(Long.valueOf(reader.getElementText()));
				if ("plannedStartForBackTransport".equalsIgnoreCase(startName))
					dia.setPlannedStartForBackTransport(Long.valueOf(reader.getElementText()));
				if ("readyTime".equalsIgnoreCase(startName))
					dia.setReadyTime(Long.valueOf(reader.getElementText()));
				if ("monday".equalsIgnoreCase(startName))
					dia.setMonday(Boolean.valueOf(reader.getElementText()));
				if ("tuesday".equalsIgnoreCase(startName))
					dia.setTuesday(Boolean.valueOf(reader.getElementText()));
				if ("wednesday".equalsIgnoreCase(startName))
					dia.setWednesday(Boolean.valueOf(reader.getElementText()));
				if ("thursday".equalsIgnoreCase(startName))
					dia.setThursday(Boolean.valueOf(reader.getElementText()));
				if ("friday".equalsIgnoreCase(startName))
					dia.setFriday(Boolean.valueOf(reader.getElementText()));
				if ("saturday".equalsIgnoreCase(startName))
					dia.setSaturday(Boolean.valueOf(reader.getElementText()));
				if ("sunday".equalsIgnoreCase(startName))
					dia.setSunday(Boolean.valueOf(reader.getElementText()));
				if ("stationary".equalsIgnoreCase(startName))
					dia.setStationary(Boolean.valueOf(reader.getElementText()));
				if ("lastTransportDate".equalsIgnoreCase(startName))
					dia.setLastTransportDate(Long.valueOf(reader.getElementText()));
				if ("lastBackTransportDate".equalsIgnoreCase(startName))
					dia.setLastBackTransportDate(Long.valueOf(reader.getElementText()));
			}
			// check for the end element, and return the object
			if (event.isEndElement()) {
				// get the name
				String endElement = event.asEndElement().getName().getLocalPart();
				// check if we have reached the end
				if (DialysisPatient.ID.equalsIgnoreCase(endElement))
					return dia;
			}
		}
		return null;
	}

}
