package at.rc.tacos.core.xml.codec;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.VehicleDetail;

public class VehicleDecoder  implements MessageDecoder
{
    @Override
    public AbstractMessage doDecode(XMLEventReader reader) throws XMLStreamException
    {
        //Create a new vechcile
        VehicleDetail vehicle = new VehicleDetail();
            
        //parse and set up the object
        while(reader.hasNext())
        {
            //the type of the event
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) 
            {
                String startName = event.asStartElement().getName().getLocalPart();
                //create a new item 
                if(VehicleDetail.ID.equalsIgnoreCase(startName))
                    vehicle = new VehicleDetail();
                //get the mobile phone detail
                if(MobilePhoneDetail.ID.equalsIgnoreCase(startName))
                {
                    //get the decoder for the staff
                    MessageDecoder decoder = ProtocolCodecFactory.getDefault().getDecoder(MobilePhoneDetail.ID);
                    vehicle.setMobilPhone((MobilePhoneDetail)decoder.doDecode(reader));
                }
                
                //get the type of the element and set the corresponding value
                if("vehicleId".equalsIgnoreCase(startName))
                    vehicle.setVehicleId(Integer.valueOf(reader.getElementText()));
                if("vehicleName".equalsIgnoreCase(startName))
                    vehicle.setVehicleName(reader.getElementText());
                if("vehicleType".equalsIgnoreCase(startName))
                    vehicle.setVehicleType(reader.getElementText());
                if("driverName".equalsIgnoreCase(startName))
                    vehicle.setDriverName(reader.getElementText());
                if("paramedicIName".equalsIgnoreCase(startName))
                    vehicle.setParamedicIName(reader.getElementText());
                if("paramedicIIName".equalsIgnoreCase(startName))
                    vehicle.setParamedicIIName(reader.getElementText());
                if("vehicleNotes".equalsIgnoreCase(startName))
                    vehicle.setVehicleNotes(reader.getElementText());
                if("basicStation".equalsIgnoreCase(startName))
                    vehicle.setBasicStation(reader.getElementText());
                if("currentStation".equalsIgnoreCase(startName))
                    vehicle.setCurrentStation(reader.getElementText());
                if("readyForAction".equalsIgnoreCase(startName))
                    vehicle.setReadyForAction(Boolean.getBoolean(reader.getElementText()));
                if("outOfOrder".equalsIgnoreCase(startName))
                    vehicle.setOutOfOrder(Boolean.getBoolean(reader.getElementText()));
                if("mostImportantTransportStatus".equalsIgnoreCase(startName))
                    vehicle.setMostImportantTransportStatus(Integer.valueOf(reader.getElementText()));
            }
            //check for the end element, and return the object
            if(event.isEndElement())
            {
                //get the name
                String endElement = event.asEndElement().getName().getLocalPart();
                //check if we have reached the end
                if (VehicleDetail.ID.equalsIgnoreCase(endElement))
                    return vehicle;
            }
        }
        return null;
    }
}
