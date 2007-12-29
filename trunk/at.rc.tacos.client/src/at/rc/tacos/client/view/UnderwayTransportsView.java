package at.rc.tacos.client.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.SubMenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.client.controller.EditTransportAction;
import at.rc.tacos.client.controller.EditTransportStatusAction;
import at.rc.tacos.client.controller.SetTransportStatusAction;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.UnderwayTransportsViewContentProvider;
import at.rc.tacos.client.providers.UnderwayTransportsViewLabelProvider;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.client.view.sorterAndTooltip.TransportSorter;
import at.rc.tacos.client.view.sorterAndTooltip.UnderwayTransportsTooltip;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.model.Transport;

/**
 * Main view, provides an overview about the transports
 * @author b.thek
 */

public class UnderwayTransportsView extends ViewPart implements PropertyChangeListener, ITransportStatus
{
	public static final String ID = "at.rc.tacos.client.view.disposition_view";
	
	private FormToolkit toolkit;
	private ScrolledForm formDisp;
	private TableViewer viewer;
	private UnderwayTransportsTooltip tooltip;
	
	//the actions for the context menu
	private SetTransportStatusAction setTransportStatusS1Action;
	private SetTransportStatusAction setTransportStatusS2Action;
	private SetTransportStatusAction setTransportStatusS3Action;
	private SetTransportStatusAction setTransportStatusS4Action;
	private SetTransportStatusAction setTransportStatusS5Action;
	private SetTransportStatusAction setTransportStatusS6Action;
	private SetTransportStatusAction setTransportStatusS7Action;
	private SetTransportStatusAction setTransportStatusS8Action;
	private SetTransportStatusAction setTransportStatusS9Action;
	private EditTransportStatusAction editTransportStatusAction;
	
	private EditTransportAction editTransportAction;



