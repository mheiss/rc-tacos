package at.rc.tacos.client.providers;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import at.rc.tacos.model.Transport;

public class TransportViewFilter  extends ViewerFilter
{
	//the criteria to filter
	private String from;
	private String patient;
	private String to;
	private String location;

	/**
	 * Default class constructor for the address filter.
	 * @param value the street or the city to filter
	 */
	public TransportViewFilter(String from,String patient,String to, String location)
	{
		this.from = from;
		this.patient = patient;
		this.to = to;
		this.location = location;
	}

	/**
	 * Returns whether or not the object should be filtered or not.
	 * @param viewer the viewer
	 * @param parentElement the parent element
	 * @param element the element to check
	 */
	@Override
	public boolean select(Viewer arg0, Object parentElement, Object element) 
	{
		//cast to a transport
		Transport transport = (Transport)element;

		//check the from field
		if(from != null &! from.trim().isEmpty())
		{
			//check the street name
			if(!transport.getFromCity().contains(from) &! transport.getFromCity().startsWith(from) 
					&!transport.getFromStreet().contains(from) &! transport.getFromStreet().startsWith(from))
				return false;
		}
		//check the patient
		if(patient != null &! patient.trim().isEmpty())
		{
			//check the patient last name
			if(!transport.getPatient().getLastname().contains(patient) &! transport.getPatient().getLastname().startsWith(patient)
					&!transport.getPatient().getFirstname().contains(patient) &! transport.getPatient().getFirstname().startsWith(patient))
				return false;
		}
		if(to != null &! to.trim().isEmpty())
		{
			if(!transport.getToStreet().contains(to) &! transport.getToStreet().startsWith(to)
					&!transport.getToCity().contains(to) &! transport.getToCity().startsWith(to))
				return false;
		}
		if(location != null &! location.trim().isEmpty())
		{
			if(!transport.getPlanedLocation().getLocationName().contains(location) &! transport.getPlanedLocation().getLocationName().startsWith(location))
			return false;
		}
		//nothing matched
		return true;
	}
}
