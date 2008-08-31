package at.rc.tacos.client.view;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import at.rc.tacos.client.controller.VehicleEditAction;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.VehicleContentProvider;
import at.rc.tacos.client.providers.VehicleLabelProvider;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.VehicleDetail;

public class VehiclesSelectForm extends TitleAreaDialog
{
    //properties
    private TableViewer viewer;
    
    //the selected vehicle
    private VehicleDetail vehicleDetail;
    
    /**
     * Default class constructor
     * @param parentShell the parent shell
     */
    public VehiclesSelectForm(Shell parentShell)
    {
        super(parentShell);
        vehicleDetail = null;
    }
    
    /**
     * Creates the dialog's contents
     * @param parent the parent composite
     * @return Control
     */
    @Override
	protected Control createContents(Composite parent) 
    {
        Control contents = super.createContents(parent);
        setTitle("Fahrzeugverwaltung");
        setMessage("Bitte wählen sie ein Fahrzeug aus", IMessageProvider.INFORMATION);
        setTitleImage(ImageFactory.getInstance().getRegisteredImage("application.logo"));
        return contents;
    }
    /**
     * Create contents of the window
     */
    @Override
    protected Control createDialogArea(Composite parent)
    {
        //setup the composite
        Composite composite = (Composite) super.createDialogArea(parent);        
        //create the table
        createTableSection(composite);
        return composite;
    }
    
    /**
     * Creates the table to show the vehicles
     * @param parent the parent composite
     */
    private void createTableSection(Composite parent)
    {               
        //create the table, set the providers and the input
        viewer = new TableViewer(parent, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        viewer.setContentProvider(new VehicleContentProvider());
        viewer.setLabelProvider(new VehicleLabelProvider());
        viewer.setInput(ModelFactory.getInstance().getVehicleManager());
        viewer.getTable().setLayout(new GridLayout());
        viewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
    }
    
    /**
     * The user pressed the cancel button
     */
    @Override
    protected void cancelPressed()
    {
        MessageBox dialog = new MessageBox(getShell(), SWT.YES | SWT.NO | SWT.ICON_QUESTION);
        dialog.setText("Abbrechen");
        dialog.setMessage("Wollen Sie wirklich abbrechen?");
        //check the result
        if (dialog.open() != SWT.NO)
            getShell().close();
    }

    /**
     * The user pressed the ok button
     */
    @Override
    protected void okPressed()
    {
        //check the required fileds
        if(checkRequiredFields())
        { 
            //Open the vehicle form
            VehicleEditAction action = new VehicleEditAction(vehicleDetail);
            action.run();
            getShell().close();
            return;
        }
        //indicate a error
        getShell().getDisplay().beep();
    }
    
    /**
     * Helper method to determine wheter all fields are valid
     * @return true if all fields are valid, otherwise false
     */
    private boolean checkRequiredFields()
    {
        IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
        if (selection.getFirstElement() instanceof VehicleDetail)
        {
            vehicleDetail = (VehicleDetail)selection.getFirstElement();
            return true;
        }
        else
        {
            setErrorMessage("Sie müssen ein Fahrzeug auswählen, welches Sie verwalten wollen");
            return false;
        }
    }
}
