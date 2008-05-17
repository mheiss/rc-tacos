package at.rc.tacos.web.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import at.rc.tacos.core.net.internal.WebClient;
import at.rc.tacos.model.Competence;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.web.session.UserSession;

/**
 * Add Staff Member Controller
 * @author Payer Martin
 * @version 1.0
 */
public class AddStaffMemberController extends Controller {
	
	private static final String ACTION_NAME = "action";
	private static final String ACTION_UPDATE_ROSTER_ENTRY = "addStaffMember";

	private static final String PARAM_PERSONNEL_NUMBER_NAME = "personnelNumber";
	private static final String MODEL_PERSONNEL_NUMBER_NAME = "personnelNumber";
	
	private static final String PARAM_FIRSTNAME_NAME = "firstName";
	private static final String MODEL_FIRSTNAME_NAME = "firstName";
	
	private static final String PARAM_LASTNAME_NAME = "lastName";
	private static final String MODEL_LASTNAME_NAME = "lastName";
	
	private static final String MODEL_CALENDAR_DEFAULT_DATE_MILLISECONDS_NAME = "calendarDefaultDateMilliseconds";
	private static final String MODEL_CALENDAR_RANGE_START_NAME = "calendarRangeStart";
	private static final String MODEL_CALENDAR_RANGE_END_NAME = "calendarRangeEnd";
	private static final int MODEL_CALENDAR_MAX_AGE= 10;
	
	private static final String PARAM_BIRTHDATE_NAME = "birthDate";
	private static final String MODEL_BIRTHDATE_NAME = "birthDate";
	
	private static final String PARAM_SEX_NAME = "sex";
	private static final String PARAM_SEX_NO_VALUE = "noValue";
	private static final String MODEL_SEX_NAME = "sex";
	
	private static final String PARAM_MOBILE_PHONE_TABLE_NAME = "mobilePhoneTable";
	private static final String MODEL_MOBILE_PHONE_LIST_NAME = "mobilePhoneList";
	private static final String MODEL_MOBILE_PHONE_TABLE_NAME = "mobilePhoneTable";
	
	private static final String PARAM_LOCATION_NAME = "locationId";
	private static final String PARAM_LOCATION_NO_VALUE = "noValue";
	private static final String MODEL_LOCATION_NAME = "location";
	private static final String MODEL_LOCATION_LIST_NAME = "locationList";
	
	private static final String PARAM_COMPETENCE_TABLE_NAME = "competenceTable";
	private static final String MODEL_COMPETENCE_LIST_NAME = "competenceList";
	private static final String MODEL_COMPETENCE_TABLE_NAME = "competenceTable";
	
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
		
		// Personnel Number
		String personnelNumber = null;
		if (request.getParameter(PARAM_PERSONNEL_NUMBER_NAME) != null) {
			personnelNumber = request.getParameter(PARAM_PERSONNEL_NUMBER_NAME);
		}
		params.put(MODEL_PERSONNEL_NUMBER_NAME, personnelNumber);
		
		// First Name
		String firstName = null;
		if (request.getParameter(PARAM_FIRSTNAME_NAME) != null) {
			firstName = request.getParameter(PARAM_FIRSTNAME_NAME);
		}
		params.put(MODEL_FIRSTNAME_NAME, firstName);
		
		// Last Name
		String lastName = null;
		if (request.getParameter(PARAM_LASTNAME_NAME) != null) {
			lastName = request.getParameter(PARAM_LASTNAME_NAME);
		}
		params.put(MODEL_LASTNAME_NAME, lastName);
		
		// Create Calendar for DatePicker
		final Calendar calendar = Calendar.getInstance();
		final int rangeStart = calendar.get(Calendar.YEAR) - MODEL_CALENDAR_MAX_AGE;
		final int rangeEnd = calendar.get(Calendar.YEAR);
		params.put(MODEL_CALENDAR_DEFAULT_DATE_MILLISECONDS_NAME, calendar.getTimeInMillis());
		params.put(MODEL_CALENDAR_RANGE_START_NAME, rangeStart);
		params.put(MODEL_CALENDAR_RANGE_END_NAME, rangeEnd);
		
		// Birthdate
		final SimpleDateFormat sdfBirthdate = new SimpleDateFormat("dd.MM.yyyy");		
		String birthdateString = null;		
		final String defaultBirthdateString = sdfBirthdate.format(userSession.getFormDefaultValues().getDefaultDate());		
		if (request.getParameter(PARAM_BIRTHDATE_NAME) != null) {
			birthdateString = request.getParameter(PARAM_BIRTHDATE_NAME);
		}
		if (birthdateString != null) {
			params.put(MODEL_BIRTHDATE_NAME, birthdateString);
		} else {
			params.put(MODEL_BIRTHDATE_NAME, defaultBirthdateString);
		}
		
