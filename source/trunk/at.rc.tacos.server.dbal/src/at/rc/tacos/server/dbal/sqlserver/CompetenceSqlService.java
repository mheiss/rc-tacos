package at.rc.tacos.server.dbal.sqlserver;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.platform.model.Competence;
import at.rc.tacos.platform.services.dbal.CompetenceService;

/**
 * Provides CRUD operation for competence.
 * 
 * @author Michael
 */
public class CompetenceSqlService extends BaseSqlService implements CompetenceService {

	@Override
	public int addCompetence(Competence competence) throws SQLException {
		// get the next id
		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.nextCompetenceID"));
		final ResultSet rs = stmt.executeQuery();
		if (!rs.next())
			return -1;

		int id = rs.getInt(1);

		// competence_ID, competence
		final PreparedStatement insertStmt = connection.prepareStatement(queries.getStatment("insert.competence"));
		insertStmt.setInt(1, id);
		insertStmt.setString(2, competence.getCompetenceName());
		if (insertStmt.executeUpdate() == 0)
			return -1;

		return id;
	}

	@Override
	public Competence getCompetenceById(int id) throws SQLException {
		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.competenceByID"));
		stmt.setInt(1, id);
		final ResultSet rs = stmt.executeQuery();
		// no result set
		if (!rs.next())
			return null;

		Competence competence = new Competence();
		competence.setCompetenceName(rs.getString("competence"));
		competence.setId(id);
		return competence;
	}

	@Override
	public List<Competence> listCompetences() throws SQLException {
		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("list.competences"));
		final ResultSet rs = stmt.executeQuery();
		List<Competence> competences = new ArrayList<Competence>();
		// assert we have a result set
		while (rs.next()) {
			Competence competence = new Competence();
			competence.setCompetenceName(rs.getString("competence"));
			competence.setId(rs.getInt("competence_ID"));
			competences.add(competence);
		}
		// return the list
		return competences;
	}

	@Override
	public boolean removeCompetence(int id) throws SQLException {
		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("remove.competence"));
		stmt.setInt(1, id);
		// assert the competence was removed
		if (stmt.executeUpdate() == 0)
			return false;
		return true;
	}

	@Override
	public boolean updateCompetence(Competence competence) throws SQLException {
		// competence where competence_ID
		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("update.competence"));
		stmt.setString(1, competence.getCompetenceName());
		stmt.setInt(2, competence.getId());
		// assert the update was successfully
		if (stmt.executeUpdate() == 0)
			return false;
		return true;
	}

	@Override
	public List<Competence> listCompetencesOfStaffMember(int id) throws SQLException {
		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("list.competenceOfStaffMember"));
		stmt.setInt(1, id);
		final ResultSet rs = stmt.executeQuery();
		// create the returned result list and loop over the database result
		List<Competence> competences = new ArrayList<Competence>();
		while (rs.next()) {
			Competence competence = new Competence();
			competence.setCompetenceName(rs.getString("competence"));
			competence.setId(rs.getInt("competence_ID"));
			competences.add(competence);
		}
		return competences;
	}
}
