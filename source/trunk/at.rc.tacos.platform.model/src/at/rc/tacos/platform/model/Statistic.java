package at.rc.tacos.platform.model;

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

	/**
	 * Default class construtor
	 */
	public Statistic()
	{
		super(ID);
		//set default values
		statisticId = -1;
	}

	//GETTERS AND SETTERS
	/**
	 * @return the statisticId
	 */
	public int getStatisticId() {
		return statisticId;
	}

	/**
	 * @param statisticId the statisticId to set
	 */
	public void setStatisticId(int statisticId) {
		this.statisticId = statisticId;
	}

	/**
	 * @return the hours
	 */
	public int getHours() {
		return hours;
	}

	/**
	 * @param hours the hours to set
	 */
	public void setHours(int hours) {
		this.hours = hours;
	}
}

