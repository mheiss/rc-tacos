package at.rc.tacos.client.ui.admin.view;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.net.handler.SickPersonHandler;
import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.admin.editors.SickPersonEditor;
import at.rc.tacos.client.ui.admin.editors.SickPersonEditorInput;
import at.rc.tacos.client.ui.controller.EditorNewSickPersonAction;
import at.rc.tacos.client.ui.controller.RefreshViewAction;
import at.rc.tacos.client.ui.jobs.FilterAddressJob;
import at.rc.tacos.client.ui.jobs.FilterPatientJob;
import at.rc.tacos.client.ui.providers.SickPersonAdminTableLabelProvider;
import at.rc.tacos.platform.model.SickPerson;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.listeners.DataChangeListener;
import at.rc.tacos.platform.net.message.GetMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;

public class SickPersonAdminView extends ViewPart implements DataChangeListener<SickPerson> {

	public static final String ID = "at.rc.tacos.client.view.admin.sickpersonAdminView";

	private Logger log = LoggerFactory.getLogger(SickPersonAdminView.class);

	// properties
	private TableViewer viewer;
	private FormToolkit toolkit;
	private ScrolledForm form;
	// text fields for the filter
	private Text lastname, firstname, svnr;

	// to show some messages
	private CLabel infoLabel;

	/**
	 * The scheduler job to start the filter
	 */
	private FilterPatientJob filterJob;

	// the data source
	private SickPersonHandler personHandler = (SickPersonHandler) NetWrapper.getHandler(SickPerson.class);

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		toolkit = new FormToolkit(Display.getDefault());
		form = toolkit.createScrolledForm(parent);
		form.setText("Liste der Patienten");
		toolkit.decorateFormHeading(form.getForm());
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		form.getBody().setLayout(layout);
		form.getBody().setLayoutData(new GridData(GridData.FILL_BOTH));

		// create the section to hold the filter
		Composite filter = createSection(form.getBody(), "Filter");

		// create the input fields
		final Label labelLastname = toolkit.createLabel(filter, "Nachname");
		lastname = toolkit.createText(filter, "");
		lastname.addModifyListener(new ModifyListener() {

			public void modifyText(final ModifyEvent e) {
				inputChanged();
			}
		});

		// the firstname
		final Label labelFirstname = toolkit.createLabel(filter, "Vorname");
		firstname = toolkit.createText(filter, "");
		firstname.addModifyListener(new ModifyListener() {

			public void modifyText(final ModifyEvent e) {
				inputChanged();
			}
		});

		// the svnr
		final Label labelSVNR = toolkit.createLabel(filter, "SVNR");
		svnr = toolkit.createText(filter, "");
		svnr.addModifyListener(new ModifyListener() {

			public void modifyText(final ModifyEvent e) {
				inputChanged();
			}
		});

		// create the info label
		infoLabel = new CLabel(filter, SWT.NONE);
		infoLabel.setText("Bitte geben sie mindestens ein Zeichen des Nachnamens ein");
		infoLabel.setImage(UiWrapper.getDefault().getImageRegistry().get("resource.info"));

