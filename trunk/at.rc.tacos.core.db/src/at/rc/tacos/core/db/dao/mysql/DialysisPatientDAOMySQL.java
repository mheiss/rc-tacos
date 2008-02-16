package at.rc.tacos.core.db.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.dao.DialysisPatientDAO;
import at.rc.tacos.model.DialysisPatient;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Patient;
import at.rc.tacos.util.MyUtils;

public class DialysisPatientDAOMySQL implements DialysisPatientDAO
{
	public static final String QUERIES_BUNDLE_PATH = "at.rc.tacos.core.db.queries";

	@Override
	public int addDialysisPatient(DialysisPatient patient) 
	{
		int dialysisId = -1;
		try
		{	
			//firstname, lastname, stationname, plannedStartOfTransport, plannedTimeAtPatient, 
			//appointmentTimeAdDialysis, plannedStartForBackTransport, readyTime, fromStreet, fromCity, toStreet, 
			//toCity, insurance, stationary, kindOfTransport, assistant, monday, tuesday, wednesday, thursday, friday, saturday, sunday)
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(DialysisPatientDAOMySQL.QUERIES_BUNDLE_PATH).getString("insert.DialysisPatient"));
			query.setString(1, patient.getPatient().getFirstname());
			query.setString(2, patient.getPatient().getLastname());
			query.setString(3, patient.getLocation().getLocationName());
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
		        dialysisId = rs.getInt(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
		return dialysisId;
	}

	@Override
	public DialysisPatient getDialysisPatientById(int id)
	{
		DialysisPatient dialysis = new DialysisPatient();
		Patient patient = new Patient();
		Location location = new Location();
		try
		{
			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.dialysisByID"));
			query1.setInt(1, id);
			final ResultSet rs = query1.executeQuery();

			if(rs.first())
			{
				dialysis.setAppointmentTimeAtDialysis(MyUtils.stringToTimestamp(rs.getString("appointmentTimeAtDialysis"), MyUtils.sqlDateTime));
				dialysis.setAssistantPerson(rs.getBoolean("assistant"));
				dialysis.setFriday(rs.getBoolean("friday"));
				dialysis.setFromCity(rs.getString("fromCity"));
				dialysis.setFromStreet(rs.getString("fromStreet"));
				dialysis.setId(rs.getInt("dialysis_ID"));
				dialysis.setInsurance(rs.getString("insurance"));
				dialysis.setKindOfTransport(rs.getString("kindOfTransport"));
				location.setLocationName(rs.getString("stationname"));
				dialysis.setLocation(location);
				dialysis.setMonday(rs.getBoolean("monday"));

				patient.setFirstname(rs.getString("firstname"));
				patient.setLastname(rs.getString("lastname"));
				dialysis.setPatient(patient);

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
			}
			else 
				return null;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return dialysis;
	}

	@Override
	public List<DialysisPatient> listDialysisPatient()
	{
		DialysisPatient dialysis = new DialysisPatient();
		List<DialysisPatient> dialysises = new ArrayList<DialysisPatient>();
		Patient patient = new Patient();
		Location location = new Location();
		try
		{
			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.dialysisPatients"));
			final ResultSet rs = query1.executeQuery();

			while(rs.next())
			{
				dialysis.setAppointmentTimeAtDialysis(MyUtils.stringToTimestamp(rs.getString("appointmentTimeAtDialysis"), MyUtils.sqlDateTime));
				dialysis.setAssistantPerson(rs.getBoolean("assistant"));
				dialysis.setFriday(rs.getBoolean("friday"));
				dialysis.setFromCity(rs.getString("fromCity"));
				dialysis.setFromStreet(rs.getString("fromStreet"));
				dialysis.setId(rs.getInt("dialysis_ID"));
				dialysis.setInsurance(rs.getString("insurance"));
				dialysis.setKindOfTransport(rs.getString("kindOfTransport"));
				location.setLocationName(rs.getString("stationname"));
				dialysis.setLocation(location);
				dialysis.setMonday(rs.getBoolean("monday"));

				patient.setFirstname(rs.getString("firstname"));
				patient.setLastname(rs.getString("lastname"));
				dialysis.setPatient(patient);

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

				dialysises.add(dialysis);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return dialysises;
	}

	@Override
	public boolean removeDialysisPatient(int id)
	{
		try
		{
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("remove.dialysis"));
			query.setInt(1, id);

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
	public boolean updateDialysisPatient(DialysisPatient patient)
	{
		try
		{
			// firstname, lastname, stationname, plannedStartOfTransport, plannedTimeAtPatient, appointmentTimeAdDialysis,
			// plannedStartForBackTransport, readyTime, fromStreet, fromCity, toStreet, toCity, insurance, stationary, 
			//kindOfTransport, assistant, monday, tuesday, wednesday, thursday, friday, saturday, sunday, WHERE dialysis_ID;
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("update.dialysis"));
			query.setString(1, patient.getPatient().getFirstname());
			query.setString(2, patient.getPatient().getLastname());
			query.setString(3, patient.getLocation().getLocationName());
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
			query.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
