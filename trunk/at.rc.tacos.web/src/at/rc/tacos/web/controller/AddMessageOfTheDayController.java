package at.rc.tacos.web.controller;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.net.internal.WebClient;
import at.rc.tacos.model.Competence;
import at.rc.tacos.model.DayInfoMessage;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.web.session.UserSession;
import at.rc.tacos.web.utils.ValidationPatterns;

/**
 * Add Staff Member Controller
 * @author Payer Martin
 * @version 1.0
 */
public class AddMessageOfTheDayController extends Controller {
	
	private static final String ACTION_NAME = "action";
	private static final String ACTION_ADD_MESSAGE_OF_THE_DAY = "addMessageOfTheDay";

	
	private static final String PARAM_MESSAGE_OF_THE_DAY = "messageOfTheDay";
	private static final String MODEL_MESSAGE_OF_THE_DAY = "messageOfTheDay";
	
	
	private static final String MODEL_CALENDAR_DEFAULT_DATE_MILLISECONDS_NAME = "calendarDefaultDateMilliseconds";
	private static final String MODEL_CALENDAR_RANGE_START_NAME = "calendarRangeStart";
	private static final String MODEL_CALENDAR_RANGE_END_NAME = "calendarRangeEnd";
	private static final int MODEL_CALENDAR_MAX_AGE= 100;
	
