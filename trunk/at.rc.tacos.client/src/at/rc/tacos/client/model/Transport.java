package at.rc.tacos.client.model;

import java.util.Date;



/**
 * Specifies the transport details
 * @author b.thek
 */

public class Transport 
{
	private String fromStreet;
	private String fromNumber;
	private String fromCity;
	private Patient patient; 
	private String toStreet;
	private String toNumber;
	private String toCity;
	private String kindOfTransport;
	private NotifierDetail notifierDetail;
	private boolean backTransport;
	private boolean accompanyingPerson;
	private boolean emergencyPhone;
	private String kindOfIllness;
	private String transportNotes;
	private String responsibleStation;
	private String realStation;
	private Date dateOfTransport;
	private long plannedStartOfTransportTime;
	private long plannedTimeAtPatient;
	private long appointmentTimeAtDestination;
	
	//alerting - TODO as boolean in the form
	private long emergencyDoctoralarmingTime;
	private long helicopterAlarmingTime;
	private boolean blueLightToPatient;
	private boolean blueLightToGoal;
	private long dfAlarmingTime;
	private long brkdtAlarmingTime;
	private long firebrigadeAlarmingTime;
	private long mountainRescueServiceAlarmingTime;
	private long policeAlarmingTime;
	
	private String feedback;
	
	//direction
	private boolean towardsGraz;
	private boolean towardsLeoben;
	private boolean towardsWien;
	private boolean towardsMariazell;
	private boolean towardsDistrict;
	private boolean longDistanceTrip;
	
	
	
	/**
	 * Constructors
	 */
	public Transport(){}


	/**
	 * @param fromStreet
	 * @param fromNumber
	 * @param fromCity
	 * @param patient
	 * @param toStreet
	 * @param toNumber
	 * @param toCity
	 * @param kindOfTransport
	 * @param notifierDetail
	 * @param backTransport
	 * @param accompanyingPerson
	 * @param emergencyPhone
	 * @param kindOfIllness
	 * @param transportNotes
	 * @param responsibleStation
	 * @param realStation
	 * @param dateOfTransport
	 * @param plannedStartOfTransportTime
	 * @param plannedTimeAtPatient
	 * @param appointmentTimeAtDestination
	 * @param emergencyDoctoralarmingTime
	 * @param helicopterAlarmingTime
	 * @param blueLightToPatient
	 * @param blueLightToGoal
	 * @param dfAlarmingTime
	 * @param brkdtAlarmingTime
	 * @param firebrigadeAlarmingTime
	 * @param mountainRescueServiceAlarmingTime
	 * @param policeAlarmingTime
	 * @param feedback
	 * @param towardsGraz
	 * @param towardsLeoben
	 * @param towardsWien
	 * @param towardsMariazell
	 * @param towardsDistrict
	 * @param longDistanceTrip
	 */
	public Transport(String fromStreet, String fromNumber, String fromCity,
			Patient patient, String toStreet, String toNumber, String toCity,
			String kindOfTransport, NotifierDetail notifierDetail,
			boolean backTransport, boolean accompanyingPerson,
			boolean emergencyPhone, String kindOfIllness, String transportNotes,
			String responsibleStation, String realStation, Date dateOfTransport,
			long plannedStartOfTransportTime, long plannedTimeAtPatient,
			long appointmentTimeAtDestination,
			long emergencyDoctoralarmingTime, long helicopterAlarmingTime,
			boolean blueLightToPatient, boolean blueLightToGoal,
			long dfAlarmingTime, long brkdtAlarmingTime,
			long firebrigadeAlarmingTime,
			long mountainRescueServiceAlarmingTime, long policeAlarmingTime,
			String feedback, boolean towardsGraz, boolean towardsLeoben,
			boolean towardsWien, boolean towardsMariazell,
			boolean towardsDistrict, boolean longDistanceTrip) 
	{
		this.fromStreet = fromStreet;
		this.fromNumber = fromNumber;
		this.fromCity = fromCity;
		this.patient = patient;
		this.toStreet = toStreet;
		this.toNumber = toNumber;
		this.toCity = toCity;
		this.kindOfTransport = kindOfTransport;
		this.notifierDetail = notifierDetail;
		this.backTransport = backTransport;
		this.accompanyingPerson = accompanyingPerson;
		this.emergencyPhone = emergencyPhone;
		this.kindOfIllness = kindOfIllness;
		this.transportNotes = transportNotes;
		this.responsibleStation = responsibleStation;
		this.realStation = realStation;
		this.dateOfTransport = dateOfTransport;
		this.plannedStartOfTransportTime = plannedStartOfTransportTime;
		this.plannedTimeAtPatient = plannedTimeAtPatient;
		this.appointmentTimeAtDestination = appointmentTimeAtDestination;
		this.emergencyDoctoralarmingTime = emergencyDoctoralarmingTime;
		this.helicopterAlarmingTime = helicopterAlarmingTime;
		this.blueLightToPatient = blueLightToPatient;
		this.blueLightToGoal = blueLightToGoal;
		this.dfAlarmingTime = dfAlarmingTime;
		this.brkdtAlarmingTime = brkdtAlarmingTime;
		this.firebrigadeAlarmingTime = firebrigadeAlarmingTime;
		this.mountainRescueServiceAlarmingTime = mountainRescueServiceAlarmingTime;
		this.policeAlarmingTime = policeAlarmingTime;
		this.feedback = feedback;
		this.towardsGraz = towardsGraz;
		this.towardsLeoben = towardsLeoben;
		this.towardsWien = towardsWien;
		this.towardsMariazell = towardsMariazell;
		this.towardsDistrict = towardsDistrict;
		this.longDistanceTrip = longDistanceTrip;
	}



