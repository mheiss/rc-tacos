package at.rc.tacos.core.db.dao.sqlserver;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import at.rc.tacos.server.db.DbWrapper;

/**
 * Base class to privode common methods for the test classes
 * @author Michael
 */
public class DBTestBase
{
	//The data source to get the connection
	private final DbWrapper source = DbWrapper.getDefault();
	
    protected void deleteTable(String table) throws SQLException
    {
    	Connection connection = source.getConnection();
        final String SQL_DELETE = "DELETE FROM " + table;
        try
        {
            PreparedStatement delteStatement = connection.prepareStatement(SQL_DELETE);
            delteStatement.executeUpdate();
        }
        finally
        {
        	connection.close();
        }
    }
}
