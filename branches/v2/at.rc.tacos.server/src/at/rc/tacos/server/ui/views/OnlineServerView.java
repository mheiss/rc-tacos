package at.rc.tacos.server.ui.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Session;
import at.rc.tacos.server.net.manager.SessionManager;
import at.rc.tacos.server.ui.filter.ServerFilter;
import at.rc.tacos.server.ui.providers.ServerSessionContentProvider;
import at.rc.tacos.server.ui.providers.ServerSessionLabelProvider;

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

		//setup the table viewer
		viewer = new TableViewer(body, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		viewer.setContentProvider(new ServerSessionContentProvider());
		viewer.setLabelProvider(new ServerSessionLabelProvider());
		viewer.setInput(getViewSite());
		viewer.getTable().setLinesVisible(true);

		//create the table for the roster entries 
		final Table table = viewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		final TableColumn columnUser = new TableColumn(table, SWT.NONE);
		columnUser.setToolTipText("Der Name des Servers");
		columnUser.setWidth(150);
		columnUser.setText("Servername");

		final TableColumn columnOnline = new TableColumn(table, SWT.NONE);
		columnOnline.setToolTipText("Online seit");
		columnOnline.setWidth(120);
		columnOnline.setText("Online seit");

		final TableColumn columnIP = new TableColumn(table, SWT.NONE);
		columnIP.setToolTipText("Die IP-Addresse des Servers");
		columnIP.setWidth(120);
		columnIP.setText("Server Addresse");
		
		//add the server filter to show only server connections
		viewer.addFilter(new ServerFilter());

		//listen to new users
		SessionManager.getInstance().addPropertyChangeListener(this);
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
		final String eventName = event.getPropertyName();
		//the updates should run in the ui thread
		Display.getDefault().syncExec(new Runnable()
		{
			@Override
			public void run() 
			{
				//a new server was added
				if(SessionManager.SESSION_ADDED.equalsIgnoreCase(eventName))
				{
					//get the new user and update the view
					Session session = (Session)event.getNewValue();
					viewer.add(session);
				}
				//a existing server was updated
				if(SessionManager.SESSION_UPDATED.equalsIgnoreCase(eventName))
				{
					//the updated session
					Session session = (Session)event.getNewValue();
					viewer.refresh(session);
				}
				//the server object was removed
				if(SessionManager.SESSION_REMOVED.equalsIgnoreCase(eventName))
				{
					//the removed session
					Session session = (Session)event.getOldValue();
					viewer.remove(session);
				}
				//all session object were removed
				if(SessionManager.SESSION_CLEARED.equalsIgnoreCase(eventName))
				{
					viewer.refresh();
				}
			}
		});
	}
}
