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
import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.client.perspectives.SwitchToAdminPerspective;
import at.rc.tacos.client.perspectives.SwitchToClientPerspective;
import at.rc.tacos.client.perspectives.SwitchToLogPerspective;
import at.rc.tacos.client.perspectives.SwitchToTransportDialysePerspective;
import at.rc.tacos.client.perspectives.SwitchToTransportJournalPerspective;
import at.rc.tacos.client.perspectives.SwitchToTransportPerspective;
import at.rc.tacos.client.perspectives.SwitchToTransportPrebookingPerspective;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.StaffMember;

/**
 * The navigation on the top including the toolbar.
 * @author Michael
 */
public class NavigationView extends ViewPart 
{
	public static final String ID = "at.rc.tacos.client.view.navigation"; 
	
    //properties
    private static Image imageLogo = ImageFactory.getInstance().getRegisteredImage("toolbar.logo");
    private Login loginInfo;

	@Override
	public void createPartControl(Composite parent) 
	{
		GridLayout layout = new GridLayout(5,false);
	    parent.setLayout(layout);
	    parent.setBackground(CustomColors.SECTION_BACKGROUND);
	    
	    //create a group for the perspective switch
		final Group perspectiveGroup = new Group(parent, SWT.NONE);
		perspectiveGroup.setLayout(new GridLayout());
		perspectiveGroup.setLayoutData(new GridData());
		perspectiveGroup.setBackground(CustomColors.SECTION_BACKGROUND);
		perspectiveGroup.setText("Ansichten");
	    
	    //the toolbar to switch the views
	    final MyToolbarManager tbmView = new MyToolbarManager(new ToolBar(perspectiveGroup,SWT.FLAT));
	    tbmView.getControl().setBackground(CustomColors.SECTION_BACKGROUND);
	    tbmView.getControl().setLayoutData(new GridData(SWT.BEGINNING,SWT.BEGINNING,false,false));
	    tbmView.add(new SwitchToTransportPrebookingPerspective());
	    tbmView.add(new SwitchToTransportPerspective());
	    tbmView.add(new SwitchToTransportJournalPerspective());
	    tbmView.add(new SwitchToTransportDialysePerspective());
	    tbmView.add(new SwitchToClientPerspective());
	    tbmView.update(true);
	    
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
	    
	    //creat ea group for other
		final Group otherGroup = new Group(parent, SWT.NONE);
		otherGroup.setLayout(new GridLayout());
		otherGroup.setLayoutData(new GridData());
		otherGroup.setBackground(CustomColors.SECTION_BACKGROUND);
		otherGroup.setText("Sonstiges");
		
		final MyToolbarManager othView = new MyToolbarManager(new ToolBar(otherGroup,SWT.FLAT));
		othView.getControl().setBackground(CustomColors.SECTION_BACKGROUND);
		othView.getControl().setLayoutData(new GridData(SWT.BEGINNING,SWT.BEGINNING,false,false));
		othView.add(new SwitchToLogPerspective());
		othView.add(new SwitchToAdminPerspective());
		othView.update(true);
		
		if(!Login.AUTH_ADMIN.equals("Administrator"))
			otherGroup.setVisible(false);

	    Composite comp1 = new Composite(parent,SWT.NONE);
	    comp1.setBackground(CustomColors.SECTION_BACKGROUND);
	    GridData gd = new GridData(SWT.BEGINNING,SWT.BEGINNING, false,false);
	    gd.widthHint = 490;
	    comp1.setLayoutData(gd);
	    
	    Label headerImageLabel = new Label(parent,SWT.NONE);
	    headerImageLabel.setImage(imageLogo);
	    gd = new GridData(SWT.BEGINNING,SWT.CENTER,false,false);
	    headerImageLabel.setLayoutData(gd);
	}

	@Override
	public void setFocus() { }
}
