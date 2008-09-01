package at.rc.tacos.platform.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.platform.factory.ProtocolCodecFactory;
import at.rc.tacos.platform.model.AbstractMessage;
import at.rc.tacos.platform.model.Location;
import at.rc.tacos.platform.model.MobilePhoneDetail;
import at.rc.tacos.platform.model.StaffMember;
import at.rc.tacos.platform.model.VehicleDetail;

public class VehicleEncoder implements MessageEncoder
{
    @Override
    public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException
    {
        //Cast the object to a item
        VehicleDetail vehicle = (VehicleDetail)message;
        
        //assert valid
        if(vehicle ==  null)
        {
            System.out.println("WARNING: Object vehicle is null and cannot be encoded");
            return;
        }

        //start
        writer.writeStartElement(VehicleDetail.ID);

        //write the elements and attributes
        if(vehicle.getVehicleName() != null)
        {
	        writer.writeStartElement("vehicleName");
	        writer.writeCharacters(vehicle.getVehicleName());
	        writer.writeEndElement();
        }
        //write the elements and attributes
        if(vehicle.getVehicleType() != null)
        {
	        writer.writeStartElement("vehicleType");
	        writer.writeCharacters(vehicle.getVehicleType());
	        writer.writeEndElement();
        }
        //get the encoder for the staff member
        if(vehicle.getDriver() != null)
        {
	        vehicle.getDriver().function = "driver";
	        MessageEncoder encoder = ProtocolCodecFactory.getDefault().getEncoder(StaffMember.ID);
	        encoder.doEncode(vehicle.getDriver(), writer);
        }
        //write the elements and attributes
        if(vehicle.getFirstParamedic()!= null)
        {
	        vehicle.getFirstParamedic().function = "firstParamedic";
	        MessageEncoder encoder = ProtocolCodecFactory.getDefault().getEncoder(StaffMember.ID);
	        encoder.doEncode(vehicle.getFirstParamedic(), writer);
        }
        //write the elements and attributes
        if(vehicle.getSecondParamedic()!= null)
        {
	        vehicle.getSecondParamedic().function = "secondParamedic";
	        MessageEncoder encoder = ProtocolCodecFactory.getDefault().getEncoder(StaffMember.ID);
	        encoder.doEncode(vehicle.getSecondParamedic(), writer);
        }
        //get the encoder for the mobile phone
        if(vehicle.getMobilePhone()!= null)
        {
	        MessageEncoder encoder = ProtocolCodecFactory.getDefault().getEncoder(MobilePhoneDetail.ID);
	        encoder.doEncode(vehicle.getMobilePhone(), writer);
        }
        //write the elements and attributes
        if(vehicle.getVehicleNotes() != null)
        {
	        writer.writeStartElement("vehicleNotes");
	        writer.writeCharacters(vehicle.getVehicleNotes());
	        writer.writeEndElement();
        }
        
        if(vehicle.getLastDestinationFree() != null)
        {
        	writer.writeStartElement("lastDestinationFree");
        	writer.writeCharacters(vehicle.getLastDestinationFree());
        	writer.writeEndElement();
        }
        
        //write the elements and attributes
        if(vehicle.getCurrentStation() != null)
        {
            vehicle.getCurrentStation().type = "current";
            MessageEncoder encoder = ProtocolCodecFactory.getDefault().getEncoder(Location.ID);
            encoder.doEncode(vehicle.getCurrentStation(), writer);
        }
        if(vehicle.getBasicStation() != null)
        {
            vehicle.getBasicStation().type = "basic";
            MessageEncoder encoder = ProtocolCodecFactory.getDefault().getEncoder(Location.ID);
            encoder.doEncode(vehicle.getBasicStation(), writer);
        }
        
        //write the elements and attributes
        writer.writeStartElement("readyForAction");
        writer.writeCharacters(String.valueOf(vehicle.isReadyForAction()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("outOfOrder");
        writer.writeCharacters(String.valueOf(vehicle.isOutOfOrder()));
        writer.writeEndElement();
        //write the elements and attributes
        if(vehicle.getTransportStatus() > 0)
        {
	        writer.writeStartElement("transportStatus");
	        writer.writeCharacters(String.valueOf(vehicle.getTransportStatus()));
	        writer.writeEndElement();
        }

        //end
        writer.writeEndElement();
    }
}