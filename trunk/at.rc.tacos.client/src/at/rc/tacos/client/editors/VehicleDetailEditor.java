package at.rc.tacos.client.editors;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.EditorPart;

import at.rc.tacos.client.controller.EditorCloseAction;
import at.rc.tacos.client.controller.EditorNewVehicleAction;
import at.rc.tacos.client.controller.EditorSaveAction;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.MobilePhoneContentProvider;
import at.rc.tacos.client.providers.MobilePhoneLabelProvider;
import at.rc.tacos.client.providers.StaffMemberContentProvider;
import at.rc.tacos.client.providers.StaffMemberLabelProvider;
import at.rc.tacos.client.providers.StationContentProvider;
import at.rc.tacos.client.providers.StationLabelProvider;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.VehicleDetail;

public class VehicleDetailEditor extends EditorPart implements PropertyChangeListener
{
	public static final String ID = "at.rc.tacos.client.editors.vehicleDetailEditor";

	//properties
	boolean isDirty;
	private FormToolkit toolkit;
	private ScrolledForm form;

	//changeable values
	private ImageHyperlink saveHyperlink,addHyperlink,closeHyperlink;
	private Text vehicleType,vehicleName;
	private ComboViewer basicLocationViewer;
	//read only values - are changed in the vehicleForm
	private ComboViewer driverViewer,firstParamedicViewer,secondParamedicViewer;
	private ComboViewer phoneViewer,currentLocationViewer;
	private TextViewer notesViewer;
	private Button readyForAction,outOfOrder;

	//managed data
	private VehicleDetail detail;
	private boolean isNew;

	/**
	 * Default class constructor
	 */
	public VehicleDetailEditor()
	{
		ModelFactory.getInstance().getVehicleList().addPropertyChangeListener(this);
		ModelFactory.getInstance().getStaffList().addPropertyChangeListener(this);
		ModelFactory.getInstance().getPhoneList().addPropertyChangeListener(this);
	}

