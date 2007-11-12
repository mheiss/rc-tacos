package at.rc.tacos.common;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamWriter;

/**
 * Methods must implement this interface so that the can 
 * be serialized to xml and back.
 * @author Michael
 */
public interface IXMLObject
{   
    /** Serialize to xml */
    public void toXML(XMLStreamWriter writer);
    
    /** Deserialize from xml */
    public IXMLObject toObject(XMLEventReader reader);
}
