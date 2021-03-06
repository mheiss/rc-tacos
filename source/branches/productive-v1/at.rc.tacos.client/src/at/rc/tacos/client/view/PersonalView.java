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
package at.rc.tacos.client.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;

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
import at.rc.tacos.client.modelManager.LockManager;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.PersonalDateFilter;
import at.rc.tacos.client.providers.PersonalViewContentProvider;
import at.rc.tacos.client.providers.PersonalViewFilter;
import at.rc.tacos.client.providers.PersonalViewLabelProvider;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.client.view.sorterAndTooltip.PersonalTooltip;
import at.rc.tacos.client.view.sorterAndTooltip.PersonalViewSorter;
import at.rc.tacos.model.Job;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.ServiceType;
import at.rc.tacos.model.StaffMember;

public class PersonalView extends ViewPart implements PropertyChangeListener {

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

	// the lock manager
	private LockManager lockManager = ModelFactory.getInstance().getLockManager();

	/**
	 * Constructs a new personal view.
	 */
	public PersonalView() {
		// add listener to model to keep on track.
		ModelFactory.getInstance().getRosterEntryManager().addPropertyChangeListener(this);
		ModelFactory.getInstance().getLocationManager().addPropertyChangeListener(this);
		ModelFactory.getInstance().getVehicleManager().addPropertyChangeListener(this);
		ModelFactory.getInstance().getLockManager().addPropertyChangeListener(this);
		// listen to changes of jobs, serviceTypes and staff member updates
		ModelFactory.getInstance().getStaffManager().addPropertyChangeListener(this);
		ModelFactory.getInstance().getServiceManager().addPropertyChangeListener(this);
		ModelFactory.getInstance().getJobList().addPropertyChangeListener(this);
	}

	/**
	 * Cleanup the view
	 */
	@Override
	public void dispose() {
		ModelFactory.getInstance().getRosterEntryManager().removePropertyChangeListener(this);
		ModelFactory.getInstance().getLocationManager().removePropertyChangeListener(this);
		ModelFactory.getInstance().getVehicleManager().removePropertyChangeListener(this);
		ModelFactory.getInstance().getLockManager().removePropertyChangeListener(this);
		// remove again
		ModelFactory.getInstance().getStaffManager().removePropertyChangeListener(this);
		ModelFactory.getInstance().getServiceManager().removePropertyChangeListener(this);
		ModelFactory.getInstance().getJobList().removePropertyChangeListener(this);
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
		viewer.setInput(ModelFactory.getInstance().getRosterEntryManager().toArray());
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

		// load the data
		loadData();
	}

