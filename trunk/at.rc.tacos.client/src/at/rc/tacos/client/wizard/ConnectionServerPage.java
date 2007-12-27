package at.rc.tacos.client.wizard;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import at.rc.tacos.client.providers.ConnectionServerContentProvider;
import at.rc.tacos.client.providers.ConnectionServerLabelProvider;
import at.rc.tacos.core.net.NetSource;
import at.rc.tacos.core.net.internal.ServerInfo;

public class ConnectionServerPage extends WizardPage 
{
	//properties
	private Composite container;
	private TableViewer viewer;
	//the controller wizard
	private ConnectionWizard wizard;
	
	/**
	 * Default constructor
	 */
	public ConnectionServerPage(ConnectionWizard wizard)
	{
		super("");
		setWizard(wizard);
		setTitle("Wählen Sie einen Server aus");
		setDescription("Sie können eine neue Verbindung zu einem der unten angeführten Servern aufbauen");
		//save the wizard
		this.wizard = wizard;
	}
	
	/**
	 * Callback method to create the page content and initialize it
	 */
	@Override
	public void createControl(Composite parent) 
	{
		container = new Composite(parent, SWT.NULL);
		FillLayout layout = new FillLayout();
		container.setLayout(layout);
		//create the table, set the providers and the input
		viewer = new TableViewer(container, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ConnectionServerContentProvider());
		viewer.setLabelProvider(new ConnectionServerLabelProvider());
		viewer.setInput(NetSource.getInstance().getServerList());
		//change listener
		viewer.addSelectionChangedListener(new ISelectionChangedListener() 
		{
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				if (selection.getFirstElement() instanceof ServerInfo) 
				{
					wizard.setNewServer((ServerInfo)selection.getFirstElement());
					setPageComplete(true);
					wizard.getContainer().updateButtons();
				}
			}
		});

		// Required to avoid an error in the system
		setControl(container);
		setPageComplete(false);	
	}
	

	@Override
	public boolean canFlipToNextPage() 
	{
		return wizard.getNewServer() != null;
	}
	
	/**
	 * Returns the top widget of the application.
	 * @return the top widget
	 */
	@Override
	public Control getControl() 
	{
		return container;
	}
}
