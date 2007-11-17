package at.rc.tacos.core.xml.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.MobilePhoneDetail;
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
        //write the elements and attributes
        writer.writeStartElement("driverName");
        writer.writeCharacters(vehicle.getDriverName());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("paramedicIName");
        writer.writeCharacters(vehicle.getParamedicIName());
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("paramedicIIName");
        writer.writeCharacters(vehicle.getParamedicIIName());
        writer.writeEndElement();
        //get the encoder for the staff member
        MessageEncoder encoder = ProtocolCodecFactory.getDefault().getEncoder(MobilePhoneDetail.ID);
        encoder.doEncode(vehicle.getMobilePhone(), writer);
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
