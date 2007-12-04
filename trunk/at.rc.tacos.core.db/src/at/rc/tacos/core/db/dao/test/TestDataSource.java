package at.rc.tacos.core.db.dao.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import at.rc.tacos.model.ITransportStatus;
import at.rc.tacos.model.Item;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.NotifierDetail;
import at.rc.tacos.model.Patient;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.Transport;
import at.rc.tacos.model.VehicleDetail;

/**
 * This class provides testing data for
 * all testing objects
 * @author Michael
 */
public class TestDataSource
{
    //the test data
    public ArrayList<Item> itemList;
    public ArrayList<MobilePhoneDetail> phoneList;
    public ArrayList<NotifierDetail> notifierList;
    public ArrayList<Patient> patientList;
    public ArrayList<RosterEntry> rosterList;
    public ArrayList<StaffMember> staffList;
    public ArrayList<Transport> transportList;
    public HashMap<String, String> userLogin;
    public ArrayList<VehicleDetail> vehicleList;
    
    /**
     * Default class constructor
     */
    public TestDataSource()
    {
        initItems();
        initPhones();
        initNotifiers();
        initPatients();
        initStaffMembers();
        initTransports();
        initRosters();
        initLogins();
        initVehicles();
    }
    
    //LOAD THE DUMMY DATA
    private void initItems()
    {
        itemList = new ArrayList<Item>();
        Item i1 = new Item("Item1");
        Item i2 = new Item("Item2");
        Item i3 = new Item("Item3");
        itemList.add(i1);
        itemList.add(i2);
        itemList.add(i3);
    }
    
    private void initPhones()
    {
        phoneList = new ArrayList<MobilePhoneDetail>();
        MobilePhoneDetail p1 = new MobilePhoneDetail("P1","0699-123456789");
        MobilePhoneDetail p2 = new MobilePhoneDetail("P2","0664-123456789");
        MobilePhoneDetail p3 = new MobilePhoneDetail("P3","0676-123456789");
        phoneList.add(p1);
        phoneList.add(p2);
        phoneList.add(p3);
    }
    
    private void initNotifiers()
    {
        notifierList = new ArrayList<NotifierDetail>();
        NotifierDetail n1 = new NotifierDetail("Notifer1","0664-123456789","Notes taken");
        NotifierDetail n2 = new NotifierDetail("Notifer2","0784-1548154","Notes taken");
        NotifierDetail n3 = new NotifierDetail("Notifer3","2147-123456789","Notes taken");
        notifierList.add(n1);
        notifierList.add(n2);
        notifierList.add(n3);
    }
    
    private void initPatients()
    {
        patientList = new ArrayList<Patient>();
        Patient p1 = new Patient(0,"Patient1","Patient1");
        Patient p2 = new Patient(0,"Patient2","Patient2");
        Patient p3 = new Patient(0,"Patient3","Patient3");
        patientList.add(p1);
        patientList.add(p2);
        patientList.add(p3);
    }
    
    private void initStaffMembers()
    {
        staffList = new ArrayList<StaffMember>();
        StaffMember s1 = new StaffMember(0,"Staff1","Staff1","nick.staff1");
        StaffMember s2 = new StaffMember(0,"Staff2","Staff2","nick.staff2");
        StaffMember s3 = new StaffMember(0,"Staff3","Staff3","nick.staff3");
        staffList.add(s1);
        staffList.add(s2);
        staffList.add(s3);
    }
    
    private void initTransports()
    {
        transportList = new ArrayList<Transport>();
        Transport t1 = new Transport();
        t1.setTransportId(0);
        t1.setFromStreet("street_from_1");
        t1.setFromNumber("number_from 1");
        t1.setFromCity("city_from_1");
        t1.setToStreet("street_to_1");
        t1.setToNumber("number_to_1");
        t1.setToCity("city_to_1");
        t1.setPatient(patientList.get(0));
        //second transport
        Transport t2 = new Transport();
        t2.setTransportId(0);
        t2.setFromStreet("street_from_2");
        t2.setFromNumber("number_from 2");
        t2.setFromCity("city_from_2");
        t2.setToStreet("street_to_2");
        t2.setToNumber("number_to_2");
        t2.setToCity("city_to_2");
        t2.setPatient(patientList.get(1));
        //third transport
        Transport t3 = new Transport();
        t3.setTransportId(0);
        t3.setFromStreet("street_from_3");
        t3.setFromNumber("number_from 3");
        t3.setFromCity("city_from_3");
        t3.setToStreet("street_to_3");
        t3.setToNumber("number_to_3");
        t3.setToCity("city_to_3");
        t3.setPatient(patientList.get(2));
        //add the transports
        transportList.add(t1);
        transportList.add(t2);
        transportList.add(t3);
    }
    
