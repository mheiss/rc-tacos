package at.rc.tacos.core.db.dao;

import java.sql.SQLException;
import at.rc.tacos.model.Login;

public interface UserLoginDAO 
{
    public boolean checkLogin(String username,String pwdHash) throws SQLException;
    public Login getLoginAndStaffmember(String username) throws SQLException;
}
