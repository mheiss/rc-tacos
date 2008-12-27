package at.rc.tacos.client.ui.admin.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPartConstants;
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

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.net.handler.LocationHandler;
import at.rc.tacos.client.net.handler.MobilePhoneHandler;
import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.controller.EditorCloseAction;
import at.rc.tacos.client.ui.controller.EditorSaveAction;
import at.rc.tacos.client.ui.providers.MobilePhoneLabelProvider;
import at.rc.tacos.client.ui.providers.StationLabelProvider;
import at.rc.tacos.client.ui.utils.CustomColors;
import at.rc.tacos.platform.model.Location;
import at.rc.tacos.platform.model.MobilePhoneDetail;
import at.rc.tacos.platform.model.VehicleDetail;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.listeners.DataChangeListener;
import at.rc.tacos.platform.net.message.AddMessage;
import at.rc.tacos.platform.net.message.RemoveMessage;
import at.rc.tacos.platform.net.message.UpdateMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;

public class VehicleDetailEditor extends EditorPart implements DataChangeListener<Object> {

	public static final String ID = "at.rc.tacos.client.editors.vehicleDetailEditor";

	// properties
	boolean isDirty;
	private FormToolkit toolkit;
	private ScrolledForm form;

	// changeable values
	private CLabel infoLabel;
	private ImageHyperlink saveHyperlink, removeHyperlink;
	private Text vehicleType, vehicleName;
	private ComboViewer basicLocationViewer, phoneViewer, currentLocationViewer;

	// managed data
	private VehicleDetail detail;
	private boolean isNew;

	// the data source
	private LocationHandler locationHandler = (LocationHandler) NetWrapper.getHandler(Location.class);
	private MobilePhoneHandler phoneHandler = (MobilePhoneHandler) NetWrapper.getHandler(MobilePhoneDetail.class);

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		detail = ((VehicleDetailEditorInput) getEditorInput()).getVehicle();
		isNew = ((VehicleDetailEditorInput) getEditorInput()).isNew();

		// Create the form
		toolkit = new FormToolkit(Display.getDefault());
		form = toolkit.createScrolledForm(parent);
		toolkit.decorateFormHeading(form.getForm());
		form.getBody().setLayout(new GridLayout());
		form.getBody().setLayoutData(new GridData(GridData.FILL_BOTH));

		// create the content
		createManageSection(form.getBody());
		createDetailSection(form.getBody());

		// load the data
		loadData();

		// register the listeners
		NetWrapper.registerListener(this, VehicleDetail.class);
		NetWrapper.registerListener(this, MobilePhoneDetail.class);

		// reset the dirty flag
		isDirty = false;

