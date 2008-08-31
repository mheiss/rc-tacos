package at.rc.tacos.core.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.Queries;
import at.rc.tacos.core.db.dao.DiseaseDAO;
import at.rc.tacos.model.Disease;

public class DiseaseDAOMySQL implements DiseaseDAO
{
	//The data source to get the connection and the queries file
	private final DataSource source = DataSource.getInstance();
	private final Queries queries = Queries.getInstance();

	@Override
	public int addDisease(Disease disease) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{	
			// disease name
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("insert.disease"));
			stmt.setString(1, disease.getDiseaseName());
			stmt.executeUpdate();
			//get the last inserted id
			final ResultSet rs = stmt.getGeneratedKeys();
		    if (rs.next()) 
		        return rs.getInt(1);
		    //no auto value
		    return -1;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public List<Disease> getDiseaseList() throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("list.diseases"));
			final ResultSet rs = stmt.executeQuery();
			//create the result list and loop over the result set
			List<Disease> diseases = new ArrayList<Disease>();
			while(rs.next())
			{
				Disease disease = new Disease();
				disease.setId(rs.getInt("disease_ID"));
				disease.setDiseaseName(rs.getString("disease"));
				diseases.add(disease);
			}
			return diseases;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public boolean removeDisease(int id) throws SQLException
	{
		Connection connection = source.getConnection();
		try
    	{
    		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("delete.disease"));
    		stmt.setInt(1, id);
    		//assert the disease is removed
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
	public boolean updateDisease(Disease disease) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
	    	final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("update.disease"));
	    	stmt.setString(1,disease.getDiseaseName());
	    	stmt.setInt(2, disease.getId());
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
}