package at.rc.tacos.platform.iface;

/**
 * The available priorities for a transport
 * Available transport priorities in the system: A to G
 * The user shown transort priorities are 1 to 7
 * @author b.thek
 */
public interface ITransportPriority 
{
	/** A (1) NEF und RTW entsenden */
	public final static String TRANSPORT_PRIORITY_EMERGENCY_DOCTOR_INTERNAL = "A";	
	/** B (2) RTW mit BD1 entsenden */
	public final static String TRANSPORT_PRIORITY_BLUELIGHT = "B";	
	/** C (3) normaler Transport */
	public final static String TRANSPORT_PRIORITY_TRANSPORT = "C";	
	/** D (4) Rücktransport (von ambulant) */
	public final static String TRANSPORT_PRIORITY_BACK_TRANSPORT = "D";		
	/** E (5) Heimtransport (von stationär) */
	public final static String TRANSPORT_PRIORITY_HOME_TRANSPORT = "E";		
	/** F (6) Sonstiges (Dienstfahrten,...) */
	public final static String TRANSPORT_PRIORITY_OTHER = "F";	
	/** G (7) NEF für andere Bezirksstelle entsenden */
	public final static String TRANSPORT_PRIORITY_EMERGENCY_DOCTOR_EXTERNAL = "G";	
}

