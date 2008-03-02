package at.rc.tacos.client.view;

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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.Section;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.MobilePhoneContentProvider;
import at.rc.tacos.client.providers.MobilePhoneLabelProvider;
import at.rc.tacos.client.providers.StaffMemberLabelProvider;
import at.rc.tacos.client.providers.StaffMemberVehicleContentProvider;
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
	
	// description text
	public final static String FORM_DESCRIPTION = "Hier kˆnnen Sie Fahrzeug und dessen Besatzung verwalten";

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
		//save
		this.vehicleDetail = vehicle;
	}

	/**
	 * Creates the dialog's contents
	 * @param parent the parent composite
	 * @return Control
	 */
	protected Control createContents(Composite parent) 
	{
		Control contents = super.createContents(parent);
		setTitle("Fahrzeugverwaltung");
		setMessage(FORM_DESCRIPTION, IMessageProvider.INFORMATION);
		setTitleImage(ImageFactory.getInstance().getRegisteredImage("application.logo"));
		//draw the content
		contents.redraw();
		Composite client = ((Composite)contents);
		client.layout(true);
		getShell().setSize(500, 600);
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
			System.out.println("VVVVVVVVVVVVVvehicleFFFFFFFFFForm, firstParamedic: " +vehicleDetail.getFirstParamedic());
			if(vehicleDetail.getFirstParamedic() != null)
				medic1ComboViewer.setSelection(new StructuredSelection(vehicleDetail.getFirstParamedic()));
			if(vehicleDetail.getSecondParamedic() != null)
				medic2ComboViewer.setSelection(new StructuredSelection(vehicleDetail.getSecondParamedic()));
			readyButton.setSelection(vehicleDetail.isReadyForAction());
			outOfOrder.setSelection(vehicleDetail.isOutOfOrder());
			noteEditor.getDocument().set(vehicleDetail.getVehicleNotes());
		}
		checkRequiredFields();

		//force redraw
		composite.redraw();
		composite.layout(true);
		return composite;
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
			getShell().dispose();
			super.cancelPressed();
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
		vehicleDetail.setOutOfOrder(outOfOrder.getSelection());
		if(vehicleDetail.isOutOfOrder())
		    vehicleDetail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_NA);
		vehicleDetail.setReadyForAction(readyButton.getSelection());
		//if the vehicle was out of order -> set the vehicle image to green
		if(vehicleDetail.isReadyForAction())
		    vehicleDetail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_GREEN);
		//phone
		index = mobilePhoneComboViewer.getCombo().getSelectionIndex();
		vehicleDetail.setMobilPhone((MobilePhoneDetail)mobilePhoneComboViewer.getElementAt(index));
		//station
		index = stationComboViewer.getCombo().getSelectionIndex();
		vehicleDetail.setCurrentStation((Location)stationComboViewer.getElementAt(index));

		//Send the update message
		NetWrapper.getDefault().sendUpdateMessage(VehicleDetail.ID, vehicleDetail);
		super.okPressed();
		getShell().dispose();
		getShell().close();
	}

	/**
	 * Creates the detail section for the vehicle
	 * @parent the parent composite
	 */
	private void createDetailSection(Composite parent)
	{
		Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | Section.TWISTIE);
		toolkit.createCompositeSeparator(section);
		section.setText("Fahrzeugdetails");
		section.setLayout(new GridLayout());
		section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		section.setExpanded(true);

		Composite client = new Composite(section, SWT.NONE);
		section.setClient(client);
		//layout
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		client.setLayout(gridLayout);

		final Label labelStaff = new Label(client, SWT.NONE);
		labelStaff.setText("Fahrzeug:");
		
		Combo vehicleCombo = new Combo(client, SWT.READ_ONLY);
		vehicleCombo.setEnabled(false);
		vehicleComboViewer = new ComboViewer(vehicleCombo);
		vehicleComboViewer.setContentProvider(new VehicleContentProvider());
		vehicleComboViewer.setLabelProvider(new VehicleLabelProvider());
		vehicleComboViewer.setInput(ModelFactory.getInstance().getVehicleList());

		//Mobile Phone
		final Label labelPhone = new Label(client, SWT.NONE);
		labelPhone.setText("Handy :");

		Combo mobilePhoneCombo = new Combo(client, SWT.READ_ONLY);
		mobilePhoneComboViewer = new ComboViewer(mobilePhoneCombo);
		mobilePhoneComboViewer.setContentProvider(new MobilePhoneContentProvider());
		mobilePhoneComboViewer.setLabelProvider(new MobilePhoneLabelProvider());
		mobilePhoneComboViewer.setInput(ModelFactory.getInstance().getPhoneList());

		//Station
		final Label labelStation = new Label(client, SWT.NONE);
		labelStation.setText("Aktuelle Ortsstelle :");

		Combo stationCombo = new Combo(client, SWT.READ_ONLY);
		stationCombo.setToolTipText("Ist das Fahrzeug einer anderen Dienststelle zugeordnet, kann dies hier ausgew‰hlt werden.");
		stationComboViewer = new ComboViewer(stationCombo);
		stationComboViewer.setContentProvider(new StationContentProvider());
		stationComboViewer.setLabelProvider(new StationLabelProvider());
		stationComboViewer.setInput(ModelFactory.getInstance().getLocationList());

		//layout for the labels
		GridData data = new GridData();
		data.widthHint = 100;
		labelStaff.setLayoutData(data);
		data = new GridData();
		data.widthHint = 100;
		labelPhone.setLayoutData(data);
		data = new GridData();
		data.widthHint = 100;
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
		Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | Section.TWISTIE);
		toolkit.createCompositeSeparator(section);
		section.setText("Einsatzstatus");
		section.setDescription("Der aktuelle Einsatzstatus des Fahrzeuges");
		section.setLayout(new GridLayout());
		section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		section.setExpanded(true);

		//composite to add the client area
		Composite client = new Composite(section, SWT.NONE);
		section.setClient(client);

		//layout
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.horizontalSpacing = 15;
		layout.makeColumnsEqualWidth = false;
		client.setLayout(layout);
		GridData clientDataLayout = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
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
		Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | Section.TWISTIE);
		toolkit.createCompositeSeparator(section);
		section.setText("Fahrzeugbesatzung");
		section.setLayout(new GridLayout());
		section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		section.setExpanded(true);

		//composite to add the client area
		Composite client = new Composite(section, SWT.NONE);
		section.setClient(client);

		//layout
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.horizontalSpacing = 15;
		layout.makeColumnsEqualWidth = false;
		client.setLayout(layout);
		GridData clientDataLayout = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		client.setLayoutData(clientDataLayout);

		//driver
		final Label labelDriver = new Label(client, SWT.NONE);
		labelDriver.setText("Fahrer :");
		
		//create composite for the combo and the image
		Composite comp = makeComposite(client, 2);

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
		if(vehicleDetail.getDriver() != null)
			driverComboViewer.setContentProvider(new StaffMemberVehicleContentProvider(vehicleDetail.getDriver()));
		else
			driverComboViewer.setContentProvider(new StaffMemberVehicleContentProvider());
		driverComboViewer.setLabelProvider(new StaffMemberLabelProvider());
		driverComboViewer.setInput(ModelFactory.getInstance().getStaffList().getUnassignedStaffList());
				
		//create the hyperlink
		ImageHyperlink removeDriver = toolkit.createImageHyperlink(comp, SWT.NONE);
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
		if(vehicleDetail.getFirstParamedic() != null)
			medic1ComboViewer.setContentProvider(new StaffMemberVehicleContentProvider(vehicleDetail.getFirstParamedic()));//TODO here!
		else
			medic1ComboViewer.setContentProvider(new StaffMemberVehicleContentProvider());
		medic1ComboViewer.setLabelProvider(new StaffMemberLabelProvider());
		medic1ComboViewer.setInput(ModelFactory.getInstance().getStaffList().getUnassignedStaffList());
		//create the hyperlink
		ImageHyperlink removeMedic = toolkit.createImageHyperlink(comp, SWT.NONE);
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
		
		//create composite for the combo and the image
		comp = makeComposite(client, 2);

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
		if(vehicleDetail.getSecondParamedic() != null)
			medic2ComboViewer.setContentProvider(new StaffMemberVehicleContentProvider(vehicleDetail.getSecondParamedic()));
		else
			medic2ComboViewer.setContentProvider(new StaffMemberVehicleContentProvider());
		medic2ComboViewer.setLabelProvider(new StaffMemberLabelProvider());
		medic2ComboViewer.setInput(ModelFactory.getInstance().getStaffList().getUnassignedStaffList());

		//create the hyperlink
		ImageHyperlink removeMedic2 = toolkit.createImageHyperlink(comp, SWT.NONE);
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
		Section dayInfoSection = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR);
		toolkit.createCompositeSeparator(dayInfoSection);
		dayInfoSection.setText("Anmerkungen");
		dayInfoSection.setExpanded(true);
		dayInfoSection.setLayout(new GridLayout());
		//info should span over two
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		dayInfoSection.setLayoutData(data);

		//create the container for the notes
		Composite notesField = toolkit.createComposite(dayInfoSection);
		dayInfoSection.setClient(notesField);
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
