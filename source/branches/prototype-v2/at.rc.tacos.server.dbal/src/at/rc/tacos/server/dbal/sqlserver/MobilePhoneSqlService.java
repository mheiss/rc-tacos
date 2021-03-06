package at.rc.tacos.server.dbal.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import at.rc.tacos.platform.model.MobilePhoneDetail;
import at.rc.tacos.platform.services.dbal.MobilePhoneService;
import at.rc.tacos.server.dbal.SQLQueries;

/**
 * Provides CRUD operation for mobile phone.
 * 
 * @author Michael
 */
public class MobilePhoneSqlService implements MobilePhoneService {

	@Resource(name = "sqlConnection")
	protected Connection connection;

	// the source for the queries
	protected final SQLQueries queries = SQLQueries.getInstance();

	@Override
	public int addMobilePhone(MobilePhoneDetail phone) throws SQLException {
		// get the next id
		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.nextPhoneID"));
		final ResultSet rs = stmt.executeQuery();
		if (!rs.next())
			return -1;
		int id = rs.getInt(1);

		// phonenumber_ID, phonenumber, phonename
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("insert.phone"));
		query.setInt(1, id);
		query.setString(2, phone.getMobilePhoneNumber());
		query.setString(3, phone.getMobilePhoneName());
		if (query.executeUpdate() == 0)
			return -1;

		return id;
	}

	@Override
	public MobilePhoneDetail getMobilePhoneByName(String mobilePhoneName) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("get.phoneByName"));
		query.setString(1, mobilePhoneName);
		final ResultSet rs = query.executeQuery();
		// assert we have a result set
		if (rs.next()) {
			return setupMobilePhone(rs);
		}
		// no result set
		return null;
	}

	@Override
	public boolean removeMobilePhone(int id) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("remove.phone"));
		query.setInt(1, id);
		// assert the phone was removed
		if (query.executeUpdate() == 0)
			return false;
		return true;
	}

	@Override
	public boolean updateMobilePhone(MobilePhoneDetail phone) throws SQLException {
		// phonenumber, phonename, phonenumber_ID
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("update.Phonenumber"));
		query.setString(1, phone.getMobilePhoneNumber());
		query.setString(2, phone.getMobilePhoneName());
		query.setInt(3, phone.getId());
		// check if the update was successfully
		if (query.executeUpdate() == 0)
			return false;
		return true;
	}

	@Override
	public List<MobilePhoneDetail> listMobilePhones() throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.phones"));
		final ResultSet rs = query.executeQuery();
		return setupMobilePhoneList(rs);
	}

	@Override
	public List<MobilePhoneDetail> listMobilePhonesOfStaffMember(int id) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.PhonenumbersOfMemberID"));
		query.setInt(1, id);
		final ResultSet rs = query.executeQuery();
		return setupMobilePhoneList(rs);
	}

	/**
	 * Helper method to setup a list of mobile phones
	 */
	private List<MobilePhoneDetail> setupMobilePhoneList(ResultSet rs) throws SQLException {
		List<MobilePhoneDetail> phoneList = new ArrayList<MobilePhoneDetail>();
		while (rs.next()) {
			MobilePhoneDetail phone = setupMobilePhone(rs);
			phoneList.add(phone);
		}
		return phoneList;
	}

	/**
	 * Helper method to setup a single mobile phone
	 */
	private MobilePhoneDetail setupMobilePhone(ResultSet rs) throws SQLException {
		MobilePhoneDetail phone = new MobilePhoneDetail();
		phone.setId(rs.getInt("phonenumber_ID"));
		phone.setMobilePhoneNumber(rs.getString("phonenumber"));
		phone.setMobilePhoneName(rs.getString("phonename"));
		return phone;
	}
}
