package at.rc.tacos.common;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamWriter;

/**
 * Methods must implement this interface so that the can 
 * be serialized to xml.
 * @author Michael
 */
public interface IXMLSerialize
{
    /** Serialize to xml */
    public void toXML(XMLStreamWriter writer);
    
    /** Deserialize from xml */
    public IXMLSerialize toObject(XMLEventReader reader);
}
