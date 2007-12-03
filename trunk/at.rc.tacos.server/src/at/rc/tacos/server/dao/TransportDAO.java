package at.rc.tacos.server.dao;

import java.util.ArrayList;
import at.rc.tacos.model.Transport;;

/**
 * Data source for transports
 * @author Michael
 */
public class TransportDAO
{
    //the shared instance
    private static TransportDAO instance;
    //the data list
    private ArrayList<Transport> transportList; 
    
    /**
     * Default private class constructor
     */
    private TransportDAO()
    {
        //create the list
        transportList = new ArrayList<Transport>();
        //load dummy data
        Transport t1 = new Transport();
        t1.setTransportId(0);
        t1.setFromStreet("street_from_1");
        t1.setFromNumber("number_from 1");
        t1.setFromCity("city_from_1");
        t1.setToStreet("street_to_1");
        t1.setToNumber("number_to_1");
        t1.setToCity("city_to_1");
        t1.setPatient(PatientDAO.getInstance().getList().get(0));
        //second transport
        Transport t2 = new Transport();
        t2.setTransportId(0);
        t2.setFromStreet("street_from_2");
        t2.setFromNumber("number_from 2");
        t2.setFromCity("city_from_2");
        t2.setToStreet("street_to_2");
        t2.setToNumber("number_to_2");
        t2.setToCity("city_to_2");
        t2.setPatient(PatientDAO.getInstance().getList().get(1));
        //third transport
        Transport t3 = new Transport();
        t3.setTransportId(0);
        t3.setFromStreet("street_from_3");
        t3.setFromNumber("number_from 3");
        t3.setFromCity("city_from_3");
        t3.setToStreet("street_to_3");
        t3.setToNumber("number_to_3");
        t3.setToCity("city_to_3");
        t3.setPatient(PatientDAO.getInstance().getList().get(2));
        //add the transports
        transportList.add(t1);
        transportList.add(t2);
        transportList.add(t3);
    }
    
    /**
     * Creates and returns the shared instance
     */
    public static TransportDAO getInstance()
    {
        if( instance == null)
            instance = new TransportDAO();
        return instance;
    }
    
    /**
     * Returns the data list
     * @return the list of data
     */
    public ArrayList<Transport> getList()
    {
        return transportList;
    }
}
