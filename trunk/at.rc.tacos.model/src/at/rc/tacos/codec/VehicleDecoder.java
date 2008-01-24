package at.rc.tacos.codec;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.factory.ProtocolCodecFactory;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.StaffMember;
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
                StartElement start = event.asStartElement();
                String startName = start.getName().getLocalPart();
                
                //create a new item 
                if(VehicleDetail.ID.equalsIgnoreCase(startName))
                    vehicle = new VehicleDetail();
                
                //get the type of the element and set the corresponding value
                if("vehicleName".equalsIgnoreCase(startName))
                    vehicle.setVehicleName(reader.getElementText());
                if("vehicleType".equalsIgnoreCase(startName))
                    vehicle.setVehicleType(reader.getElementText());                
                //get the driver
                if(StaffMember.ID.equalsIgnoreCase(startName))
                {
                    MessageDecoder decoder = ProtocolCodecFactory.getDefault().getDecoder(StaffMember.ID);
                    //get the first attribute
                    Attribute functionAttr = start.getAttributeByName(new QName("function"));
                    //assert valid
                    if(functionAttr != null)
                    {
                        //set the value
                        if("driver".equalsIgnoreCase(functionAttr.getValue()))
                            vehicle.setDriver((StaffMember)decoder.doDecode(reader));
                        if("firstParamedic".equalsIgnoreCase(functionAttr.getValue()))
                            vehicle.setFirstParamedic((StaffMember)decoder.doDecode(reader));
                        if("secondParamedic".equalsIgnoreCase(functionAttr.getValue()))
                            vehicle.setSecondParamedic((StaffMember)decoder.doDecode(reader));
                    }
                }
                //get the mobile phone detail
                if(MobilePhoneDetail.ID.equalsIgnoreCase(startName))
                {
                    //get the decoder for the staff
                    MessageDecoder decoder = ProtocolCodecFactory.getDefault().getDecoder(MobilePhoneDetail.ID);
                    vehicle.setMobilPhone((MobilePhoneDetail)decoder.doDecode(reader));
                }
                if("vehicleNotes".equalsIgnoreCase(startName))
                    vehicle.setVehicleNotes(reader.getElementText());
                if(Location.ID.equalsIgnoreCase(startName))
                {
                    MessageDecoder decoder = ProtocolCodecFactory.getDefault().getDecoder(Location.ID);
                    //get the first attribute
                    Attribute functionAttr = start.getAttributeByName(new QName("type"));
                    //assert valid
                    if(functionAttr != null)
                    {
                        //set the value
                        if("basic".equalsIgnoreCase(functionAttr.getValue()))
                            vehicle.setBasicStation((Location)decoder.doDecode(reader));
                        if("current".equalsIgnoreCase(functionAttr.getValue()))
                            vehicle.setCurrentStation((Location)decoder.doDecode(reader));
                    }
                }
                if("readyForAction".equalsIgnoreCase(startName))
                    vehicle.setReadyForAction(Boolean.valueOf(reader.getElementText()));
                if("outOfOrder".equalsIgnoreCase(startName))
                    vehicle.setOutOfOrder(Boolean.valueOf(reader.getElementText()));
                if("transportStatus".equalsIgnoreCase(startName))
                    vehicle.setTransportStatus(Integer.valueOf(reader.getElementText()));
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