	/**
	 * Getter&Setter
	 */
	
	/**
	 * @return the fromStreet
	 */
	public String getFromStreet() {
		return fromStreet;
	}


	/**
	 * @param fromStreet the fromStreet to set
	 */
	public void setFromStreet(String fromStreet) {
		this.fromStreet = fromStreet;
	}


	/**
	 * @return the fromNumber
	 * Contains: street number, floor, number of the flat if available
	 */
	public String getFromNumber() {
		return fromNumber;
	}


	/**
	 * @param fromNumber the fromNumber to set
	 */
	public void setFromNumber(String fromNumber) {
		this.fromNumber = fromNumber;
	}


	/**
	 * @return the fromCity
	 */
	public String getFromCity() {
		return fromCity;
	}


	/**
	 * @param fromCity the fromCity to set
	 */
	public void setFromCity(String fromCity) {
		this.fromCity = fromCity;
	}


	/**
	 * @return the patient
	 */
	public Patient getPatient() {
		return patient;
	}


	/**
	 * @param patient the patient to set
	 */
	public void setPatient(Patient patient) {
		this.patient = patient;
	}


	/**
	 * @return the toStreet
	 */
	public String getToStreet() {
		return toStreet;
	}


	/**
	 * @param toStreet the toStreet to set
	 */
	public void setToStreet(String toStreet) {
		this.toStreet = toStreet;
	}


	/**
	 * @return the toNumber
	 */
	public String getToNumber() {
		return toNumber;
	}


	/**
	 * @param toNumber the toNumber to set
	 */
	public void setToNumber(String toNumber) {
		this.toNumber = toNumber;
	}


	/**
	 * @return the toCity
	 */
	public String getToCity() {
		return toCity;
	}


	/**
	 * @param toCity the toCity to set
	 */
	public void setToCity(String toCity) {
		this.toCity = toCity;
	}


	/**
	 * @return the kindOfTransport
	 * Possible: 'gehend', 'sitzend', 'liegend', Rollstuhl'
	 */
	public String getKindOfTransport() {
		return kindOfTransport;
	}


	/**
	 * @param kindOfTransport the kindOfTransport to set
	 */
	public void setKindOfTransport(String kindOfTransport) {
		this.kindOfTransport = kindOfTransport;
	}


	/**
	 * @return the notifierDetail
	 */
	public NotifierDetail getNotifierDetail() {
		return notifierDetail;
	}


	/**
	 * @param notifierDetail the notifierDetail to set
	 */
	public void setNotifierDetail(NotifierDetail notifierDetail) {
		this.notifierDetail = notifierDetail;
	}


	/**
	 * @return the backTransport
	 */
	public boolean isBackTransport() {
		return backTransport;
	}


	/**
	 * @param backTransport the backTransport to set
	 */
	public void setBackTransport(boolean backTransport) {
		this.backTransport = backTransport;
	}


	/**
	 * @return the accompanyingPerson
	 */
	public boolean isAccompanyingPerson() {
		return accompanyingPerson;
	}


	/**
	 * @param accompanyingPerson the accompanyingPerson to set
	 */
	public void setAccompanyingPerson(boolean accompanyingPerson) {
		this.accompanyingPerson = accompanyingPerson;
	}


	/**
	 * @return the emergencyPhone
	 * explanation emergency phone: the patient wear a chip on the body which is connected to a special phone to alert the red cross
	 * sometimes a key for the door is deposited at the red cross station or the police station
	 */
	public boolean isEmergencyPhone() {
		return emergencyPhone;
	}


