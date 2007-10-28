package at.rc.tacos.core.xml.internal;

/**
 * Every data part of the message must implement this interface to
 * serialize the object to xml.
 * @author Michael
 */
public interface XMLObject
{
    /** Serialize the data to xml */
    public String toXML();
}
