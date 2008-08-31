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
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
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
import at.rc.tacos.client.controller.CancelTransportAction;
import at.rc.tacos.client.controller.CopyTransportAction;
import at.rc.tacos.client.controller.CopyTransportDetailsIntoClipboardAction;
import at.rc.tacos.client.controller.CreateBackTransportAction;
import at.rc.tacos.client.controller.DetachCarAction;
import at.rc.tacos.client.controller.EditTransportAction;
import at.rc.tacos.client.controller.EditTransportStatusAction;
import at.rc.tacos.client.controller.EmptyTransportAction;
import at.rc.tacos.client.controller.SetAccompanyingPersonAction;
import at.rc.tacos.client.controller.SetAlarmingAction;
import at.rc.tacos.client.controller.SetBD1Action;
import at.rc.tacos.client.controller.SetBD2Action;
import at.rc.tacos.client.controller.SetBackTransportPossibleAction;
import at.rc.tacos.client.controller.SetTransportStatusAction;
import at.rc.tacos.client.modelManager.LockManager;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.TransportStateViewFilter;
import at.rc.tacos.client.providers.TransportViewFilter;
import at.rc.tacos.client.providers.UnderwayTransportsViewContentProvider;
import at.rc.tacos.client.providers.UnderwayTransportsViewLabelProvider;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.client.view.sorterAndTooltip.TransportSorter;
//import at.rc.tacos.client.view.sorterAndTooltip.UnderwayTransportsTooltip;
import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.model.Transport;

/**
 * Main view, provides an overview about the transports
 * @author b.thek
 */
public class UnderwayTransportsView extends ViewPart implements PropertyChangeListener, ITransportStatus, IProgramStatus
{
	public static final String ID = "at.rc.tacos.client.view.disposition_view";
	
	private FormToolkit toolkit;
	private Form formDisp;
	private TableViewer viewer;
	
	//the actions for the context menu
	private SetTransportStatusAction setTransportStatusS1Action;
	private SetTransportStatusAction setTransportStatusS2Action;
	private SetTransportStatusAction setTransportStatusS3Action;
	private SetTransportStatusAction setTransportStatusS4Action;
	private SetTransportStatusAction setTransportStatusS5Action;
	private EditTransportStatusAction editTransportStatusAction;
	private DetachCarAction detachCarAction;
	private EditTransportAction editTransportAction;
	private EmptyTransportAction emptyTransportAction;
	private CancelTransportAction cancelTransportAction;
	private CopyTransportAction copyTransportAction;
	private CopyTransportDetailsIntoClipboardAction copyTransportDetailsIntoClipboardAction;
	private SetAccompanyingPersonAction setAccompanyingPersonAction;
	private SetBD2Action setBD2Action;
	private SetBD1Action setBD1Action;
	private SetBackTransportPossibleAction setBackTransportPossibleAction;
	private CreateBackTransportAction createBackTransportAction;
	private SetAlarmingAction setAlarmingActionNA;
	private SetAlarmingAction setAlarmingActionRTH;
	private SetAlarmingAction setAlarmingActionDF;
	private SetAlarmingAction setAlarmingActionBRKDT;
	private SetAlarmingAction setAlarmingActionFW;
	private SetAlarmingAction setAlarmingActionPO;
	private SetAlarmingAction setAlarmingActionBR;
	private SetAlarmingAction setAlarmingActionKIT;
	
	//the lock manager
	private LockManager lockManager = ModelFactory.getInstance().getLockManager();

