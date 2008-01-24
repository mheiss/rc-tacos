package at.rc.tacos.client.util;

import at.rc.tacos.common.ITransportStatus;

public class CalculateMostImportantTransportStatus implements ITransportStatus
{

	private int left;
	private int right;
	
	public CalculateMostImportantTransportStatus(int left, int right)
	{
		this.left = left;
		this.right = right;
	}
	
	public int calculate()
	{
		//TODO: vor Aufruf dieser Methode:
		//TransportStatusCheck muss für jeden Transport ausgeführt werden. Die jeweiligen Ergebnisse werden hier verwendet.
		//Ausgangspunkt für calculate: der jeweils wichtigste Status aller Transporte von einem (!) Fahrzeug (!) wird hier verwendet. (Programmstatus "underway")
		//Wenn ein MITS (most important transport status) S3 ist --> neu = S3
		//Wenn ein MITS S7 ist --> neu = S7
		//Wenn alle MITS gleich sind --> neu = einer der alten
		
		//S0 - S1
		if(left == TRANSPORT_STATUS_ORDER_PLACED && right == TRANSPORT_STATUS_ON_THE_WAY)
			return TRANSPORT_STATUS_ON_THE_WAY;
		//S0 - S2
		if(left == TRANSPORT_STATUS_ORDER_PLACED && right == TRANSPORT_STATUS_AT_PATIENT)
			return TRANSPORT_STATUS_AT_PATIENT;
		
		//...... TODO
		
		
		return 1;//TODO -> delete
	}
}
