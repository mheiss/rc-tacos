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
package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;

import at.rc.tacos.client.view.DialysisForm;

/**
 * Opens the form to create a new dialysis entry
 * 
 * @author b.thek
 */
public class DialysisOpenNewFormAction extends Action {

	/**
	 * Default class construtor.
	 * 
	 * @param viewer
	 *            the table viewer
	 */
	public DialysisOpenNewFormAction() {
		setText("Neuen Dialyseeintrag vornehmen");
		setToolTipText("Öffnet ein Fenster, um einen neuen Dialyseeintrag zu erstellen.");
	}

	@Override
	public void run() {
		DialysisForm form = new DialysisForm();
		form.open();
	}
}
