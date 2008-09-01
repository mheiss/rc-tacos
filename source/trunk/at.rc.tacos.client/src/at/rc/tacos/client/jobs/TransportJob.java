package at.rc.tacos.client.jobs;

import java.util.Calendar;
import java.util.Random;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import at.rc.tacos.client.Activator;
import at.rc.tacos.client.controller.CreateBackTransportFromDialysis;
import at.rc.tacos.client.controller.CreateTransportFromDialysis;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.platform.iface.IProgramStatus;
import at.rc.tacos.platform.model.DialysisPatient;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.util.MyUtils;

/**
 * Starts a background thread that checks every minutes the status of the transports.
 * When a prebooked transport is within the next 2 hours then the thread updates the 
 * transport and moves it to the outstanding transports by changing the status and 
 * sending a update request
 */
public class TransportJob extends Job
{
	//properties to set the next start time
	final Random rand = new Random(Calendar.getInstance().getTimeInMillis());
	
	/**
	 * Default class constructor
	 */
	public TransportJob()
	{
		super("transportJob");
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) 
	{
		try 
		{
			//the current time minus 2 hours
			Calendar current = Calendar.getInstance();
			current.add(Calendar.HOUR_OF_DAY, +2);
			//check the transports
			for(Transport transport:ModelFactory.getInstance().getTransportManager().getTransportList())
			{
				//check the status
				if(transport.getProgramStatus() != IProgramStatus.PROGRAM_STATUS_PREBOOKING)
					continue;
				//check the time
				if(current.getTimeInMillis() > transport.getPlannedStartOfTransport())
				{	
					transport.setProgramStatus(IProgramStatus.PROGRAM_STATUS_OUTSTANDING);
					NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
					Activator.getDefault().log("Automatically moved the transport "+ transport+" to the outstanding transports",IStatus.INFO);
				}
			}
			
			//check the (to) dialysis patients
			for(DialysisPatient patient:ModelFactory.getInstance().getDialyseManager().getDialysisList())
			{
				
				Calendar currentDialysis = Calendar.getInstance();
				//first check: do we have already generated a transport for today?
				if(MyUtils.isEqualDate(patient.getLastTransportDate(),currentDialysis.getTimeInMillis()))
					continue;

				//after the date check we can add 2 hours
				currentDialysis.add(Calendar.HOUR_OF_DAY, +2);
				//second check: is the day correct?
				int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
				switch(day)
				{
				case Calendar.MONDAY: 
					if(!patient.isMonday())
						continue;
					break;
				case Calendar.TUESDAY:
					if(!patient.isTuesday())
						continue;
					break;
				case Calendar.WEDNESDAY:
					if(!patient.isWednesday())
						continue;
					break;
				case Calendar.THURSDAY:
					if(!patient.isThursday())
						continue;
					break;
				case Calendar.FRIDAY:
					if(!patient.isFriday())
						continue;
					break;
				case Calendar.SATURDAY:
					if(!patient.isSaturday())
						continue;
					break;
				case Calendar.SUNDAY:
					if(!patient.isSunday())
						continue;
					break;
				default:
					continue;
				}
				
				//construct a calendar object with the start time (HH:mm)
				Calendar patientCal = Calendar.getInstance();
				patientCal.setTimeInMillis(patient.getPlannedStartOfTransport());
				//now add the current year,month and day
				patientCal.set(Calendar.YEAR, currentDialysis.get(Calendar.YEAR));
				patientCal.set(Calendar.MONTH, currentDialysis.get(Calendar.MONTH));
				patientCal.set(Calendar.DAY_OF_MONTH, currentDialysis.get(Calendar.DAY_OF_MONTH));

				//third check: is within the next two hour?
				if(currentDialysis.getTimeInMillis() > patientCal.getTimeInMillis())
				{
					//set the last generated transport date to now
					if(!patient.isStationary())
					{
						patient.setLastTransportDate(Calendar.getInstance().getTimeInMillis());
						NetWrapper.getDefault().sendUpdateMessage(DialysisPatient.ID, patient);
						//create and run the action
						CreateTransportFromDialysis createAction = new CreateTransportFromDialysis(patient,currentDialysis);
						createAction.run();
					}
				}
			}
			
			//check the (from) dialysis patients
			for(DialysisPatient patient:ModelFactory.getInstance().getDialyseManager().getDialysisList())
			{
				Calendar currentDialysis = Calendar.getInstance();
				//first check: do we have already generated a back transport for today?
				if(MyUtils.isEqualDate(patient.getLastBackTransporDate(),currentDialysis.getTimeInMillis()))
					continue;

				//after the date check we can add 2 hours
				currentDialysis.add(Calendar.HOUR_OF_DAY, +2);
				//second check: is the day correct?
				int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
				switch(day)
				{
				case Calendar.MONDAY: 
					if(!patient.isMonday())
						continue;
					break;
				case Calendar.TUESDAY:
					if(!patient.isTuesday())
						continue;
					break;
				case Calendar.WEDNESDAY:
					if(!patient.isWednesday())
						continue;
					break;
				case Calendar.THURSDAY:
					if(!patient.isThursday())
						continue;
					break;
				case Calendar.FRIDAY:
					if(!patient.isFriday())
						continue;
					break;
				case Calendar.SATURDAY:
					if(!patient.isSaturday())
						continue;
					break;
				case Calendar.SUNDAY:
					if(!patient.isSunday())
						continue;
					break;
				default:
					continue;
				}
				//construct a calendar object with the start time (HH:mm)
				Calendar patientCal = Calendar.getInstance();
				patientCal.setTimeInMillis(patient.getPlannedStartForBackTransport());
				//now add the current year,month and day
				patientCal.set(Calendar.YEAR, currentDialysis.get(Calendar.YEAR));
				patientCal.set(Calendar.MONTH, currentDialysis.get(Calendar.MONTH));
				patientCal.set(Calendar.DAY_OF_MONTH, currentDialysis.get(Calendar.DAY_OF_MONTH));

				//third check: is within the next two hour?
				if(currentDialysis.getTimeInMillis() > patientCal.getTimeInMillis())
				{
					if(!patient.isStationary())
					{
						//set the last generated transport date to now
						patient.setLastBackTransportDate(Calendar.getInstance().getTimeInMillis());
						NetWrapper.getDefault().sendUpdateMessage(DialysisPatient.ID, patient);
						CreateBackTransportFromDialysis createAction = new CreateBackTransportFromDialysis(patient, currentDialysis);
						createAction.run();
					}
				}
			}
			return Status.OK_STATUS;
		} 
		finally 
		{
			// start again in a minute plus a random time so that different clients use different times
			schedule(60000+rand.nextInt(30000)); 
		}
	}
}
