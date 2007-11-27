package at.rc.tacos.client.modelManager;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.PlatformObject;

import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.VehicleDetail;

/**
 * This class manages the vehicles.
 * @author Michael
 */
public class VehicleManager extends PlatformObject 
{
    //the item list
    private List<VehicleDetail> objectList = new ArrayList<VehicleDetail>();
    
    /**
     * Default class constructor, initialising the list of vehicles
     */
    public VehicleManager()
    {
        objectList = new ArrayList<VehicleDetail>();
    }
    
    /**
     * Returns the current list of vehicles 
     * @return the vehicle list
     */
    public List<VehicleDetail> getVehicleList()
    {
        return objectList;
    }
    
    /**
     * Create initialisation data
     */
    public void init()
    {
        //dummy data
        StaffMember sm1 = new StaffMember(0,"Alfred","Schwarzenegger","a.schw");
        StaffMember sm2 = new StaffMember(1,"Rene","Hartenfelser","r.hart");
        StaffMember sm3 = new StaffMember(2,"Simone", "Kraus", "s.krau");
        MobilePhoneDetail mp = new MobilePhoneDetail("Bm01","0664/1234567");
        VehicleDetail v1 = new VehicleDetail(1,"Bm01","RTW",sm1,sm2,sm3,mp, "the notes 1", "BM", "BM", true,false,1);
        VehicleDetail v2 = new VehicleDetail(1,"Bm02","RTW",sm1,sm2,sm3,mp, "", "BM", "KA", false,false,2);
        VehicleDetail v3 = new VehicleDetail(1,"Bm03","BKTW",sm1,sm2,sm3,mp, "     ", "BM", "BM", false,false,3);
        VehicleDetail v4 = new VehicleDetail(1,"Bm04","RTW",sm1,sm2,sm3,mp, "", "BM", "BM", false,false,3);
        VehicleDetail v5 = new VehicleDetail(1,"Bm05","BKTW",sm1,sm2,sm3,mp, "", "BM", "BM", false,false,2);
        VehicleDetail v6 = new VehicleDetail(1,"Bm06","RTW",sm1,sm2,sm3,mp, "", "BM", "BM", false,false,3);
        VehicleDetail v7 = new VehicleDetail(1,"Bm07","RTW",sm1,sm2,sm3,mp, "", "BM", "BM", false,false,1);
        VehicleDetail v8 = new VehicleDetail(1,"Bm08","RTW",sm1,sm2,sm3,mp, "", "BM", "BM", false,false,2);
        VehicleDetail v9 = new VehicleDetail(1,"Bm09","KTW",sm1,sm2,sm3,mp, "", "BM", "KA", true,false,3);
        VehicleDetail v10 = new VehicleDetail(1,"Bm10","RTW",sm1,sm2,sm3,mp, "", "BM", "KA", false,false,2);
        VehicleDetail v11 = new VehicleDetail(1,"Bm11","RTW",sm1,sm2,sm3,mp, "", "BM", "BM", true,false,2);
        VehicleDetail v12 = new VehicleDetail(1,"Bm12","RTW",sm1,sm2,sm3,mp, "", "BM", "BM", false,false,3);
        //add the dummy data
        objectList.add(v1);
        objectList.add(v2);
        objectList.add(v3);
        objectList.add(v4);
        objectList.add(v5);
        objectList.add(v6);
        objectList.add(v7);
        objectList.add(v8);
        objectList.add(v9);
        objectList.add(v10);
        objectList.add(v11);
        objectList.add(v12);
    }
}
