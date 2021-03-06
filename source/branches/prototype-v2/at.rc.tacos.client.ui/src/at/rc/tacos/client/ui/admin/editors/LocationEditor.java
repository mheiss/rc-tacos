package at.rc.tacos.client.ui.admin.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.ComboViewer;
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
import at.rc.tacos.client.net.handler.MobilePhoneHandler;
import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.controller.EditorCloseAction;
import at.rc.tacos.client.ui.controller.EditorSaveAction;
import at.rc.tacos.client.ui.providers.HandlerContentProvider;
import at.rc.tacos.client.ui.providers.MobilePhoneLabelProvider;
import at.rc.tacos.client.ui.utils.CustomColors;
import at.rc.tacos.platform.model.Location;
import at.rc.tacos.platform.model.MobilePhoneDetail;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.listeners.DataChangeListener;
import at.rc.tacos.platform.net.message.AddMessage;
import at.rc.tacos.platform.net.message.UpdateMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;

public class LocationEditor extends EditorPart implements DataChangeListener<Object> {

	public static final String ID = "at.rc.tacos.client.editors.locationEditor";

	// properties
	boolean isDirty;
	private FormToolkit toolkit;
	private ScrolledForm form;

	private CLabel infoLabel;
	private ImageHyperlink saveHyperlink;
	private Text locationName, street, streetNumber, zipCode, city;
	private TextViewer notesViewer;
	private ComboViewer phoneViewer;

	// managed data
	private Location location;
	private boolean isNew;

	// the handlers for the data
	private MobilePhoneHandler phoneHandler = (MobilePhoneHandler) NetWrapper.getHandler(MobilePhoneDetail.class);

	/**
	 * This is a callback that allows us to create the viewer and initialize it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		location = ((LocationEditorInput) getEditorInput()).getLocation();
		isNew = ((LocationEditorInput) getEditorInput()).isNew();
		isDirty = false;

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
		NetWrapper.registerListener(this, Location.class);
		NetWrapper.registerListener(this, MobilePhoneDetail.class);

		// force redraw
		form.pack(true);
	}

	@Override
	public void dispose() {
		NetWrapper.removeListener(this, Location.class);
		NetWrapper.removeListener(this, MobilePhoneDetail.class);
	}

	/**
	 * Creates the section to manage the changes
	 */
	private void createManageSection(Composite parent) {
		Composite client = createSection(parent, "Ortsstelle verwalten");

		// create info label and hyperlinks to save and revert the changes
		infoLabel = new CLabel(client, SWT.NONE);
		infoLabel.setText("Zum anlegen einer neuen Ortsstelle bitte die Systemadministratoren informieren");
		infoLabel.setImage(UiWrapper.getDefault().getImageRegistry().get("admin.info"));

		// Create the hyperlink to save the changes
		saveHyperlink = toolkit.createImageHyperlink(client, SWT.NONE);
		saveHyperlink.setText("�nderungen speichern");
		saveHyperlink.setEnabled(false);
		saveHyperlink.setForeground(CustomColors.COLOR_GREY);
		saveHyperlink.setImage(UiWrapper.getDefault().getImageRegistry().get("admin.saveDisabled"));
		saveHyperlink.addHyperlinkListener(new HyperlinkAdapter() {

			@Override
			public void linkActivated(HyperlinkEvent e) {
				EditorSaveAction saveAction = new EditorSaveAction();
				saveAction.run();
			}
		});

		// info label should span over two
		GridData data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 2;
		data.widthHint = 600;
		infoLabel.setLayoutData(data);
	}

