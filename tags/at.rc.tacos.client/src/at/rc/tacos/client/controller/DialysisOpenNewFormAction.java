package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import at.rc.tacos.client.view.DialysisForm;

/**
 * Opens the form to create a new dialysis entry
 * @author b.thek
 */
public class DialysisOpenNewFormAction extends Action
{
	/**
	 * Default class construtor.
	 * @param viewer the table viewer
	 */
	public DialysisOpenNewFormAction()
	{
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
