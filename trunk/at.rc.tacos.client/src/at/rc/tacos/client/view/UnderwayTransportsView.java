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
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.client.controller.EditTransportAction;
import at.rc.tacos.client.controller.SetTransportStatusAction;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.UnderwayTransportsViewContentProvider;
import at.rc.tacos.client.providers.UnderwayTransportsViewLabelProvider;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.client.view.sorterAndTooltip.UnderwayTransportsTooltip;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.model.RosterEntry;

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
		

		final SashForm sashForm = new SashForm(composite, SWT.VERTICAL);
//		dispositionTabItem.setControl(sashForm);
		sashForm.setLayout(new FillLayout());

		final Group disponierteTransporteGroup = new Group(sashForm, SWT.NONE);
		disponierteTransporteGroup.setLayout(new FillLayout());
		disponierteTransporteGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		disponierteTransporteGroup.setText("Disponierte Transporte");

		/** tabFolder Selection Listener not needed? */
		viewer = new TableViewer(disponierteTransporteGroup, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL|SWT.FULL_SELECTION);
		viewer.setContentProvider(new UnderwayTransportsViewContentProvider());
		viewer.setLabelProvider(new UnderwayTransportsViewLabelProvider());
		viewer.setInput(ModelFactory.getInstance().getTransportManager());
		viewer.getTable().setLinesVisible(true);
		
		/** Tooltip */
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
		/** Sorter */
		
		
		
		final Table tableDisp = viewer.getTable();
		tableDisp.setLinesVisible(true);
		tableDisp.setHeaderVisible(true);
		
	
		final TableColumn lockColumn = new TableColumn(tableDisp, SWT.NONE);
		lockColumn.setToolTipText("Eintrag wird gerade bearbeitet");
		lockColumn.setWidth(30);
		lockColumn.setText("L");
		
		
//		table.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));
		
		//create the tab items for the disposition view
		final TableColumn columnPrioritaetDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnPrioritaetDisponierteTransporte.setToolTipText("A (NEF), B (BD1), C (Transport), D (Rücktransport), E (Heimtransport), F (Sonstiges), E (NEF extern)");
		columnPrioritaetDisponierteTransporte.setWidth(36);
		columnPrioritaetDisponierteTransporte.setText("Pr");

		final TableColumn columnTransportNummerDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnTransportNummerDisponierteTransporte.setToolTipText("Ortsstellenabhängige Transportnummer");
		columnTransportNummerDisponierteTransporte.setMoveable(true);
		columnTransportNummerDisponierteTransporte.setWidth(54);
		columnTransportNummerDisponierteTransporte.setText("TNr");

		final TableColumn columnTerminDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnTerminDisponierteTransporte.setToolTipText("Termin am Zielort");
		columnTerminDisponierteTransporte.setMoveable(true);
		columnTerminDisponierteTransporte.setWidth(48);
		columnTerminDisponierteTransporte.setText("Termin");

		final TableColumn columnTransportVonDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnTransportVonDisponierteTransporte.setMoveable(true);
		columnTransportVonDisponierteTransporte.setWidth(118);
		columnTransportVonDisponierteTransporte.setText("Transport von");

		final TableColumn columnPatientDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnPatientDisponierteTransporte.setMoveable(true);
		columnPatientDisponierteTransporte.setWidth(100);
		columnPatientDisponierteTransporte.setText("Patient");

		final TableColumn columnTransportNachDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnTransportNachDisponierteTransporte.setWidth(100);
		columnTransportNachDisponierteTransporte.setText("Transport nach");

		final TableColumn columnAEDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnAEDisponierteTransporte.setToolTipText("Auftrag erteilt");
		columnAEDisponierteTransporte.setWidth(39);
		columnAEDisponierteTransporte.setText("AE");

		final TableColumn columnS1DisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnS1DisponierteTransporte.setToolTipText("Transportbeginn");
		columnS1DisponierteTransporte.setWidth(36);
		columnS1DisponierteTransporte.setText("S1");

		final TableColumn columnS2DisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnS2DisponierteTransporte.setToolTipText("Ankunft bei Patient");
		columnS2DisponierteTransporte.setWidth(38);
		columnS2DisponierteTransporte.setText("S2");

		final TableColumn columnS3DisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnS3DisponierteTransporte.setToolTipText("Abfahrt mit Patient");
		columnS3DisponierteTransporte.setWidth(40);
		columnS3DisponierteTransporte.setText("S3");

		final TableColumn columnS4DisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnS4DisponierteTransporte.setToolTipText("Ankunft Ziel");
		columnS4DisponierteTransporte.setWidth(36);
		columnS4DisponierteTransporte.setText("S4");

		final TableColumn columnS7DisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnS7DisponierteTransporte.setToolTipText("Einsatzgebiet verlassen");
		columnS7DisponierteTransporte.setWidth(40);
		columnS7DisponierteTransporte.setText("S7");

		final TableColumn columnS8DisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnS8DisponierteTransporte.setToolTipText("Zurück im Einsatzgebiet");
		columnS8DisponierteTransporte.setWidth(34);
		columnS8DisponierteTransporte.setText("S8");

		final TableColumn columnS9DisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnS9DisponierteTransporte.setToolTipText("Sonderstatus");
		columnS9DisponierteTransporte.setWidth(34);
		columnS9DisponierteTransporte.setText("S9");

		final TableColumn columnFzgDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnFzgDisponierteTransporte.setToolTipText("Fahrzeugkennzeichnung");
		columnFzgDisponierteTransporte.setWidth(52);
		columnFzgDisponierteTransporte.setText("Fzg");

		final TableColumn columnTDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnTDisponierteTransporte.setToolTipText("Transportart");
		columnTDisponierteTransporte.setWidth(53);
		columnTDisponierteTransporte.setText("T");
		

		final TableColumn columnErkrankungVerletzungDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnErkrankungVerletzungDisponierteTransporte.setWidth(146);
		columnErkrankungVerletzungDisponierteTransporte.setText("Erkrankung/Verletzung");
		
		
		
		
		//create the actions
		makeActions();
		hookContextMenu();

		
		
		
		
		/** make columns sort able*/

		
		
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
		
		editTransportAction = new EditTransportAction(this.viewer);
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
			
		//cast to a RosterEntry
		RosterEntry entry = (RosterEntry)firstSelectedObject;
		
		if(entry == null)
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
