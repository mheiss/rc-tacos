package at.rc.tacos.client.ui.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.client.controller.PersonalCancelSignInAction;
import at.rc.tacos.client.controller.PersonalCancelSignOutAction;
import at.rc.tacos.client.controller.PersonalDeleteEntryAction;
import at.rc.tacos.client.controller.PersonalEditEntryAction;
import at.rc.tacos.client.controller.PersonalSignInAction;
import at.rc.tacos.client.controller.PersonalSignOutAction;
import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.net.handler.LockHandler;
import at.rc.tacos.client.net.handler.RosterHandler;
import at.rc.tacos.client.providers.PersonalDateFilter;
import at.rc.tacos.client.providers.PersonalViewContentProvider;
import at.rc.tacos.client.providers.PersonalViewFilter;
import at.rc.tacos.client.providers.PersonalViewLabelProvider;
import at.rc.tacos.client.ui.utils.CustomColors;
import at.rc.tacos.client.view.sorterAndTooltip.PersonalTooltip;
import at.rc.tacos.client.view.sorterAndTooltip.PersonalViewSorter;
import at.rc.tacos.platform.model.Job;
import at.rc.tacos.platform.model.Location;
import at.rc.tacos.platform.model.Lock;
import at.rc.tacos.platform.model.RosterEntry;
import at.rc.tacos.platform.model.ServiceType;
import at.rc.tacos.platform.model.StaffMember;
import at.rc.tacos.platform.model.VehicleDetail;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.MessageType;
import at.rc.tacos.platform.net.listeners.DataChangeListener;
import at.rc.tacos.platform.net.mina.MessageIoSession;

public class PersonalView extends ViewPart implements DataChangeListener<Object> {

	public static final String ID = "at.rc.tacos.client.view.personal_view";

	// the toolkit to use
	private FormToolkit toolkit;
	private Form form;
	private TableViewer viewer;
	private PersonalTooltip tooltip;
	// the tab folder
	private TabFolder tabFolder;

	// the actions for the context menu
	private PersonalCancelSignInAction cancelSignInAction;
	private PersonalCancelSignOutAction cancelSignOutAction;
	private PersonalSignInAction signInAction;
	private PersonalSignOutAction signOutAction;
	private PersonalEditEntryAction editEntryAction;
	private PersonalDeleteEntryAction deleteEntryAction;

	// the model handlers
	private LockHandler lockHandler = (LockHandler) NetWrapper.getHandler(Lock.class);
	private RosterHandler rosterHandler = (RosterHandler) NetWrapper.getHandler(RosterEntry.class);

	/**
	 * Constructs a new personal view and register the listeners
	 */
	public PersonalView() {
		NetWrapper.registerListener(this, RosterEntry.class);
		NetWrapper.registerListener(this, Location.class);
		NetWrapper.registerListener(this, VehicleDetail.class);
		NetWrapper.registerListener(this, Lock.class);
		NetWrapper.registerListener(this, StaffMember.class);
		NetWrapper.registerListener(this, ServiceType.class);
		NetWrapper.registerListener(this, Job.class);
	}

	/**
	 * Cleanup the view and remove the listeners
	 */
	@Override
	public void dispose() {
		NetWrapper.removeListener(this, RosterEntry.class);
		NetWrapper.removeListener(this, Location.class);
		NetWrapper.removeListener(this, VehicleDetail.class);
		NetWrapper.removeListener(this, Lock.class);
		NetWrapper.removeListener(this, StaffMember.class);
		NetWrapper.removeListener(this, ServiceType.class);
		NetWrapper.removeListener(this, Job.class);
	}

