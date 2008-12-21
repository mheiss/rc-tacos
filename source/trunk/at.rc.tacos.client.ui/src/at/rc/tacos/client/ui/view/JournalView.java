package at.rc.tacos.client.ui.view;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.client.controller.CreateBackTransportAction;
import at.rc.tacos.client.controller.EditTransportAction;
import at.rc.tacos.client.controller.JournalMoveToRunningTransportsAction;
import at.rc.tacos.client.controller.MoveToOutstandingTransportsAction;
import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.net.handler.LockHandler;
import at.rc.tacos.client.net.handler.TransportHandler;
import at.rc.tacos.client.providers.JournalViewContentProvider;
import at.rc.tacos.client.providers.JournalViewLabelProvider;
import at.rc.tacos.client.providers.TransportDateFilter;
import at.rc.tacos.client.providers.TransportStateViewFilter;
import at.rc.tacos.client.providers.TransportViewFilter;
import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.ListenerConstants;
import at.rc.tacos.client.ui.sorterAndTooltip.JournalViewTooltip;
import at.rc.tacos.client.ui.sorterAndTooltip.TransportSorter;
import at.rc.tacos.client.ui.utils.CustomColors;
import at.rc.tacos.platform.iface.IProgramStatus;
import at.rc.tacos.platform.model.Lock;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.listeners.DataChangeListener;
import at.rc.tacos.platform.net.mina.MessageIoSession;

public class JournalView extends ViewPart implements DataChangeListener<Object>, PropertyChangeListener {

	public static final String ID = "at.rc.tacos.client.view.journal_view";
	private Logger log = LoggerFactory.getLogger(JournalView.class);

	// the toolkit to use
	private FormToolkit toolkit;
	private Form form;
	private TableViewer viewer;
	private JournalViewTooltip tooltip;

	// the actions for the context menu
	private EditTransportAction editTransportAction;
	private MoveToOutstandingTransportsAction moveToOutstandingTransportsAction;
	private JournalMoveToRunningTransportsAction moveToRunningTransportsAction;
	private CreateBackTransportAction createBackTransportAction;

	// the model handlers
	private LockHandler lockHandler = (LockHandler) NetWrapper.getHandler(Lock.class);
	private TransportHandler transportHandler = (TransportHandler) NetWrapper.getHandler(Transport.class);

	/**
	 * Cleanup the view and remove the listeners.
	 */
	@Override
	public void dispose() {
		NetWrapper.removeListener(this, Transport.class);
		NetWrapper.removeListener(this, Lock.class);
		UiWrapper.getDefault().removeListener(this);
		super.dispose();
	}

