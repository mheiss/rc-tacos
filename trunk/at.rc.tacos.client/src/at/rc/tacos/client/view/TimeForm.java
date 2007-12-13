package at.rc.tacos.client.view;

import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class TimeForm {

	protected Shell shell;
	private Text text;

	/**
	 * @return the text
	 */
	public Text getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(Text text) {
		this.text = text;
	}

	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TimeForm window = new TimeForm();
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
	@SuppressWarnings("deprecation")
	protected void createContents() {
		shell = new Shell();
		shell.setLayout(new FormLayout());
		shell.setSize(263, 200);
		shell.setText("An-/Abmeldung");

		text = new Text(shell, SWT.BORDER);
		final FormData fd_text = new FormData();
		fd_text.bottom = new FormAttachment(0, 83);
		fd_text.top = new FormAttachment(0, 58);
		fd_text.right = new FormAttachment(0, 232);
		fd_text.left = new FormAttachment(0, 144);
		text.setLayoutData(fd_text);
		
		//default time
		GregorianCalendar gcal = new GregorianCalendar();
		String time = gcal.get(GregorianCalendar.HOUR_OF_DAY)+ ":" +(gcal.get(GregorianCalendar.MINUTE));
		text.setText(time);
		text.selectAll();//mark the entry to overwrite easily

		
		

		final Label zeitpunktDerAbmeldungLabel = new Label(shell, SWT.NONE);
		final FormData fd_zeitpunktDerAbmeldungLabel = new FormData();
		fd_zeitpunktDerAbmeldungLabel.bottom = new FormAttachment(0, 78);
		fd_zeitpunktDerAbmeldungLabel.top = new FormAttachment(0, 65);
		fd_zeitpunktDerAbmeldungLabel.right = new FormAttachment(0, 139);
		fd_zeitpunktDerAbmeldungLabel.left = new FormAttachment(0, 10);
		zeitpunktDerAbmeldungLabel.setLayoutData(fd_zeitpunktDerAbmeldungLabel);
		zeitpunktDerAbmeldungLabel.setText("Zeitpunkt der Abmeldung:");

		final Button okButton = new Button(shell, SWT.NONE);
		final FormData fd_okButton = new FormData();
		fd_okButton.bottom = new FormAttachment(0, 156);
		fd_okButton.top = new FormAttachment(0, 133);
		fd_okButton.right = new FormAttachment(0, 139);
		fd_okButton.left = new FormAttachment(0, 51);
		okButton.setLayoutData(fd_okButton);
		okButton.setText("OK");

		final Button abbrechenButton = new Button(shell, SWT.NONE);
		final FormData fd_abbrechenButton = new FormData();
		fd_abbrechenButton.bottom = new FormAttachment(0, 156);
		fd_abbrechenButton.top = new FormAttachment(0, 133);
		fd_abbrechenButton.right = new FormAttachment(0, 232);
		fd_abbrechenButton.left = new FormAttachment(0, 144);
		abbrechenButton.setLayoutData(fd_abbrechenButton);
		abbrechenButton.setText("Abbrechen");
		//
	}

}
