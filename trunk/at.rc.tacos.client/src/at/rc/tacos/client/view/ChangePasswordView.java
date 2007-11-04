package at.rc.tacos.client.view;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import at.rc.tacos.swtdesigner.SWTResourceManager;

/**
 * GUI to change the password if desired
 * @author b.thek
 */

public class ChangePasswordView {

	private Button abbrechenButton;
	private Button okButton;
	private Text textKennwortBestaetigen;
	private Text textNeuesKennwort;
	private Text textAlteskennwort;
	private Composite composite;
	protected Shell shell;

	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ChangePasswordView window = new ChangePasswordView();
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
		shell.setLayout(new FormLayout());
		shell.setImage(SWTResourceManager.getImage(ChangePasswordView.class, "/image/Tacos_LOGO.jpg"));
		shell.setSize(378, 319);
		shell.setText("Kennwort ändern");

		composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FormLayout());
		final FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(0, 90);
		fd_composite.top = new FormAttachment(0, 0);
		fd_composite.right = new FormAttachment(0, 370);
		fd_composite.left = new FormAttachment(0, 0);
		composite.setLayoutData(fd_composite);
		composite.setBackground(SWTResourceManager.getColor(255, 255, 255));

		final Label label = new Label(composite, SWT.NONE);
		final FormData fd_label = new FormData();
		fd_label.bottom = new FormAttachment(0, 52);
		fd_label.top = new FormAttachment(0, 10);
		fd_label.right = new FormAttachment(0, 354);
		fd_label.left = new FormAttachment(0, 80);
		label.setLayoutData(fd_label);
		label.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label.setFont(SWTResourceManager.getFont("Arial", 20, SWT.BOLD));
		label.setText("Kennwort ändern");

		final Label bitteGebenSieLabel = new Label(composite, SWT.NONE);
		final FormData fd_bitteGebenSieLabel = new FormData();
		fd_bitteGebenSieLabel.bottom = new FormAttachment(0, 71);
		fd_bitteGebenSieLabel.top = new FormAttachment(0, 58);
		fd_bitteGebenSieLabel.right = new FormAttachment(0, 354);
		fd_bitteGebenSieLabel.left = new FormAttachment(0, 80);
		bitteGebenSieLabel.setLayoutData(fd_bitteGebenSieLabel);
		bitteGebenSieLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		bitteGebenSieLabel.setText("Bitte geben Sie das alte und das neue Kennwort ein.");

		final Label label_1 = new Label(composite, SWT.NONE);
		final FormData fd_label_1 = new FormData();
		fd_label_1.bottom = new FormAttachment(0, 71);
		fd_label_1.top = new FormAttachment(0, 10);
		fd_label_1.right = new FormAttachment(0, 74);
		fd_label_1.left = new FormAttachment(0, 10);
		label_1.setLayoutData(fd_label_1);
		label_1.setImage(SWTResourceManager.getImage(ChangePasswordView.class, "/image/Tacos_LOGO_kleinF.bmp"));
		label_1.setBackground(SWTResourceManager.getColor(255, 255, 255));

		final Label benutzernameLabel = new Label(shell, SWT.NONE);
		benutzernameLabel.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
		final FormData fd_benutzernameLabel = new FormData();
		fd_benutzernameLabel.bottom = new FormAttachment(0, 110);
		fd_benutzernameLabel.top = new FormAttachment(composite, 5, SWT.BOTTOM);
		benutzernameLabel.setLayoutData(fd_benutzernameLabel);
		benutzernameLabel.setText("Benutzername");

		textKennwortBestaetigen = new Text(shell, SWT.BORDER);
		final FormData fd_textKennwortBestaetigen = new FormData();
		fd_textKennwortBestaetigen.bottom = new FormAttachment(0, 215);
		fd_textKennwortBestaetigen.top = new FormAttachment(0, 194);
		fd_textKennwortBestaetigen.right = new FormAttachment(0, 320);
		fd_textKennwortBestaetigen.left = new FormAttachment(0, 129);
		textKennwortBestaetigen.setLayoutData(fd_textKennwortBestaetigen);

		Label altesKennwortLabel;
		altesKennwortLabel = new Label(shell, SWT.NONE);
		final FormData fd_altesKennwortLabel = new FormData();
		altesKennwortLabel.setLayoutData(fd_altesKennwortLabel);
		altesKennwortLabel.setText("Altes Kennwort:");

		Label neuesKennwortLabel;
		neuesKennwortLabel = new Label(shell, SWT.NONE);
		fd_altesKennwortLabel.right = new FormAttachment(neuesKennwortLabel, 104, SWT.LEFT);
		fd_altesKennwortLabel.left = new FormAttachment(neuesKennwortLabel, 0, SWT.LEFT);
		final FormData fd_neuesKennwortLabel = new FormData();
		fd_neuesKennwortLabel.bottom = new FormAttachment(0, 179);
		fd_neuesKennwortLabel.top = new FormAttachment(0, 166);
		fd_neuesKennwortLabel.right = new FormAttachment(0, 123);
		fd_neuesKennwortLabel.left = new FormAttachment(0, 19);
		neuesKennwortLabel.setLayoutData(fd_neuesKennwortLabel);
		neuesKennwortLabel.setText("Neues Kennwort:");

		final Label label_2 = new Label(shell, SWT.NONE);
		final FormData fd_label_2 = new FormData();
		fd_label_2.bottom = new FormAttachment(0, 213);
		fd_label_2.top = new FormAttachment(0, 200);
		fd_label_2.right = new FormAttachment(neuesKennwortLabel, 104, SWT.LEFT);
		fd_label_2.left = new FormAttachment(neuesKennwortLabel, 0, SWT.LEFT);
		label_2.setLayoutData(fd_label_2);
		label_2.setText("Kennwort bestätigen:");

		abbrechenButton = new Button(shell, SWT.NONE);
		final FormData fd_abbrechenButton = new FormData();
		fd_abbrechenButton.bottom = new FormAttachment(0, 276);
		fd_abbrechenButton.top = new FormAttachment(0, 253);
		fd_abbrechenButton.right = new FormAttachment(0, 361);
		fd_abbrechenButton.left = new FormAttachment(0, 248);
		abbrechenButton.setLayoutData(fd_abbrechenButton);
		abbrechenButton.setImage(SWTResourceManager.getImage(ChangePasswordView.class, "/image/LAN Warning.ico"));
		abbrechenButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
			}
		});
		abbrechenButton.setText("Abbrechen");

		okButton = new Button(shell, SWT.NONE);
		final FormData fd_okButton = new FormData();
		fd_okButton.bottom = new FormAttachment(0, 276);
		fd_okButton.top = new FormAttachment(0, 253);
		fd_okButton.right = new FormAttachment(0, 242);
		fd_okButton.left = new FormAttachment(0, 129);
		okButton.setLayoutData(fd_okButton);
		okButton.setText("OK");

		textNeuesKennwort = new Text(shell, SWT.BORDER);
		final FormData fd_textNeuesKennwort = new FormData();
		fd_textNeuesKennwort.bottom = new FormAttachment(0, 181);
		fd_textNeuesKennwort.top = new FormAttachment(0, 160);
		fd_textNeuesKennwort.right = new FormAttachment(neuesKennwortLabel, 196, SWT.RIGHT);
		fd_textNeuesKennwort.left = new FormAttachment(neuesKennwortLabel, 5, SWT.RIGHT);
		textNeuesKennwort.setLayoutData(fd_textNeuesKennwort);

		textAlteskennwort = new Text(shell, SWT.BORDER);
		fd_benutzernameLabel.right = new FormAttachment(textAlteskennwort, 0, SWT.RIGHT);
		fd_benutzernameLabel.left = new FormAttachment(textAlteskennwort, 0, SWT.LEFT);
		fd_altesKennwortLabel.top = new FormAttachment(textAlteskennwort, -13, SWT.BOTTOM);
		fd_altesKennwortLabel.bottom = new FormAttachment(textAlteskennwort, 0, SWT.BOTTOM);
		final FormData fd_textAlteskennwort = new FormData();
		fd_textAlteskennwort.bottom = new FormAttachment(0, 146);
		fd_textAlteskennwort.top = new FormAttachment(0, 125);
		fd_textAlteskennwort.right = new FormAttachment(altesKennwortLabel, 196, SWT.RIGHT);
		fd_textAlteskennwort.left = new FormAttachment(altesKennwortLabel, 5, SWT.RIGHT);
		textAlteskennwort.setLayoutData(fd_textAlteskennwort);
		shell.setTabList(new Control[] {composite, textAlteskennwort, textNeuesKennwort, textKennwortBestaetigen, okButton, abbrechenButton});
		//
	}

}
