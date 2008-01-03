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
import at.rc.tacos.client.modelManager.ModelFactory;

import at.rc.tacos.client.providers.DialysisTransportContentProvider;
import at.rc.tacos.client.providers.DialysisTransportLabelProvider;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.client.view.sorterAndTooltip.DialysisTransportSorter;
import at.rc.tacos.client.view.sorterAndTooltip.OutstandingTransportsTooltip;
import at.rc.tacos.client.view.sorterAndTooltip.TransportSorter;
import at.rc.tacos.model.DialysisPatient;


public class DialysisView extends ViewPart implements PropertyChangeListener
{
	public static final String ID = "at.rc.tacos.client.view.dialysis_view";

	
	//the toolkit to use
	private FormToolkit toolkit;
	private ScrolledForm form;
	private TableViewer viewer;
	private OutstandingTransportsTooltip tooltip;
	
	//the actions for the context menu
	//TODO - get working ;-)


	/**
	 * Constructs a new outstanding transports view.
	 */
	public DialysisView()
	{
		// add listener to model to keep on track. 
		ModelFactory.getInstance().getDialysisTransportManager().addPropertyChangeListener(this);
	}
	
	/**
	 * Cleanup the view
	 */
	@Override
	public void dispose() 
	{
		ModelFactory.getInstance().getDialysisTransportManager().removePropertyChangeListener(this);
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
		form.setText("Dialysetransporte");
		toolkit.decorateFormHeading(form.getForm());
		form.getBody().setLayout(new FillLayout());

		final Composite composite = form.getBody();
		
		/** tabFolder Selection Listener not needed? */
		
		
		
		viewer= new TableViewer(composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL|SWT.FULL_SELECTION);
		viewer.setContentProvider(new DialysisTransportContentProvider());
		viewer.setLabelProvider(new DialysisTransportLabelProvider());
		viewer.setInput(ModelFactory.getInstance().getTransportManager());
		viewer.getTable().setLinesVisible(true);
		
		viewer.refresh();
		
		/** tool tip*/
		tooltip = new OutstandingTransportsTooltip(viewer.getControl());
		//show the tool tip when the selection has changed
		
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
		
		
		/** default sorter*/
		viewer.setSorter(new TransportSorter(DialysisTransportSorter.ABF_SORTER,SWT.DOWN));
		
		
		final Table table_2 = viewer.getTable();
		table_2.setLinesVisible(true);
		table_2.setHeaderVisible(true);
	
		final TableColumn lockColumn = new TableColumn(table_2, SWT.NONE);
		lockColumn.setToolTipText("Eintrag wird gerade bearbeitet");
		lockColumn.setWidth(30);
		lockColumn.setText("L");
	

		final TableColumn newColumnTableColumnAbfDialyse = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnAbfDialyse.setToolTipText("Geplante Abfahrt an Ortsstelle");
		newColumnTableColumnAbfDialyse.setWidth(68);
		newColumnTableColumnAbfDialyse.setText("Abf");
		
		final TableColumn newColumnTableColumnStationDialyse = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnStationDialyse.setToolTipText("Zuständige Ortsstelle");
		newColumnTableColumnStationDialyse.setWidth(68);
		newColumnTableColumnStationDialyse.setText("OS");

		final TableColumn newColumnTableColumnAnkDialyse = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnAnkDialyse.setToolTipText("Geplante Ankunft beim Patienten");
		newColumnTableColumnAnkDialyse.setWidth(65);
		newColumnTableColumnAnkDialyse.setText("Ank");

		final TableColumn newColumnTableColumnTerminDialyse = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnTerminDialyse.setToolTipText("Termin auf der Dialyse");
		newColumnTableColumnTerminDialyse.setWidth(68);
		newColumnTableColumnTerminDialyse.setText("Termin");

		final TableColumn newColumnTableColumnRTAbfahrtDialyse = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnRTAbfahrtDialyse.setToolTipText("Abfahrt an der Ortsstelle");
		newColumnTableColumnRTAbfahrtDialyse.setWidth(84);
		newColumnTableColumnRTAbfahrtDialyse.setText("RT Abfahrt");

		final TableColumn newColumnTableColumnAbholbereitDialyse = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnAbholbereitDialyse.setToolTipText("Patient ist mit Dialyse fertig, abholbereit im LKH");
		newColumnTableColumnAbholbereitDialyse.setWidth(100);
		newColumnTableColumnAbholbereitDialyse.setText("Abholbereit");

		final TableColumn newColumnTableColumnWohnortDialyse = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnWohnortDialyse.setWidth(167);
		newColumnTableColumnWohnortDialyse.setText("Wohnort");

		final TableColumn newColumnTableColumnNameDialyse = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnNameDialyse.setWidth(133);
		newColumnTableColumnNameDialyse.setText("Name");

		final TableColumn newColumnTableColumnMontag = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnMontag.setData("newKey", null);
		newColumnTableColumnMontag.setWidth(40);
		newColumnTableColumnMontag.setText("Mo");

		final TableColumn newColumnTableColumnDienstag = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnDienstag.setWidth(37);
		newColumnTableColumnDienstag.setText("Di");

		final TableColumn newColumnTableColumnMittwoch = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnMittwoch.setWidth(36);
		newColumnTableColumnMittwoch.setText("Mi");

		final TableColumn newColumnTableColumnDonnerstag = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnDonnerstag.setWidth(35);
		newColumnTableColumnDonnerstag.setText("Do");

		final TableColumn newColumnTableColumnFreitag = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnFreitag.setWidth(38);
		newColumnTableColumnFreitag.setText("Fr");

		final TableColumn newColumnTableColumnSamstag = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnSamstag.setWidth(42);
		newColumnTableColumnSamstag.setText("Sa");

		final TableColumn newColumnTableColumnSonntag = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnSonntag.setWidth(43);
		newColumnTableColumnSonntag.setText("So");
		
		final TableColumn newColumnTableColumnTA = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnTA.setWidth(43);
		newColumnTableColumnTA.setText("TA");

		final TableColumn newColumnTableColumnStationaer = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnStationaer.setToolTipText("Patient wird derzeit nicht transportiert");
		newColumnTableColumnStationaer.setWidth(49);
		newColumnTableColumnStationaer.setText("Stat");

		
	
	
		/** make the columns sort able*/
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
				if(currentColumn == newColumnTableColumnNameDialyse)
					sortIdentifier = DialysisTransportSorter.PATIENT_SORTER;
				if(currentColumn == newColumnTableColumnRTAbfahrtDialyse)
					sortIdentifier = DialysisTransportSorter.RT_SORTER;
				if(currentColumn == newColumnTableColumnAbholbereitDialyse)
					sortIdentifier = DialysisTransportSorter.READY_SORTER;
				if(currentColumn == newColumnTableColumnTA)
					sortIdentifier = DialysisTransportSorter.TA_SORTER;


				
				//apply the filter
				viewer.getTable().setSortDirection(dir);
				viewer.setSorter(new DialysisTransportSorter(sortIdentifier,dir));
			}
		};
		
		
		//attach the listener
		newColumnTableColumnStationDialyse.addListener(SWT.Selection, sortListener);
		newColumnTableColumnAbfDialyse.addListener(SWT.Selection, sortListener);
		newColumnTableColumnAnkDialyse.addListener(SWT.Selection, sortListener);
		newColumnTableColumnTerminDialyse.addListener(SWT.Selection, sortListener);
		newColumnTableColumnWohnortDialyse.addListener(SWT.Selection, sortListener);
		newColumnTableColumnNameDialyse.addListener(SWT.Selection, sortListener);
		newColumnTableColumnRTAbfahrtDialyse.addListener(SWT.Selection, sortListener);
		newColumnTableColumnAbholbereitDialyse.addListener(SWT.Selection, sortListener);
		newColumnTableColumnTA.addListener(SWT.Selection, sortListener);
		
		makeActions();
		hookContextMenu();
		
