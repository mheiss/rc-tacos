package simplexml.test;

import java.io.FileOutputStream;
import java.io.FileReader;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import simplexml.model.Message;

/**
 * XStream test cases to generate xml from java and vice versa.
 * 
 * @author Michael
 */
public class XStreamTests {

	//the instance for the encoding / decoding
	private XStream xStream;
	
	@Before
	public void init() {
		xStream = new XStream();
		xStream.setMode(XStream.NO_REFERENCES);
		xStream.alias("message", Message.class);
	}
	
	@Test
	public void testMarshalMessage() throws Exception {
		// create a new message object
		Message message = new Message("add", "myClass");
		message.addParam("param1", "value1");

		// write the object to xml
		xStream.toXML(message, new FileOutputStream("gen-data/xstream_encode.xml"));
	}
	
	@Test
	public void testUnmarshalMessage() throws Exception {
		//read the message from xml
		Object object = xStream.fromXML(new FileReader("gen-data/xstream_decode.xml"));
		
		//assert we have decoded a message object
		Assert.assertEquals(object.getClass(), Message.class);
		Message message = (Message) object;
		
		Assert.assertEquals(message.getCommand(), "add");
		Assert.assertEquals(message.getContentClazz(),"myClass");
		Assert.assertEquals(message.getParams().size(), 1);
		Assert.assertTrue(message.getParams().containsValue("value1"));
		Assert.assertTrue(message.getParams().containsKey("param1"));
	}
	
	@After
	public void cleanup() {
		xStream = null;
	}
}
