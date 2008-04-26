package at.rc.tacos.client.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.part.ViewPart;
import at.rc.tacos.client.controller.OpenDialysisTransportAction;
import at.rc.tacos.client.controller.OpenEmergencyTransportAction;
import at.rc.tacos.client.controller.OpenTransportAction;
import at.rc.tacos.client.controller.PersonalNewEntryAction;
import at.rc.tacos.client.modelManager.MyToolbarManager;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.factory.ImageFactory;

/**
 * The navigation on the top including the toolbar.
 * @author Michael
 */
public class NavigationView extends ViewPart 
{
	public static final String ID = "at.rc.tacos.client.view.navigation"; 
	
    //properties
    private static Image imageLogo = ImageFactory.getInstance().getRegisteredImage("toolbar.logo");

	@Override
	public void createPartControl(Composite parent) 
	{
		GridLayout layout = new GridLayout(5,false);
	    parent.setLayout(layout);
	    parent.setBackground(CustomColors.SECTION_BACKGROUND);
	    
	    //create a group for the perspective switch
		final Group createGroup = new Group(parent, SWT.NONE);
		createGroup.setLayout(new GridLayout());
		createGroup.setLayoutData(new GridData());
		createGroup.setBackground(CustomColors.SECTION_BACKGROUND);
		createGroup.setText("Neue Einträge");
	    
	    //Create the toolbar
	    final MyToolbarManager tbmCreate = new MyToolbarManager(new ToolBar(createGroup, SWT.FLAT));
	    tbmCreate.getControl().setBackground(CustomColors.SECTION_BACKGROUND);
	    tbmCreate.getControl().setLayout(new GridLayout());
	    tbmCreate.getControl().setLayoutData(new GridData());
	    tbmCreate.add(new PersonalNewEntryAction());
	    tbmCreate.add(new OpenEmergencyTransportAction());
	    tbmCreate.add(new OpenTransportAction());
	    tbmCreate.add(new OpenDialysisTransportAction());
	    tbmCreate.update(true);

	    Composite comp1 = new Composite(parent,SWT.NONE);
	    comp1.setBackground(CustomColors.SECTION_BACKGROUND);
	    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
	    comp1.setLayoutData(gd);
	    
	    Label headerImageLabel = new Label(parent,SWT.NONE);
	    headerImageLabel.setImage(imageLogo);
	    gd = new GridData(SWT.RIGHT,SWT.CENTER,false,false);
	    headerImageLabel.setLayoutData(gd);
	}

	@Override
	public void setFocus() { }
}
