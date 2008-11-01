package at.rc.tacos.server.dbal.sqlserver;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.platform.model.MobilePhoneDetail;
import at.rc.tacos.platform.services.dbal.MobilePhoneService;

/**
 * Provides CRUD operation for mobile phone.
 * 
 * @author Michael
 */
public class MobilePhoneSqlService extends BaseSqlService implements MobilePhoneService {

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
			MobilePhoneDetail phone = new MobilePhoneDetail();
			phone.setId(rs.getInt("phonenumber_ID"));
			phone.setMobilePhoneNumber(rs.getString("phonenumber"));
			phone.setMobilePhoneName(rs.getString("phonename"));
			return phone;
		}
		// no result set
		return null;
	}

	@Override
	public List<MobilePhoneDetail> listMobilePhones() throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.phones"));
		final ResultSet rs = query.executeQuery();
		// create the result list and loop over the result
		List<MobilePhoneDetail> phones = new ArrayList<MobilePhoneDetail>();
		while (rs.next()) {
			MobilePhoneDetail phone = new MobilePhoneDetail();
			phone.setId(rs.getInt("phonenumber_ID"));
			phone.setMobilePhoneNumber(rs.getString("phonenumber"));
			phone.setMobilePhoneName(rs.getString("phonename"));
			phones.add(phone);
		}
		return phones;
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
	public List<MobilePhoneDetail> listMobilePhonesOfStaffMember(int id) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.PhonenumbersOfMemberID"));
		query.setInt(1, id);
		final ResultSet rs = query.executeQuery();
		// create a list and loop over the result
		List<MobilePhoneDetail> phones = new ArrayList<MobilePhoneDetail>();
		while (rs.next()) {
			MobilePhoneDetail phone = new MobilePhoneDetail();
			phone.setId(rs.getInt("phonenumber_ID"));
			phone.setMobilePhoneNumber(rs.getString("phonenumber"));
			phone.setMobilePhoneName(rs.getString("phonename"));
			phones.add(phone);
		}
		return phones;
	}
}