		// create the section to hold the table
		Composite tableComp = createSection(form.getBody(), "Filter");
		Table table = new Table(tableComp, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		viewer = new TableViewer(table);
		viewer.setUseHashlookup(true);
		viewer.getTable().setLayout(new GridLayout());
		viewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		viewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent dce) {
				// get the selected disease
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				SickPerson person = (SickPerson) obj;
				// create the editor input and open
				SickPersonEditorInput input = new SickPersonEditorInput(person, false);
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					page.openEditor(input, SickPersonEditor.ID);
				}
				catch (PartInitException e) {
					log.error("Failed to open the editor for the sick person " + person, e);
				}
			}
		});
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new SickPersonAdminTableLabelProvider());
		viewer.setInput(personHandler.toArray());
		viewer.getTable().setLinesVisible(true);
		viewer.getTable().setHeaderVisible(true);
		getViewSite().setSelectionProvider(viewer);

		// create the columns
		final TableColumn imageColumn = new TableColumn(table, SWT.NONE);
		imageColumn.setToolTipText("");
		imageColumn.setWidth(30);
		imageColumn.setText("");

		final TableColumn zipColumn = new TableColumn(table, SWT.NONE);
		zipColumn.setToolTipText("Sozialversicherungsnummer");
		zipColumn.setWidth(60);
		zipColumn.setText("SVNR");

		final TableColumn cityColumn = new TableColumn(table, SWT.NONE);
		cityColumn.setToolTipText("Nachname");
		cityColumn.setWidth(180);
		cityColumn.setText("Nachname");

		final TableColumn streetColumn = new TableColumn(table, SWT.NONE);
		streetColumn.setToolTipText("Vorname");
		streetColumn.setWidth(180);
		streetColumn.setText("Vorname");

		// add actions to the toolbar
		createToolBarActions();

		// set this table as a selection provider
		getViewSite().setSelectionProvider(viewer);

		// set the layout for the composites
		GridData data = new GridData();
		data.widthHint = 80;
		labelLastname.setLayoutData(data);
		data = new GridData();
		data.widthHint = 80;
		labelFirstname.setLayoutData(data);
		data.widthHint = 80;
		labelSVNR.setLayoutData(data);
		labelFirstname.setLayoutData(data);
		data.widthHint = 80;
		// layout for the text fields
		GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
		lastname.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		firstname.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		svnr.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_BOTH);
		viewer.getTable().setLayoutData(data2);
		// the section of the table
		data2 = new GridData(GridData.FILL_BOTH);
		Section tableSection = (Section) tableComp.getParent();
		tableSection.setLayoutData(data2);
		// the info label
		data2 = new GridData(GridData.FILL_BOTH);
		data2.horizontalSpan = 2;
		infoLabel.setLayoutData(data2);

		// reflow
		form.reflow(true);

		// register listeners
		NetWrapper.registerListener(this, SickPerson.class);

		// disable the form if we do not have an admin
		String authorization = NetWrapper.getSession().getLogin().getAuthorization();
		if (!authorization.equalsIgnoreCase("Administrator"))
			form.setEnabled(false);
	}

	@Override
	public void dispose() {
		NetWrapper.removeListener(this, SickPerson.class);
	}

	/**
	 * Passes the focus to the view
	 */
	@Override
	public void setFocus() {
		form.setFocus();
	}

	@Override
	public void dataChanged(Message<SickPerson> message, MessageIoSession messageIoSession) {
		viewer.refresh();
		infoLabel.setText("Es wurden " + message.getObjects().size() + " Patienten gefunden");
		infoLabel.setImage(UiWrapper.getDefault().getImageRegistry().get("resource.info"));
	}

	/**
	 * Creates and adds the actions for the toolbar
	 */
	private void createToolBarActions() {
		GetMessage<SickPerson> getMessage = new GetMessage<SickPerson>(new SickPerson());
		// create the action
		EditorNewSickPersonAction addAction = new EditorNewSickPersonAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		RefreshViewAction<SickPerson> refreshAction = new RefreshViewAction<SickPerson>(getMessage);
		// add to the toolbar
		form.getToolBarManager().add(addAction);
		form.getToolBarManager().add(refreshAction);
		form.getToolBarManager().update(true);
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
		section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.BEGINNING | GridData.HORIZONTAL_ALIGN_BEGINNING
				| GridData.VERTICAL_ALIGN_BEGINNING));
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
	 * Helper method to apply the filer
	 */
	public void inputChanged() {
		// get the values
		final String strLastname = lastname.getText().trim().toLowerCase();

		if (strLastname.length() < 1) {
			infoLabel.setText("Bitte geben Sie mindestens ein Zeichen des Nachnamens ein.");
			infoLabel.setImage(UiWrapper.getDefault().getImageRegistry().get("resource.error"));
			Display.getCurrent().beep();
			return;
		}

		if (filterJob == null)
			filterJob = new FilterPatientJob(viewer);

		// check the state
		if (filterJob.getState() == Job.RUNNING) {
			System.out.println("Job is currently running");
			return;
		}

		// pass the entered text
		filterJob.setSearchString(strLastname);
		filterJob.schedule(FilterAddressJob.INTERVAL_KEY_PRESSED);
	}
}
