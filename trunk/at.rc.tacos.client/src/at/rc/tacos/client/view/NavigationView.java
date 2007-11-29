package at.rc.tacos.client.view;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.client.controller.OpenAboutAction;
import at.rc.tacos.client.controller.OpenRosterEntryAction;
import at.rc.tacos.factory.ImageFactory;

/**
 * This class contains the top navigation bar and the logo
 * @author Michael
 */
public class NavigationView extends ViewPart
{
    //the ID of the view
    public static final String ID = "at.rc.tacos.client.view.navigation"; 
    
    //the background color for the toolbar
    private static Color TB_BG = new Color(null,new RGB(255,255,255));
    
    /**
     * Creates the controls for the view
     */
    @Override
    public void createPartControl(Composite parent)
    {
        Color white = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
        GridLayout layout = new GridLayout(4,false);
        parent.setLayout(layout);
        parent.setBackground(white);
        
        //Create the first toolbar
        ToolBar toolbar1 = new ToolBar(parent,SWT.FLAT);
        toolbar1.setBackground(TB_BG);
        GridData gd = new GridData(SWT.BEGINNING,SWT.BEGINNING,false,false);
        gd.horizontalIndent = 10;
        gd.verticalIndent = 10;
        toolbar1.setLayoutData(gd);
        ToolBarManager manager1 = new ToolBarManager(toolbar1);
        manager1.add(new OpenAboutAction());
        manager1.update(true);
        
        //create the second toolbar
        ToolBar toolbar2 = new ToolBar(parent,SWT.FLAT);
        toolbar2.setBackground(TB_BG);
        gd = new GridData(SWT.BEGINNING,SWT.BEGINNING,false,false);
        gd.horizontalIndent = 10;
        gd.verticalIndent = 10;
        toolbar2.setLayoutData(gd);
        
        ToolBarManager manager2 = new ToolBarManager(toolbar2);
        Action action = new OpenRosterEntryAction();
        action.setText("Hallo");
        manager2.add(action);
        manager2.update(true);
        
        //Space between the toolbar and the icon
        Composite comp2 = new Composite(parent,SWT.NONE);
        comp2.setBackground(white);
        gd = new GridData(SWT.FILL,SWT.BEGINNING, true,false);
        comp2.setLayoutData(gd);
        
        Label headerImageLabel = new Label(parent,SWT.NONE);
        headerImageLabel.setImage(ImageFactory.getInstance().getRegisteredImage("toolbar.logo"));
        gd = new GridData(SWT.RIGHT,SWT.TOP,false,false);
        headerImageLabel.setLayoutData(gd); 
    }

    /**
     * Passes the focus
     */
    @Override
    public void setFocus() {  }
}
