/*******************************************************************************
 * Copyright (c) 2008, 2009 Internettechnik, FH JOANNEUM
 * http://www.fh-joanneum.at/itm
 * 
 * 	Licenced under the GNU GENERAL PUBLIC LICENSE Version 2;
 * 	You may obtain a copy of the License at
 * 	http://www.gnu.org/licenses/gpl-2.0.txt
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *******************************************************************************/
package at.rc.tacos.client.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import at.rc.tacos.client.view.DialysisView;
import at.rc.tacos.client.view.NavigationView;

/**
 * This is the perspective to show the dialyse transports
 * 
 * @author Michael
 */
public class TransportDialysePerspective implements IPerspectiveFactory {

	public static final String ID = "at.rc.tacos.client.perspectives.transportDialyse";

	/**
	 * Set up the layout of the workbench
	 * 
	 * @param layout
	 *            the page layout to use
	 */
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
		// the main components
		layout.addStandaloneView(NavigationView.ID, true, IPageLayout.TOP, 0.10f, editorArea);
		layout.addStandaloneView(DialysisView.ID, true, IPageLayout.RIGHT, 0.90f, editorArea);
	}
}
