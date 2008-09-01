package at.rc.tacos.platform.codec;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

import at.rc.tacos.platform.model.AbstractMessage;

/**
 * Interface for message decoders
 * @author Michael
 */
public interface MessageDecoder
{
    public AbstractMessage doDecode(XMLEventReader reader) throws XMLStreamException;
}
