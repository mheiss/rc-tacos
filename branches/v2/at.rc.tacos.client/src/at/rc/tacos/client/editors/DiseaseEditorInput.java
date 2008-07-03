package at.rc.tacos.client.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import at.rc.tacos.model.Disease;

public class DiseaseEditorInput  implements IEditorInput
{
	//properties
	private Disease disease;
	private boolean isNew;
	
	/**
	 * Default class constructor for a disease editor
	 * @param disease the diesease to edit
	 * @param isNew flag to treat this entry as new
	 */
	public DiseaseEditorInput(Disease disease,boolean isNew)
	{
		this.disease = disease;
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
			return "Neue Erkrankung";
		return disease.getDiseaseName();
	}
	
	@Override
	public IPersistableElement getPersistable()
	{
		return null;
	}
	
	@Override
	public String getToolTipText()
	{
		return "Erkrankung: " + disease.getDiseaseName();
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
		if(other instanceof DiseaseEditorInput)
		{
			DiseaseEditorInput otherEditor = (DiseaseEditorInput)other;
			return disease.equals(otherEditor.getDisease());
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return disease.hashCode();
	}
	
	/**
	 * Returns the disease managed by the editor
	 * @return the managed disease
	 */
	public Disease getDisease()
	{
		return disease;
	}

	/**
	 * Returns whether or not the disease is new.
	 * @return true if the disease is created new
	 */
	public boolean isNew()
	{
		return isNew;
	}
}
