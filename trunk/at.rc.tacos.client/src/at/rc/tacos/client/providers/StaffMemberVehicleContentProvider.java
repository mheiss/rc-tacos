package at.rc.tacos.client.providers;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.StaffMember;

public class StaffMemberVehicleContentProvider implements IStructuredContentProvider
{
	private StaffMember staffMember;
	private Location location;
	
	public StaffMemberVehicleContentProvider(StaffMember staffMember, Location location)
	{
		this.staffMember = staffMember;
		this.location = location;
	}
	
	public StaffMemberVehicleContentProvider(Location location)
	{
		this.location = location;
	}

    /**
     * Default class constructor for a vontent provider
     */
    public StaffMemberVehicleContentProvider()
    {
    }

    /**
     * Default class constructor specifying the staff member that must be in the content 
     * @param staffMember
     */
    public StaffMemberVehicleContentProvider(StaffMember staffMember)
    {
        this.staffMember = staffMember;
    }

    @Override
    public Object[] getElements(Object arg0)
    {
    	List<StaffMember> list = ModelFactory.getInstance().getStaffList().getUnassignedCheckedInStaffListByLocation(location);

        if(staffMember != null &! list.contains(staffMember))
            list.add(staffMember);
        return list.toArray();
    }

    @Override
    public void dispose() { }

    @Override
    public void inputChanged(Viewer arg0, Object arg1, Object arg2) { }
}
