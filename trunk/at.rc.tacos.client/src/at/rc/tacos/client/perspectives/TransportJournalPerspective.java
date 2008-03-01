package at.rc.tacos.client.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import at.rc.tacos.client.view.FilterView;
import at.rc.tacos.client.view.JournalView;
import at.rc.tacos.client.view.NavigationView;

public class TransportJournalPerspective implements IPerspectiveFactory 
{
    public static final String ID = "at.rc.tacos.client.perspectives.transportJournal";
    
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
        layout.addStandaloneView(JournalView.ID,false, IPageLayout.RIGHT, 0.25f, editorArea);
        layout.addStandaloneView(FilterView.ID, false, IPageLayout.LEFT, 0.75f, editorArea);
    }
}
