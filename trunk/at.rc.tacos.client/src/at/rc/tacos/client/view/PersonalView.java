package at.rc.tacos.client.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.client.controller.PersonalCancelSignInAction;
import at.rc.tacos.client.controller.PersonalCancelSignOutAction;
import at.rc.tacos.client.controller.PersonalDeleteEntryAction;
import at.rc.tacos.client.controller.PersonalEditEntryAction;
import at.rc.tacos.client.controller.PersonalSignInAction;
import at.rc.tacos.client.controller.PersonalSignOutAction;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.PersonalViewContentProvider;
import at.rc.tacos.client.providers.PersonalViewFilter;
import at.rc.tacos.client.providers.PersonalViewLabelProvider;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.common.Constants;
import at.rc.tacos.model.RosterEntry;

public class PersonalView extends ViewPart implements PropertyChangeListener
{
	public static final String ID = "at.rc.tacos.client.view.personal_view";

	//the toolkit to use
	private FormToolkit toolkit;
	private ScrolledForm form;
	private TableViewer viewer;
	private PersonalTooltip tooltip;
	
	//the actions for the context menu
	private PersonalCancelSignInAction cancelSignInAction;
	private PersonalCancelSignOutAction cancelSignOutAction;
	private PersonalSignInAction signInAction;
	private PersonalSignOutAction signOutAction;
	private PersonalEditEntryAction editEntryAction;
	private PersonalDeleteEntryAction deleteEntryAction;

	/**
	 * Constructs a new persoal view.
	 */
	public PersonalView()
	{
		// add listener to model to keep on track. 
		ModelFactory.getInstance().getRosterManager().addPropertyChangeListener(this);
	}
	
	/**
	 * Cleanup the view
	 */
	@Override
	public void dispose() 
	{
		ModelFactory.getInstance().getRosterManager().removePropertyChangeListener(this);
	}

