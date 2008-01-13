package at.rc.tacos.core.db.dao;

import java.sql.SQLException;

public interface UserLoginDAO 
{
    public boolean checkLogin(String username,String pwdHash) throws SQLException;
}
