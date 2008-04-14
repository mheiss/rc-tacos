package at.rc.tacos.core.db.dao.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.SQLQueries;
import at.rc.tacos.core.db.dao.DialysisPatientDAO;
import at.rc.tacos.core.db.dao.LocationDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.DialysisPatient;
import at.rc.tacos.model.Patient;
import at.rc.tacos.util.MyUtils;

public class DialysisPatientDAOSQL implements DialysisPatientDAO
{
	//The data source to get the connection and the queries file
	private final DataSource source = DataSource.getInstance();
	private final SQLQueries queries = SQLQueries.getInstance();
	//the location DAO
	private final LocationDAO locationDAO = DaoFactory.SQL.createLocationDAO();
	
	@Override
	public int addDialysisPatient(DialysisPatient patient) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{	
			int id = 0;
			
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.nextDialysisID"));
			final ResultSet rs = stmt.executeQuery();
			if(!rs.next())
				return -1;
			
			id = rs.getInt(1);			
			
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("insert.dialysisPatient"));
			query.setInt(1, id);
			query.setString(2, patient.getPatient().getFirstname());
			query.setString(3, patient.getPatient().getLastname());
			query.setInt(4, patient.getLocation().getId());
			query.setString(5, MyUtils.timestampToString(patient.getPlannedStartOfTransport(), MyUtils.sqlTime));
			query.setString(6, MyUtils.timestampToString(patient.getPlannedTimeAtPatient(), MyUtils.sqlTime));
			query.setString(7, MyUtils.timestampToString(patient.getAppointmentTimeAtDialysis(), MyUtils.sqlTime));
			query.setString(8, MyUtils.timestampToString(patient.getPlannedStartForBackTransport(), MyUtils.sqlTime));
			query.setString(9, MyUtils.timestampToString(patient.getReadyTime(), MyUtils.sqlTime));
			query.setString(10, patient.getFromStreet());
			query.setString(11, patient.getFromCity());
			query.setString(12, patient.getToStreet());
			query.setString(13, patient.getToCity());
			query.setString(14, patient.getInsurance());
			query.setBoolean(15, patient.isStationary());
			query.setString(16, patient.getKindOfTransport());
			query.setBoolean(17, patient.isAssistantPerson());
			query.setBoolean(18, patient.isMonday());
			query.setBoolean(19, patient.isTuesday());
			query.setBoolean(20, patient.isWednesday());
			query.setBoolean(21, patient.isThursday());
			query.setBoolean(22, patient.isFriday());
			query.setBoolean(23, patient.isSaturday());
			query.setBoolean(24, patient.isSunday());
			query.executeUpdate();
			//get the last inserted id

