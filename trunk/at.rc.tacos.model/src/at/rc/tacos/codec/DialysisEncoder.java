package at.rc.tacos.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.factory.ProtocolCodecFactory;
import at.rc.tacos.model.DialysisPatient;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Patient;


public class DialysisEncoder  implements MessageEncoder
{
	MessageEncoder encoder;
    @Override
    public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException
    {
        //Cast the object to a item
        DialysisPatient dia = (DialysisPatient)message;
        
        //assert valid
        if(dia ==  null)
        {
            System.out.println("WARNING: Object dialysis is null and cannot be encoded");
            return;
        }
        
        //start
        writer.writeStartElement(DialysisPatient.ID);
        //the transport id
        writer.writeStartElement("id");
        writer.writeCharacters(String.valueOf(dia.getId()));
        writer.writeEndElement();
        //get the encoder for the patient
        if (dia.getPatient() != null)
        {
            encoder = ProtocolCodecFactory.getDefault().getEncoder(Patient.ID);
            encoder.doEncode(dia.getPatient(), writer);
        }
        //get the encoder for the location
        if (dia.getLocation() != null)
        {
            encoder = ProtocolCodecFactory.getDefault().getEncoder(Location.ID);
            encoder.doEncode(dia.getLocation(), writer);
        }
        //the start point of the transport: street
        writer.writeStartElement("fromStreet");
        writer.writeCharacters(dia.getFromStreet());
        writer.writeEndElement();
        //the start point of the transport: city
        writer.writeStartElement("fromCity");
        writer.writeCharacters(dia.getFromCity());
        writer.writeEndElement();
        //the target street is not mandatory
        if(dia.getToStreet() != null)
        {
            writer.writeStartElement("toStreet");
            writer.writeCharacters(dia.getToStreet());
            writer.writeEndElement();
        }
        //the target city is not mandatory
        if(dia.getToCity() != null)
        {
            writer.writeStartElement("toCity");
            writer.writeCharacters(dia.getToCity());
            writer.writeEndElement();
        }
        if(dia.getInsurance() != null)
        {
            writer.writeStartElement("insurance");
            writer.writeCharacters(dia.getInsurance());
            writer.writeEndElement();
        }
        //kind of transport is mandatory
        if(dia.getKindOfTransport() != null)
        {
            writer.writeStartElement("kindOfTransport");
            writer.writeCharacters(dia.getKindOfTransport());
            writer.writeEndElement();
        }
        //write whether there is a  accompanying person
        writer.writeStartElement("assistantPerson");
        writer.writeCharacters(String.valueOf(dia.isAssistantPerson()));
        writer.writeEndElement();  
        
        //write the elements and attributes
        writer.writeStartElement("stationary");
        writer.writeCharacters(String.valueOf(dia.isStationary()));
        writer.writeEndElement();

        //the starting time
        if(dia.getPlannedStartOfTransport() >0)
        {
	        writer.writeStartElement("plannedStartOfTransport");
	        writer.writeCharacters(Long.toString(dia.getPlannedStartOfTransport()));
	        writer.writeEndElement();
        }
        //time at patient is mandatory
        if(dia.getPlannedTimeAtPatient() > 0)
        {
            writer.writeStartElement("plannedTimeAtPatient");
            writer.writeCharacters(Long.toString(dia.getPlannedTimeAtPatient()));
            writer.writeEndElement();
        }
        //appointment time is mandatory
        if(dia.getAppointmentTimeAtDialysis() > 0)
        {
            writer.writeStartElement("appointmentTimeAtDialysis");
            writer.writeCharacters(Long.toString(dia.getAppointmentTimeAtDialysis()));
            writer.writeEndElement();
        }
        //plannedStartForBackTransport
        if(dia.getPlannedStartForBackTransport() > 0)
        {
            writer.writeStartElement("plannedStartForBackTransport");
            writer.writeCharacters(Long.toString(dia.getPlannedStartForBackTransport()));
            writer.writeEndElement();
        }
        //readyTime
        if(dia.getReadyTime() > 0)
        {
            writer.writeStartElement("readyTime");
            writer.writeCharacters(Long.toString(dia.getReadyTime()));
            writer.writeEndElement();
        }

        //write the elements and attributes
        writer.writeStartElement("monday");
        writer.writeCharacters(String.valueOf(dia.isMonday()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("tuesday");
        writer.writeCharacters(String.valueOf(dia.isTuesday()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("wednesday");
        writer.writeCharacters(String.valueOf(dia.isWednesday()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("thursday");
        writer.writeCharacters(String.valueOf(dia.isThursday()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("friday");
        writer.writeCharacters(String.valueOf(dia.isFriday()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("saturday");
        writer.writeCharacters(String.valueOf(dia.isSaturday()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("sunday");
        writer.writeCharacters(String.valueOf(dia.isSunday()));
        writer.writeEndElement();
        //write the elements
        writer.writeStartElement("lastTransportDate");
        writer.writeCharacters(String.valueOf(dia.getLastTransportDate()));
        writer.writeEndElement();
        //write the eleents
        writer.writeStartElement("lastBackTransportDate");
        writer.writeCharacters(String.valueOf(dia.getLastBackTransporDate()));
        writer.writeEndElement();
        
        //end
        writer.writeEndElement();
    }
}