	/**
	 * @param emergencyPhone the emergencyPhone to set
	 */
	public void setEmergencyPhone(boolean emergencyPhone) {
		this.emergencyPhone = emergencyPhone;
	}


	/**
	 * @return the kindOfIllness
	 */
	public String getKindOfIllness() {
		return kindOfIllness;
	}


	/**
	 * @param kindOfIllness the kindOfIllness to set
	 */
	public void setKindOfIllness(String kindOfIllness) {
		this.kindOfIllness = kindOfIllness;
	}


	/**
	 * @return the transportNotes
	 */
	public String getTransportNotes() {
		return transportNotes;
	}


	/**
	 * @param transportNotes the transportNotes to set
	 */
	public void setTransportNotes(String transportNotes) {
		this.transportNotes = transportNotes;
	}


	/**
	 * @return the responsibleStation
	 * The hole operational area is divided into six stations (see RosterEntry.java)
	 * Primary for each location within the operational area one defined station is responsible
	 */
	public String getResponsibleStation() {
		return responsibleStation;
	}


	/**
	 * @param responsibleStation the responsibleStation to set
	 */
	public void setResponsibleStation(String responsibleStation) {
		this.responsibleStation = responsibleStation;
	}
	
	/**
	 * @return the realStation
	 * The primary responsible station is not all the time the station which execute the transport
	 */
	public String getRealStation() {
		return realStation;
	}


	/**
	 * @param realStation the realStation to set
	 */
	public void setRealStation(String realStation) {
		this.realStation = realStation;
	}


	/**
	 * @return the dateOfTransport
	 */
	public Date getDateOfTransport() {
		return dateOfTransport;
	}


	/**
	 * @param dateOfTransport the dateOfTransport to set
	 */
	public void setDateOfTransport(Date dateOfTransport) {
		this.dateOfTransport = dateOfTransport;
	}


	/**
	 * @return the plannedStartOfTransportTime
	 */
	public long getPlannedStartOfTransportTime() {
		return plannedStartOfTransportTime;
	}


	/**
	 * @param plannedStartOfTransportTime the plannedStartOfTransportTime to set
	 */
	public void setPlannedStartOfTransportTime(long plannedStartOfTransportTime) {
		this.plannedStartOfTransportTime = plannedStartOfTransportTime;
	}


	/**
	 * @return the plannedTimeAtPatient
	 */
	public long getPlannedAtPatientTime() {
		return plannedTimeAtPatient;
	}


	/**
	 * @param plannedTimeAtPatient the plannedTimeAtPatient to set
	 */
	public void setPlannedAtPatientTime(long plannedTimeAtPatient) {
		this.plannedTimeAtPatient = plannedTimeAtPatient;
	}


	/**
	 * @return the appointmentTimeAtDestination
	 */
	public long getAppointmentTimeAtDestination() {
		return appointmentTimeAtDestination;
	}


	/**
	 * @param appointmentTimeAtDestination the appointmentTimeAtDestination to set
	 */
	public void setAppointmentTimeAtDestination(long appointmentTimeAtDestination) {
		this.appointmentTimeAtDestination = appointmentTimeAtDestination;
	}


	/**
	 * @return the emergencyDoctoralarmingTime
	 */
	public long getEmergencyDoctorAlarmingTime() {
		return emergencyDoctoralarmingTime;
	}


	/**
	 * @param emergencyDoctoralarmingTime the emergencyDoctoralarmingTime to set
	 */
	public void setEmergencyDoctorAlarmingTime(long emergencyDoctoralarmingTime) {
		this.emergencyDoctoralarmingTime = emergencyDoctoralarmingTime;
	}


	/**
	 * @return the helicopterAlarmingTime
	 */
	public long getHelicopterAlarmingTime() {
		return helicopterAlarmingTime;
	}


	/**
	 * @param helicopterAlarmingTime the helicopterAlarmingTime to set
	 */
	public void setHelicopterAlarmingTime(long helicopterAlarmingTime) {
		this.helicopterAlarmingTime = helicopterAlarmingTime;
	}


	/**
	 * @return the blueLightToPatient
	 * Named: 'BD1'
	 */
	public boolean isBluelightToPatient() {
		return blueLightToPatient;
	}


	/**
	 * @param blueLightToPatient the blueLightToPatient to set
	 */
	public void setBluelightToPatient(boolean blueLightToPatient) {
		this.blueLightToPatient = blueLightToPatient;
	}


	/**
	 * @return the blueLightToGoal
	 * Named: 'BD2'
	 */
	public boolean isBluelightToGoal() {
		return blueLightToGoal;
	}


