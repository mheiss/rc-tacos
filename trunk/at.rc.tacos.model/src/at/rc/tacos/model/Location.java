package at.rc.tacos.model;

/**
 * This class represents a location.
 * @author Michael
 */
public class Location
{
    private int location_ID;
    private String locationname;
    private String street;
    private String streetnumber;
    private int zipcode;
    private String city;
    private String note;
    private int phonenumber_ID;
    private String phonenumber;
    
    
	public int getLocation_ID() {
		return location_ID;
	}
	public void setLocation_ID(int location_ID) {
		this.location_ID = location_ID;
	}
	public String getLocationname() {
		return locationname;
	}
	public void setLocationname(String locationname) {
		this.locationname = locationname;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getStreetnumber() {
		return streetnumber;
	}
	public void setStreetnumber(String streetnumber) {
		this.streetnumber = streetnumber;
	}
	public int getZipcode() {
		return zipcode;
	}
	public void setZipcode(int zipcode) {
		this.zipcode = zipcode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public int getPhonenumber_ID() {
		return phonenumber_ID;
	}
	public void setPhonenumber_ID(int phonenumber_ID) {
		this.phonenumber_ID = phonenumber_ID;
	}
	public String getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
    
    
}
