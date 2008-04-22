package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.client.providers.MultiTransportContentProvider;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Transport;

public class RemoveTransportFromMultiTransportList extends Action
{
	private Transport transport;
	private MultiTransportContentProvider provider;
	private TableViewer viewer;
	
	public RemoveTransportFromMultiTransportList(Transport transport, MultiTransportContentProvider provider, TableViewer viewer)
	{
		this.transport = transport;
		this.provider = provider;
		this.viewer = viewer;
	}
    /**
     * Returns the tool tip text for the action
     * @return the tool tip text
     */
    @Override
    public String getToolTipText() 
    {
        return "Löscht den markierten Transport aus der Liste der zu speichernden Transporte";
    }
    
    /**
     * Returns the text to show in the tool bar
     * @return the text to render
     */
    @Override
    public String getText()
    {
        return "Löschen";
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
    	viewer.refresh();
    }

}
