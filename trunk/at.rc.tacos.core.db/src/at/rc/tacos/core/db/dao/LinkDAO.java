package at.rc.tacos.core.db.dao;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.model.Link;

/**
 * Link DAO Interface
 * @author Payer Martin
 * @version 1.0
 */
public interface LinkDAO {
	public List<Link> listLinks() throws SQLException;
}
