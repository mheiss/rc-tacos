package at.rc.tacos.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.factory.ProtocolCodecFactory;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.VehicleDetail;

public class VehicleEncoder  implements MessageEncoder
{
    @Override
    public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException
    {
        //Cast the object to a item
        VehicleDetail vehicle = (VehicleDetail)message;

        //start
        writer.writeStartElement(VehicleDetail.ID);

        //write the elements and attributes
        writer.writeStartElement("vehicleId");
        writer.writeCharacters(String.valueOf(vehicle.getVehicleId()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("vehicleName");
        writer.writeCharacters(vehicle.getVehicleName());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("vehicleType");
        writer.writeCharacters(vehicle.getVehicleType());
        writer.writeEndElement();
        //get the encoder for the staff member
        if(vehicle.getDriverName() != null)
        {
	        vehicle.getDriverName().setFunction("driver");
	        MessageEncoder encoder = ProtocolCodecFactory.getDefault().getEncoder(StaffMember.ID);
	        encoder.doEncode(vehicle.getDriverName(), writer);
        }
        //write the elements and attributes
        if(vehicle.getParamedicIName()!= null)
        {
	        vehicle.getParamedicIName().setFunction("medic1");
	        MessageEncoder encoder = ProtocolCodecFactory.getDefault().getEncoder(StaffMember.ID);
	        encoder.doEncode(vehicle.getParamedicIName(), writer);
        }
        //write the elements and attributes
        if(vehicle.getParamedicIIName()!= null)
        {
	        vehicle.getParamedicIIName().setFunction("medic2");
	        MessageEncoder encoder = ProtocolCodecFactory.getDefault().getEncoder(StaffMember.ID);
	        encoder.doEncode(vehicle.getParamedicIIName(), writer);
        }
        //get the encoder for the mobile phone
        if(vehicle.getMobilePhone()!= null)
        {
	        MessageEncoder encoder = ProtocolCodecFactory.getDefault().getEncoder(MobilePhoneDetail.ID);
	        encoder.doEncode(vehicle.getMobilePhone(), writer);
        }
        //write the elements and attributes
        writer.writeStartElement("vehicleNotes");
        writer.writeCharacters(vehicle.getVehicleNotes());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("basicStation");
        writer.writeCharacters(vehicle.getBasicStation());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("currentStation");
        writer.writeCharacters(vehicle.getCurrentStation());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("readyForAction");
        writer.writeCharacters(String.valueOf(vehicle.isReadyForAction()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("outOfOrder");
        writer.writeCharacters(String.valueOf(vehicle.isOutOfOrder()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("mostImportantTransportStatus");
        writer.writeCharacters(String.valueOf(vehicle.getMostImportantTransportStatus()));
        writer.writeEndElement();

        //end
        writer.writeEndElement();
    }
}
