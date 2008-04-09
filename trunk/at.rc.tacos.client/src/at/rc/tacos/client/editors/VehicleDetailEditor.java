package at.rc.tacos.client.editors;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
import at.rc.tacos.client.controller.EditorDeleteAction;
import at.rc.tacos.client.controller.EditorSaveAction;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.MobilePhoneContentProvider;
import at.rc.tacos.client.providers.MobilePhoneLabelProvider;
import at.rc.tacos.client.providers.StationContentProvider;
import at.rc.tacos.client.providers.StationLabelProvider;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.VehicleDetail;

public class VehicleDetailEditor extends EditorPart implements PropertyChangeListener
{
	public static final String ID = "at.rc.tacos.client.editors.vehicleDetailEditor";

	//properties
	boolean isDirty;
	private FormToolkit toolkit;
	private ScrolledForm form;

	//changeable values
	private ImageHyperlink saveHyperlink, removeHyperlink;
	private Text vehicleType,vehicleName;
	private ComboViewer basicLocationViewer;
	//read only values - are changed in the vehicleForm
	private ComboViewer driverViewer,firstParamedicViewer,secondParamedicViewer;
	private ComboViewer phoneViewer,currentLocationViewer;


	//managed data
	private VehicleDetail detail;
	private boolean isNew;

	/**
	 * Default class constructor
	 */
	public VehicleDetailEditor()
	{
		ModelFactory.getInstance().getVehicleManager().addPropertyChangeListener(this);
		ModelFactory.getInstance().getStaffManager().addPropertyChangeListener(this);
		ModelFactory.getInstance().getPhoneManager().addPropertyChangeListener(this);
	}

