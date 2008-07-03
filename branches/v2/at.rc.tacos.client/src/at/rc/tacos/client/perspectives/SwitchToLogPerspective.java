package at.rc.tacos.client.perspectives;

import org.eclipse.jface.resource.ImageDescriptor;

import at.rc.tacos.factory.ImageFactory;

/**
 * This is a workbench action to switch to the log perspective
 * @author Michael
 */
public class SwitchToLogPerspective extends AbstractPerspectiveSwitcher
{
	public SwitchToLogPerspective() 
	{
		super(LogPerspective.ID);
	}
	
    /**
     * Returns the image for the perspective
     */
    @Override
    public ImageDescriptor getImageDescriptor()
    {
        return ImageFactory.getInstance().getRegisteredImageDescriptor("toolbar.log");
    }

   /**
    * Returns the text to render
    */
    @Override
    public String getText()
    {
        return "Log";
    }

   /**
    * The tooltip text
    */
    @Override
    public String getToolTipText()
    {
        return "Zeigt alle mitprotokollierten Meldungen und Fehler an.";
    }
}
