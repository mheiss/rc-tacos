package at.rc.tacos.core.db.dao;

public interface UserLoginDAO 
{
    public void addUserLogin(String username,String password);
    public boolean checkLogin(String username,String password);
}
