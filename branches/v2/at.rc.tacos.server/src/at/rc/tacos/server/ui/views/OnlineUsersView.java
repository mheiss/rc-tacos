package at.rc.tacos.server.ui.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.factory.ImageFactory;
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
		form.setImage(ImageFactory.getInstance().getRegisteredImage("item.users"));
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

		final TableColumn lockColumn = new TableColumn(table, SWT.NONE);
		lockColumn.setToolTipText("Der Typ des Verbundenen Benutzers");
		lockColumn.setWidth(24);
		lockColumn.setText("");

		final TableColumn columnStandby = new TableColumn(table, SWT.NONE);
		columnStandby.setToolTipText("Der Benutzername");
		columnStandby.setWidth(150);
		columnStandby.setText("Benutzername");

		final TableColumn columnNotes = new TableColumn(table, SWT.NONE);
		columnNotes.setToolTipText("Online seit");
		columnNotes.setWidth(120);
		columnNotes.setText("Online seit");
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
	public void propertyChange(PropertyChangeEvent evt) 
	{
		final String event = evt.getPropertyName();
		
		//check the event
		if("ONLINEUSER_ADDED".equalsIgnoreCase(event) || "ONLINEUSER_REMOVED".equalsIgnoreCase(event))
		{
			//just update the view
			viewer.refresh(true);
		}
	}
}