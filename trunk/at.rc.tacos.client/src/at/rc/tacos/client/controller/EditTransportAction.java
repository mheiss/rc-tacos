package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.client.view.TransportForm;
import at.rc.tacos.model.Transport;

/**
 * Opens the editor to edit the selected entry
 * @author b.thek
 */
public class EditTransportAction extends Action
{
	//properties
	private TableViewer viewer;
	
	/**
	 * Default class construtor.
	 * @param viewer the table viewer
	 */
	public EditTransportAction(TableViewer viewer)
	{
		this.viewer = viewer;
		setText("Eintrag bearbeiten");
		setToolTipText("�ffnet ein Fenster um den Transport zu bearbeiten");
	}
	
	@Override
	public void run()
	{
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected transport
		Transport transport = (Transport)((IStructuredSelection)selection).getFirstElement();
		//open the editor
		TransportForm form = new TransportForm(transport);
		form.open();
	}
}
