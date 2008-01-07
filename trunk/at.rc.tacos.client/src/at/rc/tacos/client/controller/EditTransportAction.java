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
	private String editingType;
	
	/**
	 * Default class construtor.
	 * @param viewer the table viewer
	 */
	public EditTransportAction(TableViewer viewer, String editingType)
	{
		this.viewer = viewer;
		this.editingType = editingType;
		setText("Eintrag bearbeiten");
		setToolTipText("Öffnet ein Fenster um den Transport zu bearbeiten");
	}
	
	@Override
	public void run()
	{
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected transport
		Transport transport = (Transport)((IStructuredSelection)selection).getFirstElement();
		System.out.println("......EditTransportAction, run() transport: "+transport);
		//open the editor
		TransportForm form = new TransportForm(transport,editingType);
		form.open();
	}
}
