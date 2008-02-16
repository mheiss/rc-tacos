package at.rc.tacos.core.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.Queries;
import at.rc.tacos.core.db.dao.DialysisPatientDAO;
import at.rc.tacos.core.db.dao.LocationDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.DialysisPatient;
import at.rc.tacos.model.Patient;
import at.rc.tacos.util.MyUtils;

public class DialysisPatientDAOMySQL implements DialysisPatientDAO
{
	//The data source to get the connection and the queries file
	private final DataSource source = DataSource.getInstance();
	private final Queries queries = Queries.getInstance();
	//the location DAO
	private final LocationDAO locationDAO = DaoFactory.MYSQL.createLocationDAO();
	
	@Override
	public int addDialysisPatient(DialysisPatient patient) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{	
			//firstname, lastname, stationname, plannedStartOfTransport, plannedTimeAtPatient, 
			//appointmentTimeAdDialysis, plannedStartForBackTransport, readyTime, fromStreet, fromCity, toStreet, 
			//toCity, insurance, stationary, kindOfTransport, assistant, monday, tuesday, wednesday, thursday, friday, saturday, sunday)
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("insert.DialysisPatient"));
			query.setString(1, patient.getPatient().getFirstname());
			query.setString(2, patient.getPatient().getLastname());
			query.setInt(3, patient.getLocation().getId());
			query.setString(4, MyUtils.timestampToString(patient.getPlannedStartOfTransport(), MyUtils.sqlDateTime));
			query.setString(5, MyUtils.timestampToString(patient.getPlannedTimeAtPatient(), MyUtils.sqlDateTime));
			query.setString(6, MyUtils.timestampToString(patient.getAppointmentTimeAtDialysis(), MyUtils.sqlDateTime));
			query.setString(7, MyUtils.timestampToString(patient.getPlannedStartForBackTransport(), MyUtils.sqlDateTime));
			query.setString(8, MyUtils.timestampToString(patient.getReadyTime(), MyUtils.sqlDateTime));
			query.setString(9, patient.getFromStreet());
			query.setString(10, patient.getFromCity());
			query.setString(11, patient.getToStreet());
			query.setString(12, patient.getToCity());
			query.setString(13, patient.getInsurance());
			query.setBoolean(14, patient.isStationary());
			query.setString(15, patient.getKindOfTransport());
			query.setBoolean(16, patient.isAssistantPerson());
			query.setBoolean(17, patient.isMonday());
			query.setBoolean(18, patient.isTuesday());
			query.setBoolean(19, patient.isWednesday());
			query.setBoolean(20, patient.isThursday());
			query.setBoolean(21, patient.isFriday());
			query.setBoolean(22, patient.isSaturday());
			query.setBoolean(23, patient.isSunday());
			query.executeUpdate();
			//get the last inserted id
			final ResultSet rs = query.getGeneratedKeys();
		    if (rs.next()) 
		        return rs.getInt(1);
		    return -1;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public DialysisPatient getDialysisPatientById(int id) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.dialysisByID"));
			stmt.setInt(1, id);
			final ResultSet rs = stmt.executeQuery();
			if(rs.first())
			{
				DialysisPatient dialysis = new DialysisPatient();
				dialysis.setId(rs.getInt("dialysis_ID"));
				dialysis.setAppointmentTimeAtDialysis(MyUtils.stringToTimestamp(rs.getString("appointmentTimeAtDialysis"), MyUtils.sqlDateTime));
				dialysis.setAssistantPerson(rs.getBoolean("assistant"));
				dialysis.setFriday(rs.getBoolean("friday"));
				dialysis.setFromCity(rs.getString("fromCity"));
				dialysis.setFromStreet(rs.getString("fromStreet"));
				dialysis.setInsurance(rs.getString("insurance"));
				dialysis.setKindOfTransport(rs.getString("kindOfTransport"));
				dialysis.setPlannedStartForBackTransport(MyUtils.stringToTimestamp(rs.getString("plannedStartForBackTransport"), MyUtils.sqlDateTime));
				dialysis.setPlannedStartOfTransport(MyUtils.stringToTimestamp(rs.getString("plannedStartOfTransport"), MyUtils.sqlDateTime));
				dialysis.setPlannedTimeAtPatient(MyUtils.stringToTimestamp(rs.getString("plannedTimeAtPatient"), MyUtils.sqlDateTime));
				dialysis.setReadyTime(MyUtils.stringToTimestamp(rs.getString("readyTime"), MyUtils.sqlDateTime));
				dialysis.setSaturday(rs.getBoolean("saturday"));
				dialysis.setStationary(rs.getBoolean("stationary"));
				dialysis.setSunday(rs.getBoolean("sunday"));
				dialysis.setThursday(rs.getBoolean("thursday"));
				dialysis.setToCity(rs.getString("toCity"));
				dialysis.setToStreet(rs.getString("toStreet"));
				dialysis.setTuesday(rs.getBoolean("tuesday"));
				dialysis.setWednesday(rs.getBoolean("wednesday"));
				dialysis.setMonday(rs.getBoolean("monday"));
				//the location
				int locationId = rs.getInt("location");
				dialysis.setLocation(locationDAO.getLocation(locationId));
				//the patient for the dialysis
				Patient patient = new Patient();
				patient.setFirstname(rs.getString("firstname"));
				patient.setLastname(rs.getString("lastname"));
				dialysis.setPatient(patient);
				//return the patient
				return dialysis;
			}
			//no result set
			return null;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public List<DialysisPatient> listDialysisPatient() throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("list.dialysisPatients"));
			final ResultSet rs = stmt.executeQuery();
			List<DialysisPatient> dialysises = new ArrayList<DialysisPatient>();
			while(rs.next())
			{
				DialysisPatient dialysis = new DialysisPatient();
				dialysis.setId(rs.getInt("dialysis_ID"));
				dialysis.setAppointmentTimeAtDialysis(MyUtils.stringToTimestamp(rs.getString("appointmentTimeAtDialysis"), MyUtils.sqlDateTime));
				dialysis.setAssistantPerson(rs.getBoolean("assistant"));
				dialysis.setFriday(rs.getBoolean("friday"));
				dialysis.setFromCity(rs.getString("fromCity"));
				dialysis.setFromStreet(rs.getString("fromStreet"));
				dialysis.setInsurance(rs.getString("insurance"));
				dialysis.setKindOfTransport(rs.getString("kindOfTransport"));
				dialysis.setPlannedStartForBackTransport(MyUtils.stringToTimestamp(rs.getString("plannedStartForBackTransport"), MyUtils.sqlDateTime));
				dialysis.setPlannedStartOfTransport(MyUtils.stringToTimestamp(rs.getString("plannedStartOfTransport"), MyUtils.sqlDateTime));
				dialysis.setPlannedTimeAtPatient(MyUtils.stringToTimestamp(rs.getString("plannedTimeAtPatient"), MyUtils.sqlDateTime));
				dialysis.setReadyTime(MyUtils.stringToTimestamp(rs.getString("readyTime"), MyUtils.sqlDateTime));
				dialysis.setSaturday(rs.getBoolean("saturday"));
				dialysis.setStationary(rs.getBoolean("stationary"));
				dialysis.setSunday(rs.getBoolean("sunday"));
				dialysis.setThursday(rs.getBoolean("thursday"));
				dialysis.setToCity(rs.getString("toCity"));
				dialysis.setToStreet(rs.getString("toStreet"));
				dialysis.setTuesday(rs.getBoolean("tuesday"));
				dialysis.setWednesday(rs.getBoolean("wednesday"));
				dialysis.setMonday(rs.getBoolean("monday"));
				//the location
				int locationId = rs.getInt("location");
				dialysis.setLocation(locationDAO.getLocation(locationId));
				//the patient for the dialysis
				Patient patient = new Patient();
				patient.setFirstname(rs.getString("firstname"));
				patient.setLastname(rs.getString("lastname"));
				dialysis.setPatient(patient);
				//add to the list
				dialysises.add(dialysis);
			}
			//return the list
			return dialysises;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public boolean removeDialysisPatient(int id) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("remove.dialysis"));
			stmt.setInt(1, id);
			//assert the patient is removed
			if(stmt.executeUpdate() == 0)
				return false;
			return true;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public boolean updateDialysisPatient(DialysisPatient patient) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			// firstname, lastname, stationname, plannedStartOfTransport, plannedTimeAtPatient, appointmentTimeAdDialysis,
			// plannedStartForBackTransport, readyTime, fromStreet, fromCity, toStreet, toCity, insurance, stationary, 
			//kindOfTransport, assistant, monday, tuesday, wednesday, thursday, friday, saturday, sunday, WHERE dialysis_ID;
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("update.dialysis"));
			query.setString(1, patient.getPatient().getFirstname());
			query.setString(2, patient.getPatient().getLastname());
			query.setInt(3, patient.getLocation().getId());
			query.setString(4, MyUtils.timestampToString(patient.getPlannedStartOfTransport(), MyUtils.sqlDateTime));
			query.setString(5, MyUtils.timestampToString(patient.getPlannedTimeAtPatient(), MyUtils.sqlDateTime));
			query.setString(6, MyUtils.timestampToString(patient.getAppointmentTimeAtDialysis(), MyUtils.sqlDateTime));
			query.setString(7, MyUtils.timestampToString(patient.getPlannedStartForBackTransport(), MyUtils.sqlDateTime));
			query.setString(8, MyUtils.timestampToString(patient.getReadyTime(), MyUtils.sqlDateTime));
			query.setString(9, patient.getFromStreet());
			query.setString(10, patient.getFromCity());
			query.setString(11, patient.getToStreet());
			query.setString(12, patient.getToCity());
			query.setString(13, patient.getInsurance());
			query.setBoolean(14, patient.isStationary());
			query.setString(15, patient.getKindOfTransport());
			query.setBoolean(16, patient.isAssistantPerson());
			query.setBoolean(17, patient.isMonday());
			query.setBoolean(18, patient.isTuesday());
			query.setBoolean(19, patient.isWednesday());
			query.setBoolean(20, patient.isThursday());
			query.setBoolean(21, patient.isFriday());
			query.setBoolean(22, patient.isSaturday());
			query.setBoolean(23, patient.isSunday());
			query.setInt(24, patient.getId());
			//assert the update was successfully
			if(query.executeUpdate() == 0)
				return false;
			return true;
		}
		finally
		{
			connection.close();
		}
	}
}