package at.rc.tacos.common;

/**
 * The available status messages for a transport, set by time.
 * Below the status types for the visialisation are shown
 * - green if : 0,1,2,5,6
 * - yellow if : 2,4,9
 * - red if: 3,7 
 * @author b.thek
 */
public interface ITransportStatus 
{
	/** Auftrag erteilt */
	public final static int TRANSPORT_STATUS_ORDER_PLACED = 0;
	/** Fahrzeug unterwegs */
	public final static int TRANSPORT_STATUS_ON_THE_WAY = 1;
	/** Fahrzeug bei Patient */
	public final static int TRANSPORT_STATUS_AT_PATIENT = 2;
	/** Abfahrt mit Patient */
	public final static int TRANSPORT_STATUS_START_WITH_PATIENT = 3;
	/** Ankunft Ziel */
	public final static int TRANSPORT_STATUS_AT_DESTINATION = 4;
	/** Ziel frei */
	public final static int TRANSPORT_STATUS_DESTINATION_FREE = 5;
	/** eingerückt */
	public final static int TRANSPORT_STATUS_CAR_IN_STATION = 6;
	/** verlässt Einsatzgebiet */
	public final static int TRANSPORT_STATUS_OUT_OF_OPERATION_AREA = 7;
	/** Wieder im Einsatzgebiet */
	public final static int TRANSPORT_STATUS_BACK_IN_OPERATION_AREA = 8;
	/** Sonderstatus, Sonstiges (z.B. Ambulanz, Essen,...) */
	public final static int TRANSPORT_STATUS_OTHER = 9;
}