	/**
	 * @param blueLightToGoal the blueLightToGoal to set
	 */
	public void setBluelightToGoal(boolean blueLightToGoal) {
		this.blueLightToGoal = blueLightToGoal;
	}


	/**
	 * @return the dfAlarmingTime
	 * df='Dienstführender'
	 */
	public long getDfAlarmingTime() {
		return dfAlarmingTime;
	}


	/**
	 * @param dfAlarmingTime the dfAlarmingTime to set
	 */
	public void setDfAlarmingTime(long dfAlarmingTime) {
		this.dfAlarmingTime = dfAlarmingTime;
	}


	/**
	 * @return the brkdtAlarmingTime
	 * brkdt='Bezirksrettungskommandant'
	 */
	public long getBrkdtAlarmingTime() {
		return brkdtAlarmingTime;
	}


	/**
	 * @param brkdtAlarmingTime the brkdtAlarmingTime to set
	 */
	public void setBrkdtAlarmingTime(long brkdtAlarmingTime) {
		this.brkdtAlarmingTime = brkdtAlarmingTime;
	}


	/**
	 * @return the firebrigadeAlarmingTime
	 */
	public long getFirebrigadeAlarmingTime() {
		return firebrigadeAlarmingTime;
	}


	/**
	 * @param firebrigadeAlarmingTime the firebrigadeAlarmingTime to set
	 */
	public void setFirebrigadeAlarmingTime(long firebrigadeAlarmingTime) {
		this.firebrigadeAlarmingTime = firebrigadeAlarmingTime;
	}


	/**
	 * @return the mountainRescueServiceAlarmingTime
	 */
	public long getMountainRescueServiceAlarmingTime() {
		return mountainRescueServiceAlarmingTime;
	}


	/**
	 * @param mountainRescueServiceAlarmingTime the mountainRescueServiceAlarmingTime to set
	 */
	public void setMountainRescueServiceAlarmingTime(
			long mountainRescueServiceAlarmingTime) {
		this.mountainRescueServiceAlarmingTime = mountainRescueServiceAlarmingTime;
	}


	/**
	 * @return the policeAlarmingTime
	 */
	public long getPoliceAlarmingTime() {
		return policeAlarmingTime;
	}


	/**
	 * @param policeAlarmingTime the policeAlarmingTime to set
	 */
	public void setPoliceAlarmingTime(long policeAlarmingTime) {
		this.policeAlarmingTime = policeAlarmingTime;
	}


	/**
	 * @return the feedback
	 */
	public String getFeedback() {
		return feedback;
	}


	/**
	 * @param feedback the feedback to set
	 */
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}


	/**
	 * @return the towardsGraz
	 */
	public boolean isTowardsGraz() {
		return towardsGraz;
	}


	/**
	 * @param towardsGraz the towardsGraz to set
	 */
	public void setTowardsGraz(boolean towardsGraz) {
		this.towardsGraz = towardsGraz;
	}


	/**
	 * @return the towardsLeoben
	 */
	public boolean isTowardsLeoben() {
		return towardsLeoben;
	}


	/**
	 * @param towardsLeoben the towardsLeoben to set
	 */
	public void setTowardsLeoben(boolean towardsLeoben) {
		this.towardsLeoben = towardsLeoben;
	}


	/**
	 * @return the towardsWien
	 */
	public boolean isTowardsWien() {
		return towardsWien;
	}


	/**
	 * @param towardsWien the towardsWien to set
	 */
	public void setTowardsWien(boolean towardsWien) {
		this.towardsWien = towardsWien;
	}


	/**
	 * @return the towardsMariazell
	 */
	public boolean isTowardsMariazell() {
		return towardsMariazell;
	}


	/**
	 * @param towardsMariazell the towardsMariazell to set
	 */
	public void setTowardsMariazell(boolean towardsMariazell) {
		this.towardsMariazell = towardsMariazell;
	}


	/**
	 * @return the towardsDistrict
	 */
	public boolean isTowardsDistrict() {
		return towardsDistrict;
	}


	/**
	 * @param towardsDistrict the towardsDistrict to set
	 */
	public void setTowardsDistrict(boolean towardsDistrict) {
		this.towardsDistrict = towardsDistrict;
	}


	/**
	 * @return the longDistanceTrip
	 */
	public boolean isLongDistanceTrip() {
		return longDistanceTrip;
	}


	/**
	 * @param longDistanceTrip the longDistanceTrip to set
	 */
	public void setLongDistanceTrip(boolean longDistanceTrip) {
		this.longDistanceTrip = longDistanceTrip;
	}	
}

