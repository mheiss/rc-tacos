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
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.client.controller.CancelTransportAction;
import at.rc.tacos.client.controller.CopyTransportAction;
import at.rc.tacos.client.controller.EditTransportAction;
import at.rc.tacos.client.controller.MoveToOutstandingTransportsAction;
import at.rc.tacos.client.modelManager.LockManager;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.PrebookingViewContentProvider;
import at.rc.tacos.client.providers.PrebookingViewLabelProvider;
import at.rc.tacos.client.providers.TransportDateFilter;
import at.rc.tacos.client.providers.TransportDirectnessFilter;
import at.rc.tacos.client.providers.TransportStateViewFilter;
import at.rc.tacos.client.providers.TransportViewFilter;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.client.view.sorterAndTooltip.JournalViewTooltip;
import at.rc.tacos.client.view.sorterAndTooltip.TransportSorter;
import at.rc.tacos.platform.iface.IDirectness;
import at.rc.tacos.platform.iface.IProgramStatus;
import at.rc.tacos.platform.model.Transport;

public class PrebookingView extends ViewPart implements PropertyChangeListener, IProgramStatus, IDirectness
{
	public static final String ID = "at.rc.tacos.client.view.prebooking_view";

	//the toolkit to use
	private FormToolkit toolkit;
	private Form form;
	private TableViewer viewerGraz;
	private TableViewer viewerLeoben;
	private TableViewer viewerBruck;
	private TableViewer viewerMariazell;
	private TableViewer viewerWien;
	private TableViewer viewerKapfenberg;
	private JournalViewTooltip tooltipGraz;
	private JournalViewTooltip tooltipLeoben;
	private JournalViewTooltip tooltipBruck;
	private JournalViewTooltip tooltipMariazell;
	private JournalViewTooltip tooltipWien;
	private JournalViewTooltip tooltipKapfenberg;

	//the actions for the context menu
	private EditTransportAction editTransportActionKapfenberg;
	private CancelTransportAction cancelTransportActionKapfenberg;//!!
	private MoveToOutstandingTransportsAction moveToOutstandingTransportsActionKapfenberg;
	private CopyTransportAction copyTransportActionKapfenberg;

	private EditTransportAction editTransportActionBruck;
	private CancelTransportAction cancelTransportActionBruck;//!!
	private MoveToOutstandingTransportsAction moveToOutstandingTransportsActionBruck;
	private CopyTransportAction copyTransportActionBruck;

	private EditTransportAction editTransportActionWien;
	private CancelTransportAction cancelTransportActionWien;//!!
	private MoveToOutstandingTransportsAction moveToOutstandingTransportsActionWien;
	private CopyTransportAction copyTransportActionWien;

	private EditTransportAction editTransportActionLeoben;
	private CancelTransportAction cancelTransportActionLeoben;//!!
	private MoveToOutstandingTransportsAction moveToOutstandingTransportsActionLeoben;
	private CopyTransportAction copyTransportActionLeoben;

	private EditTransportAction editTransportActionGraz;
	private CancelTransportAction cancelTransportActionGraz;//!!
	private MoveToOutstandingTransportsAction moveToOutstandingTransportsActionGraz;
	private CopyTransportAction copyTransportActionGraz;

	private EditTransportAction editTransportActionMariazell;
	private CancelTransportAction cancelTransportActionMariazell;//!!
	private MoveToOutstandingTransportsAction moveToOutstandingTransportsActionMariazell;
	private CopyTransportAction copyTransportActionMariazell;

	//the currently filtered date
	private Calendar filteredDate = Calendar.getInstance();
	
	//the lock manager
	private LockManager lockManager = ModelFactory.getInstance().getLockManager();

	/**
	 * Constructs a new journal view.
	 */
	public PrebookingView()
	{
		// add listener to model to keep on track. 
		ModelFactory.getInstance().getTransportManager().addPropertyChangeListener(this);
		ModelFactory.getInstance().getLockManager().addPropertyChangeListener(this);
	}

