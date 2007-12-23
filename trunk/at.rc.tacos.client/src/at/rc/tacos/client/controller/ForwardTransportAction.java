package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.Transport;

/**
 * Opens the editor to edit the selected entry
 * @author b.thek
 */
public class ForwardTransportAction extends Action implements ITransportStatus
{
	//properties
	private TableViewer viewer;
	
	/**
	 * Default class construtor.
	 * @param viewer the table viewer
	 */
	public ForwardTransportAction(TableViewer viewer)
	{
		this.viewer = viewer;
		setText("Weiterleiten");
		setToolTipText("Verschiebt den Transport in das Journalblatt und setzt die Transportnummer auf \"WLTG\"");
	}
	
	@Override
	public void run()
	{
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected transport
		Transport transport = (Transport)((IStructuredSelection)selection).getFirstElement();
		transport.setTransportNumber("WLTG");//'Weiterleitung'
		NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
	}
}
