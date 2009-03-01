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

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.client.controller.VehicleTableAtStationAction;
import at.rc.tacos.client.controller.VehicleTableDetachAllStaffMembersAction;
import at.rc.tacos.client.controller.VehicleTableEditAction;
import at.rc.tacos.client.controller.VehicleTableSetReadyAction;
import at.rc.tacos.client.controller.VehicleTableSetRepairStatusAction;
import at.rc.tacos.client.modelManager.LockManager;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.VehicleContentProvider;
import at.rc.tacos.client.providers.VehicleViewTableLabelProvider;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.client.view.sorterAndTooltip.VehicleTooltip;
import at.rc.tacos.client.view.sorterAndTooltip.VehicleViewTableSorter;
import at.rc.tacos.model.VehicleDetail;

public class VehiclesViewTable extends ViewPart implements PropertyChangeListener {

	public static final String ID = "at.rc.tacos.client.view.vehiclestable_view";

	// the toolkit to use
	private FormToolkit toolkit;
	private Form form;
	private TableViewer viewer;
	private VehicleTooltip tooltip;

	// the actions for the context menu
	private VehicleTableEditAction editAction;
	private VehicleTableDetachAllStaffMembersAction detachAction;
	private VehicleTableSetReadyAction readyStatus;
	private VehicleTableSetRepairStatusAction repairStatus;
	private VehicleTableAtStationAction vehicleAtStationAction;

	// the lock manager
	private LockManager lockManager = ModelFactory.getInstance().getLockManager();

	/**
	 * Constructs a new vehicle view.
	 */
	public VehiclesViewTable() {
		// add listener to model to keep on track.
		ModelFactory.getInstance().getRosterEntryManager().addPropertyChangeListener(this);
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
		viewer = new TableViewer(composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		viewer.setContentProvider(new VehicleContentProvider());
		viewer.setLabelProvider(new VehicleViewTableLabelProvider());
		viewer.setInput(ModelFactory.getInstance().getVehicleManager().toArray());
		viewer.getTable().setLinesVisible(true);

		viewer.refresh();

		// set the tooltip
		tooltip = new VehicleTooltip(viewer.getControl());
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
		viewer.setSorter(new VehicleViewTableSorter(VehicleViewTableSorter.VEHICLE_SORTER, SWT.UP));

		// create the table for the vehicles
		final Table table = viewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		final TableColumn columnLock = new TableColumn(table, SWT.NONE);
		columnLock.setToolTipText("Gesperrt");
		columnLock.setWidth(24);
		columnLock.setText("L");

		final TableColumn columnVehicleName = new TableColumn(table, SWT.NONE);
		columnVehicleName.setToolTipText("Fahrzeugname");
		columnVehicleName.setWidth(60);

		final TableColumn columnVehicleStatus = new TableColumn(table, SWT.NONE);
		columnVehicleStatus.setToolTipText("Verfügbarkeit des Fahrzeuges");
		columnVehicleStatus.setWidth(50);

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
				if (currentColumn == columnVehicleStatus)
					sortIdentifier = VehicleViewTableSorter.STATUS_SORTER;
				if (currentColumn == columnVehicleName)
					sortIdentifier = VehicleViewTableSorter.VEHICLE_SORTER;

				// apply the filter
				viewer.getTable().setSortDirection(dir);
				viewer.setSorter(new VehicleViewTableSorter(sortIdentifier, dir));
			}
		};

		// attach the listener
		columnVehicleName.addListener(SWT.Selection, sortListener);
		columnVehicleStatus.addListener(SWT.Selection, sortListener);

		// create the actions
		makeActions();
		hookContextMenu();

		viewer.refresh();
	}

	/**
	 * Creates the needed actions
	 */
	private void makeActions() {

		editAction = new VehicleTableEditAction(this.viewer);
		detachAction = new VehicleTableDetachAllStaffMembersAction(this.viewer);
		readyStatus = new VehicleTableSetReadyAction(this.viewer);
		repairStatus = new VehicleTableSetRepairStatusAction(this.viewer);
		vehicleAtStationAction = new VehicleTableAtStationAction(this.viewer);
	}

	/**
	 * Creates the context menu
	 */
	private void hookContextMenu() {
		MenuManager menuManager = new MenuManager("#PopupMenu");
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

		// cast to a vehicle detail
		VehicleDetail vehicle = (VehicleDetail) firstSelectedObject;

		if (vehicle == null)
			return;

		// add the actions
		manager.add(editAction);
		manager.add(detachAction);
		manager.add(new Separator());
		manager.add(vehicleAtStationAction);
		manager.add(new Separator());
		manager.add(readyStatus);
		manager.add(repairStatus);

		// default
		detachAction.setEnabled(true);
		vehicleAtStationAction.setEnabled(true);
		readyStatus.setEnabled(true);
		repairStatus.setEnabled(true);

		// enable or disable the actions
		if (vehicle.isReadyForAction()) {
			readyStatus.setEnabled(false);
		}
		else
			readyStatus.setEnabled(true);
		if (vehicle.isOutOfOrder()) {
			readyStatus.setEnabled(false);
			repairStatus.setEnabled(false);
		}
		else
			repairStatus.setEnabled(true);

		if (lockManager.containsLock(VehicleDetail.ID, vehicle.getVehicleName())) {
			detachAction.setEnabled(false);
			vehicleAtStationAction.setEnabled(false);
			readyStatus.setEnabled(false);
			repairStatus.setEnabled(false);
		}
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
	}

	public void propertyChange(PropertyChangeEvent evt) {
		// the viewer represents simple model. refresh should be enough.
		if ("VEHICLE_ADD".equals(evt.getPropertyName()) || "VEHICLE_ADD_ALL".equalsIgnoreCase(evt.getPropertyName())
				|| "VEHICLE_REMOVE".equals(evt.getPropertyName()) || "VEHICLE_UPDATE".equals(evt.getPropertyName())
				|| "VEHICLE_CLEARED".equals(evt.getPropertyName())) {
			viewer.refresh();
		}

		// listen to lock changes
		if ("LOCK_ADD".equalsIgnoreCase(evt.getPropertyName()) || "LOCK_REMOVE".equalsIgnoreCase(evt.getPropertyName())) {
			viewer.refresh();
		}
	}
}
