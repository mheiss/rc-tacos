package at.rc.tacos.client.providers;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.model.VehicleDetail;

public class AssignVehicleContentProvider implements IStructuredContentProvider
{
    /**
     * Default class constructor for a content provider
     */
    public AssignVehicleContentProvider()
    {}

    @Override
    public Object[] getElements(Object arg0)
    {
    	List<VehicleDetail> list = ModelFactory.getInstance().getVehicleManager().getReadyVehicleList();
        return list.toArray();
    }

    @Override
    public void dispose() { }

    @Override
    public void inputChanged(Viewer arg0, Object arg1, Object arg2) { }
}
