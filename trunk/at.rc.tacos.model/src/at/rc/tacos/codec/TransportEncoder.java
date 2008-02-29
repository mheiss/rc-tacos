package at.rc.tacos.codec;

import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.factory.ProtocolCodecFactory;
import at.rc.tacos.model.CallerDetail;
import at.rc.tacos.model.Disease;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Patient;
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
        
        //assert valid
        if(transport ==  null)
        {
            System.out.println("WARNING: Object transport is null and cannot be encoded");
            return;
        }
        
        //start
        writer.writeStartElement(Transport.ID);

        //first
        writer.writeStartElement("transportId");
        writer.writeCharacters(String.valueOf(transport.getTransportId()));
        writer.writeEndElement();
        if(transport.getYear() > 0)
        {
	        writer.writeStartElement("year");
	        writer.writeCharacters(String.valueOf(transport.getYear()));
	        writer.writeEndElement();
        }
   
        //next
	    writer.writeStartElement("transportNumber");
	    writer.writeCharacters(String.valueOf(transport.getTransportNumber()));
	    writer.writeEndElement();
        

        //next
        if(transport.getCallerDetail() != null)
        {
            encoder = ProtocolCodecFactory.getDefault().getEncoder(CallerDetail.ID);
            encoder.doEncode(transport.getCallerDetail(), writer);
        }

        //next
        writer.writeStartElement("fromStreet");
        writer.writeCharacters(transport.getFromStreet());
        writer.writeEndElement();
        if(transport.getFromCity() != null)
        {
	        writer.writeStartElement("fromCity");
	        writer.writeCharacters(transport.getFromCity());
	        writer.writeEndElement();
        }
        if (transport.getPatient() != null)
        {
            encoder = ProtocolCodecFactory.getDefault().getEncoder(Patient.ID);
            encoder.doEncode(transport.getPatient(), writer);
        }
        if(transport.getToStreet() != null)
        {
            writer.writeStartElement("toStreet");
            writer.writeCharacters(transport.getToStreet());
            writer.writeEndElement();
        }
        if(transport.getToCity() != null)
        {
            writer.writeStartElement("toCity");
            writer.writeCharacters(transport.getToCity());
            writer.writeEndElement();
        }
        if(transport.getKindOfTransport() != null)
        {
            writer.writeStartElement("kindOfTransport");
            writer.writeCharacters(transport.getKindOfTransport());
            writer.writeEndElement();
        }
        writer.writeStartElement("transportPriority");
        writer.writeCharacters(transport.getTransportPriority());
        writer.writeEndElement();
        writer.writeStartElement("longDistanceTrip");
        writer.writeCharacters(String.valueOf(transport.isLongDistanceTrip()));
        writer.writeEndElement();
        writer.writeStartElement("direction");
        writer.writeCharacters(Integer.toString(transport.getDirection()));
        writer.writeEndElement();

        //next
        if(transport.getKindOfIllness() != null)
        {
        	encoder = ProtocolCodecFactory.getDefault().getEncoder(Disease.ID);
        	encoder.doEncode(transport.getKindOfIllness(), writer);
        }
        
//      //vehicle
//        if(transport.getVehicleDetail() != null)
//        {
//            encoder = ProtocolCodecFactory.getDefault().getEncoder(VehicleDetail.ID);
//            encoder.doEncode(transport.getVehicleDetail(), writer);
//        }
        writer.writeStartElement("backTransport");
        writer.writeCharacters(String.valueOf(transport.isBackTransport()));
        writer.writeEndElement();
        writer.writeStartElement("assistantPerson");
        writer.writeCharacters(String.valueOf(transport.isAssistantPerson()));
        writer.writeEndElement();
        writer.writeStartElement("emergencyPhone");
        writer.writeCharacters(String.valueOf(transport.isEmergencyPhone()));
        writer.writeEndElement();
        if(transport.getFeedback() != null)
        {
            writer.writeStartElement("feedback");
            writer.writeCharacters(transport.getFeedback());
            writer.writeEndElement();
        }

        //next
        if(transport.getCreationTime() >0)
        {
            writer.writeStartElement("creationTime");
            writer.writeCharacters(Long.toString(transport.getCreationTime()));
            writer.writeEndElement();
        }
        if(transport.getDateOfTransport() >0)
        {
            writer.writeStartElement("dateOfTransport");
            writer.writeCharacters(Long.toString(transport.getDateOfTransport()));
            writer.writeEndElement();
        }
        if(transport.getPlannedStartOfTransport() >0)
        {
            writer.writeStartElement("plannedStartOfTransport");
            writer.writeCharacters(Long.toString(transport.getPlannedStartOfTransport()));
            writer.writeEndElement();
        }
        if(transport.getPlannedTimeAtPatient() > 0)
        {
            writer.writeStartElement("plannedTimeAtPatient");
            writer.writeCharacters(Long.toString(transport.getPlannedTimeAtPatient()));
            writer.writeEndElement();
        }
        if(transport.getAppointmentTimeAtDestination() > 0)
        {
            writer.writeStartElement("appointmentTimeAtDestination");
            writer.writeCharacters(Long.toString(transport.getAppointmentTimeAtDestination()));
            writer.writeEndElement();
        }

        //next
        MessageEncoder encoder = ProtocolCodecFactory.getDefault().getEncoder(Location.ID);
        if(transport.getPlanedLocation() != null)
        	encoder.doEncode(transport.getPlanedLocation(), writer);
        
        if(transport.getNotes() != null)
        {
            writer.writeStartElement("notes");
            writer.writeCharacters(transport.getNotes());
            writer.writeEndElement();
        }
        if(transport.getProgramStatus() > 0)
        {
	        writer.writeStartElement("programStatus");
	        writer.writeCharacters(Integer.toString(transport.getProgramStatus()));
	        writer.writeEndElement();
        }
        writer.writeStartElement("createdByUser");
        writer.writeCharacters(transport.getCreatedByUsername());
        writer.writeEndElement();
        
        //next
        writer.writeStartElement("emergencyDoctoralarming");
        writer.writeCharacters(Boolean.toString(transport.isEmergencyDoctorAlarming()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("helicopterAlarming");
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
        
        //vehicle
        if(transport.getVehicleDetail() != null)
        {
            encoder = ProtocolCodecFactory.getDefault().getEncoder(VehicleDetail.ID);
            encoder.doEncode(transport.getVehicleDetail(), writer);
        }
        
        //encode the status messages
        for(Entry<Integer,Long> entry:transport.getStatusMessages().entrySet())
        {
            writer.writeStartElement("transportStatus");
            writer.writeAttribute("status", Integer.toString(entry.getKey()));
            writer.writeAttribute("time", Long.toString(entry.getValue()));
            writer.writeEndElement();
        }
        //end
        writer.writeEndElement();
    }
}
