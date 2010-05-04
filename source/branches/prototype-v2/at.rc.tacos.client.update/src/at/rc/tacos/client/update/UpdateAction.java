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

import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.update.ui.UpdateJob;
import org.eclipse.update.ui.UpdateManagerUI;

/**
 * 	Sets up an update job that checks for updates of the current functions
 * 	@author mheiss
 */
public class UpdateAction extends AbstractUpdateAction {
	/**
	 * Default class constructor
	 */
	public UpdateAction() {
		setId("at.rc.tacos.client.update.newUpdates");  //$NON-NLS-1$
		setText(Messages.getString("UpdateAction.update")); //$NON-NLS-1$
		setToolTipText(Messages.getString("UpdateAction.updateTooltip")); //$NON-NLS-1$
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/software-update-available.png"));	//$NON-NLS-1$
	}

	@Override
	public void run(final IWorkbenchWindow window) {
		BusyIndicator.showWhile(window.getShell().getDisplay(), new Runnable() {
			@Override
			public void run() {
				UpdateJob job = new UpdateJob(Messages.getString("UpdateAction.updateJob"),false,false); //$NON-NLS-1$
				UpdateManagerUI.openInstaller(window.getShell(),job);
			}
		});
	}
}
