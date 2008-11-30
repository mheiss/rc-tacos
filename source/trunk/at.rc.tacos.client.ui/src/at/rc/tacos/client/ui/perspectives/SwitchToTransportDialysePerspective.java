package at.rc.tacos.client.ui.perspectives;

import org.eclipse.jface.resource.ImageDescriptor;

import at.rc.tacos.client.ui.Activator;

public class SwitchToTransportDialysePerspective extends AbstractPerspectiveSwitcher {

	public SwitchToTransportDialysePerspective() {
		super(TransportDialysePerspective.ID);
	}

	/**
	 * Returns the image for the perspective
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return Activator.getDefault().getImageRegistry().getDescriptor("toolbar.transportDialyse");
	}

	/**
	 * Returns the text to render
	 */
	@Override
	public String getText() {
		return "Dialysetransporte";
	}

	/**
	 * The tooltip text
	 */
	@Override
	public String getToolTipText() {
		return "Zeigt eine Übersicht über alle Dialyse Transporte an";
	}
}
