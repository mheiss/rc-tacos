package at.rc.tacos.platform.xstream;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * Enhanced {@link XStream} implementation for common usage in the project
 * 
 * @author Michael
 */
public class XStream2 extends XStream {

	// the charset to use
	private final static Charset UTF_8 = Charset.forName("UTF-8");
	private final static String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	/**
	 * Default class constructor
	 */
	public XStream2() {
		super(new XppDriverImpl());
	}

	@Override
	protected boolean useXStream11XmlFriendlyMapper() {
		return false;
	}

	/**
	 * Initializes alias for common used classes
	 */
	@Override
	protected void setupAliases() {
		super.setupAliases();
		aliasPackage("model", "at.rc.tacos.platform.model");
		aliasPackage("message", "at.rc.tacos.platform.net.message");
	}

	/**
	 * Extended type save {@link #fromXML(InputStream)} implementation to
	 * deserialize an object from an XML <code>InputStream</code>.
	 */
	@SuppressWarnings("unchecked")
	public <T> T extFromXML(InputStream input, Class<T> clazz) {
		// save the class loader and set the new
		ClassLoader old = getClassLoader();
		setClassLoader(clazz.getClassLoader());
		T deserialized = (T) fromXML(input);
		// restore the class loader
		setClassLoader(old);
		return deserialized;
	}

	/**
	 * Extended {@link #toXML(Object, OutputStream)} implementation that writes
	 * an valid UTF-8 xml header at the beginning of the xml output stream.
	 */
	public void extToXML(Object obj, OutputStream out) throws Exception {
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, UTF_8));
		writer.write(XML_HEADER);
		writer.flush();
		OutputStreamWriter outStream = new OutputStreamWriter(out, UTF_8);
		toXML(obj, outStream);
	}

	/**
	 * Specialized implemenation of the xpp driver to use a compact writer
	 * instead of the pretty printer
	 * 
	 * @author Michael
	 */
	public static class XppDriverImpl extends XppDriver {

		@Override
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new CompactWriter(out);
		}
	}
}
