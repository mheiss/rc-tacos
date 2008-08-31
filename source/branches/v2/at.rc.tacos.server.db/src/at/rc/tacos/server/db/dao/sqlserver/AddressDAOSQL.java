package at.rc.tacos.server.db.dao.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.model.*;
import at.rc.tacos.server.db.DbWrapper;
import at.rc.tacos.server.db.SQLQueries;
import at.rc.tacos.server.db.dao.AddressDAO;

public class AddressDAOSQL implements AddressDAO
{
	//The data source to get the connection and the queries file
	private final DbWrapper source = DbWrapper.getDefault();
	private final SQLQueries queries = SQLQueries.getInstance();

	@Override
	public Address getAddress(int addressID) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			//SELECT a.address_ID, a.street, a.streetnumber, a.city, a.gkz \
			//FROM address a \
			//WHERE a.address_ID = ?;
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("get.addressByID"));
			query.setInt(1, addressID);
			final ResultSet rs = query.executeQuery();
			//assert we have a result
			if(rs.next())
			{
				Address address = new Address();
				address.setStreet(rs.getString("street"));
				address.setStreetNumber(rs.getString("streetnumber"));
				address.setCity(rs.getString("city"));
				address.setZip(rs.getInt("gkz"));

				return address;
			}
			//no result
			return null;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public int addAddress(Address address) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{	
			int id = 0;
			//get the next id
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.nextAddressID"));
			final ResultSet rs = stmt.executeQuery();
			if(!rs.next())
				return -1;

			id = rs.getInt(1);


			//INSERT INTO address(address_ID, street, streetnumber, city, gkz) \
			//VALUES(?, ?, ?, ?, ?);
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("insert.address"));
			query.setInt(1, id);
			query.setString(2, address.getStreet());
			query.setString(3, address.getStreetNumber());
			query.setString(4, address.getCity());
			query.setInt(5, address.getZip());

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
	public List<Address> getAddressList(String streetFilter,String streetNumberFilter,String cityFilter, String plzFilter) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			//SELECT a.address_ID,a.street, a.streetnumber, a.city, a.gkz 
			//FROM address a \
			//WHERE a.street like ? or a.streetnumber like ? or a.city like ? or a.gkz like ?;
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.addressesBySearchString"));
			query.setString(1, streetFilter);
			query.setString(2, streetNumberFilter);
			query.setString(3, cityFilter);
			query.setString(4, plzFilter);

			final ResultSet rs = query.executeQuery();
			//assert we have a result
			List<Address> addresses = new ArrayList<Address>();
			while(rs.next())
			{
				Address address = new Address();
				address.setAddressId(rs.getInt("address_ID"));
				address.setStreet(rs.getString("street"));
				address.setStreetNumber(rs.getString("streetnumber"));
				address.setCity(rs.getString("city"));
				address.setZip(rs.getInt("gkz"));

				addresses.add(address);
			}
			return addresses;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public boolean removeAddress(int id) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("delete.address"));
			query.setInt(1, id);
			//assert the location removed was successfully
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
	public boolean updateAddress(Address address) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			//UPDATE address SET street = ?, streetnumber = ?, city = ?, gkz = ? WHERE address_ID = ?;
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("update.address"));
			query.setString(1, address.getStreet());
			query.setString(2, address.getStreetNumber());
			query.setString(3, address.getCity());
			query.setInt(4, address.getZip());
			query.setInt(5, address.getAddressId());
			//assert the update was successfully
			if(query.executeUpdate() == 0)
				return false;
			return true;
		}
		finally
		{
			connection.close();
		}
	}
}