	private static final String PARAM_MESSAGE_OF_THE_DAY_DATE = "messageOfTheDayDate";
	private static final String MODEL_MESSAGE_OF_THE_DAY_DATE = "messageOfTheDayDate";
	


	
	private static final String MODEL_ERRORS_NAME = "errors";
	

	
	private static final String ERRORS_MESSAGE_OF_THE_DAY_MISSING = "firstNameMissing";
	private static final String ERRORS_MESSAGE_OF_THE_DAY_MISSING_VALUE = "Vorname ist ein Pflichtfeld.";
	private static final String ERRORS_MESSAGE_OF_THE_DAY_TOO_LONG = "firstNameTooLong";
	private static final String ERRORS_MESSAGE_OF_THE_DAY_TOO_LONG_VALUE = "Vorname ist zu lang. Es sind maximal 300 Zeichen erlaubt.";

	
	private static final String ERRORS_DATE = "date";
	private static final String ERRORS_DATE_VALUE = "Das Datumsformat von Datum ist nicht korrekt.";
	private static final String ERRORS_DATE_TOO_SMALL = "DateTooSmall";
	private static final String ERRORS_DATE_TOO_SMALL_VALUE = "Der Wert von Datum ist zu klein.";
	private static final String ERRORS_DATE_TOO_BIG = "DateTooBig";
	private static final String ERRORS_DATE_TOO_BIG_VALUE = "Der Wert von Datum ist zu groﬂ.";
	

	
	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request,
			HttpServletResponse response, ServletContext context)
			throws Exception {
		final ResourceBundle fileUpload = ResourceBundle.getBundle(Dispatcher.FILEUPLOAD_BUNDLE_PATH);
		
		final Map<String, Object> params = new HashMap<String, Object>();
		
		final UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		final WebClient connection = userSession.getConnection();
		
		final String authorization = userSession.getLoginInformation().getAuthorization();	
		
		// Check authorization
		if (!authorization.equals(Login.AUTH_ADMIN)) {
			throw new IllegalArgumentException("Error: User has no permission for functionality.");
		}
		
		// Parse Parameters	
		String paramAction = null;
		String paramMessageOfTheDay = null;
		String paramMessageOfTheDayDate = null;

		
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			final DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(Integer.parseInt(fileUpload.getString("memory.maxsize")));
			factory.setRepository(new File(fileUpload.getString("tmp.dir")));
			final ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setSizeMax(Long.parseLong(fileUpload.getString("request.maxsize")));
			final List<FileItem> items = upload.parseRequest(request);
			final Iterator<FileItem> iter = items.iterator();
			while (iter.hasNext()) {
			    final FileItem item = (FileItem) iter.next();
			    if (!item.isFormField()) {
			    	
			    } else {
			    	if (item.getFieldName().equals(ACTION_NAME)) {
			    		paramAction = item.getString();
			    	} else if (item.getFieldName().equals(PARAM_MESSAGE_OF_THE_DAY)) {
			    		paramMessageOfTheDay = item.getString();
			    	} else if (item.getFieldName().equals(PARAM_MESSAGE_OF_THE_DAY_DATE)) {
			    		paramMessageOfTheDayDate = item.getString();
			    	}
			    }
			}
		}
		

		
		// First Name
		final String defaultMessageOfTheDay = null;
		String messageOfTheDay = null;
		messageOfTheDay = paramMessageOfTheDay;
		if (messageOfTheDay != null) {
			params.put(MODEL_MESSAGE_OF_THE_DAY, messageOfTheDay);
		} else {
			params.put(MODEL_MESSAGE_OF_THE_DAY, defaultMessageOfTheDay);
		}
		
		
		// Create Calendar for DatePicker
		final Calendar calendar = Calendar.getInstance();
		final int rangeStart = calendar.get(Calendar.YEAR) - MODEL_CALENDAR_MAX_AGE;
		final int rangeEnd = calendar.get(Calendar.YEAR);
		params.put(MODEL_CALENDAR_DEFAULT_DATE_MILLISECONDS_NAME, calendar.getTimeInMillis());
		params.put(MODEL_CALENDAR_RANGE_START_NAME, rangeStart);
		params.put(MODEL_CALENDAR_RANGE_END_NAME, rangeEnd);
		
		// date
		final SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy");		
		String dateString = null;		
		final String defaultDateString = null;		
		if (paramMessageOfTheDayDate != null) {
			dateString = paramMessageOfTheDayDate;
		}
		if (dateString != null) {
			params.put(MODEL_MESSAGE_OF_THE_DAY_DATE, dateString);
		} else {
			params.put(MODEL_MESSAGE_OF_THE_DAY_DATE, defaultDateString);
		}
		

		// Do Action
		final String action = paramAction;
		final Map<String, String> errors = new HashMap<String, String>();
		boolean valid = true;
		if (action != null && action.equals(ACTION_ADD_MESSAGE_OF_THE_DAY)) {
	        
			
			
			// Validate message
			if (messageOfTheDay == null || messageOfTheDay.trim().equals("")) {
				valid = false;
				errors.put(ERRORS_MESSAGE_OF_THE_DAY_MISSING, ERRORS_MESSAGE_OF_THE_DAY_MISSING_VALUE);
			} else if (messageOfTheDay.length() > 300) {
				valid = false;
				errors.put(ERRORS_MESSAGE_OF_THE_DAY_TOO_LONG, ERRORS_MESSAGE_OF_THE_DAY_TOO_LONG_VALUE);
			}
			
			
			// Validate date
			final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
			Date date = null;
			
			final Calendar rangeStartCalendar = Calendar.getInstance();
			rangeStartCalendar.set(Calendar.YEAR, rangeStartCalendar.get(Calendar.YEAR) - MODEL_CALENDAR_MAX_AGE);
			
			final Calendar rangeEndCalendar = Calendar.getInstance();
			rangeEndCalendar.set(Calendar.YEAR, rangeEndCalendar.get(Calendar.YEAR));
			
			if (dateString != null && !dateString.trim().equals("")) {
				try {
					date = df.parse(dateString);
				} catch (ParseException e) {
					valid = false;
					errors.put(ERRORS_DATE, ERRORS_DATE_VALUE);
				}
				
				if (date != null) {
					if (date.getTime() < rangeStartCalendar.getTimeInMillis()) {
						valid = false;
						errors.put(ERRORS_DATE_TOO_SMALL, ERRORS_DATE_TOO_SMALL_VALUE);
					} else if (date.getTime() > rangeEndCalendar.getTimeInMillis()) {
						valid = false;
						errors.put(ERRORS_DATE_TOO_BIG, ERRORS_DATE_TOO_BIG_VALUE);
					}
				}
			}
						
			
	        
			if (valid) {				
				// Create DayInfoMessage
				
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
				
//				userSession.getDefaultFormValues().setDefaultStaffMember(staffMember);
//				userSession.getDefaultFormValues().setDefaultLocation(location);
				
//				params.put(MODEL_ADDED_COUNT_NAME, 1);
			}
		}
		
		params.put(MODEL_ERRORS_NAME, errors);
		return params;
	}

}