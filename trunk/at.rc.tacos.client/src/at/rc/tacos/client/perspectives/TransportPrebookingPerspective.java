package at.rc.tacos.client.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import at.rc.tacos.client.view.FilterView;
import at.rc.tacos.client.view.NavigationView;
import at.rc.tacos.client.view.PrebookingView;
import at.rc.tacos.client.view.SearchView;

public class TransportPrebookingPerspective implements IPerspectiveFactory 
{
    public static final String ID = "at.rc.tacos.client.perspectives.transportPrebooking";
    
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
        layout.addStandaloneView(PrebookingView.ID,false, IPageLayout.RIGHT, 0.90f, editorArea);
        layout.addStandaloneView(SearchView.ID, false, IPageLayout.BOTTOM, 0.30f, editorArea);
        layout.addStandaloneView(FilterView.ID,false,IPageLayout.LEFT,0.18f,editorArea);
        
    }
}
