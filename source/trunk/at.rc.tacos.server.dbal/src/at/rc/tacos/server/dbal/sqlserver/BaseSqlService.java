package at.rc.tacos.server.dbal.sqlserver;

import java.sql.Connection;

import javax.annotation.Resource;

import at.rc.tacos.server.dbal.SQLQueries;

/**
 * Contains common resources and annotations thate are needed by all sql service
 * classes.
 * 
 * @author Michael
 */
public abstract class BaseSqlService {

	@Resource(name = "sqlConnection")
	protected Connection connection;

	// the source for the queries
	protected final SQLQueries queries = SQLQueries.getInstance();
}
