package at.rc.tacos.core.db.dao;

import java.sql.SQLException;
import java.util.List;
import at.rc.tacos.model.MobilePhoneDetail;

public interface MobilePhoneDAO
{
    public static final String TABLE_NAME = "phonenumbers";
    public static final String TABLE_DEPENDENT_NAME = "phone_staffmember";
    
	/**
	 * Adds a new mobile phone to the database.
	 * @param phone the mobile phone to add
	 * @return the generated id
	 */
	public int addMobilePhone(MobilePhoneDetail phone) throws SQLException;
	
	/**
	 * Updates the given mobile phone in the databse
	 * @param phone the mobile phone to update
	 * @return true if the update was successfull
	 */
	public boolean updateMobilePhone(MobilePhoneDetail phone) throws SQLException;
	
	/**
	 * Removes the mobile phone from the database
	 * @param id the id of the phone to remove
	 * @return true if the remove was successfull
	 */
	public boolean removeMobilePhone(int id) throws SQLException;
	
	/**
	 * Returns the mobile phone with the specified name.
	 * The name is for example BM01 and is unique
	 * @param mobilePhoneName the name of the phone to get
	 * @return the complete information about the phone or null if nothing found
	 */
	public MobilePhoneDetail getMobilePhoneByName(String mobilePhoneName) throws SQLException;
	
	/**
	 * Returns a list of all stored mobile phones in the databse
	 * @return the list of mobile phones
	 */
	public List<MobilePhoneDetail> listMobilePhones() throws SQLException;
	
	/**
	 * Returns a list of mobile phones of a specific Staffmember
	 * @return a list of mobilephones of a specific staffmember
	 */
	public List<MobilePhoneDetail> listMobilePhonesOfStaffMember(int id) throws SQLException;
}
