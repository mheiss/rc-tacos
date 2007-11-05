package at.rc.tcos.core.net.decoder;

import java.io.StringReader;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public abstract class AbstractMessageDecoder 
{
	/**
	 * Decodes the header of the message
	 * @param message the header to decode
	 * @return the decoded message
	 */
	public AbstractMessage decode(String xmlInput)
	{
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
//                        header.setUserId(r.getElementText());
                    if("timestamp".equalsIgnoreCase(startName))
//                        header.setTimestamp(r.getElementText());
                    if("action".equalsIgnoreCase(startName))
                    {              
//                        header.setAction(r.getElementText());
                        //get the attribute
                        Attribute targetAttr = start.getAttributeByName(new QName("target"));
//                        header.setActionTarget(targetAttr.getValue());
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
        return null;
	}
	
	/**
	 * Decode the body of the message
	 * @param session the session
	 * @param message the message to decode
	 * @return the decoed object
	 */
	protected abstract AbstractMessage decodeBody(String session,String message);
}