		// force redraw
		form.pack(true);
	}

	@Override
	public void dispose() {
		NetWrapper.removeListener(this, VehicleDetail.class);
		NetWrapper.removeListener(this, MobilePhoneDetail.class);
	}

	/**
	 * Creates the section to manage the changes
	 */
	private void createManageSection(Composite parent) {
		Composite client = createSection(parent, "Fahrzeuge verwalten");

		// create info label and hyperlinks to save and revert the changes
		infoLabel = new CLabel(client, SWT.NONE);
		infoLabel.setText("Hier können sie das aktuelle Fahrzeug verwalten und die Änderungen speichern.");
		infoLabel.setImage(UiWrapper.getDefault().getImageRegistry().get("admin.info"));

		// Create the hyperlink to save the changes
		saveHyperlink = toolkit.createImageHyperlink(client, SWT.NONE);
		saveHyperlink.setText("Änderungen speichern");
		saveHyperlink.setEnabled(false);
		saveHyperlink.setForeground(CustomColors.GREY_COLOR);
		saveHyperlink.setImage(UiWrapper.getDefault().getImageRegistry().get("admin.saveDisabled"));
		saveHyperlink.addHyperlinkListener(new HyperlinkAdapter() {

			@Override
			public void linkActivated(HyperlinkEvent e) {
				EditorSaveAction saveAction = new EditorSaveAction();
				saveAction.run();
			}
		});

		// Create the hyperlink to remove the competence
		removeHyperlink = toolkit.createImageHyperlink(client, SWT.NONE);
		removeHyperlink.setText("Fahrzeug löschen");
		removeHyperlink.setImage(UiWrapper.getDefault().getImageRegistry().get("admin.vehicleRemove"));
		removeHyperlink.addHyperlinkListener(new HyperlinkAdapter() {

			@Override
			public void linkActivated(HyperlinkEvent e) {
				boolean result = MessageDialog.openConfirm(getSite().getShell(), "Löschen des Fahrzeuges bestätigen", "Möchten sie das Fahrzeug "
						+ detail.getVehicleType() + "-" + detail.getVehicleName() + " wirklich löschen?");
				if (!result)
					return;
				// reset the dirty flag to prevent the 'save changes' to popup
				// on a deleted item
				isDirty = false;

				// send the remove request
				RemoveMessage<VehicleDetail> removeMessage = new RemoveMessage<VehicleDetail>(detail);
				removeMessage.asnchronRequest(NetWrapper.getSession());
			}
		});

		// info label should span over two
		GridData data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 2;
		data.widthHint = 600;
		infoLabel.setLayoutData(data);
	}

	/**
	 * Creates the section containing the competence details
	 * 
	 * @param parent
	 *            the parent composite
	 */
	private void createDetailSection(Composite parent) {
		Composite client = createSection(parent, "Fahrzeug Details");

		// label and the text field
		final Label labelVehicleType = toolkit.createLabel(client, "Fahrzeug Typ");
		vehicleType = toolkit.createText(client, "");
		vehicleType.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent me) {
				inputChanged();
			}
		});

		final Label labelVehicleName = toolkit.createLabel(client, "Fahrzeug Name");
		vehicleName = toolkit.createText(client, "");
		vehicleName.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent me) {
				inputChanged();
			}
		});

		final Label labelBasicLoaction = toolkit.createLabel(client, "Basis Dienststelle");
		Combo stationCombo = new Combo(client, SWT.READ_ONLY);
		basicLocationViewer = new ComboViewer(stationCombo);
		basicLocationViewer.setContentProvider(new ArrayContentProvider());
		basicLocationViewer.setLabelProvider(new StationLabelProvider());
		basicLocationViewer.setInput(locationHandler.toArray());
		stationCombo.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent me) {
				inputChanged();
			}
		});

		// mobile phone
		final Label labelPhone = toolkit.createLabel(client, "Mobiltelefon");
		Combo phoneCombo = new Combo(client, SWT.READ_ONLY);
		phoneViewer = new ComboViewer(phoneCombo);
		phoneViewer.setContentProvider(new ArrayContentProvider());
		phoneViewer.setLabelProvider(new MobilePhoneLabelProvider());
		phoneViewer.setInput(phoneHandler.toArray());
		phoneCombo.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent me) {
				inputChanged();
			}
		});

		// current location
		final Label locationLabel = toolkit.createLabel(client, "Aktuelle Ortsstelle");
		Combo currentLocationCombo = new Combo(client, SWT.READ_ONLY);
		currentLocationViewer = new ComboViewer(currentLocationCombo);
		currentLocationViewer.setContentProvider(new ArrayContentProvider());
		currentLocationViewer.setLabelProvider(new StationLabelProvider());
		currentLocationViewer.setInput(locationHandler.toArray());
		currentLocationCombo.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent me) {
				inputChanged();
			}
		});

		// set the layout for the composites
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

		// layout for the text fields
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
	private void loadData() {
		// init the editor
		if (isNew) {
			removeHyperlink.setVisible(false);
			form.setText("Neues Fahrzeug anlegen");
			return;
		}

		// enable the remove link
		removeHyperlink.setVisible(true);

		// load all the data
		form.setText("Details des Fahrzeugs: " + detail.getVehicleType() + " " + detail.getVehicleName());
		vehicleName.setText(detail.getVehicleName());
		vehicleType.setText(detail.getVehicleType());
		if (detail.getBasicStation() != null)
			basicLocationViewer.setSelection(new StructuredSelection(detail.getBasicStation()));
		if (detail.getMobilePhone() != null)
			phoneViewer.setSelection(new StructuredSelection(detail.getMobilePhone()));
		if (detail.getCurrentStation() != null)
			currentLocationViewer.setSelection(new StructuredSelection(detail.getCurrentStation()));
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// reset error message
		form.setMessage(null, IMessageProvider.NONE);

		// just set and validate the changeable values
		if (vehicleType.getText().trim().isEmpty()) {
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie den Fahrzeugtyp an", IMessageProvider.ERROR);
			return;
		}
		detail.setVehicleType(vehicleType.getText());

		// validate the name
		if (vehicleName.getText().trim().isEmpty()) {
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie einen Fahrzeugnamen ein", IMessageProvider.ERROR);
			return;
		}
		detail.setVehicleName(vehicleName.getText());

		// basic location
		int index = basicLocationViewer.getCombo().getSelectionIndex();
		if (index == -1) {
			form.getDisplay().beep();
			form.setMessage("Bitte ordnen Sie diesem Fahrzeug eine Basis Ortsstelle zu", IMessageProvider.ERROR);
			return;
		}
		detail.setBasicStation((Location) basicLocationViewer.getElementAt(index));

		// mobile phone
		int index2 = phoneViewer.getCombo().getSelectionIndex();
		if (index2 == -1) {
			form.getDisplay().beep();
			form.setMessage("Bitte ordnen Sie diesem Fahrzeug eine Handynummer zu", IMessageProvider.ERROR);
			return;
		}
		detail.setMobilPhone((MobilePhoneDetail) phoneViewer.getElementAt(index2));

		// current location
		int index3 = currentLocationViewer.getCombo().getSelectionIndex();
		if (index3 == -1) {
			form.getDisplay().beep();
			form.setMessage("Bitte ordnen Sie diesem Fahrzeug eine aktuelle Ortsstelle zu", IMessageProvider.ERROR);
		}
		detail.setCurrentStation((Location) currentLocationViewer.getElementAt(index3));

		// the other fields are read only and must not be set explicite
		if (isNew) {
			AddMessage<VehicleDetail> addMessage = new AddMessage<VehicleDetail>(detail);
			addMessage.asnchronRequest(NetWrapper.getSession());
		}
		else {
			UpdateMessage<VehicleDetail> updateMessage = new UpdateMessage<VehicleDetail>(detail);
			updateMessage.asnchronRequest(NetWrapper.getSession());
		}
	}

	@Override
	public void doSaveAs() {
		// not supported
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName(input.getName());
	}

	@Override
	public void setFocus() {
		form.setFocus();
	}

	@Override
	public boolean isDirty() {
		return isDirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// not supported
		return false;
	}

	@Override
	public void dataChanged(Message<Object> message, MessageIoSession messageIoSession) {
		if (message.getFirstElement() instanceof VehicleDetail) {
			VehicleDetail detail = (VehicleDetail) message.getFirstElement();
			switch (message.getMessageType()) {
				case ADD:
				case UPDATE:
					addOrUpdateVehicle(detail);
					break;
				case REMOVE:
					removeVehicle(detail);
					break;
			}
		}

		// refresh the combos
		phoneViewer.refresh(true);
		basicLocationViewer.refresh(true);
		currentLocationViewer.refresh(true);
	}

	/**
	 * Helper method to add or update the vehicle
	 */
	private void addOrUpdateVehicle(VehicleDetail updatedVehicle) {
		if (!detail.equals(updatedVehicle)
				| !(detail.getVehicleName().equalsIgnoreCase(updatedVehicle.getVehicleName()) & !detail.getVehicleType().equalsIgnoreCase(
						updatedVehicle.getVehicleType()))) {
			return;
		}
		// save the updated competence
		setInput(new VehicleDetailEditorInput(updatedVehicle, false));
		setPartName(updatedVehicle.getVehicleType() + " " + updatedVehicle.getVehicleName());
		detail = updatedVehicle;
		isNew = false;
		// update the editor
		loadData();
		// show the result
		isDirty = false;
		infoLabel.setText("Änderungen gespeichert");
		infoLabel.setImage(UiWrapper.getDefault().getImageRegistry().get("info.ok"));
		Display.getCurrent().beep();
	}

	/**
	 * Helper method to remove a vehicle
	 */
	private void removeVehicle(VehicleDetail removedVehicle) {
		if (!detail.equals(removedVehicle)) {
			return;
		}
		MessageDialog.openInformation(getSite().getShell(), "Fahrzeug wurde gelöscht", "Das Fahrzeug welches Sie gerade editieren wurde gelöscht");
		EditorCloseAction closeAction = new EditorCloseAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		closeAction.run();
	}

	/**
	 * Creates and returns a section and a composite with two colums
	 * 
	 * @param parent
	 *            the parent composite
	 * @param sectionName
	 *            the title of the section
	 * @return the created composite to hold the other widgets
	 */
	private Composite createSection(Composite parent, String sectionName) {
		// create the section
		Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE);
		toolkit.createCompositeSeparator(section);
		section.setText(sectionName);
		section.setLayout(new GridLayout());
		section.setLayoutData(new GridData(GridData.BEGINNING | GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING));
		section.setExpanded(true);
		// composite to add the client area
		Composite client = new Composite(section, SWT.NONE);
		section.setClient(client);

		// layout
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth = false;
		client.setLayout(layout);
		GridData clientDataLayout = new GridData(GridData.BEGINNING | GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING
				| GridData.FILL_BOTH);
		client.setLayoutData(clientDataLayout);

		return client;
	}

	/**
	 * This is called when the input of a text box or a combo box was changes
	 */
	private void inputChanged() {
		// get the current input
		VehicleDetailEditorInput vehicleInput = (VehicleDetailEditorInput) getEditorInput();
		VehicleDetail persistantVehicle = vehicleInput.getVehicle();

		// reset the flag
		isDirty = false;

		// check the vehicle name
		if (!vehicleName.getText().equalsIgnoreCase(persistantVehicle.getVehicleName())) {
			isDirty = true;
		}
		// check the vehicle type
		if (!vehicleType.getText().equalsIgnoreCase(persistantVehicle.getVehicleType())) {
			isDirty = true;
		}
		// check the basic location
		if (!basicLocationViewer.getSelection().isEmpty()) {
			IStructuredSelection structuredSelection = (IStructuredSelection) basicLocationViewer.getSelection();
			Location selectedLocation = (Location) structuredSelection.getFirstElement();
			if (!selectedLocation.equals(persistantVehicle.getBasicStation()))
				isDirty = true;
		}
		// check the current location
		if (!currentLocationViewer.getSelection().isEmpty()) {
			IStructuredSelection structuredSelection = (IStructuredSelection) currentLocationViewer.getSelection();
			Location selectedLocation = (Location) structuredSelection.getFirstElement();
			if (!selectedLocation.equals(persistantVehicle.getCurrentStation()))
				isDirty = true;
		}
		// check the phone
		if (!phoneViewer.getSelection().isEmpty()) {
			IStructuredSelection structuredSelection = (IStructuredSelection) phoneViewer.getSelection();
			MobilePhoneDetail selectedPhone = (MobilePhoneDetail) structuredSelection.getFirstElement();
			if (!selectedPhone.equals(persistantVehicle.getMobilePhone()))
				isDirty = true;
		}

		// notify the user that the input has changes
		if (isDirty) {
			infoLabel.setText("Bitte speichern Sie ihre lokalen Änderungen.");
			infoLabel.setImage(UiWrapper.getDefault().getImageRegistry().get("info.warning"));
			saveHyperlink.setEnabled(true);
			saveHyperlink.setForeground(CustomColors.COLOR_BLUE);
			saveHyperlink.setImage(UiWrapper.getDefault().getImageRegistry().get("admin.save"));
		}
		else {
			infoLabel.setText("Hier können sie das aktuelle Fahrzeug verwalten und die Änderungen speichern.");
			infoLabel.setImage(UiWrapper.getDefault().getImageRegistry().get("admin.info"));
			saveHyperlink.setEnabled(false);
			saveHyperlink.setForeground(CustomColors.GREY_COLOR);
			saveHyperlink.setImage(UiWrapper.getDefault().getImageRegistry().get("admin.saveDisabled"));
		}

		// set the dirty flag
		firePropertyChange(IWorkbenchPartConstants.PROP_DIRTY);
	}
}
