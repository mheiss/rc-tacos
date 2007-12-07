package at.rc.tacos.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.swt.graphics.Image;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.factory.ImageFactory;

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
     * Class constructor to create a minimal vehicle object
     * @param vehicleName the identification name of the vehicle 
     * @param vehicleType the type of the vehicle
     * @param basicStation the station where the vehicle is located
     */
    public VehicleDetail(String vehicleName,String vehicleType,String basicStation) 
    {
        super(ID);
        setVehicleName(vehicleName);
        setVehicleType(vehicleType);
        setBasicStation(basicStation);
    }

    //METHODS
    /**
     * Adds a property change listener to this class
     */
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
        return vehicleId+","+vehicleName+","+vehicleType;
    }


    /**
     * Returns the calculated hash code based on the vehicle id.<br>
     * Two vehicles have the same hash code if the id is the same.
     * @return the calculated hash code
     */
    @Override
    public int hashCode()
    {
        return 31 + vehicleId;
    }

    /**
     * Returns whether the objects are equal or not.<br>
     * Two vehicles are equal if, and only if, the vehicleId is the same.
     * @return true if the id is the same otherwise false.
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
        final VehicleDetail other = (VehicleDetail) obj;
        if (vehicleId != other.vehicleId)
            return false;
        return true;
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

    //GETTERS FOR THE STATUS IMAGE
    /**
     * Returns the status image for the mobile phone.
     * @return the image for the phone
     */
    public Image getMobilePhoneImage()
    {
        if (!mobilePhone.getMobilePhoneId().equalsIgnoreCase(vehicleName))
            return ImageFactory.getInstance().getRegisteredImage("image.vehicle.phone");
        else
            return ImageFactory.getInstance().getRegisteredImage("image.vehicle.phone.na");
    }

    /**
     * Returns the image to visualise if there are notes
     * @return the image for the notes
     */
    public Image getVehicleNotesImage()
    {
        if (vehicleNotes == null || vehicleNotes.isEmpty())   
            return ImageFactory.getInstance().getRegisteredImage("image.vehicle.notes.na");
        else
            return ImageFactory.getInstance().getRegisteredImage("image.vehicle.notes"); 
    }

    /**
     * Returns the image to visualize whether the vehicle basic station
     * is the current station
     * @return the image for the station
     */
    public Image getStationImage()
    {
        if (!basicStation.equalsIgnoreCase(currentStation))
            return ImageFactory.getInstance().getRegisteredImage("image.vehicle.house");
        else
            return ImageFactory.getInstance().getRegisteredImage("image.vehicle.house.na");
    }
    
    /**
     * Returns the image to visualize whether the vehicle is ready for action
     * @return the vehicle status image
     */
    public Image getReadyForActionImage()
    {
        if (readyForAction)
            return ImageFactory.getInstance().getRegisteredImage("image.vehicle.ready");
        else
            return ImageFactory.getInstance().getRegisteredImage("image.vehicle.ready.na");
    }
    
    /**
     * Returns the image to visualize whether the vehicle is out of order
     * @return the repair status image
     */
    public Image getOutOfOrderImage()
    {
        if (outOfOrder)
            return ImageFactory.getInstance().getRegisteredImage("image.vehicle.repair");
        else
            return ImageFactory.getInstance().getRegisteredImage("image.vehicle.repair.na");
    }
    
    /**
     * Returns the status transport status image
     * @return the status image
     */
    public Image getTransportStatusImage()
    {
        switch(mostImportantTransportStatus)
        {
        //the green image
        case 0: 
        case 1:
        case 5:
        case 6: return ImageFactory.getInstance().getRegisteredImage("image.vehicle.status.green"); 
        //the yellow image
        case 2:
        case 4:
        case 9: return ImageFactory.getInstance().getRegisteredImage("image.vehicle.status.yellow");
        //the read image 
        case 3:
        case 7: return ImageFactory.getInstance().getRegisteredImage("image.vehicle.status.red"); 
        //out of range
        default: return ImageFactory.getInstance().getRegisteredImage("image.vehicle.status.na"); 
        }  
    }
}
