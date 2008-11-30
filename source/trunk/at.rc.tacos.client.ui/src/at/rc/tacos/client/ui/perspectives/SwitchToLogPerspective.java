package at.rc.tacos.client.ui.perspectives;

import org.eclipse.jface.resource.ImageDescriptor;

import at.rc.tacos.client.ui.Activator;

/**
 * This is a workbench action to switch to the log perspective
 * 
 * @author Michael
 */
public class SwitchToLogPerspective extends AbstractPerspectiveSwitcher {

	public SwitchToLogPerspective() {
		super(LogPerspective.ID);
	}

	/**
	 * Returns the image for the perspective
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return Activator.getDefault().getImageRegistry().getDescriptor("toolbar.log");
	}

	/**
	 * Returns the text to render
	 */
	@Override
	public String getText() {
		return "Log";
	}

	/**
	 * The tooltip text
	 */
	@Override
	public String getToolTipText() {
		return "Zeigt alle mitprotokollierten Meldungen und Fehler an.";
	}
}
