package at.rc.tacos.client.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import at.rc.tacos.client.view.NavigationView;
import at.rc.tacos.client.view.OutstandingTransportsView;
import at.rc.tacos.client.view.UnderwayTransportsView;

/**
 * The perspective for the transport overview
 * @author Michael
 */
public class TransportPerspective implements IPerspectiveFactory 
{
    public static final String ID = "at.rc.tacos.client.perspectives.transport";
    
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
        layout.addStandaloneView(NavigationView.ID, true, IPageLayout.TOP, 0.10f, editorArea);
        layout.addStandaloneView(UnderwayTransportsView.ID, true, IPageLayout.TOP, 0.45f, editorArea);
        layout.addStandaloneView(OutstandingTransportsView.ID,true, IPageLayout.BOTTOM, 0.45f, editorArea);
    }
}
