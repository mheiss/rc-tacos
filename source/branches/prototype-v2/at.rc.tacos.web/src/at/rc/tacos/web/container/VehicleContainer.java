package at.rc.tacos.web.container;

import at.rc.tacos.platform.model.Location;
import at.rc.tacos.platform.model.MobilePhoneDetail;
import at.rc.tacos.platform.model.StaffMember;

/**
 * Replaces VehicleDetail because PropertyComparator has problem with SWT Image
 * @author Payer Martin
 * @version 1.0
 */
public class VehicleContainer {
    private String vehicleName;
    private String vehicleType;
    private StaffMember driver;
    private StaffMember firstParamedic;
    private StaffMember secondParamedic;
    private MobilePhoneDetail mobilePhone;
    private String vehicleNotes;
    private Location basicStation;
    private Location currentStation;
    private boolean readyForAction;
    private boolean outOfOrder;
    private int transportStatus;
	public String getVehicleName() 
	{
		return vehicleName;
	}
	public void setVehicleName(String vehicleName) 
	{
		this.vehicleName = vehicleName;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	public StaffMember getDriver() {
		return driver;
	}
	public void setDriver(StaffMember driver) {
		this.driver = driver;
	}
	public StaffMember getFirstParamedic() {
		return firstParamedic;
	}
	public void setFirstParamedic(StaffMember firstParamedic) {
		this.firstParamedic = firstParamedic;
	}
	public StaffMember getSecondParamedic() {
		return secondParamedic;
	}
	public void setSecondParamedic(StaffMember secondParamedic) {
		this.secondParamedic = secondParamedic;
	}
	public MobilePhoneDetail getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(MobilePhoneDetail mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getVehicleNotes() {
		return vehicleNotes;
	}
	public void setVehicleNotes(String vehicleNotes) {
		this.vehicleNotes = vehicleNotes;
	}
	public Location getBasicStation() {
		return basicStation;
	}
	public void setBasicStation(Location basicStation) {
		this.basicStation = basicStation;
	}
	public Location getCurrentStation() {
		return currentStation;
	}
	public void setCurrentStation(Location currentStation) {
		this.currentStation = currentStation;
	}
	public boolean isReadyForAction() {
		return readyForAction;
	}
	public void setReadyForAction(boolean readyForAction) {
		this.readyForAction = readyForAction;
	}
	public boolean isOutOfOrder() {
		return outOfOrder;
	}
	public void setOutOfOrder(boolean outOfOrder) {
		this.outOfOrder = outOfOrder;
	}
	public int getTransportStatus() {
		return transportStatus;
	}
	public void setTransportStatus(int transportStatus) {
		this.transportStatus = transportStatus;
	}
}
