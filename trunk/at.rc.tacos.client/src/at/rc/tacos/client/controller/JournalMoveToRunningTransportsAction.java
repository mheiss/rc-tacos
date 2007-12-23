package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.Transport;

/**
 * Moves the transport to the running transports
 * @author b.thek
 */
public class JournalMoveToRunningTransportsAction extends Action implements ITransportStatus
{
	//properties
	private TableViewer viewer;
	
	/**
	 * Default class construtor.
	 * @param viewer the table viewer
	 */
	public JournalMoveToRunningTransportsAction(TableViewer viewer)
	{
		this.viewer = viewer;
		setText("Nach laufende Transporte verschieben");
		setToolTipText("Verschiebt den Transport zu den laufenden Transporten");
	}
	
	@Override
	public void run()
	{
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected transport
		Transport transport = (Transport)((IStructuredSelection)selection).getFirstElement();
		//TODO Michael bitte kontrollieren
		//change transport program status to 'outstanding'
		transport.addStatus(TRANSPORT_STATUS_DESTINATION_FREE, 0);
		transport.addStatus(TRANSPORT_STATUS_CAR_IN_STATION,0);
		NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
	}
}
