package at.rc.tacos.client.ui.dialog;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.platform.model.Lockable;
import at.rc.tacos.platform.net.message.AddMessage;
import at.rc.tacos.platform.net.message.ExecMessage;
import at.rc.tacos.platform.net.message.UpdateMessage;

/**
 * The <code>AbstractLockableDialog</code> provides the needed methods an
 * callbacks to ensure that an {@link Lockable} instance that is currently
 * edited is locked and unlocked when the dialog is openend and closed.
 * <p>
 * The newly opened dialog will be centered in the current {@link Display}
 * </p>
 * 
 * @author Michael
 */
public abstract class AbstractLockableDialog<T extends Lockable> extends TitleAreaDialog {

	// the lockable object
	private T lockable;

	/**
	 * Creates a new dialog instance without requesting and releasing the lock.
	 * This implementation should be used to create a new object instance.
	 * 
	 * @param shell
	 *            the parent shell instance
	 */
	public AbstractLockableDialog(Shell shell) {
		super(shell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		this.lockable = null;
	}

	/**
	 * Creates a new dialog instance and ensures that the element is locked and
	 * unlocked properly.
	 * 
	 * @param shell
	 *            the parent shell instance
	 * @param lockable
	 *            the current edited object instance
	 */
	public AbstractLockableDialog(Shell shell, T lockable) {
		super(shell);
		this.lockable = lockable;
	}

	/**
	 * Callback method to center and initialize the dialog
	 */
	@Override
	protected final void configureShell(Shell newShell) {
		super.configureShell(newShell);
		// center the dialog
		Rectangle parentSize = Display.getCurrent().getPrimaryMonitor().getBounds();
		Rectangle mySize = newShell.getBounds();
		int locationX = (parentSize.width - mySize.width) / 2 + parentSize.x;
		int locationY = (parentSize.height - mySize.height) / 2 + parentSize.y;
		newShell.setLocation(new Point(locationX, locationY));
	}

	/**
	 * Opens this dialog and sends a lock request for the configured
	 * {@link Lockable} instance.
	 */
	@Override
	public final int open() {
		// check if we should send a lock request
		if (lockable != null) {
			// set the lock
			lockable.setLocked(true);
			lockable.setLockedBy(NetWrapper.getSession().getUsername());
			// send lock request
			ExecMessage<T> execMessage = new ExecMessage<T>("doLock", lockable);
			execMessage.asnchronRequest(NetWrapper.getSession());
		}

		// add the listeners
		registerListeners();

		// create and open the dialog
		return super.open();
	}

	@Override
	public final boolean close() {
		// check if the user wants to close the dialog
		if (getReturnCode() == CANCEL
				& !MessageDialog.openQuestion(getShell(), "Abbrechen",
						"Wollen Sie wirklich abbrechen?\nAlle ungespeicherten Änderungen gehen verloren!")) {
			return false;
		}

		// check if we should send a lock request
		if (lockable != null) {
			lockable.setLocked(false);
			lockable.setLockedBy(null);
			// send the unlock request
			ExecMessage<T> execMessage = new ExecMessage<T>("doUnlock", lockable);
			execMessage.asnchronRequest(NetWrapper.getSession());
		}

		// remove the listeners
		removeListeners();

		// close the dialog
		return super.close();
	}

	/**
	 * Notification that the <code>OK</code> button was pressed. The input will
	 * be validated and then either the {@link #addRequest()} or the
	 * {@link #updateRequest()} will be executed to persist the changes.
	 */
	@Override
	protected final void okPressed() {
		// validate the input
		if (!validateInput()) {
			return;
		}

		// set the return code and go on
		setReturnCode(OK);
		super.okPressed();
	}

	/**
	 * Notification that the <code>CANCEL</code> button was pressed. The input
	 * will not be validated or persisted and the dialog is closed.
	 */
	@Override
	protected final void cancelPressed() {
		// set the return code and go on
		setReturnCode(CANCEL);
		super.cancelPressed();
	}

	/**
	 * The default implementation will send an {@link AddMessage} with the
	 * current object instance to the server.
	 */
	protected void addRequest() {
		AddMessage<T> addMessage = new AddMessage<T>(lockable);
		addMessage.asnchronRequest(NetWrapper.getSession());
	}

	/**
	 * The default implementation will send an {@link UpdateMessage} with the
	 * current object instance to the server.
	 */
	protected void updateRequest() {
		UpdateMessage<T> updateMessage = new UpdateMessage<T>(lockable);
		updateMessage.asnchronRequest(NetWrapper.getSession());
	}

	@Override
	protected final Control createContents(Composite parent) {
		Control contents = super.createContents(parent);
		createDialogHeader();
		return contents;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		createDialogContent(composite);
		return composite;
	}

	/**
	 * This callback allows us to validate the current input of the dialog.
	 * 
	 * @return true if the dialog content is valid otherwise false
	 */
	public abstract boolean validateInput();

	/**
	 * This callback allows us to register the needed listeners.
	 */
	public abstract void registerListeners();

	/**
	 * This callback allows us to remove the registered listeners.
	 */
	public abstract void removeListeners();

	/**
	 * Setup and initialize the dialog image and text
	 */
	public abstract void createDialogHeader();

	/**
	 * Setup and initialize the dialog content.
	 */
	public abstract void createDialogContent(Composite parent);

}
