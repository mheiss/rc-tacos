package at.rc.tacos.core.model;

/**
 * The available priorities for a transport
 * @author b.thek
 */
public interface ITransportPriority 
{
	/** Priority of transport*/
	public final static String TRANSPORT_PRIORITY_EMERGENCY_DOCTOR_INTERNAL = "A";	//NEF und RTW entsenden
	public final static String TRANSPORT_PRIORITY_BLUELIGHT = "B";					//RTW mit BD1 entsenden
	public final static String TRANSPORT_PRIORITY_TRANSPORT = "C";					//normaler Transport
	public final static String TRANSPORT_PRIORITY_BACK_TRANSPORT = "D";				//Rücktransport (von ambulant)
	public final static String TRANSPORT_PRIORITY_HOME_TRANSPORT = "E";				//Heimtransport (von stationär)
	public final static String TRANSPORT_PRIORITY_OTHER = "F";						//Sonstiges (Dienstfahrten,...)
	public final static String TRANSPORT_PRIORITY_EMERGENCY_DOCTOR_EXTERNAL = "G";	//NEF für andere Bezirksstelle entsenden
}

