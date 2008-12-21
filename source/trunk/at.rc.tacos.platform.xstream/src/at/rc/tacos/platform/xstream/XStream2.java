package at.rc.tacos.platform.xstream;

import java.io.Writer;

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
