package at.rc.tacos.platform.net;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.net.request.AbstractMessage;

/**
 * <p>
 * A {@link ProtocolEncoder} which encodes higher level objects into xml string
 * which ends up with the delimiter.
 * </p>
 * <p>
 * See {@link TextLineEncoder} for a text only approach.
 * </p>
 * 
 * @author Michael
 */
public class XmlProtocolEncoder extends ProtocolEncoderAdapter {

	private final AttributeKey ENCODER = new AttributeKey(getClass(), "encoder");

	// the logger instance
	private Logger log = LoggerFactory.getLogger(XmlProtocolEncoder.class);

	// properties for the encoder
	private final XStream2 xStream;
	private final Charset charset;
	private final LineDelimiter delimiter;

	/**
	 * Default class constructor to setup a {@link LineDelimiter#WINDOWS}
	 * delimiter and that uses UTF-8 as {@link Charset}
	 */
	public XmlProtocolEncoder() {
		this.xStream = new XStream2();
		this.charset = Charset.forName("UTF-8");
		this.delimiter = LineDelimiter.WINDOWS;
	}

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		CharsetEncoder encoder = (CharsetEncoder) session.getAttribute(ENCODER);
		if (encoder == null) {
			encoder = charset.newEncoder();
			session.setAttribute(ENCODER, encoder);
		}

		// assert we are writing only request messages
		if (!(message instanceof AbstractMessage)) {
			logAndThrowException("Can only send messages from type 'AbstractMessage' is " + message == null ? "null" : message.getClass().getName());
		}

		// setup the message to send
		StringBuffer xmlString = new StringBuffer();
		xmlString.append(xStream.toXML(message));

		if (log.isTraceEnabled()) {
			log.trace("Encoded message: " + xmlString.toString());
		}

		// write the object to the io buffer
		IoBuffer buf = IoBuffer.allocate(xmlString.length()).setAutoExpand(true);
		buf.putString(xmlString.toString(), encoder);
		buf.putString(delimiter.getValue(), encoder);
		buf.flip();
		out.write(buf);
	}

	/**
	 * Helper method to log an event and throw a exception
	 * 
	 * @param message
	 *            the message string to log
	 */
	private void logAndThrowException(String message) {
		log.error(message);
		throw new IllegalArgumentException(message);
	}
}
