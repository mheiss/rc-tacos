package at.rc.tacos.client.ui.view;

import java.beans.PropertyChangeEvent;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.net.handler.VehicleHandler;
import at.rc.tacos.client.ui.ViewerConstants;
import at.rc.tacos.client.ui.controller.RefreshViewAction;
import at.rc.tacos.client.ui.controller.VehicleTableAtStationAction;
import at.rc.tacos.client.ui.controller.VehicleTableDetachAllStaffMembersAction;
import at.rc.tacos.client.ui.controller.VehicleTableEditAction;
import at.rc.tacos.client.ui.controller.VehicleTableSetReadyAction;
import at.rc.tacos.client.ui.controller.VehicleTableSetRepairStatusAction;
import at.rc.tacos.client.ui.providers.HandlerContentProvider;
import at.rc.tacos.client.ui.providers.VehicleViewTableDetailLabelProvider;
import at.rc.tacos.client.ui.sorterAndTooltip.VehicleViewTableSorter;
import at.rc.tacos.platform.model.Job;
import at.rc.tacos.platform.model.RosterEntry;
import at.rc.tacos.platform.model.ServiceType;
import at.rc.tacos.platform.model.StaffMember;
import at.rc.tacos.platform.model.VehicleDetail;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.message.GetMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;

public class VehiclesViewTableDetailed extends AbstractView {

	public static final String ID = "at.rc.tacos.client.view.vehiclestabledetailed_view";

	private TableViewer viewer;

	// the actions for the context menu
	private VehicleTableEditAction editAction;
	private VehicleTableDetachAllStaffMembersAction detachAction;
	private VehicleTableSetReadyAction readyStatus;
	private VehicleTableSetRepairStatusAction repairStatus;
	private VehicleTableAtStationAction vehicleAtStationAction;

	// the managers
	private VehicleHandler vehicleHandler = (VehicleHandler) NetWrapper.getHandler(VehicleDetail.class);

	@Override
	public void addListeners() {
		NetWrapper.registerListener(this, RosterEntry.class);
		NetWrapper.registerListener(this, VehicleDetail.class);
		NetWrapper.registerListener(this, StaffMember.class);
		NetWrapper.registerListener(this, ServiceType.class);
		NetWrapper.registerListener(this, Job.class);
	}

	@Override
	public void removeListeners() {
		NetWrapper.removeListener(this, RosterEntry.class);
		NetWrapper.removeListener(this, VehicleDetail.class);
		NetWrapper.removeListener(this, StaffMember.class);
		NetWrapper.removeListener(this, ServiceType.class);
		NetWrapper.removeListener(this, Job.class);
	}

