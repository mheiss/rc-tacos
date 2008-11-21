package at.rc.tacos.platform.model;

/**
 * Specifies the crew and phone of an ambulance
 * 
 * @author b.thek
 */
public class VehicleDetail {

	private String vehicleName;
	private String vehicleType;
	private StaffMember driver;
	private StaffMember firstParamedic;
	private StaffMember secondParamedic;
	private MobilePhoneDetail mobilePhone;
	private String vehicleNotes;
	private String lastDestinationFree;
	private Location basicStation;
	private Location currentStation;
	private boolean readyForAction;
	private boolean outOfOrder;
	private int transportStatus;

	// the transport status
	public final static int TRANSPORT_STATUS_GREEN = 30;
	public final static int TRANSPORT_STATUS_YELLOW = 20;
	public final static int TRANSPROT_STATUS_RED = 10;
	public final static int TRANSPORT_STATUS_NA = 0;
	public final static int TRANSPORT_STATUS_BLUE = 40;

	/**
	 * Default class constructor
	 */
	public VehicleDetail() {
		vehicleName = "";
		vehicleType = "";
	}

	/**
	 * Class constructor to create a minimal vehicle object
	 * 
	 * @param vehicleName
	 *            the identification name of the vehicle
	 * @param vehicleType
	 *            the type of the vehicle
	 * @param basicStation
	 *            the station where the vehicle is located
	 */
	public VehicleDetail(String vehicleName, String vehicleType, Location basicStation, Location currentStation, MobilePhoneDetail mobilePhone, String vehicleNotes, Boolean readyForAction, Boolean outOfOrder) {
		setVehicleName(vehicleName);
		setVehicleType(vehicleType);
		setBasicStation(basicStation);
		setCurrentStation(currentStation);
		setMobilPhone(mobilePhone);
		setVehicleNotes(vehicleNotes);
		setReadyForAction(readyForAction);
		setOutOfOrder(outOfOrder);
	}

	/**
	 * Returns a string based description of the object
	 * 
	 * @return the description of the object
	 */
	@Override
	public String toString() {
		String vehicle;
		vehicle = vehicleName + ";" + vehicleType + ";";
		// staff
		if (driver != null)
			vehicle = vehicle + "Fahrer: " + driver + ";";
		if (firstParamedic != null)
			vehicle = vehicle + "SaniI: " + firstParamedic + ";";
		if (secondParamedic != null)
			vehicle = vehicle + "SaniII: " + secondParamedic + ";";
		if (mobilePhone != null)
			vehicle = vehicle + "Handy: " + mobilePhone + ";";
		if (vehicleNotes != null)
			vehicle = vehicle + "Notizen: " + vehicleNotes + ";";
		vehicle = vehicle + "OS1: " + basicStation + ";" + "OS2: " + currentStation + ";";
		if (readyForAction)
			vehicle = vehicle + "EB" + ";";
		if (outOfOrder)
			vehicle = vehicle + "AD";

		return vehicle;
	}

	/**
	 * Returns the calculated hash code based on the vehicle name.<br>
	 * Two vehicles have the same hash code if the name is the same.
	 * 
	 * @return the calculated hash code
	 */
	@Override
	public int hashCode() {
		return vehicleName.hashCode();
	}

	/**
	 * Returns whether the objects are equal or not.<br>
	 * Two vehicles are equal if, and only if, the vehicle name is the same.
	 * 
	 * @return true if the id is the same otherwise false.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final VehicleDetail other = (VehicleDetail) obj;
		if (!vehicleName.equals(other.vehicleName))
			return false;
		return true;
	}

	// GETTERS AND SETTERS
	/**
	 * Returns the unique name of the vehicle
	 * 
	 * @return the vehicleName
	 */
	public String getVehicleName() {
		return vehicleName;
	}

	/**
	 * Returns the type of the vehicle. The type could be RTW or KTW for example
	 * 
	 * @return the vehicleType
	 */
	public String getVehicleType() {
		return vehicleType;
	}

	/**
	 * Returns the assigned driver of the vehicle
	 * 
	 * @return the driverName
	 */
	public StaffMember getDriver() {
		return driver;
	}

	/**
	 * Returns the assigned primary paramedic for this vehicle
	 * 
	 * @return the paramedicIIName
	 */
	public StaffMember getFirstParamedic() {
		return firstParamedic;
	}

	/**
	 * Returns the assigend second paramedic
	 * 
	 * @return the paramedicIName
	 */
	public StaffMember getSecondParamedic() {
		return secondParamedic;
	}

