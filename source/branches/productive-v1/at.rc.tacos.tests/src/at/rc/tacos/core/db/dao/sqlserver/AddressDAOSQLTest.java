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

import at.rc.tacos.core.db.dao.AddressDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.Address;

public class AddressDAOSQLTest extends DBTestBase {

	// the dao class
	private AddressDAO addressDao = DaoFactory.SQL.createAddressDAO();

	private Address address1;
	private Address address2;

	@Before
	public void setUp() throws SQLException {
		address1 = new Address(8600, "Bruck an der Mur", "Dr. Th. Körnerstraße");
		address2 = new Address(8605, "Kapfenberg", "Mariazellerstraße");

		int id1 = addressDao.addAddress(address1);
		int id2 = addressDao.addAddress(address2);

		address1.setAddressId(id1);
		address2.setAddressId(id2);
	}

	@After
	public void tearDown() throws SQLException {
		deleteTable(AddressDAO.TABLE_NAME);
	}

	@Test
	public void testRemoveAddress() throws SQLException {
		addressDao.removeAddress(address1.getAddressId());
		Address address = addressDao.getAddress(address1.getAddressId());
		Assert.assertNull(address);
	}

	@Test
	public void testListAddress() throws SQLException {
		List<Address> list = addressDao.getAddressList("D%", "%", "%Bruck%", "%");
		Assert.assertEquals(1, list.size());
	}

	@Test
	public void testUpdateAddress() throws SQLException {
		// create two individual blocks
		{
			Address address = addressDao.getAddress(address1.getAddressId());
			address.setCity("new city");
			address.setStreet("new street");
			address.setAddressId(address1.getAddressId());

			addressDao.updateAddress(address);
		}
		{
			Address address = addressDao.getAddress(address1.getAddressId());
			Assert.assertEquals("new city", address.getCity());
			Assert.assertEquals("new street", address.getStreet());
		}
	}
}
