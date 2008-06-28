package at.rc.tacos.common;

/**
 * The available program status for a transport, used by the transport view filter
 * @author b.thek
 */

public interface IProgramStatus 
{
	/** 0 - prebooking */
	public final static int PROGRAM_STATUS_PREBOOKING = 0;
	/** 1 - outstanding */
	public final static int PROGRAM_STATUS_OUTSTANDING = 1;
	/** 2 - underway */
	public final static int PROGRAM_STATUS_UNDERWAY = 2;
	/** 3 - journal */
	public final static int PROGRAM_STATUS_JOURNAL = 3;
}
