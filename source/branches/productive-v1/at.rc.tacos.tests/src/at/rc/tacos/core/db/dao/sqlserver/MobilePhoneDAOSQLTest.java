/*******************************************************************************
 * Copyright (c) 2008, 2009 Internettechnik, FH JOANNEUM
 * http://www.fh-joanneum.at/itm
 * 
 * 	Licenced under the GNU GENERAL PUBLIC LICENSE Version 2;
 * 	You may obtain a copy of the License at
 * 	http://www.gnu.org/licenses/gpl-2.0.txt
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *******************************************************************************/
package at.rc.tacos.core.db.dao.sqlserver;

import java.sql.SQLException;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.rc.tacos.core.db.dao.MobilePhoneDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.MobilePhoneDetail;

public class MobilePhoneDAOSQLTest extends DBTestBase {

	// the dao class
	private MobilePhoneDAO mobilePhoneDAO = DaoFactory.SQL.createMobilePhoneDAO();

	// test data
	MobilePhoneDetail phone1, phone2;

	@Before
	public void setUp() throws SQLException {
		phone1 = new MobilePhoneDetail("phone1", "0664-123456789");
		phone2 = new MobilePhoneDetail("phone2", "0664-987654321");
		// insert test data
		int id1 = mobilePhoneDAO.addMobilePhone(phone1);
		int id2 = mobilePhoneDAO.addMobilePhone(phone2);
		phone1.setId(id1);
		phone2.setId(id2);
	}

	@After
	public void tearDown() throws SQLException {
		deleteTable(MobilePhoneDAO.TABLE_NAME);
	}

	@Test
	public void testFindByName() throws SQLException {
		MobilePhoneDetail MobilePhone = mobilePhoneDAO.getMobilePhoneByName("phone1");
		Assert.assertEquals("phone1", MobilePhone.getMobilePhoneName());
		Assert.assertEquals("0664-123456789", MobilePhone.getMobilePhoneNumber());
	}

	@Test
	public void testRemoveMobilePhone() throws SQLException {
		mobilePhoneDAO.removeMobilePhone(phone1.getId());
		// list all
		List<MobilePhoneDetail> list = mobilePhoneDAO.listMobilePhones();
		Assert.assertEquals(1, list.size());
	}

	@Test
	public void testListMobilePhone() throws SQLException {
		List<MobilePhoneDetail> list = mobilePhoneDAO.listMobilePhones();
		Assert.assertEquals(2, list.size());
	}

	@Test
	public void testUpdateMobilePhone() throws SQLException {
		// create two indivdual block
		{
			MobilePhoneDetail phone = mobilePhoneDAO.getMobilePhoneByName("phone1");
			phone.setMobilePhoneName("newMobilePhoneName");
			phone.setMobilePhoneNumber("0664-555555");
			mobilePhoneDAO.updateMobilePhone(phone);
		}
		{
			MobilePhoneDetail phone = mobilePhoneDAO.getMobilePhoneByName("newMobilePhoneName");
			Assert.assertEquals("newMobilePhoneName", phone.getMobilePhoneName());
			Assert.assertEquals("0664-555555", phone.getMobilePhoneNumber());
		}
	}
}
