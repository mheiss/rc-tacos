package at.rc.tacos.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.factory.ProtocolCodecFactory;
import at.rc.tacos.model.CallerDetail;
import at.rc.tacos.model.Patient;
import at.rc.tacos.model.StatusMessages;
import at.rc.tacos.model.Transport;
import at.rc.tacos.model.VehicleDetail;

public class TransportEncoder  implements MessageEncoder
{
	MessageEncoder encoder;
    @Override
    public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException
    {
        //Cast the object to a item
        Transport transport = (Transport)message;
        //start
        writer.writeStartElement(Transport.ID);
        //the transport id
        writer.writeStartElement("transportId");
        writer.writeCharacters(String.valueOf(transport.getTransportId()));
        writer.writeEndElement();
        //the start point of the transport: street
        writer.writeStartElement("fromStreet");
        writer.writeCharacters(transport.getFromStreet());
        writer.writeEndElement();
        //the start point of the transport: city
        writer.writeStartElement("fromCity");
        writer.writeCharacters(transport.getFromCity());
        writer.writeEndElement();
        //get the encoder for the patient
        if (transport.getPatient() != null)
        {
	        encoder = ProtocolCodecFactory.getDefault().getEncoder(Patient.ID);
	        encoder.doEncode(transport.getPatient(), writer);
        }
        //the target street is not mandatory
        if(transport.getToStreet() != null)
        {
            writer.writeStartElement("toStreet");
            writer.writeCharacters(transport.getToStreet());
            writer.writeEndElement();
        }
        //the target city is not mandatory
        if(transport.getToCity() != null)
        {
            writer.writeStartElement("toCity");
            writer.writeCharacters(transport.getToCity());
            writer.writeEndElement();
        }
        //kind of transport is mandatory
        if(transport.getKindOfTransport() != null)
        {
            writer.writeStartElement("kindOfTransport");
            writer.writeCharacters(transport.getKindOfTransport());
            writer.writeEndElement();
        }
        //write the notifier details
        if(transport.getCallerDetail() != null)
        {
            encoder = ProtocolCodecFactory.getDefault().getEncoder(CallerDetail.ID);
            encoder.doEncode(transport.getCallerDetail(), writer);
        }
        //write is the transport is a back transport
        writer.writeStartElement("backTransport");
        writer.writeCharacters(String.valueOf(transport.isBackTransport()));
        writer.writeEndElement();
        //write whether there is a  accompanying person
        writer.writeStartElement("accompanyingPerson");
        writer.writeCharacters(String.valueOf(transport.isAccompanyingPerson()));
        writer.writeEndElement();
        //write the transport has a emergency phone
        writer.writeStartElement("emergencyPhone");
        writer.writeCharacters(String.valueOf(transport.isEmergencyPhone()));
        writer.writeEndElement();
        //kind of illness is not mandatory
        if(transport.getKindOfIllness() != null)
        {
            writer.writeStartElement("kindOfIllness");
            writer.writeCharacters(transport.getKindOfIllness());
            writer.writeEndElement();
        }
        //notes are not mandatory
        if(transport.getDiseaseNotes() != null)
        {
            writer.writeStartElement("transportNotes");
            writer.writeCharacters(transport.getDiseaseNotes());
            writer.writeEndElement();
        }
        //the station responsible
        writer.writeStartElement("responsibleStation");
        writer.writeCharacters(transport.getResponsibleStation());
        writer.writeEndElement();
        //real station is not mandatory
        if(transport.getRealStation() != null)
        {
            writer.writeStartElement("realStation");
            writer.writeCharacters(transport.getRealStation());
            writer.writeEndElement();
        }
        //the date of the transport
        if(transport.getDateOfTransport() >0)
        {
	        writer.writeStartElement("dateOfTransport");
	        writer.writeCharacters(Long.toString(transport.getDateOfTransport()));
	        writer.writeEndElement();
        }
        //the time of receiving the order
        if(transport.getReceiveTime() >0)
        {
        	writer.writeStartElement("receivingTime");
        	writer.writeCharacters(Long.toString(transport.getReceiveTime()));
        	writer.writeEndElement();
        }
        //the starting time
        if(transport.getPlannedStartOfTransport() >0)
        {
	        writer.writeStartElement("plannedStartOfTransportTime");
	        writer.writeCharacters(Long.toString(transport.getPlannedStartOfTransport()));
	        writer.writeEndElement();
        }
        //time at patient is mandatory
        if(transport.getPlannedTimeAtPatient() > 0)
        {
            writer.writeStartElement("plannedTimeAtPatient");
            writer.writeCharacters(Long.toString(transport.getPlannedTimeAtPatient()));
            writer.writeEndElement();
        }
        //appointment time is mandatory
        if(transport.getAppointmentTimeAtDestination() > 0)
        {
            writer.writeStartElement("appointmentTimeAtDestination");
            writer.writeCharacters(Long.toString(transport.getAppointmentTimeAtDestination()));
            writer.writeEndElement();
        }
        //write the element and attributes
        writer.writeStartElement("transportPriority");
        writer.writeCharacters(transport.getTransportPriority());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("emergencyDoctoralarming");
        writer.writeCharacters(Boolean.toString(transport.isEmergencyDoctorAlarming()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("helicopterAlarmingTime");
        writer.writeCharacters(Boolean.toString(transport.isHelicopterAlarming()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("blueLightToGoal");
        writer.writeCharacters(Boolean.toString(transport.isBlueLightToGoal()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("dfAlarming");
        writer.writeCharacters(Boolean.toString(transport.isDfAlarming()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("brkdtAlarming");
        writer.writeCharacters(Boolean.toString(transport.isBrkdtAlarming()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("firebrigadeAlarming");
        writer.writeCharacters(Boolean.toString(transport.isFirebrigadeAlarming()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("mountainRescueServiceAlarming");
        writer.writeCharacters(Boolean.toString(transport.isMountainRescueServiceAlarming()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("policeAlarming");
        writer.writeCharacters(Boolean.toString(transport.isPoliceAlarming()));
        writer.writeEndElement();
        //feedback is not mandatory
        if(transport.getFeedback() != null)
        {
            writer.writeStartElement("feedback");
            writer.writeCharacters(transport.getFeedback());
            writer.writeEndElement();
        }
        //write the elements and attributes
        writer.writeStartElement("direction");
        writer.writeCharacters(Integer.toString(transport.getDirection()));
        writer.writeEndElement();
        //get the encoder for the vehicle
        if(transport.getVehicleDetail() != null)
        {
        encoder = ProtocolCodecFactory.getDefault().getEncoder(VehicleDetail.ID);
        encoder.doEncode(transport.getVehicleDetail(), writer);
        }
        //encode the status messages
        for(StatusMessages statusMessage:transport.getStatusMessages())
        {
            writer.writeStartElement("statusMessage");
            writer.writeAttribute("status", Integer.toString(statusMessage.getStatus()));
            writer.writeAttribute("time", Long.toString(statusMessage.getTimestamp()));
            writer.writeEndElement();
        }
        //end
        writer.writeEndElement();
    }
}
