package at.rc.tacos.core.db.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.dao.MobilePhoneDAO;
import at.rc.tacos.model.MobilePhoneDetail;

public class MobilePhoneDAOMySQL implements MobilePhoneDAO
{
	public static final String QUERIES_BUNDLE_PATH = "at.rc.tacos.core.db.queries";

	@Override
	public int addMobilePhone(MobilePhoneDetail phone)
	{
		int phoneId = 0;
		try
		{	
			// phonenumber_ID, phonenumber, phonename
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("insert.phone"));
			query.setInt(1, phone.getId());
			query.setString(2, phone.getMobilePhoneNumber());
			query.setString(3, phone.getMobilePhoneName());
			query.executeUpdate();

			// phonenumber, phonename
			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.phoneID"));
			query1.setString(1, phone.getMobilePhoneNumber());
			query1.setString(2, phone.getMobilePhoneName());
			final ResultSet rs = query1.executeQuery();

			if(rs.first())
				phoneId = rs.getInt("phonenumber_ID");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
		return phoneId;
	}

	@Override
	public MobilePhoneDetail getMobilePhoneByName(String mobilePhoneName)
	{
		MobilePhoneDetail phone = new MobilePhoneDetail();
		try
		{
			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.phoneByName"));
			query1.setString(1, mobilePhoneName);
			final ResultSet rs = query1.executeQuery();

			if(rs.first())
			{
			phone.setId(rs.getInt("phonenumber_ID"));
			phone.setMobilePhoneNumber(rs.getString("phonenumber"));
			phone.setMobilePhoneName(rs.getString("phonename"));
			}
			else return null;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return phone;
	}

	@Override
	public List<MobilePhoneDetail> listMobilePhones()
	{
		List<MobilePhoneDetail> phones = new ArrayList<MobilePhoneDetail>();
		try
		{
			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.phones"));
			final ResultSet rs = query1.executeQuery();

			while(rs.next())
			{
				MobilePhoneDetail phone = new MobilePhoneDetail();
				phone.setId(rs.getInt("phonenumber_ID"));
				phone.setMobilePhoneNumber(rs.getString("phonenumber"));
				phone.setMobilePhoneName(rs.getString("phonename"));
				phones.add(phone);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return phones;
	}

	@Override
	public boolean removeMobilePhone(int id)
	{
    	try
    	{
    		final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("remove.phone"));
    		query.setInt(1, id);
    		query.executeUpdate();
    	}
    	catch (SQLException e)
    	{
    		e.printStackTrace();
    		return false;
    	}
    	return true;
	}

	@Override
	public boolean updateMobilePhone(MobilePhoneDetail phone)
	{
    	try
		{
    	// phonenumber, phonename, phonenumber_ID
    	final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("update.caller"));
		query.setString(1, phone.getMobilePhoneNumber());
		query.setString(2, phone.getMobilePhoneName());
		query.setInt(3, phone.getId());
		
		query.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public List<MobilePhoneDetail> listMobilePhonesOfStaffMember(int id)
	{
		List<MobilePhoneDetail> phones = new ArrayList<MobilePhoneDetail>();
		try
		{
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.PhonenumbersOfMemberID"));
			query.setInt(1, id);
			final ResultSet rs = query.executeQuery();

			while(rs.next())
			{
				MobilePhoneDetail phone = new MobilePhoneDetail();
				phone.setId(rs.getInt("phonenumber_ID"));
				phone.setMobilePhoneNumber(rs.getString("phonenumber"));
				phone.setMobilePhoneName(rs.getString("phonename"));
				phones.add(phone);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return phones;
	}
	
	

}
