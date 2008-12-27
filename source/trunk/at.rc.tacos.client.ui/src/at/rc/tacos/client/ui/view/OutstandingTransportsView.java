package at.rc.tacos.client.ui.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.net.handler.TransportHandler;
import at.rc.tacos.client.net.handler.VehicleHandler;
import at.rc.tacos.client.ui.ListenerConstants;
import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.ViewerConstants;
import at.rc.tacos.client.ui.controller.AssignCarAction;
import at.rc.tacos.client.ui.controller.CancelTransportAction;
import at.rc.tacos.client.ui.controller.CopyTransportAction;
import at.rc.tacos.client.ui.controller.EditTransportAction;
import at.rc.tacos.client.ui.controller.ForwardTransportAction;
import at.rc.tacos.client.ui.controller.RefreshViewAction;
import at.rc.tacos.client.ui.filters.TransportStateViewFilter;
import at.rc.tacos.client.ui.filters.TransportViewFilter;
import at.rc.tacos.client.ui.providers.HandlerContentProvider;
import at.rc.tacos.client.ui.providers.OutstandingTransportsViewLabelProvider;
import at.rc.tacos.client.ui.sorterAndTooltip.TransportSorter;
import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.iface.IProgramStatus;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.model.VehicleDetail;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.listeners.DataChangeListener;
import at.rc.tacos.platform.net.message.GetMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;

public class OutstandingTransportsView extends ViewPart implements DataChangeListener<Transport>, PropertyChangeListener {

	public static final String ID = "at.rc.tacos.client.view.outstandingTransports_view";

	// the toolkit to use
	private FormToolkit toolkit;
	private Form form;
	private TableViewer viewer;

	// the actions for the context menu
	private CopyTransportAction copyTransportAction;
	private ForwardTransportAction forwardTransportAction;
	private CancelTransportAction cancelTransportAction;
	private EditTransportAction editTransportAction;

	// the model handlers
	private TransportHandler transportHandler = (TransportHandler) NetWrapper.getHandler(Transport.class);
	private VehicleHandler vehicleHandler = (VehicleHandler) NetWrapper.getHandler(VehicleDetail.class);

	/**
	 * Cleanup the view and remove the listeners
	 */
	@Override
	public void dispose() {
		NetWrapper.removeListener(this, Transport.class);
		UiWrapper.getDefault().removeListener(this);
		super.dispose();
	}

	/**
	 * Call back method to create the control and initialize them.
	 * 
	 * @param parent
	 *            the parent composite to add
	 */
	@Override
	public void createPartControl(final Composite parent) {
		// Create the parent component
		toolkit = new FormToolkit(Display.getDefault());
		form = toolkit.createForm(parent);
		form.setText("Offene Transporte");
		toolkit.decorateFormHeading(form);

		final Composite composite = form.getBody();
		composite.setLayout(new FillLayout());

		viewer = new TableViewer(composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		viewer.setContentProvider(new HandlerContentProvider());
		viewer.setLabelProvider(new OutstandingTransportsViewLabelProvider());
		viewer.setInput(transportHandler);
		viewer.getTable().setLinesVisible(true);
		viewer.getTable().addMouseListener(new MouseAdapter() {

			public void mouseDown(MouseEvent e) {
				if (viewer.getTable().getItem(new Point(e.x, e.y)) == null) {
					viewer.setSelection(new StructuredSelection());
				}
			}
		});
		// make the actions for the context menu when selection has changed
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			ArrayList<AssignCarAction> actionList = new ArrayList<AssignCarAction>();

			public void selectionChanged(SelectionChangedEvent event) {
				makeActions();
				hookContextMenu();
			}

			/**
			 * Creates the needed actions
			 */
			private void makeActions() {
				forwardTransportAction = new ForwardTransportAction(viewer);
				editTransportAction = new EditTransportAction(viewer, "outstanding");
				cancelTransportAction = new CancelTransportAction(viewer);
				copyTransportAction = new CopyTransportAction(viewer);

				// get the list of all vehicle with the status ready for action
				List<VehicleDetail> readyVehicles = vehicleHandler.getReadyVehicleList();
				actionList.clear();
				for (VehicleDetail veh : readyVehicles) {
					AssignCarAction action = new AssignCarAction(viewer, veh);
					actionList.add(action);
				}
			}

			/**
			 * Creates the context menu
			 */
			private void hookContextMenu() {
				MenuManager menuManager = new MenuManager("#OutstandingPopupMenu");
				menuManager.setRemoveAllWhenShown(true);
				menuManager.addMenuListener(new IMenuListener() {

					public void menuAboutToShow(IMenuManager manager) {
						fillContextMenu(manager);
					}
				});
				Menu menu = menuManager.createContextMenu(viewer.getControl());
				viewer.getControl().setMenu(menu);
			}

			/**
			 * Fills the context menu with the actions
			 */
			private void fillContextMenu(IMenuManager manager) {
				// get the selected transport
				final Object firstSelectedObject = ((IStructuredSelection) viewer.getSelection()).getFirstElement();
				Transport transport = (Transport) firstSelectedObject;
				if (transport == null)
					return;

				// submenu for the available vehicles
				MenuManager menuManagerSub = new MenuManager("Fahrzeug zuweisen");
				manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
				manager.add(menuManagerSub);

				// create a list of ready vehicles and disable the selection if
				// the transport is locked
				for (AssignCarAction ac : actionList) {
					menuManagerSub.add(ac);
					if (transport.isLocked()) {
						ac.setEnabled(false);
					}
				}

				manager.add(new Separator());
				manager.add(editTransportAction);
				manager.add(new Separator());
				manager.add(cancelTransportAction);
				manager.add(forwardTransportAction);
				manager.add(new Separator());
				manager.add(copyTransportAction);

				// if locked disable the actions
				copyTransportAction.setEnabled(!transport.isLocked());
			}
		});

