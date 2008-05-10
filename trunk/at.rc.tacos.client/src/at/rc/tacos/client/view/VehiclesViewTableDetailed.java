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
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.client.controller.VehicleTableAtStationAction;
import at.rc.tacos.client.controller.VehicleTableDetachAllStaffMembersAction;
import at.rc.tacos.client.controller.VehicleTableEditAction;
import at.rc.tacos.client.controller.VehicleTableSetReadyAction;
import at.rc.tacos.client.controller.VehicleTableSetRepairStatusAction;
import at.rc.tacos.client.modelManager.ModelFactory;

import at.rc.tacos.client.providers.VehicleContentProvider;
import at.rc.tacos.client.providers.VehicleViewTableDetailLabelProvider;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.client.view.sorterAndTooltip.VehicleViewTableSorter;
import at.rc.tacos.model.VehicleDetail;

public class VehiclesViewTableDetailed extends ViewPart implements PropertyChangeListener
{
    public static final String ID = "at.rc.tacos.client.view.vehiclestabledetailed_view";

    //the toolkit to use
    private FormToolkit toolkit;
    private ScrolledForm form;
    private TableViewer viewer;

    //the actions for the context menu
	private VehicleTableEditAction editAction;
	private VehicleTableDetachAllStaffMembersAction detachAction;
	private VehicleTableSetReadyAction readyStatus;
	private VehicleTableSetRepairStatusAction repairStatus;
	private VehicleTableAtStationAction vehicleAtStationAction;

    /**
     * Constructs a new vehicle view.
     */
    public VehiclesViewTableDetailed()
    {
        // add listener to model to keep on track. 
        ModelFactory.getInstance().getRosterEntryManager().addPropertyChangeListener(this);
        ModelFactory.getInstance().getVehicleManager().addPropertyChangeListener(this);
        //listen to changes of jobs, serviceTypes and staff member updates
        ModelFactory.getInstance().getStaffManager().addPropertyChangeListener(this);
        ModelFactory.getInstance().getServiceManager().addPropertyChangeListener(this);
        ModelFactory.getInstance().getJobList().addPropertyChangeListener(this);
    }

    /**
     * Cleanup the view
     */
    @Override
    public void dispose() 
    {
        ModelFactory.getInstance().getRosterEntryManager().removePropertyChangeListener(this);
        ModelFactory.getInstance().getLocationManager().removePropertyChangeListener(this);
        ModelFactory.getInstance().getVehicleManager().removePropertyChangeListener(this);
        //remove again
        ModelFactory.getInstance().getStaffManager().removePropertyChangeListener(this);
        ModelFactory.getInstance().getServiceManager().removePropertyChangeListener(this);
        ModelFactory.getInstance().getJobList().removePropertyChangeListener(this);
    }

