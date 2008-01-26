package at.rc.tacos.core.db.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.dao.VehicleDAO;
import at.rc.tacos.model.Competence;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.VehicleDetail;
import at.rc.tacos.util.MyUtils;

public class VehicleDetailDAOMySQL implements VehicleDAO
{
	public static final String QUERIES_BUNDLE_PATH = "at.rc.tacos.core.db.queries";

	@Override
	public boolean addVehicle(VehicleDetail vehicle)
	{
		try
		{	
			//vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("insert.vehicle"));
			query.setString(1, vehicle.getVehicleName());
			query.setInt(2, vehicle.getDriver().getStaffMemberId());
			query.setInt(3, vehicle.getFirstParamedic().getStaffMemberId());
			query.setInt(4, vehicle.getSecondParamedic().getStaffMemberId());
			query.setInt(5, vehicle.getMobilePhone().getId());
			query.setString(6, vehicle.getVehicleType());
			query.setInt(7, vehicle.getCurrentStation().getId());
			query.setInt(8, vehicle.getBasicStation().getId());
			query.setString(9, vehicle.getVehicleNotes());
			query.setBoolean(10, vehicle.isReadyForAction());
			query.setBoolean(11, vehicle.isOutOfOrder());

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
	public VehicleDetail getVehicleByName(String vehicleName)
	{
		VehicleDetail vehicle = new VehicleDetail();
		Location station = new Location();
		try
		{
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.vehicleByID"));
			query.setString(1, vehicleName);
			final ResultSet rs = query.executeQuery();

			//v.vehicle_ID, v.medic1_ID, v.medic2_ID, v.driver_ID, v.currentLocation, v.primaryLocation, lo.locationname, lo.location_ID,
			//v.vehicletype, v.readyForAction, v.outOfOrder, v.phonenumber_ID, pn.phonenumber, v.note
			rs.first();
			vehicle.setVehicleName(rs.getString("v.vehicle_ID"));
			vehicle.setVehicleType(rs.getString("v.vehicletype"));
			vehicle.setReadyForAction(rs.getBoolean("v.readyForAction"));
			vehicle.setOutOfOrder(rs.getBoolean("v.outOfOrder"));
			vehicle.setVehicleNotes(rs.getString("v.note"));

			MobilePhoneDetail phone = new MobilePhoneDetail();
			phone.setId(rs.getInt("v.phonenumber_ID"));
			phone.setMobilePhoneNumber(rs.getString("pn.phonenumber"));
			phone.setMobilePhoneName(rs.getString("pn.phonename"));
			vehicle.setMobilPhone(phone);

			{
				if(rs.getInt("v.currentLocation") == rs.getInt("lo.location_ID"))
				{
					station.setId(rs.getInt("v.currentLocation"));
					station.setLocationName(rs.getString("lo.locationname"));
					vehicle.setCurrentStation(station);
				}
				else if(rs.getInt("v.primaryLocation") == rs.getInt("lo.location_ID"))
				{
					station.setId(rs.getInt("v.primaryLocation"));
					station.setLocationName(rs.getString("lo.locationname"));
					vehicle.setBasicStation(station);
				}
			}while(rs.next());
			rs.first();

			int i=1;
			for(i=1; i>3; i++)
			{
				int staffID = 0;
				if(i==1)
					staffID = rs.getInt("v.medic1_ID");
				else if(i==2)
					staffID = rs.getInt("v.medic2_ID");
				else if(i==3)
					staffID = rs.getInt("v.driver_ID");

				StaffMember staff = new StaffMember();

				List<Competence> competences = new ArrayList<Competence>();
				try
				{
					//u.username, e.primaryLocation, lo.locationname, e.staffmember_ID, e.firstname, e.lastname, e.sex, e.birthday, e.email,
					//*u.authorization, *u.isloggedin, *u.locked, e.city, e.street
					final PreparedStatement query2 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.staffmemberByID"));
					query2.setInt(1, staffID);
					final ResultSet rs2 = query.executeQuery();

					rs2.first();
					staff.setStaffMemberId(rs2.getInt("e.staffmember_ID"));

					station.setId(rs2.getInt("e.primaryLocation"));
					station.setLocationName(rs2.getString("lo.locationname"));
					staff.setPrimaryLocation(station);

					staff.setLastName(rs2.getString("e.lastname"));
					staff.setFirstName(rs2.getString("e.firstname"));
					staff.setStreetname(rs2.getString("e.street"));
					staff.setCityname(rs2.getString("e.city"));
					staff.setMale(rs2.getBoolean("e.sex"));
					staff.setBirthday(MyUtils.getTimestampFromDate(rs2.getString("e.birthday")));
					staff.setEMail(rs2.getString("e.email"));
					staff.setUserName(rs2.getString("e.username"));

					{
						Competence competence = new Competence();
						competence.setId(rs2.getInt("c.competence_ID"));
						competence.setCompetenceName(rs2.getString("c.competence"));
						competences.add(competence);
					}while(rs.next());
					staff.setCompetenceList(competences);

					//ph.phonenumber, ph.phonenumber_ID
					final PreparedStatement query3 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.PhonenumbersOfMemberID"));
					query3.setInt(1, rs2.getInt("ro.staffmember_ID"));
					final ResultSet rs3 = query3.executeQuery();

					List<MobilePhoneDetail> phoneList = new ArrayList<MobilePhoneDetail>();
					while(rs3.next())
					{
						MobilePhoneDetail phone2 = new MobilePhoneDetail();
						phone2.setId(rs3.getInt("ph.phonenumber_ID"));
						phone2.setMobilePhoneNumber(rs3.getString("ph.phonenumber"));
						phoneList.add(phone2);
					}
					staff.setPhonelist(phoneList);

					if(i==1)
						vehicle.setFirstParamedic(staff);
					else if(i==2)
						vehicle.setSecondParamedic(staff);
					else if(i==3)
						vehicle.setDriver(staff);
				}
				catch (SQLException e)
				{
					e.printStackTrace();
					return null;
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return vehicle;
	}

	@Override
	public List<VehicleDetail> listVehicles()
	{

		List<VehicleDetail> vehicles = new ArrayList<VehicleDetail>();

		
		Location station = new Location();
		try
		{
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.vehicleByID"));
			final ResultSet rs = query.executeQuery();

			//v.vehicle_ID, v.medic1_ID, v.medic2_ID, v.driver_ID, v.currentLocation, v.primaryLocation, lo.locationname, lo.location_ID,
			//v.vehicletype, v.readyForAction, v.outOfOrder, v.phonenumber_ID, pn.phonenumber, v.note
			while(rs.next())
			{
				VehicleDetail vehicle = new VehicleDetail();
				vehicle.setVehicleName(rs.getString("v.vehicle_ID"));
				vehicle.setVehicleType(rs.getString("v.vehicletype"));
				vehicle.setReadyForAction(rs.getBoolean("v.readyForAction"));
				vehicle.setOutOfOrder(rs.getBoolean("v.outOfOrder"));
				vehicle.setVehicleNotes(rs.getString("v.note"));

				MobilePhoneDetail phone = new MobilePhoneDetail();
				phone.setId(rs.getInt("v.phonenumber_ID"));
				phone.setMobilePhoneNumber(rs.getString("pn.phonenumber"));
				phone.setMobilePhoneName(rs.getString("pn.phonename"));
				vehicle.setMobilPhone(phone);

				{
					if(rs.getInt("v.currentLocation") == rs.getInt("lo.location_ID"))
					{
						station.setId(rs.getInt("v.currentLocation"));
						station.setLocationName(rs.getString("lo.locationname"));
						vehicle.setCurrentStation(station);
					}
					else if(rs.getInt("v.primaryLocation") == rs.getInt("lo.location_ID"))
					{
						station.setId(rs.getInt("v.primaryLocation"));
						station.setLocationName(rs.getString("lo.locationname"));
						vehicle.setBasicStation(station);
					}
				}while(rs.next());
				rs.first();

				int i=1;
				for(i=1; i>3; i++)
				{
					int staffID = 0;
					if(i==1)
						staffID = rs.getInt("v.medic1_ID");
					else if(i==2)
						staffID = rs.getInt("v.medic2_ID");
					else if(i==3)
						staffID = rs.getInt("v.driver_ID");

					StaffMember staff = new StaffMember();

					List<Competence> competences = new ArrayList<Competence>();
					try
					{
						//u.username, e.primaryLocation, lo.locationname, e.staffmember_ID, e.firstname, e.lastname, e.sex, e.birthday, e.email,
						//*u.authorization, *u.isloggedin, *u.locked, e.city, e.street
						final PreparedStatement query2 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.staffmemberByID"));
						query2.setInt(1, staffID);
						final ResultSet rs2 = query.executeQuery();

						rs2.first();
						staff.setStaffMemberId(rs2.getInt("e.staffmember_ID"));

						station.setId(rs2.getInt("e.primaryLocation"));
						station.setLocationName(rs2.getString("lo.locationname"));
						staff.setPrimaryLocation(station);

						staff.setLastName(rs2.getString("e.lastname"));
						staff.setFirstName(rs2.getString("e.firstname"));
						staff.setStreetname(rs2.getString("e.street"));
						staff.setCityname(rs2.getString("e.city"));
						staff.setMale(rs2.getBoolean("e.sex"));
						staff.setBirthday(MyUtils.getTimestampFromDate(rs2.getString("e.birthday")));
						staff.setEMail(rs2.getString("e.email"));
						staff.setUserName(rs2.getString("e.username"));

						{
							Competence competence = new Competence();
							competence.setId(rs2.getInt("c.competence_ID"));
							competence.setCompetenceName(rs2.getString("c.competence"));
							competences.add(competence);
						}while(rs.next());
						staff.setCompetenceList(competences);

						//ph.phonenumber, ph.phonenumber_ID
						final PreparedStatement query3 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.PhonenumbersOfMemberID"));
						query3.setInt(1, rs2.getInt("ro.staffmember_ID"));
						final ResultSet rs3 = query3.executeQuery();

						List<MobilePhoneDetail> phoneList = new ArrayList<MobilePhoneDetail>();
						while(rs3.next())
						{
							MobilePhoneDetail phone2 = new MobilePhoneDetail();
							phone2.setId(rs3.getInt("ph.phonenumber_ID"));
							phone2.setMobilePhoneNumber(rs3.getString("ph.phonenumber"));
							phoneList.add(phone2);
						}
						staff.setPhonelist(phoneList);

						if(i==1)
							vehicle.setFirstParamedic(staff);
						else if(i==2)
							vehicle.setSecondParamedic(staff);
						else if(i==3)
							vehicle.setDriver(staff);
					}
					catch (SQLException e)
					{
						e.printStackTrace();
						return null;
					}
				}
				vehicles.add(vehicle);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return vehicles;
	}

	@Override
	public boolean removeVehicle(VehicleDetail vehicle)
	{
		try
		{
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("delete.vehicle"));
			query.setString(1, vehicle.getVehicleName());

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
	public boolean updateVehicle(VehicleDetail vehicle)
	{
		try
		{
			//driver_ID = ?, medic1_ID = ?, medic2_ID = ?, phonenumber_ID = ?, vehicletype = '?', currentLocation = ?,
			//primaryLocation = ?, note = '?', readyForAction = ?, outOfOrder = ? where vehicle_ID = '?';
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("update.vehicle"));
			query.setInt(1, vehicle.getDriver().getStaffMemberId());
			query.setInt(2, vehicle.getFirstParamedic().getStaffMemberId());
			query.setInt(3, vehicle.getSecondParamedic().getStaffMemberId());
			query.setInt(4, vehicle.getMobilePhone().getId());
			query.setString(5, vehicle.getVehicleType());
			query.setInt(6, vehicle.getCurrentStation().getId());
			query.setInt(7, vehicle.getBasicStation().getId());
			query.setString(8, vehicle.getVehicleNotes());
			query.setBoolean(9, vehicle.isReadyForAction());
			query.setBoolean(10, vehicle.isOutOfOrder());
			query.setString(11, vehicle.getVehicleName());

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
