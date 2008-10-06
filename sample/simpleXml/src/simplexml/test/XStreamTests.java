package simplexml.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import simplexml.model.Message;

/**
 * XStream test cases to generate xml from java and vice versa.
 * 
 * @author Michael
 */
public class XStreamTests {

	@Test
	public void testMarshalMessage() throws FileNotFoundException {
		Message message = new Message("add", "myClass");
		message.addParam("param1", "valueq");

		// create a new marshalling factory for message
		XStream xStream = new XStream();
		xStream.alias("message", Message.class);
		xStream.toXML(message, new FileOutputStream("gen-data/xstream_message.xml"));
	}

}
