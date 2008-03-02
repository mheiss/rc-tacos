

package at.rc.tacos.client.providers;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.model.StaffMember;

public class StaffMemberVehicleContentProvider implements IStructuredContentProvider
{
	private StaffMember staffMember;
	
	public StaffMemberVehicleContentProvider(StaffMember staffMember)
	{
		this.staffMember = staffMember;
	}
	
	public StaffMemberVehicleContentProvider()
	{
		
	}
    @Override
    public Object[] getElements(Object arg0)
    {
    	List<StaffMember> list = ModelFactory.getInstance().getStaffList().getUnassignedStaffList();
    	if(staffMember != null)
    		list.add(staffMember);
    	Object[] o = list.toArray();
    	
        return o;
    }

    @Override
    public void dispose() { }

    @Override
    public void inputChanged(Viewer arg0, Object arg1, Object arg2) { }
}