	/**
	 * Callback method to create the control and initalize them.
	 * 
	 * @param parent
	 *            the parent composite to add
	 */
	@Override
	public void createPartControl(final Composite parent) {
		// Create the scrolled parent component
		toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
		form = toolkit.createForm(parent);
		toolkit.decorateFormHeading(form);
		form.getBody().setLayout(new FillLayout());

		final Composite composite = form.getBody();

		// tab folder
		tabFolder = new TabFolder(composite, SWT.NONE);
		tabFolder.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				// assert valid
				if (e.item.getData() == null)
					return;
				if (!(e.item.getData() instanceof Location))
					return;

				// remove all location filter
				for (ViewerFilter filter : viewer.getFilters()) {
					if (filter instanceof PersonalViewFilter)
						viewer.removeFilter(filter);
				}

				// cast to a location and apply the new filter
				Location location = (Location) e.item.getData();
				viewer.addFilter(new PersonalViewFilter(location));
			}
		});

		viewer = new TableViewer(tabFolder, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		viewer.setContentProvider(new PersonalViewContentProvider());
		viewer.setLabelProvider(new PersonalViewLabelProvider());
		viewer.setInput(rosterHandler.toArray());
		viewer.getTable().setLinesVisible(true);

		// set the tooltip
		tooltip = new PersonalTooltip(viewer.getControl());
		// show the tooltip when the selection has changed
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				TableItem[] selection = viewer.getTable().getSelection();
				if (selection != null && selection.length > 0) {
					Rectangle bounds = selection[0].getBounds();
					tooltip.show(new Point(bounds.x, bounds.y));
				}
			}
		});

		viewer.getTable().addMouseListener(new MouseAdapter() {

			public void mouseDown(MouseEvent e) {
				if (viewer.getTable().getItem(new Point(e.x, e.y)) == null) {
					viewer.setSelection(new StructuredSelection());
				}
			}
		});
		// sort the table by default
		viewer.setSorter(new PersonalViewSorter(PersonalViewSorter.NAME_SORTER, SWT.UP));
		viewer.addFilter(new PersonalDateFilter(Calendar.getInstance()));

		// create the table for the roster entries
		final Table table = viewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		final TableColumn lockColumn = new TableColumn(table, SWT.NONE);
		lockColumn.setToolTipText("Eintrag wird gerade bearbeitet");
		lockColumn.setWidth(24);
		lockColumn.setText("L");

		final TableColumn columnStandby = new TableColumn(table, SWT.NONE);
		columnStandby.setToolTipText("Mitarbeiter auf Bereitschaft (Symbol, wenn der Fall)");
		columnStandby.setWidth(30);
		columnStandby.setText("B");

		final TableColumn columnNotes = new TableColumn(table, SWT.NONE);
		columnNotes.setToolTipText("Anmerkung (Symbol, wenn Anmerkung vorhanden)");
		columnNotes.setWidth(30);
		columnNotes.setText("A");

		final TableColumn columnStaffName = new TableColumn(table, SWT.NONE);
		columnStaffName.setWidth(130);
		columnStaffName.setText("Name");

		final TableColumn columnWorkTime = new TableColumn(table, SWT.NONE);
		columnWorkTime.setToolTipText("Dienst lt. Dienstplan");
		columnWorkTime.setWidth(120);
		columnWorkTime.setText("Dienst");

		final TableColumn columnCheckin = new TableColumn(table, SWT.NONE);
		columnCheckin.setToolTipText("Zeit der tatsächlichen Anmeldung");
		columnCheckin.setWidth(70);
		columnCheckin.setText("Anm");

		final TableColumn columnCheckout = new TableColumn(table, SWT.NONE);
		columnCheckout.setToolTipText("Zeit der tatsächlichen Abmeldung");
		columnCheckout.setWidth(70);
		columnCheckout.setText("Abm");

		final TableColumn columnService = new TableColumn(table, SWT.NONE);
		columnService.setToolTipText("Dienstverhältnis");
		columnService.setWidth(90);
		columnService.setText("DV");

		final TableColumn columnJob = new TableColumn(table, SWT.NONE);
		columnJob.setToolTipText("Verwendung");
		columnJob.setWidth(80);
		columnJob.setText("V");

		final TableColumn columnVehicle = new TableColumn(table, SWT.NONE);
		columnVehicle.setToolTipText("Fahrzeug, dem der Mitarbeiter zugewiesen ist");
		columnVehicle.setWidth(55);
		columnVehicle.setText("Fzg");

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
				if (currentColumn == columnStaffName)
					sortIdentifier = PersonalViewSorter.NAME_SORTER;
				if (currentColumn == columnWorkTime)
					sortIdentifier = PersonalViewSorter.WORKTIME_SORTER;
				if (currentColumn == columnCheckin)
					sortIdentifier = PersonalViewSorter.CHECKIN_SORTER;
				if (currentColumn == columnCheckout)
					sortIdentifier = PersonalViewSorter.CHECKOUT_SORTER;
				if (currentColumn == columnService)
					sortIdentifier = PersonalViewSorter.SERVICE_SORTER;
				if (currentColumn == columnJob)
					sortIdentifier = PersonalViewSorter.JOB_SORTER;
				// apply the filter
				viewer.getTable().setSortDirection(dir);
				viewer.setSorter(new PersonalViewSorter(sortIdentifier, dir));
			}
		};

		// attach the listener
		columnStaffName.addListener(SWT.Selection, sortListener);
		columnWorkTime.addListener(SWT.Selection, sortListener);
		columnCheckin.addListener(SWT.Selection, sortListener);
		columnCheckout.addListener(SWT.Selection, sortListener);
		columnService.addListener(SWT.Selection, sortListener);
		columnJob.addListener(SWT.Selection, sortListener);

		// create the actions
		makeActions();
		hookContextMenu();
	}

	/**
	 * Creates the needed actions
	 */
	private void makeActions() {
		cancelSignInAction = new PersonalCancelSignInAction(this.viewer);
		cancelSignOutAction = new PersonalCancelSignOutAction(this.viewer);
		signInAction = new PersonalSignInAction(this.viewer);
		signOutAction = new PersonalSignOutAction(this.viewer);
		editEntryAction = new PersonalEditEntryAction(this.viewer);
		deleteEntryAction = new PersonalDeleteEntryAction(this.viewer);
	}

	/**
	 * Creates the context menue
	 */
	private void hookContextMenu() {
		MenuManager menuManager = new MenuManager("#PersonalPopupMenu");
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				fillContextMenu(manager);
			}
		});
		Menu menu = menuManager.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuManager, viewer);
	}

	/**
	 * Fills the context menu with the actions
	 */
	private void fillContextMenu(IMenuManager manager) {
		// get the selected object
		final Object firstSelectedObject = ((IStructuredSelection) viewer.getSelection()).getFirstElement();

		// cast to a RosterEntry
		RosterEntry entry = (RosterEntry) firstSelectedObject;

		if (entry == null)
			return;

		// add the actions
		manager.add(signInAction);
		manager.add(signOutAction);
		manager.add(new Separator());
		manager.add(editEntryAction);
		manager.add(deleteEntryAction);
		manager.add(new Separator());
		manager.add(cancelSignInAction);
		manager.add(cancelSignOutAction);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));

		// default
		signInAction.setEnabled(true);
		signOutAction.setEnabled(true);
		deleteEntryAction.setEnabled(true);
		cancelSignInAction.setEnabled(true);
		cancelSignOutAction.setEnabled(true);

		// enable or disable the actions
		if (entry.getRealStartOfWork() > 0) {
			signInAction.setEnabled(false);
			cancelSignInAction.setEnabled(true);
		}
		else {
			signInAction.setEnabled(true);
			cancelSignInAction.setEnabled(false);
		}
		if (entry.getRealEndOfWork() > 0) {
			signOutAction.setEnabled(false);
			cancelSignOutAction.setEnabled(true);
		}
		else {
			signOutAction.setEnabled(true);
			cancelSignOutAction.setEnabled(false);
		}

		// disable actions if the vehicle is locked
		if (lockHandler.containsLock(entry.getRosterId(), RosterEntry.class)) {
			signInAction.setEnabled(false);
			signOutAction.setEnabled(false);
			deleteEntryAction.setEnabled(false);
			cancelSignInAction.setEnabled(false);
			cancelSignOutAction.setEnabled(false);
		}
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
	}

	@Override
	public void dataChanged(Message<Object> message, MessageIoSession messageIoSession) {
		Object object = message.getFirstElement();
		if (object instanceof Location) {
			List<Location> locations = new ArrayList<Location>();
			for (Object locationObject : message.getObjects()) {
				locations.add((Location) locationObject);
			}
			locationChanged(locations, message.getMessageType());
			return;
		}
		// add the viewer in the other cases
		viewer.refresh();
	}

	/**
	 * Helper method to update the location
	 */
	private void locationChanged(List<Location> locations, MessageType messageType) {
		switch (messageType) {
			case ADD:
				for (Location location : locations) {
					// create a new tab item
					TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
					tabItem.setText(location.getLocationName());
					// Store the location
					tabItem.setData(location);
					tabItem.setControl(viewer.getTable());
					tabFolder.setSelection(1);
					tabFolder.setSelection(0);
					// set the default filter to the first location
					if (location.getId() == 1) {
						viewer.addFilter(new PersonalViewFilter(location));
					}
				}
				break;
			case UPDATE:
				for (Location location : locations) {
					// loop and update the location in the tab folder
					for (TabItem tabItem : tabFolder.getItems()) {
						// get the location out of the tab
						Location tabLocation = (Location) tabItem.getData();
						// check if we have the location
						if (!tabLocation.equals(location)) {
							continue;
						}
						// store the new location in the data and update the tab
						tabItem.setData(location);
						tabItem.setText(location.getLocationName());
					}
				}
				break;
			case REMOVE:
				for (Location location : locations) {
					// loop and remove the location
					for (TabItem tabItem : tabFolder.getItems()) {
						// get the location out of the tab
						Location tabLocation = (Location) tabItem.getData();
						// check if we have the tab and dispose it
						if (tabLocation.equals(location))
							tabItem.dispose();
					}
				}
				break;
		}
	}
}
