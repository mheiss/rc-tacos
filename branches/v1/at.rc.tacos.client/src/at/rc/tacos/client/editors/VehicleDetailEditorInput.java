package at.rc.tacos.client.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import at.rc.tacos.model.VehicleDetail;

public class VehicleDetailEditorInput implements IEditorInput
{
    //properties
    private VehicleDetail vehicle;
    private boolean isNew;
    
    /**
     * Default class constructor.
     * @param vehicle the vehicle to manage
     * @param isNew true if the managed vehicle is created new
     */
    public VehicleDetailEditorInput(VehicleDetail vehicle,boolean isNew)
    {
        this.vehicle = vehicle;
        this.isNew = isNew;
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
    	if(isNew)
    		return "Neues Fahrzeug";
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
            return vehicle.equals(otherEditor.getVehicle());
        }
        return false;
    }
    
    @Override
    public int hashCode()
    {
        return vehicle.hashCode();
    }
    
    /**
     * Returns the vehicle managed by the editor
     * @return the managed vehicle
     */
    public VehicleDetail getVehicle()
    {
    	return vehicle;
    }
    
    /**
     * Returns whether or not the staff member is new.
     * @return true if the staff member is created new
     */
    public boolean isNew()
    {
    	return isNew;
    }
}
