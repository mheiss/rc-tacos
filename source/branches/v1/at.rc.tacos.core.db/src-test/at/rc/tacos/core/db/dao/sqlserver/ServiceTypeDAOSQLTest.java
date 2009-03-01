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

import at.rc.tacos.core.db.dao.ServiceTypeDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.ServiceType;

public class ServiceTypeDAOSQLTest extends DBTestBase {

	// the dao class
	private ServiceTypeDAO serviceTypeDao = DaoFactory.SQL.createServiceTypeDAO();

	// test data
	ServiceType serviceType1 = new ServiceType("serviceType1");
	ServiceType serviceType2 = new ServiceType("serviceType2");

	@Before
	public void setUp() throws SQLException {
		// insert test data
		int id1 = serviceTypeDao.addServiceType(serviceType1);
		int id2 = serviceTypeDao.addServiceType(serviceType2);
		serviceType1.setId(id1);
		serviceType2.setId(id2);
	}

	@After
	public void tearDown() throws SQLException {
		deleteTable(ServiceTypeDAO.TABLE_NAME);
	}

	@Test
	public void testFindById() throws SQLException {
		ServiceType serviceType = serviceTypeDao.getServiceTypeId(serviceType1.getId());
		Assert.assertEquals("serviceType1", serviceType.getServiceName());
	}

	@Test
	public void testRemoveServiceType() throws SQLException {
		serviceTypeDao.removeServiceType(serviceType1.getId());
		// list all
		List<ServiceType> list = serviceTypeDao.listServiceTypes();
		Assert.assertEquals(1, list.size());
	}

	@Test
	public void testListServiceType() throws SQLException {
		List<ServiceType> list = serviceTypeDao.listServiceTypes();
		Assert.assertEquals(2, list.size());
	}

	@Test
	public void testUpdateServiceType() throws SQLException {
		// create two indivdual block
		{
			ServiceType serviceType = serviceTypeDao.getServiceTypeId(serviceType1.getId());
			serviceType.setServiceName("newServiceTypeName");
			serviceTypeDao.updateServiceType(serviceType);
		}
		{
			ServiceType serviceType = serviceTypeDao.getServiceTypeId(serviceType1.getId());
			Assert.assertEquals("newServiceTypeName", serviceType.getServiceName());
		}
	}
}
