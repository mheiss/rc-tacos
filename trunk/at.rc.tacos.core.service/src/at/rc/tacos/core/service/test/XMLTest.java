package at.rc.tacos.core.service.test;

//model
import java.util.Date;

import javax.xml.stream.XMLEventReader;

import at.rc.tacos.core.model.*;
//xml
import at.rc.tacos.core.net.codec.*;


/**
 * Test class for xml serialization and deserialization
 * @author Michael
 */
public class XMLTest
{
    public static void main(String[] args)
    {
        //get the current time
        long start = new Date().getTime();
        //factory to de and encode
        XMLFactory factory = new XMLFactory();
        //setup
        factory.setupEncodeFactory(
                "heissm", 
                new Date().getTime(),
                "item",
                "insert",
                0L);
        //start looping
        for (int i = 0; i<1000;i++)
        {
            //The item to encode and decode
            Item item = new Item("test item ");            
            //enocde
            String xml = factory.encode(item);
            //decode
            factory.setupDecodeFactory(xml);
            //get the reader to process the body
            XMLEventReader reader = factory.decodeHeader();
            //set up the item with the reader
            Item newItem = new Item();
            newItem = newItem.toObject(reader);
        }
        //get the time
        long end = new Date().getTime();
        //print out the imte
        System.out.println("Time: "+(end-start) +"ms");
    }
}
