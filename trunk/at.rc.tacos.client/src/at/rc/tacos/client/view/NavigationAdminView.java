package at.rc.tacos.client.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.client.modelManager.MyToolbarManager;
import at.rc.tacos.client.perspectives.SwitchToClientPerspective;
import at.rc.tacos.client.perspectives.SwitchToLogPerspective;
import at.rc.tacos.client.perspectives.SwitchToTransportPerspective;
import at.rc.tacos.factory.ImageFactory;

/**
 * The navigation on the top for the admin area
 * @author Michael
 */
public class NavigationAdminView extends ViewPart 
{
	public static final String ID = "at.rc.tacos.client.view.navigationAdmin"; 
	
    //properties
    private static Image imageLogo = ImageFactory.getInstance().getRegisteredImage("toolbar.logo");

	@Override
	public void createPartControl(Composite parent) 
	{
		Color white = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
		GridLayout layout = new GridLayout(5,false);
	    parent.setLayout(layout);
	    parent.setBackground(white);
	    
//	    IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        
	    //Create the toolbar
	    final MyToolbarManager tbm = new MyToolbarManager(new ToolBar(parent, SWT.FLAT));
	    tbm.getControl().setBackground(new Color(null,new RGB(255,255,255)));
	    tbm.getControl().setLayoutData(new GridData(SWT.BEGINNING,SWT.BEGINNING,false,false));
	    //add the actions
        tbm.add(new SwitchToClientPerspective());
        tbm.add(new SwitchToTransportPerspective());
        tbm.add(new SwitchToLogPerspective());
//        tbm.add(new EditorNewStaffAction(window));
//        tbm.add(new EditorNewLocationAction(window));
//        tbm.add(new EditorNewVehicleAction(window));
//        tbm.add(new EditorNewMobilePhoneAction(window));
//        tbm.add(new EditorNewCompetenceAction(window));
//        tbm.add(new EditorNewJobAction(window));
//        tbm.add(new EditorNewServiceTypeAction(window));
        tbm.update(true);

	    Composite comp1 = new Composite(parent,SWT.NONE);
	    comp1.setBackground(white);
	    GridData gd = new GridData(SWT.BEGINNING,SWT.BEGINNING, false,false);
	    gd.widthHint = 20;
	    comp1.setLayoutData(gd);
	    
	    Composite comp2 = new Composite(parent,SWT.NONE);
	    comp2.setBackground(white);
	    gd = new GridData(SWT.FILL,SWT.BEGINNING, true,false);
	    comp2.setLayoutData(gd);
	    
	    Label headerImageLabel = new Label(parent,SWT.NONE);
	    headerImageLabel.setImage(imageLogo);
	    gd = new GridData(SWT.BEGINNING,SWT.CENTER,false,false);
	    headerImageLabel.setLayoutData(gd);
	}

	@Override
	public void setFocus() { }
}