package at.redcross.tacos.web.beans;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import at.redcross.tacos.web.config.SettingsStore;
import at.redcross.tacos.web.config.SystemSettings;
import at.redcross.tacos.web.utils.XmlFile;

@ApplicationScoped
@ManagedBean(name = "versionBean")
public class VersionBean extends BaseBean {

	private static final long serialVersionUID = 2297578084235012989L;

	/** the version string */
	private String systemVersion;

	@PostConstruct
	protected void init() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmm");
		try {
			SystemSettings settings = XmlFile.read(SettingsStore.getInstance().getSettings());
			systemVersion = settings.getTacosVersion();
		} catch (Exception ex) {
			systemVersion = sdf.format(new Date());
		}
	}

	public String getSystemVersion() {
		return systemVersion;
	}
}
