package at.redcross.tacos.web.beans;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import at.redcross.tacos.web.config.SettingsStore;

@ApplicationScoped
@ManagedBean(name = "versionBean")
public class VersionBean extends BaseBean {

	private static final long serialVersionUID = 2297578084235012989L;

	/** the version string */
	private String systemVersion;

	@PostConstruct
	protected void init() {
		// read from configuration file
		File configFile = new File(SettingsStore.getInstance().getHome(), "config.properties");
		if (configFile.exists()) {
			Properties p = SettingsStore.getInstance().getProperties("config.properties");
			systemVersion = p.getProperty("tacos.version");
		}
		// fallback to default solution
		if (systemVersion == null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmm");
			systemVersion = "0.0.0.v" + sdf.format(new Date());
		}
	}

	public String getSystemVersion() {
		return systemVersion;
	}
}
