package at.rc.tacos.client.perspectives;

import org.eclipse.jface.resource.ImageDescriptor;

import at.rc.tacos.factory.ImageFactory;

public class SwitchToTransportPrebookingPerspective  extends AbstractPerspectiveSwitcher
{
    public SwitchToTransportPrebookingPerspective() 
    {
        super(TransportPrebookingPerspective.ID);
    }
    
    /**
     * Returns the image for the perspective
     */
    @Override
    public ImageDescriptor getImageDescriptor()
    {
        return ImageFactory.getInstance().getRegisteredImageDescriptor("toolbar.transportPrebooking");
    }

   /**
    * Returns the text to render
    */
    @Override
    public String getText()
    {
        return "Vormerkungen";
    }

   /**
    * The tooltip text
    */
    @Override
    public String getToolTipText()
    {
        return "Zeigt eine Übersicht über alle vorgemerkten Transporte an.";
    }
}
