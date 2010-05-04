package at.rc.tacos.client.ui.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.platform.model.DialysisPatient;
import at.rc.tacos.platform.net.message.RemoveMessage;

/**
 * Opens a dialog to confirm and then delete the selected dialysis entry
 * 
 * @author b.thek
 */
public class DialysisDeleteAction extends Action {

	// properties
	private TableViewer viewer;

	/**
	 * Default class constructor.
	 * 
	 * @param viewer
	 *            the table viewer
	 */
	public DialysisDeleteAction(TableViewer viewer) {
		this.viewer = viewer;
		setText("L�schen");
		setToolTipText("L�scht den Dialyseeintrag endg�ltig!");
	}

	@Override
	public void run() {
		ISelection selection = viewer.getSelection();
		DialysisPatient dia = (DialysisPatient) ((IStructuredSelection) selection).getFirstElement();
		// delete the entry
		boolean cancelConfirmed = MessageDialog.openQuestion(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				"Dialyseeintrag l�schen", "M�chten Sie den Dialyseeintrag wirklich l�schen?");
		if (!cancelConfirmed)
			return;
		// request to delete
		RemoveMessage<DialysisPatient> removeMessage = new RemoveMessage<DialysisPatient>(dia);
		removeMessage.asnchronRequest(NetWrapper.getSession());
	}
}
