package at.rc.tacos.platform.net;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;


import at.rc.tacos.platform.model.MessageConstants;
import at.rc.tacos.platform.model.SystemMessage;
import at.rc.tacos.platform.net.XmlProtocolDecoder;

/**
 * Contains various tests for the protocol decoder
 * 
 * @author Michael
 */
public class XmlProtocolDecoderTest {

	// the protocol decoder
	private XmlProtocolDecoder decoder = new XmlProtocolDecoder();

	// the test objects
	private IoSession session;
	private ProtocolDecoderOutput out;

	@Before
	public void setup() {
		session = mock(IoSession.class);
		out = mock(ProtocolDecoderOutput.class);
	}

	@Test
	public void testDecoderSuccess() throws Exception {
		// prepare the test and read the xml file
		String fileContent = FileUtils.readFileToString(new File("test-data", "xstream_valid.xml"));
		IoBuffer in = IoBuffer.allocate(1024);
		in.setAutoExpand(true);
		in.putString(fileContent, Charset.forName("UTF-8").newEncoder());
		
		// now try to decode the object
		decoder.writeText(session,fileContent,out);
		
		//the expectations
		final Map<String,String> map = new HashMap<String, String>();
		map.put("param1", "value1");
		
        // verify that the expected result happened
        verify(session).setAttribute(MessageConstants.COMMAND, "command");
        verify(session).setAttribute(MessageConstants.PARAMS, map);
        verify(out).write(new SystemMessage("hello",1));
	}
	
}
