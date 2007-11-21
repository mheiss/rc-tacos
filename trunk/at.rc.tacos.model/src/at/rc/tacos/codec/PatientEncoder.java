package at.rc.tacos.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Patient;

public class PatientEncoder implements MessageEncoder
{
    @Override
    public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException
    {
        //Cast the object to a item
        Patient patient = (Patient)message;
        
        //write the start element
        writer.writeStartElement(Patient.ID);
       
        //write the elements and attributes
        writer.writeStartElement("patientId");
        writer.writeCharacters(String.valueOf(patient.getPatientId()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("firstname");
        writer.writeCharacters(patient.getFirstname());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("lastname");
        writer.writeCharacters(patient.getLastname());
        writer.writeEndElement();
        
        //end of the item
        writer.writeEndElement();
    }
}
