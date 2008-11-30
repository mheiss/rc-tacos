package at.rc.tacos.client.ui.perspectives;

import org.eclipse.jface.resource.ImageDescriptor;

import at.rc.tacos.client.ui.Activator;

public class SwitchToTransportPrebookingPerspective extends AbstractPerspectiveSwitcher {

	public SwitchToTransportPrebookingPerspective() {
		super(TransportPrebookingPerspective.ID);
	}

	/**
	 * Returns the image for the perspective
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return Activator.getDefault().getImageRegistry().getDescriptor("toolbar.transportPrebooking");
	}

	/**
	 * Returns the text to render
	 */
	@Override
	public String getText() {
		return "Vormerkungen";
	}

	/**
	 * The tooltip text
	 */
	@Override
	public String getToolTipText() {
		return "Zeigt eine Übersicht über alle vorgemerkten Transporte an.";
	}
}
