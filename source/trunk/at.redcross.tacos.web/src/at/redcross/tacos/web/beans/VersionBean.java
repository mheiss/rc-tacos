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

@ApplicationScoped
@ManagedBean(name = "versionBean")
public class VersionBean extends BaseBean {

    private static final long serialVersionUID = 2297578084235012989L;

    /** the version string */
    private String systemVersion;

    @PostConstruct
    protected void init() {
        // read from configuration file
        InputStream in = null;
        try {
            ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
            in = ext.getResourceAsStream("/WEB-INF/classes/version.properties");
            Properties p = new Properties();
            p.load(in);
            systemVersion = p.getProperty("tacos.version", "");
        }
        catch (Exception ex) {
            // cannot do anything :)
        }
        finally {
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
