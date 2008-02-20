package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;

/**
 * Provides an action to send a delete request to the server to delete
 * the content of the current editor page
 * @author Michael
 */
public class EditorDeleteAction extends Action
{
	//properties
	private String modelId;
	private AbstractMessage object;
	
	/**
	 * Default class constructor setting the type and the object to delete
	 * @param modelId the identification string of the object
	 * @param object the object to remove
	 */
	public EditorDeleteAction(String modelId,AbstractMessage object)
	{
		this.modelId = modelId;
		this.object = object;
	}
	
	/**
	 * Request the server to delete the object
	 */
	@Override
	public void run()
	{
		NetWrapper.getDefault().sendRemoveMessage(modelId, object);
	}

    /**
     * Returns the image to use for this action.
     * @return the image to use
     */
    @Override
    public ImageDescriptor getImageDescriptor() 
    {
        return ImageFactory.getInstance().getRegisteredImageDescriptor("admin.remove");
    }
}