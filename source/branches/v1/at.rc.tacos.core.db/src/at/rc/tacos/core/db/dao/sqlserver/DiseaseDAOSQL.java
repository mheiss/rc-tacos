package at.rc.tacos.core.db.dao.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.SQLQueries;
import at.rc.tacos.core.db.dao.DiseaseDAO;
import at.rc.tacos.model.Disease;

public class DiseaseDAOSQL implements DiseaseDAO
{
	//The data source to get the connection and the queries file
	private final DataSource source = DataSource.getInstance();
	private final SQLQueries queries = SQLQueries.getInstance();

	@Override
	public int addDisease(Disease disease) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{	
			int id = 0;
			//get the next id
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.nextDiseaseID"));
			final ResultSet rs = stmt.executeQuery();
			if(!rs.next())
				return -1;
			
			id = rs.getInt(1);
			// disease name
			final PreparedStatement insertstmt = connection.prepareStatement(queries.getStatment("insert.disease"));
			insertstmt.setInt(1, id);
			insertstmt.setString(2, disease.getDiseaseName());
			
			if(insertstmt.executeUpdate() == 0)
				return -1;
			
			return id;
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