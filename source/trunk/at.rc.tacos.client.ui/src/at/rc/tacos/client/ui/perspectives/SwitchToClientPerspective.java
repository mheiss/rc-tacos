package at.rc.tacos.client.ui.perspectives;

import org.eclipse.jface.resource.ImageDescriptor;

import at.rc.tacos.client.ui.Activator;

/**
 * This is a workbench action to switch the resource perspective
 * 
 * @author Michael
 */
public class SwitchToClientPerspective extends AbstractPerspectiveSwitcher {

	public SwitchToClientPerspective() {
		super(Perspective.ID);
	}

	/**
	 * Returns the image for the perspective
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return Activator.getDefault().getImageRegistry().getDescriptor("toolbar.resources");
	}

	/**
	 * Returns the text to render
	 */
	@Override
	public String getText() {
		return "Ressourcen";
	}

	/**
	 * The tooltip text
	 */
	@Override
	public String getToolTipText() {
		return "Öffnet die Ansicht zur Verwaltung des Personals und der Fahrzeuge";
	}
}
