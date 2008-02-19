package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.QueryFilter;

/**
 * This action sends a listing request to the server to update the displayed data
 * @author Michael
 */
public class RefreshViewAction  extends Action
{
	//the date, if needed for the query
	private String modelId;
	private long date;
	
	/**
	 * Default class constructor to request a refresh
	 * @param modelId the id of the model to request the update
	 */
	public RefreshViewAction(String modelId)
	{
		this.modelId = modelId;
	}
	
	/**
	 * Default class constructor to request a filtered request
	 * @param modelId the id of the model to request the update
	 * @param date the date period to limit the data
	 */
	public RefreshViewAction(String modelId,long date)
	{
		this.modelId = modelId;
		this.date = date;
	}
	
	/**
	 * Runs the action
	 */
	public void run()
	{
		//filtered by date?
		if(date > 0)
		{
			QueryFilter filter = new QueryFilter(IFilterTypes.DATE_FILTER,String.valueOf(date));
			NetWrapper.getDefault().requestListing(modelId, filter);
		}
		else
			NetWrapper.getDefault().requestListing(modelId, null);
	}
	
    /**
     * Retruns the text to show in the toolbar
     * @return the text to render
     */
    @Override
    public String getText()
    {
        return "Ansicht aktualiseren";
    }

    /**
     * Returns the image to use for this action.
     * @return the image to use
     */
    @Override
    public ImageDescriptor getImageDescriptor() 
    {
        return ImageFactory.getInstance().getRegisteredImageDescriptor("resource.refresh");
    }
}