	/**
	 * Cleanup
	 */
	@Override
	public void dispose()
	{
		ModelFactory.getInstance().getVehicleList().removePropertyChangeListener(this);
		ModelFactory.getInstance().getStaffList().removePropertyChangeListener(this);
		ModelFactory.getInstance().getPhoneList().removePropertyChangeListener(this);
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	@Override
	public void createPartControl(final Composite parent) 
	{	
		detail = ((VehicleDetailEditorInput)getEditorInput()).getVehicle();
		isNew = ((VehicleDetailEditorInput)getEditorInput()).isNew();

		//Create the form
		toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
		form = toolkit.createScrolledForm(parent);
		toolkit.decorateFormHeading(form.getForm());
		form.getBody().setLayout(new GridLayout());
		form.getBody().setLayoutData(new GridData(GridData.FILL_BOTH));

		//create the content
		createManageSection(form.getBody());
		createDetailSection(form.getBody());
		createReadOnlySection(form.getBody());

		//load the data
		if(!isNew)
			loadData();
		else
			form.setText("Neues Fahrzeug anlegen");

		//force redraw
		form.pack(true);
	}

	/**
	 * Creates the section to manage the changes
	 */
	private void createManageSection(Composite parent)
	{
		Composite client = createSection(parent, "Kompetenz verwalten");

		//create info label and hyperlinks to save and revert the changes
		CLabel infoLabel = new CLabel(client,SWT.NONE);
		infoLabel.setText("Hier k�nnen sie das aktuelle Fahrzeug verwalten und die �nderungen speichern.");
		infoLabel.setImage(ImageFactory.getInstance().getRegisteredImage("image.admin.info"));

		//Create the hyperlink to save the changes
		saveHyperlink = toolkit.createImageHyperlink(client, SWT.NONE);
		saveHyperlink.setText("Neues Fahrzeug anlegen");
		saveHyperlink.setImage(ImageFactory.getInstance().getRegisteredImage("image.admin.save"));
		saveHyperlink.addHyperlinkListener(new HyperlinkAdapter() 
		{
			@Override
			public void linkActivated(HyperlinkEvent e) 
			{
				EditorSaveAction saveAction = new EditorSaveAction();
				saveAction.run();
			}
		});

		//Create the hyperlink to close the window and revert the changes
		closeHyperlink = toolkit.createImageHyperlink(client, SWT.NONE);
		closeHyperlink.setText("Fenster schlie�en");
		closeHyperlink.setImage(ImageFactory.getInstance().getRegisteredImage("image.admin.close"));
		closeHyperlink.addHyperlinkListener(new HyperlinkAdapter()
		{
			@Override
			public void linkActivated(HyperlinkEvent e) 
			{
				EditorCloseAction closeAction = new EditorCloseAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
				closeAction.run();
			}
		});

		//create the hyperlink to add a new vehicle
		addHyperlink = toolkit.createImageHyperlink(client, SWT.NONE);
		addHyperlink.setText("Fahrzeug anlegen");
		addHyperlink.setImage(ImageFactory.getInstance().getRegisteredImage("image.admin.add"));
		addHyperlink.addHyperlinkListener(new HyperlinkAdapter()
		{
			@Override
			public void linkActivated(HyperlinkEvent e) 
			{
				EditorNewVehicleAction newAction = new EditorNewVehicleAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
				newAction.run();
			}
		});
		//show the hyperlink only when we edit a existing vehicle
		if(isNew)
			addHyperlink.setVisible(false);

		//info label should span over two
		GridData data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 2;
		infoLabel.setLayoutData(data);
	}

	/**
	 * Creates the section containing the competence details
	 * @param parent the parent composite
	 */
	private void createDetailSection(Composite parent)
	{
		Composite client = createSection(parent, "Fahrzeug Details");

		//label and the text field
		final Label labelVehicleType = toolkit.createLabel(client,"Fahrzeug Typ");
		vehicleType = toolkit.createText(client, "");

		final Label labelVehicleName = toolkit.createLabel(client, "Fahrzeug Name");
		vehicleName = toolkit.createText(client, "");

		final Label labelBasicLoaction = toolkit.createLabel(client, "Basis Dienststelle");
		Combo stationCombo = new Combo(client, SWT.READ_ONLY);
		basicLocationViewer = new ComboViewer(stationCombo);
		basicLocationViewer.setContentProvider(new StationContentProvider());
		basicLocationViewer.setLabelProvider(new StationLabelProvider());
		basicLocationViewer.setInput(ModelFactory.getInstance().getLocationList());

		//set the layout for the composites
		GridData data = new GridData();
		data.widthHint = 150;
		labelVehicleType.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelVehicleName.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelBasicLoaction.setLayoutData(data);
		//layout for the text fields
		GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
		vehicleName.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		vehicleType.setLayoutData(data2);	
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		basicLocationViewer.getCombo().setLayoutData(data2);
	}

	/**
	 * Creates the sction to display the read only section to display actual data.
	 * @param parent
	 */
	private void createReadOnlySection(Composite parent)
	{
		Composite client = createSection(parent, "Aktuelle Fahrzeug Daten");
		//driver
		final Label labelDriver = toolkit.createLabel(client, "Fahrer");
		Combo driverCombo = new Combo(client, SWT.READ_ONLY);
		driverViewer = new ComboViewer(driverCombo);
		driverViewer.setContentProvider(new StaffMemberContentProvider());
		driverViewer.setLabelProvider(new StaffMemberLabelProvider());
		driverViewer.setInput(ModelFactory.getInstance().getStaffList().getStaffList());
		//medic
		final Label labelFirstParamedic = toolkit.createLabel(client, "Sanit�ter");
		Combo firstParamedicCombo = new Combo(client, SWT.READ_ONLY);
		firstParamedicViewer = new ComboViewer(firstParamedicCombo);
		firstParamedicViewer.setContentProvider(new StaffMemberContentProvider());
		firstParamedicViewer.setLabelProvider(new StaffMemberLabelProvider());
		firstParamedicViewer.setInput(ModelFactory.getInstance().getStaffList().getStaffList());
		//medic
		final Label labelSecondParamedic = toolkit.createLabel(client, "Sanit�ter");
		Combo secondParamedicCombo = new Combo(client, SWT.READ_ONLY);
		secondParamedicViewer = new ComboViewer(secondParamedicCombo);
		secondParamedicViewer.setContentProvider(new StaffMemberContentProvider());
		secondParamedicViewer.setLabelProvider(new StaffMemberLabelProvider());
		secondParamedicViewer.setInput(ModelFactory.getInstance().getStaffList().getStaffList());
		//mobile phone
		final Label labelPhone = toolkit.createLabel(client, "Mobiltelefon");
		Combo phoneCombo = new Combo(client,SWT.READ_ONLY);
		phoneViewer = new ComboViewer(phoneCombo);
		phoneViewer.setContentProvider(new MobilePhoneContentProvider());
		phoneViewer.setLabelProvider(new MobilePhoneLabelProvider());
		phoneViewer.setInput(ModelFactory.getInstance().getPhoneList().getMobilePhoneList());
		//create the label and the dropdown field
		final Label locationLabel = toolkit.createLabel(client, "Aktuelle Ortsstelle");
		Combo currentLocationCombo = new Combo(client, SWT.READ_ONLY);
		currentLocationViewer = new ComboViewer(currentLocationCombo);
		currentLocationViewer.setContentProvider(new StationContentProvider());
		currentLocationViewer.setLabelProvider(new StationLabelProvider());
		currentLocationViewer.setInput(ModelFactory.getInstance().getLocationList());
		//the notes section
		final Label labelNotes = toolkit.createLabel(client,"Notizen zum Fahrzeug");
		notesViewer = new TextViewer(client, SWT.BORDER | SWT.FLAT | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		notesViewer.setDocument(new Document());
		notesViewer.getControl().setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		notesViewer.setEditable(true);
		//status of the vehicle
		final Label labelAction = toolkit.createLabel(client, "Einsatzbereit");
		readyForAction = toolkit.createButton(client, "", SWT.CHECK);
		final Label labelRepair = toolkit.createLabel(client, "Au�er Dienst");
		outOfOrder = toolkit.createButton(client, "", SWT.CHECK);

		//disable each field
		driverViewer.getCombo().setEnabled(false);
		driverViewer.getCombo().setBackground(CustomColors.GREY_COLOR);
		firstParamedicViewer.getCombo().setEnabled(false);
		firstParamedicViewer.getCombo().setBackground(CustomColors.GREY_COLOR);
		secondParamedicViewer.getCombo().setEnabled(false);
		secondParamedicViewer.getCombo().setBackground(CustomColors.GREY_COLOR);
		phoneViewer.getCombo().setEnabled(false);
		phoneViewer.getCombo().setBackground(CustomColors.GREY_COLOR);
		currentLocationViewer.getCombo().setEnabled(false);
		currentLocationViewer.getCombo().setBackground(CustomColors.GREY_COLOR);
		notesViewer.getTextWidget().setEditable(false);
		notesViewer.getTextWidget().setBackground(CustomColors.GREY_COLOR);
		readyForAction.setEnabled(false);
		outOfOrder.setEnabled(false);

		//set the layout for the composites
		GridData data = new GridData();
		data.widthHint = 150;
		labelDriver.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelFirstParamedic.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelSecondParamedic.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelPhone.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		locationLabel.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelNotes.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelAction.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelRepair.setLayoutData(data);
		//the input fields
		GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
		driverViewer.getCombo().setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		firstParamedicViewer.getCombo().setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		secondParamedicViewer.getCombo().setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		phoneViewer.getCombo().setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		currentLocationViewer.getCombo().setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		data2.heightHint = 100;
		notesViewer.getTextWidget().setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		readyForAction.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		outOfOrder.setLayoutData(data2);
	}

	/**
	 * Loads the data and shows them in the view
	 */
	private void loadData()
	{
		form.setText("Details des Fahrzeugs: " + detail.getVehicleType() + " " + detail.getVehicleName());
		if(!isNew)
		{
			//adjust the links
			saveHyperlink.setText("�nderungen speichern");
			addHyperlink.setVisible(true);
		}
		//load all the data
		vehicleName.setText(detail.getVehicleName());
		vehicleType.setText(detail.getVehicleType());
		if(detail.getBasicStation() != null)
			basicLocationViewer.setSelection(new StructuredSelection(detail.getBasicStation()));
		if(detail.getDriver() != null)
			driverViewer.setSelection(new StructuredSelection(detail.getDriver()));
		if(detail.getFirstParamedic() != null)
			firstParamedicViewer.setSelection(new StructuredSelection(detail.getFirstParamedic()));
		if(detail.getSecondParamedic() != null)
			secondParamedicViewer.setSelection(new StructuredSelection(detail.getSecondParamedic()));
		if(detail.getMobilePhone() != null)
			phoneViewer.setSelection(new StructuredSelection(detail.getMobilePhone()));
		if(detail.getCurrentStation() != null)
			currentLocationViewer.setSelection(new StructuredSelection(detail.getCurrentStation()));
		notesViewer.getTextWidget().setText(detail.getVehicleNotes());
		readyForAction.setSelection(detail.isReadyForAction());
		outOfOrder.setSelection(detail.isOutOfOrder());
	}

	@Override
	public void doSave(IProgressMonitor monitor) 
	{
		//reset error message
		form.setMessage(null, IMessageProvider.NONE);

		//just set and validate the changeable values
		if(vehicleType.getText().trim().isEmpty())
		{
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie den Fahrzeugtyp an", IMessageProvider.ERROR);
			return;
		}
		detail.setVehicleType(vehicleType.getText());

		//validate the name
		if(vehicleName.getText().trim().isEmpty())
		{
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie einen Fahrzeugname ein", IMessageProvider.ERROR);
			return;
		}
		detail.setVehicleName(vehicleName.getText());

		//basic location
		int index = basicLocationViewer.getCombo().getSelectionIndex();
		if(index == -1)
		{
			form.getDisplay().beep();
			form.setMessage("Bitte ordnen Sie diesem Fahrzeug eine basis Ortsstelle zu", IMessageProvider.ERROR);
			return;
		}
		detail.setBasicStation((Location)basicLocationViewer.getElementAt(index));

		//the other fields are read only and must not be set explicite
		if(isNew)
			NetWrapper.getDefault().sendAddMessage(VehicleDetail.ID, detail);
		else
			NetWrapper.getDefault().sendUpdateMessage(VehicleDetail.ID, detail);
	}

	@Override
	public void doSaveAs() 
	{
		//not supported
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException 
	{
		setSite(site);
		setInput(input);
		setPartName(input.getName());
	}

	@Override
	public void setFocus() 
	{
		form.setFocus();
	}

	@Override
	public boolean isDirty() 
	{
		return isDirty;
	}

	@Override
	public boolean isSaveAsAllowed() 
	{
		//not supported
		return false;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) 
	{
		if("VEHICLE_UPDATE".equals(evt.getPropertyName())
				|| "VEHICLE_ADD".equalsIgnoreCase(evt.getPropertyName()))
		{
			VehicleDetail updateVehicle = null;
			//get the new value
			if(evt.getNewValue() instanceof VehicleDetail)
				updateVehicle = (VehicleDetail)evt.getNewValue();

			//assert we have a value
			if(updateVehicle == null)
				return;

			//is this competence is the current one -> update it
			if(detail.equals(updateVehicle))
			{
				//save the updated competence
				setInput(new VehicleDetailEditorInput(updateVehicle,false));
				setPartName(updateVehicle.getVehicleType() +" "+updateVehicle.getVehicleName());
				detail = updateVehicle;
				isNew = false;
				//update the editor
				loadData();
			}
		}
		//keep on track on location changes
		if("LOCATION_ADD".equalsIgnoreCase(evt.getPropertyName())
				|| "LOCATION_REMOVE".equalsIgnoreCase(evt.getPropertyName())
				|| "LOCATION_UPDATE".equalsIgnoreCase(evt.getPropertyName()))
		{
			//just refresh the combo so that the new data is loaded
			basicLocationViewer.refresh(true);
			currentLocationViewer.refresh(true);
		}
		//keep on track on staff changes
		if("STAFF_ADD".equalsIgnoreCase(evt.getPropertyName())
				|| "STAFF_UPDATE".equalsIgnoreCase(evt.getPropertyName())
				|| "STAFF_REMOVE".equalsIgnoreCase(evt.getPropertyName()))
		{
			driverViewer.refresh(true);
			firstParamedicViewer.refresh(true);
			secondParamedicViewer.refresh(true);
		}
		//keep on track on phone changes
		if("PHONE_ADD".equalsIgnoreCase(evt.getPropertyName())
				|| "PHONE_UPDATE".equalsIgnoreCase(evt.getPropertyName())
				|| "PHONE_REMOVE".equalsIgnoreCase(evt.getPropertyName()))
		{
			phoneViewer.refresh(true);
		}
	}

	/**
	 * Creates and returns a section and a composite with two colums
	 * @param parent the parent composite
	 * @param sectionName the title of the section
	 * @return the created composite to hold the other widgets
	 */
	private Composite createSection(Composite parent,String sectionName)
	{
		//create the section
		Section section = toolkit.createSection(parent,ExpandableComposite.TITLE_BAR | Section.TWISTIE);
		toolkit.createCompositeSeparator(section);
		section.setText(sectionName);
		section.setLayout(new GridLayout());
		section.setLayoutData(new GridData(GridData.BEGINNING | GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING));
		section.setExpanded(true);
		//composite to add the client area
		Composite client = new Composite(section, SWT.NONE);
		section.setClient(client);

		//layout
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth = false;
		client.setLayout(layout);
		GridData clientDataLayout = new GridData(GridData.BEGINNING | GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_BOTH);
		client.setLayoutData(clientDataLayout);

		return client;
	}
}