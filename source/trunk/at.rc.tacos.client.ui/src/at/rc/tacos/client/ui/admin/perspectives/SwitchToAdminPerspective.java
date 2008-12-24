package at.rc.tacos.client.ui.admin.perspectives;

import org.eclipse.jface.resource.ImageDescriptor;

import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.perspectives.AbstractPerspectiveSwitcher;

/**
 * This is a workbench action to switch to the administrator view
 * 
 * @author Michael
 */
public class SwitchToAdminPerspective extends AbstractPerspectiveSwitcher {

	public SwitchToAdminPerspective() {
		super(AdminPerspective.ID);
	}

	/**
	 * Returns the image for the admin perspective
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return UiWrapper.getDefault().getImageRegistry().getDescriptor("toolbar.admin");
	}

	/**
	 * Returns the text to render
	 */
	@Override
	public String getText() {
		return "Einstellungen";
	}

	/**
	 * The tooltip text
	 */
	@Override
	public String getToolTipText() {
		return "Öffnet ein Fenster zur Verwaltung des TACOS Programms und der Resourcen";
	}
}
