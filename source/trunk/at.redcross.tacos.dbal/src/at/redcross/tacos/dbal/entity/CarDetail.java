package at.redcross.tacos.dbal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "CarDetail")
public class CarDetail extends EntityImpl {

	private static final long serialVersionUID = -5201014942036290320L;

	@Id
	@GeneratedValue
	private long id;

	@Column
	private String code;

	@Column
	private Date authorizedFrom;

	@Column
	private Date authorizedUntil;

	@Column
	private String companyName;

	@Column
	private String purposeOfUse;

	@Column
	private String identificationNumber;

	@Column
	private String typeOfVehicle;

	@Column
	private String make;

	@Column
	private String tradeName;

	@Column
	private String variant;

	@Column
	private String buildUp;

	@Column
	private String color;

	@Column
	private int netWeight;

	@Column
	private int techValidTotalVol;

	@Column
	private int maxAllowedTotalWeight;

	@Column
	private int maxAllowedCarryingCapicity;

	@Column
	private int maxAllowedCoupledLoad;

	@Column
	private int maxAllowedBuffLoad;

	@Column
	private String wheelDimensions;

	@Column
	private int seats;

	@Column
	private int standings;

	@Column
	private int maxAllowedAxleload1;

	@Column
	private int maxAllowedAxleload2;

	@Column
	private int maxAllowedAxleload3;

	@Column
	private int o2;

	@Column
	private String motorType;

	@Column
	private String kindOfDrive;

	@Column
	private int limitSpeed;

	@Column
	private int enginePower;

	@Column
	private int cylinderCapacity;

	@Column
	private int enginePowerAtMotorSpeed;

	@Column
	private String sticker;

	@Column
	private String careNotes;

	// ---------------------------------
	// EntityImpl
	// ---------------------------------
	@Override
	public String getDisplayString() {
		return String.valueOf(id);
	}

	// ---------------------------------
	// Object related methods
	// ---------------------------------
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", id).toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id).hashCode();
	}

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
		CarDetail rhs = (CarDetail) obj;
		return new EqualsBuilder().append(id, rhs.id).isEquals();
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setCode(String code) {
		this.code = code;
	}

	public void setAuthorizedFrom(Date authorizedFrom) {
		this.authorizedFrom = authorizedFrom;
	}

	public void setAuthorizedUntil(Date authorizedUntil) {
		this.authorizedUntil = authorizedUntil;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public void setPurposeOfUse(String purposeOfUse) {
		this.purposeOfUse = purposeOfUse;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public void setTypeOfVehicle(String typeOfVehicle) {
		this.typeOfVehicle = typeOfVehicle;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

	public void setVariant(String variant) {
		this.variant = variant;
	}

	public void setBuildUp(String buildUp) {
		this.buildUp = buildUp;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setNetWeight(int netWeight) {
		this.netWeight = netWeight;
	}

	public void setTechValidTotalVol(int techValidTotalVol) {
		this.techValidTotalVol = techValidTotalVol;
	}

	public void setMaxAllowedTotalWeight(int maxAllowedTotalWeight) {
		this.maxAllowedTotalWeight = maxAllowedTotalWeight;
	}

	public void setMaxAllowedCarryingCapicity(int maxAllowedCarryingCapicity) {
		this.maxAllowedCarryingCapicity = maxAllowedCarryingCapicity;
	}

	public void setMaxAllowedCoupledLoad(int maxAllowedCoupledLoad) {
		this.maxAllowedCoupledLoad = maxAllowedCoupledLoad;
	}

	public void setMaxAllowedBuffLoad(int maxAllowedBuffLoad) {
		this.maxAllowedBuffLoad = maxAllowedBuffLoad;
	}

	public void setWheelDimensions(String wheelDimensions) {
		this.wheelDimensions = wheelDimensions;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public void setStandings(int standings) {
		this.standings = standings;
	}

	public void setMaxAllowedAxleload1(int maxAllowedAxleload1) {
		this.maxAllowedAxleload1 = maxAllowedAxleload1;
	}

	public void setMaxAllowedAxleload2(int maxAllowedAxleload2) {
		this.maxAllowedAxleload2 = maxAllowedAxleload2;
	}

	public void setMaxAllowedAxleload3(int maxAllowedAxleload3) {
		this.maxAllowedAxleload3 = maxAllowedAxleload3;
	}

	public void setO2(int o2) {
		this.o2 = o2;
	}

	public void setMotorType(String motorType) {
		this.motorType = motorType;
	}

	public void setKindOfDrive(String kindOfDrive) {
		this.kindOfDrive = kindOfDrive;
	}

	public void setLimitSpeed(int limitSpeed) {
		this.limitSpeed = limitSpeed;
	}

	public void setEnginePower(int enginePower) {
		this.enginePower = enginePower;
	}

	public void setCylinderCapacity(int cylinderCapacity) {
		this.cylinderCapacity = cylinderCapacity;
	}

	public void setEnginePowerAtMotorSpeed(int enginePowerAtMotorSpeed) {
		this.enginePowerAtMotorSpeed = enginePowerAtMotorSpeed;
	}

	public void setSticker(String sticker) {
		this.sticker = sticker;
	}

	public void setCareNotes(String careNotes) {
		this.careNotes = careNotes;
	}

	public String getCode() {
		return code;
	}

	public Date getAuthorizedFrom() {
		return authorizedFrom;
	}

	public Date getAuthorizedUntil() {
		return authorizedUntil;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getPurposeOfUse() {
		return purposeOfUse;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public String getTypeOfVehicle() {
		return typeOfVehicle;
	}

	public String getMake() {
		return make;
	}

	public String getTradeName() {
		return tradeName;
	}

	public String getVariant() {
		return variant;
	}

	public String getBuildUp() {
		return buildUp;
	}

	public String getColor() {
		return color;
	}

	public int getNetWeight() {
		return netWeight;
	}

	public int getTechValidTotalVol() {
		return techValidTotalVol;
	}

	public int getMaxAllowedTotalWeight() {
		return maxAllowedTotalWeight;
	}

	public int getMaxAllowedCarryingCapicity() {
		return maxAllowedCarryingCapicity;
	}

	public int getMaxAllowedCoupledLoad() {
		return maxAllowedCoupledLoad;
	}

	public int getMaxAllowedBuffLoad() {
		return maxAllowedBuffLoad;
	}

	public String getWheelDimensions() {
		return wheelDimensions;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public int getSeats() {
		return seats;
	}

	public int getStandings() {
		return standings;
	}

	public int getMaxAllowedAxleload1() {
		return maxAllowedAxleload1;
	}

	public int getMaxAllowedAxleload2() {
		return maxAllowedAxleload2;
	}

	public int getMaxAllowedAxleload3() {
		return maxAllowedAxleload3;
	}

	public int getO2() {
		return o2;
	}

	public String getMotorType() {
		return motorType;
	}

	public String getKindOfDrive() {
		return kindOfDrive;
	}

	public int getLimitSpeed() {
		return limitSpeed;
	}

	public int getEnginePower() {
		return enginePower;
	}

	public int getCylinderCapacity() {
		return cylinderCapacity;
	}

	public int getEnginePowerAtMotorSpeed() {
		return enginePowerAtMotorSpeed;
	}

	public String getSticker() {
		return sticker;
	}

	public String getCareNotes() {
		return careNotes;
	}

}
