package at.rc.tacos.client.ui.providers;

import org.eclipse.jface.viewers.LabelProvider;

import at.rc.tacos.platform.model.ServiceType;

public class ServiceTypeLabelProvider extends LabelProvider 
{
    /**
     * Returns the text to render.
     */
    @Override
    public String getText(Object object)
    {
    	ServiceType service = (ServiceType)object;
        return service.getServiceName();
    }
}