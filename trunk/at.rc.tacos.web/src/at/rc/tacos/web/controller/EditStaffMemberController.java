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
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.web.session.UserSession;
import at.rc.tacos.web.utils.ValidationPatterns;

/**
 * Edit Staff Member Controller
 * @author Payer Martin
 * @version 1.0
 */
public class EditStaffMemberController extends Controller {

	private static final String PARAM_STAFF_MEMBER_NAME = "staffMemberId";
	private static final String MODEL_STAFF_MEMBER_NAME = "staffMember";
	private static final String MODEL_STAFF_MEMBER_LIST_NAME = "staffMemberList";
	
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
	
	private static final String MODEL_PHOTO_PATH_NAME = "photo";
	
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
	
	private static final int STAFF_MEMBER_MAX_AGE = 100;
	
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
		String paramStaffMemberId = null;
		String paramAction = null;
		String paramPersonnelNumber = null;
		String paramFirstName = null;
		String paramLastName = null;
		String paramBirthdate = null;
		String paramSex = null;
		String paramMobilePhoneId = null;
		String paramMobilePhoneHidden = null;
		FileItem photo = null;
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
			    	if (item.getSize() > 0) {
			    		photo = item;
			    		params.put(MODEL_PHOTO_PATH_NAME, item.getName());
			    	}
			    } else {
			    	if (item.getFieldName().equals(PARAM_STAFF_MEMBER_NAME)) {
			    		paramStaffMemberId = item.getString();	    		
			    	} else if (item.getFieldName().equals(ACTION_NAME)) {
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
		
		// Staff Member List
		int staffMemberId = 0;
		StaffMember staffMember = userSession.getDefaultFormValues().getStaffMemberDefaultStaffMember();
		if (paramStaffMemberId != null && !paramStaffMemberId.equals("")) {
			if (paramStaffMemberId.equalsIgnoreCase("noValue")) {
				staffMember = null;
			} else {
				staffMemberId = Integer.parseInt(paramStaffMemberId);
			}
		}
		final List<AbstractMessage> staffMemberList = connection.sendListingRequest(StaffMember.ID, null);
		if (!Location.ID.equalsIgnoreCase(connection.getContentType())) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}
		params.put(MODEL_STAFF_MEMBER_LIST_NAME, staffMemberList);
		for (final Iterator<AbstractMessage> itStaffMemberList = staffMemberList.iterator(); itStaffMemberList.hasNext();) {
			final StaffMember sm = (StaffMember)itStaffMemberList.next();
			if (sm.getStaffMemberId() == staffMemberId) {
				staffMember = sm;
			}
		}
		userSession.getDefaultFormValues().setStaffMemberDefaultStaffMember(staffMember);
		params.put(MODEL_STAFF_MEMBER_NAME, staffMember);
		
		// Personnel Number
		final String defaultPersonnelNumber = null;
		String personnelNumber = null;
		personnelNumber = paramPersonnelNumber;
		if (personnelNumber != null) {
			params.put(MODEL_PERSONNEL_NUMBER_NAME, personnelNumber);
		} else {
			params.put(MODEL_PERSONNEL_NUMBER_NAME, defaultPersonnelNumber);
		}
		
		// First Name
		final String defaultFirstName = null;
		String firstName = null;
		firstName = paramFirstName;
		if (firstName != null) {
			params.put(MODEL_FIRSTNAME_NAME, firstName);
		} else {
			params.put(MODEL_FIRSTNAME_NAME, defaultFirstName);
		}
		
		// Last Name
		final String defaultLastName = null;
		String lastName = null;
		lastName = paramLastName;
		if (lastName != null) {
			params.put(MODEL_LASTNAME_NAME, lastName);
		} else {
			params.put(MODEL_LASTNAME_NAME, defaultLastName);
		}
		
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
		if (mobilePhone != null || (paramMobilePhoneId != null && paramMobilePhoneId.equals(PARAM_MOBILE_PHONE_NO_VALUE))) {
			params.put(MODEL_MOBILE_PHONE_NAME, mobilePhone);
		} else {
			params.put(MODEL_MOBILE_PHONE_NAME, defaultMobilePhone);
		}
		
		params.put(MODEL_MOBILE_PHONE_LIST_NAME, mobilePhoneList);
		final List<MobilePhoneDetail> mobilePhoneTable = new ArrayList<MobilePhoneDetail>();
		final List<MobilePhoneDetail> defaultMobilePhoneTable = new ArrayList<MobilePhoneDetail>();
		String mobilePhoneHidden = null;
		String defaultMobilePhoneHidden = null;
		if (paramMobilePhoneHidden != null && !paramMobilePhoneHidden.equals("")) {
			final String[] paramMobilePhoneTableArray = paramMobilePhoneHidden.split(",");
			for (int i=0; i < paramMobilePhoneTableArray.length; i++) {
				for (final Iterator<AbstractMessage> itMobilePhoneList = mobilePhoneList.iterator(); itMobilePhoneList.hasNext();) {
					final MobilePhoneDetail mb = (MobilePhoneDetail)itMobilePhoneList.next();
					if (mb.getId() == Integer.parseInt(paramMobilePhoneTableArray[i])) {
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
		if (mobilePhoneTable.size() > 0) {
			params.put(MODEL_MOBILE_PHONE_TABLE_NAME, mobilePhoneTable);
		} else {
			params.put(MODEL_MOBILE_PHONE_TABLE_NAME, defaultMobilePhoneTable);
		}
		if (mobilePhoneHidden != null) {
			params.put(MODEL_MOBILE_PHONE_HIDDEN_NAME, mobilePhoneHidden);
		} else {
			params.put(MODEL_MOBILE_PHONE_HIDDEN_NAME, defaultMobilePhoneHidden);
		}

		
		// Location
		int locationId = 0;
		final Location defaultLocation = userSession.getDefaultFormValues().getRosterDefaultLocation();
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
		Competence volunteerCompetence = null;
		if (paramCompetenceId != null && !paramCompetenceId.equals("") && !paramCompetenceId.equals(PARAM_COMPETENCE_NO_VALUE)) {
			competenceId = Integer.parseInt(paramCompetenceId);
		}
		final List<AbstractMessage> competenceList = connection.sendListingRequest(Competence.ID, null);
		if (!Competence.ID.equalsIgnoreCase(connection.getContentType())) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}
		for (final Iterator<AbstractMessage> itCompetenceL = competenceList.iterator(); itCompetenceL.hasNext();) {
			final Competence c = (Competence)itCompetenceL.next();
			if (c.getId() == competenceId) {
				competence = c;
			}
			if (c.getCompetenceName().equals(Competence.COMPETENCE_NAME_VOLUNTEER)) {
				volunteerCompetence = c;
			}
		}
		params.put(MODEL_COMPETENCE_LIST_NAME, competenceList);
		if (competence != null || (paramCompetenceId != null && paramCompetenceId.equals(PARAM_COMPETENCE_NO_VALUE))) {
			params.put(MODEL_COMPETENCE_NAME, competence);
		} else {
			params.put(MODEL_COMPETENCE_NAME, defaultCompetence);
		}
		
		final List<Competence> competenceTable = new ArrayList<Competence>();
		final List<Competence> defaultCompetenceTable = new ArrayList<Competence>();
		String competenceHidden = null;
		String defaultCompetenceHidden = null;
		defaultCompetenceTable.add(volunteerCompetence);
		for (final Iterator<Competence> itCompTable = defaultCompetenceTable.iterator(); itCompTable.hasNext();) {
			final Competence co = (Competence)itCompTable.next();
			if (defaultCompetenceHidden == null) {
				defaultCompetenceHidden = Integer.toString(co.getId());
			} else {
				defaultCompetenceHidden = defaultCompetenceHidden + "," + Integer.toString(co.getId());
			}
		}
		if (paramCompetenceHidden != null && !paramCompetenceHidden.equals("")) {
			final String[] paramCompetenceTableArray = paramCompetenceHidden.split(",");
			for (int i=0; i < paramCompetenceTableArray.length; i++) {
				for (final Iterator<AbstractMessage> itCompetence = competenceList.iterator(); itCompetence.hasNext();) {
					final Competence co = (Competence)itCompetence.next();
					if (co.getId() == Integer.parseInt(paramCompetenceTableArray[i])) {
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
		if (competenceHidden != null) {
			params.put(MODEL_COMPETENCE_HIDDEN_NAME, competenceHidden);
		} else {
			params.put(MODEL_COMPETENCE_HIDDEN_NAME, defaultCompetenceHidden);
		}
		if (competenceTable.size() > 0) {
			params.put(MODEL_COMPETENCE_TABLE_NAME, competenceTable);
		} else {
			params.put(MODEL_COMPETENCE_TABLE_NAME, defaultCompetenceTable);
		}
		
		// Username
		final String defaultUsername = null;
		String username = null;
		username = paramUsername;
		if (username != null) {
			params.put(MODEL_USERNAME_NAME, username);
		} else {
			params.put(MODEL_USERNAME_NAME, defaultUsername);
		}
		
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
	        
			// Validate personnel number
			if (personnelNumber == null || personnelNumber.trim().equals("")) {
				valid = false;
				errors.put("personnelNumberMissing", "Personalnummer ist ein Pflichtfeld.");
			} else if (!Pattern.matches(ValidationPatterns.STAFF_MEMBER_ID_VALIDATION_PATTERN, personnelNumber)) {
				valid = false;
				errors.put("personnelNumber", "Die angegebene Nummer hat nicht das richtige Format.");
			} else {		
				// Check double staff member Ids
				final QueryFilter staffMemberIdFilter = new QueryFilter();
				staffMemberIdFilter.add(IFilterTypes.ID_FILTER, personnelNumber);
				final List<AbstractMessage> doubleStaffMemberIdList = connection.sendListingRequest(StaffMember.ID, staffMemberIdFilter);
				if (!StaffMember.ID.equalsIgnoreCase(connection.getContentType())) {
					throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
				}
				if (doubleStaffMemberIdList.size() > 0) {
					valid = false;
					errors.put("personnelNumberExists", "Die angegebene Personalnummer existiert bereits.");
				}
			}
			
			// Validate firstname
			if (firstName == null || firstName.trim().equals("")) {
				valid = false;
				errors.put("firstNameMissing", "Vorname ist ein Pflichtfeld.");
			} else if (firstName.length() > 30) {
				valid = false;
				errors.put("firstNameTooLong", "Vorname ist zu lang. Es sind maximal 30 Zeichen erlaubt.");
			}
			
			// Validate lastname
			if (lastName == null || lastName.trim().equals("")) {
				valid = false;
				errors.put("lastNameMissing", "Nachname ist ein Pflichtfeld.");
			} else if (lastName.length() > 30) {
				valid = false;
				errors.put("lastNameTooLong", "Nachname ist zu lang. Es sind maximal 30 Zeichen erlaubt.");
			}
			
			// Validate birthdate
			final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
			Date birthdate = null;
			
			final Calendar rangeStartCalendar = Calendar.getInstance();
			rangeStartCalendar.set(Calendar.YEAR, rangeStartCalendar.get(Calendar.YEAR) - STAFF_MEMBER_MAX_AGE);
			
			final Calendar rangeEndCalendar = Calendar.getInstance();
			rangeEndCalendar.set(Calendar.YEAR, rangeEndCalendar.get(Calendar.YEAR));
			
			if (birthdateString != null && !birthdateString.trim().equals("")) {
				try {
					birthdate = df.parse(birthdateString);
				} catch (ParseException e) {
					valid = false;
					errors.put("birthdate", "Das Datumsformat von Geburtsdatum ist nicht korrekt.");
				}
				
				if (birthdate != null) {
					if (birthdate.getTime() < rangeStartCalendar.getTimeInMillis()) {
						valid = false;
						errors.put("birthdateTooSmall", "Der Wert von Geburtsdatum ist zu klein.");
					}
					
					if (birthdate.getTime() > rangeEndCalendar.getTimeInMillis()) {
						valid = false;
						errors.put("birthdateTooBig", "Der Wert von Geburtsdatum ist zu gro�.");
					}
				}
			}
						
			// Validate sex
			if (sex == null) {
				valid = false;
				errors.put("sex", "Geschlecht ist ein Pflichtfeld.");
			}
						
			// Validate photo
			if (photo != null) {
				final String contentType = photo.getContentType();
		        final String fileName = photo.getName();
		        long sizeInBytes = photo.getSize();
		        if (sizeInBytes > Long.parseLong(fileUpload.getString("addStaffMember.photo.maxsize"))) {
		        	valid = false;
		        	errors.put("photoTooBig", "Die angegebene Datei ist zu gro�.");
		        } else if (!Pattern.matches(fileUpload.getString("addStaffMember.photo.contentType"), contentType)) {
		        	valid = false;
		        	errors.put("photoWrongFormat", "Die angegebene Datei hat das falsche Format.");
		        }
			}
	        
			// Validate location
	        if (location == null) {
	        	valid = false;
	        	errors.put("location", "Dienststelle ist ein Pflichtfeld.");
	        }
			
			// Validate competences
	        boolean volunteerFound = false;
	        for (final Iterator<Competence> itCT = competenceTable.iterator(); itCT.hasNext();) {
	        	final Competence co = itCT.next();
	        	if (co.getCompetenceName().equals(Competence.COMPETENCE_NAME_VOLUNTEER)) {
	        		volunteerFound = true;
	        	}
	        }
	        if (!volunteerFound) {
	        	valid = false;
	        	errors.put("competences", "Kompetenz Volont�r ist verpflichtend.");
	        }
	        
	        // Validate username
	        if (username == null || username.trim().equals("")) {
	        	valid = false;
	        	errors.put("usernameMissing", "Benutzername ist ein Pflichtfeld.");
	        } else if (username.length() > 30) {
	        	valid = false;
	        	errors.put("usernameTooLong", "Benutzername ist zu lang. Es sind maximal 30 Zeichen erlaubt.");
	        }
	        
			// Check double username
			final QueryFilter loginUsernameFilter = new QueryFilter();
			loginUsernameFilter.add(IFilterTypes.USERNAME_FILTER, username);
			final List<AbstractMessage> doubleUsername = connection.sendListingRequest(Login.ID, loginUsernameFilter);
			if (!Login.ID.equalsIgnoreCase(connection.getContentType())) {
				throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
			}
			if (doubleUsername.size() > 0) {
				valid = false;
				errors.put("usernameExists", "Der angegebene Benutzername existiert bereits.");
			}
	        
	        // Validate password
	        if (password == null || password.trim().equals("")) {
	        	valid = false;
	        	errors.put("passwordMissing", "Passwort ist ein Pflichtfeld");
	        } else if (password.length() > 255) {
	        	valid = false;
	        	errors.put("passwordTooLong", "Passwort ist zu lang. Es sind maximal 255 Zeichen erlaubt.");
	        }
	        
	        // Validate repeated password
	        if (repeatedPassword == null || repeatedPassword.trim().equals("")) {
	        	valid = false;
	        	errors.put("repeatedPasswordMissing", "Passwort wiederholen ist ein Pflichtfeld.");
	        } else if (repeatedPassword.length() > 255) {
	        	valid = false;
	        	errors.put("repeatedPasswordTooLong", "Passwort wiederholen ist zu lang. Es sind maximal 255 Zeichen erlaubt.");
	        }
	        
	        // Validate passwords
	        if (!password.equals(repeatedPassword)) {
	        	valid = false;
	        	errors.put("passwordsNotEqual", "Die zwei eingegebenen Passw�rter stimmen nicht �bereich.");
	        }
	        
	        // Validate authorization
	        if (staffMemberAuthorization == null) {
	        	valid = false;
	        	errors.put("authorization", "Authorisierung ist ein Pflichtfeld.");
	        }
	        
			if (valid) {				
				// Create Staff Member
				final Login login = new Login();
				
				// Create login for staff member
		        
		        login.setUsername(username);
		        
		        login.setPassword(password);
		        
		        login.setIslocked(lockUser);
		        
		        login.setAuthorization(staffMemberAuthorization);
				
				staffMember.setStaffMemberId(Integer.parseInt(personnelNumber));
				
				staffMember.setFirstName(firstName);
				
				staffMember.setLastName(lastName);
				
				if (birthdate != null) {
					final SimpleDateFormat dfServer = new SimpleDateFormat("dd-MM-yyyy");
					staffMember.setBirthday(dfServer.format(birthdate));
				}
				
				if (sex.equals(StaffMember.STAFF_MALE)) {
					staffMember.setMale(true);
				} else if (sex.equals(StaffMember.STAFF_FEMALE)) {
					staffMember.setMale(false);
				}
				
				staffMember.setPhonelist(mobilePhoneTable);
				
				// Write photo to disk
				if (photo != null) {
			        final File uploadedFile = new File(fileUpload.getString("addStaffMember.photo.absolute.dir") + "/" + userSession.getLoginInformation().getUserInformation().getStaffMemberId());
			        photo.write(uploadedFile);
				}
		        
		        staffMember.setPrimaryLocation(location);
		        
		        staffMember.setCompetenceList(competenceTable);
		        
		        staffMember.setUserName(username);
		        
		        login.setUserInformation(staffMember);
		        
				connection.sendAddRequest(Login.ID, login);
				if(!connection.getContentType().equalsIgnoreCase(Login.ID)) {
					throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
				}
		        
				connection.sendAddRequest(StaffMember.ID, staffMember);
				if(!connection.getContentType().equalsIgnoreCase(StaffMember.ID)) {
					throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
				}
				
				userSession.getDefaultFormValues().setStaffMemberDefaultStaffMember(staffMember);
				
				params.put("addedCount", 1);
			}
		}
		
		// Delete temporary path to image
		if (photo != null) {
			photo.delete();
		}
		
		params.put("errors", errors);
		return params;
	}

}