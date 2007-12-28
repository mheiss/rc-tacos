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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.client.controller.CancelTransportAction;
import at.rc.tacos.client.controller.ChangeResponsibleStationAction;
import at.rc.tacos.client.controller.EditTransportAction;
import at.rc.tacos.client.controller.ForwardTransportAction;
import at.rc.tacos.client.controller.PersonalCancelSignInAction;
import at.rc.tacos.client.controller.PersonalCancelSignOutAction;
import at.rc.tacos.client.controller.PersonalDeleteEntryAction;
import at.rc.tacos.client.controller.PersonalEditEntryAction;
import at.rc.tacos.client.controller.PersonalSignInAction;
import at.rc.tacos.client.controller.PersonalSignOutAction;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.DispositionViewOffContentProvider;
import at.rc.tacos.client.providers.DispositionViewOffLabelProvider;
import at.rc.tacos.client.providers.OutstandingTransportsViewContentProvider;
import at.rc.tacos.client.providers.OutstandingTransportsViewLabelProvider;
import at.rc.tacos.client.providers.TransportSorter;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.model.Transport;


public class OutstandingTransportsView extends ViewPart implements PropertyChangeListener
{
	public static final String ID = "at.rc.tacos.client.view.outstandingTransports_view";

	
	//the toolkit to use
	private FormToolkit toolkit;
	private ScrolledForm form;
	private TableViewer viewerOffTrans;
	private OutstandingTransportsTooltip tooltip;
	
	//the actions for the context menu
	//TODO - get working ;-)
//	private ChangeResponsibleStationAction changeResponsibleStationAction;
//	private AssignCarAction assignCarAction;
//	private CopyTransportAction copyTransportAction;
	private ForwardTransportAction forwardTransportAction;
	private CancelTransportAction cancelTransportAction;
	private EditTransportAction editTransportAction;
	
	
	
	
	
	
	
	/**
	 * Constructs a new outstanding transports view.
	 */
	public OutstandingTransportsView()
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
	 * Call back method to create the control and initialize them.
	 * @param parent the parent composite to add
	 */
	public void createPartControl(final Composite parent) 
	{
		// Create the scrolled parent component
		toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
		form = toolkit.createScrolledForm(parent);
		form.setText("Offene Transporte");
		toolkit.decorateFormHeading(form.getForm());
		form.getBody().setLayout(new FillLayout());

		final Composite composite = form.getBody();
		
		/** tabFolder Selection Listener not needed? */
		
		
		
		viewerOffTrans = new TableViewer(composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL|SWT.FULL_SELECTION);
		viewerOffTrans.setContentProvider(new OutstandingTransportsViewContentProvider());
		viewerOffTrans.setLabelProvider(new OutstandingTransportsViewLabelProvider());
		viewerOffTrans.setInput(ModelFactory.getInstance().getTransportManager());
		viewerOffTrans.getTable().setLinesVisible(true);
		
		viewerOffTrans.refresh();
		
		/** tool tip*/
		tooltip = new OutstandingTransportsTooltip(viewerOffTrans.getControl());
		//show the tool tip when the selection has changed
		
		viewerOffTrans.addSelectionChangedListener(new ISelectionChangedListener() 
		{
			public void selectionChanged(SelectionChangedEvent event) 
			{
				TableItem[] selection = viewerOffTrans.getTable().getSelection();
				if (selection != null && selection.length > 0) 
				{
					Rectangle bounds = selection[0].getBounds();
					tooltip.show(new Point(bounds.x, bounds.y));
				}
			}
		});  
		
		
		/** sorter*/
		viewerOffTrans.setSorter(new TransportSorter(TransportSorter.ABF_SORTER,SWT.DOWN));
		
		
		final Table tableOff = viewerOffTrans.getTable();
		tableOff.setLinesVisible(true);
		tableOff.setHeaderVisible(true);
	
		final TableColumn lockColumn = new TableColumn(tableOff, SWT.NONE);
		lockColumn.setToolTipText("Eintrag wird gerade bearbeitet");
		lockColumn.setWidth(30);
		lockColumn.setText("L");
	
		final TableColumn prioritaetOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		prioritaetOffeneTransporte.setToolTipText("A (NEF), B (BD1), C (Transport), D (Rücktransport), E (Heimtransport), F (Sonstiges), E (NEF extern)");
		prioritaetOffeneTransporte.setWidth(31);
		prioritaetOffeneTransporte.setText("Pr");
	
		final TableColumn respOSOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
//		respOSOffeneTransporte.setToolTipText("Zuständige Ortsstelle");
		respOSOffeneTransporte.setWidth(49);
		respOSOffeneTransporte.setText("OS");
	
		final TableColumn abfOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
//		abfOffeneTransporte.setToolTipText("Abfahrt auf der Dienststelle");
		abfOffeneTransporte.setWidth(54);
		abfOffeneTransporte.setText("Abf");
	
		final TableColumn ankOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
//		ankOffeneTransporte.setToolTipText("Ankunft beim Patienten");
		ankOffeneTransporte.setWidth(53);
		ankOffeneTransporte.setText("Ank.");
	
		final TableColumn terminOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
//		terminOffeneTransporte.setToolTipText("Terminzeit am Zielort");
		terminOffeneTransporte.setWidth(57);
		terminOffeneTransporte.setText("Termin");
	
		final TableColumn transportVonOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		transportVonOffeneTransporte.setWidth(102);
		transportVonOffeneTransporte.setText("Transport von");
	
		final TableColumn patientOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		patientOffeneTransporte.setWidth(100);
		patientOffeneTransporte.setText("Patient");
	
		final TableColumn transportNachOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		transportNachOffeneTransporte.setWidth(100);
		transportNachOffeneTransporte.setText("Transport nach");
	
		final TableColumn aufgOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		aufgOffeneTransporte.setToolTipText("Zeit zu der der Transport aufgenommen wurde");
		aufgOffeneTransporte.setWidth(100);
		aufgOffeneTransporte.setText("Aufg");
	
		final TableColumn tOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		tOffeneTransporte.setToolTipText("Transportart");
		tOffeneTransporte.setWidth(100);
		tOffeneTransporte.setText("T");
	
		final TableColumn erkrankungVerletzungOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		erkrankungVerletzungOffeneTransporte.setWidth(74);
		erkrankungVerletzungOffeneTransporte.setText("Erkrankung/Verletzung");
	
		final TableColumn anmerkungOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		anmerkungOffeneTransporte.setWidth(71);
		anmerkungOffeneTransporte.setText("Anmerkung");
	
	
		/** make the columns sort able*/
		Listener sortListener = new Listener() 
		{
			public void handleEvent(Event e) 
			{
				// determine new sort column and direction
				TableColumn sortColumn = viewerOffTrans.getTable().getSortColumn();
				TableColumn currentColumn = (TableColumn) e.widget;
				int dir = viewerOffTrans.getTable().getSortDirection();
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
				if(currentColumn == patientOffeneTransporte)
					sortIdentifier = TransportSorter.PATIENT_SORTER;
				if(currentColumn == transportNachOffeneTransporte)
					sortIdentifier = TransportSorter.TRANSPORT_TO_SORTER;
				if(currentColumn == aufgOffeneTransporte)
					sortIdentifier = TransportSorter.AUFG_SORTER;
				if(currentColumn == tOffeneTransporte)
					sortIdentifier = TransportSorter.TA_SORTER;
				if(currentColumn == erkrankungVerletzungOffeneTransporte)
					sortIdentifier = TransportSorter.KIND_OF_ILLNESS_SORTER;

				
				//apply the filter
				viewerOffTrans.getTable().setSortDirection(dir);
				viewerOffTrans.setSorter(new TransportSorter(sortIdentifier,dir));
			}
		};
		
		
		//attach the listener
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
		
		
		makeActions();
		hookContextMenu();
		
		viewerOffTrans.resetFilters();
	}

	
	
