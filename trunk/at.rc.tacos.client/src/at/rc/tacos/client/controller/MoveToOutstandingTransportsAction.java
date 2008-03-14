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
		System.out.println("......in run von MoveToOutstandingTransportsAction.........");
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected transport
		Transport transport = (Transport)((IStructuredSelection)selection).getFirstElement();
		System.out.println("........der transport(from street) in der run(): " +transport.getFromStreet());
		//change transport program status to 'outstanding'
		transport.getStatusMessages().clear();
		System.out.println(".... nach dem clear der StatusMessages");
		transport.setProgramStatus(PROGRAM_STATUS_OUTSTANDING);
		if(transport.getTransportNumber() <0 )
			transport.setTransportNumber(0);
		System.out.println(".....nach dem setProgramStatus (outstanding) fromstreet und der Programstatus: " +transport.getFromStreet() +" " +transport.getProgramStatus());
		//remove the vehicle to release the transport number
		transport.clearVehicleDetail();
		System.out.println(".... nach dem clearVehicleDetail.....fromstreet und der Programstatus: " +transport.getFromStreet() +" " +transport.getProgramStatus());
		NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
	}
}
