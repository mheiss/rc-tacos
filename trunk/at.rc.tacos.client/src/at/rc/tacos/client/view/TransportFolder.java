package at.rc.tacos.client.view;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;


public class TransportFolder extends ViewPart
{
	public static final String ID = "at.rc.tacos.client.view.folder_view";

	
	/**
	 * Constructs a new folder to contain the transport views
	 */
	public TransportFolder()
	{
		
	}
	
	/**
	 * Cleanup the view
	 */
	@Override
	public void dispose() 
	{
		
	}

	/**
	 * Call back method to create the control and initialize them.
	 * @param parent the parent composite to add
	 */
	@Override
	public void createPartControl(final Composite parent) 
	{
		
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus()  { }

	
}