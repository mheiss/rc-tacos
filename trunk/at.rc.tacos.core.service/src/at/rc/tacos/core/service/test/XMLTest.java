package at.rc.tacos.core.service.test;

//model
import java.util.ArrayList;
import java.util.Date;

import at.rc.tacos.model.*;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.xml.*;
import at.rc.tacos.core.xml.codec.*;

/**
 * Test class for xml serialization and deserialization
 * @author Michael
 */
public class XMLTest
{
    public static void main(String[] args)
    {
        //Set up the protocol codec facotry
        ProtocolCodecFactory protFactory = ProtocolCodecFactory.getDefault();
        protFactory.registerDecoder(Item.ID, new ItemDecoder());
        protFactory.registerEncoder(Item.ID, new ItemEncoder());
        protFactory.registerDecoder(MobilePhoneDetail.ID, new MobilePhoneDecoder());
        protFactory.registerEncoder(MobilePhoneDetail.ID, new MobilePhoneEncoder());
        protFactory.registerDecoder(NotifierDetail.ID, new NotifierDecoder());
        protFactory.registerEncoder(NotifierDetail.ID, new NotifierEncoder());
        protFactory.registerDecoder(Patient.ID, new PatientDecoder());
        protFactory.registerEncoder(Patient.ID, new PatientEncoder());
        protFactory.registerDecoder(RosterEntry.ID, new RosterEntryDecoder());
        protFactory.registerEncoder(RosterEntry.ID, new RosterEntryEncoder());
        protFactory.registerDecoder(StaffMember.ID, new StaffMemberDecoder());
        protFactory.registerEncoder(StaffMember.ID, new StaffMemberEncoder());
        protFactory.registerDecoder(Transport.ID, new TransportDecoder());
        protFactory.registerEncoder(Transport.ID, new TransportEncoder());
        protFactory.registerDecoder(VehicleDetail.ID, new VehicleDecoder());
        protFactory.registerEncoder(VehicleDetail.ID, new VehicleEncoder());
        
        //get the current time
        long start = new Date().getTime();
        //factory to de and encode
        XMLFactory factory = new XMLFactory();
        //setup
        factory.setupEncodeFactory(
                "heissm", 
                new Date().getTime(),
                VehicleDetail.ID,
                "insert",
                0L);
        //the resulting list
        ArrayList<AbstractMessage> resultObjects = new ArrayList<AbstractMessage>();
        ArrayList<AbstractMessage> inputObjects = new ArrayList<AbstractMessage>();
        //start looping
        for (int i = 0; i<10;i++)
        {
            StaffMember driver = new StaffMember(0,"Michael","Heiss","Driver");
            StaffMember medic1 = new StaffMember(1,"Michael","Heiss","Medic1");
            StaffMember medic2 = new StaffMember(2,"Michael","Heiss","Medic2");
            MobilePhoneDetail phone = new MobilePhoneDetail("PHONE_1","0699-123456789");
            
            VehicleDetail vehicle = new VehicleDetail(0, "Auto", "großes Auto",
                    driver, medic1, medic2,phone,"keine notes",
                    "daheim", "daheim", true,false, 0 );
            inputObjects.add(vehicle);
        }
        //enocde
        String xml = factory.encode(inputObjects);
        //decode
        factory.setupDecodeFactory(xml);
        //get the reader to process the body
        resultObjects = factory.decode();

        //get the time
        long end = new Date().getTime();
        //print out the imte
        System.out.println("Time: "+(end-start) +"ms");
        System.out.println("Elements: "+resultObjects.size());
        //print out the items
        for(Object object:resultObjects)
        {
            VehicleDetail detail = (VehicleDetail)object;
            System.out.println(detail);
        }
    }
}
