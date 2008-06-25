package at.rc.tacos.core.db.dao.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.SQLQueries;
import at.rc.tacos.core.db.dao.LinkDAO;
import at.rc.tacos.model.Link;

/**
 * Link SQL Server DAO
 * @author Payer Martin
 * @version 1.0
 */
public class LinkDAOSQL implements LinkDAO {
	
	//The data source to get the connection and the queries file
	private final DataSource source = DataSource.getInstance();
	private final SQLQueries queries = SQLQueries.getInstance();

	@Override
	public List<Link> listLinks() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int addLink(Link link) throws SQLException {
		Connection connection = source.getConnection();
		try
		{	
			int id = 0;
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.nextLinkID"));
			final ResultSet rs = stmt.executeQuery();
			if(!rs.next())
				return -1;			
			id = rs.getInt(1);
			
			final PreparedStatement insertstmt = connection.prepareStatement(queries.getStatment("insert.link"));
			
			insertstmt.setInt(1, id);
			
			insertstmt.setString(2, link.getInnerText());
			
			insertstmt.setString(3, link.getHref());
			
			insertstmt.setString(4, link.getTitle());
			
			insertstmt.setString(5, link.getUsername());
			
			if(insertstmt.executeUpdate() == 0)
				return -1;
			
			return id;
		}
		finally
		{
			connection.close();
		}
	}

}
