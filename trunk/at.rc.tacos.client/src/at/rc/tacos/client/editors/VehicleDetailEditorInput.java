package at.rc.tacos.client.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import at.rc.tacos.model.VehicleDetail;

public class VehicleDetailEditorInput implements IEditorInput
{
    //properties
    private VehicleDetail vehicle;
    
    /**
     * Default class constructor
     */
    public VehicleDetailEditorInput(VehicleDetail vehicle)
    {
        this.vehicle = vehicle;
    }
    
    @Override
    public boolean exists()
    {
        return false;
    }

    @Override
    public ImageDescriptor getImageDescriptor()
    {
        return null;
    }

    @Override
    public String getName()
    {
        return vehicle.getVehicleType() + " " + vehicle.getVehicleName();
    }

    @Override
    public IPersistableElement getPersistable()
    {
        return null;
    }

    @Override
    public String getToolTipText()
    {
        return "Fahrzeug: " + vehicle.getVehicleType() + " " + vehicle.getVehicleName();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object getAdapter(Class arg0)
    {
        return null;
    }
    
    @Override
    public boolean equals(Object other)
    {
        if (other instanceof VehicleDetailEditorInput)
        {
            VehicleDetailEditorInput otherEditor = (VehicleDetailEditorInput)other;
            if((vehicle.getVehicleType() + " " + vehicle.getVehicleName()).equals(otherEditor.getName()))
                return true;
        }
        return false;
    }
    
    @Override
    public int hashCode()
    {
        return vehicle.hashCode();
    }

}
