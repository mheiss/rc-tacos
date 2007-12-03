package at.rc.tacos.server.dao;

import java.util.ArrayList;

import at.rc.tacos.model.ITransportStatus;
import at.rc.tacos.model.VehicleDetail;

/**
 * Data source for vehicles
 * @author Michael
 */
public class VehicleDetailDAO
{
    //the shared instance
    private static VehicleDetailDAO instance;
    //the data list
    private ArrayList<VehicleDetail> vehicleList; 

    /**
     * Default private class constructor
     */
    private VehicleDetailDAO()
    {
        //create the list
        vehicleList = new ArrayList<VehicleDetail>();
        //load dummy data
        VehicleDetail v1 = new VehicleDetail();
        v1.setVehicleId(0);
        v1.setVehicleName("Bm01");
        v1.setVehicleType("RTW");
        v1.setDriverName(StaffMemberDAO.getInstance().getList().get(0));
        v1.setParamedicIName(StaffMemberDAO.getInstance().getList().get(1));
        v1.setParamedicIIName(StaffMemberDAO.getInstance().getList().get(2));
        v1.setVehicleNotes("notes vehicle 1");
        v1.setBasicStation("BM");
        v1.setCurrentStation("BM");
        v1.setReadyForAction(true);
        v1.setOutOfOrder(false);
        v1.setMostImportantTransportStatus(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION);
        //second vehicle
        VehicleDetail v2 = new VehicleDetail();
        v2.setVehicleId(1);
        v2.setVehicleName("Bm02");
        v2.setVehicleType("KTW");
        v2.setDriverName(StaffMemberDAO.getInstance().getList().get(0));
        v2.setParamedicIName(StaffMemberDAO.getInstance().getList().get(1));
        v2.setParamedicIIName(StaffMemberDAO.getInstance().getList().get(2));
        v2.setVehicleNotes("notes vehicle 2");
        v2.setBasicStation("BM");
        v2.setCurrentStation("KA");
        v2.setReadyForAction(true);
        v2.setOutOfOrder(false);
        v2.setMostImportantTransportStatus(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION);
        //third vehicle
        VehicleDetail v3 = new VehicleDetail();
        v3.setVehicleId(2);
        v3.setVehicleName("Bm03");
        v3.setVehicleType("RTW");
        v3.setDriverName(StaffMemberDAO.getInstance().getList().get(0));
        v3.setParamedicIName(StaffMemberDAO.getInstance().getList().get(1));
        v3.setParamedicIIName(StaffMemberDAO.getInstance().getList().get(2));
        v3.setBasicStation("KA");
        v3.setCurrentStation("KA");
        v3.setReadyForAction(false);
        v3.setOutOfOrder(true);
        v3.setMostImportantTransportStatus(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION);
        //add to list
        vehicleList.add(v1);
        vehicleList.add(v2);
        vehicleList.add(v3);
    }

    /**
     * Creates and returns the shared instance
     */
    public static VehicleDetailDAO getInstance()
    {
        if( instance == null)
            instance = new VehicleDetailDAO();
        return instance;
    }

    /**
     * Returns the data list
     * @return the list of data
     */
    public ArrayList<VehicleDetail> getList()
    {
        return vehicleList;
    }
}
