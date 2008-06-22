package at.rc.tacos.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.rc.tacos.core.net.internal.WebClient;
import at.rc.tacos.model.DayInfoMessage;
import at.rc.tacos.model.Login;
import at.rc.tacos.web.session.UserSession;

/**
 * Message Of The Day Controller
 * @author Payer Martin
 * @version 1.0
 */
public class MessageOfTheDayController extends Controller {
	
	private static final String ACTION_NAME = "action";
	private static final String ACTION_ADD_MESSAGE_OF_THE_DAY = "addMessageOfTheDay";

	private static final String PARAM_MESSAGE_OF_THE_DAY = "messageOfTheDay";
	private static final String MODEL_MESSAGE_OF_THE_DAY = "messageOfTheDay";
	
	private static final String MODEL_CALENDAR_DEFAULT_DATE_MILLISECONDS_NAME = "calendarDefaultDateMilliseconds";
	private static final String MODEL_CALENDAR_RANGE_START_NAME = "calendarRangeStart";
	private static final String MODEL_CALENDAR_RANGE_END_NAME = "calendarRangeEnd";
	private static final int MODEL_CALENDAR_RANGE_START_OFFSET = 10;
	private static final int MODEL_CALENDAR_RANGE_END_OFFSET = 1;
	
	private static final String PARAM_DATE_NAME = "date";
	private static final String MODEL_DATE_NAME = "date";
	
	private static final String MODEL_ERRORS_NAME = "errors";
	
	private static final String ERRORS_MESSAGE_OF_THE_DAY_MISSING = "messageOfTheDayMissing";
	private static final String ERRORS_MESSAGE_OF_THE_DAY_MISSING_VALUE = "Vorname ist ein Pflichtfeld.";
	
	private static final String MODEL_ADDED_COUNT_NAME = "addedCount";
	
	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request,
			HttpServletResponse response, ServletContext context)
			throws Exception {
		
		final Map<String, Object> params = new HashMap<String, Object>();
		
		final UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		final WebClient connection = userSession.getConnection();
		
		final String authorization = userSession.getLoginInformation().getAuthorization();	
		
		// Check authorization
		if (!authorization.equals(Login.AUTH_ADMIN)) {
			throw new IllegalArgumentException("Error: User has no permission for functionality.");
		}
		
		// Parse Parameters
		String paramAction = request.getParameter(ACTION_NAME);
		String paramMessageOfTheDay = request.getParameter(PARAM_MESSAGE_OF_THE_DAY);
		String paramMessageOfTheDayDate = request.getParameter(PARAM_DATE_NAME);
		
		// Get Date and create calendar for datepicker
		Date date = userSession.getDefaultFormValues().getDefaultDate();
		if (date == null) {
			date = new Date();
		}
		final Calendar calendar = Calendar.getInstance();
		final int rangeStart = calendar.get(Calendar.YEAR) - MODEL_CALENDAR_RANGE_START_OFFSET;
		final int rangeEnd = calendar.get(Calendar.YEAR) + MODEL_CALENDAR_RANGE_END_OFFSET;
		
		params.put(MODEL_CALENDAR_DEFAULT_DATE_MILLISECONDS_NAME, date.getTime());	
		params.put(MODEL_CALENDAR_RANGE_START_NAME, rangeStart);
		params.put(MODEL_CALENDAR_RANGE_END_NAME, rangeEnd);
		
		final Calendar rangeStartCalendar = Calendar.getInstance();
		rangeStartCalendar.set(Calendar.YEAR, rangeStartCalendar.get(Calendar.YEAR) - MODEL_CALENDAR_RANGE_START_OFFSET);
		
		final Calendar rangeEndCalendar = Calendar.getInstance();
		rangeEndCalendar.set(Calendar.YEAR, rangeEndCalendar.get(Calendar.YEAR) + MODEL_CALENDAR_RANGE_END_OFFSET);
		
		final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		
		Date dateTemp = null;
		if (paramMessageOfTheDayDate != null) {
			try {
				dateTemp = df.parse(paramMessageOfTheDayDate);
			}
			catch (ParseException e) {
				
			}
			if (dateTemp != null && dateTemp.getTime() < rangeStartCalendar.getTimeInMillis() || dateTemp.getTime() > rangeEndCalendar.getTimeInMillis()) {
				//throw new IllegalArgumentException();
			} else {
				date = dateTemp;
			}
		}
		params.put(MODEL_DATE_NAME, date);
		
		// Message of the day
		final String defaultMessageOfTheDay = (String)request.getAttribute("messageOfTheDay");
		String messageOfTheDay = null;
		messageOfTheDay = paramMessageOfTheDay;
		if (messageOfTheDay != null) {
			params.put(MODEL_MESSAGE_OF_THE_DAY, messageOfTheDay);
		} else {
			params.put(MODEL_MESSAGE_OF_THE_DAY, defaultMessageOfTheDay);
		}

		// Do Action
		final String action = paramAction;
		final Map<String, String> errors = new HashMap<String, String>();
		boolean valid = true;
		if (action != null && action.equals(ACTION_ADD_MESSAGE_OF_THE_DAY)) {
	        		
			// Validate Message Of The Day
			if (messageOfTheDay == null || messageOfTheDay.trim().equals("")) {
				valid = false;
				errors.put(ERRORS_MESSAGE_OF_THE_DAY_MISSING, ERRORS_MESSAGE_OF_THE_DAY_MISSING_VALUE);
			}				
      
			if (valid) {				
				
				final DayInfoMessage msgOfTheDay = new DayInfoMessage();
				
				msgOfTheDay.setMessage(messageOfTheDay);
				msgOfTheDay.setLastChangedBy(userSession.getLoginInformation().getUsername());
				
				if (date != null) {
					msgOfTheDay.setTimestamp(date.getTime());
				}
					        
				connection.sendUpdateRequest(DayInfoMessage.ID, msgOfTheDay);
				if(!connection.getContentType().equalsIgnoreCase(DayInfoMessage.ID)) {
					throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
				}
				
				params.put(MODEL_ADDED_COUNT_NAME, 1);
			}
		}
		
		params.put(MODEL_ERRORS_NAME, errors);
		return params;
	}

}