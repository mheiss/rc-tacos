package at.rc.tacos.core.xml.codec;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Patient;

public class PatientDecoder implements MessageDecoder
{
    @Override
    public AbstractMessage doDecode(XMLEventReader reader) throws XMLStreamException
    {       
        //The item to decode
        Patient patient = new Patient();
            
        //parse and set up the object
        while(reader.hasNext())
        {
            //the type of the event
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) 
            {
                String startName = event.asStartElement().getName().getLocalPart();
                //create a new item 
                if(Patient.ID.equalsIgnoreCase(startName))
                    patient = new Patient();
                
                //get the type of the element and set the corresponding value
                if("patientId".equalsIgnoreCase(startName))
                    patient.setPatientId(Long.valueOf(reader.getElementText()));
                if("firstname".equalsIgnoreCase(startName))
                    patient.setFirstname(reader.getElementText());
                if("lastname".equalsIgnoreCase(startName))
                    patient.setLastname(reader.getElementText());
            }
            //check for the end element, and return the object
            if(event.isEndElement())
            {
                //get the name
                String endElement = event.asEndElement().getName().getLocalPart();
                //check if we have reached the end
                if (Patient.ID.equalsIgnoreCase(endElement))
                    return patient;
            }
        }
        return null;
    }
}
