package at.rc.tacos.codec;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.factory.ProtocolCodecFactory;
import at.rc.tacos.model.CallerDetail;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Patient;
import at.rc.tacos.model.Transport;
import at.rc.tacos.model.VehicleDetail;

public class TransportDecoder implements MessageDecoder
{
	@Override
	public AbstractMessage doDecode(XMLEventReader reader)  throws XMLStreamException
	{
		//Create a new transport
		Transport transport = new Transport();

		//parse and set up the object
		while(reader.hasNext())
		{
			//the type of the event
			XMLEvent event = reader.nextEvent();
			if (event.isStartElement()) 
			{
				StartElement start = event.asStartElement();
				String startName = event.asStartElement().getName().getLocalPart();
				//create a new item 
				if(Transport.ID.equalsIgnoreCase(startName))
					transport = new Transport();

				if("transportId".equalsIgnoreCase(startName))
					transport.setTransportId(Integer.valueOf(reader.getElementText())); 
				if("year".equalsIgnoreCase(startName))
					transport.setYear(Integer.valueOf(reader.getElementText())); 
				if("transportNumber".equalsIgnoreCase(startName))
					transport.setTransportNumber(Integer.valueOf(reader.getElementText())); 

				//get the notifier details
				if(CallerDetail.ID.equalsIgnoreCase(startName))
				{
					//get the decoder for the staff
					MessageDecoder decoder = ProtocolCodecFactory.getDefault().getDecoder(CallerDetail.ID);
					transport.setCallerDetail((CallerDetail)decoder.doDecode(reader));
				}

				if("fromStreet".equalsIgnoreCase(startName))
					transport.setFromStreet(reader.getElementText());
				if("fromCity".equalsIgnoreCase(startName))
					transport.setFromCity(reader.getElementText());
				if(Patient.ID.equalsIgnoreCase(startName))
				{
					//get the decoder for the staff
					MessageDecoder decoder = ProtocolCodecFactory.getDefault().getDecoder(Patient.ID);
					transport.setPatient((Patient)decoder.doDecode(reader));
				}
				if("toStreet".equalsIgnoreCase(startName))
					transport.setToStreet(reader.getElementText());
				if("toCity".equalsIgnoreCase(startName))
					transport.setToCity(reader.getElementText());
				if("kindOfTransport".equalsIgnoreCase(startName))
					transport.setKindOfTransport(reader.getElementText());
				if("transportPriority".equalsIgnoreCase(startName))
					transport.setTransportPriority(reader.getElementText());
				if("longDistanceTrip".equalsIgnoreCase(startName))
					transport.setLongDistanceTrip(Boolean.valueOf(reader.getElementText()));
				if("direction".equalsIgnoreCase(startName))
					transport.setDirection(Integer.valueOf(reader.getElementText()));

				//next
				if("kindOfIllness".equalsIgnoreCase(startName))
					transport.setKindOfIllness(reader.getElementText());
				if("backTransport".equalsIgnoreCase(startName))
					transport.setBackTransport(Boolean.valueOf(reader.getElementText()));
				if("assistantPerson".equalsIgnoreCase(startName))
					transport.setAssistantPerson(Boolean.valueOf(reader.getElementText()));
				if("emergencyPhone".equalsIgnoreCase(startName))
					transport.setEmergencyPhone(Boolean.valueOf(reader.getElementText()));
				if("feedback".equalsIgnoreCase(startName))
					transport.setFeedback(reader.getElementText());

				//next
				if("creationTime".equals(startName))
					transport.setCreationTime(Long.valueOf(reader.getElementText()));
				if("dateOfTransport".equalsIgnoreCase(startName))
					transport.setDateOfTransport(Long.valueOf(reader.getElementText()));
				if("plannedStartOfTransport".equalsIgnoreCase(startName))
					transport.setPlannedStartOfTransport(Long.valueOf(reader.getElementText()));
				if("plannedTimeAtPatient".equalsIgnoreCase(startName))
					transport.setPlannedTimeAtPatient(Long.valueOf(reader.getElementText()));
				if("appointmentTimeAtDestination".equalsIgnoreCase(startName))
					transport.setAppointmentTimeAtDestination(Long.valueOf(reader.getElementText()));
				if(Location.ID.equalsIgnoreCase(startName))
				{
					//get the decoder for the Location
					MessageDecoder decoder = ProtocolCodecFactory.getDefault().getDecoder(Location.ID);
					transport.setRealLocation((Location)decoder.doDecode(reader));
				}
				if("notes".equalsIgnoreCase(startName))
					transport.setNotes(reader.getElementText());
				if("programStatus".equalsIgnoreCase(startName))
					transport.setProgramStatus(Integer.valueOf(reader.getElementText()));
				if("createdByUser".equalsIgnoreCase(startName))
					transport.setCreatedByUsername(reader.getElementText());

				//next
				if("emergencyDoctoralarming".equalsIgnoreCase(startName))
					transport.setEmergencyDoctorAlarming(Boolean.valueOf(reader.getElementText()));
				if("helicopterAlarming".equalsIgnoreCase(startName))
					transport.setHelicopterAlarming(Boolean.valueOf(reader.getElementText()));
				if("blueLightToGoal".equalsIgnoreCase(startName))
					transport.setBlueLightToGoal(Boolean.valueOf(reader.getElementText()));
				if("dfAlarming".equalsIgnoreCase(startName))
					transport.setDfAlarming(Boolean.valueOf(reader.getElementText()));
				if("brkdtAlarming".equalsIgnoreCase(startName))
					transport.setBrkdtAlarming(Boolean.valueOf(reader.getElementText()));
				if("firebrigadeAlarming".equalsIgnoreCase(startName))
					transport.setFirebrigadeAlarming(Boolean.valueOf(reader.getElementText()));
				if("mountainRescueServiceAlarming".equalsIgnoreCase(startName))
					transport.setMountainRescueServiceAlarming(Boolean.valueOf(reader.getElementText()));
				if("policeAlarming".equalsIgnoreCase(startName))
					transport.setPoliceAlarming(Boolean.valueOf(reader.getElementText()));

				//next
				if(VehicleDetail.ID.equalsIgnoreCase(startName))
				{
					//get the decoder for the staff
					MessageDecoder decoder = ProtocolCodecFactory.getDefault().getDecoder(VehicleDetail.ID);
					transport.setVehicleDetail((VehicleDetail)decoder.doDecode(reader));
				}

				//next
				if("transportStatus".equalsIgnoreCase(startName))
				{
					Attribute statusAttr = start.getAttributeByName(new QName("status"));
					Attribute timeAttr = start.getAttributeByName(new QName("time"));
					transport.addStatus(
							Integer.valueOf(statusAttr.getValue()),
							Long.valueOf(timeAttr.getValue()));
				}
			}
			//check for the end element, and return the object
			if(event.isEndElement())
			{
				//get the name
				String endElement = event.asEndElement().getName().getLocalPart();
				System.out.println("End element:"+endElement);
				//check if we have reached the end
				if (Transport.ID.equalsIgnoreCase(endElement))
				{
					System.out.println("returning: "+transport);
					return transport;
				}
			}
		}
		System.out.println("Returning null");
		return null;
	}

}
