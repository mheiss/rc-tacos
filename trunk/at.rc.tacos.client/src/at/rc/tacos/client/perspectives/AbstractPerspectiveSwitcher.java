package at.rc.tacos.client.perspectives;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

public class AbstractPerspectiveSwitcher extends Action implements IPerspectiveListener 
{
    /**
     * Instanciates an action and registers a perspective-listener
     * @param text
     */
    public AbstractPerspectiveSwitcher(String text) 
    {
        super(text,IAction.AS_RADIO_BUTTON);
        setId(text);
        IWorkbench workbench = PlatformUI.getWorkbench();
        setText(workbench.getPerspectiveRegistry().findPerspectiveWithId(getId()).getLabel());
        setImageDescriptor(workbench.getPerspectiveRegistry().findPerspectiveWithId(getId()).getImageDescriptor());
    }
    /**
     * Sets the perspective
     */
    @Override
	public void run() 
    {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        page.setPerspective(PlatformUI.getWorkbench().getPerspectiveRegistry().findPerspectiveWithId(getId()));
    }
    
    /**
     * Called to activate a perspective
     * @param page the workbench page
     * @param perspective the perspective to activate
     */
    public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) 
    {
        if (perspective.getId().equals(getId())) 
            setChecked(true);
        else 
            setChecked(false); 
    }
    /**
     * Called when the perspective is changed
     * @param page the changed page
     * @param perspective the new perspective
     * @param changeId the id of the new perspective
     */
    public void perspectiveChanged(IWorkbenchPage page, IPerspectiveDescriptor perspective, String changeId) 
    {
        // do nothing.
    }

}

