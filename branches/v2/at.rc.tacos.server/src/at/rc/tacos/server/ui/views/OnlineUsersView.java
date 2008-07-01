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
import at.rc.tacos.server.net.OnlineUserManager;
import at.rc.tacos.server.ui.providers.OnlineUserContentProvider;
import at.rc.tacos.server.ui.providers.OnlineUserLabelProvider;

public class OnlineUsersView extends ViewPart implements PropertyChangeListener
{
	public static final String ID = "at.rc.tacos.server.ui.views.OnlineUsersView";

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
		form.setText("Aktive Benutzer");
		form.setImage(ImageFactory.getInstance().getRegisteredImage("server.view.users"));
		toolkit.decorateFormHeading(form);

		//layout the body
		final Composite body = form.getBody();
		body.setLayout(new FillLayout());

		//setup the table viewer
		viewer = new TableViewer(body, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		viewer.setContentProvider(new OnlineUserContentProvider());
		viewer.setLabelProvider(new OnlineUserLabelProvider());
		viewer.setInput(getViewSite());
		viewer.getTable().setLinesVisible(true);

		//create the table for the roster entries 
		final Table table = viewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		final TableColumn columnType = new TableColumn(table, SWT.NONE);
		columnType.setToolTipText("Der Typ des Verbundenen Benutzers");
		columnType.setWidth(24);
		columnType.setText("");

		final TableColumn columnUser = new TableColumn(table, SWT.NONE);
		columnUser.setToolTipText("Der Benutzername");
		columnUser.setWidth(150);
		columnUser.setText("Benutzername");

		final TableColumn columnOnline = new TableColumn(table, SWT.NONE);
		columnOnline.setToolTipText("Online seit");
		columnOnline.setWidth(120);
		columnOnline.setText("Online seit");

		final TableColumn columnIP = new TableColumn(table, SWT.NONE);
		columnIP.setToolTipText("Die Client IP-Addresse mit der der Benutzer mit dem Server verbunden ist");
		columnIP.setWidth(120);
		columnIP.setText("IP");

		//listen to new users
		OnlineUserManager.getInstance().addPropertyChangeListener(this);
	}

	/**
	 * Called when the view is destroyed
	 */
	@Override
	public void dispose()
	{
		OnlineUserManager.getInstance().removePropertyChangeListener(this);
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
	public void propertyChange(PropertyChangeEvent event) 
	{
		final String eventName = event.getPropertyName();
		//the updates should run in the ui thread
		Display.getDefault().syncExec(new Runnable()
		{
			@Override
			public void run() 
			{
				//just update the view
				if("ONLINEUSER_ADDED".equalsIgnoreCase(eventName) || "ONLINEUSER_REMOVED".equalsIgnoreCase(eventName))
				{
					viewer.refresh(true);
				}
			}
		});
	}
}