package at.rc.tacos.model;

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
    //properties
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

    //images to use
    private Image mobilePhoneImage;
    private Image vehicleNotesImage;
    private Image stationImage;
    private Image readyForActionImage;
    private Image outOfOrderImage;
    private Image transportStatusImage;

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
    public VehicleDetail(String vehicleName,String vehicleType,Location basicStation) 
    {
        super(ID);
        setVehicleName(vehicleName);
        setVehicleType(vehicleType);
        setBasicStation(basicStation);
    }

    /**
     * Returns a string based description of the object
     * @return the description of the object
     */
    @Override
    public String toString()
    {
        return vehicleName+","+vehicleType;
    }


    /**
     * Returns the calculated hash code based on the vehicle name.<br>
     * Two vehicles have the same hash code if the name is the same.
     * @return the calculated hash code
     */
    @Override
    public int hashCode()
    {
        return vehicleName.hashCode();
    }

    /**
     * Returns whether the objects are equal or not.<br>
     * Two vehicles are equal if, and only if, the vehicle name is the same.
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
        if (!vehicleName.equals(other.vehicleName))
            return false;
        return true;
    }

    /**
     * Convenient helper method to update all images
     */
    public void updateImages()
    {
        setMobilePhoneImage();
        setVehicleNotesImage();
        setStationImage();
        setOutOfOrderImage();
        setReadyForActionImage();
        setTransportStatusImage();
    }

    //GETTERS AND SETTERS
    /**
     * Returns the unique name of the vehicle
     * @return the vehicleName
     */
    public String getVehicleName() 
    {
        return vehicleName;
    }

    /**
     * Returns the type of the vehicle.
     * The type could be RTW or KTW for example
     * @return the vehicleType
     */
    public String getVehicleType() 
    {
        return vehicleType;
    }

    /**
     * Returns the assigned driver of the vehicle
     * @return the driverName
     */
    public StaffMember getDriver() 
    {
        return driver;
    }

    /**
     * Returns the assigned primary paramedic for this vehicle
     * @return the paramedicIIName
     */
    public StaffMember getFirstParamedic() 
    {
        return firstParamedic;
    }

    /**
     * Returns the assigend second paramedic
     * @return the paramedicIName
     */
    public StaffMember getSecondParamedic() 
    {
        return secondParamedic;
    }

    /**
     * Returns the assigned mobile phone of this vehicle
     * @return the mobilePhone
     */
    public MobilePhoneDetail getMobilePhone() 
    {
        return mobilePhone;
    }
    
    /**
     * Returns the basic location where this vehicle belongs to.
     * @return the basicStation
     */
    public Location getBasicStation() 
    {
        return basicStation;
    }
    
    /**
     * Returns the current location where this vehicle is assigend to.
     * @return the currentStation
     */
    public Location getCurrentStation() 
    {
        return currentStation;
    }
    
    /**
     * Returns the notes for this vehicle
     * @return the vehicleNotes
     */
    public String getVehicleNotes() 
    {
        return vehicleNotes;
    }

    /**
     * Returns wheter or not this vehicle is ready for action
     * @return the readyForAction
     */
    public boolean isReadyForAction() 
    {
        return readyForAction;
    }

    /**
     * Returns wheter or not the vehicle is out of order
     * @return the outOfOrder
     */
    public boolean isOutOfOrder() 
    {
        return outOfOrder;
    }
    
    /**
     * Returns the transport status to visualize in the vehicle composite
     * @return the mostImportantTransportStatus
     */
    public int getTransportStatus()
    {
        return transportStatus;
    }
    
    /*-----------------------------------------
     * Setters for the vehicle details
     ------------------------------------------*/
    /**
     * Sets the name of the vehicle.
     * The name is a unique value to identify the vehicle
     * @param vehicleName the vehicleName to set
     */
    public void setVehicleName(String vehicleName) 
    {
        String oldVehicleName = this.vehicleName;
        this.vehicleName = vehicleName;
        firePropertyChange("vehicleName", oldVehicleName, vehicleName); 
    }
    
    /**
     * Sets the type of the vehicle
     * @param vehicleType the vehicleType to set
     */
    public void setVehicleType(String vehicleType) 
    {
        String oldVehicleType = this.vehicleType;
        this.vehicleType = vehicleType;
        firePropertyChange("vehicleType", oldVehicleType, vehicleType);
    }
    
    /**
     * Sets the assigend driver for the vehicle
     * @param driver the driverName to set
     */
    public void setDriver(StaffMember driver) 
    {
        StaffMember oldDriver = this.driver;
        this.driver = driver;
        firePropertyChange("driver", oldDriver, driver);
    }

    /**
     * Assignes the first paramedic to this vehicle
     * @param firstParamedic the paramedicIName to set
     */
    public void setFirstParamedic(StaffMember firstParamedic) 
    {
        StaffMember oldMedic = this.firstParamedic;
        this.firstParamedic = firstParamedic;
        firePropertyChange("firstParamedic", oldMedic, firstParamedic);
    }

    /**
     * Assignes the second paramedic to this vehicle
     * @param secondParamedic the paramedicIIName to set
     */
    public void setSecondParamedic(StaffMember secondParamedic) 
    {
        StaffMember oldMedic = this.secondParamedic;
        this.secondParamedic = secondParamedic;
        firePropertyChange("secondParamedic", oldMedic, secondParamedic);
    }
    
    /**
     * Sets the mobile phone which is on board of the vehicle
     * @param mobilePhone the mobilePhone to set
     */
    public void setMobilPhone(MobilePhoneDetail mobilePhone) 
    {
        MobilePhoneDetail oldPhone = this.mobilePhone;
        this.mobilePhone = mobilePhone;
        firePropertyChange("mobilePhone", oldPhone, mobilePhone);
    }
    
    /**
     * Sets the notes for this vehicle to display.
     * Notes are general informations
     * @param vehicleNotes the vehicleNotes to set
     */
    public void setVehicleNotes(String vehicleNotes) 
    {
        String oldNotes = this.vehicleNotes;
        this.vehicleNotes = vehicleNotes;
        firePropertyChange("vehicleNotes", oldNotes, vehicleNotes);
    }
    
    /**
     * Sets the basic station of the vehicle. 
     * @param basicStation the basicStation to set
     */
    public void setBasicStation(Location basicStation) 
    {
        Location oldStation = this.basicStation;
        this.basicStation = basicStation;
        firePropertyChange("basicStation", oldStation, basicStation);
    }


    /**
     * Sets the current station where the vehicle is located to
     * @param currentStation the currentStation to set
     */
    public void setCurrentStation(Location currentStation) 
    {
        Location oldStation = this.currentStation;
        this.currentStation = currentStation;
        firePropertyChange("currentStation", oldStation, currentStation);
    }  
    
    /**
     * Sets the current status of the vehicle.
     * A vehicle cannot be assigned to a transport if the status if not ready.
     * @param readyForAction the readyForAction to set
     */
    public void setReadyForAction(boolean readyForAction) 
    {
        boolean oldValue = this.readyForAction;
        this.readyForAction = readyForAction;
        firePropertyChange("readyForAction", oldValue, readyForAction);
    }

    /**
     * Sets this vehicle as out of order or not
     * @param outOfOrder the outOfOrder to set
     */
    public void setOutOfOrder(boolean outOfOrder) 
    {
        boolean oldStatus = this.outOfOrder;
        this.outOfOrder = outOfOrder;
        firePropertyChange("outOfOrder", oldStatus, outOfOrder);
    }

    /**
     * Sets the most important transport status to visualize
     * @param mostImportantTransportStatus the mostImportantTransportStatus to set
     */
    public void setTransportStatus(int transportStatus)
    {
        int oldStatus = this.transportStatus;
        this.transportStatus = transportStatus;
        firePropertyChange("transportStatus", oldStatus, transportStatus);
    }

    //GETTERS FOR THE STATUS IMAGE
    /**
     * Sets the image to use for the mobile phone
     */
    public void setMobilePhoneImage()
    {
        Image oldImage = this.mobilePhoneImage;    
        if(mobilePhone == null)
            mobilePhoneImage = null;
        else if (!mobilePhone.getMobilePhoneName().equalsIgnoreCase(vehicleName))
            mobilePhoneImage = ImageFactory.getInstance().getRegisteredImage("image.vehicle.phone");
        else
            mobilePhoneImage = ImageFactory.getInstance().getRegisteredImage("image.vehicle.phone.na");

        firePropertyChange("mobilePhoneImage", oldImage, mobilePhoneImage);
    }

    /**
     * Returns the status ImageDescriptor for the mobile phone.
     * @return the ImageDescriptor for the phone
     */
    public Image getMobilePhoneImage()
    {
        return mobilePhoneImage;
    }

    /**
     * Sets the ImageDescriptor to use for the notes.
     */
    public void setVehicleNotesImage()
    {
        Image oldImage = this.vehicleNotesImage;
        if (vehicleNotes == null || vehicleNotes.isEmpty())   
            vehicleNotesImage = ImageFactory.getInstance().getRegisteredImage("image.vehicle.notes.na");
        else
            vehicleNotesImage = ImageFactory.getInstance().getRegisteredImage("image.vehicle.notes"); 
        firePropertyChange("vehicleNotesImage", oldImage, vehicleNotesImage);
    }

    /**
     * Returns the Image to visualise if there are notes
     * @return the Image for the notes
     */
    public Image getVehicleNotesImage()
    {
        return vehicleNotesImage;
    }

    /**
     * Sets the Image to use for the station
     */
    public void setStationImage()
    {
        Image oldImage = this.stationImage;
        if (!basicStation.getLocationName().equalsIgnoreCase(currentStation.getLocationName()))
            stationImage =  ImageFactory.getInstance().getRegisteredImage("image.vehicle.house");
        else
            stationImage = ImageFactory.getInstance().getRegisteredImage("image.vehicle.house.na");
        firePropertyChange("stationImage", oldImage, stationImage);
    }

    /**
     * Returns the Image to visualize whether the vehicle basic station
     * is the current station
     * @return the Image for the station
     */
    public Image getStationImage()
    {
        return stationImage;
    }

    /**
     * Sets the Image to use for the ready status
     */
    public void setReadyForActionImage()
    {
        Image oldImage = this.readyForActionImage;
        if (readyForAction)
            readyForActionImage = ImageFactory.getInstance().getRegisteredImage("image.vehicle.ready");
        else
            readyForActionImage = ImageFactory.getInstance().getRegisteredImage("image.vehicle.ready.na");
        firePropertyChange("readyForActionImage", oldImage, readyForActionImage);
    }

    /**
     * Returns the Image to visualize whether the vehicle is ready for action
     * @return the vehicle status Image
     */
    public Image getReadyForActionImage()
    {
        return readyForActionImage;
    }

    /**
     * Sets the Image to use for the repair status
     */
    public void setOutOfOrderImage()
    {
        Image oldImage = this.outOfOrderImage;
        if (outOfOrder)
            outOfOrderImage = ImageFactory.getInstance().getRegisteredImage("image.vehicle.repair");
        else
            outOfOrderImage = ImageFactory.getInstance().getRegisteredImage("image.vehicle.repair.na");	
        firePropertyChange("outOfOrderImage", oldImage, outOfOrderImage);
    }

    /**
     * Returns the Image to visualize whether the vehicle is out of order
     * @return the repair status Image
     */
    public Image getOutOfOrderImage()
    {
        return outOfOrderImage;
    }

    /**
     * Sets the Image to use for the transport status
     */
    public void setTransportStatusImage()
    {
        Image oldImage = this.transportStatusImage;
        //determine the Image
        switch(transportStatus)
        {
        //the green Image
        case 0: 
        case 1:
        case 5:
        case 6: transportStatusImage = ImageFactory.getInstance().getRegisteredImage("image.vehicle.status.green"); 
        break;
        //the yellow Image
        case 2:
        case 4:
        case 9: transportStatusImage =  ImageFactory.getInstance().getRegisteredImage("image.vehicle.status.yellow"); 
        break;
        //the read Image 
        case 3:
        case 7: transportStatusImage =  ImageFactory.getInstance().getRegisteredImage("image.vehicle.status.red"); 
        break;
        //out of range
        default: transportStatusImage =  ImageFactory.getInstance().getRegisteredImage("image.vehicle.status.na"); 
        }
        firePropertyChange("transportStatusImage", oldImage, transportStatusImage);
    }

    /**
     * Returns the status transport status Image
     * @return the status Image
     */
    public Image getTransportStatusImage()
    {
        return transportStatusImage;
    }
}
