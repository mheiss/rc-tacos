package at.rc.tacos.core.db.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.dao.TransportDAO;
import at.rc.tacos.model.*;

public class TransportDAOMySQL implements TransportDAO
{
	public static final String QUERIES_BUNDLE_PATH = "at.rc.tacos.core.db.queries";

	private String convertDate (long date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		String stringdate = sdf.format(cal.getTime());
		
		return stringdate;
	}
	
	private long convertDateIntoLong (String stringdate)
	{
		long date=0;
        try
        {
            DateFormat dateFormat =  new SimpleDateFormat("yyyyMMddhhmmss"); 
            date = dateFormat.parse(stringdate).getTime();
        }
        catch(ParseException pe)
        {
            System.out.println("Failed to parse the given date");
            System.out.println(pe.getMessage());
        }
        return date;
	}
	
	@Override
	public String addTransport(Transport transport, int staffmemberVehicleId)
	{
		String transportId = null;
		CallerDetail caller = transport.getCallerDetail();
		Patient patient = transport.getPatient();
		
		try
		{	
//			transportNr, direction_ID, caller_ID, note, createdBy_user, priority, feedback, creationDate, departure,
//			appointment, appointmentPatient, assistant, staffmember_vehicle_ID, transporttype, transportstate, disease, firstname,
//			lastname, planned_location, return_transport, from_street, from_city, to_street, to_city, programstate
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("insert.transport"));
			query.setLong(1, transport.getTransportNumber());
			query.setInt(2, transport.getDirection());
			query.setInt(3, caller.getCallerId());
			query.setString(4, "note");
			query.setString(5, "created by ... ?");
			query.setString(6, transport.getTransportPriority());
			query.setString(7, transport.getFeedback());
			query.setString(8, convertDate(transport.getReceiveTime()));
			query.setString(9, convertDate(transport.getPlannedStartOfTransport()));
			query.setString(10, convertDate(transport.getAppointmentTimeAtDestination()));
			query.setString(11, convertDate(transport.getPlannedTimeAtPatient()));
			query.setString(12, transport.isAccompanyingPerson());
			query.setInt(13, staffmemberVehicleId);
			query.setString(14, transport.getKindOfTransport());
			query.setInt(15, 1); //Transportstate ?
			query.setString(16, transport.getKindOfIllness());
			query.setString(17, patient.getFirstname());
			query.setString(18, patient.getLastname());
			query.setInt(19, transport.getResponsibleStation());
			query.setString(20, null); //return transport ?
			query.setString(21, transport.getFromStreet());
			query.setString(22, transport.getFromCity());
			query.setString(23, transport.getToStreet());
			query.setString(24, transport.getToCity());
			query.setInt(25, transport.getProgramStatus());
						
			query.executeUpdate();
			
			// t.creationDate, t.lastname, to_street, staffmember_vehicle_ID, appointment
			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.transportID"));
			query1.setString(1, convertDate(transport.getDateOfTransport()));
			query1.setString(2, patient.getLastname());
			query1.setString(3, transport.getToStreet());
			query1.setInt(4, 1); // staffmember_vehicle_ID ?
			query1.setString(5, convertDate(transport.getAppointmentTimeAtDestination()));
			final ResultSet rsId = query1.executeQuery();
			
			if(rsId.next())
				transportId = rsId.getString("transport_ID");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return transportId;
	}

	@Override
	public Transport getTransportByNr(int transportId, int locationId)
	{
		Transport transport = new Transport();
    	
    	try
    	{
		final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.transportByNr"));
		query.setInt(1, transportId);
		query.setInt(2, locationId);
		final ResultSet rs = query.executeQuery();

		rs.first();
//		t.transport_ID, t.transportNr, t.direction_ID, d.direction, t.disease, t.feedback, t.creationDate, t.departure, t.appointment, t.appointmentPatient, 
//		t.assistant, t.transportstate, ca.callername, ca.caller_phonenumber, t.firstname, t.lastname, t.from_street, t.from_city, t.to_street, 
//		t.to_city, t.return_transport, t.planned_location, t.priority, t.transporttype
		transport.setTransportId(rs.getInt("t.transport_ID"));
		transport.setTransportNumber(rs.get("t.transportNr"));
		transport.setDirection(rs.getInt("t.direction_ID"));
		transport.setKindOfIllness(rs.getString("t.disease"));
		transport.setFeedback(rs.getString("t.feedback"));
		transport.setReceiveTime(convertDateIntoLong(rs.getString("creationDate")));
		transport.setPlannedStartOfTransport(convertDateIntoLong(rs.getString("t.departure")));
		transport.setAppointmentTimeAtDestination(convertDateIntoLong(rs.getString("t.appointment")));
		transport.setPlannedTimeAtPatient(convertDateIntoLong(rs.getString("t.appointmentPatient")));
		transport.setAccompanyingPerson(rs.getBoolean("t.assistant"));
		//transportstate
		CallerDetail callerDetail = new CallerDetail();
		callerDetail.setCallerName(rs.getString("ca.callername"));
		callerDetail.setCallerTelephoneNumber(rs.getString("ca.caller_phonenumber"));
		transport.setCallerDetail(callerDetail);
		Patient patient = new Patient();
		patient.setFirstname(rs.getString("t.firstname"));
		patient.setLastname(rs.getString("t.lastname"));
		transport.setPatient(patient);
		transport.setFromStreet(rs.getString("t.from_street"));
		transport.setFromCity(rs.getString("t.from_city"));
		transport.setToStreet(rs.getString("t.to_street"));
		transport.setToCity(rs.getString("t.to_city"));
		//return_transport
		transport.setResponsibleStation(rs.getInt("t.planned_location"));
		transport.setTransportPriority(rs.getString("t.priority"));
		transport.setKindOfTransport(rs.getString("t.transporttype"));
		
	}
	catch (SQLException e)
	{
		e.printStackTrace();
		return null;
	}
	return transport;
	}

