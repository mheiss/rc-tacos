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
	public VehicleDetail(String vehicleName,String vehicleType,String basicStation) 
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
		boolean oldValue = this.readyForAction;
		this.readyForAction = readyForAction;
		firePropertyChange("readyForAction", oldValue, readyForAction);
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
		if(vehicleId < 0)
			throw new IllegalArgumentException("The id cannot be negative");
		int oldId = vehicleId;
		this.vehicleId = vehicleId;
		firePropertyChange("vehicleId", oldId, vehicleId);
	}

	/**
	 * @return the mobilePhone
	 */
	public MobilePhoneDetail getMobilePhone() 
	{
		return mobilePhone;
	}

	/**
	 * @param mobilePhone the mobilePhone to set
	 */
	public void setMobilPhone(MobilePhoneDetail mobilePhone) 
	{
		MobilePhoneDetail oldPhone = this.mobilePhone;
		this.mobilePhone = mobilePhone;
		firePropertyChange("mobilePhone", oldPhone, mobilePhone);
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
		String oldNotes = this.vehicleNotes;
		this.vehicleNotes = vehicleNotes;
		firePropertyChange("vehicleNotes", oldNotes, vehicleNotes);
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
		String oldStation = this.basicStation;
		this.basicStation = basicStation;
		firePropertyChange("basicStation", oldStation, basicStation);
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
		String oldStation = this.currentStation;
		this.currentStation = currentStation;
		firePropertyChange("currentStation", oldStation, currentStation);
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
		boolean oldStatus = this.outOfOrder;
		this.outOfOrder = outOfOrder;
		firePropertyChange("outOfOrder", oldStatus, outOfOrder);
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
		int oldStatus = this.mostImportantTransportStatus;
		this.mostImportantTransportStatus = mostImportantTransportStatus;
		firePropertyChange("mostImportantTransportStatus", oldStatus, mostImportantTransportStatus);
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
		String oldVehicleName = this.vehicleName;
		this.vehicleName = vehicleName;
		firePropertyChange("vehicleName", oldVehicleName, vehicleName); 
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
		String oldVehicleType = this.vehicleType;
		this.vehicleType = vehicleType;
		firePropertyChange("vehicleType", oldVehicleType, vehicleType);
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
	public void setDriverName(StaffMember driverName) 
	{
		StaffMember oldDriver = this.driverName;
		this.driverName = driverName;
		firePropertyChange("driverName", oldDriver, driverName);
	}

	/**
	 * @return the paramedicIName
	 */
	public StaffMember getParamedicIName() 
	{
		return paramedicIName;
	}

	/**
	 * @param paramedicIName the paramedicIName to set
	 */
	public void setParamedicIName(StaffMember paramedicIName) 
	{
		StaffMember oldMedic = this.paramedicIName;
		this.paramedicIName = paramedicIName;
		firePropertyChange("paramedicIName", oldMedic, paramedicIName);
	}

	/**
	 * @return the paramedicIIName
	 */
	public StaffMember getParamedicIIName() 
	{
		return paramedicIIName;
	}

	/**
	 * @param paramedicIIName the paramedicIIName to set
	 */
	public void setParamedicIIName(StaffMember paramedicIIName) 
	{
		StaffMember oldMedic = this.paramedicIIName;
		this.paramedicIIName = paramedicIIName;
		firePropertyChange("paramedicIIName", oldMedic, paramedicIIName);
	}

	//GETTERS FOR THE STATUS IMAGE
	/**
	 * Sets the image to use for the mobile phone
	 */
	public void setMobilePhoneImage()
	{
		Image oldImage = this.mobilePhoneImage;
		if (!mobilePhone.getMobilePhoneId().equalsIgnoreCase(vehicleName))
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
		if (!basicStation.equalsIgnoreCase(currentStation))
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
		switch(mostImportantTransportStatus)
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
