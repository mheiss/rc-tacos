package at.rc.tacos.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import at.rc.tacos.common.AbstractMessage;

/**
 * Specifies the crew and phone of an ambulance
 * @author b.thek
 */
public class VehicleDetail extends AbstractMessage
{
    //unique identification string
    public final static String ID = "vehicleDetail";

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);  
    
    private int vehicleId;
    private String vehicleName;
    private String vehicleType;
    private StaffMember driverName;
    private StaffMember paramedicIName;
    private StaffMember paramedicIIName;
    private MobilePhoneDetail mobilePhone;
    private String vehicleNotes;
    private String basicStation;
    private String currentStation;
    private boolean readyForAction;
    private boolean outOfOrder;
    private int mostImportantTransportStatus;

    /**
     * Default class constructor
     */
    public VehicleDetail()
    {
        super(ID);
    }

    /**
     * Class constructor for a complete vehicle detail object
     * @param vehicleId the id of the vehicle 
     * @param vehicleName the name of the vehicle 
     * @param vehicleType the type of the vehicle
     * @param driverName the driver details
     * @param paramedicIName the medic details
     * @param paramedicIIName the medic details
     * @param mobilePhone the mobile phone details
     * @param vehicleNotes notes for the vehicle
     * @param basicStation the home station
     * @param currentStation the current location
     * @param readyForAction the status
     * @param outOfOrder the repair status
     * @param mostImportantTransportStatus the transport status 
     */
    public VehicleDetail(int vehicleId, String vehicleName, String vehicleType,
            StaffMember driverName, StaffMember paramedicIName, StaffMember paramedicIIName,
            MobilePhoneDetail mobilePhone, String vehicleNotes,
            String basicStation, String currentStation, boolean readyForAction,
            boolean outOfOrder, int mostImportantTransportStatus) {
        super(ID);
        this.vehicleId = vehicleId;
        this.vehicleName = vehicleName;
        this.vehicleType = vehicleType;
        this.driverName = driverName;
        this.paramedicIName = paramedicIName;
        this.paramedicIIName = paramedicIIName;
        this.mobilePhone = mobilePhone;
        this.vehicleNotes = vehicleNotes;
        this.basicStation = basicStation;
        this.currentStation = currentStation;
        this.readyForAction = readyForAction;
        this.outOfOrder = outOfOrder;
        this.mostImportantTransportStatus = mostImportantTransportStatus;
    }
    
    
    
    
    /**
	 * @param id
	 * @param vehicleId
	 * @param vehicleName
	 */
	public VehicleDetail(String id, int vehicleId, String vehicleName) {
		super(id);
		this.vehicleId = vehicleId;
		this.vehicleName = vehicleName;
	}

	//METHODS
    public void addPropertyChangeListener(String propertyName,PropertyChangeListener listener) 
    {
        propertyChangeSupport.addPropertyChangeListener(propertyName,listener);
    }

    /**
     * Returns a string based description of the object
     * @return the description of the object
     */
    @Override
    public String toString()
    {
        return ID;
    }

    //GETTERS AND SETTERS

    /**
     * @return the readyForAction
     */
    public boolean isReadyForAction() 
    {
        return readyForAction;
    }

    /**
     * @param readyForAction the readyForAction to set
     */
    public void setReadyForAction(boolean readyForAction) 
    {
        this.readyForAction = readyForAction;
    }

    /**
     * @return the vehicleId
     */
    public int getVehicleId() {
        return vehicleId;
    }

    /**
     * @param vehicleId the vehicleId to set
     */
    public void setVehicleId(int vehicleId) 
    {
        this.vehicleId = vehicleId;
    }

    /**
     * @return the mobilePhone
     */
    public MobilePhoneDetail getMobilePhone() 
    {
        return mobilePhone;
    }

    /**
     * @param mobilPhone the mobilePhone to set
     */
    public void setMobilPhone(MobilePhoneDetail mobilePhone) 
    {
        this.mobilePhone = mobilePhone;
    }

    /**
     * @return the vehicleNotes
     */
    public String getVehicleNotes() 
    {
        return vehicleNotes;
    }

    /**
     * @param vehicleNotes the vehicleNotes to set
     */
    public void setVehicleNotes(String vehicleNotes) 
    {
        this.vehicleNotes = vehicleNotes;
    }

    /**
     * @return the basicStation
     */
    public String getBasicStation() {
        return basicStation;
    }

    /**
     * @param basicStation the basicStation to set
     */
    public void setBasicStation(String basicStation) 
    {
        this.basicStation = basicStation;
    }

    /**
     * @return the currentStation
     */
    public String getCurrentStation() 
    {
        return currentStation;
    }

    /**
     * @param currentStation the currentStation to set
     */
    public void setCurrentStation(String currentStation) 
    {
        this.currentStation = currentStation;
    }

    /**
     * @return the outOfOrder
     */
    public boolean isOutOfOrder() {
        return outOfOrder;
    }


    /**
     * @param outOfOrder the outOfOrder to set
     */
    public void setOutOfOrder(boolean outOfOrder) 
    {
        this.outOfOrder = outOfOrder;
    }

    /**
     * @return the mostImportantTransportStatus
     */
    public int getMostImportantTransportStatus()
    {
        return mostImportantTransportStatus;
    }

    /**
     * @param mostImportantTransportStatus the mostImportantTransportStatus to set
     */
    public void setMostImportantTransportStatus(int mostImportantTransportStatus)
    {
        this.mostImportantTransportStatus = mostImportantTransportStatus;
    }

    /**
     * @return the vehicleName
     */
    public String getVehicleName() 
    {
        return vehicleName;
    }
    /**
     * @param vehicleName the vehicleName to set
     */
    public void setVehicleName(String vehicleName) 
    {
        this.vehicleName = vehicleName;
    }

    /**
     * @return the vehicleType
     */
    public String getVehicleType() 
    {
        return vehicleType;
    }

    /**
     * @param vehicleType the vehicleType to set
     */
    public void setVehicleType(String vehicleType) 
    {
        this.vehicleType = vehicleType;
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
    public StaffMember getParamedicIName() {
        return paramedicIName;
    }

    /**
     * @param paramedicIName the paramedicIName to set
     */
    public void setParamedicIName(StaffMember paramedicIName) {
        this.paramedicIName = paramedicIName;
    }

    /**
     * @return the paramedicIIName
     */
    public StaffMember getParamedicIIName() {
        return paramedicIIName;
    }

    /**
     * @param paramedicIIName the paramedicIIName to set
     */
    public void setParamedicIIName(StaffMember paramedicIIName) {
        this.paramedicIIName = paramedicIIName;
    }
}
