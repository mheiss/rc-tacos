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

import org.eclipse.jface.resource.ImageDescriptor;

import at.rc.tacos.factory.ImageFactory;

/**
 * This is a workbench action to switch to the log perspective
 * 
 * @author Michael
 */
public class SwitchToLogPerspective extends AbstractPerspectiveSwitcher {

	public SwitchToLogPerspective() {
		super(LogPerspective.ID);
	}

	/**
	 * Returns the image for the perspective
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return ImageFactory.getInstance().getRegisteredImageDescriptor("toolbar.log");
	}

	/**
	 * Returns the text to render
	 */
	@Override
	public String getText() {
		return "Log";
	}

	/**
	 * The tooltip text
	 */
	@Override
	public String getToolTipText() {
		return "Zeigt alle mitprotokollierten Meldungen und Fehler an.";
	}
}