	@Override
	public List<Transport> listTransports() {
		// TODO Sinnlos!!??
		return null;
	}

	@Override
	public List<Transport> listTransports(long startdate, long enddate)
	{
		List<Transport> transports = null;
		Transport transport = new Transport();
    	
    	try
    	{
		final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.transports"));
		query.setString(1, convertDate(startdate));
		query.setString(2, convertDate(enddate));
		final ResultSet rs = query.executeQuery();

		while(rs.next())
		{
//		t.transport_ID, t.transportNr, t.direction_ID, d.direction, t.disease, t.feedback, t.creationDate, t.departure, t.appointment, t.appointmentPatient, 
//		t.assistant, t.transportstate, ca.callername, ca.caller_phonenumber, t.firstname, t.lastname, t.from_street, t.from_city, t.to_street, 
//		t.to_city, t.return_transport, t.planned_location, t.priority, t.transporttype
		transport.setTransportId(rs.getInt("t.transport_ID"));
		transport.setTransportNumber(rs.getInt("t.transportNr"));
		transport.setDirection(rs.getInt("t.direction_ID"));
		transport.setKindOfIllness(rs.getString("t.disease"));
		transport.setFeedback(rs.getString("t.feedback"));
		transport.setReceiveTime(convertDateIntoLong(rs.getString("creationDate")));
		transport.setPlannedStartOfTransport(convertDateIntoLong(rs.getString("t.departure")));
		transport.setAppointmentTimeAtDestination(convertDateIntoLong(rs.getString("t.appointment")));
		transport.setPlannedTimeAtPatient(convertDateIntoLong(rs.getString("t.appointmentPatient")));
		transport.setAccompanyingPerson(rs.getBoolean("t.assistant"));
		//transportstate
		CallerDetail callerDetail = new CallerDetail();
		callerDetail.setCallerName(rs.getString("ca.callername"));
		callerDetail.setCallerTelephoneNumber(rs.getString("ca.caller_phonenumber"));
		transport.setCallerDetail(callerDetail);
		Patient patient = new Patient();
		patient.setFirstname(rs.getString("t.firstname"));
		patient.setLastname(rs.getString("t.lastname"));
		transport.setPatient(patient);
		transport.setFromStreet(rs.getString("t.from_street"));
		transport.setFromCity(rs.getString("t.from_city"));
		transport.setToStreet(rs.getString("t.to_street"));
		transport.setToCity(rs.getString("t.to_city"));
		//return_transport
		transport.setResponsibleStation(rs.getInt("t.planned_location"));
		transport.setTransportPriority(rs.getString("t.priority"));
		transport.setKindOfTransport(rs.getString("t.transporttype"));
		
		transports.add(transport);
		}
		
	}
	catch (SQLException e)
	{
		e.printStackTrace();
		return null;
	}
	return transports;
	}

	@Override
	public boolean removeTransportByNr(long transportNr)
	{
		// TODO BIGINT <-> LONG ??? 
    	try
    	{
    		final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("delete.transportByNr"));
    		query.setLong(1, transportNr);  //BIGINT <-> LONG ???

    		query.executeUpdate();
    	}
    	catch (SQLException e)
    	{
    		e.printStackTrace();
    		return false;
    	}
    	return true;
		
	}

	@Override
	public boolean updateTransport(Transport transport)
	{
		try
		{
//			direction_ID = ?, caller_ID = ?, note = '?', createdBy_user = '?', priority = '?', feedback = '?', creationDate = '?', departure = '?', 
//			appointment = '?', appointmentPatient = '?', assistant = ?, staffmember_vehicle_ID = ?, transporttype = '?', transportstate = ?, 
//			disease = '?', firstname = '?', lastname = '?', planned_location = ?, return_transport = ?, from_street = '?', from_city = '?', 
//			to_street = '?', to_city = '?', programstate = ? where transportNr = ?;
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("update.transport"));
			query.setInt(1, transport.getDirection());
			query.setInt(2, transport.getCallerDetail().getCallerId());
			query.setString(3, "note...");
			query.setString(4, "created by user?");
			query.setString(5, transport.getTransportPriority());
			query.setString(6, transport.getFeedback());
			query.setString(7, convertDate((transport.getReceiveTime())));
			query.setString(8, convertDate(transport.getPlannedStartOfTransport()));
			query.setString(9, convertDate(transport.getAppointmentTimeAtDestination()));
			query.setString(10, convertDate(transport.getPlannedTimeAtPatient()));
			query.setBoolean(11, false); //assistant ?
			query.setInt(12, 1); 		 // staffmember_vehicle_ID
			query.setString(23, transport.getKindOfTransport());
			query.setInt(24, 1); //transportstate ???
			query.setString(25, transport.getKindOfIllness());
			query.setString(26, transport.getPatient().getFirstname());
			query.setString(27, transport.getPatient().getLastname());
			query.setInt(28, 1); // planned_loaction
			query.setBoolean(29, false); //return_transport ?
			query.setString(30, transport.getFromStreet());
			query.setString(31, transport.getFromCity());
			query.setString(32, transport.getToStreet());
			query.setString(33, transport.getToCity());
			query.setInt(34, transport.getProgramStatus());
			query.setLong(35, 200812345); //TransportNr ?
			
			query.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;

		return false;
	}


}
