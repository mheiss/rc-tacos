DROP TABLE IF EXISTS competences;
CREATE Table competences(
competence_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
competence VARCHAR(30),
PRIMARY KEY(competence_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS servicetype;
CREATE Table servicetype(
servicetype_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
servicetype VARCHAR(30) NOT NULL,
note TEXT,
PRIMARY KEY(servicetype_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS job;
CREATE Table job(
job_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
jobname VARCHAR(30) NOT NULL,
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

DROP TABLE IF EXISTS phonenumbers;
CREATE TABLE phonenumbers(
phonenumber_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
phonenumber VARCHAR(30) NOT  NULL,
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
FOREIGN KEY(phonenumber_ID) REFERENCES phonenumbers(phonenumber_ID) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY(location_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS citys;
CREATE TABLE citys(
city_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
cityname VARCHAR(30) NOT NULL,
zipcode INTEGER NOT  NULL,
PRIMARY KEY(city_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS streets;
CREATE TABLE streets(
street_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
streetname VARCHAR(30) NOT  NULL,
PRIMARY KEY(street_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS city_street;
CREATE TABLE city_street(
city_street_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
city_ID INTEGER UNSIGNED NOT  NULL, INDEX city_ID_ind (city_ID),
street_ID INTEGER UNSIGNED NOT NULL, INDEX street_ID_ind (street_ID),
FOREIGN KEY(city_ID) REFERENCES citys(city_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(street_ID) REFERENCES streets(street_ID) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY(city_street_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS streetnumbers;
CREATE TABLE streetnumbers(
streetnumber_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
streetnumber VARCHAR(10) NOT  NULL,
PRIMARY KEY(streetnumber_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS street_streetnumber;
CREATE TABLE street_streetnumber(
street_streetnumber_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
street_ID INTEGER UNSIGNED NOT  NULL, INDEX street_ID_ind (street_ID),
streetnumber_ID INTEGER UNSIGNED NOT NULL, INDEX streetnumber_ID_ind (streetnumber_ID),
FOREIGN KEY(street_ID) REFERENCES streets(street_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(streetnumber_ID) REFERENCES streetnumbers(streetnumber_ID) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY(street_streetnumber_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS employees;
CREATE Table employees(
employee_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
primaryLocation INTEGER UNSIGNED NOT NULL, INDEX primaryLocation_ind (primaryLocation),
username VARCHAR(30) NOT NULL, INDEX username_ind (username),
firstname VARCHAR(30) NOT NULL,
lastname VARCHAR(30) NOT NULL,
sex BINARY,
birthday DATETIME,
city_street_ID INTEGER UNSIGNED NOT NULL, INDEX city_street_ID_ind (city_street_ID),
street_streetnumber_ID INTEGER UNSIGNED NOT NULL, INDEX street_streetnumber_ID_ind (street_streetnumber_ID),
email VARCHAR(50),
FOREIGN KEY(primaryLocation) REFERENCES location(location_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(username) REFERENCES userlogin(username) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(city_street_ID) REFERENCES city_street(city_street_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(street_streetnumber_ID) REFERENCES street_streetnumber(street_streetnumber_ID) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY(employee_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS employee_competence;
CREATE TABLE employee_competence(
employee_ID INTEGER UNSIGNED NOT  NULL, INDEX employee_ID_ind (employee_ID),
competence_ID INTEGER UNSIGNED NOT NULL, INDEX competence_ID_ind (competence_ID),
FOREIGN KEY(employee_ID) REFERENCES employees(employee_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(competence_ID) REFERENCES competences(competence_ID) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY(employee_ID, competence_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS phone_employee;
CREATE TABLE phone_employee(
employee_ID INTEGER UNSIGNED NOT  NULL, INDEX employee_ID_ind (employee_ID),
phonenumber_ID INTEGER UNSIGNED NOT NULL, INDEX phonenumber_ID_ind (phonenumber_ID),
FOREIGN KEY(employee_ID) REFERENCES employees(employee_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(phonenumber_ID) REFERENCES phonenumbers(phonenumber_ID) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY(employee_ID, phonenumber_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS roster;
CREATE Table roster(
roster_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
employee_ID INTEGER UNSIGNED NOT NULL, INDEX employee_ID_ind (employee_ID),
location_ID INTEGER UNSIGNED NOT NULL, INDEX location_ID_ind (location_ID),
servicetype_ID INTEGER UNSIGNED NOT NULL, INDEX servicetype_ID_ind (servicetype_ID),
job_ID INTEGER UNSIGNED NOT NULL, INDEX job_ID_ind (job_ID),
start DATETIME,
end DATETIME,
checkIn DATETIME,
checkOut DATETIME,
note TEXT,
FOREIGN KEY(employee_ID) REFERENCES employees(employee_ID) ON DELETE CASCADE ON UPDATE CASCADE,
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
maxSeats INTEGER,
mannedSeats INTEGER,
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

DROP TABLE IF EXISTS employee_vehicle;
CREATE Table employee_vehicle(
employee_vehicle_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT, INDEX employee_vehicle_ID_ind (employee_vehicle_ID),
vehicle_ID VARCHAR(10) NOT NULL, INDEX vehicle_ID_ind (vehicle_ID),
driver INTEGER UNSIGNED NOT NULL, INDEX driver_ind (driver),
medic1 INTEGER UNSIGNED NOT NULL, INDEX medic1_ind (medic1),
medic2 INTEGER UNSIGNED NOT NULL, INDEX medic2_ind (medic2),
currentLocation INTEGER UNSIGNED NOT NULL, INDEX currentLocation_ind (currentLocation),
note TEXT,
FOREIGN KEY(vehicle_ID) REFERENCES vehicles(vehicle_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(driver) REFERENCES employees(employee_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(medic1) REFERENCES employees(employee_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(medic2) REFERENCES employees(employee_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(currentLocation) REFERENCES location(location_ID) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY(employee_vehicle_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS patients;
CREATE Table patients(
patient_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
firstname VARCHAR(30) NOT NULL,
lastname VARCHAR(30) NOT NULL,
sex BINARY,
birthday DATETIME,
from_city_street_ID INTEGER UNSIGNED NOT NULL, INDEX from_city_street_ID_ind (from_city_street_ID),
from_street_streetnumber_ID INTEGER UNSIGNED NOT NULL, INDEX from_street_streetnumber_ID_ind (from_street_streetnumber_ID),
to_city_street_ID INTEGER UNSIGNED NOT NULL, INDEX to_city_street_ID_ind (to_city_street_ID),
to_street_streetnumber_ID INTEGER UNSIGNED NOT NULL, INDEX to_street_streetnumber_ID_ind (to_street_streetnumber_ID),
FOREIGN KEY(from_city_street_ID) REFERENCES city_street(city_street_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(from_street_streetnumber_ID) REFERENCES street_streetnumber(street_streetnumber_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(to_city_street_ID) REFERENCES city_street(city_street_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(to_street_streetnumber_ID) REFERENCES street_streetnumber(street_streetnumber_ID) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY(patient_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS disease;
CREATE Table disease(
disease_ID INTEGER UNSIGNED NOT NULL,
disease VARCHAR(30) NOT NULL,
PRIMARY KEY(disease_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS directions;
CREATE Table directions(
direction_ID INTEGER UNSIGNED NOT NULL,
direction VARCHAR(30) NOT NULL,
PRIMARY KEY(direction_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS notyfied;
CREATE Table notyfied(
notyfied_ID INTEGER UNSIGNED NOT NULL,
name VARCHAR(30),
PRIMARY KEY(notyfied_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS caller;
CREATE Table caller(
caller_ID INTEGER UNSIGNED NOT NULL,
callername VARCHAR(50),
caller_phonenumber VARCHAR(30),
caller_note TEXT,
PRIMARY KEY(caller_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS transports;
CREATE Table transports(
transport_ID VARCHAR(50) NOT NULL,
employee_vehicle_ID INTEGER UNSIGNED NOT NULL, INDEX employee_vehicle_ID_ind (employee_vehicle_ID),
direction_ID INTEGER UNSIGNED NOT NULL, INDEX direction_ID_ind (direction_ID),
caller_ID INTEGER UNSIGNED NOT NULL, INDEX caller_ID_ind (caller_ID),
disease_ID INTEGER UNSIGNED NOT NULL, INDEX disease_ID_ind (disease_ID),
patient_ID INTEGER UNSIGNED NOT NULL, INDEX patient_ID_ind (patient_ID),
createdBy_ID INTEGER UNSIGNED NOT NULL, INDEX createdBy_ID_ind (createdBy_ID),
creationDate DATETIME,
diseasenote TEXT,
priority INTEGER,
feedback TEXT,
departure DATETIME,
appointment DATETIME,
appointmentPatient DATETIME,
ambulant_stationary BINARY,
assistant BINARY,
transporttype VARCHAR(30),
FOREIGN KEY(employee_vehicle_ID) REFERENCES employee_vehicle(employee_vehicle_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(direction_ID) REFERENCES directions(direction_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(caller_ID) REFERENCES caller(caller_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(disease_ID) REFERENCES disease(disease_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(patient_ID) REFERENCES patients(patient_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(createdBy_ID) REFERENCES employees(employee_ID) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY(transport_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS transport_notyfied;
CREATE Table transport_notyfied(
transport_ID VARCHAR(50) NOT NULL, INDEX transport_ID_ind (transport_ID),
notyfied_ID INTEGER UNSIGNED NOT NULL, INDEX notyfied_ID_ind (notyfied_ID),
date DATETIME,
FOREIGN KEY(transport_ID) REFERENCES transports(transport_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(notyfied_ID) REFERENCES notyfied(notyfied_ID) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY(transport_ID, notyfied_ID)
)TYPE=InnoDB;

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
LOG_changes_ID INTEGER UNSIGNED NOT NULL,
username VARCHAR(30) NOT NULL,
discription VARCHAR(250) NOT NULL,
PRIMARY KEY(LOG_changes_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS Locked_Data;
CREATE Table Locked_Data(
tablename VARCHAR(30) NOT NULL,
PK VARCHAR(50) NOT NULL,
PRIMARY KEY(tablename, PK)
)TYPE=InnoDB;

DROP TABLE IF EXISTS LOG_transports;
CREATE Table LOG_transports(
LOG_transport_ID INTEGER UNSIGNED NOT NULL,
transport_ID VARCHAR(50),
direction VARCHAR(30),
disease VARCHAR(30),
diseasenote TEXT,
ambulant_stationary BINARY,
assistant BINARY,
notified_list TEXT,
caller_name VARCHAR(50),
caller_phonenumber VARCHAR(30),
caller_note TEXT,
patient_name VARCHAR(70),
patient_from_address VARCHAR(100),
patient_to_address VARCHAR(100),
patient_birthday DATETIME,
patient_sex BINARY,
transporttype VARCHAR(30),
priority INTEGER,
feedback TEXT,
creationDate DATETIME,
departure DATETIME,
appointment DATETIME,
appointmentPatient DATETIME,
PRIMARY KEY(LOG_transport_ID)
)TYPE=InnoDB;