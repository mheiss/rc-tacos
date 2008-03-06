package at.rc.tacos.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import at.rc.tacos.common.Constants;
import at.rc.tacos.common.IDirectness;
import at.rc.tacos.common.ITransportPriority;
import at.rc.tacos.common.ITransportStatus;


/**
 * This class provides testing data for
 * all testing objects
 * @author Michael
 */
public class TestDataSource
{
    //the instance
    private static TestDataSource instance;
    
    //the test data
    public ArrayList<Competence> competenceList;
    public ArrayList<Job> jobList;
    public ArrayList<ServiceType> serviceList;
    public ArrayList<Location> locationList;
    public ArrayList<MobilePhoneDetail> phoneList;
    public ArrayList<CallerDetail> notifierList;
    public ArrayList<Patient> patientList;
    public ArrayList<RosterEntry> rosterList;
    public ArrayList<StaffMember> staffList;
    public ArrayList<Transport> transportList;
    public HashMap<String, String> userLogin;
    public ArrayList<VehicleDetail> vehicleList;
    
    /**
     * Default class constructor
     */
    private TestDataSource()
    {
        initLogins();
        initJobs();
        initLocations();
        initServiceTypes();
        initCompetences();
        initPhones();
        initNotifiers();
        initPatients();
        initStaffMembers();
        initVehicles();
        initRosters();
        initTransports(); 
    }
    
    /**
     * Returns the shared instance
     * @return the instance
     */
    public static TestDataSource getInstance()
    {
        if(instance == null)
            instance = new TestDataSource();
        return instance;
    }
    
    private void initJobs()
    {
        jobList = new ArrayList<Job>();
        //add all jobs
        for(int i = 0; i < Constants.job.length; i++)
        {
            Job job = new Job(Constants.job[i]);
            job.setId(i);
            jobList.add(job);
        }
    }
    
    private void initServiceTypes()
    {
        serviceList = new ArrayList<ServiceType>();
        //add all service types
        for(int i = 0; i < Constants.service.length; i++)
        {
            ServiceType serviceType = new ServiceType(Constants.service[i]);
            serviceType.setId(i);
            serviceList.add(serviceType);
        }
    }
    
    private void initCompetences()
    {
        competenceList = new ArrayList<Competence>();
        //add all competences
        for(int i = 0; i < Constants.competence.length; i++)
        {
            Competence comp = new Competence(Constants.competence[i]);
            comp.setId(i);
            competenceList.add(comp);
        }
    }
    
    public void initLocations()
    {
        locationList = new ArrayList<Location>();
        //add all locations
        for(int i = 0; i < Constants.stations.length; i++)
        {
            Location location = new Location();
            location.setLocationName(Constants.stations[i]);
            location.setId(i);
            locationList.add(location);
        }
    }
    
    private void initPhones()
    {
        phoneList = new ArrayList<MobilePhoneDetail>();
        MobilePhoneDetail p1 = new MobilePhoneDetail("BM01","0699-123456789");
        p1.setId(0);
        MobilePhoneDetail p2 = new MobilePhoneDetail("BM02","0664-123456789");
        p2.setId(1);
        MobilePhoneDetail p3 = new MobilePhoneDetail("BM03","0676-123456789");
        p3.setId(2);
        phoneList.add(p1);
        phoneList.add(p2);
        phoneList.add(p3);
    }
    
    private void initNotifiers()
    {
        notifierList = new ArrayList<CallerDetail>();
        CallerDetail n1 = new CallerDetail("Hofer","0664-123456789");
        CallerDetail n2 = new CallerDetail("Huber","0784-1548154");
        CallerDetail n3 = new CallerDetail("Hauer","2147-123456789");
        notifierList.add(n1);
        notifierList.add(n2);
        notifierList.add(n3);
    }
    
    private void initPatients()
    {
        patientList = new ArrayList<Patient>();
        Patient p1 = new Patient("Muster","Max");
        Patient p2 = new Patient("Meier","Hans");
        Patient p3 = new Patient("Bacher","Hermine");
        patientList.add(p1);
        patientList.add(p2);
        patientList.add(p3);
    }
    
