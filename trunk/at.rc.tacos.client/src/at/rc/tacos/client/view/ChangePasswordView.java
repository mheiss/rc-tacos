package at.rc.tacos.client.view;

import org.eclipse.swt.SWT;

/**
 * GUI to change the password of the user
 * @author b.thek
 */

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import at.rc.tacos.swtdesigner.*;


public class ChangePasswordView {

	protected Shell shell;

	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			LoginView window = new LoginView();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window
	 */
	public void open() {
		final Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	/**
	 * Create contents of the window
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setImage(SWTResourceManager.getImage(LoginView.class, "image/Tacos_LoginSize.bmp"));
		shell.setSize(374, 317);
		shell.setText("Time And Coordination System");

		final Label benutzernameLabel = new Label(shell, SWT.NONE);
		benutzernameLabel.setBounds(22, 98, 108, 13);
		benutzernameLabel.setText("-- username setzen ---");

		final Label passwortLabel = new Label(shell, SWT.NONE);
		passwortLabel.setBounds(22, 135, 87, 13);
		passwortLabel.setText("Altes Kennwort:");

		final Text text = new Text(shell, SWT.BORDER);
		text.setBounds(140, 135, 174, 17);
		text.setEchoChar('*');

		final Button abbrechenButton = new Button(shell, SWT.NONE);
		abbrechenButton.setImage(SWTResourceManager.getImage(LoginView.class, "image/LAN Warning.ico"));
		abbrechenButton.setBounds(218, 250, 96, 23);
		abbrechenButton.setText("Abbrechen");

		final Button okButton = new Button(shell, SWT.NONE);
		okButton.setBounds(116, 250, 96, 23);
		okButton.setText("OK");

		final Label bitteMeldenSieLabel = new Label(shell, SWT.NONE);
		bitteMeldenSieLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		bitteMeldenSieLabel.setFont(SWTResourceManager.getFont("Arial", 14, SWT.NONE));
		bitteMeldenSieLabel.setText("Kennwort ändern");
		bitteMeldenSieLabel.setBounds(22, 30, 158, 23);

		final Label passwortLabel_1 = new Label(shell, SWT.NONE);
		passwortLabel_1.setBounds(22, 174, 87, 13);
		passwortLabel_1.setText("Neues Kennwort:");

		final Text text_1 = new Text(shell, SWT.BORDER);
		text_1.setBounds(140, 171, 174, 17);
		text_1.setEchoChar('*');

		final Label passwortLabel_1_1 = new Label(shell, SWT.NONE);
		passwortLabel_1_1.setBounds(22, 213, 108, 13);
		passwortLabel_1_1.setText("Kennwort bestätigen:");

		final Text text_1_1 = new Text(shell, SWT.BORDER);
		text_1_1.setBounds(140, 210, 174, 17);
		text_1_1.setEchoChar('*');
		//
	}

}
