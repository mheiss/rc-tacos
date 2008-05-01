package at.rc.tacos.core.net.jobs;

import org.eclipse.core.runtime.jobs.ISchedulingRule;

/**
 * This job rule is for the process data job and enshures that only one job for each model type is running at time. 
 * @author Michael
 */
public class ProcessDataJobRule implements ISchedulingRule
{
	//the content type for the rule
	private String contentType;

	/**
	 * Default class constructor defining the content type of the data which will be processed by the job
	 */
	public ProcessDataJobRule(String contentType)
	{
		this.contentType = contentType;
	}

	@Override
	public boolean contains(ISchedulingRule otherRule) 
	{	
		if(contentType == "")
			contentType = ".";
		
		return otherRule.getClass() == ProcessDataJobRule.class;
		
//		//check the rule class
//		if(otherRule instanceof ProcessDataJobRule)
//		{
//			//get the other rule
//			ProcessDataJobRule rule = (ProcessDataJobRule)otherRule;
//			//check if the are processing the same content type
//			if(rule.contentType.equalsIgnoreCase(contentType))
//				return true;
//			return false;
//		}
//		//other rule so they can run at the same time
//		return false;
	}

	@Override
	public boolean isConflicting(ISchedulingRule otherRule) 
	{
		return otherRule.getClass() == ProcessDataJobRule.class;
		
//		//check the rule class
//		if(otherRule instanceof ProcessDataJobRule)
//		{
//			//get the other rule
//			ProcessDataJobRule rule = (ProcessDataJobRule)otherRule;
//			//check if the are processing the same content type
//			if(rule.contentType.equalsIgnoreCase(contentType))
//				return true;
//			return false;
//		}
//		//other rule so they can run at the same time
//		return false;
	}
}
