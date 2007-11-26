package at.rc.tacos.core.db;

import java.sql.SQLException;

public interface PhoneLayer
{
	//phonenumbers
	Integer addPhonenumber(String phonenumber) throws SQLException;
	Integer getPhoneID(String phonenumber)throws SQLException;
	boolean updatePhonenumber(String newValue, int phoneID)throws SQLException;
	boolean deletePhonenumber(int phoneID)throws SQLException;
	boolean addPhoneEmployee(int employeeID, int phoneID)throws SQLException;
	boolean updatePhoneEmployee(String columnName, String newValue, String whereColumn, String whereValue) throws SQLException;
	boolean deletePhoneEmployee(String whereColumn, String whereValue) throws SQLException;
}
