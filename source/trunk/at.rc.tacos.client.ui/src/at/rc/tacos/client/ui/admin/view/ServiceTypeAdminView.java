package at.rc.tacos.client.ui.admin.view;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.net.handler.ServiceTypeHandler;
import at.rc.tacos.client.ui.admin.editors.ServiceTypeEditor;
import at.rc.tacos.client.ui.admin.editors.ServiceTypeEditorInput;
import at.rc.tacos.client.ui.controller.EditorNewServiceTypeAction;
import at.rc.tacos.client.ui.controller.RefreshViewAction;
import at.rc.tacos.client.ui.providers.HandlerContentProvider;
import at.rc.tacos.client.ui.providers.ServiceTypeLabelProvider;
import at.rc.tacos.platform.model.ServiceType;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.listeners.DataChangeListener;
import at.rc.tacos.platform.net.message.GetMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;

public class ServiceTypeAdminView extends ViewPart implements DataChangeListener<ServiceType> {

	public static final String ID = "at.rc.tacos.client.view.admin.serviceTypeAdminView";

	private Logger log = LoggerFactory.getLogger(ServiceTypeAdminView.class);

	// properties
	private TableViewer viewer;
	private FormToolkit toolkit;
	private ScrolledForm form;

	// the data source
	private ServiceTypeHandler serviceHandler = (ServiceTypeHandler) NetWrapper.getHandler(ServiceType.class);

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		toolkit = new FormToolkit(Display.getDefault());
		form = toolkit.createScrolledForm(parent);
		form.setText("Liste der Dienstverhältnisse");
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
				// get the selected service type
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				ServiceType serviceType = (ServiceType) obj;
				// create the editor input and open
				ServiceTypeEditorInput input = new ServiceTypeEditorInput(serviceType, false);
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					page.openEditor(input, ServiceTypeEditor.ID);
				}
				catch (PartInitException e) {
					log.error("Failed to open the editor for the service type " + serviceType, e);
				}
			}
		});
		viewer.setContentProvider(new HandlerContentProvider());
		viewer.setLabelProvider(new ServiceTypeLabelProvider());
		viewer.setInput(serviceHandler);
		getViewSite().setSelectionProvider(viewer);

		// add actions to the toolbar
		createToolBarActions();

		// set this table as a selection provider
		getViewSite().setSelectionProvider(viewer);

		// register the listener
		NetWrapper.registerListener(this, ServiceType.class);

		// disable the form if we do not have an admin
		String authorization = NetWrapper.getSession().getLogin().getAuthorization();
		if (!authorization.equalsIgnoreCase("Administrator"))
			form.setEnabled(false);
	}

	@Override
	public void dispose() {
		NetWrapper.removeListener(this, ServiceType.class);
	}

	/**
	 * Passes the focus to the view
	 */
	@Override
	public void setFocus() {
		form.setFocus();
	}

	@Override
	public void dataChanged(Message<ServiceType> message, MessageIoSession messageIoSession) {
		viewer.refresh();

	}

	/**
	 * Creates and adds the actions for the toolbar
	 */
	private void createToolBarActions() {
		GetMessage<ServiceType> getMessage = new GetMessage<ServiceType>(new ServiceType());
		// create the action
		EditorNewServiceTypeAction addAction = new EditorNewServiceTypeAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		RefreshViewAction<ServiceType> refreshView = new RefreshViewAction<ServiceType>(getMessage);
		// add to the toolbar
		form.getToolBarManager().add(addAction);
		form.getToolBarManager().add(refreshView);
		form.getToolBarManager().update(true);
	}
}
