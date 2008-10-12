package at.rc.tacos.platform.net;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import com.thoughtworks.xstream.XStream;

/**
 * <p>
 * A {@link ProtocolCodecFactory} that performs encoding and decoding between a
 * text line data and a higher level java object.
 * </p>
 * <p>
 * The conversion is realized using {@link XStream}
 * </p>
 * 
 * @author Michael
 */
public class XmlCodecFactory implements ProtocolCodecFactory {
	
	private XmlProtocolDecoder decoder;
	private XmlProtocolEncoder encoder;
	
    /**
     * Default class constructor
     */
    public XmlCodecFactory() {
        decoder = new XmlProtocolDecoder();
        encoder = new XmlProtocolEncoder();
    }
    
    public ProtocolEncoder getEncoder(IoSession session) {
        return encoder;
    }

    public ProtocolDecoder getDecoder(IoSession session) {
        return decoder;
    }
}
