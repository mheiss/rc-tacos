package at.rc.tacos.core.xml;

//java
import java.io.*;
import java.util.*;
import javax.xml.namespace.*;
import javax.xml.stream.*;
import javax.xml.stream.events.*;
//rcp
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;
//xml
import at.rc.tacos.core.xml.internal.*;
//client
import at.rc.tacos.client.model.*;

/**
 * The activator class controls the plug-in life cycle
 */
public class XmlWrapper extends Plugin implements IXmlData
{
	// The plug-in ID
	public static final String PLUGIN_ID = "at.rc.tacos.core.xml";

	// The shared instance
	private static XmlWrapper plugin;
	
	/**
	 * The constructor
	 */
	public XmlWrapper() { }

	/**
	 * Starts the plugin
	 */
	public void start(BundleContext context) throws Exception 
	{
		super.start(context);
		plugin = this;
	}

	/**
     * Stops the plugin
     */
	public void stop(BundleContext context) throws Exception 
	{
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * @return the shared instance
	 */
	public static XmlWrapper getDefault() 
	{
		return plugin;
	}
	
	/**
     * Parse the given input string into a general RCNet object.
     * The data element will not be set, because the structure is not knwon
     * at this point.
     * With the created <code>RCNet</code> object you can extract the Type of 
     * the message and then parse the string again to extract the information
     * @param xmlInput the xml string to parse
     * @return the object containing the header data
     */
    public RCNet parseHeader(String xmlInput)
    {
        //set up the object
        RCNet header = new RCNet();
        //create the input stream out of the input
        StringReader input = new StringReader(xmlInput);
        XMLInputFactory f = XMLInputFactory.newInstance();
        try 
        {
            XMLEventReader r = f.createXMLEventReader(input);   
            while (r.hasNext()) 
            {
                XMLEvent event = r.nextEvent();
                if (event.isStartElement()) 
                {
                    //get the element
                    StartElement start = event.asStartElement();
                    String startName = start.getName().getLocalPart();
                    
                    //get the type of the element and set the corresponding value
                    if("userId".equalsIgnoreCase(startName))
                        header.setUserId(r.getElementText());
                    if("timestamp".equalsIgnoreCase(startName))
                        header.setTimestamp(r.getElementText());
                    if("action".equalsIgnoreCase(startName))
                    {              
                        header.setAction(r.getElementText());
                        //get the attribute
                        Attribute targetAttr = start.getAttributeByName(new QName("target"));
                        header.setActionTarget(targetAttr.getValue());
                    }
                }
            }
            //cleanup
            r.close();
        } 
        catch(XMLStreamException xmlSE)
        {
            System.out.println("Error while parsing the given input stream");
            System.out.println(xmlSE.getMessage());
        }
        //cleanup
        input.close();
        return header;
    }
    
    /**
     * Parses the given input string an extracts the all items.
     * @param xmlInput the xml string to parse
     * @return a vector containing all items
     */
    public Vector<Item> parseItemData(String xmlInput)
    {
        //set up the object
        Vector<Item> items = new Vector<Item>();
        //create the input stream out of the input
        StringReader input = new StringReader(xmlInput);
        XMLInputFactory f = XMLInputFactory.newInstance();
        try 
        {
            XMLEventReader r = f.createXMLEventReader(input);   
            while (r.hasNext()) 
            {
                XMLEvent event = r.nextEvent();
                if (event.isStartElement()) 
                {
                    //get the element
                    StartElement start = event.asStartElement();
                    String startName = start.getName().getLocalPart();
                    if("item".equalsIgnoreCase(startName))
                        items.addElement(new Item(r.getElementText()));
                }
            }
            //cleanup
            r.close();
        } 
        catch(XMLStreamException xmlSE)
        {
            System.out.println("Error while parsing the given input stream");
            System.out.println(xmlSE.getMessage());
        }
        //cleanup
        input.close();
        return items;
    }
}
