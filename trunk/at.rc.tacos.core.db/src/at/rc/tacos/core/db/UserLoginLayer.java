package at.rc.tacos.core.db;

import java.sql.SQLException;

public interface UserLoginLayer 
{
	//userlogin
	boolean addUserlogin(String username, String pwd, String authorization, boolean isloggedin, boolean locked) throws SQLException;
	boolean updateUserlogin(String columnName, String newValue, String username) throws SQLException;
}
