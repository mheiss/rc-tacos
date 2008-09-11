/**
 *	Copyright 2008 Internettechnik, FH JOANNEUM
 *	http://www.fh-joanneum.at/itm
 *
 * 	Licenced under the GNU GENERAL PUBLIC LICENSE Version 2;
 * 	You may obtain a copy of the License at
 *
 *		http://www.gnu.org/licenses/gpl-2.0.txt
 *
 *	This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 */
package at.rc.tacos.client.update;

import java.net.URL;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.update.search.BackLevelFilter;
import org.eclipse.update.search.EnvironmentFilter;
import org.eclipse.update.search.UpdateSearchRequest;
import org.eclipse.update.search.UpdateSearchScope;
import org.eclipse.update.ui.UpdateJob;
import org.eclipse.update.ui.UpdateManagerUI;

/**
 * 	Searches for new features that the current application does NOT have.
 * 	@author mheiss
 */
public class AddExtensionAction extends AbstractUpdateAction {
	/**
	 * Default class constructor
	 */
	public AddExtensionAction() {
		setId("at.rc.tacos.client.update.addExtension"); //$NON-NLS-1$
		setText(Messages.getString("AddExtensionAction.searchForNewFeatures")); //$NON-NLS-1$
		setToolTipText(Messages.getString("AddExtensionAction.searchForNewFeaturesToolTipp"));	//$NON-NLS-1$
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/software-update-available.png"));	//$NON-NLS-1$
	}
	
	@Override
	protected void run(final IWorkbenchWindow window) {
		BusyIndicator.showWhile(window.getShell().getDisplay(), new Runnable() {
			@Override
			public void run() {
				UpdateJob job = new UpdateJob(Messages.getString("AddExtensionAction.updateJob"),getSearchRequest());	//$NON-NLS-1$
				UpdateManagerUI.openInstaller(window.getShell(),job);
			}
		});
	}
	
	/**
	 * Helper method to create a search request
	 */
	private UpdateSearchRequest getSearchRequest() {
		// get the preference for the update site
		Plugin plugin = Activator.getDefault();
		Preferences prefs = plugin.getPluginPreferences();
		String updateSite = prefs.getString("updateSite");	//$NON-NLS-1$
		
		//search for updates
		UpdateSearchRequest result = new UpdateSearchRequest(UpdateSearchRequest.createDefaultSiteSearchCategory(),new UpdateSearchScope());
		result.addFilter(new BackLevelFilter());
		result.addFilter(new EnvironmentFilter());
		UpdateSearchScope scope = new UpdateSearchScope();
		try {
			scope.addSearchSite("TACOS Update Site",new URL(updateSite),null);	//$NON-NLS-1$
		}
		catch(Exception e) {
			//skip bad urls
		}
		result.setScope(scope);
		return result;
	}

}
