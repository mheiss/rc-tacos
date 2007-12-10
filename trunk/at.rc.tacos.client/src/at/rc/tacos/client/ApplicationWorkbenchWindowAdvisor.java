package at.rc.tacos.client;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.application.*;

import at.rc.tacos.client.controller.CreateNewVehicle;
import at.rc.tacos.client.controller.MyToolbarManager;
import at.rc.tacos.client.controller.OpenAboutAction;
import at.rc.tacos.client.controller.OpenRosterEntryAction;
import at.rc.tacos.factory.ImageFactory;

/**
 * This class is used to control the status line, toolbar, title, 
 * window size, and other things can be customize.
 * @author Michael
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor 
{   
    //properties
    private Composite logo;
    private CoolBar toolbar;
    private Composite page;

    private static Image imageLogo = ImageFactory.getInstance().getRegisteredImage("toolbar.logo");
    private static Image imageLeft = ImageFactory.getInstance().getRegisteredImage("toolbar.left");

    /**
     * Creates the application workbench advisor.
     * @param configurer the configuring workbench information
     * @return the configuration information for a workbench window
     */
    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) 
    {
        super(configurer);
    }

    /**
     * Creates the action bar.
     * @param configurer the configuration action bar information
     * @return the configuration information for a action bar
     */
    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) 
    {
        return new ApplicationActionBarAdvisor(configurer);
    }

    /**
     * Called in the constructor of the workbench window
     */
    public void preWindowOpen() 
    {        
        //get access to the configuration interface
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(1024, 768));
        configurer.setTitle("Time and Coordination System");
        configurer.setShowCoolBar(false);    //ToolBar
        configurer.setShowStatusLine(true);
        configurer.setShowProgressIndicator(true);   
        configurer.setShowPerspectiveBar(true);
    }

    /**
     * Creates the content of the window
     * @param shell the shell to create the content
     */
    @Override
    public void createWindowContents(final Shell shell) 
    {
        final IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        final Menu menu = configurer.createMenuBar();
        shell.setMenuBar(menu);
        final FormLayout layout = new FormLayout();
        shell.setLayout(layout);
        
        //create the logo
        logo = new Composite(getWindowConfigurer().getWindow().getShell(), SWT.NONE);
        logo.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
        logo.setLayout(new GridLayout(3,false));
        
        final Label left = new Label(logo, SWT.NONE);
        left.setImage(imageLeft);

        GridData gd = new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false);
        left.setLayoutData(gd);

        //create the fill image composite
        final Composite fill = new Composite(logo, SWT.NONE);
        fill.setLayout(new GridLayout(2,false));
        fill.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
        fill.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        // The toolbar
        final MyToolbarManager tbm = new MyToolbarManager(new ToolBar(fill, SWT.FLAT));
        tbm.getControl().setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
        tbm.getControl().setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, true));
        tbm.add(new OpenRosterEntryAction());
        tbm.add(new OpenAboutAction());
        tbm.add(new CreateNewVehicle());
        tbm.update(true);
        toolbar = (CoolBar)configurer.createCoolBarControl(shell);
        toolbar.setLocked(true);
        
        //The logo on the right side
        final Label right = new Label(logo, SWT.NONE);
        right.setImage(imageLogo);
        right.setLayoutData(new GridData(SWT.END, SWT.BEGINNING, true, true));

        //the page for this content
        page = (Composite)configurer.createPageComposite(shell);
        
        //layout the components
        FormData data = new FormData();
        data.top = new FormAttachment(0, 0);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        logo.setLayoutData(data);
        data = new FormData();
        data.top = new FormAttachment(this.logo, 0, SWT.BOTTOM);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(this.page, 0, SWT.LEFT);
        toolbar.setLayoutData(data);
        data = new FormData();
        data.top = new FormAttachment(this.logo, 5, SWT.BOTTOM);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.bottom = new FormAttachment(100,0);
        page.setLayoutData(data);
        
        //Tell the page to adjust the layout
        getWindowConfigurer().getWindow().getShell().layout(true);
        
        if (page != null) 
            page.layout(true);
    }
}
