package at.rc.tacos.platform.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Defines a vehicle resource
 * 
 * @author b.thek
 */
public class VehicleDetail extends Lockable {

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
	 * Returns the human readable string for this <code>VehicleDetail</code>
	 * instance.
	 * 
	 * @return the build string
	 */
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("vehicleId", vehicleName);
		builder.append("vehicleType", vehicleType);
		builder.append("phone", mobilePhone);
		builder.append("basicLocation", basicStation);
		builder.append("currentLocation", currentStation);
		builder.append("notes", vehicleNotes);
		builder.append("driver", driver);
		builder.append("firstParamedic", firstParamedic);
		builder.append("secondParamedic", secondParamedic);
		return builder.toString();
	}

	/**
	 * Returns the generated hashCode of this <code>VehicleDetail</code>
	 * instance.
	 * <p>
	 * The hashCode is based uppon the {@link VehicleDetail#vehicleName}
	 * </p>
	 * 
	 * @return the generated hash code
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder(55, 65);
		builder.append(vehicleName);
		return builder.toHashCode();
	}

	/**
	 * Returns wheter or not this <code>VehicleDetail</code> instance is equal
	 * to the compared object.
	 * <p>
	 * The compared fields are {@link VehicleDetail#vehicleName}
	 * </p>
	 * 
	 * @return true if the instance is the same otherwise false.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		VehicleDetail vehicleDetail = (VehicleDetail) obj;
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(vehicleName, vehicleDetail.vehicleName);
		return builder.isEquals();
	}

	// LOCKABLE IMPLEMENTATION
	@Override
	public int getLockedId() {
		return hashCode();
	}

	@Override
	public Class<?> getLockedClass() {
		return VehicleDetail.class;
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
