package at.rc.tacos.server.net;

import java.util.HashMap;
import java.util.Map;

import at.rc.tacos.platform.model.Address;
import at.rc.tacos.platform.model.CallerDetail;
import at.rc.tacos.platform.model.Competence;
import at.rc.tacos.platform.model.DayInfoMessage;
import at.rc.tacos.platform.model.DialysisPatient;
import at.rc.tacos.platform.model.Disease;
import at.rc.tacos.platform.model.Job;
import at.rc.tacos.platform.model.Link;
import at.rc.tacos.platform.model.Location;
import at.rc.tacos.platform.model.Login;
import at.rc.tacos.platform.model.MobilePhoneDetail;
import at.rc.tacos.platform.model.Period;
import at.rc.tacos.platform.model.RosterEntry;
import at.rc.tacos.platform.model.ServiceType;
import at.rc.tacos.platform.model.SickPerson;
import at.rc.tacos.platform.model.StaffMember;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.model.VehicleDetail;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.HandlerFactory;
import at.rc.tacos.server.net.handler.AddressHandler;
import at.rc.tacos.server.net.handler.AuthenticationHandler;
import at.rc.tacos.server.net.handler.CompetenceHandler;
import at.rc.tacos.server.net.handler.DayInfoHandler;
import at.rc.tacos.server.net.handler.DialysisPatientHandler;
import at.rc.tacos.server.net.handler.DiseaseHandler;
import at.rc.tacos.server.net.handler.JobHandler;
import at.rc.tacos.server.net.handler.LinkHandler;
import at.rc.tacos.server.net.handler.LocationHandler;
import at.rc.tacos.server.net.handler.LockHandler;
import at.rc.tacos.server.net.handler.MobilePhoneHandler;
import at.rc.tacos.server.net.handler.NotifyDetailHandler;
import at.rc.tacos.server.net.handler.PeriodHandler;
import at.rc.tacos.server.net.handler.RosterHandler;
import at.rc.tacos.server.net.handler.ServiceTypeHandler;
import at.rc.tacos.server.net.handler.SickPersonHandler;
import at.rc.tacos.server.net.handler.StaffMemberHandler;
import at.rc.tacos.server.net.handler.TransportHandler;
import at.rc.tacos.server.net.handler.VehicleHandler;

/**
 * The handler factory returns the appropriate handler implementations based
 * uppon the request.
 * 
 * @author Michael
 */
@SuppressWarnings("unchecked")
public class HandlerFactoryImpl implements HandlerFactory {

	private static final HashMap<String, Handler<?>> HANDLER_MAP = new HashMap<String, Handler<?>>();

	// populate the default handlers
	static {
		HANDLER_MAP.put(Address.class.getName(), new AddressHandler());
		HANDLER_MAP.put(Login.class.getName(), new AuthenticationHandler());
		HANDLER_MAP.put(Competence.class.getName(), new CompetenceHandler());
		HANDLER_MAP.put(DayInfoMessage.class.getName(), new DayInfoHandler());
		HANDLER_MAP.put(DialysisPatient.class.getName(), new DialysisPatientHandler());
		HANDLER_MAP.put(Disease.class.getName(), new DiseaseHandler());
		HANDLER_MAP.put(Job.class.getName(), new JobHandler());
		HANDLER_MAP.put(Link.class.getName(), new LinkHandler());
		HANDLER_MAP.put(Location.class.getName(), new LocationHandler());
		HANDLER_MAP.put(LockHandler.class.getName(), new LockHandler());
		HANDLER_MAP.put(MobilePhoneDetail.class.getName(), new MobilePhoneHandler());
		HANDLER_MAP.put(CallerDetail.class.getName(), new NotifyDetailHandler());
		HANDLER_MAP.put(Period.class.getName(), new PeriodHandler());
		HANDLER_MAP.put(RosterEntry.class.getName(), new RosterHandler());
		HANDLER_MAP.put(ServiceType.class.getName(), new ServiceTypeHandler());
		HANDLER_MAP.put(SickPerson.class.getName(), new SickPersonHandler());
		HANDLER_MAP.put(StaffMember.class.getName(), new StaffMemberHandler());
		HANDLER_MAP.put(Transport.class.getName(), new TransportHandler());
		HANDLER_MAP.put(VehicleDetail.class.getName(), new VehicleHandler());
	}

	private Map<String, Handler<?>> handlerMap;

	/**
	 * Default class constructor
	 */
	public HandlerFactoryImpl() {
		handlerMap = new HashMap<String, Handler<?>>();
		handlerMap.putAll(HANDLER_MAP);
	}

	/**
	 * Returns a type save handler instance for the given model clazz
	 * 
	 * @param modelClazz
	 *            the clazz of the model object to get the handler
	 */
	@Override
	public <T> Handler<T> getHandler(T modelClazz) {
		Handler<T> handler = (Handler<T>) handlerMap.get(modelClazz.getClass().getName());
		return handler;
	}
}
