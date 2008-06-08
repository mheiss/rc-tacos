package at.rc.tacos.web.form;

/**
 * Container for Day
 * @author Payer Martin
 * @version 1.0
 */
public class Day {
	private int day;
	private int dayOfWeek;

	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public Day(int day) {
		this.day = day;
	}
	
	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}
}
