package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.client.view.DialysisForm;
import at.rc.tacos.client.view.TransportForm;
import at.rc.tacos.model.DialysisPatient;
import at.rc.tacos.model.Transport;

/**
 * Opens the form to create a new dialysis entry
 * @author b.thek
 */
public class DialysisOpenNewFormAction extends Action
{
	//properties
	private TableViewer viewer;
	
	/**
	 * Default class construtor.
	 * @param viewer the table viewer
	 */
	public DialysisOpenNewFormAction(TableViewer viewer)
	{
		this.viewer = viewer;
		setText("Neuen Dialyseeintrag vornehmen");
		setToolTipText("Öffnet ein Fenster, um einen neuen Dialyseeintrag zu erstellen.");
	}
	
	@Override
	public void run()
	{
		DialysisForm form = new DialysisForm();
		form.open();
	}
}