		    //insert a row into the dialysis transport table to save the last generated transport for
		    //this patient
		    final PreparedStatement insertstmt = connection.prepareStatement(queries.getStatment("insert.dialysisTransport"));
		    insertstmt.setInt(1, id);
		    insertstmt.setString(2, null);
		    insertstmt.setString(3, null);
		    if(insertstmt.executeUpdate() != 0)
		    	return id;
		 
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
			if(rs.next())
			{
				DialysisPatient dialysis = new DialysisPatient();
				dialysis.setId(rs.getInt("dialysis_ID"));
				dialysis.setAppointmentTimeAtDialysis(MyUtils.stringToTimestamp(rs.getString("appointmentTimeAtDialysis"), MyUtils.sqlServerTime));
				dialysis.setAssistantPerson(rs.getBoolean("assistant"));
				dialysis.setFriday(rs.getBoolean("friday"));
				dialysis.setFromCity(rs.getString("fromCity"));
				dialysis.setFromStreet(rs.getString("fromStreet"));
				dialysis.setInsurance(rs.getString("insurance"));
				dialysis.setKindOfTransport(rs.getString("kindOfTransport"));
				dialysis.setPlannedStartForBackTransport(MyUtils.stringToTimestamp(rs.getString("plannedStartForBackTransport"), MyUtils.sqlServerTime));
				dialysis.setPlannedStartOfTransport(MyUtils.stringToTimestamp(rs.getString("plannedStartOfTransport"), MyUtils.sqlServerTime));
				dialysis.setPlannedTimeAtPatient(MyUtils.stringToTimestamp(rs.getString("plannedTimeAtPatient"), MyUtils.sqlServerTime));
				dialysis.setReadyTime(MyUtils.stringToTimestamp(rs.getString("readyTime"), MyUtils.sqlServerTime));
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
				
				//set the last generated transport dates
				if(rs.getString("transport_date") != null)
					dialysis.setLastTransportDate(MyUtils.stringToTimestamp(rs.getString("transport_date"),MyUtils.sqlDate));
				if(rs.getString("return_date") != null)
					dialysis.setLastBackTransportDate(MyUtils.stringToTimestamp(rs.getString("return_date"),MyUtils.sqlDate));
				
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
				dialysis.setAppointmentTimeAtDialysis(MyUtils.stringToTimestamp(rs.getString("appointmentTimeAtDialysis"), MyUtils.sqlServerTime));
				dialysis.setAssistantPerson(rs.getBoolean("assistant"));
				dialysis.setFriday(rs.getBoolean("friday"));
				dialysis.setFromCity(rs.getString("fromCity"));
				dialysis.setFromStreet(rs.getString("fromStreet"));
				dialysis.setInsurance(rs.getString("insurance"));
				dialysis.setKindOfTransport(rs.getString("kindOfTransport"));
				dialysis.setPlannedStartForBackTransport(MyUtils.stringToTimestamp(rs.getString("plannedStartForBackTransport"), MyUtils.sqlServerTime));
				dialysis.setPlannedStartOfTransport(MyUtils.stringToTimestamp(rs.getString("plannedStartOfTransport"), MyUtils.sqlServerTime));
				dialysis.setPlannedTimeAtPatient(MyUtils.stringToTimestamp(rs.getString("plannedTimeAtPatient"), MyUtils.sqlServerTime));
				dialysis.setReadyTime(MyUtils.stringToTimestamp(rs.getString("readyTime"), MyUtils.sqlServerTime));
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
				
				//set the last generated transport dates
				if(rs.getString("transport_date") != null)
					dialysis.setLastTransportDate(MyUtils.stringToTimestamp(rs.getString("transport_date"),MyUtils.sqlServerDate));
				if(rs.getString("return_date") != null)
					dialysis.setLastBackTransportDate(MyUtils.stringToTimestamp(rs.getString("return_date"),MyUtils.sqlServerDate));
				
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
			
			//delete the dialyse transport entry
			final PreparedStatement stmtTrans = connection.prepareStatement(queries.getStatment("delete.dialsyisTransport"));
			stmtTrans.setInt(1, id);
			//assert the entry is removed
			if(stmtTrans.executeUpdate() == 0)
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
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("update.dialysis"));
			query.setString(1, patient.getPatient().getFirstname());
			query.setString(2, patient.getPatient().getLastname());
			query.setInt(3, patient.getLocation().getId());
			query.setString(4, MyUtils.timestampToString(patient.getPlannedStartOfTransport(), MyUtils.sqlServerTime));
			query.setString(5, MyUtils.timestampToString(patient.getPlannedTimeAtPatient(), MyUtils.sqlServerTime));
			query.setString(6, MyUtils.timestampToString(patient.getAppointmentTimeAtDialysis(), MyUtils.sqlServerTime));
			query.setString(7, MyUtils.timestampToString(patient.getPlannedStartForBackTransport(), MyUtils.sqlServerTime));
			query.setString(8, MyUtils.timestampToString(patient.getReadyTime(), MyUtils.sqlServerTime));
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

			//update the dialyse transport table
			final PreparedStatement stmtTran = connection.prepareStatement(queries.getStatment("update.dialysisTransport"));
			//set the values if they are not 0
			if(patient.getLastTransportDate() > 0)
				stmtTran.setString(1, MyUtils.timestampToString(patient.getLastTransportDate(), MyUtils.sqlServerDate));
			else
				stmtTran.setString(1,null);
			//set the value for the backtransport if it is not 0
			if(patient.getLastBackTransporDate() > 0)
				stmtTran.setString(2, MyUtils.timestampToString(patient.getLastBackTransporDate(), MyUtils.sqlServerDate));
			else
				stmtTran.setString(2,null);
			stmtTran.setInt(3, patient.getId());
			if(stmtTran.executeUpdate() == 0)
				return false;
			//everything is ok
			return true;
		}
		finally
		{
			connection.close();
		}
	}
}