package at.rc.tacos.core.db;

import java.sql.SQLException;

public interface CallerLayer 
{
	
	//Caller
	Integer addCaller(String callername, String caller_phonenumber, String caller_note) throws SQLException;
//	Caller getCaller(String callerID) throws SQLException;
	boolean updateCaller(String columnName, String newValue, String callerID) throws SQLException;
//	List<Caller> listCallers(String order, String orderSequence) throws SQLException;
	

}
