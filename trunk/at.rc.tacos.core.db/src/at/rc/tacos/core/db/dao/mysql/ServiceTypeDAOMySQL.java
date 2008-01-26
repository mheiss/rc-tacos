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
		int servicetypeId = 0;
		try
		{	
			// servicetype_ID, servicetype, note
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("insert.servicetype"));
			query.setInt(1, serviceType.getId());
			query.setString(2, serviceType.getServiceName());
			query.executeUpdate();

			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.servicetypeID"));
			query1.setString(1, serviceType.getServiceName());
			final ResultSet rs = query1.executeQuery();

			if(rs.first())
				servicetypeId = rs.getInt("servicetype_ID");
			else
				return -1;
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

			rs.first();
			servicetype.setId(rs.getInt("servicetype_ID"));
			servicetype.setServiceName(rs.getString("servicetype"));
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
			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.servicetypeByID"));
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
