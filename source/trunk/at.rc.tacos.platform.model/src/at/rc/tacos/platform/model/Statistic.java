package at.rc.tacos.platform.model;

/**
 * Represents one statistic
 * 
 * @author b.thek
 */
public class Statistic {

	private int statisticId;
	private int hours;

	/**
	 * Default class construtor
	 */
	public Statistic() {
		statisticId = -1;
	}

	// GETTERS AND SETTERS
	/**
	 * @return the statisticId
	 */
	public int getStatisticId() {
		return statisticId;
	}

	/**
	 * @param statisticId
	 *            the statisticId to set
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
	 * @param hours
	 *            the hours to set
	 */
	public void setHours(int hours) {
		this.hours = hours;
	}
}
