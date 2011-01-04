package at.redcross.tacos.web.config;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.redcross.tacos.web.utils.XmlFile;

/**
 * The {@code SettingsStore} provides access to configuration files that are
 * located on the local file system.
 */
public class SettingsStore {

	private final static Logger logger = LoggerFactory.getLogger(SettingsStore.class);

	/** system property that will point to a custom directory */
	private final static String TACOS_HOME = "TACOS_HOME";

	/** fallback if no property is defined */
	private final static String DEFAULT_HOME = "user.home";

	/** the one and only instance */
	private static SettingsStore instance;

	/** the current home-directory */
	private File homeDir;

	/** Default constructor */
	private SettingsStore() {
		init();
		initFiles();
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static synchronized SettingsStore getInstance() {
		if (instance == null) {
			instance = new SettingsStore();
		}
		return instance;
	}

	/**
	 * Returns the current home directory where settings and property files can
	 * be stored.
	 * 
	 * @return the path to the home directory
	 */
	public File getHome() {
		return homeDir;
	}

	/**
	 * Returns the current file where the system settings configuration is
	 * located.
	 * 
	 * @return the path to the system settings file
	 */
	public File getConfigFile() {
		return new File(homeDir, "tacos.config");
	}

	/**
	 * Reads and returns the content of the global <tt>system.properties</tt>
	 * file that is located in the classpath.
	 * 
	 * @return the system properties
	 */
	public Properties getSystemProperties() {
		InputStream in = null;
		Properties p = new Properties();
		try {
			ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
			URL resource = ext.getResource("/WEB-INF/classes/system.properties");
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

	/** Initialize the store and set the directory */
	private void init() {
		// check system property
		if (System.getProperty(TACOS_HOME) != null) {
			File customHome = new File(System.getProperty(TACOS_HOME));
			if (initHome(customHome)) {
				homeDir = customHome;
				logger.info("Using custom home directory ('" + homeDir + "')");
				return;
			}
		}

		// custom home directory not available (or failed to initialize)
		File defaultHome = new File(System.getProperty(DEFAULT_HOME), ".tacos");
		if (initHome(defaultHome)) {
			homeDir = defaultHome;
			logger.info("Using default home directory ('" + homeDir + "')");
			return;
		}

		// fallback if everything goes wrong
		logger.warn("Using temporary directory to store files");
		homeDir = new File(System.getProperty("java.io.tmpdir"), ".tacos");
		initHome(homeDir);
	}

	/** Initializes the configuration files and ensures that they are existing */
	private void initFiles() {
		try {
			File file = getConfigFile();
			if (!file.exists()) {
				XmlFile.write(file, new SystemSettings());
			}
		} catch (Exception ex) {
			logger.error("Failed to initialize settings file", ex);
		}
	}

	/** Tries to initialize the given home directory */
	private boolean initHome(File file) {
		try {
			FileUtils.forceMkdir(file);
			return true;
		} catch (Exception ex) {
			logger.error("Failed to initialize home directory '" + file + "'", ex);
			return false;
		}
	}

}
