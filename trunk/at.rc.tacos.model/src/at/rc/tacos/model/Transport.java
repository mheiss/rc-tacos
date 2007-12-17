package at.rc.tacos.model;

import java.util.ArrayList;
import java.util.Calendar;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IDirectness;
import at.rc.tacos.common.ITransportPriority;

/**
 * Specifies the transport details
 * @author b.thek
 */
public class Transport extends AbstractMessage implements ITransportPriority,IDirectness
{
    //unique identification string
    public final static String ID = "transport";

    private long transportId;
    private String fromStreet;
    private String fromNumber;
    private String fromCity;
    private Patient patient; 
    private String toStreet;
    private String toNumber;
    private String toCity;
    private String kindOfTransport;
    private CallerDetail callerDetail;
    private boolean backTransport;
    private boolean accompanyingPerson;
    private boolean emergencyPhone;
    private String kindOfIllness;
    private String transportNotes;
    private String responsibleStation;
    private String realStation;
    private long dateOfTransport;
    private long plannedStartOfTransport;
    private long plannedTimeAtPatient;
    private long appointmentTimeAtDestination;

    private String transportPriority;

    private boolean emergencyDoctorAlarming;
    private boolean helicopterAlarming;
    private boolean blueLightToPatient;
    private boolean blueLightToGoal;
    private boolean dfAlarming;
    private boolean brkdtAlarming;
    private boolean firebrigadeAlarming;
    private boolean mountainRescueServiceAlarming;
    private boolean policeAlarming;

    private String feedback;

    //directness
    private int directness;

    //vehicle and staff assigned to the transport
    private VehicleDetail vehicleDetail;

    //used for the status messages
    private ArrayList<StatusMessages> statusMessages;

    /**
     * Default class constructors for a transport<br>
     * The needed values should be accessed by the getter and setter methods
     */
    public Transport()
    {
        super(ID);
        statusMessages = new ArrayList<StatusMessages>();
    }
    
    /**
     *  Constructor for a minimal Transport object.
     *  @param fromStreet the street to get the transport
     *  @param fromNumber the housenumber
     *  @param fromCity the city to get the transport
     *  @param responsibleStation the station that is responsible
     *  @param dateOfTransport the date for the transport
     *  @param plannedStartOfTransport the starting time of the transport
     *  @param transportPriority the priority of the transport
     *  @param directness the direction of the transport
     */
    public Transport(String fromStreet,String fromNumber,String fromCity,
            String responsibleStation,long dateOfTransport, long plannedStartOfTransport,
            String transportPriority,int directness)
    {
        super(ID);
        statusMessages = new ArrayList<StatusMessages>();
        setFromStreet(fromStreet);
        setFromNumber(fromNumber);
        setFromCity(fromCity);
        setResponsibleStation(responsibleStation);
        setDateOfTransport(dateOfTransport);
        setPlannedStartOfTransport(plannedStartOfTransport);
        setTransportPriority(transportPriority);
        setDirectness(directness);
    }

    //METHODS
    /**
     * Convinience helper method to ensure a long value
     * is a valid date.
     * @param timestamp the value to check
     * @return true if the value is a date, otherwise false
     */
    private boolean isValidDate(long timestamp)
    {
        //create a calendar entry
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        //check the year
        if(cal.get(Calendar.YEAR) > 1960 && cal.get(Calendar.YEAR) < 2100)
            return true;
        //date out of range
        return false;
    }

    /**
     * Helper method to set a status for a transport
     * @param statusId the status identification 
     * @param timestamp the timestamp of the status
     */
    public void addStatus(int statusId, long timestamp)
    {
        statusMessages.add(new StatusMessages(statusId,timestamp));
    }

    /**
     * Returns a string based description of the object
     */
    @Override
    public String toString()
    {
        return transportId+","+fromStreet+","+fromNumber+","+fromCity+","+patient+","+toStreet+","+toNumber+","+toCity;
    }

