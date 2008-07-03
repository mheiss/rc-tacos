package at.rc.tacos.server.ui.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.server.net.manager.SessionManager;

public class OnlineServerView extends ViewPart implements PropertyChangeListener
{
	//the view id 
	public static final String ID = "at.rc.tacos.server.ui.views.OnlineServerView";
	
	//the table viewer
	private TableViewer viewer;
	private FormToolkit toolkit;
	private Form form;
	
	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) 
	{
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createForm(parent);
		form.setText("Aktive Server");
		form.setImage(ImageFactory.getInstance().getRegisteredImage("server.view.servers"));
		toolkit.decorateFormHeading(form);

		//layout the body
		final Composite body = form.getBody();
		body.setLayout(new FillLayout());	
	}

	/**
	 * Called when the view is destroyed
	 */
	@Override
	public void dispose()
	{
		SessionManager.getInstance().removePropertyChangeListener(this);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	/**
	 * Listen to updates
	 */
	@Override
	public void propertyChange(final PropertyChangeEvent event) 
	{
		
	}
}
