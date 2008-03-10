package at.rc.tacos.client.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.client.controller.OpenDialysisTransportAction;
import at.rc.tacos.client.controller.OpenEmergencyTransportAction;
import at.rc.tacos.client.controller.OpenTransportAction;
import at.rc.tacos.client.controller.PersonalNewEntryAction;
import at.rc.tacos.client.controller.VehicleOpenAction;
import at.rc.tacos.client.modelManager.MyToolbarManager;
import at.rc.tacos.client.perspectives.SwitchToAdminPerspective;
import at.rc.tacos.client.perspectives.SwitchToClientPerspective;
import at.rc.tacos.client.perspectives.SwitchToLogPerspective;
import at.rc.tacos.client.perspectives.SwitchToTransportDialysePerspective;
import at.rc.tacos.client.perspectives.SwitchToTransportJournalPerspective;
import at.rc.tacos.client.perspectives.SwitchToTransportPerspective;
import at.rc.tacos.client.perspectives.SwitchToTransportPrebookingPerspective;
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
		Color white = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
		Color black = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
		GridLayout layout = new GridLayout(5,false);
	    parent.setLayout(layout);
	    parent.setBackground(white);
	    
	    //create a group for the perspective switch
		final Group perspectiveGroup = new Group(parent, SWT.NONE);
		perspectiveGroup.setLayout(new GridLayout());
		perspectiveGroup.setLayoutData(new GridData());
		perspectiveGroup.setBackground(new Color(null,new RGB(255,255,255)));
		perspectiveGroup.setText("Ansichten");
	    
	    //the toolbar to switch the views
	    final MyToolbarManager tbmView = new MyToolbarManager(new ToolBar(perspectiveGroup,SWT.FLAT));
	    tbmView.getControl().setBackground(new Color(null,new RGB(255,255,255)));
	    tbmView.getControl().setLayoutData(new GridData(SWT.BEGINNING,SWT.BEGINNING,false,false));
	    tbmView.add(new SwitchToClientPerspective());
	    tbmView.add(new SwitchToTransportPerspective());
	    tbmView.add(new SwitchToTransportPrebookingPerspective());
	    tbmView.add(new SwitchToTransportDialysePerspective());
	    tbmView.add(new SwitchToTransportJournalPerspective());
	    tbmView.update(true);
	    
	    
	    //create a group for the perspective switch
		final Group createGroup = new Group(parent, SWT.NONE);
		createGroup.setLayout(new GridLayout());
//		//some space on the left side
//	    GridData data = new GridData(GridData.BEGINNING | GridData.FILL_HORIZONTAL);
//	    data.horizontalIndent = 40;
		createGroup.setLayoutData(new GridData());
		createGroup.setBackground(new Color(null,new RGB(255,255,255)));
		createGroup.setText("Neue Einträge");
	    
	    //Create the toolbar
	    final MyToolbarManager tbmCreate = new MyToolbarManager(new ToolBar(createGroup, SWT.FLAT));
	    tbmCreate.getControl().setBackground(new Color(null,new RGB(255,255,255)));
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
		otherGroup.setBackground(new Color(null,new RGB(255,255,255)));
		otherGroup.setText("Sonstiges");
		
		final MyToolbarManager othView = new MyToolbarManager(new ToolBar(otherGroup,SWT.FLAT));
		othView.getControl().setBackground(new Color(null,new RGB(255,255,255)));
		othView.getControl().setLayoutData(new GridData(SWT.BEGINNING,SWT.BEGINNING,false,false));
		othView.add(new SwitchToLogPerspective());
		othView.add(new SwitchToAdminPerspective());
		othView.add(new VehicleOpenAction());
		othView.update(true);

	    Composite comp1 = new Composite(parent,SWT.NONE);
	    comp1.setBackground(white);
	    GridData gd = new GridData(SWT.BEGINNING,SWT.BEGINNING, false,false);
	    gd.widthHint = 490;
	    comp1.setLayoutData(gd);

	    
//	    Composite comp2 = new Composite(parent,SWT.NONE);
//	    comp2.setBackground(white);
//	    gd = new GridData(SWT.FILL,SWT.BEGINNING, true,false);
//	    comp2.setLayoutData(gd);
	    
	    Label headerImageLabel = new Label(parent,SWT.NONE);
	    headerImageLabel.setImage(imageLogo);
	    gd = new GridData(SWT.BEGINNING,SWT.CENTER,false,false);
	    headerImageLabel.setLayoutData(gd);
	}

	@Override
	public void setFocus() { }
}