    private void initStaffMembers()
    {
        //perpare data for members
        staffList = new ArrayList<StaffMember>();
        
        //setup the first staff member
        StaffMember s1 = new StaffMember("Michael","Heiﬂ","heissm");
        s1.setStaffMemberId(0);
        Calendar cal = Calendar.getInstance(); 
        cal.set(Calendar.YEAR,1984); 
        cal.set(Calendar.MONTH,12); 
        cal.set(Calendar.DAY_OF_MONTH,02); 
        s1.setBirthday(cal.getTimeInMillis());
        s1.setCityname("Scharnitz");
        s1.setEMail("michael.heiss.itm05@fh-joanneum.at");
        s1.setStreetname("Gieﬂenbach 330");
        s1.addMobilePhone(phoneList.get(0));
        s1.addCompetence(competenceList.get(0));
        s1.setMale(true);
        
        //second member
        StaffMember s2 = new StaffMember("Walter","Lohmann","w.lohm");
        s2.setStaffMemberId(1);
        cal = Calendar.getInstance(); 
        cal.set(Calendar.YEAR,1984); 
        cal.set(Calendar.MONTH,10); 
        cal.set(Calendar.DAY_OF_MONTH,17); 
        s2.setBirthday(cal.getTimeInMillis());
        s2.setCityname("Seefeld");
        s2.setEMail("walter.lohmann.itm05@fh-joanneum.at");
        s2.setStreetname("Weg13");
        s2.addMobilePhone(phoneList.get(1));
        s2.addCompetence(competenceList.get(0));
        s2.addCompetence(competenceList.get(1));
        s2.setMale(true);
        
        //third member
        StaffMember s3 = new StaffMember("Birgit","Thek","b.thek");
        s3.setStaffMemberId(2);
        cal = Calendar.getInstance(); 
        cal.set(Calendar.YEAR,1973); 
        cal.set(Calendar.MONTH,05); 
        cal.set(Calendar.DAY_OF_MONTH,20); 
        s3.setBirthday(cal.getTimeInMillis());
        s3.setCityname("Kapfenberg");
        s3.setEMail("birgit.thek.itm05@fh-joanneum.at");
        s3.setStreetname("Straﬂe 30");
        s3.addMobilePhone(phoneList.get(2));
        s3.addCompetence(competenceList.get(1));
        s3.addCompetence(competenceList.get(3));
        s3.setPrimaryLocation(locationList.get(0));
        s3.setMale(false);
        
        //fourth member
        StaffMember s4 = new StaffMember("vorname","nachname","user3");
        s4.setStaffMemberId(3);
        cal = Calendar.getInstance(); 
        cal.set(Calendar.YEAR,1998); 
        cal.set(Calendar.MONTH,8); 
        cal.set(Calendar.DAY_OF_MONTH,14); 
        s4.setBirthday(cal.getTimeInMillis());
        s4.setCityname("Kapfenberg");
        s4.setEMail("test@fh-joanneum.at");
        s4.setStreetname("Wist 18");
        s4.addMobilePhone(phoneList.get(2));
        s4.addCompetence(competenceList.get(0));
        s4.setMale(true);
        
        //add the created members
        staffList.add(s1);
        staffList.add(s2);
        staffList.add(s3);
        staffList.add(s4);
    }
    
    private void initTransports()
    {
        transportList = new ArrayList<Transport>();
        Transport t1 = new Transport();
        t1.setTransportId(0);
        t1.setFromStreet("Wienerstr. 46");
        t1.setFromCity("Kapfenberg");
        t1.setToStreet("LKH Bruck");
        t1.setToCity("Unf. Amb.");
        t1.setPatient(patientList.get(0));
        t1.addStatus(1, new Date().getTime());
        t1.addStatus(2, new Date().getTime());
        t1.setCallerDetail(notifierList.get(0));
        t1.setVehicleDetail(vehicleList.get(0));
        t1.setTransportPriority(ITransportPriority.TRANSPORT_PRIORITY_BLUELIGHT);
        t1.setDirection(IDirectness.TOWARDS_BRUCK);
        //second transport
        Transport t2 = new Transport();
        t2.setTransportId(1);
        t2.setFromStreet("Forstschule");
        t2.setFromCity("Bruck an der Mur");
        t2.setToStreet("LKH Leoben");
        t2.setToCity("HNO Amb.");
        t2.setPatient(patientList.get(1));
        t2.setCallerDetail(notifierList.get(1));
        t2.setVehicleDetail(vehicleList.get(1));
        t2.setTransportPriority(ITransportPriority.TRANSPORT_PRIORITY_BLUELIGHT);
        t2.setDirection(IDirectness.TOWARDS_LEOBEN);
        //third transport
        Transport t3 = new Transport();
        t3.setTransportId(2);
        t3.setFromStreet("LKH Bruck");
        t3.setFromCity("Med A");
        t3.setToStreet("LKH Graz");
        t3.setToCity("ZRI");
        t3.setPatient(patientList.get(2));
        t3.setCallerDetail(notifierList.get(2));
        t3.setVehicleDetail(vehicleList.get(2));
        t3.setTransportPriority(ITransportPriority.TRANSPORT_PRIORITY_BLUELIGHT);
        t3.setDirection(IDirectness.TOWARDS_GRAZ);
        //add the transports
        transportList.add(t1);
        transportList.add(t2);
        transportList.add(t3);
    }
    