	public UnderwayTransportsView()
	{
		//add listener to model to keep on track
		ModelFactory.getInstance().getRosterManager().addPropertyChangeListener(this);
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
	 * Call back method to create the control and initialize them
	 * Create contents of the window
	 */
	public void createPartControl(final Composite parent) 
	{
		
		//Create the scrolled parent component
		toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
		formDisp = toolkit.createScrolledForm(parent);
		formDisp.setText("Disponierte Transporte");
		toolkit.decorateFormHeading(formDisp.getForm());
		formDisp.getBody().setLayout(new FillLayout());
		
		final Composite composite = formDisp.getBody();
		

		/** tabFolder Selection Listener not needed? */
		
		viewer = new TableViewer(composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL|SWT.FULL_SELECTION);
		viewer.setContentProvider(new UnderwayTransportsViewContentProvider());
		viewer.setLabelProvider(new UnderwayTransportsViewLabelProvider());
		viewer.setInput(ModelFactory.getInstance().getTransportManager());
		viewer.getTable().setLinesVisible(true);
		
		viewer.refresh();
		
		/** Tool tip */
		tooltip = new UnderwayTransportsTooltip(viewer.getControl());
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
		/** default Sorter */
		viewer.setSorter(new TransportSorter(TransportSorter.ABF_SORTER,SWT.DOWN));
		
		
		final Table tableDisp = viewer.getTable();
		tableDisp.setLinesVisible(true);
		tableDisp.setHeaderVisible(true);
		
	
		final TableColumn lockColumn = new TableColumn(tableDisp, SWT.NONE);
		lockColumn.setToolTipText("Eintrag wird gerade bearbeitet");
		lockColumn.setWidth(30);
		lockColumn.setText("L");
		
		
//		table.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));
		
		//create the tab items for the disposition view
		final TableColumn prioritaetDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		prioritaetDisponierteTransporte.setToolTipText("A (NEF), B (BD1), C (Transport), D (Rücktransport), E (Heimtransport), F (Sonstiges), E (NEF extern)");
		prioritaetDisponierteTransporte.setWidth(36);
		prioritaetDisponierteTransporte.setText("Pr");

		final TableColumn transportNummerDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		transportNummerDisponierteTransporte.setToolTipText("Ortsstellenabhängige Transportnummer");
		transportNummerDisponierteTransporte.setMoveable(true);
		transportNummerDisponierteTransporte.setWidth(54);
		transportNummerDisponierteTransporte.setText("TNr");

		final TableColumn terminDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		terminDisponierteTransporte.setToolTipText("Termin am Zielort");
		terminDisponierteTransporte.setMoveable(true);
		terminDisponierteTransporte.setWidth(48);
		terminDisponierteTransporte.setText("Termin");

		final TableColumn transportVonDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		transportVonDisponierteTransporte.setMoveable(true);
		transportVonDisponierteTransporte.setWidth(118);
		transportVonDisponierteTransporte.setText("Transport von");

		final TableColumn patientDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		patientDisponierteTransporte.setMoveable(true);
		patientDisponierteTransporte.setWidth(100);
		patientDisponierteTransporte.setText("Patient");

		final TableColumn transportNachDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		transportNachDisponierteTransporte.setWidth(100);
		transportNachDisponierteTransporte.setText("Transport nach");

		final TableColumn aeDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		aeDisponierteTransporte.setToolTipText("Auftrag erteilt");
		aeDisponierteTransporte.setWidth(39);
		aeDisponierteTransporte.setText("AE");

		final TableColumn s1DisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		s1DisponierteTransporte.setToolTipText("Transportbeginn");
		s1DisponierteTransporte.setWidth(36);
		s1DisponierteTransporte.setText("S1");

		final TableColumn s2DisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		s2DisponierteTransporte.setToolTipText("Ankunft bei Patient");
		s2DisponierteTransporte.setWidth(38);
		s2DisponierteTransporte.setText("S2");

		final TableColumn s3DisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		s3DisponierteTransporte.setToolTipText("Abfahrt mit Patient");
		s3DisponierteTransporte.setWidth(40);
		s3DisponierteTransporte.setText("S3");

		final TableColumn s4DisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		s4DisponierteTransporte.setToolTipText("Ankunft Ziel");
		s4DisponierteTransporte.setWidth(36);
		s4DisponierteTransporte.setText("S4");

		final TableColumn s7DisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		s7DisponierteTransporte.setToolTipText("Einsatzgebiet verlassen");
		s7DisponierteTransporte.setWidth(40);
		s7DisponierteTransporte.setText("S7");

		final TableColumn s8DisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		s8DisponierteTransporte.setToolTipText("Zurück im Einsatzgebiet");
		s8DisponierteTransporte.setWidth(34);
		s8DisponierteTransporte.setText("S8");

		final TableColumn s9DisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		s9DisponierteTransporte.setToolTipText("Sonderstatus");
		s9DisponierteTransporte.setWidth(34);
		s9DisponierteTransporte.setText("S9");

		final TableColumn fzgDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		fzgDisponierteTransporte.setToolTipText("Fahrzeugkennzeichnung");
		fzgDisponierteTransporte.setWidth(52);
		fzgDisponierteTransporte.setText("Fzg");

		final TableColumn taDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		taDisponierteTransporte.setToolTipText("Transportart");
		taDisponierteTransporte.setWidth(53);
		taDisponierteTransporte.setText("T");
		
		final TableColumn erkrankungVerletzungDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		erkrankungVerletzungDisponierteTransporte.setWidth(146);
		erkrankungVerletzungDisponierteTransporte.setText("Erkrankung/Verletzung");
		
		
		
		/** make columns sort able*/
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
				if (currentColumn == prioritaetDisponierteTransporte) 
					sortIdentifier = TransportSorter.PRIORITY_SORTER;
				if (currentColumn == transportNummerDisponierteTransporte) 
					sortIdentifier = TransportSorter.TNR_SORTER;
				if (currentColumn == fzgDisponierteTransporte) 
					sortIdentifier = TransportSorter.VEHICLE_SORTER;
				if (currentColumn == terminDisponierteTransporte)
					sortIdentifier = TransportSorter.TERM_SORTER;
				if (currentColumn == transportVonDisponierteTransporte)
					sortIdentifier = TransportSorter.TRANSPORT_FROM_SORTER;
				if(currentColumn == patientDisponierteTransporte)
					sortIdentifier = TransportSorter.PATIENT_SORTER;
				if(currentColumn == transportNachDisponierteTransporte)
					sortIdentifier = TransportSorter.TRANSPORT_TO_SORTER;
				if(currentColumn == taDisponierteTransporte)
					sortIdentifier = TransportSorter.TA_SORTER;
				if(currentColumn == aeDisponierteTransporte)
					sortIdentifier = TransportSorter.AE_SORTER;
				if(currentColumn == s1DisponierteTransporte)
					sortIdentifier = TransportSorter.S1_SORTER;
				if(currentColumn == s2DisponierteTransporte)
					sortIdentifier = TransportSorter.S2_SORTER;
				if(currentColumn == s3DisponierteTransporte)
					sortIdentifier = TransportSorter.S3_SORTER;
				if(currentColumn == s4DisponierteTransporte)
					sortIdentifier = TransportSorter.S4_SORTER;
				if(currentColumn == s7DisponierteTransporte)
					sortIdentifier = TransportSorter.S7_SORTER;
				if(currentColumn == s8DisponierteTransporte)
					sortIdentifier = TransportSorter.S8_SORTER;
				if(currentColumn == s9DisponierteTransporte)
					sortIdentifier = TransportSorter.S9_SORTER;
				if(currentColumn == erkrankungVerletzungDisponierteTransporte)
					sortIdentifier = TransportSorter.KIND_OF_ILLNESS_SORTER;
				
				//apply the filter
				viewer.getTable().setSortDirection(dir);
				viewer.setSorter(new TransportSorter(sortIdentifier,dir));
			}
		};
		
