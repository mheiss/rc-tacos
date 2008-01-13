package at.rc.tacos.server.dao;

import java.sql.SQLException;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.core.db.dao.memory.ItemDAOMemory;
import at.rc.tacos.core.db.dao.memory.MobilePhoneDAOMemory;
import at.rc.tacos.core.db.dao.memory.NotifierDAOMemory;
import at.rc.tacos.core.db.dao.memory.RosterEntryDAOMemory;
import at.rc.tacos.core.db.dao.memory.StaffMemberDAOMemory;
import at.rc.tacos.core.db.dao.memory.TransportDAOMemory;
import at.rc.tacos.core.db.dao.memory.VehicleDetailDAOMemory;
import at.rc.tacos.model.Item;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.CallerDetail;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.TestDataSource;
import at.rc.tacos.model.Transport;
import at.rc.tacos.model.VehicleDetail;

/**
 * The service class manages the dao used in the server
 * @author Michael
 */
public class DaoService
{
    //the shared instance
    private static DaoService instance;
    
    //the data source
    private DaoFactory factory;
    
    /**
     * Default private class constructor
     */
    private DaoService()
    {
        factory = DaoFactory.TEST;
    }
    
    /**
     * Returns the shared instance
     * @return the shared instance
     */
    public static DaoService getInstance()
    {
        if( instance == null )
            instance = new DaoService();
        return instance;
    }
    
    /**
     * Returns the data access object factory used 
     * in this service instance.
     * @return the dao factory
     */
    public DaoFactory getFactory()
    {
        return factory;
    }
    
    /**
     * Initalize the data access objects with data
     */
    public void initData() throws SQLException
    {
        //the data source
        TestDataSource source = new TestDataSource();
        //add items
        for(Item item:source.itemList)
            ItemDAOMemory.getInstance().addItem(item);
        //add phones
        for(MobilePhoneDetail phone:source.phoneList)
            MobilePhoneDAOMemory.getInstance().addMobilePhone(phone);
        //add callers
        for(CallerDetail notifier:source.notifierList)
            NotifierDAOMemory.getInstance().addCaller(notifier);
        //roster entries
        for(RosterEntry entry:source.rosterList)
            RosterEntryDAOMemory.getInstance().addRosterEntry(entry);
        //staff members
        for(StaffMember member:source.staffList)
            StaffMemberDAOMemory.getInstance().addStaffMember(member,"P@ssw0rd");
        //transports
        for(Transport transport:source.transportList)
            TransportDAOMemory.getInstance().addTransport(transport);
        //vehicles
        for(VehicleDetail vehicle:source.vehicleList)
            VehicleDetailDAOMemory.getInstance().addVehicle(vehicle); 
    }
}
