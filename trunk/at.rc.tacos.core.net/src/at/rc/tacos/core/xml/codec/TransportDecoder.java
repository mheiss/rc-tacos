package at.rc.tacos.core.xml.codec;

import java.util.Date;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.NotifierDetail;
import at.rc.tacos.model.Patient;
import at.rc.tacos.model.Transport;

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
                String startName = event.asStartElement().getName().getLocalPart();
                //create a new item 
                if(Transport.ID.equalsIgnoreCase(startName))
                    transport = new Transport();
                //get the notifier details
                if(NotifierDetail.ID.equalsIgnoreCase(startName))
                {
                    //get the decoder for the staff
                    MessageDecoder decoder = ProtocolCodecFactory.getDefault().getDecoder(NotifierDetail.ID);
                    transport.setNotifierDetail((NotifierDetail)decoder.doDecode(reader));
                }
                //get the patient details
                if(Patient.ID.equalsIgnoreCase(startName))
                {
                    //get the decoder for the staff
                    MessageDecoder decoder = ProtocolCodecFactory.getDefault().getDecoder(Patient.ID);
                    transport.setPatient((Patient)decoder.doDecode(reader));
                }
                
                //get the type of the element and set the corresponding value
                if("fromStreet".equalsIgnoreCase(startName))
                    transport.setFromStreet(reader.getElementText());
                if("fromNumber".equalsIgnoreCase(startName))
                    transport.setFromNumber(reader.getElementText());
                if("fromCity".equalsIgnoreCase(startName))
                    transport.setFromCity(reader.getElementText());
                if("toStreet".equalsIgnoreCase(startName))
                    transport.setToStreet(reader.getElementText());
                if("toNumber".equalsIgnoreCase(startName))
                    transport.setToNumber(reader.getElementText());
                if("toCity".equalsIgnoreCase(startName))
                    transport.setToCity(reader.getElementText());
                if("kindOfTransport".equalsIgnoreCase(startName))
                    transport.setKindOfTransport(reader.getElementText());
                if("backTransport".equalsIgnoreCase(startName))
                    transport.setBackTransport(Boolean.valueOf(reader.getElementText()));
                if("accompanyingPerson".equalsIgnoreCase(startName))
                    transport.setAccompanyingPerson(Boolean.valueOf(reader.getElementText()));
                if("emergencyPhone".equalsIgnoreCase(startName))
                    transport.setEmergencyPhone(Boolean.valueOf(reader.getElementText()));
                if("kindOfIllness".equalsIgnoreCase(startName))
                    transport.setKindOfIllness(reader.getElementText());
                if("transportNotes".equalsIgnoreCase(startName))
                    transport.setTransportNotes(reader.getElementText());
                if("responsibleStation".equalsIgnoreCase(startName))
                    transport.setResponsibleStation(reader.getElementText());
                if("realStation".equalsIgnoreCase(startName))
                    transport.setRealStation(reader.getElementText());
                if("dateOfTransport".equalsIgnoreCase(startName))
                    transport.setDateOfTransport(new Date(Long.valueOf(reader.getElementText())));
                if("plannedStartOfTransportTime".equalsIgnoreCase(startName))
                    transport.setPlannedStartOfTransportTime(Long.valueOf(reader.getElementText()));
                if("plannedTimeAtPatient".equalsIgnoreCase(startName))
                    transport.setPlannedAtPatientTime(Long.valueOf(reader.getElementText()));
                if("appointmentTimeAtDestination".equalsIgnoreCase(startName))
                    transport.setAppointmentTimeAtDestination(Long.valueOf(reader.getElementText()));
                if("emergencyDoctoralarmingTime".equalsIgnoreCase(startName))
                    transport.setEmergencyDoctorAlarmingTime(Long.valueOf(reader.getElementText()));
                if("helicopterAlarmingTime".equalsIgnoreCase(startName))
                    transport.setHelicopterAlarmingTime(Long.valueOf(reader.getElementText()));
                if("blueLightToPatient".equalsIgnoreCase(startName))
                    transport.setBluelightToPatient(Boolean.valueOf(reader.getElementText()));
                if("blueLightToGoal".equalsIgnoreCase(startName))
                    transport.setBluelightToGoal(Boolean.valueOf(reader.getElementText()));
                if("dfAlarmingTime".equalsIgnoreCase(startName))
                    transport.setDfAlarmingTime(Long.valueOf(reader.getElementText()));
                if("brkdtAlarmingTime".equalsIgnoreCase(startName))
                    transport.setBrkdtAlarmingTime(Long.valueOf(reader.getElementText()));
                if("firebrigadeAlarmingTime".equalsIgnoreCase(startName))
                    transport.setFirebrigadeAlarmingTime(Long.valueOf(reader.getElementText()));
                if("mountainRescueServiceAlarmingTime".equalsIgnoreCase(startName))
                    transport.setMountainRescueServiceAlarmingTime(Long.valueOf(reader.getElementText()));
                if("policeAlarmingTime".equalsIgnoreCase(startName))
                    transport.setPoliceAlarmingTime(Long.valueOf(reader.getElementText()));
                if("feedback".equalsIgnoreCase(startName))
                    transport.setFeedback(reader.getElementText());
                if("towardsGraz".equalsIgnoreCase(startName))
                    transport.setTowardsGraz(Boolean.valueOf(reader.getElementText()));
                if("towardsLeoben".equalsIgnoreCase(startName))
                    transport.setTowardsLeoben(Boolean.valueOf(reader.getElementText()));
                if("towardsWien".equalsIgnoreCase(startName))
                    transport.setTowardsWien(Boolean.valueOf(reader.getElementText()));
                if("towardsMariazell".equalsIgnoreCase(startName))
                    transport.setTowardsMariazell(Boolean.valueOf(reader.getElementText()));
                if("towardsDistrict".equalsIgnoreCase(startName))
                    transport.setTowardsDistrict(Boolean.valueOf(reader.getElementText()));
                if("longDistanceTrip".equalsIgnoreCase(startName))
                    transport.setLongDistanceTrip(Boolean.valueOf(reader.getElementText()));
            }
            //check for the end element, and return the object
            if(event.isEndElement())
            {
                //get the name
                String endElement = event.asEndElement().getName().getLocalPart();
                //check if we have reached the end
                if (Transport.ID.equalsIgnoreCase(endElement))
                    return transport;
            }
        }
        return null;
    }

}
