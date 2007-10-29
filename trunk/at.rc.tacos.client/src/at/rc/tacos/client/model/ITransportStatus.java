package at.rc.tacos.client.model;

/**
 * The available status messages for a transport, set by time
 * @author b.thek
 */
public interface ITransportStatus 
{
	/** Status of transport*/
	public final static int TRANSPORT_STATUS_ORDER_PLACED = 0;//Auftrag erteilt
	public final static int TRANSPORT_STATUS_ON_THE_WAY = 1;//Fahrzeug unterwegs
	public final static int TRANSPORT_STATUS_AT_PATIENT = 2;//Fahrzeug bei Patient
	public final static int TRANSPORT_STATUS_START_WITH_PATIENT = 3;//Abfahrt mit Patient
	public final static int TRANSPORT_STATUS_AT_DESTINATION = 4;//Ankunft Ziel
	public final static int TRANSPORT_STATUS_DESTINATION_FREE = 5;//Ziel frei
	public final static int TRANSPORT_STATUS_CAR_IN_STATION = 6;//eingerückt
	public final static int TRANSPORT_STATUS_OUT_OF_OPERATION_AREA = 7;//Verlässt Einsatzgebiet
	public final static int TRANSPORT_STATUS_BACK_IN_OPERATION_AREA = 8;//Wieder im Einsatzgebiet
	public final static int TRANSPORT_STATUS_OTHER = 9;//Sonderstatus, Sonstiges (z.B. Ambulanz, Essen,...)
}

