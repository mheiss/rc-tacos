package at.rc.tacos.web.container;

import java.util.Date;

/**
 * Container for Day
 * @author Payer Martin
 * @version 1.0
 */
public class Day {
	private int day;
	private int dayOfWeek;
	private Date dateOfDay;

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

	public Date getDateOfDay() {
		return dateOfDay;
	}

	public void setDateOfDay(Date dateOfDay) {
		this.dateOfDay = dateOfDay;
	}
}
