package at.redcross.tacos.web.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.web.config.SettingsStore;
import at.redcross.tacos.web.config.SystemSettings;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.utils.XmlFile;

@KeepAlive
@ManagedBean(name = "systemMaintenanceBean")
public class SystemMaintenanceBean extends BaseBean {

	private static final long serialVersionUID = 6434465652360821755L;

	/** the property object to configure */
	private SystemSettings settings;

	@Override
	protected void init() throws Exception {
		settings = XmlFile.read(SettingsStore.getInstance().getConfigFile());
	}

	// ---------------------------------
	// Business methods
	// ---------------------------------
	public void saveSettings(ActionEvent event) {
		try {
			XmlFile.write(SettingsStore.getInstance().getConfigFile(), settings);
		} catch (Exception ex) {
			FacesUtils.addErrorMessage("Die Einstellungen konnte nicht gespeichert werden");
		}
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public SystemSettings getSettings() {
		return settings;
	}
}
