package at.rc.tacos.client.providers;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import at.rc.tacos.model.Address;
import at.rc.tacos.model.Transport;

public class PrebookingViewFilter  extends ViewerFilter
{
	//the criteria to filter
	private String from;
	private String patient;
	private String to;
	
	/**
	 * Default class constructor for the address filter.
	 * @param value the street or the city to filter
	 */
	public PrebookingViewFilter(String from,String patient,String to)
	{
		this.from = from;
		this.patient = patient;
		this.to = to;
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
		//cast to a address
//		Address address = (Address)element;
		//cast to a transport
		Transport transport = (Transport)element;
		
		//check the from field
		if(from != null &! from.trim().isEmpty())
		{
			//check the street name
			if(!transport.getFromCity().contains(from) &! transport.getFromStreet().startsWith(from))
					return false;
		}
		//check the patient
		if(patient != null &! patient.trim().isEmpty())
		{
			//check the patient last name
			if(!transport.getPatient().getLastname().contains(patient) &! transport.getPatient().getLastname().startsWith(patient))
				return false;
		}
		if(to != null &! to.trim().isEmpty())
		{
//			//convert to string
//			String addNumber = String.valueOf(address.getZip());
			if(!transport.getToStreet().contains(to) &! transport.getToStreet().startsWith(to))
				return false;
		}
		//nothing matched
		return true;
	}
}
