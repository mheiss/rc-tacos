package simplexml.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.JiBXException;
import org.junit.Test;

import simplexml.model.Message;

/**
 * Tests cases for the Jibx framework to generate xml from java and vice versa
 * 
 * @author mheiss
 */
public class JiBxTests {

	@Test
	public void testMarshalMessage() throws JiBXException, FileNotFoundException {
		Message message = new Message("add", "myClass");
		message.addParam("param1", "valueq");

		// create a new marshalling factory for message
		IBindingFactory bfact = BindingDirectory.getFactory(Message.class);
		IMarshallingContext mctx = bfact.createMarshallingContext();
		mctx.marshalDocument(message, "UTF-8", null, new FileOutputStream("gen-data/jibx_encode.xml"));
	}
	


}
