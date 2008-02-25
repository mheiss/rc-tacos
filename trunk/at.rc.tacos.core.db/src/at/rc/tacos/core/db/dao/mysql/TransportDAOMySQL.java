package at.rc.tacos.core.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.Queries;
import at.rc.tacos.core.db.dao.CallerDAO;
import at.rc.tacos.core.db.dao.LocationDAO;
import at.rc.tacos.core.db.dao.StaffMemberDAO;
import at.rc.tacos.core.db.dao.TransportDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.*;
import at.rc.tacos.util.MyUtils;

public class TransportDAOMySQL implements TransportDAO, IProgramStatus
{
	//The data source to get the connection and the queries file
	private final DataSource source = DataSource.getInstance();
	private final Queries queries = Queries.getInstance();
	//dependend dao classes
	private final LocationDAO locationDAO = DaoFactory.MYSQL.createLocationDAO();
	private final StaffMemberDAO staffMemberDAO = DaoFactory.MYSQL.createStaffMemberDAO();
	private final CallerDAO callerDAO = DaoFactory.MYSQL.createNotifierDAO();

	@Override
	public int addTransport(Transport transport) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{	
			if(transport.getCallerDetail() != null)
			{
				int callerId = callerDAO.addCaller(transport.getCallerDetail());
				//assert the add was successfully
				if(callerId == -1)
					return Transport.TRANSPORT_ERROR;
				transport.getCallerDetail().setCallerId(callerId);
			}
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("insert.transport"));
			query.setString(1, null); //transport.getTransportId()
			query.setInt(2, transport.getTransportNumber());
			query.setInt(3, transport.getDirection());	
			if(transport.getCallerDetail() == null)
				query.setString(4, null);
			else
				query.setInt(4, transport.getCallerDetail().getCallerId());
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
			if(transport.getPatient() == null)
			{
				query.setString(15,null);
				query.setString(16, null);
			}
			else
			{
				query.setString(15, transport.getPatient().getFirstname());
				query.setString(16, transport.getPatient().getLastname());
			}
			if(transport.getPlanedLocation() == null)
				query.setString(17, null);
			else
				query.setInt(17, transport.getPlanedLocation().getId());
			query.setString(18, transport.getFromStreet());
			query.setString(19, transport.getFromCity());
			query.setString(20, transport.getToStreet());
			query.setString(21, transport.getToCity());
			query.setInt(22, transport.getProgramStatus());
			query.setString(23, MyUtils.timestampToString(transport.getDateOfTransport(), MyUtils.sqlDateTime));
			query.executeUpdate();
			//get the last inserted auto id
			ResultSet rs = query.getGeneratedKeys();
			if(!rs.first())
				return Transport.TRANSPORT_ERROR;
			transport.setTransportId(rs.getInt(1));

			//assign the transport items!
			if(!assignTransportItems(transport))
				return Transport.TRANSPORT_ERROR;

