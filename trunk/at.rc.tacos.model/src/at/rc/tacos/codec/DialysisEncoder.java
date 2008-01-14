package at.rc.tacos.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.factory.ProtocolCodecFactory;
import at.rc.tacos.model.CallerDetail;
import at.rc.tacos.model.DialysisPatient;
import at.rc.tacos.model.Patient;
import at.rc.tacos.model.StatusMessages;
import at.rc.tacos.model.Transport;
import at.rc.tacos.model.VehicleDetail;

public class DialysisEncoder  implements MessageEncoder
{
	MessageEncoder encoder;
    @Override
    public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException
    {
        //Cast the object to a item
        DialysisPatient dia = (DialysisPatient)message;
        //start
        writer.writeStartElement(DialysisPatient.ID);
        //the transport id
        writer.writeStartElement("transportId");
        writer.writeCharacters(String.valueOf(dia.getPatientId()));
        writer.writeEndElement();
        //the start point of the transport: street
        writer.writeStartElement("fromStreet");
        writer.writeCharacters(dia.getFromStreet());
        writer.writeEndElement();
        //the start point of the transport: city
        writer.writeStartElement("fromCity");
        writer.writeCharacters(dia.getFromCity());
        writer.writeEndElement();
        //get the encoder for the patient
        if (dia.getPatient() != null)
        {
	        encoder = ProtocolCodecFactory.getDefault().getEncoder(Patient.ID);
	        encoder.doEncode(dia.getPatient(), writer);
        }
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
        //kind of transport is mandatory
        if(dia.getKindOfTransport() != null)
        {
            writer.writeStartElement("kindOfTransport");
            writer.writeCharacters(dia.getKindOfTransport());
            writer.writeEndElement();
        }
       
        
        
       
        //write whether there is a  accompanying person
        writer.writeStartElement("accompanyingPerson");
        writer.writeCharacters(String.valueOf(dia.isAccompanyingPerson()));
        writer.writeEndElement();
       
     
        
        //the station responsible
        writer.writeStartElement("station");
        writer.writeCharacters(dia.getStation());
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
            writer.writeStartElement("appointmentTimeAtDestination");
            writer.writeCharacters(Long.toString(dia.getAppointmentTimeAtDialysis()));
            writer.writeEndElement();
        }
        //plannedStartForBackTransport
        if(dia.getplannedStartForBackTransport() > 0)
        {
            writer.writeStartElement("plannedStartForBackTransport");
            writer.writeCharacters(Long.toString(dia.getplannedStartForBackTransport()));
            writer.writeEndElement();
        }
        //readyTime
        if(dia.getreadyTime() > 0)
        {
            writer.writeStartElement("readyTime");
            writer.writeCharacters(Long.toString(dia.getreadyTime()));
            writer.writeEndElement();
        }
        
        if(dia.getInsurance() != null)
        {
        	writer.writeStartElement("insurance");
	        writer.writeCharacters(dia.getInsurance());
	        writer.writeEndElement();
        }
        
        //write the elements and attributes
        writer.writeStartElement("monday");
        writer.writeCharacters(Boolean.toString(dia.isMonday()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("tuesday");
        writer.writeCharacters(Boolean.toString(dia.isTuesday()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("wednesday");
        writer.writeCharacters(Boolean.toString(dia.isWednesday()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("thursday");
        writer.writeCharacters(Boolean.toString(dia.isThursday()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("friday");
        writer.writeCharacters(Boolean.toString(dia.isFriday()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("saturday");
        writer.writeCharacters(Boolean.toString(dia.isSaturday()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("sunday");
        writer.writeCharacters(Boolean.toString(dia.isSunday()));
        writer.writeEndElement();
        
        //write the elements and attributes
        writer.writeStartElement("stationary");
        writer.writeCharacters(Boolean.toString(dia.isStationary()));
        writer.writeEndElement();
        //end
        writer.writeEndElement();
    }
}
