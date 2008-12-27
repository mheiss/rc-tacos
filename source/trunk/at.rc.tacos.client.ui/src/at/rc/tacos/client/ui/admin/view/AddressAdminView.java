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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.net.handler.AddressHandler;
import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.admin.editors.AddressEditor;
import at.rc.tacos.client.ui.admin.editors.AddressEditorInput;
import at.rc.tacos.client.ui.controller.EditorNewAddressAction;
import at.rc.tacos.client.ui.controller.ImportAddressAction;
import at.rc.tacos.client.ui.jobs.FilterAddressJob;
import at.rc.tacos.client.ui.providers.AddressLabelProvider;
import at.rc.tacos.client.ui.sorterAndTooltip.AddressViewSorter;
import at.rc.tacos.client.ui.utils.CompositeHelper;
import at.rc.tacos.platform.model.Address;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.listeners.DataChangeListener;
import at.rc.tacos.platform.net.mina.MessageIoSession;

public class AddressAdminView extends ViewPart implements DataChangeListener<Address> {

	public static final String ID = "at.rc.tacos.client.view.admin.addressAdminView";

	private Logger log = LoggerFactory.getLogger(AddressAdminView.class);

	// properties
	private TableViewer viewer;
	private FormToolkit toolkit;
	private Form form;
	// text fields for the filter
	private Text zip, city, street;

	// to show some messages
	private CLabel infoLabel;

	/**
	 * The scheduler job to start the filter
	 */
	private FilterAddressJob filterJob;

	// the address handler
	private AddressHandler addressHandler = (AddressHandler) NetWrapper.getHandler(Address.class);

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		toolkit = new FormToolkit(Display.getDefault());
		form = toolkit.createForm(parent);
		form.setText("Liste der Adressen");
		toolkit.decorateFormHeading(form);

		Composite client = form.getBody();
		client.setLayout(new GridLayout());
		client.setLayoutData(new GridData(GridData.FILL_BOTH));

		// create the section to hold the filter
		Composite filter = CompositeHelper.createSection(toolkit, client, "Filter");

		// create the input fields
		final Label labelStreet = toolkit.createLabel(filter, "Straﬂe");
		street = toolkit.createText(filter, "");
		street.addModifyListener(new ModifyListener() {

			public void modifyText(final ModifyEvent e) {
				inputChanged();
			}
		});

		// the city
		final Label labelCity = toolkit.createLabel(filter, "Stadt");
		city = toolkit.createText(filter, "");
		city.addModifyListener(new ModifyListener() {

			public void modifyText(final ModifyEvent e) {
				inputChanged();
			}
		});

		// the zip code
		final Label labelZip = toolkit.createLabel(filter, "GKZ");
		zip = toolkit.createText(filter, "");
		zip.addModifyListener(new ModifyListener() {

			public void modifyText(final ModifyEvent e) {
				inputChanged();
			}
		});

		// create the info label
		infoLabel = new CLabel(filter, SWT.NONE);
		infoLabel.setText("Bitte geben sie mindestens ein Zeichen ein um die Daten vom Server abzurufen");
		infoLabel.setImage(UiWrapper.getDefault().getImageRegistry().get("resource.info"));

