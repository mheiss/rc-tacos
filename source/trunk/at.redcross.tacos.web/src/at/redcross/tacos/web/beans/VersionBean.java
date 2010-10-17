package at.redcross.tacos.web.beans;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ApplicationScoped
@ManagedBean(name = "versionBean")
public class VersionBean extends BaseBean {

	private static final long serialVersionUID = 2297578084235012989L;

	private final static Log logger = LogFactory.getLog(VersionBean.class);

	/** the version string */
	private String systemVersion;

	@PostConstruct
	protected void init() {
		// read from configuration file
		InputStream in = null;
		try {
			ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
			in = ext.getResourceAsStream("/WEB-INF/classes/version.properties");
			if (in != null) {
				Properties p = new Properties();
				p.load(in);
				systemVersion = p.getProperty("tacos.version", "");
				logger.info("Tacos " + systemVersion + " started");
			}
		} catch (Exception ex) {
			logger.error("Failed to read version file", ex);
		} finally {
			IOUtils.closeQuietly(in);
		}

		// fallback to default solution if anything went wrong
		if (systemVersion == null || systemVersion.isEmpty()) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmm");
			systemVersion = "0.0.0.v" + sdf.format(new Date());
		}
	}

	public String getSystemVersion() {
		return systemVersion;
	}
}