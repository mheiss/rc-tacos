package at.rc.tacos.server.dbal.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import at.rc.tacos.platform.model.Disease;
import at.rc.tacos.platform.services.dbal.DiseaseService;
import at.rc.tacos.server.dbal.SQLQueries;

/**
 * Provides CRUD operation for caller details.
 * 
 * @author Michael
 */
public class DiseaseSqlService implements DiseaseService {
	
	@Resource(name = "sqlConnection")
	protected Connection connection;

	// the source for the queries
	protected final SQLQueries queries = SQLQueries.getInstance();

	@Override
	public int addDisease(Disease disease) throws SQLException {
		int id = 0;
		// get the next id
		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.nextDiseaseID"));
		final ResultSet rs = stmt.executeQuery();
		if (!rs.next())
			return -1;

		id = rs.getInt(1);
		// disease name
		final PreparedStatement insertstmt = connection.prepareStatement(queries.getStatment("insert.disease"));
		insertstmt.setInt(1, id);
		insertstmt.setString(2, disease.getDiseaseName());

		if (insertstmt.executeUpdate() == 0)
			return -1;

		return id;
	}

	@Override
	public List<Disease> getDiseaseList() throws SQLException {
		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("list.diseases"));
		final ResultSet rs = stmt.executeQuery();
		// create the result list and loop over the result set
		List<Disease> diseases = new ArrayList<Disease>();
		while (rs.next()) {
			Disease disease = new Disease();
			disease.setId(rs.getInt("disease_ID"));
			disease.setDiseaseName(rs.getString("disease"));
			diseases.add(disease);
		}
		return diseases;
	}

	@Override
	public boolean removeDisease(int id) throws SQLException {
		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("delete.disease"));
		stmt.setInt(1, id);
		// assert the disease is removed
		if (stmt.executeUpdate() == 0)
			return false;
		return true;
	}

	@Override
	public boolean updateDisease(Disease disease) throws SQLException {
		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("update.disease"));
		stmt.setString(1, disease.getDiseaseName());
		stmt.setInt(2, disease.getId());
		// assert the update was successfully
		if (stmt.executeUpdate() == 0)
			return false;
		return true;
	}
}
