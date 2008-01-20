package at.rc.tacos.codec;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.factory.ProtocolCodecFactory;
import at.rc.tacos.model.DialysisPatient;
import at.rc.tacos.model.Patient;

public class DialysisDecoder implements MessageDecoder
{
    @Override
    public AbstractMessage doDecode(XMLEventReader reader)  throws XMLStreamException
    {
        //Create a new transport
    	DialysisPatient dia = new DialysisPatient();
            
        //parse and set up the object
        while(reader.hasNext())
        {
            //the type of the event
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) 
            {
                String startName = event.asStartElement().getName().getLocalPart();
                //create a new item 
                if(DialysisPatient.ID.equalsIgnoreCase(startName))
                    dia = new DialysisPatient();
                
                //get the patient details
                if(Patient.ID.equalsIgnoreCase(startName))
                {
                    //get the decoder for the staff
                    MessageDecoder decoder = ProtocolCodecFactory.getDefault().getDecoder(Patient.ID);
                    dia.setPatient((Patient)decoder.doDecode(reader));
                }
                
         
                //get the type of the element and set the corresponding value
                if("patientId".equalsIgnoreCase(startName))
                    dia.setPatientId(Long.valueOf(reader.getElementText()));   
                if("fromStreet".equalsIgnoreCase(startName))
                    dia.setFromStreet(reader.getElementText());
                if("fromCity".equalsIgnoreCase(startName))
                    dia.setFromCity(reader.getElementText());
                if("toStreet".equalsIgnoreCase(startName))
                    dia.setToStreet(reader.getElementText());
                if("toCity".equalsIgnoreCase(startName))
                    dia.setToCity(reader.getElementText());
                if("kindOfTransport".equalsIgnoreCase(startName))
                    dia.setKindOfTransport(reader.getElementText());
                if("accompanyingPerson".equalsIgnoreCase(startName))
                    dia.setAccompanyingPerson(Boolean.valueOf(reader.getElementText())); 
                if("station".equalsIgnoreCase(startName))
                    dia.setStation(reader.getElementText());
                if("insurance".equalsIgnoreCase(startName))
                    dia.setInsurance(reader.getElementText());
                if("plannedStartOfTransportTime".equalsIgnoreCase(startName))
                    dia.setPlannedStartOfTransport(Long.valueOf(reader.getElementText()));
                if("plannedTimeAtPatient".equalsIgnoreCase(startName))
                    dia.setPlannedTimeAtPatient(Long.valueOf(reader.getElementText()));
                if("appointmentTimeAtDestination".equalsIgnoreCase(startName))
                    dia.setAppointmentTimeAtDialysis(Long.valueOf(reader.getElementText()));
                if("plannedStartForBackTransport".equalsIgnoreCase(startName))
                    dia.setplannedStartForBackTransport(Long.valueOf(reader.getElementText()));
                if("readyTime".equalsIgnoreCase(startName))
                    dia.setreadyTime(Long.valueOf(reader.getElementText()));
                if("monday".equalsIgnoreCase(startName))
                    dia.setMonday(Boolean.valueOf(reader.getElementText()));
                if("tuesday".equalsIgnoreCase(startName))
                    dia.setTuesday(Boolean.valueOf(reader.getElementText()));
                if("wednesday".equalsIgnoreCase(startName))
                    dia.setWednesday(Boolean.valueOf(reader.getElementText()));
                if("thursday".equalsIgnoreCase(startName))
                    dia.setThursday(Boolean.valueOf(reader.getElementText()));
                if("friday".equalsIgnoreCase(startName))
                    dia.setFriday(Boolean.valueOf(reader.getElementText()));
                if("saturday".equalsIgnoreCase(startName))
                    dia.setSaturday(Boolean.valueOf(reader.getElementText()));
                if("sunday".equalsIgnoreCase(startName))
                    dia.setSunday(Boolean.valueOf(reader.getElementText()));
                if("stationary".equalsIgnoreCase(startName))
                    dia.setStationary(Boolean.valueOf(reader.getElementText())); 
            }
            //check for the end element, and return the object
            if(event.isEndElement())
            {
                //get the name
                String endElement = event.asEndElement().getName().getLocalPart();
                //check if we have reached the end
                if (DialysisPatient.ID.equalsIgnoreCase(endElement))
                    return dia;
            }
        }
        return null;
    }

}