	/**
	 * Loads the data from the model
	 */
	public void loadData() {
		ModelFactory.getInstance().getLocationManager().initViews(this);
		ModelFactory.getInstance().getVehicleManager().initViews(this);
		ModelFactory.getInstance().getRosterEntryManager().initViews(this);
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
		if (lockManager.containsLock(RosterEntry.ID, entry.getRosterId())) {
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

	public void propertyChange(PropertyChangeEvent evt) {
		// add a new tab item to the TabFolder
		if ("LOCATION_ADD".equalsIgnoreCase(evt.getPropertyName())) {
			// get the new location
			Location location = (Location) evt.getNewValue();
			// create a new tab item
			TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
			tabItem.setText(location.getLocationName());
			// Store the location
			tabItem.setData(location);
			tabItem.setControl(viewer.getTable());
			tabFolder.setSelection(1);
			tabFolder.setSelection(0);

			// set the default filter to the first location
			if (location.getId() == 1)
				viewer.addFilter(new PersonalViewFilter(location));
		}
		// update the TabItem
		if ("LOCATION_UPDATE".equalsIgnoreCase(evt.getPropertyName())) {
			// the updated location
			Location updatedLocation = (Location) evt.getNewValue();

			// loop and update the location in the tab folder
			for (TabItem tabItem : tabFolder.getItems()) {
				// get the location out of the tab
				Location tabLocation = (Location) tabItem.getData();
				// check if we have the location
				if (tabLocation.equals(updatedLocation)) {
					// store the new location in the data and update the tab
					// text
					tabItem.setData(updatedLocation);
					tabItem.setText(updatedLocation.getLocationName());
				}
			}
		}
		// remove a specific location
		if ("LOCATION_REMOVE".equalsIgnoreCase(evt.getPropertyName())) {
			// get the removed location
			Location removedLocation = (Location) evt.getOldValue();

			// loop and remove the location
			for (TabItem tabItem : tabFolder.getItems()) {
				// get the location out of the tab
				Location tabLocation = (Location) tabItem.getData();
				// check if we have the tab and dispose it
				if (tabLocation.equals(removedLocation))
					tabItem.dispose();
			}
		}

		// clear the locations
		if ("LOCATION_CLEARED".equalsIgnoreCase(evt.getPropertyName())) {
			// loop and remove all tabs
			for (TabItem tabItem : tabFolder.getItems())
				tabItem.dispose();
		}

		// add the new element to the viewer
		if ("ROSTERENTRY_ADD".equals(evt.getPropertyName())) {
			// get the new added entry
			RosterEntry entry = (RosterEntry) evt.getNewValue();
			viewer.add(entry);
		}
		// update the existing element
		if ("ROSTERENTRY_UPDATE".equals(evt.getPropertyName())) {
			// get the updated element
			RosterEntry entry = (RosterEntry) evt.getNewValue();
			viewer.refresh(entry, true);
		}
		// remove the single entry
		if ("ROSTERENTRY_REMOVE".equals(evt.getPropertyName())) {
			// get the removed element
			RosterEntry entry = (RosterEntry) evt.getOldValue();
			viewer.remove(entry);
		}
		// refresh the complete table
		if ("ROSTERENTRY_CLEARED".equals(evt.getPropertyName())) {
			viewer.refresh();
		}

		// update the staff member when it is changed
		if ("STAFF_UPDATE".equalsIgnoreCase(evt.getPropertyName()) || "SERVICETYPE_UPDATE".equalsIgnoreCase(evt.getPropertyName())
				|| "JOB_UPDATE".equalsIgnoreCase(evt.getPropertyName())) {
			// the three types
			Object updatedObject = evt.getNewValue();
			StaffMember updatedMember = null;
			Job updatedJob = null;
			ServiceType updatedService = null;

			// check the type
			if (updatedObject instanceof StaffMember)
				updatedMember = (StaffMember) updatedObject;
			if (updatedObject instanceof Job)
				updatedJob = (Job) updatedObject;
			if (updatedObject instanceof ServiceType)
				updatedService = (ServiceType) updatedObject;
			// loop over each roster entry
			for (RosterEntry entry : ModelFactory.getInstance().getRosterEntryManager().getRosterList()) {
				if (updatedMember != null && entry.getStaffMember().equals(updatedMember))
					entry.setStaffMember(updatedMember);
				if (updatedJob != null && entry.getJob().equals(updatedJob))
					entry.setJob(updatedJob);
				if (updatedService != null && entry.getServicetype().equals(updatedService))
					entry.setServicetype(updatedService);
				// update the entry
			}
			viewer.refresh();
		}
		// update the assigned vehicle of the staff member
		if ("VEHICLE_ADD".equalsIgnoreCase(evt.getPropertyName()) || "VEHICLE_UPDATE".equalsIgnoreCase(evt.getPropertyName())
				|| "VEHICLE_CLEAR".equalsIgnoreCase(evt.getPropertyName()) || "VEHICLE_REMOVE".equalsIgnoreCase(evt.getPropertyName())
				|| "VEHICLE_ADD_ALL".equalsIgnoreCase(evt.getPropertyName())) {
			viewer.refresh();
		}

		// listen to changes of the date to set up the filter
		if ("ROSTER_DATE_CHANGED".equalsIgnoreCase(evt.getPropertyName())) {
			Calendar newDate = (Calendar) evt.getNewValue();

			// remove all date filter
			for (ViewerFilter filter : viewer.getFilters()) {
				if (filter instanceof PersonalDateFilter)
					viewer.removeFilter(filter);
			}
			// apply the new date filter
			viewer.addFilter(new PersonalDateFilter(newDate));
		}
		// listen to lock changes
		if ("LOCK_ADD".equalsIgnoreCase(evt.getPropertyName()) || "LOCK_REMOVE".equalsIgnoreCase(evt.getPropertyName())) {
			viewer.refresh();
		}
	}
}
