package at.redcross.tacos.web.persistence;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebDbalServlet extends HttpServlet {

	private final static long serialVersionUID = 401282645922562652L;

	private final static Logger logger = LoggerFactory.getLogger(WebDbalServlet.class);

	// the system property that defines the persistence unit
	private static final String PERSISTENCE_PROPERTY = "tacos.persistenceUnit";

	@Override
	public void init(ServletConfig config) throws ServletException {
		Properties p = loadProperties(config.getServletContext());
		WebDbalResources.getInstance().setPersistenceUnit(p.getProperty(PERSISTENCE_PROPERTY, ""));
		logger.info("Initialized dbal resources");
	}

	/**
	 * Loads and returns content of the <tt>system.properties</tt> from the
	 * classpath.
	 */
	protected Properties loadProperties(ServletContext context) {
		InputStream in = null;
		Properties p = new Properties();
		try {
			URL resource = context.getResource("/WEB-INF/classes/system.properties");
			if (resource == null) {
				logger.warn("No system.properties file found in classpath");
				return p;
			}
			in = resource.openStream();
			p.load(in);
			return p;
		} catch (Exception ex) {
			logger.error("Failed to read system properties", ex);
			return p;
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

}
