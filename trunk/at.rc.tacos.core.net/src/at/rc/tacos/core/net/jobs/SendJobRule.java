package at.rc.tacos.core.net.jobs;

import org.eclipse.core.runtime.jobs.ISchedulingRule;

/**
 * This rule is for the send job so that the internal job manager ensures that only one job
 * of this type is running at the same time.
 * @author Michael
 */
public class SendJobRule implements ISchedulingRule
{
	@Override
	public boolean contains(ISchedulingRule otherRule) 
	{
		 return otherRule.getClass() == SendJobRule.class;
	}

	@Override
	public boolean isConflicting(ISchedulingRule otherRule) 
	{
		return otherRule.getClass() == SendJobRule.class;
	}
}