		// Sex
		final String paramSex = request.getParameter(PARAM_SEX_NAME);
		final String defaultSex = null;
		String sex = null;
		if (paramSex != null && !paramSex.equals("") && !paramSex.equals(PARAM_SEX_NO_VALUE)) {
			if (paramSex.equals(StaffMember.STAFF_MALE)) {
				sex = paramSex;
			} else if (paramSex.equals(StaffMember.STAFF_FEMALE)) {
				sex = paramSex;
			}
		}
		if (sex != null || (paramSex != null && paramSex.equals(PARAM_SEX_NO_VALUE))) {
			params.put(MODEL_SEX_NAME, sex);
		} else {
			params.put(MODEL_SEX_NAME, defaultSex);
		}
		
		// Mobile Phone
		final String paramMobilePhoneTable = request.getParameter(PARAM_MOBILE_PHONE_TABLE_NAME);
		final List<MobilePhoneDetail> mobilePhoneTable = new ArrayList<MobilePhoneDetail>();
		final List<AbstractMessage> mobilePhoneList = connection.sendListingRequest(MobilePhoneDetail.ID, null);
		if (!MobilePhoneDetail.ID.equalsIgnoreCase(connection.getContentType())) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}
		params.put(MODEL_MOBILE_PHONE_LIST_NAME, mobilePhoneList);
		if (paramMobilePhoneTable != null) {
			final String[] mobilePhoneTableArray = paramMobilePhoneTable.split(",");
			for (int i=0; i < mobilePhoneTableArray.length; i++) {
				for (final Iterator<AbstractMessage> itMobilePhoneList = mobilePhoneList.iterator(); itMobilePhoneList.hasNext();) {
					final MobilePhoneDetail mobilePhone = (MobilePhoneDetail)itMobilePhoneList.next();
					if (mobilePhone.getId() == Integer.parseInt(mobilePhoneTableArray[i])) {
						mobilePhoneTable.add(mobilePhone);
					}
				}
			}
		}
		params.put(MODEL_MOBILE_PHONE_TABLE_NAME, mobilePhoneTable);
		
		// Parse Image
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			final DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(Integer.parseInt(fileUpload.getString("memory.maxsize")));
			factory.setRepository(new File(fileUpload.getString("tmp.dir")));
			final ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setSizeMax(Long.parseLong(fileUpload.getString("request.maxsize")));
			final List<FileItem> items = upload.parseRequest(request);
			
			// Process the uploaded items
			final Iterator<FileItem> iter = items.iterator();
			while (iter.hasNext()) {
			    final FileItem item = (FileItem) iter.next();
			    if (!item.isFormField()) {
			        final String contentType = item.getContentType();
			        final String fileName = item.getName();
			        //final String extension = "." + fileName.replaceAll(".*\\.", "");
			        long sizeInBytes = item.getSize();
			        if (sizeInBytes > Long.parseLong(fileUpload.getString("addStaffMember.image.maxsize"))) {
			        	throw new IllegalArgumentException("Error: Uploaded image is too big.");
			        }
			        if (!Pattern.matches(fileUpload.getString("addStaffMember.image.contentType"), contentType)) {
			        	throw new IllegalArgumentException("Error: Uploaded image has wrong content type.");
			        }
			        final File uploadedFile = new File(fileUpload.getString("addStaffMember.image.absolute.dir") + "/" + userSession.getLoginInformation().getUserInformation().getStaffMemberId());
			        item.write(uploadedFile);
			    } 
			}
		}
		
		// Location
		final String paramLocationId = request.getParameter(PARAM_LOCATION_NAME);
		int locationId = 0;
		final Location defaultLocation = userSession.getFormDefaultValues().getDefaultLocation();
		Location location = null;
		if (paramLocationId != null && !paramLocationId.equals("") && !paramLocationId.equals(PARAM_LOCATION_NO_VALUE)) {
			locationId = Integer.parseInt(paramLocationId);
		}
		final List<AbstractMessage> locationList = connection.sendListingRequest(Location.ID, null);
		if (!Location.ID.equalsIgnoreCase(connection.getContentType())) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}
		params.put(MODEL_LOCATION_LIST_NAME, locationList);
		if (location != null || (paramLocationId != null && paramLocationId.equals(PARAM_LOCATION_NO_VALUE))) {
			params.put(MODEL_LOCATION_NAME, location);
		} else {
			params.put(MODEL_LOCATION_NAME, defaultLocation);
		}
		
		// Competences
		final List<AbstractMessage> competenceList = connection.sendListingRequest(Competence.ID, null);
		if (!Competence.ID.equalsIgnoreCase(connection.getContentType())) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}
		params.put(MODEL_COMPETENCE_LIST_NAME, competenceList);
		 
		
		return params;
	}

}
