/*******************************************************************************
 * Copyright (c) 2008, 2009 Internettechnik, FH JOANNEUM
 * http://www.fh-joanneum.at/itm
 * 
 * 	Licenced under the GNU GENERAL PUBLIC LICENSE Version 2;
 * 	You may obtain a copy of the License at
 * 	http://www.gnu.org/licenses/gpl-2.0.txt
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *******************************************************************************/
package at.rc.tacos.client.editors;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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

import at.rc.tacos.client.controller.EditorCloseAction;
import at.rc.tacos.client.controller.EditorSaveAction;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.ServiceType;

public class ServiceTypeEditor extends EditorPart implements PropertyChangeListener {

	public static final String ID = "at.rc.tacos.client.editors.serviceTypeEditor";

	// properties
	boolean isDirty;
	private FormToolkit toolkit;
	private ScrolledForm form;

	private CLabel infoLabel;
	private ImageHyperlink saveHyperlink, removeHyperlink;
	private Text id, name;

	// managed data
	private ServiceType serviceType;
	private boolean isNew;

	/**
	 * Default class constructor
	 */
	public ServiceTypeEditor() {
		ModelFactory.getInstance().getServiceManager().addPropertyChangeListener(this);
	}

	/**
	 * Cleanup
	 */
	@Override
	public void dispose() {
		ModelFactory.getInstance().getServiceManager().removePropertyChangeListener(this);
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		serviceType = ((ServiceTypeEditorInput) getEditorInput()).getServiceType();
		isNew = ((ServiceTypeEditorInput) getEditorInput()).isNew();
		isDirty = false;

		// Create the form
		toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
		form = toolkit.createScrolledForm(parent);
		toolkit.decorateFormHeading(form.getForm());
		form.getBody().setLayout(new GridLayout());
		form.getBody().setLayoutData(new GridData(GridData.FILL_BOTH));

		// create the contence
		createManageSection(form.getBody());
		createDetailSection(form.getBody());

		// load the data
		loadData();

		// disable editing of system serviceTypes
		if (serviceType.getId() <= 5 && serviceType.getId() > 0) {
			form.setText("Vom System vorgegebene Dienstverhältnisse können nicht bearbeitet werden.");
			form.setEnabled(false);
		}

		// force redraw
		form.pack(true);
	}

