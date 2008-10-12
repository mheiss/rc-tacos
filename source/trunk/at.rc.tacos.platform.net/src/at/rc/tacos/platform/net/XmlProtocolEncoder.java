package at.rc.tacos.platform.net;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Map;

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

import at.rc.tacos.platform.model.MessageCommand;
import at.rc.tacos.platform.model.MessageConstants;

/**
 * <p>
 * A {@link ProtocolEncoder} which encodes higher level objects into a text line
 * which ends up with the delimiter.
 * </p>
 * <p>
 * Please note that this is <b>NOT</b> a general purpose implementation because
 * it is highly coupled which protocol specific concerns.
 * </p>
 * <p>
 * See {@link TextLineEncoder} for a more general approach.
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
	@SuppressWarnings("unchecked")
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		CharsetEncoder encoder = (CharsetEncoder) session.getAttribute(ENCODER);
		if (encoder == null) {
			encoder = charset.newEncoder();
			session.setAttribute(ENCODER, encoder);
		}

		// try to get the message commands from the session
		Object command = (String) session.getAttribute(MessageConstants.COMMAND);
		Object params = session.getAttribute(MessageConstants.PARAMS);

		// assert valid command
		if (!(command instanceof String)) {
			logAndThrowException("The message command must be provided as a string", null);
		}
		if (command == null) {
			logAndThrowException("Cannot send a message without a command", null);
		}

		// assert valid params
		if (!(params instanceof Map)) {
			logAndThrowException("The message params must be provided as <code>Map</code>", null);
		}

		// setup a message command
		MessageCommand messageCommand = new MessageCommand((String) command);
		messageCommand.setParams((Map<String, String>) params);

		// setup the message to send
		StringBuffer xmlString = new StringBuffer();
		xmlString.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		xmlString.append("<" + MessageConstants.TAG_MESSAGE + ">");
		xmlString.append("<" + MessageConstants.TAG_HEADER + ">");
		xmlString.append(xStream.toXML(messageCommand));
		xmlString.append("</" + MessageConstants.TAG_HEADER + ">");
		xmlString.append("<" + MessageConstants.TAG_CONTENT + ">");
		xmlString.append(xStream.toXML(message));
		xmlString.append("</" + MessageConstants.TAG_CONTENT + ">");
		xmlString.append("</" + MessageConstants.TAG_MESSAGE + ">");

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
	 * @param error
	 *            the cause of the error
	 */
	private void logAndThrowException(String message, Throwable error) {
		log.error(message, error);
		throw new IllegalArgumentException(message, error);
	}
}