	/**
	 * Creates the section containing the job details
	 * 
	 * @param parent
	 *            the parent composite
	 */
	private void createDetailSection(Composite parent) {
		Composite client = createSection(parent, "Dienststellen Details");

		// label and the text field
		final Label labelLocationName = toolkit.createLabel(client, "Name der Ortsstelle");
		locationName = toolkit.createText(client, "");
		locationName.setEditable(false);
		locationName.setEnabled(false);
		locationName.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent me) {
				inputChanged();
			}
		});
		final Label labelStreet = toolkit.createLabel(client, "Addresse");
		street = toolkit.createText(client, "");
		street.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent me) {
				inputChanged();
			}
		});
		final Label labelStreetNumber = toolkit.createLabel(client, "Hausnummer");
		streetNumber = toolkit.createText(client, "");
		streetNumber.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent me) {
				inputChanged();
			}
		});
		final Label labelZip = toolkit.createLabel(client, "Postleitzahl");
		zipCode = toolkit.createText(client, "");
		zipCode.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent me) {
				inputChanged();
			}
		});
		final Label labelCity = toolkit.createLabel(client, "Stadt");
		city = toolkit.createText(client, "");
		city.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent me) {
				inputChanged();
			}
		});
		// the phone viewer
		final Label labelPhone = toolkit.createLabel(client, "Telefon der Ortstelle");
		Combo comboPhone = new Combo(client, SWT.READ_ONLY);
		phoneViewer = new ComboViewer(comboPhone);
		phoneViewer.setContentProvider(new HandlerContentProvider());
		phoneViewer.setLabelProvider(new MobilePhoneLabelProvider());
		phoneViewer.setInput(phoneHandler);
		phoneViewer.getCombo().addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent me) {
				inputChanged();
			}
		});
		// the notes section
		final Label labelNotes = toolkit.createLabel(client, "Notizen zur Ortsstelle");
		notesViewer = new TextViewer(client, SWT.BORDER | SWT.FLAT | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		notesViewer.setDocument(new Document());
		notesViewer.getControl().setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		notesViewer.setEditable(true);
		notesViewer.addTextListener(new ITextListener() {

			@Override
			public void textChanged(TextEvent te) {
				inputChanged();
			}
		});

		// set the layout for the composites
		GridData data = new GridData();
		data.widthHint = 150;
		labelLocationName.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelStreet.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelStreetNumber.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelZip.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelCity.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelPhone.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelNotes.setLayoutData(data);

		// layout for the text fields
		GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
		locationName.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		street.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		streetNumber.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		zipCode.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		city.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		phoneViewer.getCombo().setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		data2.heightHint = 100;
		notesViewer.getTextWidget().setLayoutData(data2);
	}

	/**
	 * Loads the data and shows them in the view
	 */
	private void loadData() {
		// init the editor
		if (isNew) {
			form.setText("Neue Dienststelle anlegen");
			return;
		}

		// load the data
		form.setText("Details des Ortsstelle " + location.getLocationName());
		locationName.setText(location.getLocationName());
		street.setText(location.getStreet());
		streetNumber.setText(location.getStreetNumber());
		zipCode.setText(String.valueOf(location.getZipcode()));
		city.setText(location.getCity());
		notesViewer.getTextWidget().setText(location.getNotes());
		phoneViewer.setSelection(new StructuredSelection(location.getPhone()));
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// reset error message
		form.setMessage(null, IMessageProvider.NONE);

		// save the name
		if (locationName.getText().length() > 30 || locationName.getText().trim().isEmpty()) {
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie eine g�ltige Bezeichnung f�r die Ortsstelle ein(max. 30 Zeichen)", IMessageProvider.ERROR);
			return;
		}
		location.setLocationName(locationName.getText());

		// save the street
		if (street.getText().length() > 30 || street.getText().trim().isEmpty()) {
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie eine Stra�e ein", IMessageProvider.ERROR);
			return;
		}
		location.setStreet(street.getText());

		// save the street number
		if (streetNumber.getText().length() > 10 || streetNumber.getText().trim().isEmpty()) {
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie eine g�ltige Hausnummer ein(max. 10 Zeichen)", IMessageProvider.ERROR);
			return;
		}
		location.setStreetNumber(streetNumber.getText());

		// save the city
		if (city.getText().trim().isEmpty()) {
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie eine g�ltige Stadt ein(max. 30 Zeichen)", IMessageProvider.ERROR);
			return;
		}
		location.setCity(city.getText());

		// save the zip
		if (zipCode.getText().trim().isEmpty()) {
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie eine Postleitzahl ein", IMessageProvider.ERROR);
			return;
		}
		// validate the number
		String pattern = "\\d{4}";
		if (!zipCode.getText().matches(pattern)) {
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie eine g�ltige Postleitzahl ein", IMessageProvider.ERROR);
			return;
		}
		location.setZipcode(Integer.valueOf(zipCode.getText()));

		// the notes can be empty
		location.setNotes(notesViewer.getTextWidget().getText());

		int index = phoneViewer.getCombo().getSelectionIndex();
		if (index == -1) {
			form.getDisplay().beep();
			form.setMessage("Bitte w�hlen Sie ein Telefon f�r die Ortsstelle aus.\n"
					+ "Neue Telefone k�nnen Sie im Administrationsbereich unter \"Mobiltelefone\" anlegen und dann der Ortsstelle zuweisen",
					IMessageProvider.ERROR);
			return;
		}
		location.setPhone((MobilePhoneDetail) phoneViewer.getElementAt(index));

		// add or update the location
		if (isNew) {
			AddMessage<Location> addMessage = new AddMessage<Location>(location);
			addMessage.asnchronRequest(NetWrapper.getSession());
		}
		else {
			UpdateMessage<Location> updateMessage = new UpdateMessage<Location>(location);
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
		// update the location
		if (message.getFirstElement() instanceof Location) {
			Location location = (Location) message.getFirstElement();
			switch (message.getMessageType()) {
				case ADD:
				case UPDATE:
					addOrUpdateLocation(location);
					break;
				case REMOVE:
					removeLocation(location);
					break;
			}
		}

		// update the mobile phone
		if (message.getFirstElement() instanceof MobilePhoneDetail) {
			refreshMobilPhone();
		}
	}

	/**
	 * Helper method to add or update a location
	 */
	private void addOrUpdateLocation(Location updatedLocation) {
		if (!location.equals(updatedLocation) | !location.getLocationName().equals(updatedLocation.getLocationName())) {
			return;
		}
		// save the updated location
		setInput(new LocationEditorInput(updatedLocation, false));
		setPartName(location.getLocationName());
		location = updatedLocation;
		isNew = false;
		// update the editor
		loadData();
		// show the result
		isDirty = false;
		infoLabel.setText("�nderungen gespeichert");
		infoLabel.setImage(UiWrapper.getDefault().getImageRegistry().get("info.ok"));
		Display.getCurrent().beep();
	}

	/**
	 * Helper method to remove a location
	 */
	private void removeLocation(Location removedLocation) {
		if (!location.equals(removedLocation)) {
			return;
		}
		MessageDialog.openInformation(getSite().getShell(), "Ortsstelle wurde gel�scht",
				"Die Ortsstelle, welches Sie gerade editieren, wurde gel�scht");
		EditorCloseAction closeAction = new EditorCloseAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		closeAction.run();
	}

	/**
	 * Helper method to refresh the mobile phone
	 */
	private void refreshMobilPhone() {
		phoneViewer.refresh(true);
	}

	// Helper methods
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
		// reset the flag
		isDirty = false;

		// get the current input
		LocationEditorInput locationInput = (LocationEditorInput) getEditorInput();
		Location persistantLocation = locationInput.getLocation();

		// check the location name
		if (!locationName.getText().equalsIgnoreCase(persistantLocation.getLocationName())) {
			isDirty = true;
		}
		// street
		if (!street.getText().equalsIgnoreCase(persistantLocation.getStreet())) {
			isDirty = true;
		}
		// streetnumber
		if (!streetNumber.getText().equalsIgnoreCase(persistantLocation.getStreetNumber())) {
			isDirty = true;
		}
		// city
		if (!city.getText().equalsIgnoreCase(persistantLocation.getCity())) {
			isDirty = true;
		}
		// zip
		if (!zipCode.getText().equalsIgnoreCase(String.valueOf(persistantLocation.getZipcode()))) {
			isDirty = true;
		}
		// notes
		if (!notesViewer.getTextWidget().getText().equalsIgnoreCase(persistantLocation.getNotes())) {
			isDirty = true;
		}
		// phone
		int index = phoneViewer.getCombo().getSelectionIndex();
		if (index != -1) {
			MobilePhoneDetail phone = (MobilePhoneDetail) phoneViewer.getElementAt(index);
			if (!phone.equals(persistantLocation.getPhone())) {
				isDirty = true;
			}
		}

		if (isDirty) {
			infoLabel.setText("Bitte speichern Sie ihre lokalen �nderungen.");
			infoLabel.setImage(UiWrapper.getDefault().getImageRegistry().get("info.warning"));
			saveHyperlink.setEnabled(true);
			saveHyperlink.setForeground(CustomColors.COLOR_BLUE);
			saveHyperlink.setImage(UiWrapper.getDefault().getImageRegistry().get("admin.save"));
		}
		else {
			infoLabel.setText("Zum anlegen einer neuen Ortsstelle bitte die Systemadministratoren informieren");
			infoLabel.setImage(UiWrapper.getDefault().getImageRegistry().get("admin.info"));
			saveHyperlink.setEnabled(false);
			saveHyperlink.setForeground(CustomColors.COLOR_GREY);
			saveHyperlink.setImage(UiWrapper.getDefault().getImageRegistry().get("admin.saveDisabled"));
		}

		// set the dirty flag
		firePropertyChange(IWorkbenchPartConstants.PROP_DIRTY);
	}
}
