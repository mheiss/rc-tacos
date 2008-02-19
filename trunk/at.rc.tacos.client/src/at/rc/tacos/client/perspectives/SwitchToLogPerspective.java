package at.rc.tacos.client.perspectives;

import org.eclipse.jface.resource.ImageDescriptor;

import at.rc.tacos.factory.ImageFactory;

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
        return "Öffnet ein Fenster um geloggten Meldungen anzuzeigen";
    }
}
