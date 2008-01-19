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
 * Moves the transport to the outstanding transports
 * @author b.thek
 */
public class MoveToOutstandingTransportsAction extends Action implements ITransportStatus, IProgramStatus
{
	//properties
	private TableViewer viewer;
	
	/**
	 * Default class construtor.
	 * @param viewer the table viewer
	 */
	public MoveToOutstandingTransportsAction(TableViewer viewer)
	{
		this.viewer = viewer;
		setText("Nach offene Transporte verschieben");
		setToolTipText("Verschiebt den Transport zu den offenen Transporten");
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
		transport.getStatusMessages().clear();
		transport.setProgramStatus(PROGRAM_STATUS_OUTSTANDING);//TODO- clear the staff members, write the Transport number into the help table
		NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
	}
}
