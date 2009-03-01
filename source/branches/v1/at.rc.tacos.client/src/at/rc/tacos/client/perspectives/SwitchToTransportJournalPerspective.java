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
 * Switches to the journal perspective
 * 
 * @author Michael
 */
public class SwitchToTransportJournalPerspective extends AbstractPerspectiveSwitcher {

	public SwitchToTransportJournalPerspective() {
		super(TransportJournalPerspective.ID);
	}

	/**
	 * Returns the image for the perspective
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return ImageFactory.getInstance().getRegisteredImageDescriptor("toolbar.transportJournal");
	}

	/**
	 * Returns the text to render
	 */
	@Override
	public String getText() {
		return "Journal";
	}

	/**
	 * The tooltip text
	 */
	@Override
	public String getToolTipText() {
		return "Zeigt das Transport Journalblatt an.";
	}
}
