package at.rc.tacos.client.perspectives;

import org.eclipse.jface.resource.ImageDescriptor;

import at.rc.tacos.factory.ImageFactory;

/**
 * This is a workbench action to switch the 
 * perspective
 * @author Michael
 */
public class SwitchToClientPerspective extends AbstractPerspectiveSwitcher
{
    public SwitchToClientPerspective() 
    {
        super(Perspective.ID);
    }
    
    /**
     * Returns the image for the perspective
     */
    @Override
    public ImageDescriptor getImageDescriptor()
    {
        return ImageFactory.getInstance().getRegisteredImageDescriptor("toolbar.resources");
    }

   /**
    * Returns the text to render
    */
    @Override
    public String getText()
    {
        return "Resourcen";
    }

   /**
    * The tooltip text
    */
    @Override
    public String getToolTipText()
    {
        return "Öffnet die Ansicht zur Verwaltung des Personals und der Fahrzeuge";
    }
}
