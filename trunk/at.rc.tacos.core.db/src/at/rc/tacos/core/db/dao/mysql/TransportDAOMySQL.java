package at.rc.tacos.core.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
	
	
	public static final String QUERIES_BUNDLE_PATH = "at.rc.tacos.core.db.queries";
	private final LocationDAO locationDAO = DaoFactory.MYSQL.createLocationDAO();
	private final StaffMemberDAO staffMemberDAO = DaoFactory.MYSQL.createStaffMemberDAO();
	private final CallerDAO callerDAO = DaoFactory.MYSQL.createNotifierDAO();

	@Override
	public int addTransport(Transport transport)
	{
		CallerDetail caller = new CallerDetail();
		try
		{	
			int callerId = 0;
			if(transport.getCallerDetail() != null)
			{
				callerId = callerDAO.addCaller(transport.getCallerDetail());
				if(callerId == -1)
					return -1;
				caller = transport.getCallerDetail();
				caller.setCallerId(callerId);
				transport.setCallerDetail(caller);
			}

			//transport_ID, transportNr, direction, caller_ID, note, createdBy_user, priority, feedback, creationDate,
			//departure, appointment, appointmentPatient, transporttype, disease, firstname, lastname, planned_location,
			//from_street, from_city, to_street, to_city, programstate, dateOfTransport
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(TransportDAOMySQL.QUERIES_BUNDLE_PATH).getString("insert.transport"));
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


			// t.creationDate, t.firstname, t.lastname, t.to_street, t.appointment = ?;
			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.transportID"));
			query1.setString(1, transport.getCreatedByUsername());
			query1.setString(2, MyUtils.timestampToString(transport.getCreationTime(), MyUtils.sqlDateTime));
			query1.setString(3, MyUtils.timestampToString(transport.getDateOfTransport(), MyUtils.sqlDateTime));
			System.out.println(query1);
			final ResultSet rsId = query1.executeQuery();

			if(!rsId.first())
				return -3;
			int transportId = rsId.getInt(1);
			transport.setTransportId(transportId);
			//assign the transport items!
			assignTransportItems(transport);
			return transport.getTransportId();

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return -2;
		}

	}

	@Override
	public List<Transport> listTransportsByDateOfTransport(long startdate, long enddate)
	{
		System.out.println("listTransportsByDateOfTransport: start: "+MyUtils.timestampToString(startdate, MyUtils.sqlDateTime)+" end: "+MyUtils.timestampToString(enddate, MyUtils.sqlDateTime));
		List<Transport> transports = new ArrayList<Transport>();
		try
		{
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.transportsByDateOfTransport"));
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

				if(rs.getInt("t.planned_location") != 0)
				{
					Location station = new Location();
					station.setId(rs.getInt("t.planned_location"));
					station.setLocationName(rs.getString("lo.locationname"));
					transport.setPlanedLocation(station);
					System.out.println("TransportDAOMySQL, listTransports...., location: " +station.getLocationName());
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

				if(rs.getString("av.locationname") != null)
				{
					VehicleDetail vehicle = new VehicleDetail();
					Location currentStation = new Location();
					currentStation = locationDAO.getLocationByName(rs.getString("av.locationname"));
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
				System.out.println("transport: "+transport);
				transports.add(transport);
			}
			return transports;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
		update von am transport -> 
		wenn der transport kein vehicle hat dann werden einfach alle felder aktualisert
		wenn der transport ein vehicle hat aber keine transportnummer -> transportnummer generieren und transport updaten
		wenn der transport ein vehicle und a transportnummer hat dann kann ma auch einfach updaten

	 */
	@Override
	public boolean updateTransport(Transport transport)
	{
		try
		{
			// wenn der transport in der db schon ein vehicle hat aber keine transportnummer, soll eine transportnummer 
			//generiert werden und das vehicle auch in der db zugewiesen werden   ------------------------------------------------------
			if(transport.getVehicleDetail() != null && transport.getTransportNumber() == 0 )
			{
				int number = this.assignVehicleToTransportAndGenerateTransportNumber(transport);
				transport.setTransportNumber(number);
			}
			
			//always--------------------------------------------------------------------
			/** add or update the caller of the transport*/
			int callerId;
			CallerDetail caller = new CallerDetail();
			caller = transport.getCallerDetail();
			if(transport.getCallerDetail() != null)
			{
				if(transport.getCallerDetail().getCallerId() == -1)
				{
					//add new Caller
					callerId = callerDAO.addCaller(transport.getCallerDetail());
					caller.setCallerId(callerId);
					transport.setCallerDetail(caller);
				}
				else
					callerDAO.updateCaller(transport.getCallerDetail());
			}
				
			/** execute the update query for the transport*/
			this.executeTheTransportUpdateQuery(transport);

			/** execute the queries for the booleans*/
			this.assignTransportItems(transport);
			
			//TODO:
			/** update the status messages*/
			//--------------------------------------------------------------------------
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

	/**
	 * searches through the tmp table for existing transportnumbers or generates a new one
	 * @param locationname
	 * @return locationnumber
	 */
	public int getNewTransportNrForLocation(String locationname)
	{
		try
		{
			//search through the tmp table whether tmp transportNumber exists or not
			final PreparedStatement query4 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.tmpTransportNrFromLocation"));
			query4.setString(1, locationname);
			final ResultSet rs4 = query4.executeQuery();
			if(rs4.first())
				return rs4.getInt("transportNr");
			else
			{
				//calculate new transport number from transports
				final PreparedStatement query5 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.MaxTransportNr"));
				query5.setString(1, locationname);
				final ResultSet rs5 = query5.executeQuery();
				if(rs5.first())
				{
					return rs5.getInt("max(t.transportNr)") +1;
				}
				return -1;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
	}

	private boolean archiveTransportNumber(int transportNr, String locationname)
	{
		try
		{
			//adds the old transportnumber to the tmp table
			final PreparedStatement query3 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("add.tmpTransport"));
			query3.setInt(1, transportNr);
			query3.setString(2, locationname);
			query3.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public int assignVehicleToTransportAndGenerateTransportNumber(Transport transport)
	{
		try
		{
//			//select vehicle from transport
//			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.vehicleFromTransport"));
//			query1.setInt(1, transport.getTransportId());
//			final ResultSet rs1 = query1.executeQuery();
//
//			/**
//			 * checks if a vehicle already exists?  -- situation doesn't exist
//			 */
//			if(rs1.next())
//			{
//				/**
//				 * checks if old and new vehicle are from the same location?
//				 */
//				if(transport.getVehicleDetail().getCurrentStation().getLocationName() == rs1.getString("locationname"))
//				{
//					/**
//					 * Updates the existing Vehicle without changing the Transportnumber
//					 */
//					//vehicle_ID = ?, medic2_ID = ?, medic1_ID = ?, driver_ID = ?, locationname = ?, note = ?, vehicletype = ? WHERE transport_ID = ?;
//					final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("update.assignedVehicle"));
//					if(transport.getVehicleDetail() == null)
//						query.setString(1, null);
//					else
//						query.setString(1, transport.getVehicleDetail().getVehicleName());
//					query.setInt(2, transport.getVehicleDetail().getSecondParamedic().getStaffMemberId());
//					query.setInt(3, transport.getVehicleDetail().getFirstParamedic().getStaffMemberId());
//					query.setInt(4, transport.getVehicleDetail().getDriver().getStaffMemberId());
//					query.setString(5, transport.getVehicleDetail().getCurrentStation().getLocationName());
//					query.setString(6, transport.getVehicleDetail().getVehicleNotes());
//					query.setString(7, transport.getVehicleDetail().getVehicleType());
//					query.setInt(8, transport.getTransportId());
//					query.executeUpdate();
//					return transport.getTransportNumber();
//				}
//				else
//				{
//					/**
//					 * Updates the existing Vehicle but has to generate a new transportNumber
//					 */
//					//select the old transport Number
//					final PreparedStatement query2 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.transportNr"));
//					query2.setInt(1, transport.getTransportId());
//					final ResultSet rs2 = query2.executeQuery();
//					if(rs2.first())
//					{
//						//archive transportnumber
//						boolean result = archiveTransportNumber(rs2.getInt("transportNr"), transport.getVehicleDetail().getCurrentStation().getLocationName());
//						if(result == false)
//							return -1;
//
//						int transportNr = getNewTransportNrForLocation(transport.getVehicleDetail().getCurrentStation().getLocationName());
//						if(transportNr == -1)
//							return -1;
//
//						//insert new transportnumber to existing transport
//						final PreparedStatement query6 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("update.transportNr"));
//						query6.setInt(1, transportNr);
//						query6.setInt(2, transport.getTransportId());
//						query6.executeUpdate();
//						return transportNr;
//					}
//				}
//				return -1;
//			}
			//----> ab hier relevant
//			else
//			{
				/**
				 * add new Vehicle to Transport
				 */
				//transport_ID, vehicle_ID, medic2_ID, medic1_ID, driver_ID, locationname, note, vehicletype
				final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("add.assignedVehicle"));
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
					query.setString(6, transport.getVehicleDetail().getCurrentStation().getLocationName());
				else
					query.setString(6, null);
				query.setString(7, transport.getVehicleDetail().getVehicleNotes());
				query.setString(8, transport.getVehicleDetail().getVehicleType());
				query.executeUpdate();
				
				/** create a transport number*/
				int transportNr=0;
				if(transport.getVehicleDetail().getCurrentStation() != null)
				{
					transportNr = getNewTransportNrForLocation(transport.getVehicleDetail().getCurrentStation().getLocationName());
					//insert new transport number to existing transport
					final PreparedStatement query6 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("update.transportNr"));
					query6.setInt(1, transportNr);
					query6.setInt(2, transport.getTransportId());
					query6.executeUpdate();
				}
				return transportNr;
//			}
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
			int oldNumber = transport.getTransportNumber();
			int transportId = transport.getTransportId();
			String location = transport.getVehicleDetail().getCurrentStation().getLocationName();
		
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("remove.assignedVehicle"));
			query.setInt(1, transportId);
			query.executeUpdate();
			
			final PreparedStatement query2 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("update.transportNr"));
			query2.setInt(1, 0);
			query2.setInt(2, transportId);
			query2.executeUpdate();
			
			/**archive the old transport number */
			this.archiveTransportNumber(oldNumber, location);
			
			
			
			//TODO
			//write old transport number and current location (AND YEAR) into tmpdb
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}	
		return true;
	}

	
	@Override
	public boolean cancelTransport(Transport transport) throws SQLException
	{
		Connection connection = source.getConnection();
		int transportId = transport.getTransportId();
		try
		{
			/** archive the old transport number if there is one*/
			if(transport.getTransportNumber() != 0)
			{
				boolean check = true;
				check = this.archiveTransportNumber(transport.getTransportNumber(), transport.getVehicleDetail().getBasicStation().getLocationName());
				return check;
			}
			
			/**set the transport number to 'STORNO' (-1)*/
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("update.TransportNr"));
			query.setInt(1, -1);//'-1' for STORNO
			query.setInt(2, transportId);
			query.executeUpdate();
			if(query.executeUpdate() == 0)
				return false;
				
					
			/** set the program status to 'JOURNAL'*/
			transport.setProgramStatus(IProgramStatus.PROGRAM_STATUS_JOURNAL);
			boolean check = true;
			check = this.executeTheTransportUpdateQuery(transport);
				return check;		
		}
		finally
		{
			connection.close();
		}
	}
	

	@Override
	public List<Transport> getTransportsFromVehicle(String vehicleName)
	{
		// TODO ???
		return null;
	}

	@Override
	public List<Transport> listArchivedTransports(long startdate, long enddate)
	{
		List<Transport> transports = null;
		try
		{
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.archivedTransports"));
			query.setString(1, MyUtils.timestampToString(startdate, MyUtils.sqlDateTime));
			query.setString(2, MyUtils.timestampToString(enddate, MyUtils.sqlDateTime));
			query.setInt(3, PROGRAM_STATUS_JOURNAL);
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

	/**
	 * inserts a new transportstate to the database
	 * @param transportId
	 * @param selectedId
	 * @return true if insert was sucessful
	 */
	private boolean addTransportItem(int transportId, int selectedId)
	{
		try
		{
			System.out.println("addTransportItem");
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("add.selectedTransportItem"));
			System.out.println("selectedId: "+selectedId);
			query.setInt(1, selectedId);
			System.out.println("transportId: "+transportId);
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

	/**
	 * first removes all old transport items and sets all to the new value in the database
	 * @param transport
	 * @return true if update was ok
	 */
	private boolean assignTransportItems(Transport transport)
	{
		//removes all selected transports for specific transportId
		removeTransportItems(transport);

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
	 * removes all selected items from a transport in the database
	 * @param transport
	 * @return true if all items were removed
	 */
	private boolean removeTransportItems(Transport transport)
	{
		try
		{
			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("remove.AllSelectedTransportItems"));
			query1.setInt(1, transport.getTransportId());
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
	   	public boolean assignTransportstate(Transport transport)
	   	{
	   		removeTransportstate(transport);
	   		try
	   		{
	   			for(Entry<Integer, Long> entry:transport.getStatusMessages().entrySet())
	   			{
	   				int state = entry.getKey();
	   				long time = entry.getValue();
	   
	   				//transportstate, transport_ID, date
	   				final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("add.transportstate"));
	   				query.setInt(1, state);
	   				query.setInt(2, transport.getTransportId());
	   				query.setString(3, MyUtils.timestampToString(time, MyUtils.sqlDateTime));
	   				query.executeUpdate();
	   			}
	   		}
	   		catch (SQLException e)
	   		{
	   			e.printStackTrace();
	   			return false;
	   		}
	   		return true;
	   	}
	   
	   	@Override
	   	public boolean removeTransportstate(Transport transport)
	   	{
	   		try
	   		{
	   			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("remove.transportstate"));
	   			query1.setInt(1, transport.getTransportId());
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
	   		//boolean result = assignTransportstate(transport);
	   		return false;
	   	}

	public Transport getTransportById(int id)
	{
		System.out.println("TransportDAOMySQL, getTransportById - plannedLocation");
		Transport transport = new Transport();
		try
		{
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.transportById"));
			query.setInt(1, id);
			final ResultSet rs = query.executeQuery();

			if(rs.first())
			{
				//t.transport_ID, t.transportNr, t.planned_location, lo.locationname, lo.location_ID, t.caller_ID, ca.callername,
				//ca.caller_phonenumber, t.createdBy_user, t.note, t.feedback, t.creationDate, t.departure, t.appointment,
				//t.appointmentPatient, t.disease, t.firstname, t.lastname, t.from_street, t.from_city, t.to_street, t.to_city,
				//t.programstate, t.transporttype, t.priority, t.direction, t.dateOfTransport, av.vehicle_ID, av.driver_ID,
				//av.medic1_ID, av.medic2_ID, av.locationname, av.note, av.vehicletype

				transport.setTransportId(rs.getInt("t.transport_ID"));
				transport.setTransportNumber(rs.getInt("t.transportNr"));

				System.out.println("plannedLocation ID: "+rs.getInt("t.planned_location"));
				System.out.println("plannedLocation name: "+rs.getString("lo.locationname"));
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

				if(rs.getString("av.locationname") != null)
				{
					VehicleDetail vehicle = new VehicleDetail();
					Location currentStation = new Location();
					currentStation = locationDAO.getLocationByName(rs.getString("av.locationname"));
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

				// find the selected items (boolean values)
				final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.selectedTransportItems"));
				query1.setInt(1, transport.getTransportId());
				final ResultSet rs1 = query1.executeQuery();
				
				transport.setEmergencyDoctorAlarming(false);
				transport.setPoliceAlarming(false);
				transport.setFirebrigadeAlarming(false);
				transport.setMountainRescueServiceAlarming(false);
				transport.setDfAlarming(false);
				transport.setBrkdtAlarming(false);
				transport.setBlueLightToGoal(false);
				transport.setHelicopterAlarming(false);
				transport.setAssistantPerson(false);
				transport.setBackTransport(false);
				transport.setLongDistanceTrip(false);
				transport.setEmergencyPhone(false);
				while(rs1.next())
				{
					System.out.println("TransportDAOMySQL, getTransportsByID, selected_ID: " +rs1.getInt("selected_ID"));
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
			}
			return transport;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	

	private boolean executeTheTransportUpdateQuery(Transport transport) throws SQLException
	{
			// direction = ?, caller_ID = ?, note = ?, createdBy_user = ?, priority = ?, feedback = ?, creationDate = ?, 
			//departure = ?, appointment = ?, appointmentPatient = ?, transporttype = ?, disease = ?, firstname = ?, lastname = ?, 
			//planned_location = ?, from_street = ?, from_city = ?, to_street = ?, to_city = ?, programstate = ?, dateOfTransport = ?, transport_ID = ?;
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("update.transport"));
			
			/** set the values for the ?*/
			query.setInt(1, transport.getDirection());
			if(transport.getCallerDetail() == null)
				query.setInt(2, 0);
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
			/** execute the query*/
			query.executeUpdate();
			
			if(query.executeUpdate() == 0)
				return false;
			return true;
			
		
		
		


	}
}
