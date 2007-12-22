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

import at.rc.tacos.client.controller.CreateRosterEntryAction;
import at.rc.tacos.client.controller.UpdateRosterEntryAction;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.StaffMember;

public class TimeForm 
{

	protected Shell shell;
	private Text text;
	private Listener exitListener;
	private RosterEntry re;
	private String type;

	public TimeForm(RosterEntry re, String type)
	{
		this.type = type;
		this.re = re;
		this.createContents();
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
			TimeForm window = new TimeForm(re,"Anmeldung");
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
		shell.setSize(263, 200);
		System.out.println("type: " +type);
		shell.setText(type);
		//shell.setImage(ImageFactory.getInstance().getRegisteredImage("application.logo.small"));

		text = new Text(shell, SWT.BORDER);
		final FormData fd_text = new FormData();
		fd_text.bottom = new FormAttachment(0, 83);
		fd_text.top = new FormAttachment(0, 58);
		fd_text.right = new FormAttachment(0, 232);
		fd_text.left = new FormAttachment(0, 144);
		text.setLayoutData(fd_text);
		
		//default time
		GregorianCalendar gcal = new GregorianCalendar();
		String time = (gcal.get(GregorianCalendar.HOUR_OF_DAY) <=9 ? "0" : "") +gcal.get(GregorianCalendar.HOUR_OF_DAY)+":" +((gcal.get(GregorianCalendar.MINUTE) <= 9 ? "0" : "") +gcal.get(GregorianCalendar.MINUTE));
		text.setText(time);
		text.selectAll();//mark the entry to overwrite easily

		
		
		//listener
		exitListener = new Listener() {
			public void handleEvent(Event e) 
			{
//				MessageBox dialog = new MessageBox(shell, SWT.YES | SWT.NO | SWT.ICON_QUESTION);
//				dialog.setText("Abbrechen");
//				dialog.setMessage("Wollen Sie wirklich abbrechen?");
//				if (e.type == SWT.Close) 
//					e.doit = false;
//				if (dialog.open() != SWT.YES) 
//					return;
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
		fd_okButton.bottom = new FormAttachment(0, 156);
		fd_okButton.top = new FormAttachment(0, 133);
		fd_okButton.right = new FormAttachment(0, 139);
		fd_okButton.left = new FormAttachment(0, 51);
		okButton.setLayoutData(fd_okButton);
		okButton.setText("OK");
		
		// Adding the controller
		okButton.addListener(SWT.Selection, new Listener()
		{
			String time;
			int hour;
			int minutes;
			String requiredFields;
			String formatTime;
			Calendar cal = Calendar.getInstance();
			long realWorkTime;

			public void handleEvent(Event event) 
			{
				requiredFields = "";
				hour = -1;
				minutes = -1;
				formatTime = "";
				
				//get field content
				time = text.getText();
	
				//check required Fields
				if(!this.checkRequiredFields().equalsIgnoreCase(""))
				{
					this.displayMessageBox(event, requiredFields, "Feld bitte ausfüllen");
					return;
				}

				//validating
				if(!this.checkFormatOfRealWorkTimeFields().equalsIgnoreCase(""))
				{
					this.displayMessageBox(event,formatTime, "Format von tatsächlicher Dienstzeit falsch: ");
					return;
				}

				this.transformToLong();//set planned work time
			
				if (type.equalsIgnoreCase("Anmeldung"))
					re.setRealStartOfWork(realWorkTime);
				
				if (type.equalsIgnoreCase("Abmeldung"))
					re.setRealEndOfWork(realWorkTime);
		        
				
				
				UpdateRosterEntryAction action = new UpdateRosterEntryAction(re);
				action.run();
				shell.dispose();
				
			}
			
			
			private String checkRequiredFields()
			{
				
				if (time.equalsIgnoreCase(""))
					requiredFields = requiredFields +" - Ortsstelle";

				return requiredFields;
			}
			
			
			private String checkFormatOfRealWorkTimeFields()
			{
				Pattern p4 = Pattern.compile("(\\d{2})(\\d{2})");//if content is e.g. 1234
				Pattern p5 = Pattern.compile("(\\d{2}):(\\d{2})");//if content is e.g. 12:34

				//check in
				if(!time.equalsIgnoreCase(""))
				{
					Matcher m41= p4.matcher(time);
					Matcher m51= p5.matcher(time);
					if(m41.matches())
					{
						hour = Integer.parseInt(m41.group(1));
						minutes = Integer.parseInt(m41.group(2));

						if(hour >= 0 && hour <=23 && minutes >= 0 && minutes <=59)
						{
							time = hour + ":" +minutes;//for the splitter
						}
						else
						{
							formatTime = " - Anmeldung (Zeit)";
						}
					}
					else if(m51.matches())
					{
						hour = Integer.parseInt(m51.group(1));
						minutes = Integer.parseInt(m51.group(2));

						if(!(hour >= 0 && hour <=23 && minutes >= 0 && minutes <=59))
						{
							formatTime = " - Anmeldung (Zeit)";
						}
					}
					else
					{
						formatTime = " - Anmeldung (Zeit)";
					}
				}

				
				return formatTime;
			}
			
			private void transformToLong()
			{
				//get a new instance of the calendar
				cal = Calendar.getInstance();
		
				//check in time
				String[] realTime = time.split(":");
				int hoursReal = Integer.valueOf(realTime[0]).intValue();
				int minutesReal = Integer.valueOf(realTime[1]).intValue();

				cal.set(Calendar.HOUR_OF_DAY, hoursReal);
				cal.set(Calendar.MINUTE,minutesReal);
				realWorkTime = cal.getTimeInMillis();
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
		fd_abbrechenButton.bottom = new FormAttachment(0, 156);
		fd_abbrechenButton.top = new FormAttachment(0, 133);
		fd_abbrechenButton.right = new FormAttachment(0, 232);
		fd_abbrechenButton.left = new FormAttachment(0, 144);
		abbrechenButton.setLayoutData(fd_abbrechenButton);
		abbrechenButton.setText("Abbrechen");
		//abbrechenButton.setImage(ImageFactory.getInstance().getRegisteredImage("icon.stop"));
		abbrechenButton.addListener(SWT.Selection, exitListener);
		//
	}

}
