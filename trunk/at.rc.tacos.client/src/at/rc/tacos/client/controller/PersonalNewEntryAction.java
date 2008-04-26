package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.client.view.RosterEntryForm;
import at.rc.tacos.factory.ImageFactory;

public class PersonalNewEntryAction extends Action
{
	/**
	 * Returns the tooltip text for the action
	 * @return the tooltip text
	 */
	@Override
	public String getToolTipText() 
	{
		return "Öffnet ein Fenster um einen Dienstplan eintrag zu erstellen";
	}

	/**
	 * Retruns the text to show in the toolbar
	 * @return the text to render
	 */
	@Override
	public String getText()
	{
		return "Dienst";
	}

	/**
	 * Returns the image to use for this action.
	 * @return the image to use
	 */
	@Override
	public ImageDescriptor getImageDescriptor() 
	{
		return ImageFactory.getInstance().getRegisteredImageDescriptor("toolbar.createRoster");
	}

	/**
	 * Shows the abut dialog of the application
	 */
	@Override
	public void run()
	{
		//open the editor
		Shell parent = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

		//get the parent and the window shell
		RosterEntryForm window = new RosterEntryForm(parent);
		window.getShell().setVisible(false);
		window.create();

		//get the shell and resize
		Shell myShell = window.getShell();
		myShell.setSize(500, 600);

		//calculate and draw centered
		Rectangle workbenchSize = parent.getBounds();
		Rectangle mySize = myShell.getBounds();
		int locationX, locationY;
		locationX = (workbenchSize.width - mySize.width)/2+workbenchSize.x;
		locationY = (workbenchSize.height - mySize.height)/2+workbenchSize.y;
		myShell.setLocation(locationX,locationY);

		//now open the window
		myShell.open();
		myShell.setVisible(true);
	}
}
