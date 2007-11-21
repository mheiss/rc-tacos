package at.rc.tacos.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Item;

/**
 * Class to encode a item to xml
 * @author Michael
 */
public class ItemEncoder implements MessageEncoder
{
    @Override
    public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException
    {
        //Cast the object to a item
        Item item = (Item)message;
        
        //write the start element
        writer.writeStartElement(Item.ID);
       
        //write the elements and attributes
        writer.writeStartElement("name");
        writer.writeCharacters(item.getName());
        writer.writeEndElement();
        
        //end of the item
        writer.writeEndElement();
    }
}
