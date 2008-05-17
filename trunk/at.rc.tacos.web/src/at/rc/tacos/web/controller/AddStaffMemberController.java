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
	private static final String ACTION_ADD_STAFF_MEMBER = "addStaffMember";

	private static final String PARAM_PERSONNEL_NUMBER_NAME = "personnelNumber";
	private static final String MODEL_PERSONNEL_NUMBER_NAME = "personnelNumber";
	
	private static final String PARAM_FIRSTNAME_NAME = "firstName";
	private static final String MODEL_FIRSTNAME_NAME = "firstName";
	
	private static final String PARAM_LASTNAME_NAME = "lastName";
	private static final String MODEL_LASTNAME_NAME = "lastName";
	
	private static final String MODEL_CALENDAR_DEFAULT_DATE_MILLISECONDS_NAME = "calendarDefaultDateMilliseconds";
	private static final String MODEL_CALENDAR_RANGE_START_NAME = "calendarRangeStart";
	private static final String MODEL_CALENDAR_RANGE_END_NAME = "calendarRangeEnd";
	private static final int MODEL_CALENDAR_MAX_AGE= 100;
	
	private static final String PARAM_BIRTHDATE_NAME = "birthDate";
	private static final String MODEL_BIRTHDATE_NAME = "birthDate";
	
	private static final String PARAM_SEX_NAME = "sex";
	private static final String PARAM_SEX_NO_VALUE = "noValue";
	private static final String MODEL_SEX_NAME = "sex";
	
	private static final String PARAM_MOBILE_PHONE_NAME = "mobilePhoneId";
	private static final String PARAM_MOBILE_PHONE_NO_VALUE = "noValue";
	private static final String PARAM_MOBILE_PHONE_HIDDEN_NAME = "mobilePhoneIds";
	private static final String MODEL_MOBILE_PHONE_NAME = "mobilePhone";
	private static final String MODEL_MOBILE_PHONE_HIDDEN_NAME = "mobilePhoneIds";
	private static final String MODEL_MOBILE_PHONE_LIST_NAME = "mobilePhoneList";
	private static final String MODEL_MOBILE_PHONE_TABLE_NAME = "mobilePhoneTable";
	
	private static final String PARAM_LOCATION_NAME = "locationId";
	private static final String PARAM_LOCATION_NO_VALUE = "noValue";
	private static final String MODEL_LOCATION_NAME = "location";
	private static final String MODEL_LOCATION_LIST_NAME = "locationList";
	
	private static final String PARAM_COMPETENCE_NAME = "competenceId";
	private static final String PARAM_COMPETENCE_NO_VALUE = "noValue";
	private static final String PARAM_COMPETENCE_HIDDEN_NAME = "competenceIds";
	private static final String MODEL_COMPETENCE_NAME = "competence";
	private static final String MODEL_COMPETENCE_HIDDEN_NAME = "competenceIds";
	private static final String MODEL_COMPETENCE_LIST_NAME = "competenceList";
	private static final String MODEL_COMPETENCE_TABLE_NAME = "competenceTable";
	
	private static final String PARAM_USERNAME_NAME = "username";
	private static final String MODEL_USERNAME_NAME = "username";
	
	private static final String PARAM_PASSWORD_NAME = "password";
	
	private static final String PARAM_REPEATED_PASSWORD_NAME = "repeatedPassword";
	
	private static final String PARAM_LOCK_USER_NAME = "lockUser";
	private static final String MODEL_LOCK_USER_NAME = "lockUser";
	
	private static final String PARAM_AUTHORIZATION_NAME = "authorization";
	private static final String PARAM_AUTHORIZATION_NO_VALUE = "noValue";
	private static final String MODEL_AUTHORIZATION_NAME = "authorization";
	
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
		String paramPersonnelNumber = null;
		String paramFirstName = null;
		String paramLastName = null;
		String paramBirthdate = null;
		String paramSex = null;
		String paramMobilePhoneId = null;
		String paramMobilePhoneHidden = null;
		FileItem image = null;
		String paramLocationId = null;
		String paramCompetenceId = null;
		String paramCompetenceHidden = null;
		String paramUsername = null;
		String paramPassword = null;
		String paramRepeatedPassword = null;
		String paramLockUser = null;
		String paramStaffMemberAuthorization = null;
		
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
			        image = item;
			    } else {
			    	if (item.getFieldName().equals(ACTION_NAME)) {
			    		paramAction = item.getString();
			    	} else if (item.getFieldName().equals(PARAM_PERSONNEL_NUMBER_NAME)) {
			    		paramPersonnelNumber = item.getString();
			    	} else if (item.getFieldName().equals(PARAM_FIRSTNAME_NAME)) {
			    		paramFirstName = item.getString();
			    	} else if (item.getFieldName().equals(PARAM_LASTNAME_NAME)) {
			    		paramLastName = item.getString();
			    	} else if (item.getFieldName().equals(PARAM_BIRTHDATE_NAME)) {
			    		paramBirthdate = item.getString();
			    	} else if (item.getFieldName().equals(PARAM_SEX_NAME)) {
			    		paramSex = item.getString();
			    	} else if (item.getFieldName().equals(PARAM_MOBILE_PHONE_NAME)) {
			    		paramMobilePhoneId = item.getString();
			    	} else if (item.getFieldName().equals(PARAM_MOBILE_PHONE_HIDDEN_NAME)) {
			    		paramMobilePhoneHidden = item.getString();
			    	} else if (item.getFieldName().equals(PARAM_LOCATION_NAME)) {
			    		paramLocationId = item.getString();
			    	} else if (item.getFieldName().equals(PARAM_COMPETENCE_NAME)) {
			    		paramCompetenceId = item.getString();
			    	} else if (item.getFieldName().equals(PARAM_COMPETENCE_HIDDEN_NAME)) {
			    		paramCompetenceHidden = item.getString();
			    	} else if (item.getFieldName().equals(PARAM_USERNAME_NAME)) {
			    		paramUsername = item.getString();
			    	} else if (item.getFieldName().equals(PARAM_PASSWORD_NAME)) {
			    		paramPassword = item.getString();
			    	} else if (item.getFieldName().equals(PARAM_REPEATED_PASSWORD_NAME)) {
			    		paramRepeatedPassword = item.getString();
			    	} else if (item.getFieldName().equals(PARAM_LOCK_USER_NAME)) {
			    		paramLockUser = item.getString();
			    	} else if (item.getFieldName().equals(PARAM_AUTHORIZATION_NAME)) {
			    		paramStaffMemberAuthorization = item.getString();
			    	}
			    }
			}
		}
		
		// Personnel Number
		String personnelNumber = null;
		if (paramPersonnelNumber != null) {
			personnelNumber = paramPersonnelNumber;
		}
		params.put(MODEL_PERSONNEL_NUMBER_NAME, personnelNumber);
		
		// First Name
		String firstName = null;
		if (paramFirstName != null) {
			firstName = paramFirstName;
		}
		params.put(MODEL_FIRSTNAME_NAME, firstName);
		
		// Last Name
		String lastName = null;
		if (paramLastName != null) {
			lastName = paramLastName;
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
		final String defaultBirthdateString = null;		
		if (paramBirthdate != null) {
			birthdateString = paramBirthdate;
		}
		if (birthdateString != null) {
			params.put(MODEL_BIRTHDATE_NAME, birthdateString);
		} else {
			params.put(MODEL_BIRTHDATE_NAME, defaultBirthdateString);
		}
		
		// Sex
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
		int mobilePhoneId = 0;
		final MobilePhoneDetail defaultMobilePhone = null;
		MobilePhoneDetail mobilePhone = null;
		if (paramMobilePhoneId != null && !paramMobilePhoneId.equals("") && !paramMobilePhoneId.equals(PARAM_MOBILE_PHONE_NO_VALUE)) {
			mobilePhoneId = Integer.parseInt(paramMobilePhoneId);
		}
		final List<MobilePhoneDetail> mobilePhoneTable = new ArrayList<MobilePhoneDetail>();
		String mobilePhoneHidden = null;
		final List<AbstractMessage> mobilePhoneList = connection.sendListingRequest(MobilePhoneDetail.ID, null);
		if (!MobilePhoneDetail.ID.equalsIgnoreCase(connection.getContentType())) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}
		for (final Iterator<AbstractMessage> itMobilePhoneL = mobilePhoneList.iterator(); itMobilePhoneL.hasNext();) {
			final MobilePhoneDetail m = (MobilePhoneDetail)itMobilePhoneL.next();
			if (m.getId() == mobilePhoneId) {
				mobilePhone = m;
			}
		}
		params.put(MODEL_MOBILE_PHONE_LIST_NAME, mobilePhoneList);
		if (paramMobilePhoneHidden != null && !paramMobilePhoneHidden.equals("")) {
			final String[] mobilePhoneTableArray = paramMobilePhoneHidden.split(",");
			for (int i=0; i < mobilePhoneTableArray.length; i++) {
				for (final Iterator<AbstractMessage> itMobilePhoneList = mobilePhoneList.iterator(); itMobilePhoneList.hasNext();) {
					final MobilePhoneDetail mb = (MobilePhoneDetail)itMobilePhoneList.next();
					if (mb.getId() == Integer.parseInt(mobilePhoneTableArray[i])) {
						mobilePhoneTable.add(mb);
						if (mobilePhoneHidden == null) {
							mobilePhoneHidden = Integer.toString(mb.getId());
						} else {
							mobilePhoneHidden = mobilePhoneHidden + "," + Integer.toString(mb.getId());
						}
					}
				}
			}
		}
		params.put(MODEL_MOBILE_PHONE_TABLE_NAME, mobilePhoneTable);
		params.put(MODEL_MOBILE_PHONE_HIDDEN_NAME, mobilePhoneHidden);
		if (mobilePhone != null || (paramMobilePhoneId != null && paramMobilePhoneId.equals(PARAM_MOBILE_PHONE_NO_VALUE))) {
			params.put(MODEL_MOBILE_PHONE_NAME, mobilePhone);
		} else {
			params.put(MODEL_MOBILE_PHONE_NAME, defaultMobilePhone);
		}
		
		// Location
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
		for (final Iterator<AbstractMessage> itLoactionList = locationList.iterator(); itLoactionList.hasNext();) {
			final Location l = (Location)itLoactionList.next();
			if (l.getId() == locationId) {
				location = l;
			}
		}
		params.put(MODEL_LOCATION_LIST_NAME, locationList);
		if (location != null || (paramLocationId != null && paramLocationId.equals(PARAM_LOCATION_NO_VALUE))) {
			params.put(MODEL_LOCATION_NAME, location);
		} else {
			params.put(MODEL_LOCATION_NAME, defaultLocation);
		}
		
		// Competences
		int competenceId = 0;
		final Competence defaultCompetence = null;
		Competence competence = null;
		if (paramCompetenceId != null && !paramCompetenceId.equals("") && !paramCompetenceId.equals(PARAM_COMPETENCE_NO_VALUE)) {
			competenceId = Integer.parseInt(paramCompetenceId);
		}
		final List<Competence> competenceTable = new ArrayList<Competence>();
		String competenceHidden = null;
		final List<AbstractMessage> competenceList = connection.sendListingRequest(Competence.ID, null);
		if (!Competence.ID.equalsIgnoreCase(connection.getContentType())) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}
		for (final Iterator<AbstractMessage> itCompetenceL = competenceList.iterator(); itCompetenceL.hasNext();) {
			final Competence c = (Competence)itCompetenceL.next();
			if (c.getId() == competenceId) {
				competence = c;
			}
		}
		params.put(MODEL_COMPETENCE_LIST_NAME, competenceList);
		if (paramCompetenceHidden != null && !paramCompetenceHidden.equals("")) {
			final String[] competenceTableArray = paramCompetenceHidden.split(",");
			for (int i=0; i < competenceTableArray.length; i++) {
				for (final Iterator<AbstractMessage> itCompetence = competenceList.iterator(); itCompetence.hasNext();) {
					final Competence co = (Competence)itCompetence.next();
					if (co.getId() == Integer.parseInt(competenceTableArray[i])) {
						competenceTable.add(co);
						if (competenceHidden == null) {
							competenceHidden = Integer.toString(co.getId());
						} else {
							competenceHidden = competenceHidden + "," + Integer.toString(co.getId());
						}
					}
				}
			}
			
		}
		params.put(MODEL_COMPETENCE_HIDDEN_NAME, competenceHidden);
		params.put(MODEL_COMPETENCE_TABLE_NAME, competenceTable);
		if (competence != null || (paramCompetenceId != null && paramCompetenceId.equals(PARAM_COMPETENCE_NO_VALUE))) {
			params.put(MODEL_COMPETENCE_NAME, competence);
		} else {
			params.put(MODEL_COMPETENCE_NAME, defaultCompetence);
		}
		
		// Username
		String username = null;
		if (paramUsername != null) {
			username = paramUsername;
		}
		params.put(MODEL_USERNAME_NAME, username);
		
		// Password
		String password = null;
		if (paramPassword != null) {
			password = paramPassword;
		}
		
		// Repeated Password
		String repeatedPassword = null;
		if (paramRepeatedPassword != null) {
			repeatedPassword = paramRepeatedPassword;
		}
		
		// Lock User
		boolean lockUser = false;
		if (paramLockUser != null) {
			lockUser = true;
		}
		params.put(MODEL_LOCK_USER_NAME, lockUser);
		
		// Authorization for New Staff Member
		final String defaultStaffMemberAuthorization = null;
		String staffMemberAuthorization = null;
		if (paramStaffMemberAuthorization != null && !paramStaffMemberAuthorization.equals("") && !paramStaffMemberAuthorization.equals(PARAM_AUTHORIZATION_NO_VALUE)) {
			if (paramStaffMemberAuthorization.equals(Login.AUTH_USER)) {
				staffMemberAuthorization = paramStaffMemberAuthorization;
			} else if (paramStaffMemberAuthorization.equals(Login.AUTH_ADMIN)) {
				staffMemberAuthorization = paramStaffMemberAuthorization;
			}
		}
		if (staffMemberAuthorization != null || (paramStaffMemberAuthorization != null && paramStaffMemberAuthorization.equals(PARAM_AUTHORIZATION_NO_VALUE))) {
			params.put(MODEL_AUTHORIZATION_NAME, staffMemberAuthorization);
		} else {
			params.put(MODEL_AUTHORIZATION_NAME, defaultStaffMemberAuthorization);
		}
		
		// Do Action
		final String action = paramAction;
		final Map<String, String> errors = new HashMap<String, String>();
		boolean valid = true;
		if (action != null && action.equals(ACTION_ADD_STAFF_MEMBER)) {
	        /*final String contentType = image.getContentType();
	        final String fileName = image.getName();
	        long sizeInBytes = image.getSize();
	        if (sizeInBytes > Long.parseLong(fileUpload.getString("addStaffMember.image.maxsize"))) {
	        	throw new IllegalArgumentException("Error: Uploaded image is too big.");
	        }
	        if (!Pattern.matches(fileUpload.getString("addStaffMember.image.contentType"), contentType)) {
	        	throw new IllegalArgumentException("Error: Uploaded image has wrong content type.");
	        }*/
	        
			valid = false;
			
			if (valid) {
				// Write image to disk
		        final File uploadedFile = new File(fileUpload.getString("addStaffMember.image.absolute.dir") + "/" + userSession.getLoginInformation().getUserInformation().getStaffMemberId());
		        image.write(uploadedFile);
		        image.delete();
			}
		}
		
		
		params.put("errors", errors);
		return params;
	}

}
