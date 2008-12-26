package at.rc.tacos.client.ui.admin.view;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.net.handler.LocationHandler;
import at.rc.tacos.client.ui.admin.editors.LocationEditor;
import at.rc.tacos.client.ui.admin.editors.LocationEditorInput;
import at.rc.tacos.client.ui.controller.EditorNewLocationAction;
import at.rc.tacos.client.ui.controller.RefreshViewAction;
import at.rc.tacos.client.ui.providers.StationLabelProvider;
import at.rc.tacos.client.ui.utils.CustomColors;
import at.rc.tacos.platform.model.Location;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.listeners.DataChangeListener;
import at.rc.tacos.platform.net.message.GetMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;

public class LocationAdminView extends ViewPart implements DataChangeListener<Location> {

	public static final String ID = "at.rc.tacos.client.view.admin.locationAdminView";

	private Logger log = LoggerFactory.getLogger(LocationAdminView.class);

	// properties
	private TableViewer viewer;
	private FormToolkit toolkit;
	private ScrolledForm form;

	// the data source
	private LocationHandler locationHandler = (LocationHandler) NetWrapper.getHandler(Location.class);

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		// the scrolled form
		toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
		form = toolkit.createScrolledForm(parent);
		form.setText("Liste der Dienststellen");
		toolkit.decorateFormHeading(form.getForm());
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		form.getBody().setLayout(layout);
		form.getBody().setLayoutData(new GridData(GridData.FILL_BOTH));

		viewer = new TableViewer(form.getBody(), SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.getTable().setLayout(new GridLayout());
		viewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		viewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent dce) {
				// get the selected location
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				Location location = (Location) obj;
				// create the editor input and open
				LocationEditorInput input = new LocationEditorInput(location, false);
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					page.openEditor(input, LocationEditor.ID);
				}
				catch (PartInitException e) {
					log.error("Failed to open the editor for the location " + location, e);
				}
			}
		});
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new StationLabelProvider());
		viewer.setInput(locationHandler.toArray());
		getViewSite().setSelectionProvider(viewer);

		// add actions to the toolbar
		createToolBarActions();

		// set this table as a selection provider
		getViewSite().setSelectionProvider(viewer);

		// register the listener
		NetWrapper.registerListener(this, Location.class);

		// disable the form if we do not have an admin
		String authorization = NetWrapper.getSession().getLogin().getAuthorization();
		if (!authorization.equalsIgnoreCase("Administrator"))
			form.setEnabled(false);
	}

	@Override
	public void dispose() {
		NetWrapper.removeListener(this, Location.class);
	}

	/**
	 * Passes the focus to the view
	 */
	@Override
	public void setFocus() {
		form.setFocus();
	}

	@Override
	public void dataChanged(Message<Location> message, MessageIoSession messageIoSession) {
		viewer.refresh();
	}

	/**
	 * Creates and adds the actions for the toolbar
	 */
	private void createToolBarActions() {
		GetMessage<Location> getMessage = new GetMessage<Location>(new Location());
		// create the action
		EditorNewLocationAction addAction = new EditorNewLocationAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		RefreshViewAction<Location> viewAction = new RefreshViewAction<Location>(getMessage);
		// add to the toolbar
		form.getToolBarManager().add(addAction);
		form.getToolBarManager().add(viewAction);
		form.getToolBarManager().update(true);
	}
}