    /**
     * Returns whether or not the given transports are equal.<br>
     * Two <code>Transport</code> objects are equal if they have the 
     * same transport id.
     * @param true if the transports  are equal.
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
        final Transport other = (Transport) obj;
        if (transportId != other.transportId)
            return false;
        return true;
    }

    /**
     * Returns the calculated hash code based on the transport id.<br>
     * Two transports have the same hash code if the id is the same.
     * @return the calculated hash code
     */
    @Override
    public int hashCode()
    {
        return 31  + (int) (transportId ^ (transportId >>> 32));
    } 

    //GETTERS AND SETTERS
    /**
     * Returns the identification string of this transport
     * @return the transportId
     */
    public long getTransportId() 
    {
        return transportId;
    }

    /**
     * Sets the identification string of this transport
     * @param transportId the transportId to set
     * @throws IllegalArgumentException if the id is negative
     */
    public void setTransportId(long transportId) 
    {
        if(transportId < 0)
            throw new IllegalArgumentException("The id cannot be negative");
        this.transportId = transportId;
    }

    /**
     * @return the fromStreet
     */
    public String getFromStreet() 
    {
        return fromStreet;
    }

    /**
     * @param fromStreet the fromStreet to set
     */
    public void setFromStreet(String fromStreet) 
    {
        if(fromStreet == null || fromStreet.trim().isEmpty())
            throw new IllegalArgumentException("The fromStreet cannot be null or empty");
        this.fromStreet = fromStreet;
    }

    /**
     * Returns the street number, floor, number of the flat if available
     * @return the fromNumber
     */
    public String getFromNumber() 
    {
        return fromNumber;
    }

    /**
     * @param fromNumber the fromNumber to set
     */
    public void setFromNumber(String fromNumber) 
    {
        if(fromNumber == null)
            throw new IllegalArgumentException("fromNumber cannot be null");
        this.fromNumber = fromNumber;
    }

    /**
     * @return the fromCity
     */
    public String getFromCity() 
    {
        return fromCity;
    }

    /**
     * @param fromCity the fromCity to set
     */
    public void setFromCity(String fromCity) 
    {
        if(fromCity == null)
            throw new IllegalArgumentException("fromCity cannot be null");
        this.fromCity = fromCity;
    }

    /**
     * @return the patient
     */
    public Patient getPatient() 
    {
        return patient;
    }

    /**
     * @param patient the patient to set
     */
    public void setPatient(Patient patient)
    {
        if(patient == null)
            throw new IllegalArgumentException("The patient cannot be null");
        this.patient = patient;
    }

    /**
     * @return the toStreet
     */
    public String getToStreet() 
    {
        return toStreet;
    }

    /**
     * @param toStreet the toStreet to set
     */
    public void setToStreet(String toStreet) 
    {
        if(toStreet == null)
            throw new IllegalArgumentException("The toStreet cannot be null");
        this.toStreet = toStreet;
    }

    /**
     * @return the toNumber
     */
    public String getToNumber() 
    {
        return toNumber;
    }

    /**
     * @param toNumber the toNumber to set
     */
    public void setToNumber(String toNumber) 
    {
        if(toNumber == null)
            throw new IllegalArgumentException("toNumber cannot be null");
        this.toNumber = toNumber;
    }

    /**
     * @return the toCity
     */
    public String getToCity() 
    {
        return toCity;
    }

    /**
     * @param toCity the toCity to set
     */
    public void setToCity(String toCity) 
    {
        if(toCity == null)
            throw new IllegalArgumentException("toCity cannot be null");
        this.toCity = toCity;
    }

    /**
     * Possible: 'gehend', 'sitzend', 'liegend', Rollstuhl'
     * @return the kindOfTransport
     */
    public String getKindOfTransport() 
    {
        return kindOfTransport;
    }

    /**
     * @param kindOfTransport the kindOfTransport to set
     */
    public void setKindOfTransport(String kindOfTransport) 
    {
        if(kindOfTransport == null)
            throw new IllegalArgumentException("The kind of transport cannot be null");
        this.kindOfTransport = kindOfTransport;
    }

