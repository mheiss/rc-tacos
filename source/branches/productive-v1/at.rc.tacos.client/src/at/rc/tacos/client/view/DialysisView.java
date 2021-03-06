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
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.client.controller.DialysisDeleteAction;
import at.rc.tacos.client.controller.DialysisEditAction;
import at.rc.tacos.client.controller.DialysisTransportNowAction;
import at.rc.tacos.client.controller.RefreshViewAction;
import at.rc.tacos.client.modelManager.LockManager;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.DialysisTransportContentProvider;
import at.rc.tacos.client.providers.DialysisTransportLabelProvider;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.client.view.sorterAndTooltip.DialysisTransportSorter;
import at.rc.tacos.model.DialysisPatient;

public class DialysisView extends ViewPart implements PropertyChangeListener {

	public static final String ID = "at.rc.tacos.client.view.dialysis_view";

	// the toolkit to use
	private FormToolkit toolkit;
	private ScrolledForm form;
	private TableViewer viewer;

	// the actions for the context menu
	private DialysisEditAction dialysisEditAction;
	private DialysisDeleteAction dialysisDeleteAction;
	private DialysisTransportNowAction dialysisTransportNowAction;

	// the lock manager
	private LockManager lockManager = ModelFactory.getInstance().getLockManager();

	/**
	 * Constructs a new outstanding transports view adds listeners.
	 */
	public DialysisView() {
		ModelFactory.getInstance().getDialyseManager().addPropertyChangeListener(this);
		ModelFactory.getInstance().getLockManager().addPropertyChangeListener(this);
	}

	/**
	 * Cleanup the view
	 */
	@Override
	public void dispose() {
		ModelFactory.getInstance().getDialyseManager().removePropertyChangeListener(this);
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
		form = toolkit.createScrolledForm(parent);
		toolkit.decorateFormHeading(form.getForm());
		form.getBody().setLayout(new FillLayout());

		final Composite composite = form.getBody();

		viewer = new TableViewer(composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		viewer.setContentProvider(new DialysisTransportContentProvider());
		viewer.setLabelProvider(new DialysisTransportLabelProvider());
		viewer.setInput(ModelFactory.getInstance().getDialyseManager());
		viewer.getTable().setLinesVisible(true);
		viewer.refresh();

		final Table table_2 = viewer.getTable();
		table_2.setLinesVisible(true);
		table_2.setHeaderVisible(true);

		final TableColumn lockColumn = new TableColumn(table_2, SWT.NONE);
		lockColumn.setToolTipText("Eintrag wird gerade bearbeitet");
		lockColumn.setWidth(24);
		lockColumn.setText("L");

		final TableColumn newColumnTableColumnStationDialyse = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnStationDialyse.setToolTipText("Zustšndige Ortsstelle");
		newColumnTableColumnStationDialyse.setWidth(200);
		newColumnTableColumnStationDialyse.setText("OS");

		final TableColumn newColumnTableColumnAbfDialyse = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnAbfDialyse.setToolTipText("Geplante Abfahrt an Ortsstelle");
		newColumnTableColumnAbfDialyse.setWidth(68);
		newColumnTableColumnAbfDialyse.setText("Abf");

		final TableColumn newColumnTableColumnAnkDialyse = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnAnkDialyse.setToolTipText("Geplante Ankunft beim Patienten");
		newColumnTableColumnAnkDialyse.setWidth(68);
		newColumnTableColumnAnkDialyse.setText("Ank");

		final TableColumn newColumnTableColumnTerminDialyse = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnTerminDialyse.setToolTipText("Termin auf der Dialyse");
		newColumnTableColumnTerminDialyse.setWidth(68);
		newColumnTableColumnTerminDialyse.setText("Termin");

		final TableColumn newColumnTableColumnRTAbfahrtDialyse = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnRTAbfahrtDialyse.setToolTipText("Abfahrt an der Ortsstelle");
		newColumnTableColumnRTAbfahrtDialyse.setWidth(68);
		newColumnTableColumnRTAbfahrtDialyse.setText("RT Abfahrt");

		final TableColumn newColumnTableColumnAbholbereitDialyse = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnAbholbereitDialyse.setToolTipText("Patient ist mit Dialyse fertig, abholbereit im LKH");
		newColumnTableColumnAbholbereitDialyse.setWidth(68);
		newColumnTableColumnAbholbereitDialyse.setText("Abholbereit");

		final TableColumn newColumnTableColumnWohnortDialyse = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnWohnortDialyse.setWidth(250);
		newColumnTableColumnWohnortDialyse.setText("Wohnort");

		final TableColumn newColumnTableColumnNameDialyse = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnNameDialyse.setWidth(250);
		newColumnTableColumnNameDialyse.setText("Name");

		final TableColumn newColumnTableColumnDialyseort = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnDialyseort.setWidth(250);
		newColumnTableColumnDialyseort.setText("Dialyseort");

		final TableColumn newColumnTableColumnMontag = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnMontag.setData("newKey", null);
		newColumnTableColumnMontag.setWidth(30);
		newColumnTableColumnMontag.setText("Mo");

		final TableColumn newColumnTableColumnDienstag = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnDienstag.setWidth(30);
		newColumnTableColumnDienstag.setText("Di");

		final TableColumn newColumnTableColumnMittwoch = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnMittwoch.setWidth(30);
		newColumnTableColumnMittwoch.setText("Mi");

		final TableColumn newColumnTableColumnDonnerstag = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnDonnerstag.setWidth(30);
		newColumnTableColumnDonnerstag.setText("Do");

		final TableColumn newColumnTableColumnFreitag = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnFreitag.setWidth(30);
		newColumnTableColumnFreitag.setText("Fr");

		final TableColumn newColumnTableColumnSamstag = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnSamstag.setWidth(30);
		newColumnTableColumnSamstag.setText("Sa");

		final TableColumn newColumnTableColumnSonntag = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnSonntag.setWidth(30);
		newColumnTableColumnSonntag.setText("So");

		final TableColumn newColumnTableColumnTA = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnTA.setWidth(90);
		newColumnTableColumnTA.setText("TA");

		final TableColumn newColumnTableColumnStationaer = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnStationaer.setToolTipText("Patient wird derzeit nicht transportiert");
		newColumnTableColumnStationaer.setWidth(40);
		newColumnTableColumnStationaer.setText("Stat");

		viewer.getTable().addMouseListener(new MouseAdapter() {

			public void mouseDown(MouseEvent e) {
				if (viewer.getTable().getItem(new Point(e.x, e.y)) == null) {
					viewer.setSelection(new StructuredSelection());
				}
			}
		});

		/** make the columns sort able */
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

				if (currentColumn == newColumnTableColumnStationDialyse)
					sortIdentifier = DialysisTransportSorter.RESP_STATION_SORTER;
				if (currentColumn == newColumnTableColumnAbfDialyse)
					sortIdentifier = DialysisTransportSorter.ABF_SORTER;
				if (currentColumn == newColumnTableColumnAnkDialyse)
					sortIdentifier = DialysisTransportSorter.AT_PATIENT_SORTER;
				if (currentColumn == newColumnTableColumnTerminDialyse)
					sortIdentifier = DialysisTransportSorter.TERM_SORTER;
				if (currentColumn == newColumnTableColumnWohnortDialyse)
					sortIdentifier = DialysisTransportSorter.TRANSPORT_FROM_SORTER;
				if (currentColumn == newColumnTableColumnNameDialyse)
					sortIdentifier = DialysisTransportSorter.PATIENT_SORTER;
				if (currentColumn == newColumnTableColumnDialyseort)
					sortIdentifier = DialysisTransportSorter.TRANSPORT_TO_SORTER;
				if (currentColumn == newColumnTableColumnRTAbfahrtDialyse)
					sortIdentifier = DialysisTransportSorter.RT_SORTER;
				if (currentColumn == newColumnTableColumnAbholbereitDialyse)
					sortIdentifier = DialysisTransportSorter.READY_SORTER;
				if (currentColumn == newColumnTableColumnTA)
					sortIdentifier = DialysisTransportSorter.TA_SORTER;
				// apply the filter
				viewer.getTable().setSortDirection(dir);
				viewer.setSorter(new DialysisTransportSorter(sortIdentifier, dir));
			}
		};

