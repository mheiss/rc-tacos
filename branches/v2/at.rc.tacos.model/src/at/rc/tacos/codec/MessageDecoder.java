package at.rc.tacos.codec;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

import at.rc.tacos.common.AbstractMessage;

/**
 * Interface for message decoders
 * @author Michael
 */
public interface MessageDecoder
{
    public AbstractMessage doDecode(XMLEventReader reader) throws XMLStreamException;
}
