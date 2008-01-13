package at.rc.tacos.core.db.dao.mysql;

import java.sql.SQLException;

import at.rc.tacos.core.db.dao.DiseaseDAO;

public class DiseaseDAOMySQL implements DiseaseDAO
{
	public static final String QUERIES_BUNDLE_PATH = "at.rc.tacos.core.db.queries";

	@Override
	public Integer addDisease(String disease) throws SQLException {
		// TODO vorher Disease Objekt anlegen
		return null;
	}

	@Override
	public boolean updateDisease(String columnName, String newValue,
			String diseaseID) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}


}
