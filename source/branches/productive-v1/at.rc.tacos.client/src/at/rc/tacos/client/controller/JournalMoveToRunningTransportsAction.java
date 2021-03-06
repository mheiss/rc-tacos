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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.Transport;

/**
 * Moves the given transport to the running transports
 * 
 * @author b.thek
 */
public class JournalMoveToRunningTransportsAction extends Action implements ITransportStatus, IProgramStatus {

	// properties
	private TableViewer viewer;

	/**
	 * Default class constructor.
	 * 
	 * @param viewer
	 *            the table viewer
	 */
	public JournalMoveToRunningTransportsAction(TableViewer viewer) {
		this.viewer = viewer;
		setText("Nach laufende Transporte verschieben");
		setToolTipText("Verschiebt den Transport zu den laufenden Transporten");
	}

	@Override
	public void run() {
		// the selection
		ISelection selection = viewer.getSelection();
		// get the selected transport
		Transport transport = (Transport) ((IStructuredSelection) selection).getFirstElement();
		// change transport program status to 'underway'
		transport.setProgramStatus(PROGRAM_STATUS_UNDERWAY);
		transport.removeStatus(ITransportStatus.TRANSPORT_STATUS_DESTINATION_FREE);
		NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
	}
}
