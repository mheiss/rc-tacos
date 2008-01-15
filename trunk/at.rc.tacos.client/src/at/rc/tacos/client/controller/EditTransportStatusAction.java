package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.client.view.TransportForm;
import at.rc.tacos.client.view.TransportStatiForm;
import at.rc.tacos.model.Transport;

/**
 * Opens the form to edit the transport stati
 * @author b.thek
 */
public class EditTransportStatusAction extends Action
{
	//properties
	private TableViewer viewer;
	
	/**
	 * Default class constructor.
	 * @param viewer the table viewer
	 */
	public EditTransportStatusAction(TableViewer viewer)
	{
		this.viewer = viewer;
		setText("Stati bearbeiten");
		setToolTipText("Öffnet ein Fenster um die Stati zu bearbeiten");
	}
	
	@Override
	public void run()
	{
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected transport
		Transport transport = (Transport)((IStructuredSelection)selection).getFirstElement();
		//open the editor
		TransportStatiForm form = new TransportStatiForm(transport);
		form.open();
	}
}
