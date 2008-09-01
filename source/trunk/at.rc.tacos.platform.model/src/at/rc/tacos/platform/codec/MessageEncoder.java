package at.rc.tacos.platform.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.platform.model.AbstractMessage;

/**
 * Interface for message encoders
 * @author Michael
 */
public interface MessageEncoder
{
    public void doEncode(AbstractMessage message,XMLStreamWriter writer) throws XMLStreamException;
}