		//attach the listener
		prioritaetDisponierteTransporte.addListener(SWT.Selection, sortListener);
		transportNummerDisponierteTransporte.addListener(SWT.Selection, sortListener);
		fzgDisponierteTransporte.addListener(SWT.Selection, sortListener);
		terminDisponierteTransporte.addListener(SWT.Selection, sortListener);
		transportVonDisponierteTransporte.addListener(SWT.Selection, sortListener);
		patientDisponierteTransporte.addListener(SWT.Selection, sortListener);
		transportNachDisponierteTransporte.addListener(SWT.Selection, sortListener);
		taDisponierteTransporte.addListener(SWT.Selection, sortListener);
		aeDisponierteTransporte.addListener(SWT.Selection, sortListener);
		s1DisponierteTransporte.addListener(SWT.Selection, sortListener);
		s2DisponierteTransporte.addListener(SWT.Selection, sortListener);
		s3DisponierteTransporte.addListener(SWT.Selection, sortListener);
		s4DisponierteTransporte.addListener(SWT.Selection, sortListener);
		s7DisponierteTransporte.addListener(SWT.Selection, sortListener);
		s8DisponierteTransporte.addListener(SWT.Selection, sortListener);
		s9DisponierteTransporte.addListener(SWT.Selection, sortListener);
		
		//create the actions
		makeActions();
		hookContextMenu();

		
		
		
	

		
		
	}
	
	
	/**
	 * Creates the needed actions
	 */
	private void makeActions()
	{
		setTransportStatusS1Action = new SetTransportStatusAction(this.viewer,TRANSPORT_STATUS_ON_THE_WAY, "S1 Transportbeginn");
		setTransportStatusS2Action = new SetTransportStatusAction(this.viewer,TRANSPORT_STATUS_AT_PATIENT, "S2 Bei Patient");
		setTransportStatusS3Action = new SetTransportStatusAction(this.viewer,TRANSPORT_STATUS_START_WITH_PATIENT, "S3 Abfahrt mit Patient");
		setTransportStatusS4Action = new SetTransportStatusAction(this.viewer,TRANSPORT_STATUS_AT_DESTINATION, "S4 Ankunft am Ziel");
		setTransportStatusS5Action = new SetTransportStatusAction(this.viewer,TRANSPORT_STATUS_DESTINATION_FREE, "S5 Ziel frei");
		setTransportStatusS6Action = new SetTransportStatusAction(this.viewer,TRANSPORT_STATUS_CAR_IN_STATION, "S6 Eingerückt");
		setTransportStatusS7Action = new SetTransportStatusAction(this.viewer,TRANSPORT_STATUS_OUT_OF_OPERATION_AREA, "S7 Verlässt Einsatzgebiet");
		setTransportStatusS8Action = new SetTransportStatusAction(this.viewer,TRANSPORT_STATUS_BACK_IN_OPERATION_AREA, "S8 Wieder im Einsatzgebiet");
		setTransportStatusS9Action = new SetTransportStatusAction(this.viewer,TRANSPORT_STATUS_OTHER, "S9 Sonderstatus");
		editTransportStatusAction = new EditTransportStatusAction(this.viewer);
		
		editTransportAction = new EditTransportAction(this.viewer);
	}
	
	/**
	 * Creates the context menu 
	 */
	private void hookContextMenu() 
	{
		MenuManager menuManager = new MenuManager("#PopupMenu");
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() 
		{
			public void menuAboutToShow(IMenuManager manager) 
			{
				fillContextMenu(manager);
			}
		});
		Menu menu = menuManager.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuManager, viewer);
		
		
//		SubMenuManager subMenuManager = new SubMenuManager(menuManager);
//		subMenuManager.setRemoveAllWhenShown(true);
//		subMenuManager.addMenuListener(new IMenuListener()
//		{
//			public void menuAboutToShow(IMenuManager manager)
//			{
////				fill
//			}
//		});
		
	}
	
	
	/**
	 * Fills the context menu with the actions
	 */
	private void fillContextMenu(IMenuManager manager)
	{
		//get the selected object
		final Object firstSelectedObject = ((IStructuredSelection) viewer.getSelection()).getFirstElement();
			
		//cast to a transport
		Transport transport = (Transport)firstSelectedObject;
		
		if(transport == null)
			return;
		
		//add the actions
		manager.add(setTransportStatusS1Action);
		manager.add(setTransportStatusS2Action);
		manager.add(setTransportStatusS3Action);
		manager.add(setTransportStatusS4Action);
		manager.add(setTransportStatusS5Action);
		manager.add(setTransportStatusS6Action);
		manager.add(setTransportStatusS7Action);
		manager.add(setTransportStatusS8Action);
		manager.add(setTransportStatusS9Action);
		manager.add(new Separator());
		manager.add(editTransportStatusAction);
		manager.add(new Separator());
		manager.add(editTransportAction);
		
		
		
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
			System.out.println("UnderwayTransportsView, propertyChange, TRANSPORT_ADD");
			this.viewer.refresh();
		}
		// event on deletion --> also just refresh
		if ("TRANSPORT_REMOVE".equals(evt.getPropertyName())) 
		{ 
			this.viewer.refresh();
		}
		// event on deletion --> also just refresh
		if ("TRANSPORT_UPDATE".equals(evt.getPropertyName())) 
		{ 
			this.viewer.refresh();
		}
		// event on deletion --> also just refresh
		if ("TRANSPORT_CLEARED".equals(evt.getPropertyName())) 
		{ 
			this.viewer.refresh();
		}
	}
}
