package at.rc.tacos.platform.model;

/**
 * Stores information about a specifiy period
 * @author Birgit
 */
public class Period extends AbstractMessage
{
	//the id
	public final static String ID = "period";
	
	//properties
	private int periodId;
	private String period;
	private String serviceTypeCompetence;

	/**
	 * Default class constructor
	 */
	public Period()
	{
		super(ID);
		period = "";
		serviceTypeCompetence = "";
	}
	
	/**
	 * Default class constructor for a complete period object.
	 * @param zip the zip code
	 * @param period the name of the period
	 * @param serviceTypeCompetence the name of the serviceTypeCompetence
	 */
	public Period(int zip,String period,String serviceTypeCompetence)
	{
		this();
		this.period = period;
		this.serviceTypeCompetence = serviceTypeCompetence;
	}
	
	/**
	 * Returns the string based description 
	 * @return the human readable version
	 */
	@Override
	public String toString()
	{
	 return "period: " +period+";" +"serviceTypeCompetence: " +serviceTypeCompetence;
			
	}

	/**
	 * Returns the calculated hash code based on the complete period<br>
	 * Two periodes have the same hash code if all fields are the same.
	 * @return the calculated hash code
	 */
	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((period == null) ? 0 : period.hashCode());
		result = prime * result + ((serviceTypeCompetence == null) ? 0 : serviceTypeCompetence.hashCode());
		return result;
	}

	/**
	 * Returns whether the objects are equal or not.<br>
	 * Two periodes are equal if all propertie fields are the same.
	 * @return true if all fields are the same otherwise false.
	 */
	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Period other = (Period) obj;
		if (period == null) 
		{
			if (other.period != null)
				return false;
		} 
		else if (!period.equals(other.period))
			return false;
		if (serviceTypeCompetence == null) 
		{
			if (other.serviceTypeCompetence != null)
				return false;
		} 
		else if (!serviceTypeCompetence.equals(other.serviceTypeCompetence))
			return false;
		return true;
	}
	
	/**
	 * Returns the name of the period.
	 * @return the name of the period
	 */
	public String getPeriod()
	{
		return period;
	}
	
	/**
	 * Returns the name of the serviceTypeCompetence
	 * @return the serviceTypeCompetencename
	 */
	public String getServiceTypeCompetence()
	{
		return serviceTypeCompetence;
	}
	
	/**
	 * Sets the name of the period
	 * @param period the period
	 */
	public void setPeriod(String period)
	{
		this.period = period;
	}
	
	/**
	 * Sets the name of the serviceTypeCompetence
	 * @param serviceTypeCompetence the serviceTypeCompetencename
	 */
	public void setServiceTypeCompetence(String serviceTypeCompetence)
	{
		this.serviceTypeCompetence = serviceTypeCompetence;
	}
	
	public int getPeriodId() {
		return periodId;
	}

	public void setPeriodId(int periodId) {
		this.periodId = periodId;
	}
}
