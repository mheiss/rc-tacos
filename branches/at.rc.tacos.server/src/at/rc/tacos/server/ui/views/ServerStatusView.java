package at.rc.tacos.server.ui.views;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.factory.ImageFactory;

public class ServerStatusView extends ViewPart 
{
	public static final String ID = "at.rc.tacos.server.ui.views.ServerStatusView";
	
	//the properties of the view
	private FormToolkit toolkit;
	private Form form;
	
	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) 
	{
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createForm(parent);
		form.setText("Serverstatus");
		form.setImage(ImageFactory.getInstance().getRegisteredImage("item.users"));
		toolkit.decorateFormHeading(form);
		
		//the client
		Composite body = form.getBody();
		body.setLayout(new GridLayout());
		body.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		
		
	}
	
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() 
	{
		
	}
}
