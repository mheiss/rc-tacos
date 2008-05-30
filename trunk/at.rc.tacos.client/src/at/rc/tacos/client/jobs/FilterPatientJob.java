package at.rc.tacos.client.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Display;

import at.rc.tacos.client.providers.PatientViewFilter;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.SickPerson;

public class FilterPatientJob extends Job
{
	//the properties
	private String searchString;
	private TableViewer viewer;
	
	/**
	 * The time in milliseconds between two keystrokes
	 */
	public static final int INTERVAL_KEY_PRESSED = 1500;
	
	/**
	 * Default class construcotor
	 * @param viewer the viewer to update and apply the filter
	 */
	public FilterPatientJob(TableViewer viewer)
	{
		super("filterPatientJob");
		this.viewer = viewer;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor)
	{		
		//assert valid text
		if(searchString.length() < 1)
			return Status.OK_STATUS;
		
		//send a request to the server to list all matching patients
		NetWrapper.getDefault().requestListing(SickPerson.ID,new QueryFilter(IFilterTypes.SEARCH_STRING,searchString));
		
		//apply the filter
		Display.getDefault().asyncExec(new Runnable ()    
		{
			public void run ()       
			{
				//get the values and create the filter
				viewer.resetFilters();
				//create new filter and apply
				PatientViewFilter filter = new PatientViewFilter(searchString);
				viewer.addFilter(filter);
			}
		});
		
		return Status.OK_STATUS;
	}

	/**
	 * @param searchString the searchString to set
	 */
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
}