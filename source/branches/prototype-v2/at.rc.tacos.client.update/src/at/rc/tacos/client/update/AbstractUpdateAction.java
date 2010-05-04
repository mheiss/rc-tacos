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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

/**
 * Abstract Action to be used as base for all update actions
 */
public abstract class AbstractUpdateAction extends Action implements IWorkbenchWindowActionDelegate {

	private IWorkbenchWindow window;

	public AbstractUpdateAction() {
		// do nothing
	}

	public void run() {
		run(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
	}

	public void run(IAction action) {
		run(window);
	}

    abstract protected void run(final IWorkbenchWindow window);

	public void selectionChanged(IAction action, ISelection selection) {
		// do nothing
	}

	public void dispose() {
		// do nothing
	}

	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}
