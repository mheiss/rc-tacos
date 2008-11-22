package at.rc.taocs.server.impl;

import java.util.HashMap;
import java.util.Map;

import at.rc.tacos.platform.services.DbalServiceFactory;
import at.rc.tacos.platform.services.dbal.AddressService;
import at.rc.tacos.platform.services.dbal.CallerService;
import at.rc.tacos.platform.services.dbal.CompetenceService;
import at.rc.tacos.platform.services.dbal.DayInfoService;
import at.rc.tacos.platform.services.dbal.DialysisPatientService;
import at.rc.tacos.platform.services.dbal.DiseaseService;
import at.rc.tacos.platform.services.dbal.JobService;
import at.rc.tacos.platform.services.dbal.LinkService;
import at.rc.tacos.platform.services.dbal.LocationService;
import at.rc.tacos.platform.services.dbal.MobilePhoneService;
import at.rc.tacos.platform.services.dbal.RosterService;
import at.rc.tacos.platform.services.dbal.ServiceTypeService;
import at.rc.tacos.platform.services.dbal.SickPersonService;
import at.rc.tacos.platform.services.dbal.StaffMemberService;
import at.rc.tacos.platform.services.dbal.TransportService;
import at.rc.tacos.platform.services.dbal.AuthenticationService;
import at.rc.tacos.platform.services.dbal.VehicleService;
import at.rc.tacos.server.dbal.sqlserver.AddressSqlService;
import at.rc.tacos.server.dbal.sqlserver.CallerSqlService;
import at.rc.tacos.server.dbal.sqlserver.CompetenceSqlService;
import at.rc.tacos.server.dbal.sqlserver.DayInfoSqlService;
import at.rc.tacos.server.dbal.sqlserver.DialysisPatientSqlService;
import at.rc.tacos.server.dbal.sqlserver.DiseaseSqlService;
import at.rc.tacos.server.dbal.sqlserver.JobSqlService;
import at.rc.tacos.server.dbal.sqlserver.LinkSqlService;
import at.rc.tacos.server.dbal.sqlserver.LocationSqlService;
import at.rc.tacos.server.dbal.sqlserver.MobilePhoneSqlService;
import at.rc.tacos.server.dbal.sqlserver.PeriodsSqlService;
import at.rc.tacos.server.dbal.sqlserver.RosterSqlService;
import at.rc.tacos.server.dbal.sqlserver.ServiceTypeSqlService;
import at.rc.tacos.server.dbal.sqlserver.SickPersonSqlService;
import at.rc.tacos.server.dbal.sqlserver.StaffMemberSqlService;
import at.rc.tacos.server.dbal.sqlserver.TransportSqlService;
import at.rc.tacos.server.dbal.sqlserver.UserLoginSqlService;
import at.rc.tacos.server.dbal.sqlserver.VehicleDetailSqlService;

/**
 * All available services are registered in here
 * 
 * @author Michael
 */
public class ServiceFactoryImpl implements DbalServiceFactory {

	private static final HashMap<String, Object> SERVICE_MAP = new HashMap<String, Object>();

	// populate the services
	static {
		SERVICE_MAP.put(AddressService.class.getName(), new AddressSqlService());
		SERVICE_MAP.put(CallerService.class.getName(), new CallerSqlService());
		SERVICE_MAP.put(CompetenceService.class.getName(), new CompetenceSqlService());
		SERVICE_MAP.put(DayInfoService.class.getName(), new DayInfoSqlService());
		SERVICE_MAP.put(DialysisPatientService.class.getName(), new DialysisPatientSqlService());
		SERVICE_MAP.put(DiseaseService.class.getName(), new DiseaseSqlService());
		SERVICE_MAP.put(JobService.class.getName(), new JobSqlService());
		SERVICE_MAP.put(LinkService.class.getName(), new LinkSqlService());
		SERVICE_MAP.put(LocationService.class.getName(), new LocationSqlService());
		SERVICE_MAP.put(MobilePhoneService.class.getName(), new MobilePhoneSqlService());
		SERVICE_MAP.put(PeriodsSqlService.class.getName(), new PeriodsSqlService());
		SERVICE_MAP.put(RosterService.class.getName(), new RosterSqlService());
		SERVICE_MAP.put(ServiceTypeService.class.getName(), new ServiceTypeSqlService());
		SERVICE_MAP.put(SickPersonService.class.getName(), new SickPersonSqlService());
		SERVICE_MAP.put(StaffMemberService.class.getName(), new StaffMemberSqlService());
		SERVICE_MAP.put(TransportService.class.getName(), new TransportSqlService());
		SERVICE_MAP.put(AuthenticationService.class.getName(), new UserLoginSqlService());
		SERVICE_MAP.put(VehicleService.class.getName(), new VehicleDetailSqlService());
	}

	private Map<String, Object> serviceMap;

	/**
	 * Default class constructor
	 */
	public ServiceFactoryImpl() {
		serviceMap = new HashMap<String, Object>();
		serviceMap.putAll(SERVICE_MAP);
	}

	@Override
	public Object getService(String modelClazz) {
		if (modelClazz == null || modelClazz.equals("")) {
			return null;
		}
		return serviceMap.get(modelClazz);
	}

}
