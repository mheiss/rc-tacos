package at.rc.tacos.client.ui.perspectives;

import org.eclipse.jface.resource.ImageDescriptor;

import at.rc.tacos.client.ui.Activator;

/**
 * This is a workbench action to switch the perspective
 * 
 * @author Michael
 */
public class SwitchToTransportPerspective extends AbstractPerspectiveSwitcher {

	public SwitchToTransportPerspective() {
		super(TransportPerspective.ID);
	}

	/**
	 * Returns the image for the perspective
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return Activator.getDefault().getImageRegistry().getDescriptor("toolbar.transport");
	}

	/**
	 * Returns the text to render
	 */
	@Override
	public String getText() {
		return "Disposition";
	}

	/**
	 * The tooltip text
	 */
	@Override
	public String getToolTipText() {
		return "Zeigt eine Übersicht der offenen und disponierten Transporte";
	}
}