	/**
	 * Cleanup the view
	 */
	@Override
	public void dispose() 
	{
		ModelFactory.getInstance().getTransportManager().removePropertyChangeListener(this);
		ModelFactory.getInstance().getLockManager().removePropertyChangeListener(this);
	}

	/**
	 * Callback method to create the control and initialize them.
	 * @param parent the parent composite to add
	 */
	@Override
	public void createPartControl(final Composite parent) 
	{
		// Create the scrolled parent component
		toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
		form = toolkit.createForm(parent);
		toolkit.decorateFormHeading(form);
		form.getBody().setLayout(new FillLayout());

		final Composite composite = form.getBody();
		
		SashForm sash_prebooking = new SashForm(composite, SWT.HORIZONTAL);

		//groups-----------------------------------
		final SashForm sashForm_8 = new SashForm(sash_prebooking, SWT.VERTICAL);
		
		final Group richtungBruckGroup = new Group(sashForm_8, SWT.NONE);
		richtungBruckGroup.setLayout(new FillLayout());
		richtungBruckGroup.setForeground(CustomColors.RED_COLOR);
		richtungBruckGroup.setText("Richtung Bruck");

		final SashForm sashForm_7 = new SashForm(sashForm_8, SWT.VERTICAL);

		final Group richtungKapfenbergGroup = new Group(sashForm_7, SWT.NONE);
		richtungKapfenbergGroup.setLayout(new FillLayout());
		richtungKapfenbergGroup.setText("Richtung Kapfenberg");

		final Group richtungMariazellGroup = new Group(sashForm_7, SWT.NONE);
		richtungMariazellGroup.setLayout(new FillLayout());
		richtungMariazellGroup.setText("Richtung Mariazell");

		//----------------------------------------------
		final SashForm sashForm_9 = new SashForm(sash_prebooking, SWT.VERTICAL);

		final Group richtungGrazGroup = new Group(sashForm_9, SWT.NONE);
		richtungGrazGroup.setLayout(new FillLayout());
		richtungGrazGroup.setText("Richtung Graz");

		final SashForm sashForm_1 = new SashForm(sashForm_9, SWT.VERTICAL);
		
		final Group richtungLeobenGroup = new Group(sashForm_1, SWT.NONE);
		richtungLeobenGroup.setLayout(new FillLayout());
		richtungLeobenGroup.setText("Richtung Leoben");

		final Group richtungWienGroup = new Group(sashForm_1, SWT.NONE);
		richtungWienGroup.setLayout(new FillLayout());
		richtungWienGroup.setText("Richtung Wien");

		//viewers
		viewerLeoben = createTableViewer(richtungLeobenGroup);
		viewerGraz = createTableViewer(richtungGrazGroup);
		viewerKapfenberg = createTableViewer(richtungKapfenbergGroup);
		viewerBruck = createTableViewer(richtungBruckGroup);
		viewerWien = createTableViewer(richtungWienGroup);
		viewerMariazell = createTableViewer(richtungMariazellGroup);

		//create the tooltip
		tooltipLeoben = new JournalViewTooltip(viewerLeoben.getControl());
		tooltipGraz = new JournalViewTooltip(viewerGraz.getControl());
		tooltipKapfenberg = new JournalViewTooltip(viewerKapfenberg.getControl());
		tooltipBruck = new JournalViewTooltip(viewerBruck.getControl());
		tooltipWien = new JournalViewTooltip(viewerWien.getControl());
		tooltipMariazell = new JournalViewTooltip(viewerMariazell.getControl());

		//show the tool tip when the selection has changed
		viewerLeoben.addSelectionChangedListener(createTooltipListener(viewerLeoben, tooltipLeoben));     
		viewerGraz.addSelectionChangedListener(createTooltipListener(viewerGraz, tooltipGraz));
		viewerKapfenberg.addSelectionChangedListener(createTooltipListener(viewerKapfenberg, tooltipKapfenberg));
		viewerBruck.addSelectionChangedListener(createTooltipListener(viewerBruck, tooltipBruck));
		viewerWien.addSelectionChangedListener(createTooltipListener(viewerWien,tooltipWien));
		viewerMariazell.addSelectionChangedListener(createTooltipListener(viewerMariazell, tooltipMariazell));

		//sort the table by default
		viewerLeoben.setSorter(new TransportSorter(TransportSorter.ABF_SORTER,SWT.DOWN));
		viewerGraz.setSorter(new TransportSorter(TransportSorter.ABF_SORTER,SWT.DOWN));
		viewerKapfenberg.setSorter(new TransportSorter(TransportSorter.ABF_SORTER,SWT.DOWN));
		viewerBruck.setSorter(new TransportSorter(TransportSorter.ABF_SORTER,SWT.DOWN));
		viewerWien.setSorter(new TransportSorter(TransportSorter.ABF_SORTER,SWT.DOWN));
		viewerMariazell.setSorter(new TransportSorter(TransportSorter.ABF_SORTER,SWT.DOWN));

		makeActionsBruck(viewerBruck);
		makeActionsKapfenberg(viewerKapfenberg);
		makeActionsLeoben(viewerLeoben);
		makeActionsMariazell(viewerMariazell);
		makeActionsGraz(viewerGraz);
		makeActionsWien(viewerWien);

		hookContextMenuLeoben(viewerLeoben);
		hookContextMenuGraz(viewerGraz);
		hookContextMenuWien(viewerWien);
		hookContextMenuMariazell(viewerMariazell);
		hookContextMenuBruck(viewerBruck);
		hookContextMenuKapfenberg(viewerKapfenberg);

		//apply the filters
		applyFilters(Calendar.getInstance());

		//refresh the views
		viewerBruck.refresh();
		viewerGraz.refresh();
		viewerKapfenberg.refresh();
		viewerLeoben.refresh();
		viewerMariazell.refresh();
		viewerWien.refresh();
	}

