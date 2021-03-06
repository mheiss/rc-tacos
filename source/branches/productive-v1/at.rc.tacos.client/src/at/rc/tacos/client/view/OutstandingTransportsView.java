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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.client.controller.AssignCarAction;
import at.rc.tacos.client.controller.CancelTransportAction;
import at.rc.tacos.client.controller.CopyTransportAction;
import at.rc.tacos.client.controller.EditTransportAction;
import at.rc.tacos.client.controller.ForwardTransportAction;
import at.rc.tacos.client.modelManager.LockManager;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.OutstandingTransportsViewContentProvider;
import at.rc.tacos.client.providers.OutstandingTransportsViewLabelProvider;
import at.rc.tacos.client.providers.TransportStateViewFilter;
import at.rc.tacos.client.providers.TransportViewFilter;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.client.view.sorterAndTooltip.TransportSorter;
import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.model.Transport;
import at.rc.tacos.model.VehicleDetail;

public class OutstandingTransportsView extends ViewPart implements PropertyChangeListener, IProgramStatus {

	public static final String ID = "at.rc.tacos.client.view.outstandingTransports_view";

	// the toolkit to use
	private FormToolkit toolkit;
	private Form form;
	private TableViewer viewerOffTrans;

	// the actions for the context menu
	private CopyTransportAction copyTransportAction;
	private ForwardTransportAction forwardTransportAction;
	private CancelTransportAction cancelTransportAction;
	private EditTransportAction editTransportAction;

	// the lock manager
	private LockManager lockManager = ModelFactory.getInstance().getLockManager();

	ArrayList<AssignCarAction> actionList = new ArrayList<AssignCarAction>();

	/**
	 * Constructs a new outstanding transports view.
	 */
	public OutstandingTransportsView() {
		ModelFactory.getInstance().getTransportManager().addPropertyChangeListener(this);
		ModelFactory.getInstance().getLockManager().addPropertyChangeListener(this);
	}

	/**
	 * Cleanup the view
	 */
	@Override
	public void dispose() {
		ModelFactory.getInstance().getTransportManager().removePropertyChangeListener(this);
		ModelFactory.getInstance().getLockManager().removePropertyChangeListener(this);
	}

	/**
	 * Call back method to create the control and initialize them.
	 * 
	 * @param parent
	 *            the parent composite to add
	 */
	@Override
	public void createPartControl(final Composite parent) {
		// Create the scrolled parent component
		toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
		form = toolkit.createForm(parent);
		form.setText("Offene Transporte");
		toolkit.decorateFormHeading(form);
		form.getBody().setLayout(new FillLayout());

		final Composite composite = form.getBody();

		viewerOffTrans = new TableViewer(composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		viewerOffTrans.setContentProvider(new OutstandingTransportsViewContentProvider());
		viewerOffTrans.setLabelProvider(new OutstandingTransportsViewLabelProvider());
		viewerOffTrans.setInput(ModelFactory.getInstance().getTransportManager());
		viewerOffTrans.getTable().setLinesVisible(true);

		viewerOffTrans.getTable().addMouseListener(new MouseAdapter() {

			public void mouseDown(MouseEvent e) {
				if (viewerOffTrans.getTable().getItem(new Point(e.x, e.y)) == null) {
					viewerOffTrans.setSelection(new StructuredSelection());
				}
			}
		});

		viewerOffTrans.refresh();

		// make the actions for the context menu when selection has changed
		viewerOffTrans.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				this.makeActions();
				this.hookContextMenu();
			}

			/**
			 * Creates the needed actions
			 */
			public void makeActions() {
				forwardTransportAction = new ForwardTransportAction(viewerOffTrans);
				editTransportAction = new EditTransportAction(viewerOffTrans, "outstanding");
				cancelTransportAction = new CancelTransportAction(viewerOffTrans);
				copyTransportAction = new CopyTransportAction(viewerOffTrans);
				// get the list of all vehicle with the status ready for action
				List<VehicleDetail> readyVehicles = ModelFactory.getInstance().getVehicleManager().getReadyVehicleList();
				// loop and create the actions
				actionList.clear();
				for (VehicleDetail veh : readyVehicles) {
					AssignCarAction action = new AssignCarAction(viewerOffTrans, veh);
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
				Menu menu = menuManager.createContextMenu(viewerOffTrans.getControl());
				viewerOffTrans.getControl().setMenu(menu);
				// getSite().registerContextMenu(menuManager, viewerOffTrans);
			}

			/**
			 * Fills the context menu with the actions
			 */
			private void fillContextMenu(IMenuManager manager) {
				// get the selected object
				final Object firstSelectedObject = ((IStructuredSelection) viewerOffTrans.getSelection()).getFirstElement();

				// cast to a RosterEntry
				Transport transport = (Transport) firstSelectedObject;

				if (transport == null)
					return;

				// submenu for the available vehicles
				MenuManager menuManagerSub = new MenuManager("Fahrzeug zuweisen");

				// add the actions
				manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));

				manager.add(menuManagerSub);

				// create a list of ready vehicles and disable the selection if
				// the transport is locked
				boolean locked = lockManager.containsLock(Transport.ID, transport.getTransportId());
				for (AssignCarAction ac : actionList) {
					menuManagerSub.add(ac);
					if (locked) {
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

				// if locked
				if (locked) {
					copyTransportAction.setEnabled(false);
				}
				else
					copyTransportAction.setEnabled(true);
			}
		});