		// attach the listener
		newColumnTableColumnStationDialyse.addListener(SWT.Selection, sortListener);
		newColumnTableColumnAbfDialyse.addListener(SWT.Selection, sortListener);
		newColumnTableColumnAnkDialyse.addListener(SWT.Selection, sortListener);
		newColumnTableColumnTerminDialyse.addListener(SWT.Selection, sortListener);
		newColumnTableColumnWohnortDialyse.addListener(SWT.Selection, sortListener);
		newColumnTableColumnNameDialyse.addListener(SWT.Selection, sortListener);
		newColumnTableColumnRTAbfahrtDialyse.addListener(SWT.Selection, sortListener);
		newColumnTableColumnAbholbereitDialyse.addListener(SWT.Selection, sortListener);
		newColumnTableColumnTA.addListener(SWT.Selection, sortListener);
		newColumnTableColumnDialyseort.addListener(SWT.Selection, sortListener);

		makeActions();
		hookContextMenu();
		createToolBarActions();

		viewer.refresh();
	}

	/**
	 * Creates the needed actions
	 */
	private void makeActions() {
		dialysisEditAction = new DialysisEditAction(viewer);
		dialysisDeleteAction = new DialysisDeleteAction(viewer);
		dialysisTransportNowAction = new DialysisTransportNowAction(viewer);
	}

	/**
	 * Creates the context menu
	 */
	private void hookContextMenu() {
		MenuManager menuManager = new MenuManager("#DialysisPopupMenu");
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

		// cast to a dialysis transport
		DialysisPatient dia = (DialysisPatient) firstSelectedObject;

		if (dia == null)
			return;

		// add the actions
		manager.add(dialysisEditAction);
		manager.add(new Separator());
		manager.add(dialysisDeleteAction);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(dialysisTransportNowAction);

		if (lockManager.containsLock(DialysisPatient.ID, dia.getId())) {
			dialysisDeleteAction.setEnabled(false);
			dialysisTransportNowAction.setEnabled(false);
		}
		else {
			dialysisDeleteAction.setEnabled(true);
			dialysisTransportNowAction.setEnabled(true);
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
		if ("DIALYSISPATIENT_ADD".equals(evt.getPropertyName()) || "DIALYSISPATIENT_REMOVE".equals(evt.getPropertyName())
				|| "DIALYSISPATIENT_UPDATE".equals(evt.getPropertyName()) || "DIALYSISPATIENT_CLEARED".equals(evt.getPropertyName())) {
			viewer.refresh();
		}

		// listen to lock changes
		if ("LOCK_ADD".equalsIgnoreCase(evt.getPropertyName()) || "LOCK_REMOVE".equalsIgnoreCase(evt.getPropertyName())) {
			viewer.refresh();
		}
	}

	/**
	 * Creates and adds the actions for the toolbar
	 */
	private void createToolBarActions() {
		// create the action
		RefreshViewAction viewAction = new RefreshViewAction(DialysisPatient.ID);
		// add to the toolbar
		form.getToolBarManager().add(viewAction);
		form.getToolBarManager().update(true);
	}
}