		// set the default sorter
		viewer.setSorter(new TransportSorter(TransportSorter.PRIORITY_SORTER, SWT.UP));

		final Table tableOff = viewer.getTable();
		tableOff.setLinesVisible(true);
		tableOff.setHeaderVisible(true);

		final TableColumn lockColumn = new TableColumn(tableOff, SWT.NONE);
		lockColumn.setToolTipText("Eintrag wird gerade bearbeitet");
		lockColumn.setWidth(ViewerConstants.ICON_WIDTH);
		lockColumn.setText("L");

		final TableColumn prioritaetOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		prioritaetOffeneTransporte
				.setToolTipText("A (NEF), B (BD1), C (Transport), D (Rücktransport), E (Heimtransport), F (Sonstiges), E (NEF extern)");
		prioritaetOffeneTransporte.setWidth(ViewerConstants.CHAR_WIDTH);
		prioritaetOffeneTransporte.setText("Pr");

		final TableColumn respOSOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		respOSOffeneTransporte.setToolTipText("Zuständige Ortsstelle");
		respOSOffeneTransporte.setWidth(ViewerConstants.CHAR_WIDTH);
		respOSOffeneTransporte.setText("OS");

		final TableColumn abfOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		abfOffeneTransporte.setToolTipText("Abfahrt auf der Dienststelle");
		abfOffeneTransporte.setWidth(ViewerConstants.TIME_WIDTH);
		abfOffeneTransporte.setText("Abf");

		final TableColumn ankOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		ankOffeneTransporte.setToolTipText("Ankunft beim Patienten");
		ankOffeneTransporte.setWidth(ViewerConstants.TIME_WIDTH);
		ankOffeneTransporte.setText("Ank.");

		final TableColumn terminOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		terminOffeneTransporte.setToolTipText("Terminzeit am Zielort");
		terminOffeneTransporte.setWidth(ViewerConstants.TIME_WIDTH);
		terminOffeneTransporte.setText("Termin");

		final TableColumn transportVonOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		transportVonOffeneTransporte.setWidth(ViewerConstants.LONG_TEXT_WIDTH);
		transportVonOffeneTransporte.setText("Transport von");

		final TableColumn patientOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		patientOffeneTransporte.setWidth(ViewerConstants.LONG_TEXT_WIDTH);
		patientOffeneTransporte.setText("Patient");

		final TableColumn transportNachOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		transportNachOffeneTransporte.setWidth(ViewerConstants.LONG_TEXT_WIDTH);
		transportNachOffeneTransporte.setText("Transport nach");

		final TableColumn aufgOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		aufgOffeneTransporte.setToolTipText("Zeit zu der der Transport aufgenommen wurde");
		aufgOffeneTransporte.setWidth(ViewerConstants.TIME_WIDTH);
		aufgOffeneTransporte.setText("Aufg");

		final TableColumn tOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		tOffeneTransporte.setToolTipText("Transportart");
		tOffeneTransporte.setWidth(ViewerConstants.CHAR_WIDTH);
		tOffeneTransporte.setText("T");

		final TableColumn erkrankungVerletzungOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		erkrankungVerletzungOffeneTransporte.setWidth(ViewerConstants.MEDIUM_TEXT_WIDTH);
		erkrankungVerletzungOffeneTransporte.setText("Erkrankung/Verletzung");

		final TableColumn anmerkungOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		anmerkungOffeneTransporte.setWidth(ViewerConstants.LONG_TEXT_WIDTH);
		anmerkungOffeneTransporte.setText("Anmerkung");

