package at.rc.tacos.client.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.util.Util;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.VehicleDetail;

/**
 * GUI (form) to manage an ambulance
 * @author b.thek
 */
public class VehicleForm  
{
	private Label aktuelleOrtsstelleLabel;
	private Combo combo;
	private Button einsatzbereitButton;
	private Button button;
	private Text rosterTimeDriver;
	private Text rosterTimeParamedicI;
	private Text rosterTimeParamedicII;
	private Text textAnmerkungen;
	private Combo setDriverCombo;
	private Combo setParamedicICombo;
	private Combo setParamedicIICombo;
	private Combo setVehicleCombo;
	private Combo setMobilePhoneCombo;
	private Button abbrechenButton;
	private Button okButton;
	private Group groupCarDetails;
	private Group staffGroup;
	protected Shell shell;
	private Color inactiveBackgroundColor = Util.getColor(245, 245, 245);
	private Listener exitListener;
	
	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try {
				VehicleForm window = new VehicleForm();
				window.open();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * used to edit an vehicleDetail entry
	 * @param vehicleDetail the roster entry to edit
	 */
	public VehicleForm(VehicleDetail vehicleDetail)
	{
		this.createContents();
		
		//TODO - get date if this object is used to edit an existing (has staff) car
	}
	
	public VehicleForm()
	{
		this.createContents();
	}

	/**
	 * Open the window
	 */
	public void open() {
		final Display display = Display.getDefault();
//		createContents();
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
	protected void createContents() 
	{
		//TODO: get a list of StaffMembers from the database
		//get data
		StaffMember sm1 = new StaffMember("Helmut", "Maier", "h.maie");
		StaffMember sm2 = new StaffMember("Daniel", "Haberl", "d.habe");

		//staff member list  with all staff members checked in as "Fahrer"
		ArrayList<StaffMember> staffMemberListDriver = new ArrayList<StaffMember>(Arrays.asList(sm1,sm2));

		//staff member list with all! staff members checked in
		ArrayList<StaffMember> staffMemberListAllCheckedIn = new ArrayList<StaffMember>(Arrays.asList(sm1,sm2));
		
		//vehicleDetail list with all vehicles which are not out of order
		List<VehicleDetail> vehicleList = ModelFactory.getInstance().getVehicleManager().getVehicleList();
		
		
		//mobilephone list with all mobile phones
		MobilePhoneDetail m1 = new MobilePhoneDetail("BM05","0664/1234567");
		MobilePhoneDetail m2 = new MobilePhoneDetail("KA03","0664/8912345");
		ArrayList<MobilePhoneDetail> mobphoneList = new ArrayList<MobilePhoneDetail>(Arrays.asList(m1,m2));
		
		

		//listener
		exitListener = new Listener() 
		{
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

		//GUI
		shell = new Shell();
		//shell.setImage(ImageFactory.getInstance().getRegisteredImage("application.logo.small"));
		shell.setSize(500, 390);
		shell.setText("Fahrzeugdetails");
		
		
		groupCarDetails = new Group(shell, SWT.NONE);
		groupCarDetails.setText("Fahrzeugdetails");
		groupCarDetails.setBounds(10,10,469,78);
		

		staffGroup = new Group(shell, SWT.NONE);
		staffGroup.setText("Besatzung");
		staffGroup.setBounds(10,92,469,210);

		
		//driver
		final Label fahrerLabel = new Label(staffGroup, SWT.NONE);
		fahrerLabel.setBounds(10,20,55, 13);
		fahrerLabel.setText("Fahrer:");

		setDriverCombo = new Combo(staffGroup, SWT.READ_ONLY);
		final ComboViewer comboViewerDriver = new ComboViewer(setDriverCombo);

		//fill combo employee name with data
		for(StaffMember staffMember: staffMemberListDriver)
		{
			comboViewerDriver.add(staffMember.getLastName() +" " +staffMember.getFirstName());
		}

		setDriverCombo.setBounds(100,15,226, 24);
		setDriverCombo.setFont(new Font(null,"Arial", 10, SWT.BOLD));
		
		rosterTimeDriver = new Text(staffGroup, SWT.NONE);
		rosterTimeDriver.setBounds(340,15,100,21);
		rosterTimeDriver.setBackground(inactiveBackgroundColor);
		rosterTimeDriver.setEditable(false);

		
		//paramedic I
		final Label saniILabel = new Label(staffGroup, SWT.NONE);
		saniILabel.setBounds(10,47,55, 13);
		saniILabel.setText("Sanit‰ter:");
		
		setParamedicICombo = new Combo(staffGroup, SWT.READ_ONLY);
		final ComboViewer comboViewerParamedicI = new ComboViewer(setParamedicICombo);

		for(StaffMember staffMember: staffMemberListAllCheckedIn)
		{
			comboViewerParamedicI.add(staffMember.getLastName() +" " +staffMember.getFirstName());
		}

		setParamedicICombo.setBounds(100,42,226,24);
		setParamedicICombo.setFont(new Font(null,"Arial", 10, SWT.BOLD));

		rosterTimeParamedicI = new Text(staffGroup, SWT.NONE);
		rosterTimeParamedicI.setBounds(340,42,100,21);
		rosterTimeParamedicI.setBackground(inactiveBackgroundColor);
		rosterTimeParamedicI.setEditable(false);
		
		
		
		
		//paramedic II
		final Label saniIILabel = new Label(staffGroup, SWT.NONE);
		saniIILabel.setBounds(10,74,55, 13);
		saniIILabel.setText("Sanit‰ter:");
		
		setParamedicIICombo = new Combo(staffGroup, SWT.READ_ONLY);
		final ComboViewer comboViewerParamedicII = new ComboViewer(setParamedicIICombo);

		for(StaffMember staffMember: staffMemberListAllCheckedIn)
		{
			comboViewerParamedicII.add(staffMember.getLastName() +" " +staffMember.getFirstName());
		}

		setParamedicIICombo.setBounds(100,69,226, 24);
		setParamedicIICombo.setFont(new Font(null,"Arial", 10, SWT.BOLD));
		
		rosterTimeParamedicII = new Text(staffGroup, SWT.NONE);
		rosterTimeParamedicII.setBounds(340,69,100,21);
		rosterTimeParamedicII.setText("12:30 - 18:00");
		rosterTimeParamedicII.setBackground(inactiveBackgroundColor);
		rosterTimeParamedicII.setEditable(false);
		
		
		//notes
		final Label notesLabel = new Label(staffGroup, SWT.NONE);
		notesLabel.setBounds(10,101,71,13);
		notesLabel.setText("Anmerkungen:");
		
		textAnmerkungen = new Text(staffGroup, SWT.BORDER);
		textAnmerkungen.setBounds(100,96, 226, 100);
		
		staffGroup.setTabList(new Control[] {setDriverCombo, setParamedicICombo,setParamedicIICombo, textAnmerkungen});


		//vehicle
		final Label vehicleLabel = new Label(groupCarDetails, SWT.NONE);
		vehicleLabel.setBounds(10,20,55, 13);
		vehicleLabel.setText("Fahrzeug:");
		
		setVehicleCombo = new Combo(groupCarDetails, SWT.READ_ONLY);
		final ComboViewer comboViewerVehicle = new ComboViewer(setVehicleCombo);

		for(VehicleDetail vehicleDetail: vehicleList)
		{
			comboViewerVehicle.add(vehicleDetail.getVehicleName());
		}

		setVehicleCombo.setBounds(100,15,100, 24);
		setVehicleCombo.setFont(new Font(null,"Arial", 10, SWT.BOLD));
		
		
		
		//mobile phone
		final Label mobphoneLabel = new Label(groupCarDetails, SWT.NONE);
		mobphoneLabel.setBounds(10,47,55, 13);
		mobphoneLabel.setText("Handy:");
		
		setMobilePhoneCombo = new Combo(groupCarDetails, SWT.READ_ONLY);
		final ComboViewer comboViewerMobPhone = new ComboViewer(setMobilePhoneCombo);

		for(MobilePhoneDetail mobilePhone : mobphoneList)
		{
			comboViewerMobPhone.add(mobilePhone.getMobilePhoneId());
		}

		setMobilePhoneCombo.setBounds(100,42,100, 24);
		setMobilePhoneCombo.setFont(new Font(null,"Arial", 10, SWT.BOLD));

		button = new Button(groupCarDetails, SWT.CHECK);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
			}
		});
		button.setText("Auﬂer Dienst");
		button.setBounds(235, 20, 85, 16);

		einsatzbereitButton = new Button(groupCarDetails, SWT.CHECK);
		einsatzbereitButton.setText("Einsatzbereit");
		einsatzbereitButton.setBounds(235, 45, 85, 16);

		combo = new Combo(groupCarDetails, SWT.NONE);
		combo.setToolTipText("Ist das Fahrzeug einer anderen Dienststelle zugeordnet, kann dies hier ausgew‰hlt werden.");
		combo.setBounds(333, 44, 126, 21);

		aktuelleOrtsstelleLabel = new Label(groupCarDetails, SWT.NONE);
		aktuelleOrtsstelleLabel.setText("Aktuelle Ortsstelle:");
		aktuelleOrtsstelleLabel.setBounds(333, 20, 126, 13);
				
		
		groupCarDetails.setTabList(new Control[] {setVehicleCombo, setMobilePhoneCombo, button, einsatzbereitButton, combo, aktuelleOrtsstelleLabel});

		
		//Buttons
		abbrechenButton = new Button(shell, SWT.NONE);
		//abbrechenButton.setImage(ImageFactory.getInstance().getRegisteredImage("icon.stop"));
		abbrechenButton.setBounds(383, 315, 96, 23);
		abbrechenButton.setText("Abbrechen");
		abbrechenButton.addListener(SWT.Selection, exitListener);

		okButton = new Button(shell, SWT.NONE);
		okButton.setBounds(281, 315, 96, 23);
		okButton.setText("OK");

		//this.setRealWorktimesInactive();


		// Adding the controller
		okButton.addListener(SWT.Selection, new Listener()
		{		
			public void handleEvent(Event event) 
			{

			}
			
//			public void displayMessageBox(Event event, String fields, String message)
//			{
//				 MessageBox mb = new MessageBox(shell, 0);
//			     mb.setText(message);
//			     mb.setMessage(fields);
//			     mb.open();
//			     if(event.type == SWT.Close) event.doit = false;
//			}
		});
		shell.setTabList(new Control[] {groupCarDetails,staffGroup, okButton, abbrechenButton});
	}
}