    /**
     * Callback method to create the control and initalize them.
     * @param parent the parent composite to add
     */
    @Override
	public void createPartControl(final Composite parent) 
    {
        // Create the scrolled parent component
        toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
        form = toolkit.createScrolledForm(parent);
//        form.setText("Fzg.");
        toolkit.decorateFormHeading(form.getForm());
        form.getBody().setLayout(new FillLayout());

        final Composite composite = form.getBody();
        viewer = new TableViewer(composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL|SWT.FULL_SELECTION);
        viewer.setContentProvider(new VehicleContentProvider());
        viewer.setLabelProvider(new VehicleViewTableDetailLabelProvider());
        viewer.setInput(ModelFactory.getInstance().getVehicleManager().toArray());
        viewer.getTable().setLinesVisible(true);
        
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
        //sort the table by default
        viewer.setSorter(new VehicleViewTableSorter(VehicleViewTableSorter.VEHICLE_SORTER,SWT.UP));

        //create the table for the vehicles 
        final Table table = viewer.getTable();
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        
        final TableColumn columnReady = new TableColumn(table, SWT.NONE);
        columnReady.setToolTipText("Einsatzbereit");
        columnReady.setWidth(20);
        columnReady.setText("EB");

        final TableColumn columnVehicleName = new TableColumn(table, SWT.NONE);
        columnVehicleName.setToolTipText("Fahrzeugname");
        columnVehicleName.setWidth(60);
//        columnVehicleName.setText("Fzg.");

        final TableColumn columnVehicleStatus = new TableColumn(table, SWT.NONE);
        columnVehicleStatus.setToolTipText("Verf�gbarkeit des Fahrzeuges");
        columnVehicleStatus.setWidth(20);
//        columnVehicleStatus.setText("Status");
        
        final TableColumn columnDriver = new TableColumn(table, SWT.NONE);
        columnDriver.setToolTipText("Fahrer");
        columnDriver.setWidth(100);
        columnDriver.setText("Fahrer");
        
        final TableColumn columnMedicI = new TableColumn(table, SWT.NONE);
        columnMedicI.setToolTipText("Sanit�ter I");
        columnMedicI.setWidth(100);
        columnMedicI.setText("Sanit�ter I");
        
        final TableColumn columnMedicII = new TableColumn(table, SWT.NONE);
        columnMedicII.setToolTipText("Sanit�ter");
        columnMedicII.setWidth(100);
        columnMedicII.setText("Sanit�ter II");
        
        final TableColumn columnPhone = new TableColumn(table, SWT.NONE);
        columnPhone.setToolTipText("Anderes als prim�res Handy");
        columnPhone.setWidth(15);
        
        final TableColumn columnStation = new TableColumn(table, SWT.NONE);
        columnStation.setToolTipText("Andere als prim�res Ortsstelle");
        columnStation.setWidth(15);
        
        final TableColumn columnOutOfOrder = new TableColumn(table, SWT.NONE);
        columnOutOfOrder.setToolTipText("Fahrzeug ist au�er Dienst");
        columnOutOfOrder.setWidth(15);
        
        final TableColumn columnNotes = new TableColumn(table, SWT.NONE);
        columnNotes.setToolTipText("Notizen zum Fahrzeug vorhanden");
        columnNotes.setWidth(15);
        
        final TableColumn columnLastDestinationFree = new TableColumn(table, SWT.NONE);
        columnLastDestinationFree.setToolTipText("Zeigt den Standort der letzten Meldung \"Ziel frei\" (S6)an");
        columnLastDestinationFree.setWidth(100);
        columnLastDestinationFree.setText("Letzter Status S5"); 

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
                if (currentColumn == columnVehicleStatus) 
                    sortIdentifier = VehicleViewTableSorter.STATUS_SORTER;
                if (currentColumn == columnVehicleName) 
                    sortIdentifier = VehicleViewTableSorter.VEHICLE_SORTER;
               
                //apply the filter
                viewer.getTable().setSortDirection(dir);
                viewer.setSorter(new VehicleViewTableSorter(sortIdentifier,dir));
            }
        };

        //attach the listener
        columnVehicleName.addListener(SWT.Selection, sortListener);
        columnVehicleStatus.addListener(SWT.Selection, sortListener);
        
        //create the actions
        makeActions();
        hookContextMenu();
        
        viewer.refresh();
    }

    /**
     * Creates the needed actions
     */
    private void makeActions()
    {
    	
    	editAction = new VehicleTableEditAction(this.viewer);
		detachAction = new VehicleTableDetachAllStaffMembersAction(this.viewer);
		readyStatus = new VehicleTableSetReadyAction(this.viewer);
		repairStatus = new VehicleTableSetRepairStatusAction(this.viewer);
		vehicleAtStationAction = new VehicleTableAtStationAction(this.viewer);
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

        //cast to a vehicle detail
        VehicleDetail vehicle = (VehicleDetail)firstSelectedObject;

        if(vehicle == null)
            return;

        //add the actions
        manager.add(editAction);
		manager.add(detachAction);
		manager.add(new Separator());
		manager.add(vehicleAtStationAction);
		manager.add(new Separator());
		manager.add(readyStatus);
		manager.add(repairStatus);
		
		//enable or disable the actions
		if(vehicle.isReadyForAction())
			readyStatus.setEnabled(false);
		else
			readyStatus.setEnabled(true);
		if(vehicle.isOutOfOrder())
		{
			readyStatus.setEnabled(false);
			repairStatus.setEnabled(false);
		}
		else 
			repairStatus.setEnabled(true);
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
	public void setFocus()  { }

    public void propertyChange(PropertyChangeEvent evt) 
    {		
        // the viewer represents simple model. refresh should be enough.
        if ("VEHICLE_ADD".equals(evt.getPropertyName())
        		|| "VEHICLE_ADD_ALL".equalsIgnoreCase(evt.getPropertyName())
                || "VEHICLE_REMOVE".equals(evt.getPropertyName())
                || "VEHICLE_UPDATE".equals(evt.getPropertyName())
                || "VEHICLE_CLEARED".equals(evt.getPropertyName())) 
        {
            viewer.refresh();
        }
    }
}
