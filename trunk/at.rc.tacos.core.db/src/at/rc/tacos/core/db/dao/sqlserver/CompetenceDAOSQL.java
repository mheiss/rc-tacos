package at.rc.tacos.core.db.dao.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.Queries;
import at.rc.tacos.core.db.dao.CompetenceDAO;
import at.rc.tacos.model.Competence;

public class CompetenceDAOSQL implements CompetenceDAO
{
	//The data source to get the connection and the queries file
	private final DataSource source = DataSource.getInstance();
	private final Queries queries = Queries.getInstance();

	@Override
	public int addCompetence(Competence competence) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{	
			// competence_ID, competence
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("insert.competence"));
			stmt.setString(1, competence.getCompetenceName());
			stmt.executeUpdate();

			//get the last inserted id
			final PreparedStatement stmt1 = connection.prepareStatement(queries.getStatment("get.highestCompetenceID"));
			final ResultSet rs1 = stmt1.executeQuery();
//			final ResultSet rs = stmt.getGeneratedKeys();
			if (rs1.next()) 
				return rs1.getInt(1);
			//no auto value generated
			return -1;
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
			while(rs.next())
			{
				Competence competence = new Competence();
				competence.setCompetenceName(rs.getString("competence"));
				competence.setId(id);
				return competence;
			}
			//no result set
			return null;
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