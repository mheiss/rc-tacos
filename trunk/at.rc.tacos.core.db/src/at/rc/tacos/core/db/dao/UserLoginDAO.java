package at.rc.tacos.core.db.dao;

public interface UserLoginDAO 
{
    public boolean checkLogin(String username,String password);
}
