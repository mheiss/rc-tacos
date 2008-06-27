package at.rc.tacos.client.view;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import at.rc.tacos.client.modelManager.LockManager;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.modelManager.TransportManager;
import at.rc.tacos.client.providers.MobilePhoneContentProvider;
import at.rc.tacos.client.providers.MobilePhoneLabelProvider;
import at.rc.tacos.client.providers.StaffMemberVehicleContentProvider;
import at.rc.tacos.client.providers.StaffMemberVehicleLabelProvider;
import at.rc.tacos.client.providers.StationContentProvider;
import at.rc.tacos.client.providers.StationLabelProvider;
import at.rc.tacos.client.providers.VehicleContentProvider;
import at.rc.tacos.client.providers.VehicleLabelProvider;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.VehicleDetail;
import at.rc.tacos.model.Transport;

/**
 * The gui to manage a vehicle
 * @author Michael
 */
public class VehicleForm extends TitleAreaDialog
{
	//properties
	private FormToolkit toolkit;
	private ComboViewer vehicleComboViewer;
	private ComboViewer mobilePhoneComboViewer;
	private Button readyButton;
	private Button outOfOrder;
	private ComboViewer stationComboViewer;
	private ComboViewer driverComboViewer;
	private ComboViewer medic1ComboViewer;
	private ComboViewer medic2ComboViewer;
	private TextViewer noteEditor;  

	//the vehicle
	private VehicleDetail vehicleDetail;
	
	private List<Transport> transportList = new ArrayList<Transport>();

	// description text
	public final static String FORM_DESCRIPTION = "Hier kˆnnen Sie Fahrzeug und dessen Besatzung verwalten";
	
	//the transport manager
	TransportManager transportManager = ModelFactory.getInstance().getTransportManager();

	/**
	 * Default class constructor for the vehicle form
	 * @param parentShell the parent shell
	 */
	public VehicleForm(Shell parentShell)
	{
		super(parentShell);
	}

	/**
	 * Default class constructor for the vehicle form to edit a vehicle
	 * @param parentShell the parent shell
	 */
	public VehicleForm(Shell parentShell,VehicleDetail vehicle)
	{
		super(parentShell);
		this.vehicleDetail = vehicle;
	}

	/**
	 * Creates the dialog's contents
	 * @param parent the parent composite
	 * @return Control
	 */
	@Override
	protected Control createContents(Composite parent) 
	{
		Control contents = super.createContents(parent);
		setTitle("Fahrzeugverwaltung");
		setMessage(FORM_DESCRIPTION, IMessageProvider.INFORMATION);
		setTitleImage(ImageFactory.getInstance().getRegisteredImage("application.logo"));
		return contents;
	}

	/**
	 * Create contents of the window
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		//setup the composite
		Composite composite = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 30;
		layout.verticalSpacing = 10;
		composite.setLayout(layout);
		composite.setBackground(CustomColors.SECTION_BACKGROUND);
		toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));

		//create the sections
		createDetailSection(composite);
		createStatusSection(composite);
		createCrewSection(composite);
		createNotesSection(composite);

		//init if we have a vehicle
		if(vehicleDetail != null)
		{
			vehicleComboViewer.setSelection(new StructuredSelection(vehicleDetail));
			mobilePhoneComboViewer.setSelection(new StructuredSelection(vehicleDetail.getMobilePhone()));
			stationComboViewer.setSelection(new StructuredSelection(vehicleDetail.getCurrentStation()));
			if(vehicleDetail.getDriver() != null)
				driverComboViewer.setSelection(new StructuredSelection(vehicleDetail.getDriver()));
			if(vehicleDetail.getFirstParamedic() != null)
				medic1ComboViewer.setSelection(new StructuredSelection(vehicleDetail.getFirstParamedic()));
			if(vehicleDetail.getSecondParamedic() != null)
				medic2ComboViewer.setSelection(new StructuredSelection(vehicleDetail.getSecondParamedic()));
			readyButton.setSelection(vehicleDetail.isReadyForAction());
			outOfOrder.setSelection(vehicleDetail.isOutOfOrder());
			noteEditor.getDocument().set(vehicleDetail.getVehicleNotes());
		}
		checkRequiredFields();
		return composite;
	}

	
	@Override
	public boolean close()
	{
		//remove the lock from the object
		LockManager.removeLock(VehicleDetail.ID, vehicleDetail.getVehicleName());
		return super.close();
	}
	
	/**
	 * The user pressed the cancel button
	 */
	@Override
	protected void cancelPressed()
	{
		MessageBox dialog = new MessageBox(getShell(), SWT.YES | SWT.NO | SWT.ICON_QUESTION);
		dialog.setText("Abbrechen");
		dialog.setMessage("Wollen Sie wirklich abbrechen?");
		//check the result
		if (dialog.open() != SWT.NO)
		{
			//remove the lock from the object
			LockManager.removeLock(VehicleDetail.ID, vehicleDetail.getVehicleName());
			getShell().close();
		}
	}

