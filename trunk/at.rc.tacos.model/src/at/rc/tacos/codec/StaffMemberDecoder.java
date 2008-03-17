package at.rc.tacos.codec;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.factory.ProtocolCodecFactory;
import at.rc.tacos.model.Competence;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.StaffMember;

public class StaffMemberDecoder implements MessageDecoder
{
    @Override
    public AbstractMessage doDecode(XMLEventReader reader) throws XMLStreamException
    {
        //Create a new staff member
        StaffMember member = new StaffMember();
            
        //parse and set up the object
        while(reader.hasNext())
        {
            //the type of the event
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) 
            {
                String startName = event.asStartElement().getName().getLocalPart();
                //create a new item 
                if(StaffMember.ID.equalsIgnoreCase(startName))
                    member = new StaffMember();
                
                //get the type of the element and set the corresponding value
                if("staffMemberId".equalsIgnoreCase(startName))
                    member.setStaffMemberId(Integer.valueOf(reader.getElementText()));
                //get the type of the element and set the corresponding value
                if(Location.ID.equalsIgnoreCase(startName))
                {
                    //get the decoder for the staff
                    MessageDecoder decoder = ProtocolCodecFactory.getDefault().getDecoder(Location.ID);
                    member.setPrimaryLocation((Location)decoder.doDecode(reader));
                }
                if("lastName".equalsIgnoreCase(startName))
                    member.setLastName(reader.getElementText());
                if("firstName".equalsIgnoreCase(startName))
                    member.setFirstName(reader.getElementText());
                if("userName".equalsIgnoreCase(startName))
                    member.setUserName(reader.getElementText());
                if("streetname".equalsIgnoreCase(startName))
                    member.setStreetname(reader.getElementText());
                if("cityname".equalsIgnoreCase(startName))
                    member.setCityname(reader.getElementText());
                if("eMail".equalsIgnoreCase(startName))
                    member.setEMail(reader.getElementText());
                if("birthday".equalsIgnoreCase(startName))
                	member.setBirthday(reader.getElementText());
                if("sex".equalsIgnoreCase(startName))
                	member.setMale(Boolean.valueOf(reader.getElementText()));
                //decode the list of phones
                if(MobilePhoneDetail.ID.equalsIgnoreCase(startName))
                {
                    MessageDecoder decoder = ProtocolCodecFactory.getDefault().getDecoder(MobilePhoneDetail.ID);
                    member.addMobilePhone((MobilePhoneDetail)decoder.doDecode(reader));
                }
                //decode the list of competences
                if(Competence.ID.equalsIgnoreCase(startName))
                {
                    MessageDecoder decoder = ProtocolCodecFactory.getDefault().getDecoder(Competence.ID);
                    member.addCompetence((Competence)decoder.doDecode(reader));
                }
            }
            //check for the end element, and return the object
            if(event.isEndElement())
            {
                //get the name
                String endElement = event.asEndElement().getName().getLocalPart();
                //check if we have reached the end
                if (StaffMember.ID.equalsIgnoreCase(endElement))
                    return member;
            }
        }
        return null;
    }
}
