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
        //write the patient id
        writer.writeStartElement("patientId");
        writer.writeCharacters(String.valueOf(patient.getPatientId()));
        writer.writeEndElement();
        //name is not mandatory -> could be that the name is just not known
        if(patient.getFirstname() != null)
        {
            writer.writeStartElement("firstname");
            writer.writeCharacters(patient.getFirstname());
            writer.writeEndElement();
        }
        //name is not mandatory -> could be that the name is just not known
        if(patient.getLastname() != null)
        {
            writer.writeStartElement("lastname");
            writer.writeCharacters(patient.getLastname());
            writer.writeEndElement();
        } 
        writer.writeStartElement("sex");
        writer.writeCharacters(String.valueOf(patient.isMale()));
        writer.writeEndElement();
        if(patient.getBirthday() > 0)
        {
            writer.writeStartElement("birthday");
            writer.writeCharacters(String.valueOf(patient.getBirthday()));
            writer.writeEndElement();
        } 
        //end of the item
        writer.writeEndElement();
    }
}
