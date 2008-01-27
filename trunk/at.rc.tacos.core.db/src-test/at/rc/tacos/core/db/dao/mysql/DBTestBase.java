package at.rc.tacos.core.db.dao.mysql;

import java.sql.SQLException;
import java.sql.PreparedStatement;

import at.rc.tacos.core.db.DataSource;

/**
 * Base class to privode common methods for the test classes
 * @author Michael
 */
public class DBTestBase
{
    protected void deleteTable(String table)
    {
        final String SQL_DELETE = "DELETE FROM " + table;
        try
        {
            PreparedStatement delteStatement = DataSource.getInstance().getConnection().prepareStatement(SQL_DELETE);
            delteStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Cannot delete from "+table);
        }
    }
}