		// set the default sorter
		viewerOffTrans.setSorter(new TransportSorter(TransportSorter.PRIORITY_SORTER, SWT.UP));

		final Table tableOff = viewerOffTrans.getTable();
		tableOff.setLinesVisible(true);
		tableOff.setHeaderVisible(true);

		final TableColumn lockColumn = new TableColumn(tableOff, SWT.NONE);
		lockColumn.setToolTipText("Eintrag wird gerade bearbeitet");
		lockColumn.setWidth(24);
		lockColumn.setText("L");

		final TableColumn prioritaetOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		prioritaetOffeneTransporte
				.setToolTipText("A (NEF), B (BD1), C (Transport), D (Rücktransport), E (Heimtransport), F (Sonstiges), E (NEF extern)");
		prioritaetOffeneTransporte.setWidth(26);
		prioritaetOffeneTransporte.setText("Pr");

		final TableColumn respOSOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		respOSOffeneTransporte.setToolTipText("Zuständige Ortsstelle");
		respOSOffeneTransporte.setWidth(30);
		respOSOffeneTransporte.setText("OS");

		final TableColumn abfOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		abfOffeneTransporte.setToolTipText("Abfahrt auf der Dienststelle");
		abfOffeneTransporte.setWidth(43);
		abfOffeneTransporte.setText("Abf");

		final TableColumn ankOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		ankOffeneTransporte.setToolTipText("Ankunft beim Patienten");
		ankOffeneTransporte.setWidth(43);
		ankOffeneTransporte.setText("Ank.");

		final TableColumn terminOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		terminOffeneTransporte.setToolTipText("Terminzeit am Zielort");
		terminOffeneTransporte.setWidth(43);
		terminOffeneTransporte.setText("Termin");

		final TableColumn transportVonOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		transportVonOffeneTransporte.setWidth(250);
		transportVonOffeneTransporte.setText("Transport von");

		final TableColumn patientOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		patientOffeneTransporte.setWidth(200);
		patientOffeneTransporte.setText("Patient");

		final TableColumn transportNachOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		transportNachOffeneTransporte.setWidth(250);
		transportNachOffeneTransporte.setText("Transport nach");

		final TableColumn aufgOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		aufgOffeneTransporte.setToolTipText("Zeit zu der der Transport aufgenommen wurde");
		aufgOffeneTransporte.setWidth(43);
		aufgOffeneTransporte.setText("Aufg");

		final TableColumn tOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		tOffeneTransporte.setToolTipText("Transportart");
		tOffeneTransporte.setWidth(22);
		tOffeneTransporte.setText("T");

		final TableColumn erkrankungVerletzungOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		erkrankungVerletzungOffeneTransporte.setWidth(150);
		erkrankungVerletzungOffeneTransporte.setText("Erkrankung/Verletzung");

		final TableColumn anmerkungOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		anmerkungOffeneTransporte.setWidth(542);
		anmerkungOffeneTransporte.setText("Anmerkung");

		// make the columns sortable
		Listener sortListener = new Listener() {

			public void handleEvent(Event e) {
				// determine new sort column and direction
				TableColumn sortColumn = viewerOffTrans.getTable().getSortColumn();
				TableColumn currentColumn = (TableColumn) e.widget;
				int dir = viewerOffTrans.getTable().getSortDirection();
				// revert the sort order if the column is the same
				if (sortColumn == currentColumn) {
					if (dir == SWT.UP)
						dir = SWT.DOWN;
					else
						dir = SWT.UP;
				}
				else {
					viewerOffTrans.getTable().setSortColumn(currentColumn);
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
				viewerOffTrans.getTable().setSortDirection(dir);
				viewerOffTrans.setSorter(new TransportSorter(sortIdentifier, dir));
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

		viewerOffTrans.resetFilters();
		// apply the filter to show only outstanding transports
		viewerOffTrans.addFilter(new TransportStateViewFilter(PROGRAM_STATUS_OUTSTANDING));
		viewerOffTrans.refresh();
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
	}

	public void propertyChange(PropertyChangeEvent evt) {
		// the viewer represents simple model. refresh should be enough.
		if ("TRANSPORT_ADD".equals(evt.getPropertyName()) || "TRANSPORT_REMOVE".equals(evt.getPropertyName())
				|| "TRANSPORT_UPDATE".equals(evt.getPropertyName()) || "TRANSPORT_CLEARED".equals(evt.getPropertyName())) {
			if (!viewerOffTrans.getTable().isDisposed())
				viewerOffTrans.refresh();
		}
		// listen to filter events
		if ("TRANSPORT_FILTER_CHANGED".equalsIgnoreCase(evt.getPropertyName())) {
			// get the new filter
			TransportViewFilter searchFilter = (TransportViewFilter) evt.getNewValue();
			// remove all filters and apply the new
			for (ViewerFilter filter : viewerOffTrans.getFilters()) {
				if (!(filter instanceof TransportViewFilter))
					continue;
				viewerOffTrans.removeFilter(filter);

			}
			if (searchFilter != null) {
				viewerOffTrans.addFilter(searchFilter);
			}
		}

		// listen to lock changes
		if ("LOCK_ADD".equalsIgnoreCase(evt.getPropertyName()) || "LOCK_REMOVE".equalsIgnoreCase(evt.getPropertyName())) {
			viewerOffTrans.refresh();
		}
	}
}
