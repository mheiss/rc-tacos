package at.rc.tacos.client.ui.providers;

import org.eclipse.jface.viewers.LabelProvider;

import at.rc.tacos.platform.model.Job;

public class JobLabelProvider extends LabelProvider {

	/**
	 * Returns the name of the job to render
	 */
	@Override
	public String getText(Object object) {
		Job job = (Job) object;
		return job.getJobName();
	}
}
