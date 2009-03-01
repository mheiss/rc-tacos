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
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.client.providers.MultiTransportContentProvider;
import at.rc.tacos.factory.ImageFactory;

public class RemoveTransportFromMultiTransportList extends Action {

	private MultiTransportContentProvider provider;
	private TableViewer viewer;
	private int index;

	public RemoveTransportFromMultiTransportList(MultiTransportContentProvider provider, TableViewer viewer, int index) {
		this.provider = provider;
		this.viewer = viewer;
		this.index = index;
	}

	/**
	 * Returns the tool tip text for the action
	 * 
	 * @return the tool tip text
	 */
	@Override
	public String getToolTipText() {
		return "Löscht den markierten Transport aus der Liste der zu speichernden Transporte";
	}

	/**
	 * Returns the text to show in the tool bar
	 * 
	 * @return the text to render
	 */
	@Override
	public String getText() {
		return "Löschen";
	}

	/**
	 * Returns the image to use for this action.
	 * 
	 * @return the image to use
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return ImageFactory.getInstance().getRegisteredImageDescriptor("toolbar.createTransport");
	}

	/**
	 * Shows the abut dialog of the application
	 */
	@Override
	public void run() {
		provider.removeTransport(index);
		viewer.refresh();
	}
}
