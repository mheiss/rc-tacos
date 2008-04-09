package at.rc.tacos.core.db.dao.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.SQLQueries;
import at.rc.tacos.core.db.dao.MobilePhoneDAO;
import at.rc.tacos.model.MobilePhoneDetail;

public class MobilePhoneDAOSQL implements MobilePhoneDAO
{
	//The data source to get the connection and the queries file
	private final DataSource source = DataSource.getInstance();
	private final SQLQueries queries = SQLQueries.getInstance();

	@Override
	public int addMobilePhone(MobilePhoneDetail phone) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{	
			int id = 0;
			//get the next id
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.nextPhoneID"));
			final ResultSet rs = stmt.executeQuery();
			if(!rs.next())
				return -1;
			id = rs.getInt(1);
			
			// phonenumber_ID, phonenumber, phonename
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("insert.phone"));
			query.setInt(1, id);
			query.setString(2, phone.getMobilePhoneNumber());
			query.setString(3, phone.getMobilePhoneName());
			if(query.executeUpdate() == 0)
				return -1;

			return id;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public MobilePhoneDetail getMobilePhoneByName(String mobilePhoneName) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("get.phoneByName"));
			query.setString(1, mobilePhoneName);
			final ResultSet rs = query.executeQuery();
			//assert we have a result set
			if(rs.first())
			{
				MobilePhoneDetail phone = new MobilePhoneDetail();
				phone.setId(rs.getInt("phonenumber_ID"));
				phone.setMobilePhoneNumber(rs.getString("phonenumber"));
				phone.setMobilePhoneName(rs.getString("phonename"));
				return phone;
			}
			//no result set
			return null;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public List<MobilePhoneDetail> listMobilePhones() throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.phones"));
			final ResultSet rs = query.executeQuery();
			//create the result list and loop over the result
			List<MobilePhoneDetail> phones = new ArrayList<MobilePhoneDetail>();
			while(rs.next())
			{
				MobilePhoneDetail phone = new MobilePhoneDetail();
				phone.setId(rs.getInt("phonenumber_ID"));
				phone.setMobilePhoneNumber(rs.getString("phonenumber"));
				phone.setMobilePhoneName(rs.getString("phonename"));
				phones.add(phone);
			}
			return phones;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public boolean removeMobilePhone(int id) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("remove.phone"));
			query.setInt(1, id);
			//assert the phone was removed
			if(query.executeUpdate() == 0)
				return false;
			return true;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public boolean updateMobilePhone(MobilePhoneDetail phone) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			// phonenumber, phonename, phonenumber_ID
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("update.Phonenumber"));
			query.setString(1, phone.getMobilePhoneNumber());
			query.setString(2, phone.getMobilePhoneName());
			query.setInt(3, phone.getId());
			//check if the update was successfully
			if(query.executeUpdate() == 0)
				return false;
			return true;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public List<MobilePhoneDetail> listMobilePhonesOfStaffMember(int id) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.PhonenumbersOfMemberID"));
			query.setInt(1, id);
			final ResultSet rs = query.executeQuery();
			//create a list and loop over the result
			List<MobilePhoneDetail> phones = new ArrayList<MobilePhoneDetail>();
			while(rs.next())
			{
				MobilePhoneDetail phone = new MobilePhoneDetail();
				phone.setId(rs.getInt("phonenumber_ID"));
				phone.setMobilePhoneNumber(rs.getString("phonenumber"));
				phone.setMobilePhoneName(rs.getString("phonename"));
				phones.add(phone);
			}
			return phones;
		}
		finally
		{
			connection.close();
		}
	}
}