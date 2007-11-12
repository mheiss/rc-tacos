package at.rc.tacos.model;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.XMLEvent;

import at.rc.tacos.common.IXMLObject;

/**
 * A simple itemen that has a name
 * @author Michael
 */
public class Item implements IXMLObject
{
    /** The identification of the object */
    public final static String ITEM_ID = "item";
    
    private String id = null;

    /**
     * Default constructor
     */
    public Item() { }

    /**
     * Default constructor specifying the name of the item
     * @param id the name of the item
     */
    public Item(String id)
    {
        this.id = id;
    }

    //METHODS
    /**
     * Serialize the content to xml
     */
    @Override
    public void toXML(XMLStreamWriter writer)
    {
        try
        {
            //start element
            writer.writeStartElement("data");
            
            //write the elements and attributes
            writer.writeStartElement("id");
            writer.writeCharacters(id);
            writer.writeEndElement();
            
            //end of this element
            writer.writeEndElement();
        }
        catch(XMLStreamException xmlse)
        {
            System.out.println("Failed to serialize object: item");
            System.out.println(xmlse.getMessage());
        }
    }


    @Override
    public IXMLObject toObject(XMLEventReader reader)
    {
        //create the new item
        Item item = new Item();
        try
        {
            //parse and set up the object
            while(reader.hasNext())
            {
                //the type of the event
                XMLEvent event = reader.nextEvent();
                if (event.isStartElement()) 
                {
                    String startName = event.asStartElement().getName().getLocalPart();
                    //get the type of the element and set the corresponding value
                    if("id".equalsIgnoreCase(startName))
                        item.setId(reader.getElementText());
                }
                //check for the end element, and return the object
                if(event.isEndElement())
                {
                    //get the name
                    String endElement = event.asEndElement().getName().getLocalPart();
                    //check if we have reached the end
                    if ("data".equalsIgnoreCase(endElement))
                        return item;
                }
            }
            return null;
        }
        catch(XMLStreamException xmlse)
        {
            System.out.println("Failed to create item from xml");
            System.out.println(xmlse.getMessage());
            return null;
        }
        finally
        {
            try
            {
                //close the source
                reader.close();
            }
            catch(XMLStreamException xmlse)
            {
                System.out.println("Failed to close the reader");
                System.out.println(xmlse.getMessage());
            }
        }
    }

    //GETTERS AND SETTERS   
    /**
     * @return the id
     */
    public String getId() 
    {
        return this.id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) 
    {
        this.id = id;
    }
}
