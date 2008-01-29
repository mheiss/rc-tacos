DROP TABLE IF EXISTS competences;
CREATE Table competences(
competence_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
competence VARCHAR(30) NOT NULL,
UNIQUE competence (competence),
PRIMARY KEY(competence_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS servicetype;
CREATE Table servicetype(
servicetype_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
servicetype VARCHAR(30) NOT NULL,
note TEXT,
UNIQUE servicetype (servicetype),
PRIMARY KEY(servicetype_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS job;
CREATE Table job(
job_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
jobname VARCHAR(30) NOT NULL,
UNIQUE jobname (jobname),
PRIMARY KEY(job_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS userlogin;
CREATE Table userlogin(
username VARCHAR(30) NOT NULL,
pwd VARCHAR(250) NOT NULL,
authorization VARCHAR(20) NOT NULL,
isloggedin BINARY,
locked BINARY,
PRIMARY KEY(username)
)TYPE=InnoDB;

DROP TABLE IF EXISTS documentation;
CREATE Table documentation(
PK INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
date DATETIME NOT NULL,
note TEXT NOT NULL,
username VARCHAR(30) NOT NULL, INDEX username_ind (username),
FOREIGN KEY(username) REFERENCES userlogin(username) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY(PK)
)TYPE=InnoDB;

DROP TABLE IF EXISTS phonenumbers;
CREATE TABLE phonenumbers(
phonenumber_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
phonenumber VARCHAR(30) NOT  NULL,
UNIQUE phonenumber (phonenumber),
PRIMARY KEY(phonenumber_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS location;
CREATE Table location(
location_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
locationname VARCHAR(30) NOT NULL,
street VARCHAR(30),
streetnumber VARCHAR(10),
zipcode VARCHAR(10),
city VARCHAR(30),
phonenumber_ID INTEGER UNSIGNED NOT NULL, INDEX phonenumber_ID_ind (phonenumber_ID),
note VARCHAR(250),
UNIQUE locationname (locationname),
FOREIGN KEY(phonenumber_ID) REFERENCES phonenumbers(phonenumber_ID) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY(location_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS citys;
CREATE TABLE citys(
city_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
cityname VARCHAR(50) NOT NULL,
zipcode INTEGER,
UNIQUE zipcode (zipcode),
UNIQUE city_zip(zipcode, cityname),
PRIMARY KEY(city_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS streets;
CREATE TABLE streets(
street_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
streetname VARCHAR(50) NOT  NULL,
UNIQUE streetname (streetname),
PRIMARY KEY(street_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS staffmembers;
CREATE Table staffmembers(
staffmember_ID INTEGER UNSIGNED NOT NULL,
primaryLocation INTEGER UNSIGNED NOT NULL, INDEX primaryLocation_ind (primaryLocation),
username VARCHAR(30) NOT NULL, INDEX username_ind (username),
firstname VARCHAR(30) NOT NULL,
lastname VARCHAR(30) NOT NULL,
city VARCHAR(50),
street VARCHAR(50),
sex BINARY,
birthday DATE,
email VARCHAR(100),
FOREIGN KEY(primaryLocation) REFERENCES location(location_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(username) REFERENCES userlogin(username) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY(staffmember_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS staffmember_competence;
CREATE TABLE staffmember_competence(
staffmember_ID INTEGER UNSIGNED NOT  NULL, INDEX staffmember_ID_ind (staffmember_ID),
competence_ID INTEGER UNSIGNED NOT NULL, INDEX competence_ID_ind (competence_ID),
FOREIGN KEY(staffmember_ID) REFERENCES staffmembers(staffmember_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(competence_ID) REFERENCES competences(competence_ID) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY(staffmember_ID, competence_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS phone_staffmember;
CREATE TABLE phone_staffmember(
staffmember_ID INTEGER UNSIGNED NOT  NULL, INDEX staffmember_ID_ind (staffmember_ID),
phonenumber_ID INTEGER UNSIGNED NOT NULL, INDEX phonenumber_ID_ind (phonenumber_ID),
FOREIGN KEY(staffmember_ID) REFERENCES staffmembers(staffmember_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(phonenumber_ID) REFERENCES phonenumbers(phonenumber_ID) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY(staffmember_ID, phonenumber_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS roster;
CREATE Table roster(
roster_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
staffmember_ID INTEGER UNSIGNED NOT NULL, INDEX staffmember_ID_ind (staffmember_ID),
location_ID INTEGER UNSIGNED NOT NULL, INDEX location_ID_ind (location_ID),
servicetype_ID INTEGER UNSIGNED NOT NULL, INDEX servicetype_ID_ind (servicetype_ID),
job_ID INTEGER UNSIGNED NOT NULL, INDEX job_ID_ind (job_ID),
starttime DATETIME,
endtime DATETIME,
checkIn DATETIME,
checkOut DATETIME,
note TEXT,
standby BINARY,
UNIQUE staffmember_starttime_endtime (staffmember_ID, starttime, endtime),
FOREIGN KEY(staffmember_ID) REFERENCES staffmembers(staffmember_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(location_ID) REFERENCES location(location_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(servicetype_ID) REFERENCES servicetype(servicetype_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(job_ID) REFERENCES job(job_ID) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY(roster_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS vehicletype;
CREATE Table vehicletype(
vehicletype_ID VARCHAR(30) NOT NULL,
note TEXT,
PRIMARY KEY(vehicletype_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS vehicles;
CREATE Table vehicles(
vehicle_ID VARCHAR(10) NOT NULL,
primaryLocation INTEGER UNSIGNED NOT NULL, INDEX primaryLocation_ind (primaryLocation),
vehicletype_ID VARCHAR(30) NOT NULL, INDEX vehicletype_ID_ind (vehicletype_ID),
phonenumber_ID INTEGER UNSIGNED NOT NULL, INDEX phonenumber_ID_ind (phonenumber_ID),
note TEXT,
FOREIGN KEY(primaryLocation) REFERENCES location(location_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(vehicletype_ID) REFERENCES vehicletype(vehicletype_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(phonenumber_ID) REFERENCES phonenumbers(phonenumber_ID) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY(vehicle_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS vehiclestate;
CREATE Table vehiclestate(
vehiclestate_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
vehiclestate VARCHAR(30),
UNIQUE vehiclestate (vehiclestate),
PRIMARY KEY(vehiclestate_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS vehicles_vehiclestate;
CREATE Table vehicles_vehiclestate(
vehiclestate_ID INTEGER UNSIGNED NOT NULL, INDEX vehiclestate_ID_ind (vehiclestate_ID),
vehicle_ID VARCHAR(10) NOT NULL, INDEX vehicle_ID_ind (vehicle_ID),
date DATETIME NOT NULL, INDEX date_ind (date),
FOREIGN KEY(vehicle_ID) REFERENCES vehicles(vehicle_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(vehiclestate_ID) REFERENCES vehiclestate(vehiclestate_ID) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY(vehicle_ID, vehiclestate_ID, date)
)TYPE=InnoDB;

DROP TABLE IF EXISTS staffmember_vehicle;
CREATE Table staffmember_vehicle(
staffmember_vehicle_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT, INDEX staffmember_vehicle_ID_ind (staffmember_vehicle_ID),
vehicle_ID VARCHAR(10) NOT NULL, INDEX vehicle_ID_ind (vehicle_ID),
driver_ID INTEGER UNSIGNED NOT NULL, INDEX driver_ID_ind (driver_ID),
medic1_ID INTEGER UNSIGNED NOT NULL, INDEX medic1_ID_ind (medic1_ID),
medic2_ID INTEGER UNSIGNED NOT NULL, INDEX medic2_ID_ind (medic2_ID),
currentLocation INTEGER UNSIGNED NOT NULL, INDEX currentLocation_ind (currentLocation),
FOREIGN KEY(vehicle_ID) REFERENCES vehicles(vehicle_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(driver_ID) REFERENCES staffmembers(staffmember_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(medic1_ID) REFERENCES staffmembers(staffmember_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(medic2_ID) REFERENCES staffmembers(staffmember_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(currentLocation) REFERENCES location(location_ID) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY(staffmember_vehicle_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS disease;
CREATE Table disease(
disease_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
disease VARCHAR(30) NOT NULL,
UNIQUE disease (disease),
PRIMARY KEY(disease_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS directions;
CREATE Table directions(
direction_ID INTEGER UNSIGNED NOT NULL,
direction VARCHAR(30) NOT NULL,
UNIQUE direction (direction),
PRIMARY KEY(direction_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS notyfied;
CREATE Table notyfied(
notyfied_ID INTEGER UNSIGNED NOT NULL,
name VARCHAR(30),
UNIQUE name (name),
PRIMARY KEY(notyfied_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS caller;
CREATE Table caller(
caller_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
callername VARCHAR(50),
caller_phonenumber VARCHAR(30),
UNIQUE callername_callerphone (callername, caller_phonenumber),
PRIMARY KEY(caller_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS transporttype;
CREATE Table transporttype(
transporttype_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
transporttype VARCHAR(30),
UNIQUE transporttype (transporttype),
PRIMARY KEY(transporttype_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS priority;
CREATE Table priority(
priority_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
priorityname VARCHAR(30),
UNIQUE priorityname (priorityname),
PRIMARY KEY(priority_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS transports;
CREATE Table transports(
transport_ID VARCHAR(50) NOT NULL,
staffmember_vehicle_ID INTEGER UNSIGNED NOT NULL, INDEX staffmember_vehicle_ID_ind (staffmember_vehicle_ID),
direction_ID INTEGER UNSIGNED NOT NULL, INDEX direction_ID_ind (direction_ID),
caller_ID INTEGER UNSIGNED NOT NULL, INDEX caller_ID_ind (caller_ID),
disease VARCHAR(30),
createdBy_user VARCHAR(30) NOT NULL, INDEX createdBy_user_ind (createdBy_user),
creationDate DATETIME,
note TEXT,
priority_ID INTEGER UNSIGNED NOT NULL, INDEX priority_ID_ind (priority_ID),
feedback TEXT,
departure DATETIME,
appointment DATETIME,
appointmentPatient DATETIME,
return_transport BINARY,
assistant BINARY,
transporttype_ID INTEGER UNSIGNED NOT NULL, INDEX transporttype_ID_ind (transporttype_ID),
transportstate INTEGER,
firstname VARCHAR(30),
lastname VARCHAR(30),
planned_location INTEGER UNSIGNED NOT NULL, INDEX planned_location_ind(planned_location),
from_street VARCHAR(50),
from_city VARCHAR(50),
to_street VARCHAR(50),
to_city VARCHAR(50),
FOREIGN KEY(planned_location) REFERENCES location(location_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(staffmember_vehicle_ID) REFERENCES staffmember_vehicle(staffmember_vehicle_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(direction_ID) REFERENCES directions(direction_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(caller_ID) REFERENCES caller(caller_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(priority_ID) REFERENCES priority(priority_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(createdBy_user) REFERENCES userlogin(username) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(transporttype_ID) REFERENCES transporttype(transporttype_ID) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY(transport_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS transport_notyfied;
CREATE Table transport_notyfied(
transport_ID VARCHAR(50) NOT NULL, INDEX transport_ID_ind (transport_ID),
notyfied_ID INTEGER UNSIGNED NOT NULL, INDEX notyfied_ID_ind (notyfied_ID),
FOREIGN KEY(transport_ID) REFERENCES transports(transport_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(notyfied_ID) REFERENCES notyfied(notyfied_ID) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY(transport_ID, notyfied_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS Locked_Data;
CREATE Table Locked_Data(
tablename VARCHAR(30) NOT NULL,
PK VARCHAR(50) NOT NULL,
username VARCHAR(30) NOT NULL,
PRIMARY KEY(tablename, PK)
)TYPE=InnoDB;

DROP TABLE IF EXISTS daily_information;
CREATE Table daily_information(
PK INTEGER UNSIGNED NOT NULL,
date DATETIME NOT NULL,
note TEXT NOT NULL,
PRIMARY KEY(PK)
)TYPE=InnoDB;

DROP TABLE IF EXISTS dialysis;
CREATE Table dialysis(
PK INTEGER UNSIGNED NOT NULL,
MO BINARY,
DI BINARY,
MI BINARY,
DO BINARY,
FR BINARY,
SA BINARY,
SO BINARY,
appointment DATETIME,
departure DATETIME,
appointmentPatient DATETIME,
departure_back DATETIME,
appointment_back DATETIME,
insurance VARCHAR(30),
stationary BINARY,
cancled BINARY,
firstname VARCHAR(30),
lastname VARCHAR(30),
from_street VARCHAR(50),
from_city VARCHAR(50),
to_street VARCHAR(50),
to_city VARCHAR(50),
PRIMARY KEY(PK)
)TYPE=InnoDB;

-- LOGGING
DROP TABLE IF EXISTS LOG_transportstates;
CREATE Table LOG_transportstates(
LOG_transportstate_ID INTEGER UNSIGNED NOT NULL,
transport_ID VARCHAR(50),
transportstate INTEGER,
date DATETIME,
PRIMARY KEY(LOG_transportstate_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS LOG_changes;
CREATE Table LOG_changes(
LOG_changes_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
username VARCHAR(30) NOT NULL,
discription VARCHAR(250) NOT NULL,
PRIMARY KEY(LOG_changes_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS LOG_transports;
CREATE Table LOG_transports(
LOG_transport_ID INTEGER UNSIGNED NOT NULL,
transport_ID VARCHAR(50),
createdBy_user VARCHAR(30),
direction VARCHAR(30),
disease VARCHAR(30),
note TEXT,
return_transport BINARY,
assistant BINARY,
notified_list TEXT,
caller_name VARCHAR(50),
caller_phonenumber VARCHAR(30),
patient_name VARCHAR(70),
patient_from_address VARCHAR(100),
patient_to_address VARCHAR(100),
transporttype VARCHAR(30),
priorityname INTEGER,
feedback TEXT,
creationDate DATETIME,
departure DATETIME,
appointment DATETIME,
appointmentPatient DATETIME,
PRIMARY KEY(LOG_transport_ID)
)TYPE=InnoDB;