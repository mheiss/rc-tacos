package at.rc.tacos.client.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import at.rc.tacos.client.view.DialysisView;
import at.rc.tacos.client.view.NavigationView;

/**
 * This is the perspective to show the dialyse transports
 * @author Michael
 */
public class TransportDialysePerspective implements IPerspectiveFactory 
{
    public static final String ID = "at.rc.tacos.client.perspectives.transportDialyse";
    
    /**
     * Set up the layout of the workbench
     * @param layout the page layout to use
     */
    public void createInitialLayout(IPageLayout layout) 
    {
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);
        layout.setFixed(true);
        //the main components
        layout.addStandaloneView(NavigationView.ID, false, IPageLayout.TOP, 0.10f, editorArea);
        layout.addStandaloneView(DialysisView.ID, false, IPageLayout.RIGHT, 0.90f, editorArea);
    }
}