	/**
	 * Creates the needed actions
	 */
	private void makeActionsBruck(TableViewer viewer)
	{		

		editTransportActionBruck = new EditTransportAction(viewer, "prebooking");
		moveToOutstandingTransportsActionBruck = new MoveToOutstandingTransportsAction(viewer);
		cancelTransportActionBruck = new CancelTransportAction(viewer);
		copyTransportActionBruck = new CopyTransportAction(viewer);
	}
	private void makeActionsKapfenberg(TableViewer viewer)
	{		

		editTransportActionKapfenberg = new EditTransportAction(viewer, "prebooking");
		moveToOutstandingTransportsActionKapfenberg = new MoveToOutstandingTransportsAction(viewer);
		cancelTransportActionKapfenberg = new CancelTransportAction(viewer);
		copyTransportActionKapfenberg = new CopyTransportAction(viewer);
	}
	private void makeActionsLeoben(TableViewer viewer)
	{		

		editTransportActionLeoben = new EditTransportAction(viewer, "prebooking");
		moveToOutstandingTransportsActionLeoben = new MoveToOutstandingTransportsAction(viewer);
		cancelTransportActionLeoben = new CancelTransportAction(viewer);
		copyTransportActionLeoben = new CopyTransportAction(viewer);
	}
	private void makeActionsMariazell(TableViewer viewer)
	{		

		editTransportActionMariazell = new EditTransportAction(viewer, "prebooking");
		moveToOutstandingTransportsActionMariazell = new MoveToOutstandingTransportsAction(viewer);
		cancelTransportActionMariazell = new CancelTransportAction(viewer);
		copyTransportActionMariazell = new CopyTransportAction(viewer);
	}
	private void makeActionsGraz(TableViewer viewer)
	{		
		editTransportActionGraz = new EditTransportAction(viewer, "prebooking");
		moveToOutstandingTransportsActionGraz = new MoveToOutstandingTransportsAction(viewer);
		cancelTransportActionGraz = new CancelTransportAction(viewer);
		copyTransportActionGraz = new CopyTransportAction(viewer);
	}
	private void makeActionsWien(TableViewer viewer)
	{		
		editTransportActionWien = new EditTransportAction(viewer, "prebooking");
		moveToOutstandingTransportsActionWien = new MoveToOutstandingTransportsAction(viewer);
		cancelTransportActionWien = new CancelTransportAction(viewer);
		copyTransportActionWien = new CopyTransportAction(viewer);
	}

