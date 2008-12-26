package at.rc.tacos.client.ui.controller;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.platform.model.Address;
import at.rc.tacos.platform.net.message.AddMessage;
import at.rc.tacos.platform.util.CsvParser;

/**
 * Import action to load the new street names Is only once used ad system
 * implementation phase
 * 
 * @author Michael
 */
public class ImportAddressAction extends Action {

	private Logger log = LoggerFactory.getLogger(ImportAddressAction.class);

	private IWorkbenchWindow window;

	/**
	 * Default class constructor for creating the editor
	 */
	public ImportAddressAction(IWorkbenchWindow window) {
		this.window = window;
	}

	/**
	 * Returns the tooltip text for the action
	 * 
	 * @return the tooltip text
	 */
	@Override
	public String getToolTipText() {
		return "Importiert die Adressen von der gewählten Datei";
	}

	/**
	 * Retruns the text to show in the toolbar
	 * 
	 * @return the text to render
	 */
	@Override
	public String getText() {
		return "Adressen importieren";
	}

	/**
	 * Returns the image to use for this action.
	 * 
	 * @return the image to use
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return UiWrapper.getDefault().getImageRegistry().getDescriptor("resource.import");
	}

	/**
	 * Opens a dialog to choose the file and then imports the content
	 */
	@Override
	public void run() {
		FileDialog fileDialog = new FileDialog(window.getShell());
		fileDialog.setText("Straßen Importieren.");
		String[] filterExt = { "*.csv" };
		fileDialog.setFilterExtensions(filterExt);
		final String path = fileDialog.open();

		// assert valid
		if (path == null)
			return;

		// create a new instance and parse the file
		try {
			// parse the given file
			final List<Map<String, Object>> elementList = CsvParser.getInstance().parseCSV(new File(path));

			// ask again ;)
			boolean result = MessageDialog.openConfirm(window.getShell(), "Straßen importieren", "Möchten Sie wirklich die Adressen importieren? ("
					+ elementList.size() + " Einträge)");
			if (!result)
				return;

			// Start a new job
			final Job job = new Job("AddressMonitor") {

				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						List<Address> addressList = new ArrayList<Address>();
						monitor.beginTask("Importiere die Adressdaten", elementList.size());
						// loop an import
						for (int i = 0; i < elementList.size(); i++) {
							Map<String, Object> line = elementList.get(i);

							// access every element of the line
							int gkz = Integer.parseInt((String) line.get("GKZ"));
							String city = (String) line.get("Gemeindename");
							String street = (String) line.get("BEZEICHNUNG");
							Address newAddress = new Address(gkz, city, street);
							monitor.setTaskName("Importiere Datensatz #" + i + " (" + newAddress + ")");

							// add to the list
							addressList.add(newAddress);

							// commit 100 entries at one time
							if (addressList.size() > 100) {
								monitor.setTaskName("Sende Daten an Server");
								// send the data to the server
								AddMessage<Address> addMessage = new AddMessage<Address>(addressList);
								addMessage.asnchronRequest(NetWrapper.getSession());
								addressList.clear();
							}
						}
						// commit the remaining entries
						if (!addressList.isEmpty()) {
							monitor.setTaskName("Sende Daten an Server");
							AddMessage<Address> addMessage = new AddMessage<Address>(addressList);
							addMessage.asnchronRequest(NetWrapper.getSession());
						}
						return Status.OK_STATUS;
					}
					catch (Exception e) {
						log.error("Failed to parse the given csv file :" + path, e);
						return Status.CANCEL_STATUS;
					}
					finally {
						monitor.done();
					}
				}
			};
			job.addJobChangeListener(new JobChangeAdapter() {

				@Override
				public void done(IJobChangeEvent event) {
					if (!event.getResult().isOK()) {
						log.error("Failed to import the addresses");
					}
				}
			});
			job.setUser(true);
			job.setSystem(false);
			// start immediate
			job.schedule();
		}
		catch (Exception e) {
			log.error("Failed to parse the given csv file :" + path, e);
		}
	}
}
