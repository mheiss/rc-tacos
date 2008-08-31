package at.rc.tacos.common;


/**
 * The possible direction for a transport.
 * @author b.thek
 */
public interface IDirectness 
{

	/** Richtung Bezirk (default, wenn nichts anderes angegeben*/
	public final static int TOWARDS_BRUCK = 1;	
	/** Richtung Graz */
	public final static int TOWARDS_GRAZ = 2;	
	/** Richtung Leoben */
	public final static int TOWARDS_LEOBEN = 3;	
	/** Richtung Wien */
	public final static int TOWARDS_VIENNA = 4;		
	/** Richtung Mariazell*/
	public final static int TOWARDS_MARIAZELL = 5;
	/** Richtung Mariazell*/
	public final static int TOWARDS_KAPFENBERG = 6;
		
}
