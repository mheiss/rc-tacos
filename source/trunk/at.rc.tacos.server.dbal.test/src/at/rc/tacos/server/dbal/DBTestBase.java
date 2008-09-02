package at.rc.tacos.server.dbal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Base class to privode common methods for the test classes
 * @author Michael
 */
public class DBTestBase
{
	//The data source to get the connection
	private final DataSource source = DataSource.getInstance();
	
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
