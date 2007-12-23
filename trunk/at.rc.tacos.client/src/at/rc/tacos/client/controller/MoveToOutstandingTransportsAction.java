package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.model.Transport;

/**
 * Moves the transport to the outstanding transports
 * @author b.thek
 */
public class MoveToOutstandingTransportsAction extends Action implements ITransportStatus
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
		transport.addStatus(TRANSPORT_STATUS_ORDER_PLACED, 0);
		transport.addStatus(TRANSPORT_STATUS_ON_THE_WAY,0);
		transport.addStatus(TRANSPORT_STATUS_AT_PATIENT, 0);
		transport.addStatus(TRANSPORT_STATUS_START_WITH_PATIENT, 0);
		transport.addStatus(TRANSPORT_STATUS_AT_DESTINATION,0);
		transport.addStatus(TRANSPORT_STATUS_DESTINATION_FREE, 0);
		transport.addStatus(TRANSPORT_STATUS_CAR_IN_STATION,0);
		transport.addStatus(TRANSPORT_STATUS_OUT_OF_OPERATION_AREA, 0);
		transport.addStatus(TRANSPORT_STATUS_BACK_IN_OPERATION_AREA, 0);
		transport.addStatus(TRANSPORT_STATUS_OTHER, 0);
	}
}
