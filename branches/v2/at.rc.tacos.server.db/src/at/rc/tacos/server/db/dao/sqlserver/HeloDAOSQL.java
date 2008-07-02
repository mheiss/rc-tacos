package at.rc.tacos.server.db.dao.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.Helo;
import at.rc.tacos.server.db.DbWrapper;
import at.rc.tacos.server.db.SQLQueries;
import at.rc.tacos.server.db.dao.HeloDAO;

public class HeloDAOSQL implements HeloDAO
{
	@Override
	public int addServer(Helo helo) throws SQLException, DAOException 
	{
		Connection connection = DbWrapper.getDefault().getConnection();
		try
		{
			PreparedStatement addStatement = connection.prepareStatement(SQLQueries.getInstance().getStatment("insert.server"));
			
			return 0;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public List<Helo> getServerList() throws SQLException, DAOException 
	{
		return null;
	}

	@Override
	public boolean removeServer(int id) throws SQLException, DAOException 
	{
		return false;
	}

	@Override
	public boolean setAsPrimary(int id) throws SQLException, DAOException 
	{
		return false;
	}
}
