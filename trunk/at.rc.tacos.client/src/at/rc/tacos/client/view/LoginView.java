package at.rc.tacos.client.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import at.rc.tacos.swtdesigner.*;

/**
 * GUI to login to the program
 * @author b.thek
 */


public class LoginView {

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
		shell.setSize(437, 272);
		shell.setText("Time And Coordination System");

		final Label bitteMeldenSieLabel_1 = new Label(shell, SWT.NONE);
		bitteMeldenSieLabel_1.setBackground(SWTResourceManager.getColor(255, 255, 255));
		bitteMeldenSieLabel_1.setText("Bitte melden Sie sich mit Ihrem Benutzernamen und Kennwort an.");
		bitteMeldenSieLabel_1.setBounds(92, 77, 327, 13);

		final Label anmeldungLabel = new Label(shell, SWT.NONE);
		anmeldungLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		anmeldungLabel.setFont(SWTResourceManager.getFont("", 20, SWT.BOLD));
		anmeldungLabel.setText("Anmeldung");
		anmeldungLabel.setBounds(92, 34, 225, 37);

		final Label label = new Label(shell, SWT.NONE);
		label.setImage(SWTResourceManager.getImage(LoginView.class, "image/tacos2.bmp"));
		label.setBounds(21, 17, 65, 73);

		final Label benutzernameLabel = new Label(shell, SWT.NONE);
		benutzernameLabel.setBounds(10, 134, 73, 13);
		benutzernameLabel.setText("Benutzername:");

		final CCombo combo = new CCombo(shell, SWT.BORDER);
		combo.setBounds(89, 130, 330, 17);

		final Label passwortLabel = new Label(shell, SWT.NONE);
		passwortLabel.setBounds(10, 159, 73, 13);
		passwortLabel.setText("Kennwort:");

		final Text text = new Text(shell, SWT.BORDER);
		text.setBounds(89, 156, 330, 17);
		text.setEchoChar('*');

		final Button abbrechenButton = new Button(shell, SWT.NONE);
		abbrechenButton.setImage(SWTResourceManager.getImage(LoginView.class, "image/LAN Warning.ico"));
		abbrechenButton.setBounds(323, 205, 96, 23);
		abbrechenButton.setText("Abbrechen");

		final Button okButton = new Button(shell, SWT.NONE);
		okButton.setBounds(221, 205, 96, 23);
		okButton.setText("OK");

		final Label bitteMeldenSieLabel = new Label(shell, SWT.NONE);
		bitteMeldenSieLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		bitteMeldenSieLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		bitteMeldenSieLabel.setFont(SWTResourceManager.getFont("Arial", 14, SWT.NONE));
		bitteMeldenSieLabel.setBounds(10, 10, 409, 94);
		//
	}

}

