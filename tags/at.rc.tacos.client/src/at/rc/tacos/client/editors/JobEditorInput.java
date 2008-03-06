package at.rc.tacos.client.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import at.rc.tacos.model.Job;

public class JobEditorInput implements IEditorInput
{
	//properties
	private Job job;
	private boolean isNew;

	/**
	 * Default class constructor for the job editor.
	 * @param job the job to edit
	 * @param isNew a flag to determine whether the job is new
	 */
	public JobEditorInput(Job job,boolean isNew)
	{
		this.job = job;
		this.isNew = isNew;
	}

	@Override
	public boolean exists()
	{
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor()
	{
		return null;
	}

	@Override
	public String getName()
	{
		if(isNew)
			return "Neue Verwendung";
		return job.getJobName();
	}

	@Override
	public IPersistableElement getPersistable()
	{
		return null;
	}

	@Override
	public String getToolTipText()
	{
		return "Verwendung: " + job.getJobName();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object getAdapter(Class arg0)
	{
		return null;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof JobEditorInput)
		{
			JobEditorInput otherEditor = (JobEditorInput)other;
			return job.equals(otherEditor.getJob());
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return job.hashCode();
	}

	/**
	 * Returns the job managed by the editor
	 * @return the managed job
	 */
	public Job getJob()
	{
		return job;
	}

	/**
	 * Returns whether or not the job is new.
	 * @return true if the job is created new
	 */
	public boolean isNew()
	{
		return isNew;
	}
}
