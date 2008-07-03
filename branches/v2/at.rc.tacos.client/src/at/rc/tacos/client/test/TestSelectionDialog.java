package at.rc.tacos.client.test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SelectionStatusDialog;

import at.rc.tacos.client.Activator;
import at.rc.tacos.client.jobs.FilterAddressJob;
import at.rc.tacos.client.modelManager.ModelFactory;


public class TestSelectionDialog  extends SelectionStatusDialog implements PropertyChangeListener
{
	//the input and the viewer to show the result
	private Text filterText;
	private TableViewer viewer;
	private AutoCompleteField acInputText;
	
	/**
	 * The scheduler job to start the filter
	 */
	private FilterAddressJob filterJob;

	/**
	 * Defaul class constructor to set up a new patient select dialog
	 * @param parent
	 */
	public TestSelectionDialog(final Shell parent) 
	{
		super(parent);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		//listen to sick person updates
		ModelFactory.getInstance().getAddressManager().addPropertyChangeListener(this);
	}
	
	@Override
	public boolean close() 
	{
		//cleanup the listeners
		ModelFactory.getInstance().getAddressManager().removePropertyChangeListener(this);
		//colse the dialog
		return super.close();
	}

	@Override
    protected void configureShell(final Shell shell)
    {
        shell.setText("TestSuche"); 
        super.configureShell(shell);
    }

	@Override
	protected Control createDialogArea(final Composite parent) 
	{
		final Composite area = (Composite) super.createDialogArea(parent);

		final Label message = new Label(area, SWT.NONE);
		message.setText("&Testeingabe:\n");
		filterText = new Text(area, SWT.SINGLE | SWT.BORDER);
		filterText.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
		filterText.addModifyListener(new ModifyListener() 
		{
			public void modifyText(final ModifyEvent e) 
			{
				inputChanged();
			}
		});
		acInputText = new AutoCompleteField(filterText, new TextContentAdapter(), new String[] { });
		
		final Label matches = new Label(area, SWT.NONE);
		matches.setText("&Gefundene Einträge:"); 
		
		Table table = new Table(area, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		viewer = new TableViewer(table);
		final Control control = this.viewer.getControl();final GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		control.setLayoutData(gd);
		gd.widthHint = 400;
		gd.heightHint = 200;
        
		viewer.getTable().setLinesVisible(true);
		viewer.getTable().setHeaderVisible(true);
		viewer.setLabelProvider(new LabelProvider());
		viewer.setContentProvider(new IStructuredContentProvider() 
		{
			@Override
			public Object[] getElements(Object arg0) 
			{
				return ModelFactory.getInstance().getAddressManager().toCityArray();
			}

			@Override
			public void dispose() { }

			@Override
			public void inputChanged(Viewer arg0, Object arg1, Object arg2) { }
		});
		viewer.setInput(ModelFactory.getInstance().getAddressManager().toStreetArray());

        setStatusLineAboveButtons(true);
        
		return area;
	}

	@Override
	protected void computeResult() 
	{
		//not needed
	}

	/**
	 * This listener will be informed when the server sends new patients based on the entered text
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) 
	{
		//listen to address events
		String event = evt.getPropertyName();
		if("ADDRESS_ADD".equalsIgnoreCase(event) ||
				"ADDRESS_REMOVE".equalsIgnoreCase(event) ||
				"ADDRESS_UPDATE".equalsIgnoreCase(event) ||
				"ADDRESS_CLEARED".equalsIgnoreCase(event) ||
				"ADDRESS_ADD_ALL".equalsIgnoreCase(event))
		{
			viewer.refresh();
			acInputText.setProposals(ModelFactory.getInstance().getAddressManager().toStreetArray());
		}
	}
	
	//PRIVATE METHODS
	/**
	 * Called when the input text of a filter is changes
	 */
	private void inputChanged()
	{
		//get the entered text
		String filterValue = filterText.getText().toLowerCase();
		if(filterValue.trim().length() < 1)
		{
			updateStatus(new Status(Status.WARNING,Activator.PLUGIN_ID,"Bitte geben sie mindestens ein Zeiche ein"));
			Display.getCurrent().beep();
			return;
		}
		
		if(filterJob == null)
			filterJob = new FilterAddressJob(null);
		
		//check the state
		if(filterJob.getState() == Job.RUNNING)
		{
			System.out.println("Job is currently running");
			return;
		}
		
		//pass the entered text
		filterJob.setStrCity("");
		filterJob.setStrStreet(filterValue);
		filterJob.setStrZip("");
		filterJob.schedule(0);
	}
}
