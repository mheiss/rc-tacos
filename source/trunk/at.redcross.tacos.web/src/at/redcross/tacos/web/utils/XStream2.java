package at.redcross.tacos.web.utils;

import at.redcross.tacos.web.config.SystemSettings;

import com.thoughtworks.xstream.XStream;

/**
 * Customized {@link XStream} implementation.
 */
public class XStream2 extends XStream {

	public XStream2() {
		setMode(XStream.ID_REFERENCES);
		init();
	}

	/** Initialize the instance */
	private void init() {
		processAnnotations(SystemSettings.class);
	}
}