	/**
	 * The user pressed the ok button
	 */
	@Override
	protected void okPressed()
	{
		//driver
		int index = driverComboViewer.getCombo().getSelectionIndex();
		vehicleDetail.setDriver((StaffMember)driverComboViewer.getElementAt(index));
		//medic
		index = medic1ComboViewer.getCombo().getSelectionIndex();
		vehicleDetail.setFirstParamedic((StaffMember)medic1ComboViewer.getElementAt(index));
		//medic1
		index = medic2ComboViewer.getCombo().getSelectionIndex();
		vehicleDetail.setSecondParamedic((StaffMember)medic2ComboViewer.getElementAt(index));
		//notes
		vehicleDetail.setVehicleNotes(noteEditor.getTextWidget().getText());
		//status
		//default
		vehicleDetail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_GREEN);
		
		vehicleDetail.setOutOfOrder(outOfOrder.getSelection());
		if(vehicleDetail.isOutOfOrder())
			vehicleDetail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_NA);
		vehicleDetail.setReadyForAction(readyButton.getSelection());
		
		//handle the blue status
		//if the vehicle was(!) out of order -> set the vehicle image to green
		if(vehicleDetail.isReadyForAction() &! vehicleDetail.isOutOfOrder())
		{
			if(vehicleDetail.getLastDestinationFree() != null)
			{
				if(!vehicleDetail.getLastDestinationFree().equalsIgnoreCase(""))
				{
					vehicleDetail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_BLUE);
				}
			}
			else
			{
				vehicleDetail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_GREEN);
			}
			
		}
		else
			vehicleDetail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_NA);
		
		//phone
		index = mobilePhoneComboViewer.getCombo().getSelectionIndex();
		vehicleDetail.setMobilPhone((MobilePhoneDetail)mobilePhoneComboViewer.getElementAt(index));
		//station
		index = stationComboViewer.getCombo().getSelectionIndex();
		vehicleDetail.setCurrentStation((Location)stationComboViewer.getElementAt(index));

		//check the status of the vehicle (red,yellow, green)
		if(driverComboViewer.getCombo().getSelectionIndex() == -1 &&
				medic1ComboViewer.getCombo().getSelectionIndex() == -1 &&
				medic2ComboViewer.getCombo().getSelectionIndex() == -1)
			vehicleDetail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_NA);
		
		if(!readyButton.getSelection())
			vehicleDetail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_NA);
		//Send the update message
		NetWrapper.getDefault().sendUpdateMessage(VehicleDetail.ID, vehicleDetail);
		getShell().close();
		
		//overwrite the vehicle details of all outstanding transports
		transportList = transportManager.getUnderwayTransportsByVehicle(vehicleDetail.getVehicleName());
		for(Transport transport: transportList)
		{
			transport.setVehicleDetail(vehicleDetail);
			NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
		}
		
		LockManager.removeLock(VehicleDetail.ID, vehicleDetail.getVehicleName());
	}

	/**
	 * Creates the detail section for the vehicle
	 * @parent the parent composite
	 */
	private void createDetailSection(Composite parent)
	{
		Group group = new Group(parent,SWT.NONE);
		group.setText("Fahrzeugdetails");
		group.setLayout(new GridLayout());
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setBackground(CustomColors.SECTION_BACKGROUND);

		Composite client = new Composite(group, SWT.NONE);
		client.setBackground(CustomColors.SECTION_BACKGROUND);
		//layout
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		client.setLayout(gridLayout);
		GridData clientDataLayout = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL);
		client.setLayoutData(clientDataLayout);

		final Label labelCar = new Label(client, SWT.NONE);
		labelCar.setText("Fahrzeug:");
		labelCar.setBackground(CustomColors.SECTION_BACKGROUND);

		Combo vehicleCombo = new Combo(client, SWT.READ_ONLY);
		vehicleCombo.setEnabled(false);
		vehicleComboViewer = new ComboViewer(vehicleCombo);
		vehicleComboViewer.setContentProvider(new VehicleContentProvider());
		vehicleComboViewer.setLabelProvider(new VehicleLabelProvider());
		vehicleComboViewer.setInput(ModelFactory.getInstance().getVehicleManager());

		//Mobile Phone
		final Label labelPhone = new Label(client, SWT.NONE);
		labelPhone.setText("Handy :");
		labelPhone.setBackground(CustomColors.SECTION_BACKGROUND);

		Combo mobilePhoneCombo = new Combo(client, SWT.READ_ONLY);
		mobilePhoneComboViewer = new ComboViewer(mobilePhoneCombo);
		mobilePhoneComboViewer.setContentProvider(new MobilePhoneContentProvider());
		mobilePhoneComboViewer.setLabelProvider(new MobilePhoneLabelProvider());
		mobilePhoneComboViewer.setInput(ModelFactory.getInstance().getPhoneManager());

		//Station
		final Label labelStation = new Label(client, SWT.NONE);
		labelStation.setText("Aktuelle Ortsstelle :");
		labelStation.setBackground(CustomColors.SECTION_BACKGROUND);

		Combo stationCombo = new Combo(client, SWT.READ_ONLY);
		stationCombo.setToolTipText("Ist das Fahrzeug einer anderen Dienststelle zugeordnet, kann dies hier ausgew‰hlt werden.");
		stationComboViewer = new ComboViewer(stationCombo);
		stationComboViewer.setContentProvider(new StationContentProvider());
		stationComboViewer.setLabelProvider(new StationLabelProvider());
		stationComboViewer.setInput(ModelFactory.getInstance().getLocationManager());
		stationComboViewer.getCombo().addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				int index = stationComboViewer.getCombo().getSelectionIndex();
				Location location = (Location)stationComboViewer.getElementAt(index);
				//update the crew based on the current location
				vehicleDetail.setCurrentStation(location);
				driverComboViewer.setContentProvider(new StaffMemberVehicleContentProvider(vehicleDetail));
				medic1ComboViewer.setContentProvider(new StaffMemberVehicleContentProvider(vehicleDetail));
				medic2ComboViewer.setContentProvider(new StaffMemberVehicleContentProvider(vehicleDetail));	
			}
		});

		//layout for the labels
		GridData data = new GridData();
		data.widthHint = 115;
		labelCar.setLayoutData(data);
		data = new GridData();
		data.widthHint = 115;
		labelPhone.setLayoutData(data);
		data = new GridData();
		data.widthHint = 115;
		labelStation.setLayoutData(data);
		//layout for the text fields
		GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
		data2.widthHint= 200;
		vehicleCombo.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		data2.widthHint= 200;
		mobilePhoneCombo.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		data2.widthHint= 200;
		stationCombo.setLayoutData(data2);
	}

	/**
	 * Creates the status section for the vehicle
	 * @param parent the parent composite
	 */
	private void createStatusSection(Composite parent)
	{
		//create the section
		Group group = new Group(parent,SWT.NONE);
		group.setText("Einsatzstatus");
		group.setLayout(new GridLayout());
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setBackground(CustomColors.SECTION_BACKGROUND);

		//composite to add the client area
		Composite client = new Composite(group, SWT.NONE);
		client.setBackground(CustomColors.SECTION_BACKGROUND);

		//layout
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.horizontalSpacing = 15;
		layout.makeColumnsEqualWidth = false;
		client.setLayout(layout);
		GridData clientDataLayout = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL);
		client.setLayoutData(clientDataLayout);

		//Selection listener for the combos
		SelectionListener listener = new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent se)
			{
				//Ready for action status and out of order is activated
				if(readyButton.getSelection() && outOfOrder.getSelection())
				{
					getShell().getDisplay().beep();
					setErrorMessage("Ein Fahrzeug kann nicht als AuﬂerDienst gestellt werden werden wenn es noch Einsatzbereit ist.");
					outOfOrder.setSelection(false);
					return;
				}
				if(outOfOrder.getSelection())
				{
					setMessage("Das Fahrzeug kann keinem Transport zugeordnet werden da es auﬂer Dienst ist.",IMessageProvider.WARNING);
					return;
				}
				else
					checkRequiredFields();	
			}
		};

		//Ready for action
		readyButton = new Button(client, SWT.CHECK);
		readyButton.setBackground(CustomColors.SECTION_BACKGROUND);
		readyButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent se)
			{
				//Ready for action status and out of order is activated
				if(readyButton.getSelection() && outOfOrder.getSelection())
				{
					getShell().getDisplay().beep();
					setErrorMessage("Ein Fahrzeug kann nicht als Einsatzbereit markiert werden wenn es noch Auﬂer Dienst gestellt ist");
					readyButton.setSelection(false);
					return;
				}
				if(!readyButton.getSelection())
				{
					getShell().getDisplay().beep();
					setMessage("Das Fahrzeug kann keinem Transport zugeordnet werden da es nicht Einsatzbereit ist",IMessageProvider.WARNING);
					return;
				}
				else
					checkRequiredFields();	
			}
		});
		readyButton.setText("Einsatzbereit");

		//Out of Order
		outOfOrder = new Button(client, SWT.CHECK); 
		outOfOrder.setBackground(CustomColors.SECTION_BACKGROUND);
		outOfOrder.addSelectionListener(listener);
		outOfOrder.setText("Auﬂer Dienst");
	}

	/**
	 * Creates the crew section for the vehicle
	 * @parent the parent composite
	 */
	private void createCrewSection(Composite parent)
	{
		//create the section
		Group group = new Group(parent,SWT.NONE);
		group.setText("Fahrzeugbesatzung");
		group.setLayout(new GridLayout());
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setBackground(CustomColors.SECTION_BACKGROUND);

		//composite to add the client area
		Composite client = new Composite(group, SWT.NONE);
		client.setBackground(CustomColors.SECTION_BACKGROUND);

		//layout
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.horizontalSpacing = 15;
		layout.makeColumnsEqualWidth = false;
		client.setLayout(layout);
		GridData clientDataLayout = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL);
		client.setLayoutData(clientDataLayout);

		//driver
		final Label labelDriver = new Label(client, SWT.NONE);
		labelDriver.setText("Fahrer :");
		labelDriver.setBackground(CustomColors.SECTION_BACKGROUND);

		//create composite for the combo and the image
		Composite comp = makeComposite(client, 2);
		comp.setBackground(CustomColors.SECTION_BACKGROUND);

		Combo driverCombo = new Combo(comp, SWT.READ_ONLY);
		driverComboViewer = new ComboViewer(driverCombo);
		driverComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) 
			{
				checkRequiredFields();
			}
		});
		driverComboViewer.setContentProvider(new StaffMemberVehicleContentProvider(vehicleDetail));
		driverComboViewer.setLabelProvider(new StaffMemberVehicleLabelProvider());
		driverComboViewer.setInput(ModelFactory.getInstance().getStaffManager().getUnassignedStaffList());

		//create the hyperlink
		ImageHyperlink removeDriver = toolkit.createImageHyperlink(comp, SWT.NONE);
		removeDriver.setBackground(CustomColors.SECTION_BACKGROUND);
		removeDriver.setToolTipText("Zieht den aktuell zugewiesenen Fahrer vom Fahrzeug ab");
		removeDriver.setImage(ImageFactory.getInstance().getRegisteredImage("admin.remove"));
		removeDriver.addHyperlinkListener(new HyperlinkAdapter()
		{
			@Override
			public void linkActivated(HyperlinkEvent e) 
			{
				driverComboViewer.setSelection(null);	
			}
		});

		//medic1
		final Label labelMedic1 = new Label(client, SWT.NONE);
		labelMedic1.setText("Sanit‰ter :");
		labelMedic1.setBackground(CustomColors.SECTION_BACKGROUND);

		//create composite for the combo and the image
		comp = makeComposite(client, 2);

		Combo medic1Combo = new Combo(comp, SWT.READ_ONLY);
		medic1ComboViewer = new ComboViewer(medic1Combo);
		medic1ComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) 
			{
				checkRequiredFields();
			}
		});	
		medic1ComboViewer.setContentProvider(new StaffMemberVehicleContentProvider(vehicleDetail));
		medic1ComboViewer.setLabelProvider(new  StaffMemberVehicleLabelProvider());
		medic1ComboViewer.setInput(ModelFactory.getInstance().getStaffManager().getUnassignedStaffList());
		//create the hyperlink
		ImageHyperlink removeMedic = toolkit.createImageHyperlink(comp, SWT.NONE);
		removeMedic.setBackground(CustomColors.SECTION_BACKGROUND);
		removeMedic.setToolTipText("Zieht den aktuell zugewiesenen Sanit‰ter vom Fahrzeug ab");
		removeMedic.setImage(ImageFactory.getInstance().getRegisteredImage("admin.remove"));
		removeMedic.addHyperlinkListener(new HyperlinkAdapter()
		{
			@Override
			public void linkActivated(HyperlinkEvent e) 
			{
				medic1ComboViewer.setSelection(null);	
			}
		});

		//medic2
		final Label labelMedic2 = new Label(client, SWT.NONE);
		labelMedic2.setText("Sanit‰ter :");
		labelMedic2.setBackground(CustomColors.SECTION_BACKGROUND);

		//create composite for the combo and the image
		comp = makeComposite(client, 2);
		comp.setBackground(CustomColors.SECTION_BACKGROUND);

		Combo medic2Combo = new Combo(comp, SWT.READ_ONLY);
		medic2ComboViewer = new ComboViewer(medic2Combo);
		medic2ComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) 
			{
				checkRequiredFields();
			}
		});
		medic2ComboViewer.setContentProvider(new StaffMemberVehicleContentProvider(vehicleDetail));
		medic2ComboViewer.setLabelProvider(new  StaffMemberVehicleLabelProvider());
		medic2ComboViewer.setInput(ModelFactory.getInstance().getStaffManager().getUnassignedStaffList());

		//create the hyperlink
		ImageHyperlink removeMedic2 = toolkit.createImageHyperlink(comp, SWT.NONE);
		removeMedic2.setBackground(CustomColors.SECTION_BACKGROUND);
		removeMedic2.setToolTipText("Zieht den aktuell zugewiesenen Sanit‰ter vom Fahrzeug ab");
		removeMedic2.setImage(ImageFactory.getInstance().getRegisteredImage("admin.remove"));
		removeMedic2.addHyperlinkListener(new HyperlinkAdapter()
		{
			@Override
			public void linkActivated(HyperlinkEvent e) 
			{
				medic2ComboViewer.setSelection(null);	
			}
		});

		//layout for the labels
		GridData data = new GridData();
		data.widthHint = 100;
		labelDriver.setLayoutData(data);
		data = new GridData();
		data.widthHint = 100;
		labelMedic1.setLayoutData(data);
		data = new GridData();
		data.widthHint = 100;
		labelMedic2.setLayoutData(data);
		//layout for the text fields
		GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
		driverCombo.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		medic1Combo.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		medic2Combo.setLayoutData(data2);
	}

	/**
	 * Creates the notes section
	 * @param parent the parent composite
	 */
	private void createNotesSection(Composite parent)
	{
		//create the section
		Group group = new Group(parent,SWT.NONE);
		group.setText("Anmerkungen");
		group.setLayout(new GridLayout());
		group.setLayoutData(new GridData(GridData.FILL_BOTH));

		//create the container for the notes
		Composite notesField = toolkit.createComposite(group);
		group.setBackground(CustomColors.SECTION_BACKGROUND);
		notesField.setLayout(new GridLayout());
		GridData notesData = new GridData(GridData.FILL_BOTH);
		notesData.heightHint = 80;
		notesData.grabExcessVerticalSpace = true;
		notesData.grabExcessHorizontalSpace = true;
		notesField.setLayoutData(notesData);

		noteEditor = new TextViewer(notesField, SWT.BORDER | SWT.FLAT | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		noteEditor.setDocument(new Document());
		noteEditor.getControl().setLayoutData(notesData);
		noteEditor.getControl().setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		noteEditor.setEditable(true);
	}

	/**
	 * Helper method to create a composite
	 * @param parent the parent control
	 * @param col the number of cols
	 * @return the returned composite
	 */
	public Composite makeComposite(Composite parent, int col) 
	{
		Composite nameValueComp = toolkit.createComposite(parent);
		GridLayout layout = new GridLayout(col, false);
		layout.marginHeight = 3;
		nameValueComp.setLayout(layout);
		nameValueComp.setLayoutData(new GridData(GridData.FILL_BOTH));
		nameValueComp.setBackground(CustomColors.SECTION_BACKGROUND);
		return nameValueComp;
	}

	/**
	 * Helper method to determine wheter all fields are valid
	 * @returns true if we have all required fields
	 */
	private boolean checkRequiredFields()
	{
		//reset the fields
		setErrorMessage(null);	
		setMessage(FORM_DESCRIPTION,IMessageProvider.INFORMATION);
		//Check the crew
		if(driverComboViewer.getSelection().isEmpty())
		{
			setErrorMessage("Dem Fahrzeug wurde noch kein Fahrer zugewiesen.");
			readyButton.setSelection(false);
			return false;
		}
		if(medic1ComboViewer.getSelection().isEmpty())
		{
			setMessage("Dem Fahrzeug wurde noch kein Sanit‰ter zugewiesen oder es fehlt ein Sanit‰ter.",IMessageProvider.WARNING);
			return false;
		}
		if(medic2ComboViewer.getSelection().isEmpty())
		{
			setMessage("Dem Fahrzeug wurde noch kein Sanit‰ter zugewiesen oder es fehlt ein Sanit‰ter.",IMessageProvider.WARNING);
			return false;
		}
		return true;
	}
}