	/**
	 * Callback method to create the control and initalize them.
	 * @param parent the parent composite to add
	 */
	public void createPartControl(final Composite parent) 
	{
		// Create the scrolled parent component
		toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
		form = toolkit.createScrolledForm(parent);
		form.setText("Personalübersicht");
		toolkit.decorateFormHeading(form.getForm());
		form.getBody().setLayout(new FillLayout());

		final Composite composite = form.getBody();
		//'Dienstplan'
		final Group group = new Group(composite, SWT.NONE);
		group.setLayout(new FillLayout());
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		group.setText("Dienstplan");

		//tab folder "Bruck - Kapfenberg"
		final TabFolder tabFolder = new TabFolder(group, SWT.NONE);
		tabFolder.addSelectionListener(new SelectionListener() 
		{
			public void widgetSelected(SelectionEvent e) 
			{
				//get the selected station
				String station = tabFolder.getItem(tabFolder.getSelectionIndex()).getText();
				//remove all filters and add the new
				viewer.resetFilters();
				viewer.addFilter(new PersonalViewFilter(station));
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		viewer = new TableViewer(tabFolder, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL|SWT.FULL_SELECTION);
		viewer.setContentProvider(new PersonalViewContentProvider());
		viewer.setLabelProvider(new PersonalViewLabelProvider());
		viewer.setInput(ModelFactory.getInstance().getRosterManager());
		viewer.getTable().setLinesVisible(true);
		//set the tooltip
		tooltip = new PersonalTooltip(viewer.getControl());
		//show the tooltip when the selection has changed
		viewer.addSelectionChangedListener(new ISelectionChangedListener() 
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
		});     
		//sort the table by default
		viewer.setSorter(new PersonalViewSorter(PersonalViewSorter.WORKTIME_SORTER,SWT.DOWN));

		//create the table for the roster entries 
		final Table table = viewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		final TableColumn lockColumn = new TableColumn(table, SWT.NONE);
		lockColumn.setToolTipText("Eintrag wird gerade bearbeitet");
		lockColumn.setWidth(30);
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
		columnService.setWidth(75);
		columnService.setText("DV");

		final TableColumn columnJob = new TableColumn(table, SWT.NONE);
		columnJob.setToolTipText("Verwendung");
		columnJob.setWidth(65);
		columnJob.setText("V");

		final TableColumn columnStation = new TableColumn(table, SWT.NONE);
		columnStation.setToolTipText("Ortsstelle, an der der Mitarbeiter Dienst macht");
		columnStation.setWidth(75);
		columnStation.setText("OS");

		final TableColumn columnVehicle = new TableColumn(table, SWT.NONE);
		columnVehicle.setToolTipText("Fahrzeug, dem der Mitarbeiter zugewiesen ist");
		columnVehicle.setWidth(40);
		columnVehicle.setText("Fzg");

		//make the columns sortable
		Listener sortListener = new Listener() 
		{
			public void handleEvent(Event e) 
			{
				// determine new sort column and direction
				TableColumn sortColumn = viewer.getTable().getSortColumn();
				TableColumn currentColumn = (TableColumn) e.widget;
				int dir = viewer.getTable().getSortDirection();
				//revert the sortorder if the column is the same
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
				if(currentColumn == columnStation)
					sortIdentifier = PersonalViewSorter.STATION_SORTER;
				//apply the filter
				viewer.getTable().setSortDirection(dir);
				viewer.setSorter(new PersonalViewSorter(sortIdentifier,dir));
			}
		};

		//attach the listener
		columnStaffName.addListener(SWT.Selection, sortListener);
		columnWorkTime.addListener(SWT.Selection, sortListener);
		columnCheckin.addListener(SWT.Selection, sortListener);
		columnCheckout.addListener(SWT.Selection, sortListener);
		columnService.addListener(SWT.Selection, sortListener);
		columnJob.addListener(SWT.Selection, sortListener);
		columnStation.addListener(SWT.Selection, sortListener);

		//create the tab items for the personal overview
		final TabItem bezirkTabItem = new TabItem(tabFolder, SWT.NONE);
		bezirkTabItem.setText(Constants.STATION_BEZIRK);
		bezirkTabItem.setControl(table);

		final TabItem bruckmurTabItem = new TabItem(tabFolder, SWT.NONE);
		bruckmurTabItem.setText(Constants.STATION_BRUCK);
		bruckmurTabItem.setControl(table);

		final TabItem kapfenbergTabItem = new TabItem(tabFolder, SWT.NONE);
		kapfenbergTabItem.setText(Constants.STATION_KAPFENBERG);
		kapfenbergTabItem.setControl(table);

		final TabItem stMareinTabItem = new TabItem(tabFolder, SWT.NONE);
		stMareinTabItem.setText(Constants.STATION_MAREIN);
		stMareinTabItem.setControl(table);

		final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText(Constants.STATION_THOERL);
		tabItem.setControl(table);

		final TabItem turnauTabItem = new TabItem(tabFolder, SWT.NONE);
		turnauTabItem.setText(Constants.STATION_TURNAU);
		turnauTabItem.setControl(table);

		final TabItem breitenauTabItem = new TabItem(tabFolder, SWT.NONE);
		breitenauTabItem.setText(Constants.STATION_BREITENAU);
		breitenauTabItem.setControl(table);
		
		//create the actions
		makeActions();
		hookContextMenu();
		contributeToActionBars();

		tabFolder.setSelection(1);
		tabFolder.setSelection(0);
	}
	
	/**
	 * Creates the needed actions
	 */
	private void makeActions()
	{
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
	private void hookContextMenu() 
	{
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
	private void fillContextMenu(IMenuManager manager)
	{
		//get the selected object
		final Object firstSelectedObject = ((IStructuredSelection) viewer.getSelection()).getFirstElement();
			
		//cast to a RosterEntry
		RosterEntry entry = (RosterEntry)firstSelectedObject;
		
		if(entry == null)
			return;
		
		//add the actions
		manager.add(signInAction);
		manager.add(signOutAction);
		manager.add(new Separator());
		manager.add(editEntryAction);
		manager.add(deleteEntryAction);
		manager.add(new Separator());
		manager.add(cancelSignInAction);
		manager.add(cancelSignOutAction);
		
		//enable or disable the actions
		if(entry.getRealStartOfWork() > 0)
		{
			signInAction.setEnabled(false);
			cancelSignInAction.setEnabled(true);
		}
		else
		{
			signInAction.setEnabled(true);
			cancelSignInAction.setEnabled(false);
		}
		if(entry.getRealEndOfWork() > 0)
		{
			signOutAction.setEnabled(false);
			cancelSignOutAction.setEnabled(true);
		}
		else
		{
			signOutAction.setEnabled(true);
			cancelSignOutAction.setEnabled(false);
		}
	}
	
	/**
	 * Fills the action bar at the rigt side and the tool bar
	 */
	private void contributeToActionBars() 
	{
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}
	
	/**
	 * Fills the pull down bar
	 * @param manager the manager to add the actions
	 */
	private void fillLocalPullDown(IMenuManager manager) 
	{
		manager.add(new Separator());
		manager.add(editEntryAction);
		manager.add(deleteEntryAction);
	}

	private void fillLocalToolBar(IToolBarManager manager)
	{
		manager.add(new Separator());
		manager.add(editEntryAction);
		manager.add(deleteEntryAction);
	}


	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus()  { }

	public void propertyChange(PropertyChangeEvent evt) 
	{
		// the viewer represents simple model. refresh should be enough.
		if ("ROSTERENTRY_ADD".equals(evt.getPropertyName())) 
			this.viewer.refresh();
		// event on deletion --> also just refresh
		if ("ROSTERENTRY_REMOVE".equals(evt.getPropertyName())) 
			this.viewer.refresh();
		// event on deletion --> also just refresh
		if ("ROSTERENTRY_UPDATE".equals(evt.getPropertyName())) 
			this.viewer.refresh();
		// event on deletion --> also just refresh
		if ("ROSTERENTRY_CLEARED".equals(evt.getPropertyName())) 
			this.viewer.refresh();
	}
}