    /**
     * @return the callerDetail
     */
    public CallerDetail getCallerDetail() 
    {
        return callerDetail;
    }

    /**
     * @param callerDetail the callerDetail to set
     */
    public void setCallerDetail(CallerDetail callerDetail) 
    {
        if(callerDetail == null)
            throw new IllegalArgumentException("The callerDetail cannot be null");
        this.callerDetail = callerDetail;
    }

    /**
     * @return the backTransport
     */
    public boolean isBackTransport() 
    {
        return backTransport;
    }

    /**
     * @param backTransport the backTransport to set
     */
    public void setBackTransport(boolean backTransport) 
    {
        this.backTransport = backTransport;
    }

    /**
     * @return the accompanyingPerson
     */
    public boolean isAccompanyingPerson() 
    {
        return accompanyingPerson;
    }

    /**
     * @param accompanyingPerson the accompanyingPerson to set
     */
    public void setAccompanyingPerson(boolean accompanyingPerson) 
    {
        this.accompanyingPerson = accompanyingPerson;
    }

    /**
     * the patient wear a chip on the body which is connected to a special phone to alert the red cross
     * sometimes a key for the door is deposited at the red cross station or the police station
     * @return the emergencyPhone
     */
    public boolean isEmergencyPhone() 
    {
        return emergencyPhone;
    }

    /**
     * @param emergencyPhone the emergencyPhone to set
     */
    public void setEmergencyPhone(boolean emergencyPhone) 
    {
        this.emergencyPhone = emergencyPhone;
    }

    /**
     * @return the kindOfIllness
     */
    public String getKindOfIllness() 
    {
        return kindOfIllness;
    }

    /**
     * @param kindOfIllness the kindOfIllness to set
     */
    public void setKindOfIllness(String kindOfIllness) 
    {
        if(kindOfIllness == null)
            throw new IllegalArgumentException("The kind of illness cannot be null");
        this.kindOfIllness = kindOfIllness;
    }

    /**
     * @return the transportNotes
     */
    public String getTransportNotes() 
    {
        return transportNotes;
    }

    /**
     * @param transportNotes the transportNotes to set
     */
    public void setTransportNotes(String transportNotes) 
    {
        if(transportNotes == null)
            throw new IllegalArgumentException("The transport notes cannot be null");
        this.transportNotes = transportNotes;
    }

    /**
     * The hole operational area is divided into six stations.
     * Primary for each location within the operational area one defined station is responsible
     * @return the responsibleStation
     * @see RosterEntry
     */
    public String getResponsibleStation() 
    {
        return responsibleStation;
    }

    /**
     * @param responsibleStation the responsibleStation to set
     */
    public void setResponsibleStation(String responsibleStation)
    {
        if(responsibleStation == null)
            throw new IllegalArgumentException("The responsible station cannot be null");
        this.responsibleStation = responsibleStation;
    }

    /**
     * The primary responsible station is not all the time the station which execute the transport
     * @return the realStation
     */
    public String getRealStation() 
    {
        return realStation;
    }

    /**
     * @param realStation the realStation to set
     */
    public void setRealStation(String realStation) 
    {
        if(realStation == null)
            throw new IllegalArgumentException("The real station cannot be null");
        this.realStation = realStation;
    }

    /**
     * @return the dateOfTransport
     */
    public long getDateOfTransport() 
    {
        return dateOfTransport;
    }

    /**
     * @param dateOfTransport the dateOfTransport to set
     */
    public void setDateOfTransport(long dateOfTransport) 
    {
        if(dateOfTransport < 0)
            throw new IllegalArgumentException("Date cannot be negative");
        if(!isValidDate(dateOfTransport))
            throw new IllegalArgumentException("Date is out of range");
        this.dateOfTransport = dateOfTransport;
    }

    /**
     * @return the plannedStartOfTransportTime
     */
    public long getPlannedStartOfTransport() 
    {
        return plannedStartOfTransport;
    }

