package at.rc.tacos.client.ui.notification;

import org.eclipse.mylyn.internal.provisional.commons.ui.AbstractNotificationPopup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import at.rc.tacos.model.RosterEntry;

/**
 * This is a popup notification to inform the user about a roster entry update (add, update, delete, ...)
 * @author Michael
 */
public class RosterNotificationPopup extends AbstractNotificationPopup
{
	//the roster entry to inform about the changes
	private RosterEntry rosterEntry;
	
	/**
	 * Default class constructor to create a popup
	 * @param display
	 */
	public RosterNotificationPopup(Display display,RosterEntry rosterEntry) 
	{
		super(display);
		this.rosterEntry = rosterEntry;
	}
	
	@Override
	protected void createTitleArea(Composite parent) 
	{
		((GridData) parent.getLayoutData()).heightHint = 24;

		Label titleCircleLabel = new Label(parent, SWT.NONE);
		titleCircleLabel.setText("RSSOwl - Incoming News");
		titleCircleLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		titleCircleLabel.setCursor(parent.getDisplay().getSystemCursor(SWT.CURSOR_HAND));

		Label closeButton = new Label(parent, SWT.NONE);
		closeButton.setText("Close");
		closeButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		closeButton.setCursor(parent.getDisplay().getSystemCursor(SWT.CURSOR_HAND));
		closeButton.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseUp(MouseEvent e) {
				close();
			}
		});
	}

	@Override
	protected void createContentArea(Composite parent) {
		for (int i = 0; i < 5; i++) {
			Label l = new Label(parent, SWT.None);
			l.setText("News: " + rosterEntry.getPlannedStartOfWork() + " - "+rosterEntry.getPlannedEndOfWork());
			l.setBackground(parent.getBackground());
		}
	}

	@Override
	protected String getPopupShellTitle() {
		return "Sample Notification";
	}
	
	@Override
	public long getDelayClose() {
		return 5000;
	}
}
