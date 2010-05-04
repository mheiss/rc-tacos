package at.rc.tacos.client.ui.dialog;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.platform.model.Lockable;
import at.rc.tacos.platform.net.message.AddMessage;
import at.rc.tacos.platform.net.message.ExecMessage;
import at.rc.tacos.platform.net.message.UpdateMessage;

/**
 * The <code>AbstractLockableDialog</code> provides the needed methods and
 * callbacks to ensure that an {@link Lockable} instance that is currently
 * edited is locked and unlocked when the dialog is openend and closed.
 * <p>
 * The newly opened dialog will be centered in the current {@link Display}.
 * </p>
 * 
 * @author Michael
 */
public abstract class AbstractLockableDialog<T extends Lockable> extends TitleAreaDialog {

	private Logger log = LoggerFactory.getLogger(AbstractLockableDialog.class);

	// the lockable object
	private T lockable;

	protected FormToolkit toolkit;

	// flag to indicate a new entry
	private final boolean createNew;

	/**
	 * Default class constructor to setup a new dialog instance.
	 * 
	 * @param shell
	 *            the parent shell instance
	 * @param lockable
	 *            the current edited object instance
	 * @param createNew
	 *            a flag to indicat whether if the entry should be created or
	 *            updated
	 */
	public AbstractLockableDialog(Shell shell, T lockable, boolean createNew) {
		super(shell);
		this.lockable = lockable;
		this.createNew = createNew;
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
		// check if the object is currenlty locked
		if (lockable.isLocked()) {
			boolean forceEdit = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "Information: Eintrag wird bearbeitet",
					"Der Eintrag den Sie bearbeiten möchten wird bereits von " + lockable.getLockedBy() + " bearbeitet\n"
							+ "Ein gleichzeitiges Bearbeiten kann zu unerwarteten Fehlern führen!\n\n"
							+ "Möchten Sie den Eintrag trotzdem bearbeiten?");
			if (!forceEdit)
				return CANCEL;

			// logg the override of the lock
			String username = NetWrapper.getSession().getUsername();
			log.warn("Der Eintrag " + lockable + " wird trotz Sperrung durch " + lockable.getLockedBy() + " von " + username + " bearbeitet");
		}

		// set the lock
		lockable.setLocked(true);
		lockable.setLockedBy(NetWrapper.getSession().getUsername());
		// send lock request
		ExecMessage<T> execMessage = new ExecMessage<T>("doLock", lockable);
		execMessage.asnchronRequest(NetWrapper.getSession());

		// add the listeners
		registerListeners();

		// create and open the dialog
		return super.open();
	}

	@Override
	public final boolean close() {
		// check if the user wants to close the dialog
		if (getReturnCode() == CANCEL) {
			if (!MessageDialog.openQuestion(getShell(), "Abbrechen",
					"Wollen Sie wirklich abbrechen?\nAlle ungespeicherten Änderungen gehen verloren!")) {
				return false;
			}
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
			Display.getCurrent().beep();
			return;
		}

		// syncronize the object with the values from the dialog
		persistObject(lockable);

		// create a new entry or update an existing
		if (createNew) {
			addRequest();
		}
		else {
			updateRequest();
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
	protected final Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		toolkit = new FormToolkit(Display.getCurrent());
		createDialogContent(composite);
		if (!createNew) {
			loadObject(lockable);
		}
		return composite;
	}

	/**
	 * This callback allows us to validate the current input of the dialog.
	 * 
	 * @return true if the dialog content is valid otherwise false
	 */
	public abstract boolean validateInput();

	/**
	 * This callback will be invoked after the dialog has been created to
	 * initialize the controls with the values from the object.
	 */
	public abstract void loadObject(T lockable);

	/**
	 * Syncronizes the values from the dialog with the current edited or added
	 * object.
	 */
	public abstract void persistObject(T lockable);

	/**
	 * This callback allows us to register the needed listeners.
	 */
	protected void registerListeners() {
		// default do nothing
	}

	/**
	 * This callback allows us to remove the registered listeners.
	 */
	protected void removeListeners() {
		// default do nothing
	}

	/**
	 * Setup and initialize the dialog image and text
	 */
	public abstract void createDialogHeader();

	/**
	 * Setup and initialize the dialog content.
	 */
	public abstract void createDialogContent(Composite parent);

	// GETTERS AND SETTERS
	public boolean isCreateNew() {
		return createNew;
	}

	public T getObject() {
		return lockable;
	}
}
