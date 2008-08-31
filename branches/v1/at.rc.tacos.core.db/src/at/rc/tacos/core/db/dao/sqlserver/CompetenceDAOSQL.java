package at.rc.tacos.core.db.dao.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.SQLQueries;
import at.rc.tacos.core.db.dao.CompetenceDAO;
import at.rc.tacos.model.Competence;

public class CompetenceDAOSQL implements CompetenceDAO
{
	//The data source to get the connection and the queries file
	private final DataSource source = DataSource.getInstance();
	private final SQLQueries queries = SQLQueries.getInstance();

	@Override
	public int addCompetence(Competence competence) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{	
			int id = 0;
			//get the next id
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.nextCompetenceID"));
			final ResultSet rs = stmt.executeQuery();
			if(!rs.next())
				return -1;
			
			id = rs.getInt(1);
				
			// competence_ID, competence
			final PreparedStatement insertStmt = connection.prepareStatement(queries.getStatment("insert.competence"));
			insertStmt.setInt(1, id);
			insertStmt.setString(2, competence.getCompetenceName());
			if(insertStmt.executeUpdate() == 0)
				return -1;
			
			return id;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public Competence getCompetenceById(int id) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.competenceByID"));
			stmt.setInt(1, id);
			final ResultSet rs = stmt.executeQuery();
			//no result set
			if(!rs.next())
				return null;
			
			Competence competence = new Competence();
			competence.setCompetenceName(rs.getString("competence"));
			competence.setId(id);
			return competence;
			
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public List<Competence> listCompetences() throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("list.competences"));
			final ResultSet rs = stmt.executeQuery();
			List<Competence> competences = new ArrayList<Competence>();
			//assert we have a result set
			while(rs.next())
			{
				Competence competence = new Competence();
				competence.setCompetenceName(rs.getString("competence"));
				competence.setId(rs.getInt("competence_ID"));
				competences.add(competence);
			}
			//return the list
			return competences;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public boolean removeCompetence(int id) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("remove.competence"));
			stmt.setInt(1, id);
			//assert the competence was removed
			if(stmt.executeUpdate() == 0)
				return false;
			return true;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public boolean updateCompetence(Competence competence) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			// competence where competence_ID
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("update.competence"));
			stmt.setString(1, competence.getCompetenceName());
			stmt.setInt(2, competence.getId());
			//assert the update was successfully
			if(stmt.executeUpdate() == 0)
				return false;
			return true;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public List<Competence> listCompetencesOfStaffMember(int id) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("list.competenceOfStaffMember"));
			stmt.setInt(1, id);
			final ResultSet rs = stmt.executeQuery();
			//create the returned result list and loop over the database result
			List<Competence> competences = new ArrayList<Competence>();
			while(rs.next())
			{
				Competence competence = new Competence();
				competence.setCompetenceName(rs.getString("competence"));
				competence.setId(rs.getInt("competence_ID"));
				competences.add(competence);
			}
			return competences;
		}
		finally
		{
			connection.close();
		}
	}
}