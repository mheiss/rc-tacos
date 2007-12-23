package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.Transport;

/**
 * Opens the editor to edit the selected entry
 * @author b.thek
 */
public class ChangeResponsibleStationAction extends Action
{
	//properties
	private TableViewer viewer;
	
	/**
	 * Default class construtor.
	 * @param viewer the table viewer
	 */
	public ChangeResponsibleStationAction(TableViewer viewer)
	{
		this.viewer = viewer;
		setText("Dienststelle");
		setToolTipText("Öffnet ein Fenster um den Transport zu bearbeiten");
	}
	
	@Override
	public void run()
	{
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected transport
		Transport transport = (Transport)((IStructuredSelection)selection).getFirstElement();
		//TODO- how to set the responsible station from a submenu?
//		transport.setResponsibleStation(responsibleStation);
		NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
	}
}
