package at.rc.tacos.core.db.dao;

import java.sql.SQLException;
import java.util.List;
import at.rc.tacos.model.*;

public interface DiseaseDAO
{
	Integer addDisease(String disease) throws SQLException;
	boolean updateDisease(String columnName, String newValue, String diseaseID) throws SQLException;
	//List<Disease> listDisease() throws SQLException;
}
