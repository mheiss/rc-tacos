package at.rc.tacos.core.db.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.dao.CompetenceDAO;
import at.rc.tacos.model.Competence;

public class CompetenceDAOMySQL implements CompetenceDAO
{
	public static final String QUERIES_BUNDLE_PATH = "at.rc.tacos.core.db.queries";

	@Override
	public int addCompetence(Competence competence)
	{
		int competenceId = -1;
		try
		{	
			// competence_ID, competence
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("insert.competence"));
			query.setString(1, competence.getCompetenceName());
			query.executeUpdate();

			//get the last inserted id
			final ResultSet rs = query.getGeneratedKeys();
		    if (rs.next()) 
		        competenceId = rs.getInt(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
		return competenceId;
	}

	@Override
	public Competence getCompetenceById(int id)
	{
		Competence competence = new Competence();
		try
		{
			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.competenceByID"));
			query1.setInt(1, id);
			final ResultSet rs = query1.executeQuery();

			if(rs.first())
			{
				competence.setCompetenceName(rs.getString("competence"));
				competence.setId(id);
			}
			else
				return null;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return competence;
	}

	@Override
	public List<Competence> listCompetences()
	{
		List<Competence> competences = new ArrayList<Competence>();
		try
		{
			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.competences"));
			final ResultSet rs = query1.executeQuery();

			while(rs.next())
			{
				Competence competence = new Competence();
				competence.setCompetenceName(rs.getString("competence"));
				competence.setId(rs.getInt("competence_ID"));
				competences.add(competence);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return competences;
	}

	@Override
	public boolean removeCompetence(int id)
	{
		try
		{
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("remove.competence"));
			query.setInt(1, id);
			query.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean updateCompetence(Competence competence)
	{
		try
		{
			// competence, competence_ID
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("update.competence"));
			query.setString(1, competence.getCompetenceName());
			query.setInt(2, competence.getId());
			query.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public List<Competence> listCompetencesOfStaffMember(int id)
	{
		List<Competence> competences = new ArrayList<Competence>();
		try
		{
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.competenceOfStaffMember"));
			query.setInt(1, id);
			final ResultSet rs = query.executeQuery();

			while(rs.next())
			{
				Competence competence = new Competence();
				competence.setCompetenceName(rs.getString("competence"));
				competence.setId(rs.getInt("competence_ID"));
				competences.add(competence);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return competences;
	}
}
