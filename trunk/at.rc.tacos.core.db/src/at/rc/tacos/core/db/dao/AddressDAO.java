package at.rc.tacos.core.db.dao;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.model.Address;

public interface AddressDAO
{
    public static final String TABLE_NAME = "address";
    
	/**
	 * Adds a new address into the database and returns the generated id
	 * @param address the address to add
	 * @return the id of the new address
	 */
	public int addAddress(Address address) throws SQLException;
	
	/**
	 * Updates the given address in the database
	 * @param address the address to update
	 * @return true if the update was successfully
	 */
	public boolean updateAddress(Address address) throws SQLException;
	
	/**
	 * Removes the address from the database
	 * @param id the address to remove
	 * @return true if the removal was successfully
	 */
	public boolean removeAddress(int id) throws SQLException;
	
	/**
	 * Returns a list of all addresses in the database if one of the fields contains the given string
	 * @return the address list with matched entries
	 */
	public List<Address> getAddressList(String searchString) throws SQLException;
	
	/**
	 * Returns a specific id identified by the address id
	 * @param id the id of the address to get
	 * @return the address or null if no address with this id was found
	 */
	public Address getAddress(int id) throws SQLException;
}
