package at.redcross.tacos.web.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

import com.thoughtworks.xstream.XStream;

/**
 * The {@code XmlFile} class contains static helper methods to read {@code
 * Object}s from <i>XML</i> files and persists {@code Object}s to <i>XML</i>. It
 * is recommended to use the methods provided by this class instead of directly
 * using the {@link XStream} instance.
 * <p>
 * Within this class the access to the underlying {@link File} object is
 * synchronized. So it is guaranteed that only one thread can read or write a
 * file at any time.
 * </p>
 */
public class XmlFile {

	// the charset to use
	private final static Charset UTF_8 = Charset.forName("UTF-8");
	private final static String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	/** {@link XStream} instance is supposed to be thread-safe. */
	private final static XStream2 xStream = new XStream2();

	/**
	 * Reads the content of the given <i>XML</i> file and creates a new {@code
	 * Object} instance.
	 * 
	 * @param xmlFile
	 *            the xml file to read
	 * @return the newly created object from the xml file
	 * @throws IOException
	 *             if an error occurs during the read operation
	 */
	@SuppressWarnings("unchecked")
	public static synchronized <T> T read(File xmlFile) throws IOException {
		if (xmlFile == null) {
			throw new NullPointerException("The xmlFile to read from cannot be null");
		}
		if (xmlFile.length() == 0) {
			throw new IllegalStateException("The xmlFile contains no data");
		}
		// Synchronize the read access to the file
		Reader reader = null;
		synchronized (xmlFile) {
			try {
				reader = new InputStreamReader(new FileInputStream(xmlFile), UTF_8.name());
				return (T) xStream.fromXML(reader);
			} catch (Exception e) {
				throw new IOException("Unable to read " + xmlFile, e);
			} finally {
				// close the read
				IOUtils.closeQuietly(reader);
			}
		}
	}

	/**
	 * Persists the given {@code Object} to the <i>XML</i> file.
	 * 
	 * @param xmlFile
	 *            the xml file to write to
	 * @param object
	 *            the object to persist
	 * @throws IOException
	 *             if an error occurs during the marshaling to xml
	 */
	public static synchronized void write(File xmlFile, Object object) throws IOException {
		// Synchronize the write access to the file
		FileOutputStream fileOut = null;
		OutputStreamWriter outStream = null;
		PrintWriter writer = null;
		synchronized (xmlFile) {
			try {
				fileOut = new FileOutputStream(xmlFile);
				outStream = new OutputStreamWriter(fileOut, UTF_8);
				writer = new PrintWriter(new OutputStreamWriter(fileOut, UTF_8));
				// write an xml header
				writer.println(XML_HEADER);
				writer.flush();
				xStream.toXML(object, outStream);
			} catch (Exception e) {
				throw new IOException(e);
			} finally {
				IOUtils.closeQuietly(writer);
				IOUtils.closeQuietly(outStream);
				IOUtils.closeQuietly(fileOut);
			}
		}
	}

	/**
	 * Appends the given {@code Object} to the <i>XML</i> file.
	 * 
	 * @param xmlFile
	 *            the xml file to write to
	 * @param object
	 *            the object to persist
	 * @throws IOException
	 *             if an error occurs during the marshaling to xml
	 */
	public static synchronized void append(File xmlFile, Object object) throws IOException {
		// Synchronize the write access to the file
		FileOutputStream fileOut = null;
		OutputStreamWriter outStream = null;
		PrintWriter writer = null;
		synchronized (xmlFile) {
			try {
				fileOut = new FileOutputStream(xmlFile);
				outStream = new OutputStreamWriter(fileOut, UTF_8);
				writer = new PrintWriter(new OutputStreamWriter(fileOut, UTF_8));
				// write an xml header
				writer.println(XML_HEADER);
				writer.flush();
				xStream.toXML(object, outStream);
			} catch (Exception e) {
				throw new IOException(e);
			} finally {
				IOUtils.closeQuietly(writer);
				IOUtils.closeQuietly(outStream);
				IOUtils.closeQuietly(fileOut);
			}
		}
	}

}
