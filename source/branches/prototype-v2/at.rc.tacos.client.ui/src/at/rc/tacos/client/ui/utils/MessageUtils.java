package at.rc.tacos.client.ui.utils;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * The <code>MessageUtils</code> contains static helper method to show an
 * {@link MessageDialog} to the user.
 * 
 * @author Michael
 */
public class MessageUtils {

	/**
	 * Shows an <b>error</b> message to the user with the given title and
	 * content.
	 * <p>
	 * This method can be called within any thread, the
	 * {@link Display#syncExec(Runnable)} is used to wrapp the dialog
	 * </p>
	 * 
	 * @param title
	 *            the title of the message box
	 * @param content
	 *            the message to display
	 */
	public static void showSyncErrorMessage(final String title, final String content) {
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				Shell shell = Display.getDefault().getActiveShell();
				MessageDialog.openError(shell, title, content);
			}
		});
	}

}
