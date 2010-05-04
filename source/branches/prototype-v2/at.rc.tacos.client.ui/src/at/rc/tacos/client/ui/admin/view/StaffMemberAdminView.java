package at.rc.tacos.client.ui.admin.view;

import org.eclipse.jface.dialogs.MessageDialog;
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
import at.rc.tacos.client.net.handler.LoginHandler;
import at.rc.tacos.client.net.handler.StaffHandler;
import at.rc.tacos.client.ui.admin.editors.StaffMemberEditor;
import at.rc.tacos.client.ui.admin.editors.StaffMemberEditorInput;
import at.rc.tacos.client.ui.controller.EditorNewStaffAction;
import at.rc.tacos.client.ui.controller.RefreshViewAction;
import at.rc.tacos.client.ui.providers.HandlerContentProvider;
import at.rc.tacos.client.ui.providers.StaffMemberLabelProvider;
import at.rc.tacos.platform.model.Login;
import at.rc.tacos.platform.model.StaffMember;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.listeners.DataChangeListener;
import at.rc.tacos.platform.net.message.GetMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;

public class StaffMemberAdminView extends ViewPart implements DataChangeListener<StaffMember> {

	public static final String ID = "at.rc.tacos.client.view.admin.staffMemberAdminView";

	private Logger log = LoggerFactory.getLogger(StaffMemberAdminView.class);

	// properties
	private TableViewer viewer;
	private FormToolkit toolkit;
	private ScrolledForm form;

	// the data source
	private StaffHandler staffHandler = (StaffHandler) NetWrapper.getHandler(StaffMember.class);
	private LoginHandler loginHandler = (LoginHandler) NetWrapper.getHandler(Login.class);

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		toolkit = new FormToolkit(Display.getDefault());
		form = toolkit.createScrolledForm(parent);
		form.setText("Liste der Mitarbeiter");
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
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				StaffMember member = (StaffMember) obj;
				Login login = loginHandler.getLoginByUsername(member.getUserName());
				// assert valid
				if (login == null) {
					log.error("Failed to open the editor for the staff member " + member + "\n The login object for the user is null");
					MessageDialog.openError(getSite().getShell(), "Fehler beim ändern von " + member.getUserName(), "Der Mitarbeiter"
							+ member.getFirstName() + " " + member.getLastName() + " kann nicht editiert werden");
					return;
				}

				StaffMemberEditorInput input = new StaffMemberEditorInput(member, login, false);
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					page.openEditor(input, StaffMemberEditor.ID);
				}
				catch (PartInitException e) {
					log.error("Failed to open the editor for the staff member " + member, e);
				}
			}
		});
		viewer.setContentProvider(new HandlerContentProvider());
		viewer.setLabelProvider(new StaffMemberLabelProvider());
		viewer.setInput(staffHandler);
		getViewSite().setSelectionProvider(viewer);

		// add actions to the toolbar
		createToolBarActions();

		// set this table as a selection provider
		getViewSite().setSelectionProvider(viewer);

		// register the listener
		NetWrapper.registerListener(this, StaffMember.class);
	}

	@Override
	public void dispose() {
		NetWrapper.removeListener(this, StaffMember.class);
	}

	/**
	 * Passes the focus to the view
	 */
	@Override
	public void setFocus() {
		form.setFocus();
	}

	@Override
	public void dataChanged(Message<StaffMember> message, MessageIoSession messageIoSession) {
		viewer.refresh();
	}

	/**
	 * Creates and adds the actions for the toolbar
	 */
	private void createToolBarActions() {
		GetMessage<StaffMember> getMessage = new GetMessage<StaffMember>(new StaffMember());
		// create the action
		EditorNewStaffAction addAction = new EditorNewStaffAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		RefreshViewAction<StaffMember> refreshView = new RefreshViewAction<StaffMember>(getMessage);
		// add to the toolbar
		form.getToolBarManager().add(addAction);
		form.getToolBarManager().add(refreshView);
		form.getToolBarManager().update(true);
	}
}
