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

import at.rc.tacos.core.db.dao.SickPersonDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.SickPerson;

public class SickPersonDAOSQLTest extends DBTestBase {

	// the dao class
	private SickPersonDAO personDao = DaoFactory.SQL.createSickPersonDAO();

	private SickPerson person1;
	private SickPerson person2;

	@Before
	public void setUp() throws SQLException {
		person1 = new SickPerson("Hans", "Maier");
		person2 = new SickPerson("Franz", "Huber");

		int id1 = personDao.addSickPerson(person1);
		int id2 = personDao.addSickPerson(person2);

		person1.setSickPersonId(id1);
		person2.setSickPersonId(id2);
	}

	@After
	public void tearDown() throws SQLException {
		deleteTable(SickPersonDAO.TABLE_NAME);
	}

	@Test
	public void testRemoveSickPerson() throws SQLException {
		personDao.removeSickPerson(person1.getSickPersonId());
		List<SickPerson> list = personDao.getSickPersonList("ube");
		Assert.assertEquals(1, list.size());
	}

	@Test
	public void testListSickPerson() throws SQLException {
		List<SickPerson> list = personDao.getSickPersonList("mai");
		Assert.assertEquals(1, list.size());
	}

	@Test
	public void testUpdateLocation() throws SQLException {
		// create two individual blocks
		{
			SickPerson person = personDao.getSickPerson(person1.getSickPersonId());
			person.setCityname("new city");
			person.setFirstName("newFirstname");
			person.setSickPersonId(person1.getSickPersonId());

			personDao.updateSickPerson(person);
		}
		{
			SickPerson person = personDao.getSickPerson(person1.getSickPersonId());
			Assert.assertEquals("new city", person.getCityname());
			Assert.assertEquals("newFirstname", person.getFirstName());
		}
	}
}
