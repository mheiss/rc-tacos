package at.rc.tacos.model;

/**
 * The available priorities for a transport
 * @author b.thek
 */
public interface ITransportPriority 
{
	/** NEF und RTW entsenden */
	public final static String TRANSPORT_PRIORITY_EMERGENCY_DOCTOR_INTERNAL = "A";	
	/** RTW mit BD1 entsenden */
	public final static String TRANSPORT_PRIORITY_BLUELIGHT = "B";	
	/** normaler Transport */
	public final static String TRANSPORT_PRIORITY_TRANSPORT = "C";	
	/** Rücktransport (von ambulant) */
	public final static String TRANSPORT_PRIORITY_BACK_TRANSPORT = "D";		
	/** Heimtransport (von stationär) */
	public final static String TRANSPORT_PRIORITY_HOME_TRANSPORT = "E";		
	/** Sonstiges (Dienstfahrten,...) */
	public final static String TRANSPORT_PRIORITY_OTHER = "F";	
	/** NEF für andere Bezirksstelle entsenden */
	public final static String TRANSPORT_PRIORITY_EMERGENCY_DOCTOR_EXTERNAL = "G";	
}

