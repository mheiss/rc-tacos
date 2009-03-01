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
package at.rc.tacos.client.modelManager;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.widgets.ToolBar;

/**
 * Customized toolbar manager to show the text of the added actions
 * 
 * @author Michael
 */
public class MyToolbarManager extends ToolBarManager {

	/**
	 * Default constructor
	 */
	public MyToolbarManager() {
		super();
	}

	/**
	 * Default constructor to set the style of the manager
	 * 
	 * @param style
	 *            the style to set
	 */
	public MyToolbarManager(int style) {
		super(style);
	}

	/**
	 * Default constructor to set the toolbar to use
	 * 
	 * @param toolbar
	 *            the toolbar to manage
	 */
	public MyToolbarManager(ToolBar toolbar) {
		super(toolbar);
	}

	/**
	 * Customized add method to fore the rendering of the action text
	 * 
	 * @param action
	 *            the action to add
	 */

	@Override
	public void add(IAction action) {
		ActionContributionItem contributionItem = new ActionContributionItem(action);
		contributionItem.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		add(contributionItem);
	}
}
