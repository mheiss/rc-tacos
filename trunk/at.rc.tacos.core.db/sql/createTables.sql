DROP TABLE IF EXISTS competence;
CREATE Table competence(
competence_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
zivilworker BINARY,
rescuemedic BINARY,
emergencymedic BINARY,
driver BINARY,
PRIMARY KEY(competence_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS servicetype;
CREATE Table servicetype(
servicetype_ID VARCHAR(30) NOT NULL,
note VARCHAR(250),
PRIMARY KEY(servicetype_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS job;
CREATE Table job(
job_ID VARCHAR(30) NOT NULL,
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

DROP TABLE IF EXISTS blackboard;
CREATE Table blackboard(
blackboard_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
note TEXT,
PRIMARY KEY(blackboard_ID)
)TYPE=InnoDB;


DROP TABLE IF EXISTS location;
CREATE Table location(
locationname_ID VARCHAR(30) NOT NULL,
blackboard_ID INTEGER UNSIGNED NOT NULL, INDEX blackboard_ID_ind (blackboard_ID),
street VARCHAR(30),
streetnumber VARCHAR(10),
zipcode VARCHAR(10),
city VARCHAR(30),
phone VARCHAR(30),
note VARCHAR(250),
FOREIGN KEY(blackboard_ID) REFERENCES blackboard(blackboard_ID) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY(locationname_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS employees;
CREATE Table employees(
employee_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
primaryLocation VARCHAR(30) NOT NULL, INDEX primaryLocation_ind (primaryLocation),
competence_ID INTEGER UNSIGNED NOT NULL, INDEX competence_ID_ind (competence_ID),
username VARCHAR(30) NOT NULL, INDEX username_ind (username),
firstname VARCHAR(30) NOT NULL,
lastname VARCHAR(30) NOT NULL,
sex BINARY,
birthday DATETIME,
street VARCHAR(30),
streetnumber VARCHAR(10),
zipcode VARCHAR(10),
city VARCHAR(30),
phone TEXT,
email VARCHAR(30),
FOREIGN KEY(primaryLocation) REFERENCES location(locationname_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(competence_ID) REFERENCES competence(competence_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(username) REFERENCES userlogin(username) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY(employee_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS roster;
CREATE Table roster(
roster_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
employee_ID INTEGER UNSIGNED NOT NULL, INDEX employee_ID_ind (employee_ID),
locationname_ID VARCHAR(30) NOT NULL, INDEX locationname_ID_ind (locationname_ID),
servicetype_ID VARCHAR(30) NOT NULL, INDEX servicetype_ID_ind (servicetype_ID),
job_ID VARCHAR(30) NOT NULL, INDEX job_ID_ind (job_ID),
start DATETIME,
end DATETIME,
checkIn DATETIME,
checkOut DATETIME,
note VARCHAR(250),
FOREIGN KEY(employee_ID) REFERENCES employees(employee_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(locationname_ID) REFERENCES location(locationname_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(servicetype_ID) REFERENCES servicetype(servicetype_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(job_ID) REFERENCES job(job_ID) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY(roster_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS mobilephones;
CREATE Table mobilephones(
phonenumber VARCHAR(30) NOT NULL,
mobilephonename VARCHAR(30),
PRIMARY KEY(phonenumber)
)TYPE=InnoDB;

DROP TABLE IF EXISTS vehicletype;
CREATE Table vehicletype(
vehicletype_ID VARCHAR(30) NOT NULL,
note VARCHAR(250),
PRIMARY KEY(vehicletype_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS vehiclestate;
CREATE Table vehiclestate(
vehiclestate_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
outOfOrder DATETIME,
readyForAction DATETIME,
manned DATETIME,
PRIMARY KEY(vehiclestate_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS vehicles;
CREATE Table vehicles(
vehicle_ID VARCHAR(10) NOT NULL,
primaryLocation VARCHAR(30) NOT NULL, INDEX primaryLocation_ind (primaryLocation),
vehicletype_ID VARCHAR(30) NOT NULL, INDEX vehicletype_ID_ind (vehicletype_ID),
phonenumber VARCHAR(30) NOT NULL, INDEX phonenumber_ind (phonenumber),
note VARCHAR(250),
FOREIGN KEY(primaryLocation) REFERENCES location(locationname_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(vehicletype_ID) REFERENCES vehicletype(vehicletype_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(phonenumber) REFERENCES mobilephones(phonenumber) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY(vehicle_ID)
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
currentLocation VARCHAR(30) NOT NULL, INDEX currentLocation_ind (currentLocation),
note VARCHAR(250),
FOREIGN KEY(vehicle_ID) REFERENCES vehicles(vehicle_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(driver) REFERENCES employees(employee_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(medic1) REFERENCES employees(employee_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(medic2) REFERENCES employees(employee_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(currentLocation) REFERENCES location(locationname_ID) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY(employee_vehicle_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS transportdetails;
CREATE Table transportdetails(
transportdetails_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
locationname_ID VARCHAR(30) NOT NULL, INDEX locationname_ID_ind (locationname_ID),
ambulant_stationary BINARY,
assistant BINARY,
FOREIGN KEY(locationname_ID) REFERENCES location(locationname_ID) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY(transportdetails_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS transportdata;
CREATE Table transportdata(
patient_ID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
firstname VARCHAR(30) NOT NULL,
lastname VARCHAR(30) NOT NULL,
sex BINARY,
birthday DATETIME,
from_street VARCHAR(30),
from_streetnumber VARCHAR(10),
from_zipcode VARCHAR(10),
from_city VARCHAR(30),
to_street VARCHAR(30),
to_streetnumber VARCHAR(10),
to_zipcode VARCHAR(10),
to_city VARCHAR(30),
transporttype VARCHAR(30),
PRIMARY KEY(patient_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS disease;
CREATE Table disease(
disease_ID VARCHAR(30) NOT NULL,
PRIMARY KEY(disease_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS notyfied;
CREATE Table notyfied(
notyfied_ID VARCHAR(30) NOT NULL,
PRIMARY KEY(notyfied_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS caller;
CREATE Table caller(
caller_ID INTEGER UNSIGNED NOT NULL,
callername VARCHAR(30),
caller_phonenumber VARCHAR(30),
caller_note VARCHAR(250),
PRIMARY KEY(caller_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS directions;
CREATE Table directions(
direction_ID VARCHAR(30) NOT NULL,
PRIMARY KEY(direction_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS transportstate;
CREATE Table transportstate(
transportstate_ID INTEGER UNSIGNED NOT NULL,
PRIMARY KEY(transportstate_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS transports;
CREATE Table transports(
transport_ID INTEGER UNSIGNED NOT NULL,
employee_vehicle_ID INTEGER UNSIGNED NOT NULL, INDEX employee_vehicle_ID_ind (employee_vehicle_ID),
direction_ID VARCHAR(30) NOT NULL, INDEX direction_ID_ind (direction_ID),
caller_ID INTEGER UNSIGNED NOT NULL, INDEX caller_ID_ind (caller_ID),
disease_ID VARCHAR(30) NOT NULL, INDEX disease_ID_ind (disease_ID),
patient_ID INTEGER UNSIGNED NOT NULL, INDEX patient_ID_ind (patient_ID),
transportdetails_ID INTEGER UNSIGNED NOT NULL, INDEX transportdetails_ID_ind (transportdetails_ID),
primary_scheduler INTEGER UNSIGNED NOT NULL, INDEX primary_scheduler_ind (primary_scheduler),
current_scheduler INTEGER UNSIGNED NOT NULL, INDEX current_scheduler_ind (current_scheduler),
diseasenote TEXT,
priority INTEGER,
feedback TEXT,
creationDate DATETIME,
departure DATETIME,
appointment DATETIME,
appointmentPatient DATETIME,
FOREIGN KEY(employee_vehicle_ID) REFERENCES employee_vehicle(employee_vehicle_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(direction_ID) REFERENCES directions(direction_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(caller_ID) REFERENCES caller(caller_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(disease_ID) REFERENCES disease(disease_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(patient_ID) REFERENCES transportdata(patient_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(transportdetails_ID) REFERENCES transportdetails(transportdetails_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(primary_scheduler) REFERENCES employees(employee_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(current_scheduler) REFERENCES employees(employee_ID) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY(transport_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS transport_notyfied;
CREATE Table transport_notyfied(
transport_ID INTEGER UNSIGNED NOT NULL, INDEX transport_ID_ind (transport_ID),
notyfied_ID VARCHAR(30) NOT NULL, INDEX notyfied_ID_ind (notyfied_ID),
FOREIGN KEY(transport_ID) REFERENCES transports(transport_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(notyfied_ID) REFERENCES notyfied(notyfied_ID) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY(transport_ID, notyfied_ID)
)TYPE=InnoDB;

DROP TABLE IF EXISTS transport_transportstate;
CREATE Table transport_transportstate(
transportstate_ID INTEGER UNSIGNED NOT NULL, INDEX transportstate_ID_ind (transportstate_ID),
transport_ID INTEGER UNSIGNED NOT NULL, INDEX transport_ID_ind (transport_ID),
date DATETIME NOT NULL,
FOREIGN KEY(transport_ID) REFERENCES transports(transport_ID) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(transportstate_ID) REFERENCES transportstate(transportstate_ID) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY(transportstate_ID, transport_ID)
)TYPE=InnoDB;

