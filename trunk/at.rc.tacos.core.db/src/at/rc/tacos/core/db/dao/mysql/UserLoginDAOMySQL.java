package at.rc.tacos.core.db.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.dao.UserLoginDAO;

public class UserLoginDAOMySQL implements UserLoginDAO
{
	public static final String QUERIES_BUNDLE_PATH = "at.rc.tacos.core.db.queries";

	@Override
	public boolean checkLogin(String username, String pwdHash)
	{
		try
		{
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("check.UserLogin"));
			query.setString(1, username);
			query.setString(2, pwdHash);
			final ResultSet rs = query.executeQuery();

			rs.first();
			if (rs.getString("username") != null)
				return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
}