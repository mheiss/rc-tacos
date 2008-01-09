package at.rc.tacos.client.perspectives;

/**
 * This is a workbench action to switch the 
 * perspective
 * @author Michael
 */
public class SwitchToTransportPerspective extends AbstractPerspectiveSwitcher
{
    public SwitchToTransportPerspective() 
    {
        super(TransportPerspective.ID);
    }
}