	/**
	 * Callback method to create the control and initialize them.
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
		viewer.setContentProvider(new JournalViewContentProvider());
		viewer.setLabelProvider(new JournalViewLabelProvider());
		viewer.setInput(transportHandler.toArray());
		viewer.getTable().setLinesVisible(true);

		// set the tooltip
		tooltip = new JournalViewTooltip(viewer.getControl());

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
		viewer.setSorter(new TransportSorter(TransportSorter.TRANSPORT_FROM_SORTER, SWT.DOWN));

		// create the table for the transports
		final Table table = viewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		final TableColumn lockColumn = new TableColumn(table, SWT.NONE);
		lockColumn.setToolTipText("Eintrag wird gerade bearbeitet");
		lockColumn.setWidth(24);
		lockColumn.setText("L");

		final TableColumn realOSJournal = new TableColumn(table, SWT.NONE);
		realOSJournal.setToolTipText("Ortsstelle, die den Transport durchgeführt hat");
		realOSJournal.setWidth(29);
		realOSJournal.setText("OS");

		final TableColumn columnTNrJournal = new TableColumn(table, SWT.NONE);
		columnTNrJournal.setMoveable(true);
		columnTNrJournal.setToolTipText("Transportnummer");
		columnTNrJournal.setWidth(70);
		columnTNrJournal.setText("TNr");

		final TableColumn columnPrioritaetJournal = new TableColumn(table, SWT.NONE);
		columnPrioritaetJournal
				.setToolTipText("1 (NEF), 2 (Transport), 3 (Terminfahrt), 4 (Rücktransport), 5 (Heimtransport), 6 (Sonstiges), 7 (NEF extern)");
		columnPrioritaetJournal.setWidth(20);
		columnPrioritaetJournal.setText("Pr");

		final TableColumn columnTransportVonJournal = new TableColumn(table, SWT.NONE);
		columnTransportVonJournal.setMoveable(true);
		columnTransportVonJournal.setWidth(250);
		columnTransportVonJournal.setText("Transport von");

		final TableColumn columnPatientJournal = new TableColumn(table, SWT.NONE);
		columnPatientJournal.setMoveable(true);
		columnPatientJournal.setWidth(195);
		columnPatientJournal.setText("Patient");

		final TableColumn columnTransportNachJournal = new TableColumn(table, SWT.NONE);
		columnTransportNachJournal.setWidth(250);
		columnTransportNachJournal.setText("Transport nach");

		final TableColumn columnErkrVerlJournal = new TableColumn(table, SWT.NONE);
		columnErkrVerlJournal.setWidth(120);
		columnErkrVerlJournal.setText("Erkr/Verl");

		final TableColumn columnAEJournal = new TableColumn(table, SWT.NONE);
		columnAEJournal.setToolTipText("Auftrag erteilt");
		columnAEJournal.setWidth(43);
		columnAEJournal.setText("AE");

		final TableColumn columnS1Journal = new TableColumn(table, SWT.NONE);
		columnS1Journal.setToolTipText("Transportbeginn");
		columnS1Journal.setWidth(43);
		columnS1Journal.setText("S1");

		final TableColumn columnS2Journal = new TableColumn(table, SWT.NONE);
		columnS2Journal.setToolTipText("Bei Patient");
		columnS2Journal.setWidth(43);
		columnS2Journal.setText("S2");

		final TableColumn columnS3Journal = new TableColumn(table, SWT.NONE);
		columnS3Journal.setToolTipText("Abfahrt mit Patient");
		columnS3Journal.setWidth(43);
		columnS3Journal.setText("S3");

		final TableColumn columnS4Journal = new TableColumn(table, SWT.NONE);
		columnS4Journal.setToolTipText("Ankunft Zielort");
		columnS4Journal.setWidth(43);
		columnS4Journal.setText("S4");

		final TableColumn columnS5Journal = new TableColumn(table, SWT.NONE);
		columnS5Journal.setToolTipText("Ziel frei");
		columnS5Journal.setWidth(43);
		columnS5Journal.setText("S5");

		final TableColumn columnS6Journal = new TableColumn(table, SWT.NONE);
		columnS6Journal.setToolTipText("Fahrzeug eingerückt");
		columnS6Journal.setWidth(43);
		columnS6Journal.setText("S6");

		final TableColumn columnFzgJournal = new TableColumn(table, SWT.NONE);
		columnFzgJournal.setWidth(45);
		columnFzgJournal.setText("Fzg");

		final TableColumn columnFahrerJournal = new TableColumn(table, SWT.NONE);
		columnFahrerJournal.setWidth(120);
		columnFahrerJournal.setText("Fahrer");

		final TableColumn columnSaniIJournal = new TableColumn(table, SWT.NONE);
		columnSaniIJournal.setWidth(120);
		columnSaniIJournal.setText("Sanitäter I");

		final TableColumn columnSaniIIJournal = new TableColumn(table, SWT.NONE);
		columnSaniIIJournal.setWidth(120);
		columnSaniIIJournal.setText("Sanitäter II");

		// make the columns sort able
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
				if (currentColumn == columnTNrJournal)
					sortIdentifier = TransportSorter.TNR_SORTER;
				if (currentColumn == columnPrioritaetJournal)
					sortIdentifier = TransportSorter.PRIORITY_SORTER;
				if (currentColumn == columnTransportVonJournal)
					sortIdentifier = TransportSorter.TRANSPORT_FROM_SORTER;
				if (currentColumn == columnPatientJournal)
					sortIdentifier = TransportSorter.PATIENT_SORTER;
				if (currentColumn == columnTransportNachJournal)
					sortIdentifier = TransportSorter.TRANSPORT_TO_SORTER;
				if (currentColumn == columnErkrVerlJournal)
					sortIdentifier = TransportSorter.KIND_OF_ILLNESS_SORTER;
				if (currentColumn == columnAEJournal)
					sortIdentifier = TransportSorter.AE_SORTER;
				if (currentColumn == columnS1Journal)
					sortIdentifier = TransportSorter.S1_SORTER;
				if (currentColumn == columnS2Journal)
					sortIdentifier = TransportSorter.S2_SORTER;
				if (currentColumn == columnS3Journal)
					sortIdentifier = TransportSorter.S3_SORTER;
				if (currentColumn == columnS4Journal)
					sortIdentifier = TransportSorter.S4_SORTER;
				if (currentColumn == columnS5Journal)
					sortIdentifier = TransportSorter.S5_SORTER;
				if (currentColumn == columnS6Journal)
					sortIdentifier = TransportSorter.S6_SORTER;
				if (currentColumn == columnFzgJournal)
					sortIdentifier = TransportSorter.VEHICLE_SORTER;
				if (currentColumn == columnFahrerJournal)
					sortIdentifier = TransportSorter.DRIVER_SORTER;
				if (currentColumn == columnSaniIJournal)
					sortIdentifier = TransportSorter.PARAMEDIC_I_SORTER;
				if (currentColumn == columnSaniIIJournal)
					sortIdentifier = TransportSorter.PARAMEDIC_II_SORTER;

				// apply the filter
				viewer.getTable().setSortDirection(dir);
				viewer.setSorter(new TransportSorter(sortIdentifier, dir));
			}
		};

		// attach the listener
		columnTNrJournal.addListener(SWT.Selection, sortListener);
		columnPrioritaetJournal.addListener(SWT.Selection, sortListener);
		columnTransportVonJournal.addListener(SWT.Selection, sortListener);
		columnPatientJournal.addListener(SWT.Selection, sortListener);
		columnTransportNachJournal.addListener(SWT.Selection, sortListener);
		columnErkrVerlJournal.addListener(SWT.Selection, sortListener);
		columnAEJournal.addListener(SWT.Selection, sortListener);
		columnS1Journal.addListener(SWT.Selection, sortListener);
		columnS2Journal.addListener(SWT.Selection, sortListener);
		columnS3Journal.addListener(SWT.Selection, sortListener);
		columnS4Journal.addListener(SWT.Selection, sortListener);
		columnS5Journal.addListener(SWT.Selection, sortListener);
		columnS6Journal.addListener(SWT.Selection, sortListener);
		columnFzgJournal.addListener(SWT.Selection, sortListener);
		columnSaniIJournal.addListener(SWT.Selection, sortListener);
		columnSaniIIJournal.addListener(SWT.Selection, sortListener);

		// create the actions
		makeActions();
		hookContextMenu();

		// show only transport with the status journal
		viewer.addFilter(new TransportStateViewFilter(IProgramStatus.PROGRAM_STATUS_JOURNAL));
		viewer.addFilter(new TransportDateFilter(Calendar.getInstance()));
		viewer.refresh();

		// register as transport date and view listener
		UiWrapper.getDefault().registerListener(this);
		NetWrapper.registerListener(this, Transport.class);
		NetWrapper.registerListener(this, Lock.class);
	}

	/**
	 * Creates the needed actions
	 */
	private void makeActions() {
		editTransportAction = new EditTransportAction(viewer, "journal");
		moveToOutstandingTransportsAction = new MoveToOutstandingTransportsAction(viewer);
		moveToRunningTransportsAction = new JournalMoveToRunningTransportsAction(viewer);
		createBackTransportAction = new CreateBackTransportAction(viewer);
	}

