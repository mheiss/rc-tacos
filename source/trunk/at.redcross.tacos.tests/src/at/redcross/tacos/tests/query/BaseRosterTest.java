package at.redcross.tacos.tests.query;

import java.util.Date;

import at.redcross.tacos.dbal.entity.RosterEntry;
import at.redcross.tacos.dbal.query.RosterQueryParam;

/** Provides common helper method for roster based tests */
public abstract class BaseRosterTest extends BaseEntityTest {

	/** Returns a new parameter using the given date */
	protected RosterQueryParam getParam(Date date) {
		RosterQueryParam param = new RosterQueryParam();
		param.startDate = date;
		return param;
	}

	/** Returns whether or not the string matches the planned start date time */
	protected boolean checkStartDate(String expected, RosterEntry entry) throws Exception {
		Date acutal = entry.getPlannedStartDateTime();
		return DateHelper.parseDateTime(expected).compareTo(acutal) == 0;
	}

}
