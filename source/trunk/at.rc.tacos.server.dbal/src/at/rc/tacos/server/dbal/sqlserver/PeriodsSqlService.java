package at.rc.tacos.server.dbal.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import at.rc.tacos.platform.model.Period;
import at.rc.tacos.platform.services.dbal.PeriodsService;
import at.rc.tacos.server.dbal.SQLQueries;

/**
 * Provides CRUD operation for periods.
 * 
 * @author Michael
 */
public class PeriodsSqlService implements PeriodsService {
	
	@Resource(name = "sqlConnection")
	protected Connection connection;

	// the source for the queries
	protected final SQLQueries queries = SQLQueries.getInstance();

	@Override
	public Period getPeriod(int periodID) throws SQLException {
		// SELECT p.period_ID, p.period, p.serviceTypeCompetence \
		// FROM period p \
		// WHERE p.period_ID = ?;
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("get.periodByID"));
		query.setInt(1, periodID);
		final ResultSet rs = query.executeQuery();
		System.out.println("periods dao sql nach executeQuery");
		// assert we have a result
		if (rs.next()) {
			Period period = new Period();
			period.setPeriod(rs.getString("period"));
			period.setServiceTypeCompetence(rs.getString("serviceTypeCompetence"));

			return period;
		}
		// no result
		return null;
	}

	@Override
	public int addPeriod(Period period) throws SQLException {
		// get the next id
		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.nextPeriodID"));
		final ResultSet rs = stmt.executeQuery();
		if (!rs.next())
			return -1;

		int id = rs.getInt(1);

		// INSERT INTO period(period_ID, period, serviceTypeCompetence \
		// VALUES(?, ?, ?);
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("insert.period"));
		query.setInt(1, id);
		query.setString(2, period.getPeriod());
		query.setString(3, period.getServiceTypeCompetence());

		if (query.executeUpdate() == 0)
			return -1;

		return id;
	}

	@Override
	public List<Period> getPeriodListByServiceTypeCompetence(String serviceTypeCompetence) throws SQLException {
		// SELECT p.period_ID, p.period, p.serviceTypeCompetence \
		// FROM period p where p.serviceTypeCompetence = ?;
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.periods"));
		query.setString(1, serviceTypeCompetence);

		final ResultSet rs = query.executeQuery();
		// assert we have a result
		List<Period> periods = new ArrayList<Period>();
		while (rs.next()) {
			Period period = new Period();
			period.setPeriodId(rs.getInt("period_ID"));
			period.setPeriod(rs.getString("period"));
			period.setServiceTypeCompetence(rs.getString("serviceTypeCompetence"));

			periods.add(period);
		}
		return periods;
	}

	@Override
	public boolean removePeriod(int id) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("delete.period"));
		query.setInt(1, id);
		// assert the location removed was successfully
		if (query.executeUpdate() == 0)
			return false;
		return true;
	}

	@Override
	public boolean updatePeriod(Period period) throws SQLException {
		// UPDATE period SET period = ?, serviceTypeCompetence = ? WHERE
		// period_ID = ?;
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("update.period"));
		query.setString(1, period.getPeriod());
		query.setString(2, period.getServiceTypeCompetence());
		query.setInt(3, period.getPeriodId());
		// assert the update was successfully
		if (query.executeUpdate() == 0)
			return false;
		return true;
	}
}
