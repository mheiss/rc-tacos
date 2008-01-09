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
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
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
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.client.controller.EditTransportAction;
import at.rc.tacos.client.controller.JournalMoveToRunningTransportsAction;
import at.rc.tacos.client.controller.MoveToOutstandingTransportsAction;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.JournalViewContentProvider;
import at.rc.tacos.client.providers.JournalViewLabelProvider;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.client.view.sorterAndTooltip.JournalViewTooltip;
import at.rc.tacos.client.view.sorterAndTooltip.TransportSorter;

import at.rc.tacos.model.Transport;

public class JournalView extends ViewPart implements PropertyChangeListener
{
	public static final String ID = "at.rc.tacos.client.view.journal_view";

	//the toolkit to use
	private FormToolkit toolkit;
	private ScrolledForm form;
	private TableViewer viewer;
	private JournalViewTooltip tooltip;
	
	//the actions for the context menu
	private EditTransportAction editTransportAction;
	private MoveToOutstandingTransportsAction moveToOutstandingTransportsAction;
	private JournalMoveToRunningTransportsAction moveToRunningTransportsAction;
	

	/**
	 * Constructs a new journal view.
	 */
	public JournalView()
	{
		// add listener to model to keep on track. 
		ModelFactory.getInstance().getTransportManager().addPropertyChangeListener(this);
	}
	
	/**
	 * Cleanup the view
	 */
	@Override
	public void dispose() 
	{
		ModelFactory.getInstance().getTransportManager().removePropertyChangeListener(this);
	}

	/**
	 * Callback method to create the control and initialize them.
	 * @param parent the parent composite to add
	 */
	public void createPartControl(final Composite parent) 
	{
		// Create the scrolled parent component
		toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
		form = toolkit.createScrolledForm(parent);
		form.setText("Journalblatt");
		toolkit.decorateFormHeading(form.getForm());
		form.getBody().setLayout(new FillLayout());

		final Composite composite = form.getBody();
		//'Journalblatt'
//		final Group group = new Group(composite, SWT.NONE);
//		group.setLayout(new FillLayout());
//		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//		group.setText("Journalblatt");

//		//tab folder 
//		final TabFolder tabFolder = new TabFolder(group, SWT.NONE);
//		tabFolder.addSelectionListener(new SelectionListener() 
//		{
//			public void widgetSelected(SelectionEvent e) 
//			{
//				//get the selected station
//				String station = tabFolder.getItem(tabFolder.getSelectionIndex()).getText();
//				//remove all filters and add the new
//				viewer.resetFilters();
//				viewer.addFilter(new PersonalViewFilter(station));
//			}
//			public void widgetDefaultSelected(SelectionEvent e) {
//				widgetSelected(e);
//			}
//		});
		
		/** Selection listener? */
		
		viewer = new TableViewer(composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL|SWT.FULL_SELECTION);
		viewer.setContentProvider(new JournalViewContentProvider());
		viewer.setLabelProvider(new JournalViewLabelProvider());
		viewer.setInput(ModelFactory.getInstance().getTransportManager());
		viewer.getTable().setLinesVisible(true);
		
		//set the tooltip
		tooltip = new JournalViewTooltip(viewer.getControl());
		
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
		//TODO- bug fix: causes an error when the view gets updated--> only if the cells are empty	
		viewer.setSorter(new TransportSorter(TransportSorter.TRANSPORT_FROM_SORTER,SWT.DOWN));

		//create the table for the transports
		final Table table = viewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		

		final TableColumn lockColumn = new TableColumn(table, SWT.NONE);
		lockColumn.setToolTipText("Eintrag wird gerade bearbeitet");
		lockColumn.setWidth(30);
		lockColumn.setText("L");

		final TableColumn columnTNrJournal = new TableColumn(table, SWT.NONE);
		columnTNrJournal.setMoveable(true);
		columnTNrJournal.setToolTipText("Transportnummer");
		columnTNrJournal.setWidth(36);
		columnTNrJournal.setText("TNr");
		
		final TableColumn columnPrioritaetJournal = new TableColumn(table, SWT.NONE);
		columnPrioritaetJournal.setToolTipText("A (NEF), B (BD1), C (Transport), D (Rücktransport), E (Heimtransport), F (Sonstiges), E (NEF extern)");
		columnPrioritaetJournal.setWidth(29);
		columnPrioritaetJournal.setText("Pr");

		final TableColumn columnTransportVonJournal = new TableColumn(table, SWT.NONE);
		columnTransportVonJournal.setMoveable(true);
		columnTransportVonJournal.setWidth(39);
		columnTransportVonJournal.setText("Transport von");

		final TableColumn columnPatientJournal = new TableColumn(table, SWT.NONE);
		columnPatientJournal.setMoveable(true);
		columnPatientJournal.setWidth(38);
		columnPatientJournal.setText("Patient");

		final TableColumn columnTransportNachJournal = new TableColumn(table, SWT.NONE);
		columnTransportNachJournal.setWidth(30);
		columnTransportNachJournal.setText("Transport nach");

		final TableColumn columnErkrVerlJournal = new TableColumn(table, SWT.NONE);
		columnErkrVerlJournal.setWidth(23);
		columnErkrVerlJournal.setText("Erkr/Verl");
		
		final TableColumn columnAEJournal = new TableColumn(table, SWT.NONE);
		columnAEJournal.setToolTipText("Auftrag erteilt");
		columnAEJournal.setWidth(39);
		columnAEJournal.setText("AE");
		
		final TableColumn columnS1Journal = new TableColumn(table, SWT.NONE);
		columnS1Journal.setToolTipText("Transportbeginn");
		columnS1Journal.setWidth(36);
		columnS1Journal.setText("S1");

		final TableColumn columnS2Journal = new TableColumn(table, SWT.NONE);
		columnS2Journal.setToolTipText("Bei Patient");
		columnS2Journal.setWidth(38);
		columnS2Journal.setText("S2");

		final TableColumn columnS3Journal = new TableColumn(table, SWT.NONE);
		columnS3Journal.setToolTipText("Abfahrt mit Patient");
		columnS3Journal.setWidth(40);
		columnS3Journal.setText("S3");

		final TableColumn columnS4Journal = new TableColumn(table, SWT.NONE);
		columnS4Journal.setToolTipText("Ankunft Zielort");
		columnS4Journal.setWidth(36);
		columnS4Journal.setText("S4");

		final TableColumn columnS5Journal = new TableColumn(table, SWT.NONE);
		columnS5Journal.setToolTipText("Ziel frei");
		columnS5Journal.setWidth(46);
		columnS5Journal.setText("S5");

		final TableColumn columnS6Journal = new TableColumn(table, SWT.NONE);
		columnS6Journal.setToolTipText("Fahrzeug eingerückt");
		columnS6Journal.setWidth(36);
		columnS6Journal.setText("S6");
		
		final TableColumn columnFzgJournal = new TableColumn(table, SWT.NONE);
		columnFzgJournal.setWidth(52);
		columnFzgJournal.setText("Fzg");

		final TableColumn columnFahrerJournal = new TableColumn(table, SWT.NONE);
		columnFahrerJournal.setWidth(19);
		columnFahrerJournal.setText("Fahrer");

		final TableColumn columnSaniIJournal = new TableColumn(table, SWT.NONE);
		columnSaniIJournal.setWidth(24);
		columnSaniIJournal.setText("Sanitäter I");

		final TableColumn columnSaniIIJournal = new TableColumn(table, SWT.NONE);
		columnSaniIIJournal.setWidth(35);
		columnSaniIIJournal.setText("Sanitäter II");

		final TableColumn columnAnruferJournal = new TableColumn(table, SWT.NONE);
		columnAnruferJournal.setWidth(23);
		columnAnruferJournal.setText("Anrufer");



		//make the columns sort able
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
				if(currentColumn == columnAEJournal)
					sortIdentifier = TransportSorter.AE_SORTER;
				if(currentColumn == columnS1Journal)
					sortIdentifier = TransportSorter.S1_SORTER;
				if(currentColumn == columnS2Journal)
					sortIdentifier = TransportSorter.S2_SORTER;
				if(currentColumn == columnS3Journal)
					sortIdentifier = TransportSorter.S3_SORTER;
				if(currentColumn == columnS4Journal)
					sortIdentifier = TransportSorter.S4_SORTER;
				if(currentColumn == columnS5Journal)
					sortIdentifier = TransportSorter.S5_SORTER;
				if(currentColumn == columnS6Journal)
					sortIdentifier = TransportSorter.S6_SORTER;
				if(currentColumn == columnFzgJournal)
					sortIdentifier = TransportSorter.VEHICLE_SORTER;
				if(currentColumn == columnFahrerJournal)
					sortIdentifier = TransportSorter.DRIVER_SORTER;
				if(currentColumn == columnSaniIJournal)
					sortIdentifier = TransportSorter.PARAMEDIC_I_SORTER;
				if(currentColumn == columnSaniIIJournal)
					sortIdentifier = TransportSorter.PARAMEDIC_II_SORTER;
				if(currentColumn == columnAnruferJournal)
					sortIdentifier = TransportSorter.CALLER_SORTER;
				
				//apply the filter
				viewer.getTable().setSortDirection(dir);
				viewer.setSorter(new TransportSorter(sortIdentifier,dir));
			}
		};

		//attach the listener
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
		columnAnruferJournal.addListener(SWT.Selection, sortListener);
		
		//create the actions
		makeActions();
		hookContextMenu();
