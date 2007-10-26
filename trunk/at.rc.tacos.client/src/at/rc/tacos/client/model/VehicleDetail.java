package at.rc.tacos.client.model;

/**
 * Specifies the crew and phone of an ambulance
 * @author b.thek
 */

public class VehicleDetail 
{
	private int vehicleId;
	private StaffMember driverName;
	private StaffMember paramedicIName;
	private StaffMember paramedicIIName;
	private MobilePhoneDetail mobilePhone;
	private String vehicleNotes;
	
	
	
	/**
	 * Constructors
	 */
	public VehicleDetail(){}


	public VehicleDetail(int vehicleId, StaffMember driverName, StaffMember paramedicIName,
			StaffMember paramedicIIName, MobilePhoneDetail mobilePhone, String vehicleNotes) 
	{
		this.vehicleId = vehicleId;
		this.driverName = driverName;
		this.paramedicIName = paramedicIName;
		this.paramedicIIName = paramedicIIName;
		this.mobilePhone = mobilePhone;
		this.vehicleNotes = vehicleNotes;
	}


	/**
	 * Setter&Getter
	 */
	
	/**
	 * @return the vehicleId
	 */
	public int getVehicleId() {
		return vehicleId;
	}


	/**
	 * @param vehicleId the vehicleId to set
	 */
	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}


	/**
	 * @return the driverName
	 */
	public StaffMember getDriverName() {
		return driverName;
	}


	/**
	 * @param driverName the driverName to set
	 */
	public void setDriverName(StaffMember driverName) {
		this.driverName = driverName;
	}


	/**
	 * @return the paramedicIName
	 */
	public StaffMember getParameticIName() {
		return paramedicIName;
	}


	/**
	 * @param paramedicIName the paramedicIName to set
	 */
	public void setParameticIName(StaffMember paramedicIName) {
		this.paramedicIName = paramedicIName;
	}


	/**
	 * @return the paramedicIIName
	 */
	public StaffMember getParameticIIName() {
		return paramedicIIName;
	}


	/**
	 * @param paramedicIIName the paramedicIIName to set
	 */
	public void setParameticIIName(StaffMember paramedicIIName) {
		this.paramedicIIName = paramedicIIName;
	}


	/**
	 * @return the mobilePhone
	 */
	public MobilePhoneDetail getMobilePhone() {
		return mobilePhone;
	}


	/**
	 * @param mobilPhone the mobilePhone to set
	 */
	public void setMobilPhone(MobilePhoneDetail mobilePhone) {
		this.mobilePhone = mobilePhone;
	}


	/**
	 * @return the vehicleNotes
	 */
	public String getVehicleNotes() {
		return vehicleNotes;
	}


	/**
	 * @param vehicleNotes the vehicleNotes to set
	 */
	public void setVehicleNotes(String vehicleNotes) {
		this.vehicleNotes = vehicleNotes;
	}
}
