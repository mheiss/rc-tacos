package at.rc.tacos.client.perspectives;

import org.eclipse.jface.resource.ImageDescriptor;

import at.rc.tacos.factory.ImageFactory;

public class SwitchToTransportDialysePerspective  extends AbstractPerspectiveSwitcher
{
    public SwitchToTransportDialysePerspective() 
    {
        super(TransportDialysePerspective.ID);
    }
    
    /**
     * Returns the image for the perspective
     */
    @Override
    public ImageDescriptor getImageDescriptor()
    {
        return ImageFactory.getInstance().getRegisteredImageDescriptor("toolbar.transportDialyse");
    }

   /**
    * Returns the text to render
    */
    @Override
    public String getText()
    {
        return "Dialysetransporte";
    }

   /**
    * The tooltip text
    */
    @Override
    public String getToolTipText()
    {
        return "Zeigt eine Übersicht über alle Dialyse Transporte an";
    }
}
