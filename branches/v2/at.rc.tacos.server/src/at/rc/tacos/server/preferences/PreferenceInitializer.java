package at.rc.tacos.server.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import at.rc.tacos.server.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/**
	 * Initializes the preference store with default values
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		
		//setup default network values
		store.setDefault(PreferenceConstants.P_SERVER_PORT, 4711);
		store.setDefault(PreferenceConstants.P_FAILOVER_HOST, "localhost");
		store.setDefault(PreferenceConstants.P_FAILOVER_PORT, "4711");
		
		//setup defaul database values
		store.setDefault(PreferenceConstants.P_DB_DRIVER, "com.microsoft.sqlserver.jdbc.SQLServerDriver");
		store.setDefault(PreferenceConstants.P_DB_HOST,"jdbc:sqlserver://localhost:1433");
		store.setDefault(PreferenceConstants.P_DB_USER, "tacos");
		store.setDefault(PreferenceConstants.P_DB_PW, "P@ssw0rd");
	}
}
