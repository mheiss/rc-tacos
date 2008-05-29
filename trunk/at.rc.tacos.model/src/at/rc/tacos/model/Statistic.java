package at.rc.tacos.model;

import at.rc.tacos.common.AbstractMessage;

/**
 * Represents one statistic 
 * @author b.thek
 */
public class Statistic extends AbstractMessage
{
	//unique identification string
	public final static String ID = "statistic";
	
	//Properties    
	private int statisticId;
	private int hours;
	
	

	public int getHours() 
	{
		return hours;
	}



	public void setHours(int hours) 
	{
		this.hours = hours;
	}



	/**
	 * Default class construtor
	 */
	public Statistic()
	{
		super(ID);
		//set default values
		statisticId = -1;
	}

	
}