    /**
     * @param plannedStartOfTransport the plannedStartOfTransport to set
     */
    public void setPlannedStartOfTransport(long plannedStartOfTransport) 
    {
        if(plannedStartOfTransport < 0)
            throw new IllegalArgumentException("Date cannot be negative");
        if(!isValidDate(plannedStartOfTransport))
            throw new IllegalArgumentException("Date is out of range");
        this.plannedStartOfTransport = plannedStartOfTransport;
    }

    /**
     * @return the plannedTimeAtPatient
     */
    public long getPlannedTimeAtPatient() 
    {
        return plannedTimeAtPatient;
    }

    /**
     * @param plannedTimeAtPatient the plannedTimeAtPatient to set
     */
    public void setPlannedTimeAtPatient(long plannedTimeAtPatient) 
    {
        if(plannedTimeAtPatient < 0)
            throw new IllegalArgumentException("Date cannot be negative");
        if(!isValidDate(plannedTimeAtPatient))
            throw new IllegalArgumentException("Date is out of range");
        this.plannedTimeAtPatient = plannedTimeAtPatient;
    }

    /**
     * @return the appointmentTimeAtDestination
     */
    public long getAppointmentTimeAtDestination() 
    {
        return appointmentTimeAtDestination;
    }

    /**
     * @param appointmentTimeAtDestination the appointmentTimeAtDestination to set
     */
    public void setAppointmentTimeAtDestination(long appointmentTimeAtDestination) 
    {
        if(appointmentTimeAtDestination < 0)
            throw new IllegalArgumentException("Date cannot be negative");
        if(!isValidDate(appointmentTimeAtDestination))
            throw new IllegalArgumentException("Date is out of range");
        this.appointmentTimeAtDestination = appointmentTimeAtDestination;
    }

    /**
     * Named: 'BD1'
     * @return the blueLightToPatient
     */
    public boolean isBluelightToPatient() {
        return blueLightToPatient;
    }

    /**
     * @param blueLightToPatient the blueLightToPatient to set
     */
    public void setBluelightToPatient(boolean blueLightToPatient) 
    {
        this.blueLightToPatient = blueLightToPatient;
    }

    /**
     * @return the blueLightToGoal
     * Named: 'BD2'
     */
    public boolean isBluelightToGoal() 
    {
        return blueLightToGoal;
    }

    /**
     * @param blueLightToGoal the blueLightToGoal to set
     */
    public void setBluelightToGoal(boolean blueLightToGoal) 
    {
        this.blueLightToGoal = blueLightToGoal;
    }

    /**
     * @return the feedback
     */
    public String getFeedback() 
    {
        return feedback;
    }

    /**
     * @param feedback the feedback to set
     */
    public void setFeedback(String feedback) 
    {
        if(feedback == null)
            throw new IllegalArgumentException("The feedback cannot be null");
        this.feedback = feedback;
    }

    /**
     * @return the directness
     */
    public int getDirectness() 
    {
        return directness;
    }

    /**
     * @param directness the directness to set
     * @see IDirectness
     */
    public void setDirectness(int directness) 
    {
        if(directness < 1 || directness > 5)
            throw new IllegalArgumentException("Invalid value for directness. Vaild values: 1,2,3,4,5");
        this.directness = directness;
    }

    /**
     * @return the vehicleDetail
     */
    public VehicleDetail getVehicleDetail() 
    {
        return vehicleDetail;
    }

    /**
     * @param vehicleDetail the vehicleDetail to set
     */
    public void setVehicleDetail(VehicleDetail vehicleDetail) 
    {
        if(vehicleDetail == null)
            throw new IllegalArgumentException("The vehicle detail cannot be null");
        this.vehicleDetail = vehicleDetail;
    }

    /**
     * @return the emergencyDoctorAlarming
     */
    public boolean isEmergencyDoctorAlarming() 
    {
        return emergencyDoctorAlarming;
    }