    private void initRosters()
    {
        rosterList = new ArrayList<RosterEntry>();
        RosterEntry e1 = new RosterEntry();
        e1.setRosterId(0);
        e1.setCompetence("Fahrer");
        e1.setPlannedStartOfWork(new Date().getTime());
        e1.setPlannedEndOfWork(new Date().getTime());
        e1.setRealEndOfWork(new Date().getTime());
        e1.setRealStartOfWork(new Date().getTime());
        e1.setRosterId(0);
        e1.setRosterNotes("mix");
        e1.setServicetype("Zivi");
        e1.setStandby(false);
        e1.setStation("Bruck");
        e1.setStaffMember(staffList.get(0));
        //second entry
        RosterEntry e2 = new RosterEntry();
        e2.setRosterId(1);
        e2.setCompetence("Fahrer");
        e2.setPlannedStartOfWork(new Date().getTime());
        e2.setPlannedEndOfWork(new Date().getTime());
        e2.setRealEndOfWork(new Date().getTime());
        e2.setRealStartOfWork(new Date().getTime());
        e2.setRosterId(0);
        e2.setRosterNotes("mix");
        e2.setServicetype("Zivi");
        e2.setStandby(false);
        e2.setStation("Bruck");
        e2.setStaffMember(staffList.get(1));
        //third entry
        RosterEntry e3 = new RosterEntry();
        e3.setRosterId(2);
        e3.setCompetence("Fahrer");
        e3.setPlannedStartOfWork(new Date().getTime());
        e3.setPlannedEndOfWork(new Date().getTime());
        e3.setRealEndOfWork(new Date().getTime());
        e3.setRealStartOfWork(new Date().getTime());
        e3.setRosterId(0);
        e3.setRosterNotes("mix");
        e3.setServicetype("Zivi");
        e3.setStandby(false);
        e3.setStation("Bruck");
        e3.setStaffMember(staffList.get(2));
        //add to list
        rosterList.add(e1);
        rosterList.add(e2);
        rosterList.add(e3);
    }
    
    private void initLogins()
    {
        userLogin = new HashMap<String, String>();
        userLogin.put("user1", "P@ssw0rd");
        userLogin.put("user2", "P@ssw0rd");
        userLogin.put("user3", "P@ssw0rd");
    }
    
    private void initVehicles()
    {
        //create the list
        vehicleList = new ArrayList<VehicleDetail>();
        //load dummy data
        VehicleDetail v1 = new VehicleDetail();
        v1.setVehicleId(0);
        v1.setVehicleName("Bm01");
        v1.setVehicleType("RTW");
        v1.setVehicleNotes("notes vehicle 1");
        v1.setBasicStation("BM");
        v1.setCurrentStation("BM");
        v1.setReadyForAction(true);
        v1.setOutOfOrder(false);
        v1.setMostImportantTransportStatus(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION);
        v1.setDriverName(staffList.get(0));
        v1.setParamedicIName(staffList.get(1));
        v1.setParamedicIIName(staffList.get(2));
        //second vehicle
        VehicleDetail v2 = new VehicleDetail();
        v2.setVehicleId(1);
        v2.setVehicleName("Bm02");
        v2.setVehicleType("KTW");
        v2.setVehicleNotes("notes vehicle 2");
        v2.setBasicStation("BM");
        v2.setCurrentStation("KA");
        v2.setReadyForAction(true);
        v2.setOutOfOrder(false);
        v2.setMostImportantTransportStatus(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION);
        v2.setDriverName(staffList.get(0));
        v2.setParamedicIName(staffList.get(1));
        v2.setParamedicIIName(staffList.get(2));
        //third vehicle
        VehicleDetail v3 = new VehicleDetail();
        v3.setVehicleId(2);
        v3.setVehicleName("Bm03");
        v3.setVehicleType("RTW");
        v3.setBasicStation("KA");
        v3.setCurrentStation("KA");
        v3.setReadyForAction(false);
        v3.setOutOfOrder(true);
        v3.setMostImportantTransportStatus(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION);
        v3.setDriverName(staffList.get(0));
        v3.setParamedicIName(staffList.get(1));
        v3.setParamedicIIName(staffList.get(2));
        //add to list
        vehicleList.add(v1);
        vehicleList.add(v2);
        vehicleList.add(v3);
    }
}
