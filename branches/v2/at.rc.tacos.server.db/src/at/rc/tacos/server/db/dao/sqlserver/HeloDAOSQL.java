package at.rc.tacos.server.db.dao.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.Helo;
import at.rc.tacos.server.db.DbWrapper;
import at.rc.tacos.server.db.SQLQueries;
import at.rc.tacos.server.db.dao.HeloDAO;

public class HeloDAOSQL implements HeloDAO
{
	//The data source to get the connection and the queries file
	private final DbWrapper source = DbWrapper.getDefault();
	private final SQLQueries queries = SQLQueries.getInstance();

	@Override
	public int addServer(Helo helo) throws SQLException, DAOException 
	{
		Connection connection = source.getConnection();
		try
		{
			int id = 0;
			//get the next id
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.nextServerId"));
			final ResultSet rs = stmt.executeQuery();
			if(!rs.next())
				return -1;

			id = rs.getInt(1);

			PreparedStatement addStatement = connection.prepareStatement(SQLQueries.getInstance().getStatment("insert.server"));
			addStatement.setInt(1, id);
			addStatement.setString(2, helo.getServerIp());
			addStatement.setInt(3, helo.getServerPort());
			addStatement.setBoolean(4, false);

			return id;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public List<Helo> getServerList() throws SQLException, DAOException 
	{
		Connection connection = source.getConnection();
		try
		{
			PreparedStatement listStatement = connection.prepareStatement(SQLQueries.getInstance().getStatment("list.servers"));
			ResultSet rs = listStatement.executeQuery();

			//the result list of servers
			List<Helo> serverList = new ArrayList<Helo>();
			while(rs.next())
			{
				Helo helo = new Helo();
				helo.setId(rs.getInt("ID"));
				helo.setServerIp(rs.getString("IP_ADDRESSE"));
				helo.setServerPort(rs.getInt("PORT"));
				helo.setServerPrimary(rs.getBoolean("PRIMARY_SERVER"));

				//add to the list
				serverList.add(helo);
			}
			return serverList;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public boolean removeServer(int id) throws SQLException, DAOException 
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("delete.server"));
			stmt.setInt(1, id);
			//assert the server is removed
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
	public boolean setAsPrimary(int id) throws SQLException, DAOException 
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("update.setPrimary"));
			stmt.setBoolean(1, true);
			stmt.setInt(2, id);
			//assert the server is set as primary
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
