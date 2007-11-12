package at.rc.tacos.core.service.test;

//model
import java.util.ArrayList;
import java.util.Date;

import at.rc.tacos.model.*;
import at.rc.tacos.common.IXMLObject;
import at.rc.tacos.core.xml.*;

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
        //te resulting list
        ArrayList<IXMLObject> resultObjects = new ArrayList<IXMLObject>();
        ArrayList<IXMLObject> inputObjects = new ArrayList<IXMLObject>();
        //start looping
        for (int i = 0; i<1000;i++)
        {
            //The item to encode and decode
            Item item = new Item("test item ");   
            inputObjects.add(item);
        }
        //enocde
        String xml = factory.encode(inputObjects);
        System.out.println(xml);
        //decode
        factory.setupDecodeFactory(xml);
        //get the reader to process the body
        resultObjects = factory.decode();

        //get the time
        long end = new Date().getTime();
        //print out the imte
        System.out.println("Time: "+(end-start) +"ms");
        //print out the items
        for(IXMLObject object:resultObjects)
        {
            Item item = (Item)object;
            System.out.println(item);
        }
    }
}
