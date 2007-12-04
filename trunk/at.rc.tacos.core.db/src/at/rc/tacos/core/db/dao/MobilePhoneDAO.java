package at.rc.tacos.core.db.dao;

import java.util.List;
import at.rc.tacos.model.MobilePhoneDetail;

public interface MobilePhoneDAO
{
	public int addMobilePhone(MobilePhoneDetail phone);
	public void updateMobilePhone(MobilePhoneDetail phone);
	public void removeMobilePhone(MobilePhoneDetail phone);
	
	public MobilePhoneDetail getMobilePhoneById(String mobilePhoneId);
	public List<MobilePhoneDetail> listMobilePhones();

//	Integer getPhoneID(String phonenumber)throws SQLException;
//	boolean addPhoneEmployee(int employeeID, int phoneID)throws SQLException;
//	boolean updatePhoneEmployee(String columnName, String newValue, String whereColumn, String whereValue) throws SQLException;
//	boolean deletePhoneEmployee(String whereColumn, String whereValue) throws SQLException;
}
