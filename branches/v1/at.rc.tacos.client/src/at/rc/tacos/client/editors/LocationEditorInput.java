package at.rc.tacos.client.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import at.rc.tacos.model.Location;

public class LocationEditorInput implements IEditorInput
{
	//the location for the editor
	private Location location;
	private boolean isNew;

	/**
	 * Default class constructor for a new location editor input.
	 * To add a new location and to open a empty editor the flag is new shoul be true.
	 * @param location the location to manage
	 * @param isNew true if the editor should be empty
	 */
	public LocationEditorInput(Location location,boolean isNew)
	{
		this.location = location;
		this.isNew = isNew;
	}

	@Override
	public boolean exists() 
	{
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() 
	{
		return null;
	}

	@Override
	public String getName() 
	{
		if(isNew)
			return "Neue Dienststelle";
		return location.getLocationName();
	}

	@Override
	public IPersistableElement getPersistable() 
	{
		return null;
	}

	@Override
	public String getToolTipText() 
	{
		return "Dienststelle "+ location.getLocationName();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object getAdapter(Class arg0)
	{
		return null;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof LocationEditorInput)
		{
			LocationEditorInput otherEditor = (LocationEditorInput)other;
			return location.equals(otherEditor.getLocation());
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return location.hashCode();
	}

	/**
	 * Returns the location managed by this editor 
	 * @return the location
	 */
	public Location getLocation()
	{
		return location;
	}

	/**
	 * Returns whether or not the location is new.
	 * @return true if the location is created new
	 */
	public boolean isNew()
	{
		return isNew;
	}
}
