package at.rc.tacos.platform.iface;


public interface IKindOfTransport 
{
	/** gehend*/
	public final static String TRANSPORT_KIND_GEHEND ="Gehend";
	/** Tragsessel*/
	public final static String TRANSPORT_KIND_TRAGSESSEL ="Tragsessel";
	/** Krankentrage*/
	public final static String TRANSPORT_KIND_KRANKENTRAGE ="Krankentrage";
	/** Eigener Rollstuhl*/
	public final static String TRANSPORT_KIND_ROLLSTUHL ="Eigener Rollstuhl";
	/** All */
	public final static String KINDS[] = { TRANSPORT_KIND_GEHEND, TRANSPORT_KIND_TRAGSESSEL, TRANSPORT_KIND_KRANKENTRAGE, TRANSPORT_KIND_ROLLSTUHL };
		
}
