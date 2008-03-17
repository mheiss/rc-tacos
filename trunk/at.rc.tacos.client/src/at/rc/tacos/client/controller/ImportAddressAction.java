package at.rc.tacos.client.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;
import at.rc.tacos.client.Activator;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.factory.CSVParser;
import at.rc.tacos.model.Address;

/**
 * Import action to load the new street names
 * @author Michael
 */
public class ImportAddressAction extends Action
{
	private IWorkbenchWindow window;

	/**
	 * Default class constructor for creating the editor
	 */
	public ImportAddressAction(IWorkbenchWindow window)
	{
		this.window = window;
	}

	/**
	 * Returns the tooltip text for the action
	 * @return the tooltip text
	 */
	@Override
	public String getToolTipText() 
	{
		return "Importiert die Adressen von der gew‰hlten Datei";
	}

	/**
	 * Retruns the text to show in the toolbar
	 * @return the text to render
	 */
	@Override
	public String getText()
	{
		return "Adressen importieren";
	}

	/**
	 * Returns the image to use for this action.
	 * @return the image to use
	 */
	@Override
	public ImageDescriptor getImageDescriptor() 
	{
		return ImageFactory.getInstance().getRegisteredImageDescriptor("resource.import");
	}

	/**
	 * Opens a dialog to choose the file and then imports the content
	 */
	@Override
	public void run()
	{
		FileDialog fileDialog = new FileDialog(window.getShell());
		fileDialog.setText("Straﬂen Importieren.");
		String[] filterExt = { "*.csv" };
		fileDialog.setFilterExtensions(filterExt);
		final String path = fileDialog.open();

		//assert valid
		if(path == null)
			return;

		//create a new instance and parse the file
		try
		{
			//parse the given file
			final List<Map<String, Object>> elementList = CSVParser.getInstance().parseCSV(new File(path));

			//ask again ;)
			boolean result = MessageDialog.openConfirm(window.getShell(), 
					"Straﬂen importieren", 
					"Wollen Sie wirklich die Straﬂen importieren? ("+elementList.size()+" Eintr‰ge)\n"+
			"Alle bisherigen Eintr‰ge werden ¸berschrieben!");
			if(!result)
				return;

			//send a request to delete the current files
			NetWrapper.getDefault().requestListing(Address.ID, null);

			//Start a new job
			final Job job = new Job("AddressMonitor") 
			{
				@Override
				protected IStatus run(IProgressMonitor monitor)
				{
					try
					{
						List<AbstractMessage> addressList = new ArrayList<AbstractMessage>();
						//loop an import
						for(int i = 0; i<elementList.size(); i++)
						{
							Map<String, Object> line = elementList.get(i);			
							//access every element of the line			
							int gkz = Integer.parseInt((String)line.get("GKZ"));
							String city = (String)line.get("Gemeindename");
							String street = (String)line.get("BEZEICHNUNG");
							//create the address and add to the list
							addressList.add(new Address(gkz,city,street));

							//only add 200 address items at time
							if(i%200 == 0)
							{
								//send the request to add
								NetWrapper.getDefault().sendAddAllMessage(Address.ID, addressList);
								addressList.clear();
							}

						}
						return Status.OK_STATUS;
					}
					catch(Exception e)
					{
						Activator.getDefault().log("Failed to parse the given csv file :"+path, IStatus.ERROR);
						return Status.CANCEL_STATUS;
					}
				}
			};
			job.addJobChangeListener(new JobChangeAdapter() 
			{
				@Override
				public void done(IJobChangeEvent event) 
				{
					if (!event.getResult().isOK())
						Activator.getDefault().log("Failed to import the addresses",IStatus.ERROR);
				}
			});
			job.setSystem(true);
			// start immediate
			job.schedule(); 
		}
		catch(Exception e)
		{
			Activator.getDefault().log("Failed to parse the given csv file :"+path, IStatus.ERROR);
			e.printStackTrace();
		}
	}
}