	/**
	 * Creates the context menu 
	 */
	private void hookContextMenuBruck(final TableViewer viewer) 
	{
		MenuManager menuManager = new MenuManager("#PrebookingBruckPopupMenu");
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				fillContextMenuBruck(manager, viewer);
			}
		});

		Menu menu = menuManager.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuManager, viewer);
	}
	private void hookContextMenuKapfenberg(final TableViewer viewer) 
	{
		MenuManager menuManager = new MenuManager("#PrebookingKapfenbergPopupMenu");
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				fillContextMenuKapfenberg(manager, viewer);
			}
		});

		Menu menu = menuManager.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuManager, viewer);
	}
	private void hookContextMenuLeoben(final TableViewer viewer) 
	{
		MenuManager menuManager = new MenuManager("#PrebookingLeobenPopupMenu");
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				fillContextMenuLeoben(manager, viewer);
			}
		});

		Menu menu = menuManager.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuManager, viewer);
	}
	private void hookContextMenuMariazell(final TableViewer viewer) 
	{
		MenuManager menuManager = new MenuManager("#PrebookingMariazellPopupMenu");
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				fillContextMenuMariazell(manager, viewer);
			}
		});

		Menu menu = menuManager.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuManager, viewer);
	}
	private void hookContextMenuGraz(final TableViewer viewer) 
	{
		MenuManager menuManager = new MenuManager("#PrebookingGrazPopupMenu");
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				fillContextMenuGraz(manager, viewer);
			}
		});

		Menu menu = menuManager.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuManager, viewer);
	}
	private void hookContextMenuWien(final TableViewer viewer) 
	{
		MenuManager menuManager = new MenuManager("#PrebookingWienPopupMenu");
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				fillContextMenuWien(manager, viewer);
			}
		});

		Menu menu = menuManager.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuManager, viewer);
	}

	/**
	 * Fills the context menu with the actions
	 */
	private void fillContextMenuBruck(IMenuManager manager, TableViewer viewer)
	{
		//get the selected object
		final Object firstSelectedObject = ((IStructuredSelection) viewer.getSelection()).getFirstElement();

		//cast to a Transport
		Transport transport = (Transport)firstSelectedObject;

		if(transport == null)
			return;

		//add the actions
		manager.add(editTransportActionBruck);
		manager.add(new Separator());
		manager.add(moveToOutstandingTransportsActionBruck);
		manager.add(cancelTransportActionBruck);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(copyTransportActionBruck);
		
		//disable actions if the transport is locked
		if(lockManager.containsLock(Transport.ID, transport.getTransportId()))
		{
			moveToOutstandingTransportsActionBruck.setEnabled(false);
			cancelTransportActionBruck.setEnabled(false);
			copyTransportActionBruck.setEnabled(false);
		}
		else
		{
			moveToOutstandingTransportsActionBruck.setEnabled(true);
			cancelTransportActionBruck.setEnabled(true);
			copyTransportActionBruck.setEnabled(true);
		}
			
	}
	private void fillContextMenuKapfenberg(IMenuManager manager, TableViewer viewer)
	{
		//get the selected object
		final Object firstSelectedObject = ((IStructuredSelection) viewer.getSelection()).getFirstElement();

		//cast to a Transport
		Transport transport = (Transport)firstSelectedObject;

		if(transport == null)
			return;

		//add the actions
		manager.add(editTransportActionKapfenberg);
		manager.add(new Separator());
		manager.add(moveToOutstandingTransportsActionKapfenberg);
		manager.add(cancelTransportActionKapfenberg);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(copyTransportActionKapfenberg);
		
		//disable actions if the transport is locked
		if(lockManager.containsLock(Transport.ID, transport.getTransportId()))
		{
			moveToOutstandingTransportsActionKapfenberg.setEnabled(false);
			cancelTransportActionKapfenberg.setEnabled(false);
			copyTransportActionKapfenberg.setEnabled(false);
		}
		else
		{
			moveToOutstandingTransportsActionKapfenberg.setEnabled(true);
			cancelTransportActionKapfenberg.setEnabled(true);
			copyTransportActionKapfenberg.setEnabled(true);
		}
	}
	private void fillContextMenuLeoben(IMenuManager manager, TableViewer viewer)
	{
		//get the selected object
		final Object firstSelectedObject = ((IStructuredSelection) viewer.getSelection()).getFirstElement();

		//cast to a Transport
		Transport transport = (Transport)firstSelectedObject;

		if(transport == null)
			return;

		//add the actions
		manager.add(editTransportActionLeoben);
		manager.add(new Separator());
		manager.add(moveToOutstandingTransportsActionLeoben);
		manager.add(cancelTransportActionLeoben);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(copyTransportActionLeoben);
		
		//disable actions if the transport is locked
		if(lockManager.containsLock(Transport.ID, transport.getTransportId()))
		{
			moveToOutstandingTransportsActionLeoben.setEnabled(false);
			cancelTransportActionLeoben.setEnabled(false);
			copyTransportActionLeoben.setEnabled(false);
		}
		else
		{
			moveToOutstandingTransportsActionLeoben.setEnabled(true);
			cancelTransportActionLeoben.setEnabled(true);
			copyTransportActionLeoben.setEnabled(true);
		}
	}
	private void fillContextMenuMariazell(IMenuManager manager, TableViewer viewer)
	{
		//get the selected object
		final Object firstSelectedObject = ((IStructuredSelection) viewer.getSelection()).getFirstElement();

		//cast to a Transport
		Transport transport = (Transport)firstSelectedObject;

		if(transport == null)
			return;

		//add the actions
		manager.add(editTransportActionMariazell);
		manager.add(new Separator());
		manager.add(moveToOutstandingTransportsActionMariazell);
		manager.add(cancelTransportActionMariazell);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(copyTransportActionMariazell);
		
		//disable actions if the transport is locked
		if(lockManager.containsLock(Transport.ID, transport.getTransportId()))
		{
			moveToOutstandingTransportsActionMariazell.setEnabled(false);
			cancelTransportActionMariazell.setEnabled(false);
			copyTransportActionMariazell.setEnabled(false);
		}
		else
		{
			moveToOutstandingTransportsActionMariazell.setEnabled(true);
			cancelTransportActionMariazell.setEnabled(true);
			copyTransportActionMariazell.setEnabled(true);
		}
	}
	private void fillContextMenuGraz(IMenuManager manager, TableViewer viewer)
	{
		//get the selected object
		final Object firstSelectedObject = ((IStructuredSelection) viewer.getSelection()).getFirstElement();

		//cast to a Transport
		Transport transport = (Transport)firstSelectedObject;

		if(transport == null)
			return;

		//add the actions
		manager.add(editTransportActionGraz);
		manager.add(new Separator());
		manager.add(moveToOutstandingTransportsActionGraz);
		manager.add(cancelTransportActionGraz);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(copyTransportActionGraz);
		
		//disable actions if the transport is locked
		if(lockManager.containsLock(Transport.ID, transport.getTransportId()))
		{
			moveToOutstandingTransportsActionGraz.setEnabled(false);
			cancelTransportActionGraz.setEnabled(false);
			copyTransportActionGraz.setEnabled(false);
		}
		else
		{
			moveToOutstandingTransportsActionGraz.setEnabled(true);
			cancelTransportActionGraz.setEnabled(true);
			copyTransportActionGraz.setEnabled(true);
		}
	}
	private void fillContextMenuWien(IMenuManager manager, TableViewer viewer)
	{
		//get the selected object
		final Object firstSelectedObject = ((IStructuredSelection) viewer.getSelection()).getFirstElement();

		//cast to a Transport
		Transport transport = (Transport)firstSelectedObject;

		if(transport == null)
			return;

		//add the actions
		manager.add(editTransportActionWien);
		manager.add(new Separator());
		manager.add(moveToOutstandingTransportsActionWien);
		manager.add(cancelTransportActionWien);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(copyTransportActionWien);
		
		//disable actions if the transport is locked
		if(lockManager.containsLock(Transport.ID, transport.getTransportId()))
		{
			moveToOutstandingTransportsActionWien.setEnabled(false);
			cancelTransportActionWien.setEnabled(false);
			copyTransportActionWien.setEnabled(false);
		}
		else
		{
			moveToOutstandingTransportsActionWien.setEnabled(true);
			cancelTransportActionWien.setEnabled(true);
			copyTransportActionWien.setEnabled(true);
		}
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus()  { }

	public void propertyChange(PropertyChangeEvent evt) 
	{
		// the viewer represents simple model. refresh should be enough.
		if ("TRANSPORT_ADD".equals(evt.getPropertyName())
				|| "TRANSPORT_REMOVE".equals(evt.getPropertyName())
				|| "TRANSPORT_UPDATE".equals(evt.getPropertyName())
				|| "TRANSPORT_CLEARED".equals(evt.getPropertyName())) 
		{
			viewerLeoben.refresh();
			viewerBruck.refresh();
			viewerGraz.refresh();
			viewerKapfenberg.refresh();
			viewerMariazell.refresh();
			viewerWien.refresh();
		}
		//listen to changes of the date to set up the filter
		if("TRANSPORT_DATE_CHANGED".equalsIgnoreCase(evt.getPropertyName()))
		{
			//get the new value
			this.filteredDate = (Calendar)evt.getNewValue();
			//set up the new view filter
			resetFilters();
			applyFilters(filteredDate);
		}

		//listen to filter events
		if("TRANSPORT_FILTER_CHANGED".equalsIgnoreCase(evt.getPropertyName()))
		{
			//get the new filter
			TransportViewFilter filter = (TransportViewFilter)evt.getNewValue();
			//remove all filters and apply the new
			resetFilters();
			applyFilters(filteredDate);
			applySearchFilter(filter);
		}
		
		//listen to lock changes
		if("LOCK_ADD".equalsIgnoreCase(evt.getPropertyName()) || "LOCK_REMOVE".equalsIgnoreCase(evt.getPropertyName()))
		{
			//refresh the views
			viewerBruck.refresh();
			viewerGraz.refresh();
			viewerKapfenberg.refresh();
			viewerLeoben.refresh();
			viewerMariazell.refresh();
			viewerWien.refresh();
		}
	}

	/***********************************
	 * Helper methods to reduce the code
	 **********************************/

	/**
	 * Creates and returns the table viewer.
	 * @param parent the parent composite to insert
	 * @return the created table
	 */
	private TableViewer createTableViewer(Composite parent)
	{
		final TableViewer viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL|SWT.FULL_SELECTION);
		viewer.setContentProvider(new PrebookingViewContentProvider());
		viewer.setLabelProvider(new PrebookingViewLabelProvider());
		viewer.setInput(ModelFactory.getInstance().getTransportManager().toArray());
		viewer.getTable().setLinesVisible(true);
		
		viewer.getTable().addMouseListener(new MouseAdapter() 
		{
			public void mouseDown(MouseEvent e) 
			{
				if( viewer.getTable().getItem(new Point(e.x,e.y))==null ) 
				{
					viewer.setSelection(new StructuredSelection());
				}
			}
		});

		final Table table = viewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		final TableColumn blockColumn = new TableColumn(table, SWT.NONE);
		blockColumn.setToolTipText("Eintrag wird gerade bearbeitet");
		blockColumn.setWidth(24);
		blockColumn.setText("L");
	

		final TableColumn bTableColumnOrtsstelle = new TableColumn(table, SWT.NONE);
		bTableColumnOrtsstelle.setWidth(30);
		bTableColumnOrtsstelle.setText("OS");

		final TableColumn bTableColumnAbfahrt = new TableColumn(table, SWT.NONE);
		bTableColumnAbfahrt.setToolTipText("Geplante Abfahrt an der Ortsstelle");
		bTableColumnAbfahrt.setWidth(43);
		bTableColumnAbfahrt.setText("Abf");

		final TableColumn bTableColumnAnkunft = new TableColumn(table, SWT.NONE);
		bTableColumnAnkunft.setToolTipText("Geplante Ankunft beim Patienten");
		bTableColumnAnkunft.setWidth(43);
		bTableColumnAnkunft.setText("Ank");

		final TableColumn bTableColumnTermin = new TableColumn(table, SWT.NONE);
		bTableColumnTermin.setToolTipText("Termin am Zielort");
		bTableColumnTermin.setWidth(43);
		bTableColumnTermin.setText("Termin");

		final TableColumn bTableColumnTransportVon = new TableColumn(table, SWT.NONE);
		bTableColumnTransportVon.setWidth(188);
		bTableColumnTransportVon.setText("Transport von");

		final TableColumn bTtableColumnPatient = new TableColumn(table, SWT.NONE);
		bTtableColumnPatient.setWidth(160);
		bTtableColumnPatient.setText("Patient");

		final TableColumn bTableColumnTransportNach = new TableColumn(table, SWT.NONE);
		bTableColumnTransportNach.setWidth(188);
		bTableColumnTransportNach.setText("Transport nach");

		final TableColumn bTableColumnTA = new TableColumn(table, SWT.NONE);
		bTableColumnTA.setToolTipText("Transportart");
		bTableColumnTA.setWidth(23);
		bTableColumnTA.setText("T");
		
		final TableColumn anmerkungTransporte = new TableColumn(table, SWT.NONE);
		anmerkungTransporte.setWidth(66);
		anmerkungTransporte.setText("Anmerkung");

		Listener sortListener = new Listener() 
		{
			public void handleEvent(Event e) 
			{
				// determine new sort column and direction
				TableColumn sortColumn = viewer.getTable().getSortColumn();
				TableColumn currentColumn = (TableColumn) e.widget;
				int dir = viewer.getTable().getSortDirection();
				//revert the sort order if the column is the same
				if (sortColumn == currentColumn) 
				{
					if(dir == SWT.UP)
						dir = SWT.DOWN;
					else
						dir = SWT.UP;
				} 
				else 
				{
					viewer.getTable().setSortColumn(currentColumn);
					dir = SWT.UP;
				}
				// sort the data based on column and direction
				String sortIdentifier = null;
				if (currentColumn == bTableColumnOrtsstelle) 
					sortIdentifier = TransportSorter.RESP_STATION_SORTER;
				if (currentColumn == bTableColumnAbfahrt) 
					sortIdentifier = TransportSorter.ABF_SORTER;
				if (currentColumn == bTableColumnAnkunft) 
					sortIdentifier = TransportSorter.AT_PATIENT_SORTER;
				if (currentColumn == bTableColumnTermin)
					sortIdentifier = TransportSorter.TERM_SORTER;
				if (currentColumn == bTableColumnTransportVon)
					sortIdentifier = TransportSorter.TRANSPORT_FROM_SORTER;
				if(currentColumn == bTtableColumnPatient)
					sortIdentifier = TransportSorter.PATIENT_SORTER;
				if(currentColumn == bTableColumnTransportNach)
					sortIdentifier = TransportSorter.TRANSPORT_TO_SORTER;
				if(currentColumn == bTableColumnTA)
					sortIdentifier = TransportSorter.TA_SORTER;
				if(currentColumn == anmerkungTransporte)
					sortIdentifier = TransportSorter.NOTES_SORTER;
				
				//apply the filter
				viewer.getTable().setSortDirection(dir);
				viewer.setSorter(new TransportSorter(sortIdentifier,dir));
			}
		};

		//attach the listener
		bTableColumnOrtsstelle.addListener(SWT.Selection, sortListener);
		bTableColumnAbfahrt.addListener(SWT.Selection, sortListener);
		bTableColumnAnkunft.addListener(SWT.Selection, sortListener);
		bTableColumnTermin.addListener(SWT.Selection, sortListener);
		bTableColumnTransportVon.addListener(SWT.Selection, sortListener);
		bTtableColumnPatient.addListener(SWT.Selection, sortListener);
		bTableColumnTransportNach.addListener(SWT.Selection, sortListener);
		bTableColumnTA.addListener(SWT.Selection, sortListener);
		anmerkungTransporte.addListener(SWT.Selection, sortListener);

		return viewer;
	}

	/**
	 * Creates the selection listener to show the tooltip.
	 * @param viewer the viewer to listet to changes
	 * @param tooltip the tooltip to show
	 * @return the created listener
	 */
	private ISelectionChangedListener createTooltipListener(final TableViewer viewer,final JournalViewTooltip tooltip)
	{
		return new ISelectionChangedListener() 
		{
			public void selectionChanged(SelectionChangedEvent event) 
			{
				TableItem[] selection = viewer.getTable().getSelection();
				if (selection != null && selection.length > 0) 
				{
					Rectangle bounds = selection[0].getBounds();
					tooltip.show(new Point(bounds.x, bounds.y));
				}
			}
		};
	}

	/**
	 * Removes all the filters from the table
	 */
	public void resetFilters()
	{
		//remove all filters
		viewerBruck.resetFilters();
		viewerGraz.resetFilters();
		viewerWien.resetFilters();
		viewerMariazell.resetFilters();
		viewerKapfenberg.resetFilters();
		viewerLeoben.resetFilters();
	}

	/**
	 * Sets the filter for the dialysis transports 
	 * @param date the date of the transports to render
	 */
	private void applyFilters(Calendar date)
	{
		//set up the filters for the view
		viewerBruck.addFilter(new TransportStateViewFilter(PROGRAM_STATUS_PREBOOKING));
		viewerGraz.addFilter(new TransportStateViewFilter(PROGRAM_STATUS_PREBOOKING));
		viewerWien.addFilter(new TransportStateViewFilter(PROGRAM_STATUS_PREBOOKING));
		viewerMariazell.addFilter(new TransportStateViewFilter(PROGRAM_STATUS_PREBOOKING));
		viewerKapfenberg.addFilter(new TransportStateViewFilter(PROGRAM_STATUS_PREBOOKING));
		viewerLeoben.addFilter(new TransportStateViewFilter(PROGRAM_STATUS_PREBOOKING));
		//apply the filter for the tables
		viewerBruck.addFilter(new TransportDirectnessFilter(TOWARDS_BRUCK));
		viewerGraz.addFilter(new TransportDirectnessFilter(TOWARDS_GRAZ));
		viewerWien.addFilter(new TransportDirectnessFilter(TOWARDS_VIENNA));
		viewerMariazell.addFilter(new TransportDirectnessFilter(TOWARDS_MARIAZELL));
		viewerKapfenberg.addFilter(new TransportDirectnessFilter(TOWARDS_KAPFENBERG));
		viewerLeoben.addFilter(new TransportDirectnessFilter(TOWARDS_LEOBEN));		
		//show only transports from today
		TransportDateFilter dateFilter = new TransportDateFilter(date);
		//set it new
		viewerBruck.addFilter(dateFilter);
		viewerGraz.addFilter(dateFilter);
		viewerWien.addFilter(dateFilter);
		viewerMariazell.addFilter(dateFilter);
		viewerKapfenberg.addFilter(dateFilter);
		viewerLeoben.addFilter(dateFilter);
	}

	/**
	 * Sets the filter when transport is searched
	 * @param filter the filter to apply
	 */
	public void applySearchFilter(TransportViewFilter searchFilter)
	{
		viewerBruck.addFilter(searchFilter);
		viewerGraz.addFilter(searchFilter);
		viewerWien.addFilter(searchFilter);
		viewerMariazell.addFilter(searchFilter);
		viewerKapfenberg.addFilter(searchFilter);
		viewerLeoben.addFilter(searchFilter);
	}
}