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
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.DispositionViewOffContentProvider;
import at.rc.tacos.client.providers.DispositionViewOffLabelProvider;
import at.rc.tacos.client.providers.TransportSorter;
import at.rc.tacos.client.util.CustomColors;

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
		viewerOffTrans.setContentProvider(new DispositionViewOffContentProvider());
		viewerOffTrans.setLabelProvider(new DispositionViewOffLabelProvider());
		viewerOffTrans.setInput(ModelFactory.getInstance().getTransportManager());
		viewerOffTrans.getTable().setLinesVisible(true);
		
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
		tableOff.setRedraw(true);
		tableOff.setLinesVisible(true);
		tableOff.setHeaderVisible(true);
	
		//create the tab items for the personal overview
		final TableColumn newColumnTableColumnIdOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		newColumnTableColumnIdOffeneTransporte.setWidth(29);
		newColumnTableColumnIdOffeneTransporte.setText("Id");
	
		final TableColumn newColumnTableColumnPrioritaetOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		newColumnTableColumnPrioritaetOffeneTransporte.setToolTipText("A (NEF), B (BD1), C (Transport), D (Rücktransport), E (Heimtransport), F (Sonstiges), E (NEF extern)");
		newColumnTableColumnPrioritaetOffeneTransporte.setWidth(31);
		newColumnTableColumnPrioritaetOffeneTransporte.setText("Pr");
	
		final TableColumn newColumnTableColumnOSOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		newColumnTableColumnOSOffeneTransporte.setToolTipText("Zuständige Ortsstelle");
		newColumnTableColumnOSOffeneTransporte.setWidth(49);
		newColumnTableColumnOSOffeneTransporte.setText("OS");
	
		final TableColumn newColumnTableColumnAbfOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		newColumnTableColumnAbfOffeneTransporte.setToolTipText("Abfahrt auf der Dienststelle");
		newColumnTableColumnAbfOffeneTransporte.setWidth(54);
		newColumnTableColumnAbfOffeneTransporte.setText("Abf");
	
		final TableColumn newColumnTableColumnAnkOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		newColumnTableColumnAnkOffeneTransporte.setToolTipText("Ankunft beim Patienten");
		newColumnTableColumnAnkOffeneTransporte.setWidth(53);
		newColumnTableColumnAnkOffeneTransporte.setText("Ank.");
	
		final TableColumn newColumnTableColumnTerminOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		newColumnTableColumnTerminOffeneTransporte.setToolTipText("Terminzeit am Zielort");
		newColumnTableColumnTerminOffeneTransporte.setWidth(57);
		newColumnTableColumnTerminOffeneTransporte.setText("Termin");
	
		final TableColumn newColumnTableColumnTransportVonOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		newColumnTableColumnTransportVonOffeneTransporte.setWidth(102);
		newColumnTableColumnTransportVonOffeneTransporte.setText("Transport von");
	
		final TableColumn newColumnTableColumnPatientOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		newColumnTableColumnPatientOffeneTransporte.setWidth(100);
		newColumnTableColumnPatientOffeneTransporte.setText("Patient");
	
		final TableColumn newColumnTableColumnTransportNachOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		newColumnTableColumnTransportNachOffeneTransporte.setWidth(100);
		newColumnTableColumnTransportNachOffeneTransporte.setText("Transport nach");
	
		final TableColumn newColumnTableColumnAufgOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		newColumnTableColumnAufgOffeneTransporte.setToolTipText("Zeit zu der der Transport aufgenommen wurde");
		newColumnTableColumnAufgOffeneTransporte.setWidth(100);
		newColumnTableColumnAufgOffeneTransporte.setText("Aufg");
	
		final TableColumn newColumnTableColumnTOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		newColumnTableColumnTOffeneTransporte.setToolTipText("Transportart");
		newColumnTableColumnTOffeneTransporte.setWidth(100);
		newColumnTableColumnTOffeneTransporte.setText("T");
	
		final TableColumn newColumnTableColumnErkrankungVerletzungOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		newColumnTableColumnErkrankungVerletzungOffeneTransporte.setWidth(74);
		newColumnTableColumnErkrankungVerletzungOffeneTransporte.setText("Erkrankung/Verletzung");
	
		final TableColumn newColumnTableColumnAnmerkungOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		newColumnTableColumnAnmerkungOffeneTransporte.setWidth(71);
		newColumnTableColumnAnmerkungOffeneTransporte.setText("Anmerkung");
	
	
		/** make the columns sort able*/
	
		//TODO --> for the personal view
		//group filter
		
		
		//
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