	/**
	 * Creates the needed actions
	 */
	private void makeActions()
	{
		forwardTransportAction = new ForwardTransportAction(this.viewerOffTrans);
		editTransportAction = new EditTransportAction(this.viewerOffTrans);
		cancelTransportAction = new CancelTransportAction(this.viewerOffTrans);
		
	}
	
	/**
	 * Creates the context menu
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
		Menu menu = menuManager.createContextMenu(viewerOffTrans.getControl());
		viewerOffTrans.getControl().setMenu(menu);
		getSite().registerContextMenu(menuManager, viewerOffTrans);
	}
	
	/**
	 * Fills the context menu with the actions
	 */
	private void fillContextMenu(IMenuManager manager)
	{
		//get the selected object
		final Object firstSelectedObject = ((IStructuredSelection) viewerOffTrans.getSelection()).getFirstElement();
			
		//cast to a RosterEntry
		Transport transport = (Transport)firstSelectedObject;
		
		if(transport == null)
			return;
		
		//add the actions
		manager.add(forwardTransportAction);
		manager.add(editTransportAction);
		manager.add(new Separator());
		manager.add(cancelTransportAction);
		
	}
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus()  { }
	
	public void propertyChange(PropertyChangeEvent evt) 
	{
		// the viewer represents simple model. refresh should be enough.
		if ("TRANSPORT_ADD".equals(evt.getPropertyName())) 
		{ 
			this.viewerOffTrans.refresh();
		}
		// event on deletion --> also just refresh
		if ("TRANSPORT_REMOVE".equals(evt.getPropertyName())) 
		{ 
			this.viewerOffTrans.refresh();
		}
		// event on deletion --> also just refresh
		if ("TRANSPORT_UPDATE".equals(evt.getPropertyName())) 
		{ 
			this.viewerOffTrans.refresh();
		}
		// event on deletion --> also just refresh
		if ("TRANSPORT_CLEARED".equals(evt.getPropertyName())) 
		{ 
			this.viewerOffTrans.refresh();
		}
	}
}