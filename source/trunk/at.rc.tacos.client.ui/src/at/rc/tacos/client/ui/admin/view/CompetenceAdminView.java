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
import at.rc.tacos.client.net.handler.CompetenceHandler;
import at.rc.tacos.client.ui.admin.editors.CompetenceEditor;
import at.rc.tacos.client.ui.admin.editors.CompetenceEditorInput;
import at.rc.tacos.client.ui.controller.EditorNewCompetenceAction;
import at.rc.tacos.client.ui.controller.RefreshViewAction;
import at.rc.tacos.client.ui.providers.CompetenceLabelProvider;
import at.rc.tacos.client.ui.utils.CustomColors;
import at.rc.tacos.platform.model.Competence;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.listeners.DataChangeListener;
import at.rc.tacos.platform.net.message.GetMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;

public class CompetenceAdminView extends ViewPart implements DataChangeListener<Competence> {

	public static final String ID = "at.rc.tacos.client.view.admin.competenceAdminView";

	private Logger log = LoggerFactory.getLogger(CompetenceAdminView.class);

	// properties
	private TableViewer viewer;
	private FormToolkit toolkit;
	private ScrolledForm form;

	// the data source
	private CompetenceHandler competenceHandler = (CompetenceHandler) NetWrapper.getHandler(Competence.class);

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(final Composite parent) {

		// the scrolled form
		toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
		form = toolkit.createScrolledForm(parent);
		form.setText("Liste der Kompetenzen");
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
				// get the selected competence
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				Competence competence = (Competence) obj;
				// create the editor input and open
				CompetenceEditorInput input = new CompetenceEditorInput(competence, false);
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					page.openEditor(input, CompetenceEditor.ID);
				}
				catch (PartInitException e) {
					log.error("Failed to open the editor for the competence " + competence, e);
				}
			}
		});
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new CompetenceLabelProvider());
		viewer.setInput(competenceHandler.toArray());
		getViewSite().setSelectionProvider(viewer);

		// add actions to the toolbar
		createToolBarActions();

		// set this table as a selection provider
		getViewSite().setSelectionProvider(viewer);

		// disable if we do not have an admin
		String authorization = NetWrapper.getSession().getLogin().getAuthorization();
		if (!authorization.equalsIgnoreCase("Administrator"))
			form.setEnabled(false);

		// register the listeners
		NetWrapper.registerListener(this, Competence.class);
	}

	@Override
	public void dispose() {
		NetWrapper.removeListener(this, Competence.class);
	}

	/**
	 * Passes the focus to the view
	 */
	@Override
	public void setFocus() {
		form.setFocus();
	}

	@Override
	public void dataChanged(Message<Competence> message, MessageIoSession messageIoSession) {
		viewer.refresh();
	}

	/**
	 * Creates and adds the actions for the toolbar
	 */
	private void createToolBarActions() {
		// create the action
		EditorNewCompetenceAction addAction = new EditorNewCompetenceAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		GetMessage<Competence> getMessage = new GetMessage<Competence>(new Competence());
		RefreshViewAction<Competence> viewAction = new RefreshViewAction<Competence>(getMessage);
		// add to the toolbar
		form.getToolBarManager().add(addAction);
		form.getToolBarManager().add(viewAction);
		form.getToolBarManager().update(true);
	}
}
