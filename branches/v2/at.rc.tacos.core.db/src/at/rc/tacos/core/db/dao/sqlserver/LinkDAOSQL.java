package at.rc.tacos.core.db.dao.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.core.db.DbWrapper;
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
	private final DbWrapper source = DbWrapper.getDefault();
	private final SQLQueries queries = SQLQueries.getInstance();

	@Override
	public List<Link> listLinks() throws SQLException {
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("list.links"));
			final ResultSet rs = stmt.executeQuery();
			//create the returned list and loop over the result set
			List<Link> linkList = new ArrayList<Link>();
			while(rs.next())
			{
				Link link = new Link();
				link.setId(rs.getInt("link_ID"));
				link.setInnerText(rs.getString("link_inner_text"));
				link.setHref(rs.getString("link_href"));
				link.setTitle(rs.getString("link_title"));
				link.setUsername(rs.getString("username"));
				linkList.add(link);
			}
			return linkList;
		}
		finally
		{
			connection.close();
		}
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
			
			if (link.getTitle() == null || link.getTitle().equals("")) {
				insertstmt.setString(4, null);
			} else {
				insertstmt.setString(4, link.getTitle());
			}
			
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

	@Override
	public Link getLinkById(int linkId) throws SQLException {
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.linkByID"));
			stmt.setInt(1, linkId);
			final ResultSet rs = stmt.executeQuery();
			//assert we have the job
			if(!rs.next())
				return null;
			
			Link link = new Link();
			link.setId(rs.getInt("link_ID"));
			link.setInnerText(rs.getString("link_inner_text"));
			link.setHref(rs.getString("link_href"));
			link.setTitle(rs.getString("link_title"));
			link.setUsername(rs.getString("username"));
			
			return link;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public boolean removeLink(int linkId) throws SQLException {
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("remove.link"));
			stmt.setInt(1, linkId);
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
	public boolean updateLink(Link link) throws SQLException {
		Connection connection = source.getConnection();
		try
		{
			// jobname, job_ID
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("update.link"));
			stmt.setString(1, link.getInnerText());
			stmt.setString(2, link.getHref());
			if (link.getTitle() == null || link.getTitle().equals("")) {
				stmt.setString(3, null);
			} else {
				stmt.setString(3, link.getTitle());
			}
			stmt.setString(4, link.getUsername());
			stmt.setInt(5, link.getId());
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
