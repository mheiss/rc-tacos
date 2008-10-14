package at.rc.tacos.platform.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.rc.tacos.platform.model.MessageCommand;
import at.rc.tacos.platform.model.SystemMessage;
import at.rc.tacos.platform.net.XStream2;

/**
 * Contains thest cases for the xstream implemntation
 * 
 * @author Michael
 */
public class XStreamTest {

	// the instance for the encoding / decoding
	private XStream2 xStream;

	@Before
	public void init() {
		xStream = new XStream2();
	}

	/**
	 * Test case to write a message, no result checked
	 */
	@Test
	public void testMarshalMessage() throws Exception {
		// create a new message object
		MessageCommand message = new MessageCommand("<test>hallo</test>");
		message.addParam("param1", "value1");

		// write the object to xml
		xStream.toXML(message, new FileOutputStream("test-data/xstream_encode.xml"));
	}
	
	/**
	 * Test case to write a list of messages, no result checked
	 */
	@Test
	public void testMarshalList() throws Exception {
		// create a new message object
		List<MessageCommand> messageList = new ArrayList<MessageCommand>();
		messageList.add(new MessageCommand("command1"));
		messageList.add(new MessageCommand("command2"));
		// write the object to xml
		xStream.toXML(messageList, new FileOutputStream("test-data/xstream_encode_list.xml"));
	}
	
	/**
	 *  Test case to decode a message, the result is validated
	 */
	@Test
	public void testUnmarshalMessage() throws Exception {
		// read the message from xml
		Object object = xStream.fromXML(new FileReader("test-data/xstream_decode.xml"));

		// assert we have decoded a message object
		Assert.assertEquals(object.getClass(), MessageCommand.class);
		MessageCommand message = (MessageCommand) object;

		Assert.assertEquals(message.getCommand(), "add");
		Assert.assertEquals(message.getParams().size(), 2);
		Assert.assertTrue(message.getParams().containsValue("value1"));
		Assert.assertTrue(message.getParams().containsKey("param1"));
	}
	
	@Test 
	public void testValidHeader() throws Exception {
		//read the content of the file
		String xml = FileUtils.readFileToString(new File("test-data","xstream_valid.xml"));
		//try to decode the header
		Object object = xStream.decodeHeader(xml);
		Assert.assertEquals(MessageCommand.class, object.getClass());
	}
	
	@Test 
	public void testValidContent() throws Exception {
		//read the content of the file
		String xml = FileUtils.readFileToString(new File("test-data","xstream_valid.xml"));
		//try to decode the header
		Object object = xStream.decodeContent(xml);
		Assert.assertEquals(SystemMessage.class, object.getClass());
	}

	@After
	public void cleanup() {
		xStream = null;
	}
}
