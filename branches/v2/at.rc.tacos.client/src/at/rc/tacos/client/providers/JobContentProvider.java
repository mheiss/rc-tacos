package at.rc.tacos.client.providers;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import at.rc.tacos.client.modelManager.ModelFactory;

public class JobContentProvider implements IStructuredContentProvider
{
    @Override
    public Object[] getElements(Object arg0)
    {
        return ModelFactory.getInstance().getJobList().toArray();
    }

    @Override
    public void dispose() { }

    @Override
    public void inputChanged(Viewer arg0, Object arg1, Object arg2) { }
}
