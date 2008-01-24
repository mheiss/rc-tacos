package at.rc.tacos.core.db.dao.mysql;

import java.util.List;

import at.rc.tacos.core.db.dao.DiseaseDAO;
import at.rc.tacos.model.Disease;

public class DiseaseDAOMySQL implements DiseaseDAO
{
	public static final String QUERIES_BUNDLE_PATH = "at.rc.tacos.core.db.queries";

	@Override
	public int addDisease(Disease disease) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Disease> getDiseaseList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeDisease(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateDisease(Disease disease) {
		// TODO Auto-generated method stub
		return false;
	}


}