	/**
	 * Returns the assigned mobile phone of this vehicle
	 * 
	 * @return the mobilePhone
	 */
	public MobilePhoneDetail getMobilePhone() {
		return mobilePhone;
	}

	/**
	 * Returns the basic location where this vehicle belongs to.
	 * 
	 * @return the basicStation
	 */
	public Location getBasicStation() {
		return basicStation;
	}

	/**
	 * Returns the current location where this vehicle is assigend to.
	 * 
	 * @return the currentStation
	 */
	public Location getCurrentStation() {
		return currentStation;
	}

	/**
	 * Returns the notes for this vehicle
	 * 
	 * @return the vehicleNotes
	 */
	public String getVehicleNotes() {
		return vehicleNotes;
	}

	/**
	 * Returns wheter or not this vehicle is ready for action
	 * 
	 * @return the readyForAction
	 */
	public boolean isReadyForAction() {
		return readyForAction;
	}

	/**
	 * Returns wheter or not the vehicle is out of order
	 * 
	 * @return the outOfOrder
	 */
	public boolean isOutOfOrder() {
		return outOfOrder;
	}

	/**
	 * Returns the transport status to visualize in the vehicle composite
	 * 
	 * @return the mostImportantTransportStatus
	 */
	public int getTransportStatus() {
		return transportStatus;
	}

	/*
	 * ----------------------------------------- Setters for the vehicle details
	 * ------------------------------------------
	 */
	/**
	 * Sets the name of the vehicle. The name is a unique value to identify the
	 * vehicle
	 * 
	 * @param vehicleName
	 *            the vehicleName to set
	 */
	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
	}

	/**
	 * Sets the type of the vehicle
	 * 
	 * @param vehicleType
	 *            the vehicleType to set
	 */
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	/**
	 * Sets the assigend driver for the vehicle
	 * 
	 * @param driver
	 *            the driverName to set
	 */
	public void setDriver(StaffMember driver) {
		this.driver = driver;
	}

	/**
	 * Assignes the first paramedic to this vehicle
	 * 
	 * @param firstParamedic
	 *            the paramedicIName to set
	 */
	public void setFirstParamedic(StaffMember firstParamedic) {
		this.firstParamedic = firstParamedic;
	}

	/**
	 * Assignes the second paramedic to this vehicle
	 * 
	 * @param secondParamedic
	 *            the paramedicIIName to set
	 */
	public void setSecondParamedic(StaffMember secondParamedic) {
		this.secondParamedic = secondParamedic;
	}

	/**
	 * Sets the mobile phone which is on board of the vehicle
	 * 
	 * @param mobilePhone
	 *            the mobilePhone to set
	 */
	public void setMobilPhone(MobilePhoneDetail mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	/**
	 * Sets the notes for this vehicle to display. Notes are general
	 * informations
	 * 
	 * @param vehicleNotes
	 *            the vehicleNotes to set
	 */
	public void setVehicleNotes(String vehicleNotes) {
		this.vehicleNotes = vehicleNotes;
	}

	public void setLastDestinationFree(String lastDestinationFree) {
		this.lastDestinationFree = lastDestinationFree;
	}

	/**
	 * Sets the basic station of the vehicle.
	 * 
	 * @param basicStation
	 *            the basicStation to set
	 */
	public void setBasicStation(Location basicStation) {
		this.basicStation = basicStation;
	}

	/**
	 * Sets the current station where the vehicle is located to
	 * 
	 * @param currentStation
	 *            the currentStation to set
	 */
	public void setCurrentStation(Location currentStation) {
		this.currentStation = currentStation;
	}

	/**
	 * Sets the current status of the vehicle. A vehicle cannot be assigned to a
	 * transport if the status if not ready.
	 * 
	 * @param readyForAction
	 *            the readyForAction to set
	 */
	public void setReadyForAction(boolean readyForAction) {
		this.readyForAction = readyForAction;
	}

	/**
	 * Sets this vehicle as out of order or not
	 * 
	 * @param outOfOrder
	 *            the outOfOrder to set
	 */
	public void setOutOfOrder(boolean outOfOrder) {
		this.outOfOrder = outOfOrder;
	}

	/**
	 * Sets the most important transport status to visualize
	 * 
	 * @param transportStatus
	 *            the transportStatus to set
	 */
	public void setTransportStatus(int transportStatus) {
		this.transportStatus = transportStatus;
	}

	public String getLastDestinationFree() {
		return lastDestinationFree;
	}
}
