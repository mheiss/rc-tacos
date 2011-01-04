package at.redcross.tacos.web.beans;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.redcross.tacos.web.config.SettingsStore;

@ApplicationScoped
@ManagedBean(name = "versionBean")
public class VersionBean extends BaseBean {

	private static final long serialVersionUID = 2297578084235012989L;

	private final static Log logger = LogFactory.getLog(VersionBean.class);

	/** the version string */
	private String systemVersion;

	@PostConstruct
	protected void init() {
		try {
			systemVersion = readVersionString();
			logger.info("Tacos '" + systemVersion + "' started");
		} catch (Exception ex) {
			logger.error("Failed to read system properties", ex);
		}
	}

	private String readVersionString() throws IOException {
		// read from configuration file
		Properties p = SettingsStore.getInstance().getSystemProperties();
		systemVersion = p.getProperty("tacos.version", "DEV-${date}");
		// replace patterns in the format string
		if (!systemVersion.contains("${date}")) {
			return systemVersion;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmm");
		return systemVersion.replace("${date}", sdf.format(new Date()));
	}

	public String getSystemVersion() {
		return systemVersion;
	}
}
