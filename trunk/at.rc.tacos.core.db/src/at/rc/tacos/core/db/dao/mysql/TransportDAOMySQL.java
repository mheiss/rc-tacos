package at.rc.tacos.core.db.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.dao.LocationDAO;
import at.rc.tacos.core.db.dao.StaffMemberDAO;
import at.rc.tacos.core.db.dao.TransportDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.*;
import at.rc.tacos.util.MyUtils;

public class TransportDAOMySQL implements TransportDAO
{
	public static final String QUERIES_BUNDLE_PATH = "at.rc.tacos.core.db.queries";
	private final LocationDAO locationDAO = DaoFactory.MYSQL.createLocationDAO();
	private final StaffMemberDAO staffMemberDAO = DaoFactory.MYSQL.createStaffMemberDAO();

	@Override
	public int addTransport(Transport transport)
	{
		int transportId = 0;
		CallerDetail caller = transport.getCallerDetail();
		Patient patient = transport.getPatient();

		try
		{	
			//transport_ID, transportNr, direction, caller_ID, note, createdBy_user, priority, feedback, creationDate,
			//departure, appointment, appointmentPatient, transporttype, disease, firstname, lastname, planned_location,
			//from_street, from_city, to_street, to_city, programstate, dateOfTransport
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("insert.transport"));
			query.setInt(1, transport.getTransportId());
			query.setInt(2, transport.getTransportNumber());
			query.setInt(3, transport.getDirection());			
			query.setInt(4, caller.getCallerId());
			query.setString(5, transport.getNotes());
			query.setString(6, transport.getCreatedByUsername());
			query.setString(7, transport.getTransportPriority());
			query.setString(8, transport.getFeedback());
			query.setString(9, MyUtils.timestampToString(transport.getCreationTime(), MyUtils.sqlDateTime));
			query.setString(10, MyUtils.timestampToString(transport.getPlannedStartOfTransport(), MyUtils.sqlDateTime));
			query.setString(11, MyUtils.timestampToString(transport.getAppointmentTimeAtDestination(), MyUtils.sqlDateTime));
			query.setString(12, MyUtils.timestampToString(transport.getPlannedTimeAtPatient(), MyUtils.sqlDateTime));
			query.setString(13, transport.getKindOfTransport());
			query.setString(14, transport.getKindOfIllness());
			query.setString(15, patient.getFirstname());
			query.setString(16, patient.getLastname());
			query.setInt(17, transport.getPlanedLocation().getId());
			query.setString(18, transport.getFromStreet());
			query.setString(19, transport.getFromCity());
			query.setString(20, transport.getToStreet());
			query.setString(21, transport.getToCity());
			query.setInt(22, transport.getProgramStatus());
			query.setString(23, MyUtils.timestampToString(transport.getDateOfTransport(), MyUtils.sqlDateTime));
			query.executeUpdate();

			// t.creationDate, t.firstname, t.lastname, t.to_street, t.appointment = ?;
			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.transportID"));
			query1.setString(1, MyUtils.timestampToString(transport.getCreationTime(), MyUtils.sqlDateTime));
			query1.setString(2, transport.getPatient().getFirstname());
			query1.setString(3, transport.getPatient().getLastname());
			query1.setString(4, transport.getToStreet());
			query1.setString(5, MyUtils.timestampToString(transport.getAppointmentTimeAtDestination(), MyUtils.sqlDateTime));
			final ResultSet rsId = query1.executeQuery();

			if(rsId.next())
				transportId = rsId.getInt("transport_ID");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
		return transportId;
	}

