package at.rc.tacos.platform.net.mina;

import java.nio.charset.Charset;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.net.XStream2;
import at.rc.tacos.platform.net.request.AbstractMessage;

import com.thoughtworks.xstream.XStreamException;

/**
 * <p>
 * A {@link ProtocolDecoder} which decodes a text line into a string.
 * </p>
 * <p>
 * The decoded line of text will be parser with {@link XStream2} to convert it
 * to a higher level object and then passed to the {@link IoHandler} instance.
 * </p>
 * 
 * @author Michael
 */
public class XmlProtocolDecoder extends TextLineDecoder {

	// the logger instance
	private Logger log = LoggerFactory.getLogger(XmlProtocolDecoder.class);

	// properties for the decoder
	private final XStream2 xStream;

	/**
	 * Default class constructor to setup a {@link LineDelimiter#WINDOWS}
	 * delimiter and that uses UTF-8 as {@link Charset}
	 */
	public XmlProtocolDecoder() {
		super(Charset.forName("UTF-8"), LineDelimiter.WINDOWS);
		setMaxLineLength(Integer.MAX_VALUE);
		this.xStream = new XStream2();
	}

	/**
	 * Coverts the received line of text to a higher level object and propagates
	 * it to the {@code ProtocolDecoderOutput#write(Object)}.
	 * 
	 * @param session
	 *            the {@code IoSession} the received data.
	 * @param text
	 *            the decoded text
	 * @param out
	 *            the upstream {@code ProtocolDecoderOutput}.
	 */
	@Override
	protected void writeText(IoSession session, String text, ProtocolDecoderOutput out) {

		if (log.isTraceEnabled()) {
			log.trace("Decoding message: " + text);
		}

		// try to decode the message
		try {
			Object message = xStream.fromXML(text);

			// assert we are receiving only messages from type abstract message
			if (!(message instanceof AbstractMessage)) {
				logAndThrowException("Can only send messages from type 'AbstractMessage' is " + message == null ? "null" : message.getClass()
						.getName(), null);
			}

			// pass it to the next filter
			out.write(message);
		}
		catch (XStreamException xse) {
			logAndThrowException("Failed to decode the message body: " + xse.getMessage(), xse.getCause());
		}
	}

	/**
	 * Helper method to log an event and throw a exception
	 * 
	 * @param message
	 *            the message string to log
	 * @param error
	 *            the cause of the error
	 */
	private void logAndThrowException(String message, Throwable error) {
		log.error(message, error);
		throw new IllegalArgumentException(message, error);
	}
}