	/**
	 * Creates the section to manage the changes
	 */
	private void createManageSection(Composite parent) {
		Composite client = createSection(parent, "Dienstverhältnis verwalten");

		// create info label and hyperlinks to save and revert the changes
		infoLabel = new CLabel(client, SWT.NONE);
		infoLabel.setText("Hier können sie das aktuelle Dienstverhältnis verwalten und die Änderungen speichern.");
		infoLabel.setImage(ImageFactory.getInstance().getRegisteredImage("admin.info"));

		// Create the hyperlink to save the changes
		saveHyperlink = toolkit.createImageHyperlink(client, SWT.NONE);
		saveHyperlink.setText("Änderungen speichern");
		saveHyperlink.setEnabled(false);
		saveHyperlink.setForeground(CustomColors.GREY_COLOR);
		saveHyperlink.setImage(ImageFactory.getInstance().getRegisteredImage("admin.saveDisabled"));
		saveHyperlink.addHyperlinkListener(new HyperlinkAdapter() {

			@Override
			public void linkActivated(HyperlinkEvent e) {
				EditorSaveAction saveAction = new EditorSaveAction();
				saveAction.run();
			}
		});

		// Create the hyperlink to remove the competence
		removeHyperlink = toolkit.createImageHyperlink(client, SWT.NONE);
		removeHyperlink.setText("Dienstverhältnis löschen");
		removeHyperlink.setImage(ImageFactory.getInstance().getRegisteredImage("admin.serviceTypeRemove"));
		removeHyperlink.addHyperlinkListener(new HyperlinkAdapter() {

			@Override
			public void linkActivated(HyperlinkEvent e) {
				boolean result = MessageDialog.openConfirm(getSite().getShell(), "Löschen des Dienstverhältnisses bestätigen",
						"Möchten sie das Dienstverhältnis " + serviceType.getServiceName() + " wirklich löschen?");
				if (!result)
					return;
				// reset the dirty flag to prevent the 'save changes' to popup
				// on a deleted item
				isDirty = false;
				// send the remove request
				NetWrapper.getDefault().sendRemoveMessage(ServiceType.ID, serviceType);
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
		Composite client = createSection(parent, "Dienstverhältnis Details");

		// label and the text field
		final Label labelId = toolkit.createLabel(client, "Dienstverhältnis ID");
		id = toolkit.createText(client, "");
		id.setEditable(false);
		id.setBackground(CustomColors.GREY_COLOR);
		id.setToolTipText("Die ID wird automatisch generiert");

		final Label labelCompName = toolkit.createLabel(client, "Dienstverhältnis");
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
		// init the editor
		if (isNew) {
			form.setText("Details des Dienstverhältnisses " + serviceType.getServiceName());
			removeHyperlink.setVisible(false);
			return;
		}

		// enable the remove link
		removeHyperlink.setVisible(true);

		// load the data
		form.setText("Neues Dienstverhältnis anlegen");
		id.setText(String.valueOf(serviceType.getId()));
		name.setText(serviceType.getServiceName());
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// reset error message
		form.setMessage(null, IMessageProvider.NONE);

		// name must be provided
		if (name.getText().length() > 30 || name.getText().trim().isEmpty()) {
			form.getDisplay().beep();
			form.setMessage("Bitte geben sie eine gültige Bezeichnung für das Dienstverhältnis an(max. 30 Zeichen)", IMessageProvider.ERROR);
			return;
		}
		serviceType.setServiceName(name.getText());

		// add or update the service type
		if (isNew)
			NetWrapper.getDefault().sendAddMessage(ServiceType.ID, serviceType);
		else
			NetWrapper.getDefault().sendUpdateMessage(ServiceType.ID, serviceType);
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
	public void propertyChange(PropertyChangeEvent evt) {
		if ("SERVICETYPE_UPDATE".equals(evt.getPropertyName()) || "SERVICETYPE_ADD".equalsIgnoreCase(evt.getPropertyName())) {
			ServiceType updateService = null;
			// get the new value
			if (evt.getNewValue() instanceof ServiceType)
				updateService = (ServiceType) evt.getNewValue();

			// assert we have a value
			if (updateService == null)
				return;

			// is this service type is the current -> update it
			if (serviceType.equals(updateService) || serviceType.getServiceName().equals(updateService.getServiceName())) {
				// save the updated service type
				setInput(new ServiceTypeEditorInput(updateService, false));
				setPartName(updateService.getServiceName());
				serviceType = updateService;
				isNew = false;
				// update the editor
				loadData();
				// show the result
				isDirty = false;
				infoLabel.setText("Änderungen gespeichert");
				infoLabel.setImage(ImageFactory.getInstance().getRegisteredImage("info.ok"));
				Display.getCurrent().beep();
			}
		}
		if ("SERVICETYPE_REMOVE".equalsIgnoreCase(evt.getPropertyName())) {
			// get the removed service type
			ServiceType removedService = (ServiceType) evt.getOldValue();
			// check the type
			if (serviceType.equals(removedService)) {
				MessageDialog.openInformation(getSite().getShell(), "Dienstverhältnis wurde gelöscht",
						"Das Dienstverhältnis, welches Sie gerade editieren, wurde gelöscht");
				EditorCloseAction closeAction = new EditorCloseAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
				closeAction.run();
			}
		}
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
		ServiceTypeEditorInput serviceInput = (ServiceTypeEditorInput) getEditorInput();
		ServiceType persistantService = serviceInput.getServiceType();

		// check the service type name
		if (!name.getText().equalsIgnoreCase(persistantService.getServiceName())) {
			isDirty = true;
			infoLabel.setText("Bitte speichern Sie ihre lokalen Änderungen.");
			infoLabel.setImage(ImageFactory.getInstance().getRegisteredImage("info.warning"));
			saveHyperlink.setEnabled(true);
			saveHyperlink.setForeground(CustomColors.COLOR_LINK);
			saveHyperlink.setImage(ImageFactory.getInstance().getRegisteredImage("admin.save"));
		}
		else {
			infoLabel.setText("Hier können sie das aktuelle Dienstverhältnis verwalten und die Änderungen speichern.");
			infoLabel.setImage(ImageFactory.getInstance().getRegisteredImage("admin.info"));
			saveHyperlink.setEnabled(false);
			saveHyperlink.setForeground(CustomColors.GREY_COLOR);
			saveHyperlink.setImage(ImageFactory.getInstance().getRegisteredImage("admin.saveDisabled"));
		}

		// set the dirty flag
		firePropertyChange(IWorkbenchPartConstants.PROP_DIRTY);
	}
}
