package at.rc.tacos.client.providers;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.modelManager.RosterEntryManager;

public class PersonalViewContentProvider implements IStructuredContentProvider 
{
    public void inputChanged(Viewer v, Object oldInput, Object newInput) 
    {
        // do nothing
    }

    public void dispose() 
    {
        // do nothing
    }

    public Object[] getElements(Object parent) 
    {
        RosterEntryManager manager = ModelFactory.getInstance().getRosterManager();
        //do we have a active station
        if(manager.getActiveStation() != null && manager.getActiveStation() != "")
            return manager.toFilteredArray();
        else
            return manager.toArray();
    }
}
