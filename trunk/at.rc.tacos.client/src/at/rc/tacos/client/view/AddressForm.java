package at.rc.tacos.client.view;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import at.rc.tacos.model.RosterEntry;


/**
 * form to manage streets and cities of the transports
 * use with ManageAddressView
 * @author b.thek
 *
 */
public class AddressForm 
{

	protected Shell shell;
	private Text text;
	private Listener exitListener;
	private String headline;
	
	/** street or city*/
	private String type;
	private String value ="";

	/**
	 * use this constructor to edit an existent entry
	 * value: the street name or the city name
	 * type: street or city
	 */
	public AddressForm(String value, String type)
	{
		this.type = type;
		this.value = value;
		this.createContents();
		headline = " ‰ndern";
	}
	
	/**
	 * use this constructor to create a new entry
	 * value: the street name or the city name
	 * type: street or city
	 */
	public AddressForm(String type)
	{
		this.type = type;
		this.createContents();
		headline = " neu anlegen";
	}
	/**
	 * @return the text
	 */
	public Text getText() 
	{
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(Text text) 
	{
		this.text = text;
	}

	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try 
		{
			RosterEntry re = new RosterEntry();
			TimeForm window = new TimeForm(re,"Straﬂe");
			window.open();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * Open the window
	 */
	public void open() 
	{
		final Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) 
		{
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	/**
	 * Create contents of the window
	 */
	@SuppressWarnings("deprecation")
	protected void createContents() 
	{
		shell = new Shell();
		shell.setLayout(new FormLayout());
		shell.setSize(412, 200);
		System.out.println("type: " +type);
		shell.setText(type +headline);

		text = new Text(shell, SWT.BORDER);
		final FormData fd_text = new FormData();
		fd_text.right = new FormAttachment(0, 381);
		fd_text.left = new FormAttachment(0, 28);
		fd_text.bottom = new FormAttachment(0, 83);
		fd_text.top = new FormAttachment(0, 58);
		text.setLayoutData(fd_text);
		text.setText(value);
		
		
		
		//listener
		exitListener = new Listener() {
			public void handleEvent(Event e) 
			{
				MessageBox dialog = new MessageBox(shell, SWT.YES | SWT.NO | SWT.ICON_QUESTION);
				dialog.setText("Abbrechen");
				dialog.setMessage("Wollen Sie wirklich abbrechen?");
				if (e.type == SWT.Close) 
					e.doit = false;
				if (dialog.open() != SWT.YES) 
					return;
				shell.dispose();
			}
		};
		
		
		

		final Label Label = new Label(shell, SWT.NONE);
		final FormData fd_Label = new FormData();
		fd_Label.bottom = new FormAttachment(0, 78);
		fd_Label.top = new FormAttachment(0, 65);
		fd_Label.right = new FormAttachment(0, 139);
		fd_Label.left = new FormAttachment(0, 10);
		Label.setLayoutData(fd_Label);
		Label.setText("Zeitpunkt der :" +type);

		final Button okButton = new Button(shell, SWT.NONE);
		final FormData fd_okButton = new FormData();
		fd_okButton.top = new FormAttachment(0, 132);
		fd_okButton.bottom = new FormAttachment(0, 155);
		fd_okButton.left = new FormAttachment(0, 195);
		fd_okButton.right = new FormAttachment(0, 283);
		okButton.setLayoutData(fd_okButton);
		okButton.setText("OK");
		
		
		
		
		// Adding the controller
		okButton.addListener(SWT.Selection, new Listener()
		{
			
		        
				
			public void handleEvent(Event event) 
			{
				//TODO ab in die Datenbank mit dem value (text.getText())
//				UpdateRosterEntryAction action = new UpdateRosterEntryAction(re);
//				action.run();
				shell.dispose();
				
			}
			
		
			
			
			
			private void displayMessageBox(Event event, String fields, String message)
			{
				MessageBox mb = new MessageBox(shell, 0);
				mb.setText(message);
				mb.setMessage(fields);
				mb.open();
				if(event.type == SWT.Close) event.doit = false;
			}
		});

		final Button abbrechenButton = new Button(shell, SWT.NONE);
		final FormData fd_abbrechenButton = new FormData();
		fd_abbrechenButton.top = new FormAttachment(0, 131);
		fd_abbrechenButton.bottom = new FormAttachment(0, 154);
		fd_abbrechenButton.right = new FormAttachment(0, 379);
		fd_abbrechenButton.left = new FormAttachment(0, 291);
		abbrechenButton.setLayoutData(fd_abbrechenButton);
		abbrechenButton.setText("Abbrechen");
		abbrechenButton.addListener(SWT.Selection, exitListener);

		final Label label = new Label(shell, SWT.NONE);
		final FormData fd_hierLabel = new FormData();
		fd_hierLabel.top = new FormAttachment(0, 25);
		fd_hierLabel.left = new FormAttachment(0, 34);
		label.setLayoutData(fd_hierLabel);
		label.setText(type);
		//
	}

}
