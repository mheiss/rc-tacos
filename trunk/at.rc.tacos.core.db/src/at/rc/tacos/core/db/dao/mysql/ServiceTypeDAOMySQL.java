package at.rc.tacos.core.db.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.dao.ServiceTypeDAO;
import at.rc.tacos.model.ServiceType;

public class ServiceTypeDAOMySQL implements ServiceTypeDAO
{
	public static final String QUERIES_BUNDLE_PATH = "at.rc.tacos.core.db.queries";

	@Override
	public int addServiceType(ServiceType serviceType)
	{
		int servicetypeId = -1;
		try
		{	
			// servicetype_ID, servicetype, note
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("insert.servicetype"));
			query.setString(1, serviceType.getServiceName());
			query.executeUpdate();

			//get the last inserted id
			final ResultSet rs = query.getGeneratedKeys();
			if (rs.next()) 
				servicetypeId = rs.getInt(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
		return servicetypeId;
	}

	@Override
	public ServiceType getServiceTypeId(int id)
	{
		ServiceType servicetype = new ServiceType();
		try
		{
			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.servicetypeByID"));
			query1.setInt(1, id);
			final ResultSet rs = query1.executeQuery();

			if(rs.first())
			{
				servicetype.setId(rs.getInt("servicetype_ID"));
				servicetype.setServiceName(rs.getString("servicetype"));
			}
			else return null;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return servicetype;
	}

	@Override
	public List<ServiceType> listServiceTypes()
	{
		List<ServiceType> servicetypes = new ArrayList<ServiceType>();
		try
		{
			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.servicetypes"));
			final ResultSet rs = query1.executeQuery();

			while(rs.next())
			{
				ServiceType servicetype = new ServiceType();
				servicetype.setId(rs.getInt("servicetype_ID"));
				servicetype.setServiceName(rs.getString("servicetype"));
				servicetypes.add(servicetype);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return servicetypes;
	}

	@Override
	public boolean removeServiceType(int id)
	{
		try
		{
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("remove.servicetype"));
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
	public boolean updateServiceType(ServiceType serviceType)
	{
		try
		{
			// servicetype, servicetype_ID
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("update.servicetype"));
			query.setString(1, serviceType.getServiceName());
			query.setInt(2, serviceType.getId());
			query.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