	/**
	 * Cleanup
	 */
	@Override
	public void dispose()
	{
		ModelFactory.getInstance().getVehicleManager().removePropertyChangeListener(this);
		ModelFactory.getInstance().getStaffManager().removePropertyChangeListener(this);
		ModelFactory.getInstance().getPhoneManager().removePropertyChangeListener(this);
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
		infoLabel.setText("Hier können sie das aktuelle Fahrzeug verwalten und die Änderungen speichern.");
		infoLabel.setImage(ImageFactory.getInstance().getRegisteredImage("admin.info"));

		//Create the hyperlink to save the changes
		saveHyperlink = toolkit.createImageHyperlink(client, SWT.NONE);
		saveHyperlink.setText("Neues Fahrzeug speichern");
		saveHyperlink.setImage(ImageFactory.getInstance().getRegisteredImage("admin.save"));
		saveHyperlink.addHyperlinkListener(new HyperlinkAdapter() 
		{
			@Override
			public void linkActivated(HyperlinkEvent e) 
			{
				EditorSaveAction saveAction = new EditorSaveAction();
				saveAction.run();
			}
		});
		
		//Create the hyperlink to remove the competence
		removeHyperlink = toolkit.createImageHyperlink(client, SWT.NONE);
		removeHyperlink.setText("Fahrzeug löschen");
		removeHyperlink.setImage(ImageFactory.getInstance().getRegisteredImage("admin.vehicleRemove"));
		removeHyperlink.addHyperlinkListener(new HyperlinkAdapter()
		{
			@Override
			public void linkActivated(HyperlinkEvent e) 
			{
				boolean result = MessageDialog.openConfirm(getSite().getShell(), 
						"Löschen des Fahrzeuges bestätigen", 
						"Möchten sie das Fahrzeug "+detail.getVehicleType()+"-"+detail.getVehicleName()+" wirklich löschen?");
				if(!result)
					return;
				//send the remove request
				EditorDeleteAction deleteAction = new EditorDeleteAction(VehicleDetail.ID,detail);
				deleteAction.run();
			}
		});

		//info label should span over two
		GridData data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 2;
		infoLabel.setLayoutData(data);
		//save hyperlink should span over two
		data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 2;
		saveHyperlink.setLayoutData(data);
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
		basicLocationViewer.setInput(ModelFactory.getInstance().getLocationManager());
		
		//mobile phone
		final Label labelPhone = toolkit.createLabel(client, "Mobiltelefon");
		Combo phoneCombo = new Combo(client,SWT.READ_ONLY);
		phoneViewer = new ComboViewer(phoneCombo);
		phoneViewer.setContentProvider(new MobilePhoneContentProvider());
		phoneViewer.setLabelProvider(new MobilePhoneLabelProvider());
		phoneViewer.setInput(ModelFactory.getInstance().getPhoneManager().getMobilePhoneList());
		
		//current location
		final Label locationLabel = toolkit.createLabel(client, "Aktuelle Ortsstelle");
		Combo currentLocationCombo = new Combo(client, SWT.READ_ONLY);
		currentLocationViewer = new ComboViewer(currentLocationCombo);
		currentLocationViewer.setContentProvider(new StationContentProvider());
		currentLocationViewer.setLabelProvider(new StationLabelProvider());
		currentLocationViewer.setInput(ModelFactory.getInstance().getLocationManager());

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
		data = new GridData();
		data.widthHint = 150;
		labelPhone.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		locationLabel.setLayoutData(data);
		
		//layout for the text fields
		GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
		vehicleName.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		vehicleType.setLayoutData(data2);	
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		basicLocationViewer.getCombo().setLayoutData(data2);
		phoneViewer.getCombo().setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		currentLocationViewer.getCombo().setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		data2.heightHint = 100;
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
			saveHyperlink.setText("Änderungen speichern");
		}
		//load all the data
		vehicleName.setText(detail.getVehicleName());
		vehicleType.setText(detail.getVehicleType());
		if(detail.getBasicStation() != null)
			basicLocationViewer.setSelection(new StructuredSelection(detail.getBasicStation()));
		if(detail.getMobilePhone() != null)
			phoneViewer.setSelection(new StructuredSelection(detail.getMobilePhone()));
		if(detail.getCurrentStation() != null)
			currentLocationViewer.setSelection(new StructuredSelection(detail.getCurrentStation()));	
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
			form.setMessage("Bitte geben Sie einen Fahrzeugnamen ein", IMessageProvider.ERROR);
			return;
		}
		detail.setVehicleName(vehicleName.getText());

		//basic location
		int index = basicLocationViewer.getCombo().getSelectionIndex();
		if(index == -1)
		{
			form.getDisplay().beep();
			form.setMessage("Bitte ordnen Sie diesem Fahrzeug eine Basis Ortsstelle zu", IMessageProvider.ERROR);
			return;
		}
		detail.setBasicStation((Location)basicLocationViewer.getElementAt(index));

		
		//mobile phone
		int index2 = phoneViewer.getCombo().getSelectionIndex();
		if(index2 == -1)
		{
			form.getDisplay().beep();
			form.setMessage("Bitte ordnen Sie diesem Fahrzeug eine Handynummer zu", IMessageProvider.ERROR);
			return;
		}
		detail.setMobilPhone((MobilePhoneDetail)phoneViewer.getElementAt(index2));
		
		
		//current location
		int index3 = currentLocationViewer.getCombo().getSelectionIndex();
		if(index3 == -1)
		{
			form.getDisplay().beep();
			form.setMessage("Bitte ordnen Sie diesem Fahrzeug eine aktuelle Ortsstelle zu", IMessageProvider.ERROR);
		}
		detail.setCurrentStation((Location)currentLocationViewer.getElementAt(index3));
		
		
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

			//is this vehicle is the current one -> update it
			if(detail.equals(updateVehicle) || 
					(detail.getVehicleName().equalsIgnoreCase(updateVehicle.getVehicleName())
							&& detail.getVehicleType().equalsIgnoreCase(updateVehicle.getVehicleType())))
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
		if("VEHICLE_REMOVE".equalsIgnoreCase(evt.getPropertyName()))
		{
			//the removed vehicle
			VehicleDetail removedVehicle = (VehicleDetail)evt.getOldValue();
			//the current edited
			if(detail.equals(removedVehicle))
			{
				MessageDialog.openInformation(getSite().getShell(), 
						"Fahrzeug wurde gelöscht",
						"Das Fahrzeug welches Sie gerade editieren wurde gelöscht");
				EditorCloseAction closeAction = new EditorCloseAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
				closeAction.run();
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
		Section section = toolkit.createSection(parent,ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE);
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