			return transport.getTransportId();
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public List<Transport> listRunningTransports() throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.runningTransports"));
			query.setInt(1, IProgramStatus.PROGRAM_STATUS_OUTSTANDING);
			query.setInt(2, IProgramStatus.PROGRAM_STATUS_UNDERWAY);
			final ResultSet rs = query.executeQuery();
			List<Transport> transports = new ArrayList<Transport>();
			while(rs.next())
			{
				//create the new transport
				Transport transport = new Transport();

				transport.setTransportId(rs.getInt("t.transport_ID"));
				transport.setTransportNumber(rs.getInt("t.transportNr"));

				if(rs.getInt("t.planned_location") != 0)
				{
					Location station = new Location();
					station.setId(rs.getInt("t.planned_location"));
					station.setLocationName(rs.getString("lo.locationname"));
					transport.setPlanedLocation(station);
				}

				if(rs.getInt("t.caller_ID") != 0)
				{
					CallerDetail caller = new CallerDetail();
					caller.setCallerId(rs.getInt("caller_ID"));
					caller.setCallerName(rs.getString("callername"));
					caller.setCallerTelephoneNumber(rs.getString("caller_phonenumber"));
					transport.setCallerDetail(caller);
				}
				transport.setCreatedByUsername(rs.getString("t.createdBy_user"));
				if(rs.getString("t.note") == null)
					transport.setNotes("");
				else
					transport.setNotes(rs.getString("t.note"));
				if(rs.getString("t.feedback") == null)
					transport.setFeedback("");
				else
					transport.setFeedback(rs.getString("t.feedback"));
				transport.setCreationTime(MyUtils.stringToTimestamp(rs.getString("creationDate"), MyUtils.sqlDateTime));
				transport.setPlannedStartOfTransport(MyUtils.stringToTimestamp(rs.getString("t.departure"), MyUtils.sqlDateTime));
				transport.setAppointmentTimeAtDestination(MyUtils.stringToTimestamp(rs.getString("t.appointment"), MyUtils.sqlDateTime));
				transport.setPlannedTimeAtPatient(MyUtils.stringToTimestamp(rs.getString("t.appointmentPatient"), MyUtils.sqlDateTime));
				if(rs.getString("t.disease") == null)
					transport.setKindOfIllness("");
				else
					transport.setKindOfIllness(rs.getString("t.disease"));
				if(rs.getString("t.from_street") == null)
					transport.setFromStreet("");
				else
					transport.setFromStreet(rs.getString("t.from_street"));
				if(rs.getString("t.from_city") == null)
					transport.setFromCity("");
				else
					transport.setFromCity(rs.getString("t.from_city"));
				if(rs.getString("t.to_street") == null)
					transport.setToStreet("");
				else
					transport.setToStreet(rs.getString("t.to_street"));
				if(rs.getString("t.to_city") == null)
					transport.setToCity("");
				else
					transport.setToCity(rs.getString("t.to_city"));
				transport.setProgramStatus(rs.getInt("t.programstate"));
				if(rs.getString("t.transporttype") == null)
					transport.setKindOfTransport("");
				else
					transport.setKindOfTransport(rs.getString("t.transporttype"));
				transport.setTransportPriority(rs.getString("t.priority"));
				transport.setDirection(rs.getInt("t.direction"));
				transport.setDateOfTransport(MyUtils.stringToTimestamp(rs.getString("t.dateOfTransport"), MyUtils.sqlDateTime));
				
				//The patient of the transport
				Patient patient = new Patient();
				if(rs.getString("t.firstname") == null)
					patient.setFirstname("");
				else
					patient.setFirstname(rs.getString("t.firstname"));
				if(rs.getString("t.lastname") == null)
					patient.setLastname("");
				else
					patient.setLastname(rs.getString("t.lastname"));
				transport.setPatient(patient);
				
				//The assigned vehicle of the transport
				if(rs.getString("av.location_ID") != null)
				{
					//get the location 
					Location currentStation = locationDAO.getLocation(rs.getInt("av.location_ID"));
					VehicleDetail vehicle = new VehicleDetail();
					vehicle.setCurrentStation(currentStation);
					vehicle.setVehicleName(rs.getString("av.vehicle_ID"));

					StaffMember driver = new StaffMember();
					driver = staffMemberDAO.getStaffMemberByID(rs.getInt("av.driver_ID"));
					vehicle.setDriver(driver);

					StaffMember firstParamedic = new StaffMember();
					firstParamedic = staffMemberDAO.getStaffMemberByID(rs.getInt("av.medic1_ID"));
					vehicle.setFirstParamedic(firstParamedic);

					StaffMember secondParamedic = new StaffMember();
					secondParamedic = staffMemberDAO.getStaffMemberByID(rs.getInt("av.medic2_ID"));
					vehicle.setSecondParamedic(secondParamedic);

					vehicle.setVehicleNotes(rs.getString("av.note"));
					vehicle.setVehicleType(rs.getString("av.vehicletype"));
					transport.setVehicleDetail(vehicle);
				}

				//get the transport states
				final PreparedStatement stateQuery = connection.prepareStatement(queries.getStatment("list.transportstates"));
				stateQuery.setInt(1, transport.getTransportId());
				final ResultSet stateResult = stateQuery.executeQuery();
				//loop over the result set
				while(stateResult.next())
					transport.addStatus(stateResult.getInt("transportstate"), MyUtils.stringToTimestamp(stateResult.getString("date"), MyUtils.sqlDateTime));

				// find the selected items (boolean values)
				final PreparedStatement query1 = connection.prepareStatement(queries.getStatment("list.selectedTransportItems"));
				query1.setInt(1, transport.getTransportId());
				final ResultSet rs1 = query1.executeQuery();

				while(rs1.next())
				{
					if(rs1.getInt("selected_ID") == 1)
						transport.setEmergencyDoctorAlarming(true);
					else 
						transport.setEmergencyDoctorAlarming(false);

					if(rs1.getInt("selected_ID") == 2)
						transport.setPoliceAlarming(true);
					else 
						transport.setPoliceAlarming(false);

					if(rs1.getInt("selected_ID") == 3)
						transport.setFirebrigadeAlarming(true);
					else 
						transport.setFirebrigadeAlarming(false);

					if(rs1.getInt("selected_ID") == 4)
						transport.setMountainRescueServiceAlarming(true);
					else 
						transport.setMountainRescueServiceAlarming(false);

					if(rs1.getInt("selected_ID") == 5)
						transport.setDfAlarming(true);
					else 
						transport.setDfAlarming(false);

					if(rs1.getInt("selected_ID") == 6)
						transport.setBrkdtAlarming(true);
					else 
						transport.setBrkdtAlarming(false);

					if(rs1.getInt("selected_ID") == 7)
						transport.setBlueLightToGoal(true);
					else 
						transport.setBlueLightToGoal(false);

					if(rs1.getInt("selected_ID") == 8)
						transport.setHelicopterAlarming(true);
					else 
						transport.setHelicopterAlarming(false);

					if(rs1.getInt("selected_ID") == 9)
						transport.setAssistantPerson(true);
					else 
						transport.setAssistantPerson(false);

					if(rs1.getInt("selected_ID") == 10)
						transport.setBackTransport(true);
					else 
						transport.setBackTransport(false);

					if(rs1.getInt("selected_ID") == 11)
						transport.setLongDistanceTrip(true);
					else 
						transport.setLongDistanceTrip(false);

					if(rs1.getInt("selected_ID") == 12)
						transport.setEmergencyPhone(true);
					else 
						transport.setEmergencyPhone(false);
				}
				
				//add the transport to the list
				transports.add(transport);
			}
			//return the listed transports
			return transports;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public List<Transport> listPrebookedTransports() throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.prebookedTransports"));
			query.setInt(1, IProgramStatus.PROGRAM_STATUS_PREBOOKING);
			final ResultSet rs = query.executeQuery();
			List<Transport> transports = new ArrayList<Transport>();
			while(rs.next())
			{
				//create the new transport
				Transport transport = new Transport();

				transport.setTransportId(rs.getInt("t.transport_ID"));
				transport.setTransportNumber(rs.getInt("t.transportNr"));

				if(rs.getInt("t.planned_location") != 0)
				{
					Location station = new Location();
					station.setId(rs.getInt("t.planned_location"));
					station.setLocationName(rs.getString("lo.locationname"));
					transport.setPlanedLocation(station);
				}

				if(rs.getInt("t.caller_ID") != 0)
				{
					CallerDetail caller = new CallerDetail();
					caller.setCallerId(rs.getInt("caller_ID"));
					caller.setCallerName(rs.getString("callername"));
					caller.setCallerTelephoneNumber(rs.getString("caller_phonenumber"));
					transport.setCallerDetail(caller);
				}
				transport.setCreatedByUsername(rs.getString("t.createdBy_user"));
				if(rs.getString("t.note") == null)
					transport.setNotes("");
				else
					transport.setNotes(rs.getString("t.note"));
				if(rs.getString("t.feedback") == null)
					transport.setFeedback("");
				else
					transport.setFeedback(rs.getString("t.feedback"));
				transport.setCreationTime(MyUtils.stringToTimestamp(rs.getString("creationDate"), MyUtils.sqlDateTime));
				transport.setPlannedStartOfTransport(MyUtils.stringToTimestamp(rs.getString("t.departure"), MyUtils.sqlDateTime));
				transport.setAppointmentTimeAtDestination(MyUtils.stringToTimestamp(rs.getString("t.appointment"), MyUtils.sqlDateTime));
				transport.setPlannedTimeAtPatient(MyUtils.stringToTimestamp(rs.getString("t.appointmentPatient"), MyUtils.sqlDateTime));
				if(rs.getString("t.disease") == null)
					transport.setKindOfIllness("");
				else
					transport.setKindOfIllness(rs.getString("t.disease"));
				if(rs.getString("t.from_street") == null)
					transport.setFromStreet("");
				else
					transport.setFromStreet(rs.getString("t.from_street"));
				if(rs.getString("t.from_city") == null)
					transport.setFromCity("");
				else
					transport.setFromCity(rs.getString("t.from_city"));
				if(rs.getString("t.to_street") == null)
					transport.setToStreet("");
				else
					transport.setToStreet(rs.getString("t.to_street"));
				if(rs.getString("t.to_city") == null)
					transport.setToCity("");
				else
					transport.setToCity(rs.getString("t.to_city"));
				transport.setProgramStatus(rs.getInt("t.programstate"));
				if(rs.getString("t.transporttype") == null)
					transport.setKindOfTransport("");
				else
					transport.setKindOfTransport(rs.getString("t.transporttype"));
				transport.setTransportPriority(rs.getString("t.priority"));
				transport.setDirection(rs.getInt("t.direction"));
				transport.setDateOfTransport(MyUtils.stringToTimestamp(rs.getString("t.dateOfTransport"), MyUtils.sqlDateTime));
				
				//The patient of the transport
				Patient patient = new Patient();
				if(rs.getString("t.firstname") == null)
					patient.setFirstname("");
				else
					patient.setFirstname(rs.getString("t.firstname"));
				if(rs.getString("t.lastname") == null)
					patient.setLastname("");
				else
					patient.setLastname(rs.getString("t.lastname"));
				transport.setPatient(patient);
				
				// find the selected items (boolean values)
				final PreparedStatement query1 = connection.prepareStatement(queries.getStatment("list.selectedTransportItems"));
				query1.setInt(1, transport.getTransportId());
				final ResultSet rs1 = query1.executeQuery();

				while(rs1.next())
				{
					if(rs1.getInt("selected_ID") == 1)
						transport.setEmergencyDoctorAlarming(true);
					else 
						transport.setEmergencyDoctorAlarming(false);

					if(rs1.getInt("selected_ID") == 2)
						transport.setPoliceAlarming(true);
					else 
						transport.setPoliceAlarming(false);

					if(rs1.getInt("selected_ID") == 3)
						transport.setFirebrigadeAlarming(true);
					else 
						transport.setFirebrigadeAlarming(false);

					if(rs1.getInt("selected_ID") == 4)
						transport.setMountainRescueServiceAlarming(true);
					else 
						transport.setMountainRescueServiceAlarming(false);

					if(rs1.getInt("selected_ID") == 5)
						transport.setDfAlarming(true);
					else 
						transport.setDfAlarming(false);

					if(rs1.getInt("selected_ID") == 6)
						transport.setBrkdtAlarming(true);
					else 
						transport.setBrkdtAlarming(false);

					if(rs1.getInt("selected_ID") == 7)
						transport.setBlueLightToGoal(true);
					else 
						transport.setBlueLightToGoal(false);

					if(rs1.getInt("selected_ID") == 8)
						transport.setHelicopterAlarming(true);
					else 
						transport.setHelicopterAlarming(false);

					if(rs1.getInt("selected_ID") == 9)
						transport.setAssistantPerson(true);
					else 
						transport.setAssistantPerson(false);

					if(rs1.getInt("selected_ID") == 10)
						transport.setBackTransport(true);
					else 
						transport.setBackTransport(false);

					if(rs1.getInt("selected_ID") == 11)
						transport.setLongDistanceTrip(true);
					else 
						transport.setLongDistanceTrip(false);

					if(rs1.getInt("selected_ID") == 12)
						transport.setEmergencyPhone(true);
					else 
						transport.setEmergencyPhone(false);
				}
				
				//add the transport to the list
				transports.add(transport);
			}
			//return the listed transports
			return transports;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public List<Transport> listArchivedTransports(long startdate, long enddate) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.archivedTransports"));
			query.setString(1, MyUtils.timestampToString(startdate, MyUtils.sqlDate));
			query.setString(2, MyUtils.timestampToString(enddate, MyUtils.sqlDate));
			query.setInt(3, PROGRAM_STATUS_JOURNAL);
			final ResultSet rs = query.executeQuery();
			List<Transport> transports = new ArrayList<Transport>();
			while(rs.next())
			{
				//create the new transport
				Transport transport = new Transport();

				transport.setTransportId(rs.getInt("t.transport_ID"));
				transport.setTransportNumber(rs.getInt("t.transportNr"));

				if(rs.getInt("t.planned_location") != 0)
				{
					Location station = new Location();
					station.setId(rs.getInt("t.planned_location"));
					station.setLocationName(rs.getString("lo.locationname"));
					transport.setPlanedLocation(station);
				}

				if(rs.getInt("t.caller_ID") != 0)
				{
					CallerDetail caller = new CallerDetail();
					caller.setCallerId(rs.getInt("caller_ID"));
					caller.setCallerName(rs.getString("callername"));
					caller.setCallerTelephoneNumber(rs.getString("caller_phonenumber"));
					transport.setCallerDetail(caller);
				}
				transport.setCreatedByUsername(rs.getString("t.createdBy_user"));
				if(rs.getString("t.note") == null)
					transport.setNotes("");
				else
					transport.setNotes(rs.getString("t.note"));
				if(rs.getString("t.feedback") == null)
					transport.setFeedback("");
				else
					transport.setFeedback(rs.getString("t.feedback"));
				transport.setCreationTime(MyUtils.stringToTimestamp(rs.getString("creationDate"), MyUtils.sqlDateTime));
				transport.setPlannedStartOfTransport(MyUtils.stringToTimestamp(rs.getString("t.departure"), MyUtils.sqlDateTime));
				transport.setAppointmentTimeAtDestination(MyUtils.stringToTimestamp(rs.getString("t.appointment"), MyUtils.sqlDateTime));
				transport.setPlannedTimeAtPatient(MyUtils.stringToTimestamp(rs.getString("t.appointmentPatient"), MyUtils.sqlDateTime));
				if(rs.getString("t.disease") == null)
					transport.setKindOfIllness("");
				else
					transport.setKindOfIllness(rs.getString("t.disease"));
				if(rs.getString("t.from_street") == null)
					transport.setFromStreet("");
				else
					transport.setFromStreet(rs.getString("t.from_street"));
				if(rs.getString("t.from_city") == null)
					transport.setFromCity("");
				else
					transport.setFromCity(rs.getString("t.from_city"));
				if(rs.getString("t.to_street") == null)
					transport.setToStreet("");
				else
					transport.setToStreet(rs.getString("t.to_street"));
				if(rs.getString("t.to_city") == null)
					transport.setToCity("");
				else
					transport.setToCity(rs.getString("t.to_city"));
				transport.setProgramStatus(rs.getInt("t.programstate"));
				if(rs.getString("t.transporttype") == null)
					transport.setKindOfTransport("");
				else
					transport.setKindOfTransport(rs.getString("t.transporttype"));
				transport.setTransportPriority(rs.getString("t.priority"));
				transport.setDirection(rs.getInt("t.direction"));
				transport.setDateOfTransport(MyUtils.stringToTimestamp(rs.getString("t.dateOfTransport"), MyUtils.sqlDateTime));
				
				//The patient of the transport
				Patient patient = new Patient();
				if(rs.getString("t.firstname") == null)
					patient.setFirstname("");
				else
					patient.setFirstname(rs.getString("t.firstname"));
				if(rs.getString("t.lastname") == null)
					patient.setLastname("");
				else
					patient.setLastname(rs.getString("t.lastname"));
				transport.setPatient(patient);
				
				//The assigned vehicle of the transport
				if(rs.getString("av.location_ID") != null)
				{
					//get the location 
					Location currentStation = locationDAO.getLocation(rs.getInt("av.location_ID"));
					VehicleDetail vehicle = new VehicleDetail();
					vehicle.setCurrentStation(currentStation);
					vehicle.setVehicleName(rs.getString("av.vehicle_ID"));

					StaffMember driver = new StaffMember();
					driver = staffMemberDAO.getStaffMemberByID(rs.getInt("av.driver_ID"));
					vehicle.setDriver(driver);

					StaffMember firstParamedic = new StaffMember();
					firstParamedic = staffMemberDAO.getStaffMemberByID(rs.getInt("av.medic1_ID"));
					vehicle.setFirstParamedic(firstParamedic);

					StaffMember secondParamedic = new StaffMember();
					secondParamedic = staffMemberDAO.getStaffMemberByID(rs.getInt("av.medic2_ID"));
					vehicle.setSecondParamedic(secondParamedic);

					vehicle.setVehicleNotes(rs.getString("av.note"));
					vehicle.setVehicleType(rs.getString("av.vehicletype"));
					transport.setVehicleDetail(vehicle);
				}

				//get the transport states
				final PreparedStatement stateQuery = connection.prepareStatement(queries.getStatment("list.transportstates"));
				stateQuery.setInt(1, transport.getTransportId());
				final ResultSet stateResult = stateQuery.executeQuery();
				//loop over the result set
				while(stateResult.next())
					transport.addStatus(stateResult.getInt("transportstate"), MyUtils.stringToTimestamp(stateResult.getString("date"), MyUtils.sqlDateTime));

				// find the selected items (boolean values)
				final PreparedStatement query1 = connection.prepareStatement(queries.getStatment("list.selectedTransportItems"));
				query1.setInt(1, transport.getTransportId());
				final ResultSet rs1 = query1.executeQuery();

				while(rs1.next())
				{
					if(rs1.getInt("selected_ID") == 1)
						transport.setEmergencyDoctorAlarming(true);
					else 
						transport.setEmergencyDoctorAlarming(false);

					if(rs1.getInt("selected_ID") == 2)
						transport.setPoliceAlarming(true);
					else 
						transport.setPoliceAlarming(false);

					if(rs1.getInt("selected_ID") == 3)
						transport.setFirebrigadeAlarming(true);
					else 
						transport.setFirebrigadeAlarming(false);

					if(rs1.getInt("selected_ID") == 4)
						transport.setMountainRescueServiceAlarming(true);
					else 
						transport.setMountainRescueServiceAlarming(false);

					if(rs1.getInt("selected_ID") == 5)
						transport.setDfAlarming(true);
					else 
						transport.setDfAlarming(false);

					if(rs1.getInt("selected_ID") == 6)
						transport.setBrkdtAlarming(true);
					else 
						transport.setBrkdtAlarming(false);

					if(rs1.getInt("selected_ID") == 7)
						transport.setBlueLightToGoal(true);
					else 
						transport.setBlueLightToGoal(false);

					if(rs1.getInt("selected_ID") == 8)
						transport.setHelicopterAlarming(true);
					else 
						transport.setHelicopterAlarming(false);

					if(rs1.getInt("selected_ID") == 9)
						transport.setAssistantPerson(true);
					else 
						transport.setAssistantPerson(false);

					if(rs1.getInt("selected_ID") == 10)
						transport.setBackTransport(true);
					else 
						transport.setBackTransport(false);

					if(rs1.getInt("selected_ID") == 11)
						transport.setLongDistanceTrip(true);
					else 
						transport.setLongDistanceTrip(false);

					if(rs1.getInt("selected_ID") == 12)
						transport.setEmergencyPhone(true);
					else 
						transport.setEmergencyPhone(false);
				}
				
				//add the transport to the list
				transports.add(transport);
			}
			return transports;
		}
		finally
		{
			connection.close();
		}
	}

	/**
	 * Update the transport 
	 */
	@Override
	public boolean updateTransport(Transport transport) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			/** add or update the caller of the transport*/
			if(transport.getCallerDetail() != null && transport.getCallerDetail().getCallerId() == -1)
			{
				System.out.println("Adding caller");
				int callerId = callerDAO.addCaller(transport.getCallerDetail());
				//assert the add was successfully
				if(callerId == -1)
				{
					System.out.println("Caller adding failed");
					return false;
				}
				transport.getCallerDetail().setCallerId(callerId);
			}
			else if(transport.getCallerDetail() != null)
			{
				if(!callerDAO.updateCaller(transport.getCallerDetail()))
				{
					System.out.println("Caller update failed");
					return false;
				}
			}

			//update the transport
			if(!executeTheTransportUpdateQuery(transport))
			{
				System.out.println("Transport update failed");
				return false;
			}

			//assign the boolean values police,fire alarming, ...
			if(!assignTransportItems(transport))
			{
				System.out.println("Assigning transport items failed");
				return false;
			}

			//assign the transport states S1,S2,....
			if(!assignTransportstate(transport))
			{
				System.out.println("Assigning transport state failed");
				return false;
			}
			//everything is ok
			return true;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public int generateTransportNumber(Transport transport) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			//assert that we have a current station and a vehicle
			if(transport.getVehicleDetail() == null || transport.getVehicleDetail().getCurrentStation() == null)
				return Transport.TRANSPORT_ERROR;

			int locationId = transport.getVehicleDetail().getCurrentStation().getId();

			//STEP1: INSERT the vehicle in the assigned_vehicle table
			if(!assignVehicle(transport))
				return Transport.TRANSPORT_ERROR;

			//STEP2: Query the tmptransport
			int tmpNr = getTransportNrFromTemp(locationId);
			//transport number valid, so return it
			if(tmpNr > 0)
			{
				if(!updateTransportNr(transport.getTransportId(),tmpNr))
					return Transport.TRANSPORT_ERROR;
				return tmpNr;
			}

			//check for errors
			if(tmpNr == Transport.TRANSPORT_ERROR)
				return Transport.TRANSPORT_ERROR;

			//get the highest number from the table and update the transport
			tmpNr = getHighestTransportNumber(locationId, transport.getYear());
			tmpNr++;
			if(!updateTransportNr(transport.getTransportId(), tmpNr))
				return Transport.TRANSPORT_ERROR;
			return tmpNr;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public boolean removeVehicleFromTransport(Transport transport) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			int oldNumber = getTransportNrById(transport.getTransportId());
			int transportId = transport.getTransportId();
			int locationId = getLocationOfTransport(transport.getTransportId());

			//remove the vehicle from the transport
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("remove.assignedVehicle"));
			query.setInt(1, transportId);
			//assert the remove was successfully
			if(query.executeUpdate() == 0)
				return false;

			//reset the transport number
			transport.setTransportNumber(0);
			if(!updateTransportNr(transport.getTransportId(), transport.getTransportNumber()))
				return false;

			//store the number in the temp table
			if(!archiveTransportNumber(oldNumber, locationId))
				return false;

			//set the transport status to IProgramStatus.PROGRAM_STATUS_OUTSTANDING 
			transport.setProgramStatus(IProgramStatus.PROGRAM_STATUS_OUTSTANDING);
			if(!executeTheTransportUpdateQuery(transport))
				return false;

			return true;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public boolean cancelTransport(Transport transport) throws SQLException
	{
		Connection connection = source.getConnection();
		int locationId = 0;
		try
		{
			int transportId = transport.getTransportId();
			int oldNumber = getTransportNrById(transport.getTransportId());
			if(transport.getVehicleDetail() != null)
			{
				locationId = transport.getVehicleDetail().getCurrentStation().getId();
			}
			//archive the transport number if we have one
			if(oldNumber > 0)
			{
				if(!archiveTransportNumber(oldNumber, locationId))
				{
					return false;
				}
			}
			//set the transport number to TRANSPORT_STORNO or TRANSPORT_FORWARD
			if(!updateTransportNr(transportId, transport.getTransportNumber()))
			{
				return false;
			}
			//set the program status to journal
			transport.setProgramStatus(IProgramStatus.PROGRAM_STATUS_JOURNAL);
			return true;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public Transport getTransportById(int id) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("get.transportById"));
			query.setInt(1, id);
			final ResultSet rs = query.executeQuery();
			if(rs.first())
			{
				Transport transport = new Transport();
				transport.setTransportId(rs.getInt("t.transport_ID"));
				transport.setTransportNumber(rs.getInt("t.transportNr"));
				if(rs.getInt("t.planned_location") != 0)
				{
					Location station = locationDAO.getLocation(rs.getInt("t.planned_location"));
					transport.setPlanedLocation(station);
				}

				if(rs.getInt("t.caller_ID") != 0)
				{
					CallerDetail caller = new CallerDetail();
					caller.setCallerId(rs.getInt("t.caller_ID"));
					caller.setCallerName(rs.getString("ca.callername"));
					caller.setCallerTelephoneNumber(rs.getString("ca.caller_phonenumber"));
					transport.setCallerDetail(caller);
				}
				transport.setCreatedByUsername(rs.getString("t.createdBy_user"));
				if(rs.getString("t.note") == null)
					transport.setNotes("");
				else
					transport.setNotes(rs.getString("t.note"));
				if(rs.getString("t.feedback") == null)
					transport.setFeedback("");
				else
					transport.setFeedback(rs.getString("t.feedback"));
				transport.setCreationTime(MyUtils.stringToTimestamp(rs.getString("creationDate"), MyUtils.sqlDateTime));
				transport.setPlannedStartOfTransport(MyUtils.stringToTimestamp(rs.getString("t.departure"), MyUtils.sqlDateTime));
				transport.setAppointmentTimeAtDestination(MyUtils.stringToTimestamp(rs.getString("t.appointment"), MyUtils.sqlDateTime));
				transport.setPlannedTimeAtPatient(MyUtils.stringToTimestamp(rs.getString("t.appointmentPatient"), MyUtils.sqlDateTime));
				if(rs.getString("t.disease") == null)
					transport.setKindOfIllness("");
				else
					transport.setKindOfIllness(rs.getString("t.disease"));

				Patient patient = new Patient();
				if(rs.getString("t.firstname") == null)
					patient.setFirstname("");
				else
					patient.setFirstname(rs.getString("t.firstname"));
				if(rs.getString("t.lastname") == null)
					patient.setLastname("");
				else
					patient.setLastname(rs.getString("t.lastname"));
				transport.setPatient(patient);

				if(rs.getString("t.from_street") == null)
					transport.setFromStreet("");
				else
					transport.setFromStreet(rs.getString("t.from_street"));
				if(rs.getString("t.from_city") == null)
					transport.setFromCity("");
				else
					transport.setFromCity(rs.getString("t.from_city"));
				if(rs.getString("t.to_street") == null)
					transport.setToStreet("");
				else
					transport.setToStreet(rs.getString("t.to_street"));
				if(rs.getString("t.to_city") == null)
					transport.setToCity("");
				else
					transport.setToCity(rs.getString("t.to_city"));
				transport.setProgramStatus(rs.getInt("t.programstate"));
				if(rs.getString("t.transporttype") == null)
					transport.setKindOfTransport("");
				else
					transport.setKindOfTransport(rs.getString("t.transporttype"));
				transport.setTransportPriority(rs.getString("t.priority"));
				transport.setDirection(rs.getInt("t.direction"));
				transport.setDateOfTransport(MyUtils.stringToTimestamp(rs.getString("t.dateOfTransport"), MyUtils.sqlDateTime));

				if(rs.getString("av.location_ID") != null)
				{
					VehicleDetail vehicle = new VehicleDetail();
					Location currentStation = new Location();
					currentStation = locationDAO.getLocation(rs.getInt("av.location_ID"));
					vehicle.setCurrentStation(currentStation);

					vehicle.setVehicleName(rs.getString("av.vehicle_ID"));

					StaffMember driver = new StaffMember();
					driver = staffMemberDAO.getStaffMemberByID(rs.getInt("av.driver_ID"));
					vehicle.setDriver(driver);

					StaffMember firstParamedic = new StaffMember();
					firstParamedic = staffMemberDAO.getStaffMemberByID(rs.getInt("av.medic1_ID"));
					vehicle.setFirstParamedic(firstParamedic);

					StaffMember secondParamedic = new StaffMember();
					secondParamedic = staffMemberDAO.getStaffMemberByID(rs.getInt("av.medic2_ID"));
					vehicle.setSecondParamedic(secondParamedic);

					vehicle.setVehicleNotes(rs.getString("av.note"));
					vehicle.setVehicleType(rs.getString("av.vehicletype"));
					transport.setVehicleDetail(vehicle);
				}

				//get the transport states
				final PreparedStatement stateQuery = connection.prepareStatement(queries.getStatment("list.transportstates"));
				stateQuery.setInt(1, transport.getTransportId());
				final ResultSet stateResult = stateQuery.executeQuery();
				//loop over the result set
				while(stateResult.next())
				{
					transport.addStatus(stateResult.getInt("transportstate"), MyUtils.stringToTimestamp(stateResult.getString("date"), MyUtils.sqlDateTime));
				}

				// find the selected items (boolean values)
				final PreparedStatement query1 = connection.prepareStatement(queries.getStatment("list.selectedTransportItems"));
				query1.setInt(1, transport.getTransportId());
				final ResultSet rs1 = query1.executeQuery();
				while(rs1.next())
				{
					if(rs1.getInt("selected_ID") == 1)
						transport.setEmergencyDoctorAlarming(true);

					if(rs1.getInt("selected_ID") == 2)
						transport.setPoliceAlarming(true);

					if(rs1.getInt("selected_ID") == 3)
						transport.setFirebrigadeAlarming(true);

					if(rs1.getInt("selected_ID") == 4)
						transport.setMountainRescueServiceAlarming(true);

					if(rs1.getInt("selected_ID") == 5)
						transport.setDfAlarming(true);

					if(rs1.getInt("selected_ID") == 6)
						transport.setBrkdtAlarming(true);

					if(rs1.getInt("selected_ID") == 7)
						transport.setBlueLightToGoal(true);

					if(rs1.getInt("selected_ID") == 8)
						transport.setHelicopterAlarming(true);

					if(rs1.getInt("selected_ID") == 9)
						transport.setAssistantPerson(true);

					if(rs1.getInt("selected_ID") == 10)
						transport.setBackTransport(true);

					if(rs1.getInt("selected_ID") == 11)
						transport.setLongDistanceTrip(true);

					if(rs1.getInt("selected_ID") == 12)
						transport.setEmergencyPhone(true);
				}
				return transport;
			}
			//nothing in the result set
			return null;
		}
		finally
		{
			connection.close();
		}
	}

	private boolean executeTheTransportUpdateQuery(Transport transport) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("update.transport"));
			query.setInt(1, transport.getDirection());
			//no caller, so set to null
			if(transport.getCallerDetail() == null)
				query.setString(2, null);
			else
				query.setInt(2, transport.getCallerDetail().getCallerId());
			query.setString(3, transport.getNotes());
			query.setString(4, transport.getCreatedByUsername());
			query.setString(5, transport.getTransportPriority());
			query.setString(6, transport.getFeedback());
			query.setString(7, MyUtils.timestampToString(transport.getCreationTime(), MyUtils.sqlDateTime));
			query.setString(8, MyUtils.timestampToString(transport.getPlannedStartOfTransport(), MyUtils.sqlDateTime));
			query.setString(9, MyUtils.timestampToString(transport.getAppointmentTimeAtDestination(), MyUtils.sqlDateTime));
			query.setString(10, MyUtils.timestampToString(transport.getPlannedTimeAtPatient(), MyUtils.sqlDateTime));
			query.setString(11, transport.getKindOfTransport());
			query.setString(12, transport.getKindOfIllness());
			if (transport.getPatient() == null)
			{
				query.setString(13, null);
				query.setString(14, null);
			}
			else
			{
				query.setString(13, transport.getPatient().getFirstname());
				query.setString(14, transport.getPatient().getLastname());
			}
			if(transport.getPlanedLocation() == null)
				query.setString(15, null);
			else
				query.setInt(15, transport.getPlanedLocation().getId());
			query.setString(16, transport.getFromStreet());
			query.setString(17, transport.getFromCity());
			query.setString(18, transport.getToStreet());
			query.setString(19, transport.getToCity());
			query.setInt(20, transport.getProgramStatus());
			query.setString(21, MyUtils.timestampToString(transport.getDateOfTransport(), MyUtils.sqlDate));
			query.setInt(22, transport.getTransportNumber());
			query.setInt(23, transport.getTransportId());
			System.out.println("++++++++TransportDAOMySQL, transport number: " +transport.getTransportNumber());
			//assert the update was successfull
			if(query.executeUpdate() == 0)
				return false;
			return true;
		}
		finally
		{
			connection.close();
		}
	}

	/***********************************************
	 * HELPER METHODS FOR THE TRANSPORT DAO        *
	 * *********************************************/
	/**
	 * Assigns the vehicle to the transport.
	 * @param transport the transport to assign the vehicle to
	 * @return true if the insert was successfully
	 */
	private boolean assignVehicle(Transport transport) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("add.assignedVehicle"));
			query.setInt(1, transport.getTransportId());
			query.setString(2, transport.getVehicleDetail().getVehicleName());
			if(transport.getVehicleDetail().getSecondParamedic() != null)
				query.setInt(3, transport.getVehicleDetail().getSecondParamedic().getStaffMemberId());
			else
				query.setString(3, null);
			if(transport.getVehicleDetail().getFirstParamedic() != null)
				query.setInt(4, transport.getVehicleDetail().getFirstParamedic().getStaffMemberId());
			else
				query.setString(4, null);
			if(transport.getVehicleDetail().getDriver() != null)
				query.setInt(5, transport.getVehicleDetail().getDriver().getStaffMemberId());
			else
				query.setString(5, null);
			if(transport.getVehicleDetail().getCurrentStation() != null)
				query.setInt(6, transport.getVehicleDetail().getCurrentStation().getId());
			else
				query.setString(6, null);
			query.setString(7, transport.getVehicleDetail().getVehicleNotes());
			query.setString(8, transport.getVehicleDetail().getVehicleType());
			//assert the vehicle is inserted
			if(query.executeUpdate() == 0)
				return false;
			return true;
		}
		finally
		{
			connection.close();
		}
	}

	/**
	 * Searches for a canceled transport numer.
	 * @param locationId the location to search the transport number
	 * @return the canceled transport number, -1 if nothing found or -3 if an error occured
	 */
	private int getTransportNrFromTemp(int locationId) throws SQLException
	{
		System.out.println("Get Transport nummber from tmp");
		Connection connection = source.getConnection();
		try
		{
			//search through the tmp table whether tmp transportNumber exists or not
			final PreparedStatement queryStmt = connection.prepareStatement(queries.getStatment("get.tmpTransportNr"));
			queryStmt.setInt(1, locationId);
			final ResultSet rs = queryStmt.executeQuery();
			//return the transport number and remove the value from the table
			if(rs.first())
			{
				//the found transport number
				int transportNr = rs.getInt("transportNr");

				//remove from the table
				final PreparedStatement removeStmt = connection.prepareStatement(queries.getStatment("remove.tmpTransportNr"));
				removeStmt.setInt(1, locationId);
				removeStmt.setInt(2, transportNr);
				//assert the remove was successfully
				if(removeStmt.executeUpdate() == 0)
					return Transport.TRANSPORT_ERROR;

				//we have a transport number and removed the old from the temp
				return transportNr;
			}
			//no transport number found
			return -1;
		}
		finally
		{
			connection.close();
		}
	}

	/**
	 * Returns the highest transport number in the transport table for the given year and location.
	 * If no transport number matches the criteria the method will return 0<br>
	 * <b>The returned number have to be count up by one before use</b>
	 * @param location the location to get the number
	 * @param year the year to get the number
	 * @return the found transport number.
	 */
	private int getHighestTransportNumber(int locationId,int year) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			System.out.println("Getting the highest number");
			//calculate new transport number from transports
			final PreparedStatement queryStmt = connection.prepareStatement(queries.getStatment("get.MaxTransportNr"));
			queryStmt.setInt(1, locationId);
			queryStmt.setInt(2,year);
			final ResultSet rs = queryStmt.executeQuery();
			//assert we have a result
			if(rs.first())
			{
				int nr = rs.getInt(1);
				System.out.println("Highest number for location: "+ locationId + " nr: "+nr);
				return nr;
			}
			System.out.println("no result");
			//nothing found so return the number 0 to start again
			return 0;
		}
		finally
		{
			connection.close();
		}
	}

	/**
	 * Sets the transport number for the given transport
	 * @param transportId the transport to set the number
	 * @param transportNr the number to set
	 * @return true if the update was successfully
	 */
	private boolean updateTransportNr(int transportId,int transportNr) throws SQLException
	{
		System.out.println("updating transport number for transport:"+transportId);
		System.out.println("Updating nr: "+transportNr);
		Connection connection = source.getConnection();
		try
		{
			PreparedStatement updateStmt = connection.prepareStatement(queries.getStatment("update.transportNr"));
			updateStmt.setInt(1, transportNr);
			updateStmt.setInt(2, transportId);
			//assert the transport is updated
			if(updateStmt.executeUpdate() == 0)
				return false;
			return true;
		}
		finally
		{
			connection.close();
		}
	}

	/**
	 * Returns the transport number identified by the transport id.
	 * @param transportId the transport to get the number from
	 * @return the transport number or -1 in case of an error
	 */
	private int getTransportNrById(int transportId) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			PreparedStatement queryStmt = connection.prepareStatement(queries.getStatment("get.transportNr"));
			queryStmt.setInt(1, transportId);
			ResultSet rs = queryStmt.executeQuery();
			if(rs.first())
				return rs.getInt("transportNr");
			//no result set
			return -1;
		}
		finally
		{
			connection.close();
		}
	}

	/**
	 * Returns the location to which the vehicle of the given transport is assigned
	 * @param transportId the id of the transport to get the location
	 * @return the assigned location
	 */
	private int getLocationOfTransport(int transportId) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.LocationFromTransport"));
			stmt.setInt(1, transportId);
			ResultSet rs = stmt.executeQuery();
			if(rs.first())
				return rs.getInt("location_ID");
			//nothing found
			return -1;
		}
		finally
		{
			connection.close();
		}
	}

	/**
	 * Writes the current transport number in the tmp table so that it can be reused
	 * @param transportNr the transport number
	 * @param locationId the location id
	 * @return if the archive was successfull
	 */
	private boolean archiveTransportNumber(int transportNr, int locationId) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			//adds the old transportnumber to the tmp table
			final PreparedStatement query3 = connection.prepareStatement(queries.getStatment("add.tmpTransport"));
			query3.setInt(1, transportNr);
			query3.setInt(2, locationId);
			//assert the archive was successfully
			if(query3.executeUpdate() == 0)
				return false;
			return true;
		}
		finally
		{
			connection.close();
		}
	}

	/**
	 * Assigns a new transport state to an existing transport
	 * @param transport
	 * @return boolean if insert was successful or not
	 */
	private boolean assignTransportstate(Transport transport) throws SQLException
	{
		Connection connection = source.getConnection();
		//remove all curren states to set them new
		if(!removeTransportstate(transport))
			return false;
		try
		{
			for(Entry<Integer, Long> entry:transport.getStatusMessages().entrySet())
			{
				int state = entry.getKey();
				long time = entry.getValue();

				//transportstate, transport_ID, date
				final PreparedStatement query = connection.prepareStatement(queries.getStatment("add.transportstate"));
				query.setInt(1, state);
				query.setInt(2, transport.getTransportId());
				query.setString(3, MyUtils.timestampToString(time, MyUtils.sqlDateTime));
				if(query.executeUpdate() == 0)
					return false;
			}
			return true;
		}
		finally
		{
			connection.close();
		}
	}

	/**
	 * Clears the transportstate table.
	 * @param transport the transport to clear the state table
	 * @return boolean if remove was successful or not
	 */
	private boolean removeTransportstate(Transport transport) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement query1 = connection.prepareStatement(queries.getStatment("remove.transportstate"));
			query1.setInt(1, transport.getTransportId());
			//remove the transport state
			query1.executeUpdate();
			return true;
		}
		finally
		{
			connection.close();
		}
	}

	/**
	 * first removes all old transport items and sets all to the new value in the database
	 * @param transport
	 * @return true if update was ok
	 */
	private boolean assignTransportItems(Transport transport) throws SQLException
	{
		//removes all selected transports for specific transportId
		if(!removeTransportItems(transport))
		{
			System.out.println("Failed to clear the selected transports");
			return false;
		}

		if(transport.isEmergencyDoctorAlarming() == true)
			addTransportItem(transport.getTransportId(), 1);
		if(transport.isPoliceAlarming() == true)
			addTransportItem(transport.getTransportId(), 2);
		if(transport.isFirebrigadeAlarming() == true)
			addTransportItem(transport.getTransportId(), 3);
		if(transport.isMountainRescueServiceAlarming() == true)
			addTransportItem(transport.getTransportId(), 4);
		if(transport.isDfAlarming() == true)
			addTransportItem(transport.getTransportId(), 5);
		if(transport.isBrkdtAlarming() == true)
			addTransportItem(transport.getTransportId(), 6);
		if(transport.isBlueLightToGoal() == true)
			addTransportItem(transport.getTransportId(), 7);
		if(transport.isHelicopterAlarming() == true)
			addTransportItem(transport.getTransportId(), 8);
		if(transport.isAssistantPerson() == true)
			addTransportItem(transport.getTransportId(), 9);
		if(transport.isBackTransport() == true)
			addTransportItem(transport.getTransportId(), 10);
		if(transport.isLongDistanceTrip() == true)
			addTransportItem(transport.getTransportId(), 11);
		if(transport.isEmergencyPhone() == true)
			addTransportItem(transport.getTransportId(), 12);

		return true;
	}	

	/**
	 * inserts a new transportstate to the database
	 * @param transportId
	 * @param selectedId
	 * @return true if insert was sucessful
	 */
	private boolean addTransportItem(int transportId, int selectedId) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("add.selectedTransportItem"));
			query.setInt(1, selectedId);
			query.setInt(2, transportId);			
			if(query.executeUpdate() == 0)
				return false;
			return true;
		}
		finally
		{
			connection.close();
		}
	}

	/**
	 * removes all selected items from a transport in the database
	 * @param transport
	 * @return true if all items were removed
	 */
	private boolean removeTransportItems(Transport transport) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement query1 = connection.prepareStatement(queries.getStatment("remove.AllSelectedTransportItems"));
			query1.setInt(1, transport.getTransportId());
			query1.executeUpdate();
			return true;
		}
		finally
		{
			connection.close();
		}
	}
}