		// create the section to hold the table
		Composite tableComp = CompositeHelper.createSection(toolkit, client, "Filter");
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
				Address address = (Address) obj;
				// create the editor input and open
				AddressEditorInput input = new AddressEditorInput(address, false);
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					page.openEditor(input, AddressEditor.ID);
				}
				catch (PartInitException e) {
					log.error("Failed to open the editor for the address " + address, e);
				}
			}
		});
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new AddressLabelProvider());
		viewer.setInput(addressHandler.toArray());
		viewer.getTable().setLinesVisible(true);
		viewer.getTable().setHeaderVisible(true);
		getViewSite().setSelectionProvider(viewer);

		// create the columns
		final TableColumn imageColumn = new TableColumn(table, SWT.NONE);
		imageColumn.setToolTipText("");
		imageColumn.setWidth(30);
		imageColumn.setText("");

		final TableColumn zipColumn = new TableColumn(table, SWT.NONE);
		zipColumn.setToolTipText("Gemeindekennzeichen");
		zipColumn.setWidth(60);
		zipColumn.setText("GKZ");

		final TableColumn cityColumn = new TableColumn(table, SWT.NONE);
		cityColumn.setToolTipText("Name der Stadt");
		cityColumn.setWidth(180);
		cityColumn.setText("Stadt");

		final TableColumn streetColumn = new TableColumn(table, SWT.NONE);
		streetColumn.setToolTipText("Name der Straﬂe");
		streetColumn.setWidth(180);
		streetColumn.setText("Straﬂe");

		// make the columns sortable
		Listener sortListener = new Listener() {

			public void handleEvent(Event e) {
				// determine new sort column and direction
				TableColumn sortColumn = viewer.getTable().getSortColumn();
				TableColumn currentColumn = (TableColumn) e.widget;
				int dir = viewer.getTable().getSortDirection();
				// revert the sortorder if the column is the same
				if (sortColumn == currentColumn) {
					if (dir == SWT.UP)
						dir = SWT.DOWN;
					else
						dir = SWT.UP;
				}
				else {
					viewer.getTable().setSortColumn(currentColumn);
					dir = SWT.UP;
				}
				// sort the data based on column and direction
				String sortIdentifier = null;
				if (currentColumn == zipColumn)
					sortIdentifier = AddressViewSorter.ZIP_SORTER;
				if (currentColumn == cityColumn)
					sortIdentifier = AddressViewSorter.CITY_SORTER;
				if (currentColumn == streetColumn)
					sortIdentifier = AddressViewSorter.STREET_SORTER;
				// apply the filter
				viewer.getTable().setSortDirection(dir);
				viewer.setSorter(new AddressViewSorter(sortIdentifier, dir));
			}
		};

		// attach the listener
		zipColumn.addListener(SWT.Selection, sortListener);
		cityColumn.addListener(SWT.Selection, sortListener);
		streetColumn.addListener(SWT.Selection, sortListener);

		// add actions to the toolbar
		createToolBarActions();

		// set this table as a selection provider
		getViewSite().setSelectionProvider(viewer);

		// set the layout for the composites
		GridData data = new GridData();
		data.widthHint = 80;
		labelStreet.setLayoutData(data);
		data = new GridData();
		data.widthHint = 80;
		labelCity.setLayoutData(data);
		data.widthHint = 80;
		labelZip.setLayoutData(data);
		labelCity.setLayoutData(data);
		data.widthHint = 80;
		// layout for the text fields
		GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
		street.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		city.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		zip.setLayoutData(data2);
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

		// register the listener
		NetWrapper.registerListener(this, Address.class);

		// disable the form if we do not have a admin
		String authorization = NetWrapper.getSession().getLogin().getAuthorization();
		if (!authorization.equalsIgnoreCase("Administrator"))
			form.setEnabled(false);
	}

	@Override
	public void dispose() {
		NetWrapper.removeListener(this, Address.class);
	}

	/**
	 * Passes the focus to the view
	 */
	@Override
	public void setFocus() {
		form.setFocus();
	}

	@Override
	public void dataChanged(Message<Address> message, MessageIoSession messageIoSession) {
		viewer.refresh();
		infoLabel.setText("Es wurden " + message.getObjects().size() + " Addressen gefunden");
		infoLabel.setImage(UiWrapper.getDefault().getImageRegistry().get("resource.info"));
	}

	/**
	 * Creates and adds the actions for the toolbar
	 */
	private void createToolBarActions() {
		// create the action
		EditorNewAddressAction addAction = new EditorNewAddressAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		ImportAddressAction importAction = new ImportAddressAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		// add to the toolbar
		form.getToolBarManager().add(addAction);
		form.getToolBarManager().add(importAction);
		form.getToolBarManager().update(true);
	}

	/**
	 * Helper method to apply the filer
	 */
	public void inputChanged() {
		// get the values
		final String strStreet = street.getText().trim().toLowerCase();
		final String strCity = city.getText().trim().toLowerCase();
		final String strZip = zip.getText().trim().toLowerCase();

		// check the length of the entered text
		if (strStreet.length() < 1 && strCity.length() < 1 && strZip.length() < 1) {
			infoLabel.setText("Bitte geben Sie mindestens ein Zeichen ein.");
			infoLabel.setImage(UiWrapper.getDefault().getImageRegistry().get("resource.error"));
			Display.getCurrent().beep();
			return;
		}

		if (filterJob == null)
			filterJob = new FilterAddressJob(viewer);

		// check the state
		if (filterJob.getState() == Job.RUNNING) {
			System.out.println("Job is currently running");
			return;
		}

		// pass the entered text
		filterJob.setStrCity(strCity);
		filterJob.setStrStreet(strStreet);
		filterJob.setStrZip(strZip);
		filterJob.schedule(FilterAddressJob.INTERVAL_KEY_PRESSED);
	}
}