	/**
	 * Creates the context menue
	 */
	private void hookContextMenu() {
		MenuManager menuManager = new MenuManager("#JournalPopupMenu");
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

		// cast to a Transport
		Transport transport = (Transport) firstSelectedObject;

		if (transport == null)
			return;

		// add the actions
		manager.add(editTransportAction);
		manager.add(new Separator());
		manager.add(moveToOutstandingTransportsAction);
		manager.add(moveToRunningTransportsAction);
		manager.add(new Separator());
		manager.add(createBackTransportAction);

		// disable actions if the transport is locked
		if (lockHandler.containsLock(transport.getTransportId(), Transport.class)) {
			moveToOutstandingTransportsAction.setEnabled(false);
			moveToRunningTransportsAction.setEnabled(false);
		}
		else {
			moveToOutstandingTransportsAction.setEnabled(true);
			moveToRunningTransportsAction.setEnabled(true);
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
		if (object instanceof Transport) {
			viewer.refresh();
			return;
		}
		if (object instanceof Lock) {
			viewer.refresh();
		}

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		final String event = evt.getPropertyName();
		final Object newValue = evt.getNewValue();

		// update the view filter with the new date
		if (ListenerConstants.TRANSPORT_DATE_CHANGED.equalsIgnoreCase(event)) {
			if (!(newValue instanceof Calendar)) {
				log.error("Expected 'Calendar' but was " + newValue == null ? "null" : newValue.getClass().getName());
			}
			// apply the filter
			viewer.resetFilters();
			viewer.addFilter(new TransportStateViewFilter(IProgramStatus.PROGRAM_STATUS_JOURNAL));
			viewer.addFilter(new TransportDateFilter((Calendar) newValue));
			viewer.refresh();
		}

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
