package at.rc.tacos.server.dbal.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import at.rc.tacos.platform.model.ServiceType;
import at.rc.tacos.platform.services.dbal.ServiceTypeService;
import at.rc.tacos.server.dbal.SQLQueries;

/**
 * Provides CRUD operation for service type.
 * 
 * @author Michael
 */
public class ServiceTypeSqlService implements ServiceTypeService {

	@Resource(name = "sqlConnection")
	protected Connection connection;

	// the source for the queries
	protected final SQLQueries queries = SQLQueries.getInstance();

	@Override
	public int addServiceType(ServiceType serviceType) throws SQLException {
		// get the next id
		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.nextServicetypeID"));
		final ResultSet rs = stmt.executeQuery();
		if (!rs.next())
			return -1;

		int id = rs.getInt(1);

		// servicetype_ID, servicetype
		final PreparedStatement insertstmt = connection.prepareStatement(queries.getStatment("insert.servicetype"));
		insertstmt.setInt(1, id);
		insertstmt.setString(2, serviceType.getServiceName());
		if (insertstmt.executeUpdate() == 0)
			return -1;

		return id;
	}

	@Override
	public boolean removeServiceType(int id) throws SQLException {
		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("remove.servicetype"));
		stmt.setInt(1, id);
		// assert the service type is removed
		if (stmt.executeUpdate() == 0)
			return false;
		return true;
	}

	@Override
	public boolean updateServiceType(ServiceType serviceType) throws SQLException {
		// updates the servicetype where servicetype_ID
		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("update.servicetype"));
		stmt.setString(1, serviceType.getServiceName());
		stmt.setInt(2, serviceType.getId());
		// assert the servicetype is updated
		if (stmt.executeUpdate() == 0)
			return false;
		return true;
	}

	@Override
	public ServiceType getServiceTypeId(int id) throws SQLException {
		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.servicetypeByID"));
		stmt.setInt(1, id);
		final ResultSet rs = stmt.executeQuery();

		// no result set
		if (!rs.next())
			return null;

		return setupServiceType(rs);
	}

	@Override
	public List<ServiceType> listServiceTypesByName(String name) throws SQLException {
		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("list.servicetypesByName"));
		stmt.setString(1, name);
		final ResultSet rs = stmt.executeQuery();
		return setupServiceList(rs);
	}

	@Override
	public List<ServiceType> listServiceTypes() throws SQLException {
		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("list.servicetypes"));
		final ResultSet rs = stmt.executeQuery();
		// create the result list and loop over the result set
		return setupServiceList(rs);
	}

	/**
	 * Helper method to setup a list of service types
	 */
	private List<ServiceType> setupServiceList(ResultSet rs) throws SQLException {
		List<ServiceType> serviceList = new ArrayList<ServiceType>();
		while (rs.next()) {
			ServiceType serviceType = setupServiceType(rs);
			serviceList.add(serviceType);
		}
		return serviceList;
	}

	/**
	 * Helper method to setup a single service type
	 */
	private ServiceType setupServiceType(ResultSet rs) throws SQLException {
		ServiceType servicetype = new ServiceType();
		servicetype.setId(rs.getInt("servicetype_ID"));
		servicetype.setServiceName(rs.getString("servicetype"));
		return servicetype;
	}
}
