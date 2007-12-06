package at.rc.tacos.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.factory.ProtocolCodecFactory;
import at.rc.tacos.model.NotifierDetail;
import at.rc.tacos.model.Patient;
import at.rc.tacos.model.StatusMessages;
import at.rc.tacos.model.Transport;
import at.rc.tacos.model.VehicleDetail;

public class TransportEncoder  implements MessageEncoder
{
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
        
        //write the elements and attributes
        writer.writeStartElement("fromStreet");
        writer.writeCharacters(transport.getFromStreet());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("fromNumber");
        writer.writeCharacters(transport.getFromNumber());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("fromCity");
        writer.writeCharacters(transport.getFromCity());
        writer.writeEndElement();
        //get the encoder for the patient
        MessageEncoder encoder = ProtocolCodecFactory.getDefault().getEncoder(Patient.ID);
        encoder.doEncode(transport.getPatient(), writer);
        //write the elements and attributes
        writer.writeStartElement("toStreet");
        writer.writeCharacters(transport.getToStreet());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("toNumber");
        writer.writeCharacters(transport.getToNumber());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("toCity");
        writer.writeCharacters(transport.getToCity());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("kindOfTransport");
        writer.writeCharacters(transport.getKindOfTransport());
        writer.writeEndElement();
        //get the encoder for the patient
        encoder = ProtocolCodecFactory.getDefault().getEncoder(NotifierDetail.ID);
        encoder.doEncode(transport.getNotifierDetail(), writer);
        //write the elements and attributes
        writer.writeStartElement("backTransport");
        writer.writeCharacters(String.valueOf(transport.isBackTransport()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("accompanyingPerson");
        writer.writeCharacters(String.valueOf(transport.isAccompanyingPerson()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("emergencyPhone");
        writer.writeCharacters(String.valueOf(transport.isEmergencyPhone()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("kindOfIllness");
        writer.writeCharacters(transport.getKindOfIllness());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("transportNotes");
        writer.writeCharacters(transport.getTransportNotes());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("responsibleStation");
        writer.writeCharacters(transport.getResponsibleStation());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("realStation");
        writer.writeCharacters(transport.getRealStation());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("dateOfTransport");
        writer.writeCharacters(Long.toString(transport.getDateOfTransport()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("plannedStartOfTransportTime");
        writer.writeCharacters(Long.toString(transport.getPlannedStartOfTransport()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("plannedTimeAtPatient");
        writer.writeCharacters(Long.toString(transport.getPlannedTimeAtPatient()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("appointmentTimeAtDestination");
        writer.writeCharacters(Long.toString(transport.getAppointmentTimeAtDestination()));
        writer.writeEndElement();
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
        writer.writeStartElement("blueLightToPatient");
        writer.writeCharacters(Boolean.toString(transport.isBluelightToPatient()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("blueLightToGoal");
        writer.writeCharacters(Boolean.toString(transport.isBluelightToGoal()));
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
        //write the elements and attributes
        writer.writeStartElement("feedback");
        writer.writeCharacters(transport.getFeedback());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("directness");
        writer.writeCharacters(Integer.toString(transport.getDirectness()));
        writer.writeEndElement();
        //get the encoder for the vehicle
        encoder = ProtocolCodecFactory.getDefault().getEncoder(VehicleDetail.ID);
        encoder.doEncode(transport.getVehicleDetail(), writer);
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
