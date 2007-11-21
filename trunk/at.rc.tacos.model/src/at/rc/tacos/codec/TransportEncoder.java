package at.rc.tacos.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.factory.ProtocolCodecFactory;
import at.rc.tacos.model.NotifierDetail;
import at.rc.tacos.model.Patient;
import at.rc.tacos.model.Transport;

public class TransportEncoder  implements MessageEncoder
{
    @Override
    public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException
    {
        //Cast the object to a item
        Transport transport = (Transport)message;

        //start
        writer.writeStartElement(Transport.ID);

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
        writer.writeCharacters(Long.toString(transport.getDateOfTransport().getTime()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("plannedStartOfTransportTime");
        writer.writeCharacters(Long.toString(transport.getPlannedStartOfTransportTime()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("plannedTimeAtPatient");
        writer.writeCharacters(Long.toString(transport.getPlannedStartOfTransportTime()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("appointmentTimeAtDestination");
        writer.writeCharacters(Long.toString(transport.getAppointmentTimeAtDestination()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("emergencyDoctoralarmingTime");
        writer.writeCharacters(Long.toString(transport.getEmergencyDoctorAlarmingTime()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("helicopterAlarmingTime");
        writer.writeCharacters(Long.toString(transport.getHelicopterAlarmingTime()));
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
        writer.writeStartElement("dfAlarmingTime");
        writer.writeCharacters(Long.toString(transport.getDfAlarmingTime()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("brkdtAlarmingTime");
        writer.writeCharacters(Long.toString(transport.getBrkdtAlarmingTime()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("firebrigadeAlarmingTime");
        writer.writeCharacters(Long.toString(transport.getFirebrigadeAlarmingTime()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("mountainRescueServiceAlarmingTime");
        writer.writeCharacters(Long.toString(transport.getMountainRescueServiceAlarmingTime()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("policeAlarmingTime");
        writer.writeCharacters(Long.toString(transport.getPoliceAlarmingTime()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("feedback");
        writer.writeCharacters(transport.getFeedback());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("towardsGraz");
        writer.writeCharacters(Boolean.toString(transport.isTowardsGraz()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("towardsLeoben");
        writer.writeCharacters(Boolean.toString(transport.isTowardsLeoben()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("towardsWien");
        writer.writeCharacters(Boolean.toString(transport.isTowardsWien()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("towardsMariazell");
        writer.writeCharacters(Boolean.toString(transport.isTowardsMariazell()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("towardsDistrict");
        writer.writeCharacters(Boolean.toString(transport.isTowardsDistrict()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("longDistanceTrip");
        writer.writeCharacters(Boolean.toString(transport.isLongDistanceTrip()));
        writer.writeEndElement();

        //end
        writer.writeEndElement();
    }

}
