package at.rc.tacos.client.view.admin;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.model.VehicleDetail;

public class VehicleDetailAdminView extends ViewPart implements ISelectionListener 
{
    public static final String ID = "at.rc.tacos.client.view.admin.vehicleDetailAdminView"; 
    //properties
    private Label headerLabel;
    private FormToolkit toolkit;
    private ScrolledForm form;
    private Text vehicleName;
    private Text vehicleType;
    private Text basicStation;
    private Label vehicleNameLabel;
    private Label vehicleTypeLabel;
    private Label basicStationLabel;  
    
    /**
     * Default class constructor
     */
    public VehicleDetailAdminView()
    {
        //Attach a selection change listener to the view
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().addSelectionListener(this);
    }
    
    /**
     * Clean up
     */
    @Override
    public void dispose()
    {
        //remove the listener
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().removeSelectionListener(this); 
    }

    /**
     * This is a callback that will allow us to create the viewer and initialize it.
     */
    @Override
    public void createPartControl(final Composite parent) 
    {
        //Create the form
        toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
        form = toolkit.createScrolledForm(parent);
        form.setText("Fahrzeug");
        toolkit.decorateFormHeading(form.getForm());
        form.getBody().setLayout(new GridLayout());
        //the body for the other components to add
        final Composite composite = form.getBody();
        
        //the subheader
        final Composite headerComp = new Composite(composite, SWT.NONE);
        headerComp.setLayout(new GridLayout(1,false));
        headerComp.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true ,false));
        
        headerLabel = new Label(headerComp, SWT.NONE);
        headerLabel.setForeground(CustomColors.RED_COLOR);
        headerLabel.setFont(CustomColors.SUBHEADER_FONT);
        headerLabel.setText("Details");
        headerLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true ,true));
        
        //the labels
        final Composite client = toolkit.createComposite(composite, SWT.WRAP);
        //layout
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        client.setLayout(gridLayout);
        
        //vehicle name
        vehicleNameLabel = toolkit.createLabel(client, "Fahrzeugname");
        vehicleName = toolkit.createText(client, "");
        //vehicle type
        vehicleTypeLabel = toolkit.createLabel(client, "Fahrzeugtyp");
        vehicleType = toolkit.createText(client, "");
        //basic station
        basicStationLabel = this.toolkit.createLabel(client, "Primäre Ortsstelle");
        basicStation = this.toolkit.createText(client, "");
        
        //layout for the labels
        GridData data = new GridData();
        data.widthHint = 80;
        vehicleNameLabel.setLayoutData(data);
        data = new GridData();
        data.widthHint = 80;
        vehicleTypeLabel.setLayoutData(data);
        data = new GridData();
        data.widthHint = 80;
        basicStationLabel.setLayoutData(data);
        //layout for the text fields
        GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
        vehicleName.setLayoutData(data2);
        data2 = new GridData(GridData.FILL_HORIZONTAL);
        vehicleType.setLayoutData(data2);
        data2 = new GridData(GridData.FILL_HORIZONTAL);
        basicStation.setLayoutData(data2);
       
        //redraw the form
        form.reflow(true);
    }

    /**
     * Passes the focus to the view
     */
    @Override
    public void setFocus() {  }

    /**
     * The selection has changed.
     * @param part the workbenchpart where the selection event was triggered
     * @param selection the selected element
     */
    public void selectionChanged(final IWorkbenchPart part, final ISelection selection) 
    {
        if (!selection.isEmpty()) 
        {
            IStructuredSelection structuredSelection = (IStructuredSelection)selection;
            VehicleDetail detail = (VehicleDetail)structuredSelection.getFirstElement();
       
            System.out.println(selection);
            System.out.println(structuredSelection);
            System.out.println(detail);
            vehicleName.setText(detail.getVehicleName());
            vehicleType.setText(detail.getVehicleType());
            basicStation.setText(detail.getBasicStation().getLocationName());
        } 
        else 
        {
            vehicleName.setText(""); 
            vehicleType.setText(""); 
            basicStation.setText("");
        }
    }
}