	@Override
	public void createPartBody(Composite body) {
		FieldDecoration decoration = new FieldDecoration(null, "Fahrzeug�bersicht");
		decorateView(decoration);

		// setup the layout and initialize the controls
		body.setLayout(new FillLayout());
		viewer = new TableViewer(body, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		viewer.setContentProvider(new HandlerContentProvider());
		viewer.setLabelProvider(new VehicleViewTableDetailLabelProvider());
		viewer.setInput(vehicleHandler);
		viewer.getTable().setLinesVisible(true);
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
		columnLock.setWidth(ViewerConstants.CHAR_WIDTH);
		columnLock.setText("L");

		final TableColumn columnReady = new TableColumn(table, SWT.NONE);
		columnReady.setToolTipText("Einsatzbereit");
		columnReady.setWidth(ViewerConstants.ICON_WIDTH);
		columnReady.setText("EB");

		final TableColumn columnVehicleName = new TableColumn(table, SWT.NONE);
		columnVehicleName.setToolTipText("Fahrzeugname");
		columnVehicleName.setWidth(ViewerConstants.SHORT_TEXT_WIDTH);

		final TableColumn columnVehicleStatus = new TableColumn(table, SWT.NONE);
		columnVehicleStatus.setToolTipText("Verf�gbarkeit des Fahrzeuges");
		columnVehicleStatus.setWidth(ViewerConstants.ICON_WIDTH);

		final TableColumn columnDriver = new TableColumn(table, SWT.NONE);
		columnDriver.setToolTipText("Fahrer");
		columnDriver.setWidth(ViewerConstants.MEDIUM_TEXT_WIDTH);
		columnDriver.setText("Fahrer");

		final TableColumn columnMedicI = new TableColumn(table, SWT.NONE);
		columnMedicI.setToolTipText("Sanit�ter I");
		columnMedicI.setWidth(ViewerConstants.MEDIUM_TEXT_WIDTH);
		columnMedicI.setText("Sanit�ter I");

		final TableColumn columnMedicII = new TableColumn(table, SWT.NONE);
		columnMedicII.setToolTipText("Sanit�ter");
		columnMedicII.setWidth(ViewerConstants.MEDIUM_TEXT_WIDTH);
		columnMedicII.setText("Sanit�ter II");

		final TableColumn columnPhone = new TableColumn(table, SWT.NONE);
		columnPhone.setToolTipText("Anderes als prim�res Handy");
		columnPhone.setWidth(ViewerConstants.ICON_WIDTH);

		final TableColumn columnStation = new TableColumn(table, SWT.NONE);
		columnStation.setToolTipText("Andere als prim�res Ortsstelle");
		columnStation.setWidth(ViewerConstants.ICON_WIDTH);

		final TableColumn columnOutOfOrder = new TableColumn(table, SWT.NONE);
		columnOutOfOrder.setToolTipText("Fahrzeug ist au�er Dienst");
		columnOutOfOrder.setWidth(ViewerConstants.ICON_WIDTH);

		final TableColumn columnNotes = new TableColumn(table, SWT.NONE);
		columnNotes.setToolTipText("Notizen zum Fahrzeug vorhanden");
		columnNotes.setWidth(ViewerConstants.ICON_WIDTH);

		final TableColumn columnLastDestinationFree = new TableColumn(table, SWT.NONE);
		columnLastDestinationFree.setToolTipText("Zeigt den Standort der letzten Meldung \"Ziel frei\" (S6)an");
		columnLastDestinationFree.setWidth(ViewerConstants.LONG_TEXT_WIDTH);
		columnLastDestinationFree.setText("Letzter Status S5");

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
				if (currentColumn == columnDriver)
					sortIdentifier = VehicleViewTableSorter.DRIVER_SORTER;
				if (currentColumn == columnMedicI)
					sortIdentifier = VehicleViewTableSorter.PARAMEDIC_I_SORTER;
				if (currentColumn == columnMedicII)
					sortIdentifier = VehicleViewTableSorter.PARAMEDIC_II_SORTER;
				if (currentColumn == columnNotes)
					sortIdentifier = VehicleViewTableSorter.NOTES_SORTER;
				if (currentColumn == columnLastDestinationFree)
					sortIdentifier = VehicleViewTableSorter.LDF_SORTER;

				// apply the filter
				viewer.getTable().setSortDirection(dir);
				viewer.setSorter(new VehicleViewTableSorter(sortIdentifier, dir));
			}
		};

		// attach the listener
		columnVehicleName.addListener(SWT.Selection, sortListener);
		columnVehicleStatus.addListener(SWT.Selection, sortListener);
		columnDriver.addListener(SWT.Selection, sortListener);
		columnMedicI.addListener(SWT.Selection, sortListener);
		columnMedicII.addListener(SWT.Selection, sortListener);
		columnNotes.addListener(SWT.Selection, sortListener);
		columnLastDestinationFree.addListener(SWT.Selection, sortListener);

		// create the actions
		makeActions();
		hookContextMenu();
		createToolBarActions();

		// initialize the view with current data
		initView();
	}

	/**
	 * Helper method to initialize the view
	 */
	private void initView() {
		viewer.refresh(true);
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

		//the staff of the vehicle
		boolean hasDriver = vehicle.getDriver() == null ? false : true;
		boolean hasMedic1 = vehicle.getFirstParamedic() == null ? false : true;
		boolean hasMedic2 = vehicle.getSecondParamedic() == null ? false : true;

		// default
		detachAction.setEnabled(hasDriver || hasMedic1 || hasMedic2);
		vehicleAtStationAction.setEnabled(true);
		readyStatus.setEnabled(hasDriver);
		repairStatus.setEnabled(true);

		// ready for action
		if (vehicle.isReadyForAction()) {
			readyStatus.setEnabled(false);
		}

		// out of order actions
		if (vehicle.isOutOfOrder()) {
			readyStatus.setEnabled(false);
			repairStatus.setEnabled(false);
		}

		if (vehicle.isLocked()) {
			detachAction.setEnabled(false);
			vehicleAtStationAction.setEnabled(false);
			readyStatus.setEnabled(false);
			repairStatus.setEnabled(false);
		}
	}

	/**
	 * Creates and adds the actions for the toolbar
	 */
	private void createToolBarActions() {
		// create the action
		GetMessage<VehicleDetail> getMessage = new GetMessage<VehicleDetail>(new VehicleDetail());

		// add to the toolbar
		form.getToolBarManager().add(new RefreshViewAction<VehicleDetail>(getMessage));
		form.getToolBarManager().update(true);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// not needed
	}

	@Override
	public void dataChanged(Message<Object> message, MessageIoSession messageIoSession) {
		viewer.refresh();
	}
}
