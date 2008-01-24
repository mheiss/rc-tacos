package at.rc.tacos.core.db.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.dao.DialysisPatientDAO;
import at.rc.tacos.model.DialysisPatient;
import at.rc.tacos.util.MyUtils;

public class DialysisPatientDAOMySQL implements DialysisPatientDAO
{
	public static final String QUERIES_BUNDLE_PATH = "at.rc.tacos.core.db.queries";

	@Override
	public int addDialysisPatient(DialysisPatient patient) 
	{
		Integer dialysisId = null;
		try
		{	
			//firstname, lastname, stationname, plannedStartOfTransport, plannedTimeAtPatient, 
			//appointmentTimeAdDialysis, plannedStartForBackTransport, readyTime, fromStreet, fromCity, toStreet, 
			//toCity, insurance, stationary, kindOfTransport, assistant, monday, tuesday, wednesday, thursday, friday, saturday, sunday)
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(DialysisPatientDAOMySQL.QUERIES_BUNDLE_PATH).getString("insert.DialysisPatient"));
			query.setString(1, patient.getPatient().getFirstname());
			query.setString(2, patient.getPatient().getLastname());
			query.setString(3, patient.getLocation().getLocationName());
			query.setString(4, MyUtils.formatTime(patient.getPlannedStartOfTransport()));
			query.setString(5, MyUtils.formatTime(patient.getPlannedTimeAtPatient()));
			query.setString(6, MyUtils.formatTime(patient.getAppointmentTimeAtDialysis()));
			query.setString(7, MyUtils.formatTime(patient.getPlannedStartForBackTransport()));
			query.setString(8, MyUtils.formatTime(patient.getReadyTime()));
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
			
			//SELECT dialysis_ID FROM dialysis WHERE firstname = ? AND lastname = ? AND fromStreet = ?;
			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(DialysisPatientDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.DialysisPatientId"));
			query1.setString(1, patient.getPatient().getFirstname());
			query1.setString(2, patient.getPatient().getLastname());
			query1.setString(3, patient.getFromStreet());
			final ResultSet rsDialysisPatientId = query1.executeQuery();
			
			if(rsDialysisPatientId.next())
				dialysisId = rsDialysisPatientId.getInt("dialysis_ID");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
		return dialysisId;
	}

	@Override
	public DialysisPatient getDialysisPatientById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DialysisPatient> listDialysisPatient() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeDialysisPatient(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateDialysisPatient(DialysisPatient patient) {
		// TODO Auto-generated method stub
		return false;
	}

	
}
