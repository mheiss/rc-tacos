package at.rc.tacos.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.common.AbstractMessage;

/**
 * Interface for message encoders
 * @author Michael
 */
public interface MessageEncoder
{
    public void doEncode(AbstractMessage message,XMLStreamWriter writer) throws XMLStreamException;
}
