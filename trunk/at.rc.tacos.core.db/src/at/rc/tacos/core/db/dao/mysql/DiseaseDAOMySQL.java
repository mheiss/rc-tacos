package at.rc.tacos.core.db.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.dao.DiseaseDAO;
import at.rc.tacos.model.Disease;

public class DiseaseDAOMySQL implements DiseaseDAO
{
	public static final String QUERIES_BUNDLE_PATH = "at.rc.tacos.core.db.queries";

	@Override
	public int addDisease(Disease disease) 
	{
		try
		{	
			// disease name
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(DiseaseDAOMySQL.QUERIES_BUNDLE_PATH).getString("insert.disease"));
			query.setString(1, disease.getDiseaseName());
			query.executeUpdate();
			
			//get the last inserted id
			final ResultSet rs = query.getGeneratedKeys();
		    if (rs.next()) 
		        return rs.getInt(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
		return -1;
	}

	@Override
	public List<Disease> getDiseaseList() 
	{
		List<Disease> diseases = new ArrayList<Disease>();
		try
		{
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(DiseaseDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.diseases"));
			final ResultSet rs = query.executeQuery();

			while(rs.next())
			{
				Disease disease = new Disease();
				disease.setId(rs.getInt("disease_ID"));
				disease.setDiseaseName(rs.getString("disease"));
				diseases.add(disease);
			}

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return 
				null;
		}
		return 
			diseases;
	}

	@Override
	public boolean removeDisease(int id) 
	{
		try
    	{
    		final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(DiseaseDAOMySQL.QUERIES_BUNDLE_PATH).getString("delete.disease"));
    		query.setInt(1, id);
    		query.executeUpdate();
    	}
    	catch (SQLException e)
    	{
    		e.printStackTrace();
    		return 
    			false;
    	}
    	return 
    		true;
	}

	@Override
	public boolean updateDisease(Disease disease) 
	{
		try
		{
	    	final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(DiseaseDAOMySQL.QUERIES_BUNDLE_PATH).getString("update.disease"));
	    	query.setString(1,disease.getDiseaseName());
	    	query.setInt(2, disease.getId());
			query.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return 
				false;
		}
		return 
			true;	
	}
}
