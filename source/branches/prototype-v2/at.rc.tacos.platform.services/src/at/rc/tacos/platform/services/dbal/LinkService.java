package at.rc.tacos.platform.services.dbal;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.platform.model.Link;

/**
 * Link DAO Interface
 * 
 * @author Payer Martin
 * @version 1.0
 */
public interface LinkService {

	public static final String TABLE_NAME = "link";

	public int addLink(Link link) throws SQLException;

	public List<Link> listLinks() throws SQLException;

	public Link getLinkById(int linkId) throws SQLException;

	public boolean removeLink(int linkId) throws SQLException;

	public boolean updateLink(Link link) throws SQLException;
}