		// make the columns sortable
		Listener sortListener = new Listener() {

			public void handleEvent(Event e) {
				// determine new sort column and direction
				TableColumn sortColumn = viewer.getTable().getSortColumn();
				TableColumn currentColumn = (TableColumn) e.widget;
				int dir = viewer.getTable().getSortDirection();
				// revert the sort order if the column is the same
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
				if (currentColumn == prioritaetOffeneTransporte)
					sortIdentifier = TransportSorter.PRIORITY_SORTER;
				if (currentColumn == respOSOffeneTransporte)
					sortIdentifier = TransportSorter.RESP_STATION_SORTER;
				if (currentColumn == abfOffeneTransporte)
					sortIdentifier = TransportSorter.ABF_SORTER;
				if (currentColumn == ankOffeneTransporte)
					sortIdentifier = TransportSorter.AT_PATIENT_SORTER;
				if (currentColumn == terminOffeneTransporte)
					sortIdentifier = TransportSorter.TERM_SORTER;
				if (currentColumn == transportVonOffeneTransporte)
					sortIdentifier = TransportSorter.TRANSPORT_FROM_SORTER;
				if (currentColumn == patientOffeneTransporte)
					sortIdentifier = TransportSorter.PATIENT_SORTER;
				if (currentColumn == transportNachOffeneTransporte)
					sortIdentifier = TransportSorter.TRANSPORT_TO_SORTER;
				if (currentColumn == aufgOffeneTransporte)
					sortIdentifier = TransportSorter.AUFG_SORTER;
				if (currentColumn == tOffeneTransporte)
					sortIdentifier = TransportSorter.TA_SORTER;
				if (currentColumn == erkrankungVerletzungOffeneTransporte)
					sortIdentifier = TransportSorter.KIND_OF_ILLNESS_SORTER;
				if (currentColumn == anmerkungOffeneTransporte)
					sortIdentifier = TransportSorter.NOTES_SORTER;

				// apply the filter
				viewer.getTable().setSortDirection(dir);
				viewer.setSorter(new TransportSorter(sortIdentifier, dir));
			}
		};

		// attach the listener
		prioritaetOffeneTransporte.addListener(SWT.Selection, sortListener);
		respOSOffeneTransporte.addListener(SWT.Selection, sortListener);
		abfOffeneTransporte.addListener(SWT.Selection, sortListener);
		ankOffeneTransporte.addListener(SWT.Selection, sortListener);
		terminOffeneTransporte.addListener(SWT.Selection, sortListener);
		transportVonOffeneTransporte.addListener(SWT.Selection, sortListener);
		patientOffeneTransporte.addListener(SWT.Selection, sortListener);
		transportNachOffeneTransporte.addListener(SWT.Selection, sortListener);
		aufgOffeneTransporte.addListener(SWT.Selection, sortListener);
		tOffeneTransporte.addListener(SWT.Selection, sortListener);
		erkrankungVerletzungOffeneTransporte.addListener(SWT.Selection, sortListener);
		anmerkungOffeneTransporte.addListener(SWT.Selection, sortListener);

		// add the form actions
		createToolBarActions();

		// apply the filter to show only outstanding transports
		viewer.addFilter(new TransportStateViewFilter(IProgramStatus.PROGRAM_STATUS_OUTSTANDING));

		// register as transport date and view listener
		UiWrapper.getDefault().registerListener(this);
		NetWrapper.registerListener(this, Transport.class);
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
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
	}

	/**
	 * Creates and adds the actions for the toolbar
	 */
	private void createToolBarActions() {
		// create the action
		GetMessage<Transport> getMessage = new GetMessage<Transport>(new Transport());
		getMessage.addParameter(IFilterTypes.TRANSPORT_TODO_FILTER, "");

		// add to the toolbar
		form.getToolBarManager().add(new RefreshViewAction<Transport>(getMessage));
		form.getToolBarManager().update(true);
	}

	@Override
	public void dataChanged(Message<Transport> message, MessageIoSession messageIoSession) {
		viewer.refresh();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		final String event = evt.getPropertyName();
		final Object newValue = evt.getNewValue();

		// filter out unwanted elements
		if (ListenerConstants.TRANSPORT_FILTER_CHANGED.equalsIgnoreCase(event)) {
			TransportViewFilter searchFilter = (TransportViewFilter) newValue;
			// remove all filters and apply the new
			for (ViewerFilter filter : viewer.getFilters()) {
				if (filter instanceof TransportViewFilter) {
					viewer.removeFilter(filter);
				}
			}
			// apply the new filter
			if (searchFilter != null) {
				viewer.addFilter(searchFilter);
			}
		}
	}
}
