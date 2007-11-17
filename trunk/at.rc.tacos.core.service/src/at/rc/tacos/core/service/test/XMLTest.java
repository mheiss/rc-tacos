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
        
        //get the current time
        long start = new Date().getTime();
        //factory to de and encode
        XMLFactory factory = new XMLFactory();
        //setup
        factory.setupEncodeFactory(
                "heissm", 
                new Date().getTime(),
                Item.ID,
                "insert",
                0L);
        //te resulting list
        ArrayList<AbstractMessage> resultObjects = new ArrayList<AbstractMessage>();
        ArrayList<AbstractMessage> inputObjects = new ArrayList<AbstractMessage>();
        //start looping
        for (int i = 0; i<10;i++)
        {
            //The item to encode and decode
            Item item = new Item("test item ");   
            inputObjects.add(item);
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
        //print out the items
        for(Object object:resultObjects)
        {
            Item item = (Item)object;
            System.out.println(item.getName());
        }
    }
}