//		contributeToActionBars();

//		tabFolder.setSelection(1);
//		tabFolder.setSelection(0);
		
		viewer.refresh();
	}
	
	/**
	 * Creates the needed actions
	 */
	private void makeActions()
	{		
		editTransportAction = new EditTransportAction(this.viewer, "journal");
		moveToOutstandingTransportsAction = new MoveToOutstandingTransportsAction(this.viewer);
		moveToRunningTransportsAction = new JournalMoveToRunningTransportsAction(this.viewer);
	}
	
	/**
	 * Creates the context menue 
	 */
	private void hookContextMenu() 
	{
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
	private void fillContextMenu(IMenuManager manager)
	{
		//get the selected object
		final Object firstSelectedObject = ((IStructuredSelection) viewer.getSelection()).getFirstElement();
			
		//cast to a Transport
		Transport transport = (Transport)firstSelectedObject;
		
		if(transport == null)
			return;
		
		//add the actions
		manager.add(editTransportAction);
		manager.add(new Separator());
		manager.add(moveToOutstandingTransportsAction);
		manager.add(moveToRunningTransportsAction);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus()  { }

	public void propertyChange(PropertyChangeEvent evt) 
	{
		// the viewer represents simple model. refresh should be enough.
		if ("TRANSPORT_ADD".equals(evt.getPropertyName())) 
			this.viewer.refresh();
		// event on deletion --> also just refresh
		if ("TRANSPORT_REMOVE".equals(evt.getPropertyName())) 
			this.viewer.refresh();
		// event on deletion --> also just refresh
		if ("TRANSPORT_UPDATE".equals(evt.getPropertyName())) 
			this.viewer.refresh();
		// event on deletion --> also just refresh
		if ("TRANSPORT_CLEARED".equals(evt.getPropertyName())) 
			this.viewer.refresh();
	}
}
