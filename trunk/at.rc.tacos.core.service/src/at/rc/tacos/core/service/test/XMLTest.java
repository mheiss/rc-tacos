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
                RosterEntry.ID,
                "insert",
                0L);
        //the resulting list
        ArrayList<AbstractMessage> resultObjects = new ArrayList<AbstractMessage>();
        ArrayList<AbstractMessage> inputObjects = new ArrayList<AbstractMessage>();
        //start looping
        for (int i = 0; i<10000;i++)
        {
            StaffMember member = new StaffMember(0,"Michael","Heiss","Ich");
            RosterEntry newRosterEntry = new RosterEntry(
                    i,member,new Date().getTime(),
                    new Date().getTime(), new Date().getTime(),
                    new Date().getTime(), new Date().getTime(), "Kapfenberg",
                    "Fahrer", "Zivi", "nix",false);
            inputObjects.add(newRosterEntry);
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
            RosterEntry entry = (RosterEntry)object;
            System.out.println(entry);
        }
    }
}