	/**
	 * Defaul class constructor
	 */
	public UnderwayTransportsView()
	{
		//add listener to model to keep on track
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
	 * Call back method to create the control and initialize them
	 * Create contents of the window
	 */
	@Override
	public void createPartControl(final Composite parent) 
	{
		//Create the scrolled parent component
		toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
		formDisp = toolkit.createForm(parent);
		formDisp.setText("Disponierte Transporte");
		toolkit.decorateFormHeading(formDisp);
		formDisp.getBody().setLayout(new FillLayout());

		final Composite composite = formDisp.getBody();
		
		SashForm sashForm = new SashForm(composite, SWT.VERTICAL);
		
		viewer = new TableViewer(sashForm, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL|SWT.FULL_SELECTION);
		viewer.setContentProvider(new UnderwayTransportsViewContentProvider());
		viewer.setLabelProvider(new UnderwayTransportsViewLabelProvider());
		viewer.setInput(ModelFactory.getInstance().getTransportManager().toArray());
		viewer.getTable().setLinesVisible(true);
		
		//integrate the outstandings transports view
		OutstandingTransportsView outstandingView = new OutstandingTransportsView();
		outstandingView.createPartControl(sashForm);
		
		viewer.refresh();
		
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
		//set a default sorter
		viewer.setSorter(new TransportSorter(TransportSorter.PRIORITY_SORTER,SWT.UP));
		
		final Table tableDisp = viewer.getTable();
		tableDisp.setLinesVisible(true);
		tableDisp.setHeaderVisible(true);
			
		final TableColumn lockColumn = new TableColumn(tableDisp, SWT.NONE);
		lockColumn.setToolTipText("Eintrag wird gerade bearbeitet");
		lockColumn.setWidth(24);
		lockColumn.setText("L");
	
		//create the tab items for the disposition view
		final TableColumn prioritaetDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		prioritaetDisponierteTransporte.setToolTipText("1 (NEF), 2 (BD1), 3 (Transport), 4 (Rücktransport), 5 (Heimtransport), 6 (Sonstiges), 7 (NEF extern)");
		prioritaetDisponierteTransporte.setWidth(26);
		prioritaetDisponierteTransporte.setText("Pr");

		final TableColumn transportNummerDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		transportNummerDisponierteTransporte.setToolTipText("Ortsstellenabhängige Transportnummer");
		transportNummerDisponierteTransporte.setMoveable(true);
		transportNummerDisponierteTransporte.setWidth(70);
		transportNummerDisponierteTransporte.setText("TNr");

		final TableColumn terminDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		terminDisponierteTransporte.setToolTipText("Termin am Zielort");
		terminDisponierteTransporte.setMoveable(true);
		terminDisponierteTransporte.setWidth(43);
		terminDisponierteTransporte.setText("Termin");

		final TableColumn transportVonDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		transportVonDisponierteTransporte.setMoveable(true);
		transportVonDisponierteTransporte.setWidth(250);
		transportVonDisponierteTransporte.setText("Transport von");

		final TableColumn patientDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		patientDisponierteTransporte.setMoveable(true);
		patientDisponierteTransporte.setWidth(200);
		patientDisponierteTransporte.setText("Patient");

		final TableColumn transportNachDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		transportNachDisponierteTransporte.setWidth(250);
		transportNachDisponierteTransporte.setText("Transport nach");

		final TableColumn aeDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		aeDisponierteTransporte.setToolTipText("Auftrag erteilt");
		aeDisponierteTransporte.setWidth(43);
		aeDisponierteTransporte.setText("AE");

		final TableColumn s1DisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		s1DisponierteTransporte.setToolTipText("Transportbeginn");
		s1DisponierteTransporte.setWidth(43);
		s1DisponierteTransporte.setText("S1");

		final TableColumn s2DisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		s2DisponierteTransporte.setToolTipText("Ankunft bei Patient");
		s2DisponierteTransporte.setWidth(43);
		s2DisponierteTransporte.setText("S2");

		final TableColumn s3DisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		s3DisponierteTransporte.setToolTipText("Abfahrt mit Patient");
		s3DisponierteTransporte.setWidth(43);
		s3DisponierteTransporte.setText("S3");

		final TableColumn s4DisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		s4DisponierteTransporte.setToolTipText("Ankunft Ziel");
		s4DisponierteTransporte.setWidth(43);
		s4DisponierteTransporte.setText("S4");

		final TableColumn fzgDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		fzgDisponierteTransporte.setToolTipText("Fahrzeugkennzeichnung");
		fzgDisponierteTransporte.setWidth(60);
		fzgDisponierteTransporte.setText("Fzg");

		final TableColumn taDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		taDisponierteTransporte.setToolTipText("Transportart");
		taDisponierteTransporte.setWidth(22);
		taDisponierteTransporte.setText("T");
		
		final TableColumn erkrankungVerletzungDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		erkrankungVerletzungDisponierteTransporte.setWidth(200);
		erkrankungVerletzungDisponierteTransporte.setText("Erkrankung/Verletzung");
		
		final TableColumn anmerkungUnderwayTransporte = new TableColumn(tableDisp, SWT.NONE);
		anmerkungUnderwayTransporte.setWidth(312);
		anmerkungUnderwayTransporte.setText("Anmerkung");
		
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
				if(currentColumn == erkrankungVerletzungDisponierteTransporte)
					sortIdentifier = TransportSorter.KIND_OF_ILLNESS_SORTER;
				if(currentColumn == anmerkungUnderwayTransporte)
					sortIdentifier = TransportSorter.NOTES_SORTER;
				
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
		erkrankungVerletzungDisponierteTransporte.addListener(SWT.Selection, sortListener);
		anmerkungUnderwayTransporte.addListener(SWT.Selection, sortListener);
		
		//create the actions
		makeActions();
		hookContextMenu();
		
		viewer.addFilter(new TransportStateViewFilter(PROGRAM_STATUS_UNDERWAY));
		
		viewer.refresh();
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
		editTransportStatusAction = new EditTransportStatusAction(this.viewer);
		
		setAccompanyingPersonAction = new SetAccompanyingPersonAction(this.viewer);
		setBD1Action = new SetBD1Action(this.viewer);
		setBD2Action = new SetBD2Action(this.viewer);
		setBackTransportPossibleAction = new SetBackTransportPossibleAction(this.viewer);
		createBackTransportAction = new CreateBackTransportAction(this.viewer);
		editTransportAction = new EditTransportAction(this.viewer,"underway");
		detachCarAction = new DetachCarAction(this.viewer);
		emptyTransportAction = new EmptyTransportAction(this.viewer);
		cancelTransportAction = new CancelTransportAction(this.viewer);
		copyTransportAction = new CopyTransportAction(this.viewer);
		copyTransportDetailsIntoClipboardAction = new CopyTransportDetailsIntoClipboardAction(this.viewer);
		setAlarmingActionNA = new SetAlarmingAction(this.viewer,"NA extern");
		setAlarmingActionRTH = new SetAlarmingAction(this.viewer,"RTH");
		setAlarmingActionDF = new SetAlarmingAction(this.viewer,"DF/Inspektion");
		setAlarmingActionBRKDT = new SetAlarmingAction(this.viewer,"BRKDT");
		setAlarmingActionFW = new SetAlarmingAction(this.viewer,"FW");
		setAlarmingActionPO = new SetAlarmingAction(this.viewer,"Polizei");
		setAlarmingActionBR = new SetAlarmingAction(this.viewer,"Bergrettung");
		setAlarmingActionKIT = new SetAlarmingAction(this.viewer,"KIT");
	}
	
	/**
	 * Creates the context menu 
	 */
	private void hookContextMenu() 
	{
		MenuManager menuManager = new MenuManager("#DispositionPopupMenu");
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
	}
	
	/**
	 * Fills the context menu with the actions
	 */
	private void fillContextMenu(IMenuManager manager)
	{
		makeActions();
		//get the selected object
		final Object firstSelectedObject = ((IStructuredSelection) viewer.getSelection()).getFirstElement();
			
		//cast to a transport
		Transport transport = (Transport)firstSelectedObject;
		
		if(transport == null)
			return;
		
		//submenu for the details
		MenuManager menuManagerSub = new MenuManager("Details");
		//submenu for the alarmings
		MenuManager menuManagerAlarming = new MenuManager("Alarmierung setzen");
		menuManagerAlarming.add(setAlarmingActionNA);
		menuManagerAlarming.add(setAlarmingActionRTH);
		menuManagerAlarming.add(setAlarmingActionDF);
		menuManagerAlarming.add(setAlarmingActionBRKDT);
		menuManagerAlarming.add(setAlarmingActionFW);
		menuManagerAlarming.add(setAlarmingActionPO);
		menuManagerAlarming.add(setAlarmingActionBR);
		menuManagerAlarming.add(setAlarmingActionKIT);

		//add the actions
		menuManagerSub.add(setAccompanyingPersonAction);
		menuManagerSub.add(new Separator());
		menuManagerSub.add(setBD1Action);
		menuManagerSub.add(setBD2Action);
		menuManagerSub.add(new Separator());
		menuManagerSub.add(setBackTransportPossibleAction);
		menuManagerSub.add(createBackTransportAction);
		menuManagerSub.add(new Separator());
		menuManagerSub.add(menuManagerAlarming);
		
		//add the actions
		manager.add(setTransportStatusS1Action);
		manager.add(setTransportStatusS2Action);
		manager.add(setTransportStatusS3Action);
		manager.add(setTransportStatusS4Action);
		manager.add(setTransportStatusS5Action);
		manager.add(editTransportStatusAction);
		manager.add(new Separator());
		manager.add(menuManagerSub);
		manager.add(new Separator());
		manager.add(editTransportAction);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(detachCarAction);
		manager.add(emptyTransportAction);
		manager.add(cancelTransportAction);
		manager.add(new Separator());
		manager.add(copyTransportAction);
		manager.add(copyTransportDetailsIntoClipboardAction);
		
		//disable the selection if the transport is locked
		if(lockManager.containsLock(Transport.ID, transport.getTransportId()))
		{
			//transport detail actions
			setAccompanyingPersonAction.setEnabled(false);
			setBD1Action.setEnabled(false);
			setBD2Action.setEnabled(false);
			setBackTransportPossibleAction.setEnabled(false);
			createBackTransportAction.setEnabled(false);
			copyTransportAction.setEnabled(false);
			
			//transport stati
			setTransportStatusS1Action.setEnabled(false);
			setTransportStatusS2Action.setEnabled(false);
			setTransportStatusS3Action.setEnabled(false);
			setTransportStatusS4Action.setEnabled(false);
			setTransportStatusS5Action.setEnabled(false);
			
			//alarmings
			setAlarmingActionNA.setEnabled(false);
			setAlarmingActionRTH.setEnabled(false);
			setAlarmingActionDF.setEnabled(false);
			setAlarmingActionBRKDT.setEnabled(false);
			setAlarmingActionFW.setEnabled(false);
			setAlarmingActionPO.setEnabled(false);
			setAlarmingActionBR.setEnabled(false);
			setAlarmingActionKIT.setEnabled(false);
		}
		else
		{
			//default action = true
			//transport detail actions
			setAccompanyingPersonAction.setEnabled(true);
			setBD1Action.setEnabled(true);
			setBD2Action.setEnabled(true);
			setBackTransportPossibleAction.setEnabled(true);
			createBackTransportAction.setEnabled(true);
			copyTransportAction.setEnabled(true);
			
			//transport stati
			setTransportStatusS1Action.setEnabled(true);
			setTransportStatusS2Action.setEnabled(true);
			setTransportStatusS3Action.setEnabled(true);
			setTransportStatusS4Action.setEnabled(true);
			setTransportStatusS5Action.setEnabled(true);
			
			//alarmings
			setAlarmingActionNA.setEnabled(true);
			setAlarmingActionRTH.setEnabled(true);
			setAlarmingActionDF.setEnabled(true);
			setAlarmingActionBRKDT.setEnabled(true);
			setAlarmingActionFW.setEnabled(true);
			setAlarmingActionPO.setEnabled(true);
			setAlarmingActionBR.setEnabled(true);
			setAlarmingActionKIT.setEnabled(true);
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
			this.viewer.refresh();
		}
		//listen to filter events
		if("TRANSPORT_FILTER_CHANGED".equalsIgnoreCase(evt.getPropertyName()))
		{
			//get the new filter
			TransportViewFilter searchFilter = (TransportViewFilter)evt.getNewValue();
			//remove all filters and apply the new
			for(ViewerFilter filter:viewer.getFilters())
			{
				if(!(filter instanceof TransportViewFilter))
					continue;
				viewer.removeFilter(filter);
					
			}
			if(searchFilter != null)
			{
				viewer.addFilter(searchFilter);
			}	
		}
		
		//listen to lock changes
		if("LOCK_ADD".equalsIgnoreCase(evt.getPropertyName()) || "LOCK_REMOVE".equalsIgnoreCase(evt.getPropertyName()))
		{
			viewer.refresh();
		}
	}
}