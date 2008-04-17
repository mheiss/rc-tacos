package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.client.providers.MultiTransportContentProvider;
import at.rc.tacos.client.view.TransportForm;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Transport;

public class RemoveTransportFromMultiTransportList extends Action
{
	private Transport transport;
	private MultiTransportContentProvider provider;
	
	public RemoveTransportFromMultiTransportList(Transport transport, MultiTransportContentProvider provider)
	{
		this.transport = transport;
		this.provider = provider;
	}
    /**
     * Returns the tool tip text for the action
     * @return the tool tip text
     */
    @Override
    public String getToolTipText() 
    {
        return "Öffnet ein Fenster um einen neuen Transportauftrag zu erstellen";
    }
    
    /**
     * Returns the text to show in the tool bar
     * @return the text to render
     */
    @Override
    public String getText()
    {
        return "Transport";
    }
    
    /**
     * Returns the image to use for this action.
     * @return the image to use
     */
    @Override
    public ImageDescriptor getImageDescriptor() 
    {
        return ImageFactory.getInstance().getRegisteredImageDescriptor("toolbar.createTransport");
    }
    
    /**
     * Shows the abut dialog of the application
     */
    @Override
    public void run()
    {
    	provider.removeTransport(transport);
    }

}
