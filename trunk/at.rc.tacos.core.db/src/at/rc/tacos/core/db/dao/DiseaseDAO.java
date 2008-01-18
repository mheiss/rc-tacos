package at.rc.tacos.core.db.dao;

import java.sql.SQLException;

public interface DiseaseDAO
{
	Integer addDisease(String disease) throws SQLException;
	boolean updateDisease(String columnName, String newValue, String diseaseID) throws SQLException;
}