    /**
     * @param emergencyDoctorAlarming the emergencyDoctorAlarming to set
     */
    public void setEmergencyDoctorAlarming(boolean emergencyDoctorAlarming) 
    {
        this.emergencyDoctorAlarming = emergencyDoctorAlarming;
    }

    /**
     * @return the helicopterAlarming
     */
    public boolean isHelicopterAlarming() 
    {
        return helicopterAlarming;
    }

    /**
     * @param helicopterAlarming the helicopterAlarming to set
     */
    public void setHelicopterAlarming(boolean helicopterAlarming) 
    {
        this.helicopterAlarming = helicopterAlarming;
    }

    /**
     * @return the blueLightToPatient
     */
    public boolean isBlueLightToPatient() 
    {
        return blueLightToPatient;
    }

    /**
     * @param blueLightToPatient the blueLightToPatient to set
     */
    public void setBlueLightToPatient(boolean blueLightToPatient) 
    {
        this.blueLightToPatient = blueLightToPatient;
    }

    /**
     * @return the blueLightToGoal
     */
    public boolean isBlueLightToGoal() 
    {
        return blueLightToGoal;
    }

    /**
     * @param blueLightToGoal the blueLightToGoal to set
     */
    public void setBlueLightToGoal(boolean blueLightToGoal) 
    {
        this.blueLightToGoal = blueLightToGoal;
    }

    /**
     *  df = 'Dienstführender'
     * @return the dfAlarming
     */
    public boolean isDfAlarming() 
    {
        return dfAlarming;
    }

    /**
     * @param dfAlarming the dfAlarming to set
     */
    public void setDfAlarming(boolean dfAlarming) 
    {
        this.dfAlarming = dfAlarming;
    }


    /**
     * @return the brkdtAlarming
     * brkdt = 'Bezirksrettungskommandant'
     */
    public boolean isBrkdtAlarming() 
    {
        return brkdtAlarming;
    }

    /**
     * @param brkdtAlarming the brkdtAlarming to set
     */
    public void setBrkdtAlarming(boolean brkdtAlarming) 
    {
        this.brkdtAlarming = brkdtAlarming;
    }

    /**
     * @return the firebrigadeAlarming
     */
    public boolean isFirebrigadeAlarming() 
    {
        return firebrigadeAlarming;
    }

    /**
     * @param firebrigadeAlarming the firebrigadeAlarming to set
     */
    public void setFirebrigadeAlarming(boolean firebrigadeAlarming) 
    {
        this.firebrigadeAlarming = firebrigadeAlarming;
    }

    /**
     * @return the mountainRescueServiceAlarming
     */
    public boolean isMountainRescueServiceAlarming() 
    {
        return mountainRescueServiceAlarming;
    }

    /**
     * @param mountainRescueServiceAlarming the mountainRescueServiceAlarming to set
     */
    public void setMountainRescueServiceAlarming(
            boolean mountainRescueServiceAlarming) 
    {
        this.mountainRescueServiceAlarming = mountainRescueServiceAlarming;
    }

    /**
     * @return the policeAlarming
     */
    public boolean isPoliceAlarming() 
    {
        return policeAlarming;
    }

    /**
     * @param policeAlarming the policeAlarming to set
     */
    public void setPoliceAlarming(boolean policeAlarming) 
    {
        this.policeAlarming = policeAlarming;
    }

    /**
     * @return the statusMessages
     */
    public ArrayList<StatusMessages> getStatusMessages() 
    {
        return statusMessages;
    }

    /**
     * @return the transportPriority
     * the transport priorities are shown in the ITransportPriority Interface
     */
    public String getTransportPriority() 
    {
        return transportPriority;
    }

    /**
     * @param transportPriority the transportPriority to set
     */
    public void setTransportPriority(String transportPriority) 
    {
        if(transportPriority == null || transportPriority.trim().isEmpty())
            throw new IllegalArgumentException("The transport priority cannot be null");
        this.transportPriority = transportPriority;
    }	
}