	@Override
	public List<Transport> listTransports(long startdate, long enddate)
	{
		List<Transport> transports = null;
		try
		{
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.transports"));
			query.setString(1, MyUtils.timestampToString(startdate, MyUtils.sqlDateTime));
			query.setString(2, MyUtils.timestampToString(enddate, MyUtils.sqlDateTime));
			final ResultSet rs = query.executeQuery();

			while(rs.next())
			{
				Transport transport = new Transport();
				//t.transport_ID, t.transportNr, t.planned_location, lo.locationname, lo.location_ID, t.caller_ID, ca.callername,
				//ca.caller_phonenumber, t.createdBy_user, t.note, t.feedback, t.creationDate, t.departure, t.appointment,
				//t.appointmentPatient, t.disease, t.firstname, t.lastname, t.from_street, t.from_city, t.to_street, t.to_city,
				//t.programstate, t.transporttype, t.priority, t.direction, t.dateOfTransport, av.vehicle_ID, av.driver_ID,
				//av.medic1_ID, av.medic2_ID, av.locationname, av.note, av.vehicletype

				transport.setTransportId(rs.getInt("t.transport_ID"));
				transport.setTransportNumber(rs.getInt("t.transportNr"));

				Location station = new Location();
				station.setId(rs.getInt("t.planned_location"));
				station.setLocationName(rs.getString("lo.locationname"));
				transport.setPlanedLocation(station);

				CallerDetail caller = new CallerDetail();
				caller.setCallerId(rs.getInt("t.caller_ID"));
				caller.setCallerName(rs.getString("ca.callername"));
				caller.setCallerTelephoneNumber(rs.getString("ca.caller_phonenumber"));
				transport.setCallerDetail(caller);
				transport.setCreatedByUsername(rs.getString("t.createdBy_user"));
				transport.setNotes(rs.getString("t.note"));
				transport.setFeedback(rs.getString("t.feedback"));
				transport.setCreationTime(MyUtils.stringToTimestamp(rs.getString("creationDate"), MyUtils.sqlDateTime));
				transport.setPlannedStartOfTransport(MyUtils.stringToTimestamp(rs.getString("t.departure"), MyUtils.sqlDateTime));
				transport.setAppointmentTimeAtDestination(MyUtils.stringToTimestamp(rs.getString("t.appointment"), MyUtils.sqlDateTime));
				transport.setPlannedTimeAtPatient(MyUtils.stringToTimestamp(rs.getString("t.appointmentPatient"), MyUtils.sqlDateTime));
				transport.setKindOfIllness(rs.getString("t.disease"));

				Patient patient = new Patient();
				patient.setFirstname(rs.getString("t.firstname"));
				patient.setLastname(rs.getString("t.lastname"));
				transport.setPatient(patient);

				transport.setFromStreet(rs.getString("t.from_street"));
				transport.setFromCity(rs.getString("t.from_city"));
				transport.setToStreet(rs.getString("t.to_street"));
				transport.setToCity(rs.getString("t.to_city"));
				transport.setProgramStatus(rs.getInt("t.programstate"));
				transport.setKindOfTransport(rs.getString("t.transporttype"));
				transport.setTransportPriority(rs.getString("t.priority"));
				transport.setDirection(rs.getInt("t.direction_ID"));
				transport.setDateOfTransport(MyUtils.stringToTimestamp(rs.getString("t.dateOfTransport"), MyUtils.sqlDateTime));

				VehicleDetail vehicle = new VehicleDetail();

				Location currentStation = new Location();
				currentStation = locationDAO.getLocationByName(rs.getString("av.locationname"));
				vehicle.setCurrentStation(currentStation);

				vehicle.setVehicleName(rs.getString("av.vehicle_ID"));

				StaffMember driver = new StaffMember();
				driver = staffMemberDAO.getStaffMemberByID(rs.getInt("v.driver_ID"));
				vehicle.setDriver(driver);

				StaffMember firstParamedic = new StaffMember();
				firstParamedic = staffMemberDAO.getStaffMemberByID(rs.getInt("v.medic1_ID"));
				vehicle.setFirstParamedic(firstParamedic);

				StaffMember secondParamedic = new StaffMember();
				secondParamedic = staffMemberDAO.getStaffMemberByID(rs.getInt("v.medic2_ID"));
				vehicle.setSecondParamedic(secondParamedic);

				vehicle.setVehicleNotes(rs.getString("av.note"));
				vehicle.setVehicleType(rs.getString("av.vehicletype"));
				transport.setVehicleDetail(vehicle);

				// find the selected items (boolean values)
				final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.selectedTransportItems"));
				query1.setInt(1, transport.getTransportId());
				final ResultSet rs1 = query1.executeQuery();

				while(rs1.next())
				{
					if(rs1.getInt("selected_ID") == 1)
						transport.setEmergencyDoctorAlarming(true);
					else transport.setEmergencyDoctorAlarming(false);

					if(rs1.getInt("selected_ID") == 2)
						transport.setPoliceAlarming(true);
					else transport.setPoliceAlarming(false);

					if(rs1.getInt("selected_ID") == 3)
						transport.setFirebrigadeAlarming(true);
					else transport.setFirebrigadeAlarming(false);

					if(rs1.getInt("selected_ID") == 4)
						transport.setMountainRescueServiceAlarming(true);
					else transport.setMountainRescueServiceAlarming(false);

					if(rs1.getInt("selected_ID") == 5)
						transport.setDfAlarming(true);
					else transport.setDfAlarming(false);

					if(rs1.getInt("selected_ID") == 6)
						transport.setBrkdtAlarming(true);
					else transport.setBrkdtAlarming(false);

					if(rs1.getInt("selected_ID") == 7)
						transport.setBlueLightToGoal(true);
					else transport.setBlueLightToGoal(false);

					if(rs1.getInt("selected_ID") == 8)
						transport.setHelicopterAlarming(true);
					else transport.setHelicopterAlarming(false);

					if(rs1.getInt("selected_ID") == 9)
						transport.setAssistantPerson(true);
					else transport.setAssistantPerson(false);

					if(rs1.getInt("selected_ID") == 10)
						transport.setBackTransport(true);
					else transport.setBackTransport(false);

					if(rs1.getInt("selected_ID") == 11)
						transport.setLongDistanceTrip(true);
					else transport.setLongDistanceTrip(false);

					if(rs1.getInt("selected_ID") == 12)
						transport.setEmergencyPhone(true);
					else transport.setEmergencyPhone(false);
				}
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
	public boolean updateTransport(Transport transport)
	{
		try
		{
			// transportNr = ?, direction = ?, caller_ID = ?, note = ?, createdBy_user = ?, priority = ?, feedback = ?, creationDate = ?, 
			//departure = ?, appointment = ?, appointmentPatient = ?, transporttype = ?, disease = ?, firstname = ?, lastname = ?, 
			//planned_location = ?, from_street = ?, from_city = ?, to_street = ?, to_city = ?, programstate = ?, dateOfTransport = ?, transport_ID = ?;
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("update.transport"));
			query.setInt(1, transport.getTransportNumber());
			query.setInt(2, transport.getDirection());
			query.setInt(3, transport.getCallerDetail().getCallerId());
			query.setString(4, transport.getNotes());
			query.setString(5, transport.getCreatedByUsername());
			query.setString(6, transport.getTransportPriority());
			query.setString(7, transport.getFeedback());
			query.setString(8, MyUtils.timestampToString(transport.getCreationTime(), MyUtils.sqlDateTime));
			query.setString(9, MyUtils.timestampToString(transport.getPlannedStartOfTransport(), MyUtils.sqlDateTime));
			query.setString(10, MyUtils.timestampToString(transport.getAppointmentTimeAtDestination(), MyUtils.sqlDateTime));
			query.setString(11, MyUtils.timestampToString(transport.getPlannedTimeAtPatient(), MyUtils.sqlDateTime));
			query.setString(12, transport.getKindOfTransport());
			query.setString(13, transport.getKindOfIllness());
			query.setString(14, transport.getPatient().getFirstname());
			query.setString(15, transport.getPatient().getLastname());
			query.setInt(16, transport.getPlanedLocation().getId());
			query.setString(17, transport.getFromStreet());
			query.setString(18, transport.getFromCity());
			query.setString(19, transport.getToStreet());
			query.setString(20, transport.getToCity());
			query.setInt(21, transport.getProgramStatus());
			query.setString(22, MyUtils.timestampToString(transport.getDateOfTransport(), MyUtils.sqlDate));
			query.setInt(23, transport.getTransportId());
			query.executeUpdate();

			assignTransportstate(transport);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean archiveTransport(Transport transport)
	{
		// TODO Not needed any more!
		return false;
	}

	@Override
	public int assignVehicleToTransport(Transport transport)
	{
		try
		{
			//select vehicle from transport
			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.vehicleFromTransport"));
			query1.setInt(1, transport.getTransportId());
			final ResultSet rs1 = query1.executeQuery();

			if(rs1.next())
			{
				//Updates existing vehicle
				//vehicle_ID = ?, medic2_ID = ?, medic1_ID = ?, driver_ID = ?, locationname = ?, note = ?, vehicletype = ? WHERE transport_ID = ?;
				final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("update.assignedVehicle"));
				query.setString(1, transport.getVehicleDetail().getVehicleName());
				query.setInt(2, transport.getVehicleDetail().getSecondParamedic().getStaffMemberId());
				query.setInt(3, transport.getVehicleDetail().getFirstParamedic().getStaffMemberId());
				query.setInt(4, transport.getVehicleDetail().getDriver().getStaffMemberId());
				query.setString(5, transport.getVehicleDetail().getCurrentStation().getLocationName());
				query.setString(6, transport.getVehicleDetail().getVehicleNotes());
				query.setString(7, transport.getVehicleDetail().getVehicleType());
				query.setInt(8, transport.getTransportId());
				query.executeUpdate();
				return 1;
			}
			else
			{
				//transport_ID, vehicle_ID, medic2_ID, medic1_ID, driver_ID, locationname, note, vehicletype
				final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("add.assignedVehicle"));
				query.setInt(1, transport.getTransportId());
				query.setString(2, transport.getVehicleDetail().getVehicleName());
				query.setInt(3, transport.getVehicleDetail().getSecondParamedic().getStaffMemberId());
				query.setInt(4, transport.getVehicleDetail().getFirstParamedic().getStaffMemberId());
				query.setInt(5, transport.getVehicleDetail().getDriver().getStaffMemberId());
				query.setString(6, transport.getVehicleDetail().getCurrentStation().getLocationName());
				query.setString(7, transport.getVehicleDetail().getVehicleNotes());
				query.setString(8, transport.getVehicleDetail().getVehicleType());
				query.executeUpdate();
				return 1;
			}


		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return -1;
		}	
	}

	@Override
	public boolean removeVehicleFromTransport(Transport transport)
	{
		try
		{
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("remove.assignedVehicle"));
			query.setInt(1, transport.getTransportId());
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
	public boolean cancelTransport(int transportId)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Transport> getTransportsFromVehicle(String vehicleName)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Transport> listArchivedTransports(long startdate, long enddate)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * inserts a new transportstate
	 * @param transportId
	 * @param selectedId
	 * @return true if insert was sucessful
	 */
	private boolean addTransportState(int transportId, int selectedId)
	{
		try
		{
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("add.selectedTransportItem"));
			query.setInt(1, selectedId);
			query.setInt(2, transportId);			
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
	public boolean assignTransportstate(Transport transport)
	{
		//removes all selected transports for specific transportId
		removeTransportstate(transport);

		if(transport.isEmergencyDoctorAlarming() == true)
			addTransportState(transport.getTransportId(), 1);
		if(transport.isPoliceAlarming() == true)
			addTransportState(transport.getTransportId(), 2);
		if(transport.isFirebrigadeAlarming() == true)
			addTransportState(transport.getTransportId(), 3);
		if(transport.isMountainRescueServiceAlarming() == true)
			addTransportState(transport.getTransportId(), 4);
		if(transport.isDfAlarming() == true)
			addTransportState(transport.getTransportId(), 5);
		if(transport.isBrkdtAlarming() == true)
			addTransportState(transport.getTransportId(), 6);
		if(transport.isBlueLightToGoal() == true)
			addTransportState(transport.getTransportId(), 7);
		if(transport.isHelicopterAlarming() == true)
			addTransportState(transport.getTransportId(), 8);
		if(transport.isAssistantPerson() == true)
			addTransportState(transport.getTransportId(), 9);
		if(transport.isBackTransport() == true)
			addTransportState(transport.getTransportId(), 10);
		if(transport.isLongDistanceTrip() == true)
			addTransportState(transport.getTransportId(), 11);
		if(transport.isEmergencyPhone() == true)
			addTransportState(transport.getTransportId(), 12);

		return true;
	}

	@Override
	public boolean removeTransportstate(Transport transport)
	{
		try
		{
			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("remove.AllSelectedTransportItems"));
			query1.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean updateTransportstate(Transport transport)
	{
		boolean result = assignTransportstate(transport);
		return result;
	}
}
