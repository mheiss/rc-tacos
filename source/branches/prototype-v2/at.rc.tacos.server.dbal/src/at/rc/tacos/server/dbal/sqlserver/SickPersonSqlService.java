package at.rc.tacos.server.dbal.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import at.rc.tacos.platform.model.SickPerson;
import at.rc.tacos.platform.services.dbal.SickPersonService;
import at.rc.tacos.server.dbal.SQLQueries;

/**
 * Provides CRUD operation for sick person.
 * 
 * @author Michael
 */
public class SickPersonSqlService implements SickPersonService {

	@Resource(name = "sqlConnection")
	protected Connection connection;

	// the source for the queries
	protected final SQLQueries queries = SQLQueries.getInstance();

	@Override
	public int addSickPerson(SickPerson person) throws SQLException {
		// get the next id
		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.nextSickPersonID"));
		final ResultSet rs = stmt.executeQuery();
		if (!rs.next())
			return -1;

		int id = rs.getInt(1);
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("insert.sickperson"));
		query.setInt(1, id);
		query.setString(2, person.getLastName());
		query.setString(3, person.getFirstName());
		query.setBoolean(4, person.isMale());
		query.setString(5, person.getStreetname());
		query.setString(6, person.getCityname());
		query.setString(7, person.getSVNR());
		query.setString(8, person.getKindOfTransport());
		query.setString(9, person.getNotes());

		if (query.executeUpdate() == 0)
			return -1;

		return id;
	}

	@Override
	public boolean removeSickPerson(int id) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("delete.sickperson"));
		query.setInt(1, id);
		// assert the location removed was successfully
		if (query.executeUpdate() == 0)
			return false;
		return true;
	}

	@Override
	public boolean updateSickPerson(SickPerson person) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("update.sickperson"));
		query.setString(1, person.getFirstName());
		query.setString(2, person.getLastName());
		query.setBoolean(3, person.isMale());
		query.setString(4, person.getStreetname());
		query.setString(5, person.getCityname());
		query.setString(6, person.getSVNR());
		query.setString(7, person.getKindOfTransport());
		query.setString(8, person.getNotes());
		query.setInt(9, person.getSickPersonId());
		// assert the update was successfully
		if (query.executeUpdate() == 0)
			return false;
		return true;
	}

	@Override
	public SickPerson getSickPerson(int personID) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("get.sickpersonByID"));
		query.setInt(1, personID);
		final ResultSet rs = query.executeQuery();
		// assert we have a result
		if (rs.next()) {
			return setupSickPerson(rs);
		}
		// no result
		return null;
	}

	@Override
	public List<SickPerson> getSickPersonList(String searchString) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.sickpersonsByLastNameSearchString"));
		query.setString(1, "%" + searchString + "%");
		final ResultSet rs = query.executeQuery();
		return setupSickPersonList(rs);
	}

	/**
	 * Helper method to setup a list of sick persons
	 */
	private List<SickPerson> setupSickPersonList(ResultSet rs) throws SQLException {
		List<SickPerson> personList = new ArrayList<SickPerson>();
		while (rs.next()) {
			SickPerson sickPerson = setupSickPerson(rs);
			personList.add(sickPerson);
		}
		return personList;
	}

	/**
	 * Helper method to setup a single sick person
	 */
	private SickPerson setupSickPerson(ResultSet rs) throws SQLException {
		SickPerson person = new SickPerson();
		person.setSickPersonId(rs.getInt("sickperson_ID"));
		person.setLastName(rs.getString("lastname"));
		person.setFirstName(rs.getString("firstname"));
		person.setMale(rs.getBoolean("sex"));
		person.setCityname(rs.getString("city"));
		person.setStreetname(rs.getString("street"));
		person.setSVNR(rs.getString("svnr"));
		person.setKindOfTransport(rs.getString("kindoftransport"));
		person.setNotes(rs.getString("notes"));
		return person;
	}
}
