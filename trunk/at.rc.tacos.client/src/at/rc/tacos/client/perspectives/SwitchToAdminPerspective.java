package at.rc.tacos.client.perspectives;

/**
 * This is a workbench action to switch to the administrator view
 * @author Michael
 */
public class SwitchToAdminPerspective extends AbstractPerspectiveSwitcher
{
    public SwitchToAdminPerspective() 
    {
        super(AdminPerspective.ID);
    }
}