//		viewerOffTrans.resetFilters();
	}

	
	
	/**
	 * Creates the needed actions
	 */
	private void makeActions()
	{
//		forwardTransportAction = new ForwardTransportAction(this.viewerOffTrans);
//		editTransportAction = new EditTransportAction(this.viewerOffTrans);
//		cancelTransportAction = new CancelTransportAction(this.viewerOffTrans);
//		
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
			
		//cast to a dialysis transport
		DialysisPatient dia = (DialysisPatient)firstSelectedObject;
		
		if(dia == null)
			return;
		
		//add the actions
//		manager.add(forwardTransportAction);
//		manager.add(editTransportAction);
		manager.add(new Separator());
//		manager.add(cancelTransportAction);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus()  { }
	
	public void propertyChange(PropertyChangeEvent evt) 
	{
		// the viewer represents simple model. refresh should be enough.
		if ("DIALYSISTRANSPORT_ADD".equals(evt.getPropertyName())) 
		{ 
			System.out.println("OutstandingTransportsView, probertychange........ TRANSPORT_ADD");
			this.viewer.refresh();
		}
		// event on deletion --> also just refresh
		if ("DIALYSISTRANSPORT_REMOVE".equals(evt.getPropertyName())) 
		{ 
			this.viewer.refresh();
		}
		// event on deletion --> also just refresh
		if ("DIALYSISTRANSPORT_UPDATE".equals(evt.getPropertyName())) 
		{ 
			this.viewer.refresh();
		}
		// event on deletion --> also just refresh
		if ("DIALYSISTRANSPORT_CLEARED".equals(evt.getPropertyName())) 
		{ 
			this.viewer.refresh();
		}
	}
}