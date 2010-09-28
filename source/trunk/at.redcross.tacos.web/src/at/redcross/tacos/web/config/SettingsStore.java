package at.redcross.tacos.web.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private SettingsStore() {
		init();
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
	 * Reads and returns the given property file that is located inside the home
	 * directory.
	 * 
	 * @param file
	 *            the file to read
	 * @return the property file initialized with the values from the file
	 */
	public Properties getProperties(String fileName) {
		fileName = FilenameUtils.separatorsToUnix(fileName);
		if (!fileName.startsWith("/")) {
			fileName = "/" + fileName;
		}
		Properties properties = new Properties();
		FileInputStream in = null;
		try {
			in = new FileInputStream(new File(homeDir, fileName));
			properties.load(in);
			return properties;
		} catch (Exception ex) {
			logger.error("Failed to read property file '" + fileName + "'", ex);
			return properties;
		} finally {
			IOUtils.closeQuietly(in);
		}
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

	/** Initialize the store and set the directory */
	private void init() {
		// check system property
		if (System.getProperty(TACOS_HOME) != null) {
			File customHome = new File(System.getProperty(TACOS_HOME));
			if (initHome(customHome)) {
				homeDir = customHome;
				logger.info("Using custom home directory");
				return;
			}
		}

		// custom home directory not available (or failed to initialize)
		File defaultHome = new File(System.getProperty(DEFAULT_HOME), ".tacos");
		if (initHome(defaultHome)) {
			homeDir = defaultHome;
			logger.info("Using default home directory");
			return;
		}

		// fallback if everything goes wrong
		logger.warn("Using temporary directory to store files");
		homeDir = new File(System.getProperty("java.io.tmpdir"), ".tacos");
		initHome(homeDir);
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
