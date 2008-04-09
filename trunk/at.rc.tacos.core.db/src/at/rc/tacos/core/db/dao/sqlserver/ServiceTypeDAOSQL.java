package at.rc.tacos.core.db.dao.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.Queries;
import at.rc.tacos.core.db.dao.ServiceTypeDAO;
import at.rc.tacos.model.ServiceType;

public class ServiceTypeDAOSQL implements ServiceTypeDAO
{
	//The data source to get the connection and the queries file
	private final DataSource source = DataSource.getInstance();
	private final Queries queries = Queries.getInstance();
	
	@Override
	public int addServiceType(ServiceType serviceType) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("insert.servicetype"));
			stmt.setString(1, serviceType.getServiceName());
			stmt.executeUpdate();
			//get the last inserted id
			final ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) 
				return rs.getInt(1);
			//no auto update
			return -1;
		}
		finally
		{
			connection.close();	
		}
	}

	@Override
	public ServiceType getServiceTypeId(int id) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.servicetypeByID"));
			stmt.setInt(1, id);
			final ResultSet rs = stmt.executeQuery();
			
			//assert we have a result
			if(rs.first())
			{
				ServiceType servicetype = new ServiceType();
				servicetype.setId(rs.getInt("servicetype_ID"));
				servicetype.setServiceName(rs.getString("servicetype"));
				return servicetype;
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
	public List<ServiceType> listServiceTypes() throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("list.servicetypes"));
			final ResultSet rs = stmt.executeQuery();
			//create the result list and loop over the result set
			List<ServiceType> servicetypes = new ArrayList<ServiceType>();
			while(rs.next())
			{
				ServiceType servicetype = new ServiceType();
				servicetype.setId(rs.getInt("servicetype_ID"));
				servicetype.setServiceName(rs.getString("servicetype"));
				servicetypes.add(servicetype);
			}
			return servicetypes;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public boolean removeServiceType(int id) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("remove.servicetype"));
			stmt.setInt(1, id);
			//assert the service type is removed
			if(stmt.executeUpdate() == 0)
				return false;
			return true;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public boolean updateServiceType(ServiceType serviceType) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			// updates the servicetype where servicetype_ID
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("update.servicetype"));
			stmt.setString(1, serviceType.getServiceName());
			stmt.setInt(2, serviceType.getId());
			//assert the servicetype is updated
			if(stmt.executeUpdate() == 0)
				return false;
			return true;
		}
		finally
		{
			connection.close();
		}
	}
}