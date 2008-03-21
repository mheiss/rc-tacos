package at.rc.tacos.client.providers;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.VehicleDetail;

public class StaffMemberVehicleContentProvider implements IStructuredContentProvider
{
	private VehicleDetail vehicle;

    /**
     * Default class constructor for a vontent provider
     */
    public StaffMemberVehicleContentProvider(VehicleDetail detail)
    {
    	this.vehicle = detail;
    }

    @Override
    public Object[] getElements(Object arg0)
    {
    	List<StaffMember> list = ModelFactory.getInstance().getStaffManager().getFreeStaffMembers(vehicle);
        return list.toArray();
    }

    @Override
    public void dispose() { }

    @Override
    public void inputChanged(Viewer arg0, Object arg1, Object arg2) { }
}
