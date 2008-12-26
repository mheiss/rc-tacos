package at.rc.tacos.client.ui.admin.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.controller.EditorCloseAction;
import at.rc.tacos.client.ui.controller.EditorSaveAction;
import at.rc.tacos.client.ui.utils.CustomColors;
import at.rc.tacos.platform.model.Disease;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.listeners.DataChangeListener;
import at.rc.tacos.platform.net.message.AddMessage;
import at.rc.tacos.platform.net.message.RemoveMessage;
import at.rc.tacos.platform.net.message.UpdateMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;

public class DiseaseEditor extends EditorPart implements DataChangeListener<Disease> {

	public static final String ID = "at.rc.tacos.client.editors.diseaseEditor";

	// properties
	boolean isDirty;
	private FormToolkit toolkit;
	private ScrolledForm form;

	private CLabel infoLabel;
	private ImageHyperlink saveHyperlink, removeHyperlink;
	private Text id, name;

	// managed data
	private Disease disease;
	private boolean isNew;

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		disease = ((DiseaseEditorInput) getEditorInput()).getDisease();
		isNew = ((DiseaseEditorInput) getEditorInput()).isNew();
		isDirty = false;

		// Create the form
		toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
		form = toolkit.createScrolledForm(parent);
		toolkit.decorateFormHeading(form.getForm());
		form.getBody().setLayout(new GridLayout());
		form.getBody().setLayoutData(new GridData(GridData.FILL_BOTH));

		// create the content
		createManageSection(form.getBody());
		createDetailSection(form.getBody());

		// load the data
		loadData();

		// register the listener
		NetWrapper.registerListener(this, Disease.class);

		// force redraw
		form.pack(true);
	}

	@Override
	public void dispose() {
		NetWrapper.removeListener(this, Disease.class);
	}

	/**
	 * Creates the section to manage the changes
	 */
	private void createManageSection(Composite parent) {
		Composite client = createSection(parent, "Erkrankung verwalten");

		// create info label and hyperlinks to save and revert the changes
		infoLabel = new CLabel(client, SWT.NONE);
		infoLabel.setText("Hier können sie die aktuelle Erkrankung verwalten und die Änderungen speichern.");
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
		removeHyperlink.setText("Erkrankung löschen");
		removeHyperlink.setImage(UiWrapper.getDefault().getImageRegistry().get("admin.diseaseRemove"));
		removeHyperlink.addHyperlinkListener(new HyperlinkAdapter() {

			@Override
			public void linkActivated(HyperlinkEvent e) {
				boolean result = MessageDialog.openConfirm(getSite().getShell(), "Löschen der Erkrankung bestätigen", "Möchten sie die Erkrankung "
						+ disease.getDiseaseName() + " wirklich löschen?");
				if (!result)
					return;
				// reset the dirty flag to prevent the 'save changes' to popup
				// on a deleted item
				isDirty = false;
				// send the remove request
				RemoveMessage<Disease> removeMessage = new RemoveMessage<Disease>(disease);
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
	 * Creates the section containing the disease details
	 * 
	 * @param parent
	 *            the parent composite
	 */
	private void createDetailSection(Composite parent) {
		Composite client = createSection(parent, "Erkrankungs Details");

		// label and the text field
		final Label labelId = toolkit.createLabel(client, "Erkrankungs ID");
		id = toolkit.createText(client, "");
		id.setEditable(false);
		id.setBackground(CustomColors.GREY_COLOR);
		id.setToolTipText("Die ID wird automatisch generiert");

		final Label labelCompName = toolkit.createLabel(client, "Erkrankungs Bezeichnung");
		name = toolkit.createText(client, "");
		name.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent me) {
				inputChanged();
			}
		});

		// set the layout for the composites
		GridData data = new GridData();
		data.widthHint = 150;
		labelId.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelCompName.setLayoutData(data);
		// layout for the text fields
		GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
		id.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		name.setLayoutData(data2);
	}

	/**
	 * Loads the data and shows them in the view
	 */
	private void loadData() {
		// initialize the editor
		if (isNew) {
			form.setText("Neue Erkrankung anlegen");
			removeHyperlink.setVisible(false);
			return;
		}

		// enable the remove link
		removeHyperlink.setVisible(true);

		// load the data
		form.setText("Details der Erkrankung " + disease.getDiseaseName());
		id.setText(String.valueOf(disease.getId()));
		name.setText(disease.getDiseaseName());
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// reset error message
		form.setMessage(null, IMessageProvider.NONE);

		// name must be provided
		if (name.getText().length() > 30 || name.getText().trim().isEmpty()) {
			form.getDisplay().beep();
			form.setMessage("Bitte geben sie eine gültige Bezeichnung für die Erkrankung an (max. 30 Zeichen)", IMessageProvider.ERROR);
			return;
		}
		disease.setDiseaseName(name.getText());

		// add or update the disease
		if (isNew) {
			AddMessage<Disease> addMessage = new AddMessage<Disease>(disease);
			addMessage.asnchronRequest(NetWrapper.getSession());
		}
		else {
			UpdateMessage<Disease> updateMessage = new UpdateMessage<Disease>(disease);
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
	public void dataChanged(Message<Disease> message, MessageIoSession messageIoSession) {
		switch (message.getMessageType()) {
			case ADD:
			case UPDATE:
				addOrUpdate(message.getFirstElement());
				break;
			case REMOVE:
				remove(message.getFirstElement());
				break;
		}
	}

	/**
	 * Helper method a add or update a disease
	 */
	private void addOrUpdate(Disease updateDisease) {
		if (!disease.equals(updateDisease) | !disease.getDiseaseName().equals(updateDisease.getDiseaseName())) {
			return;
		}
		// save the updated disease
		setInput(new DiseaseEditorInput(updateDisease, false));
		setPartName(updateDisease.getDiseaseName());
		disease = updateDisease;
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
	 * Helper method to remove a disease
	 */
	private void remove(Disease removedDisease) {
		if (!disease.equals(removedDisease)) {
			return;
		}
		MessageDialog.openInformation(getSite().getShell(), "Erkrankung wurde gelöscht",
				"Die Erkrankung, welches Sie gerade editieren, wurde gelöscht");
		EditorCloseAction closeAction = new EditorCloseAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		closeAction.run();
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
		DiseaseEditorInput diseaseInput = (DiseaseEditorInput) getEditorInput();
		Disease persistantDisease = diseaseInput.getDisease();

		// check the disease name
		if (!name.getText().equalsIgnoreCase(persistantDisease.getDiseaseName())) {
			isDirty = true;
			infoLabel.setText("Bitte speichern Sie ihre lokalen Änderungen.");
			infoLabel.setImage(UiWrapper.getDefault().getImageRegistry().get("info.warning"));
			saveHyperlink.setEnabled(true);
			saveHyperlink.setForeground(CustomColors.COLOR_BLUE);
			saveHyperlink.setImage(UiWrapper.getDefault().getImageRegistry().get("admin.save"));
		}
		else {
			infoLabel.setText("Hier können sie die aktuelle Erkrankung verwalten und die Änderungen speichern.");
			infoLabel.setImage(UiWrapper.getDefault().getImageRegistry().get("admin.info"));
			saveHyperlink.setEnabled(false);
			saveHyperlink.setForeground(CustomColors.GREY_COLOR);
			saveHyperlink.setImage(UiWrapper.getDefault().getImageRegistry().get("admin.saveDisabled"));
		}

		// set the dirty flag
		firePropertyChange(IWorkbenchPartConstants.PROP_DIRTY);
	}
}
