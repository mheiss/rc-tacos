package at.rc.tacos.client.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * The perspective for the administrator
 * @author Michael
 */
public class AdminPerspective implements IPerspectiveFactory 
{
    public static final String ID = "at.rc.tacos.client.perspectives.admin";
    
    /**
     * Set up the layout of the workbench
     * @param layout the page layout to use
     */
    public void createInitialLayout(IPageLayout layout) 
    {
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);
        layout.setFixed(true);
        layout.addStandaloneView("org.eclipse.ui.views.log", false,IPageLayout.LEFT, 0.25f, editorArea);
    }
}