    private void initRosters()
    {
        rosterList = new ArrayList<RosterEntry>();
        //date for the entries
        Calendar cal = Calendar.getInstance();
        RosterEntry e1 = new RosterEntry();
        e1.setRosterId(0);
        e1.setJob(jobList.get(0));
        //start -> now
        e1.setPlannedStartOfWork(cal.getTimeInMillis());
        e1.setRealStartOfWork(cal.getTimeInMillis());
        //end -> 6h
        cal.add(Calendar.HOUR_OF_DAY,6);
        e1.setPlannedEndOfWork(cal.getTimeInMillis());
        e1.setRealEndOfWork(cal.getTimeInMillis());
        e1.setServicetype(serviceList.get(0));
        e1.setStandby(false);
        e1.setStation(locationList.get(0));
        e1.setStaffMember(staffList.get(0));
        //second entry
        RosterEntry e2 = new RosterEntry();
        e2.setRosterId(1);
        e2.setJob(jobList.get(1));
        //start -> tomorrow
        cal.add(Calendar.DAY_OF_MONTH, +1);
        e2.setPlannedStartOfWork(cal.getTimeInMillis());
        e2.setRealStartOfWork(cal.getTimeInMillis());
        //end -> 6h
        cal.add(Calendar.HOUR_OF_DAY,6);
        e2.setPlannedEndOfWork(cal.getTimeInMillis());
        e2.setRealEndOfWork(cal.getTimeInMillis());
        e2.setRosterNotes("mix");
        e2.setServicetype(serviceList.get(1));
        e2.setStandby(true);
        e2.setStation(locationList.get(1));
        e2.setStaffMember(staffList.get(1));
        //third entry
        RosterEntry e3 = new RosterEntry();
        e3.setRosterId(2);
        e3.setJob(jobList.get(2));
        //start -> one day after tomorrow
        cal.add(Calendar.DAY_OF_MONTH, +1);
        e3.setPlannedStartOfWork(cal.getTimeInMillis());
        e3.setRealEndOfWork(cal.getTimeInMillis());
        //end -> 6h
        cal.add(Calendar.HOUR_OF_DAY,6);
        e3.setPlannedEndOfWork(cal.getTimeInMillis());
        e3.setRealStartOfWork(cal.getTimeInMillis());
        e3.setRosterNotes("mix");
        e3.setServicetype(serviceList.get(2));
        e3.setStandby(false);
        e3.setStation(locationList.get(2));
        e3.setStaffMember(staffList.get(2));
        //add to list
        rosterList.add(e1);
        rosterList.add(e2);
        rosterList.add(e3);
    }
    
    private void initLogins()
    {
        userLogin = new HashMap<String, String>();
        userLogin.put("heissm", "P@ssw0rd");
        userLogin.put("w.lohm", "P@ssw0rd");
        userLogin.put("b.thek", "P@ssw0rd");
        userLogin.put("user3", "P@ssw0rd");
    }
    
    private void initVehicles()
    {
        //create the list
        vehicleList = new ArrayList<VehicleDetail>();
        //load dummy data
        VehicleDetail v1 = new VehicleDetail();
        v1.setVehicleName("Bm01");
        v1.setVehicleType("RTW");
        v1.setVehicleNotes("notes vehicle 1");
        v1.setBasicStation(locationList.get(1));
        v1.setCurrentStation(locationList.get(1));
        v1.setReadyForAction(true);
        v1.setOutOfOrder(false);
        v1.setTransportStatus(ITransportStatus.TRANSPORT_STATUS_START_WITH_PATIENT);
        v1.setDriver(staffList.get(0));
        v1.setFirstParamedic(staffList.get(1));
        v1.setSecondParamedic(staffList.get(2));
        v1.setMobilPhone(phoneList.get(0));
        //second vehicle
        VehicleDetail v2 = new VehicleDetail();
        v2.setVehicleName("Bm02");
        v2.setVehicleType("KTW");
        v2.setVehicleNotes("notes vehicle 2");
        v2.setBasicStation(locationList.get(1));
        v2.setCurrentStation(locationList.get(1));
        v2.setReadyForAction(true);
        v2.setOutOfOrder(false);
        v2.setTransportStatus(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION);
        v2.setDriver(staffList.get(0));
        v2.setFirstParamedic(staffList.get(1));
        v2.setSecondParamedic(staffList.get(2));
        v2.setMobilPhone(phoneList.get(1));
        //third vehicle
        VehicleDetail v3 = new VehicleDetail();
        v3.setVehicleName("Bm03");
        v3.setVehicleType("RTW");
        v3.setBasicStation(locationList.get(1));
        v3.setCurrentStation(locationList.get(0));
        v3.setReadyForAction(false);
        v3.setOutOfOrder(true);
        v3.setTransportStatus(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION);
        v3.setDriver(staffList.get(0));
        v3.setFirstParamedic(staffList.get(1));
        v3.setSecondParamedic(staffList.get(2));
        v3.setMobilPhone(phoneList.get(2));
        //fourth vehicle
        VehicleDetail v4 = new VehicleDetail();
        v4.setVehicleName("Ka04");
        v4.setVehicleType("RTW");
        v4.setBasicStation(locationList.get(2));
        v4.setCurrentStation(locationList.get(3));
        v4.setReadyForAction(false);
        v4.setOutOfOrder(true);
        v4.setTransportStatus(ITransportStatus.TRANSPORT_STATUS_DESTINATION_FREE);
        v4.setDriver(staffList.get(0));
        v4.setFirstParamedic(staffList.get(1));
        v4.setSecondParamedic(staffList.get(2));
        v4.setMobilPhone(phoneList.get(2));
        //add to list
        vehicleList.add(v1);
        vehicleList.add(v2);
        vehicleList.add(v3);
        vehicleList.add(v4);
    }
}
