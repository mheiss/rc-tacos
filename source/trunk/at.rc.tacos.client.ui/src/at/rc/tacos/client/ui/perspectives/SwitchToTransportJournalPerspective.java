package at.rc.tacos.client.ui.perspectives;

import org.eclipse.jface.resource.ImageDescriptor;

import at.rc.tacos.client.ui.UiWrapper;

/**
 * Switches to the journal perspective
 * 
 * @author Michael
 */
public class SwitchToTransportJournalPerspective extends AbstractPerspectiveSwitcher {

	public SwitchToTransportJournalPerspective() {
		super(TransportJournalPerspective.ID);
	}

	/**
	 * Returns the image for the perspective
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return UiWrapper.getDefault().getImageRegistry().getDescriptor("toolbar.transportJournal");
	}

	/**
	 * Returns the text to render
	 */
	@Override
	public String getText() {
		return "Journal";
	}

	/**
	 * The tooltip text
	 */
	@Override
	public String getToolTipText() {
		return "Zeigt das Transport Journalblatt an.";
	}
}
