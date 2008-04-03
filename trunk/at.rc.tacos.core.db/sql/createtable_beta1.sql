CREATE TABLE servicetype (
  servicetype_ID BIGINT IDENTITY (1, 1) NOT NULL, --AUTO_INCREMENT NOT NULL,
  servicetype VARCHAR(30) NOT NULL,
  PRIMARY KEY(servicetype_ID)
)


CREATE TABLE job (
  job_ID BIGINT IDENTITY (1,1) NOT NULL,
  jobname VARCHAR(30) NOT NULL,
  PRIMARY KEY(job_ID)
)


CREATE TABLE phonenumbers (
  phonenumber_ID BIGINT IDENTITY (1,1) NOT NULL,
  phonenumber VARCHAR(30) NOT NULL,
  phonename VARCHAR(30) NULL,
  PRIMARY KEY(phonenumber_ID)
)


CREATE TABLE userlogin (
  username VARCHAR(30) NOT NULL,
  pwd VARCHAR(255) NULL,
  --authorization VARCHAR(20) NULL,
  "authorization" VARCHAR(20) NULL,
  isloggedin BIT NULL,
  locked BIT NULL,
  PRIMARY KEY(username)
)


CREATE TABLE selected (
  selected_ID BIGINT NOT NULL, --IDENTITY (1,1) NOT NULL,
  name VARCHAR(30) NOT NULL,
  PRIMARY KEY(selected_ID)
)


-- Insert the possible values for the selected table
-- Do NOT clear this table
-- This MUST be done in the createTables script
INSERT INTO selected(selected_ID, name) VALUES(1, 'Notarzt');
INSERT INTO selected(selected_ID, name) VALUES(2, 'Exekutive');
INSERT INTO selected(selected_ID, name) VALUES(3, 'Feuerwehr');
INSERT INTO selected(selected_ID, name) VALUES(4, 'Bergrettung');
INSERT INTO selected(selected_ID, name) VALUES(5, 'Dienstführender');
INSERT INTO selected(selected_ID, name) VALUES(6, 'Bezirksrettungskommandant');
INSERT INTO selected(selected_ID, name) VALUES(7, 'BD2');
INSERT INTO selected(selected_ID, name) VALUES(8, 'Hubschrauber');
INSERT INTO selected(selected_ID, name) VALUES(9, 'Begleitperson');
INSERT INTO selected(selected_ID, name) VALUES(10, 'Ruecktransport');
INSERT INTO selected(selected_ID, name) VALUES(11, 'Fernfahrt');
INSERT INTO selected(selected_ID, name) VALUES(12, 'Rufhilfepatient');
INSERT INTO selected(selected_ID, name) VALUES(13, 'KIT');
INSERT INTO selected(selected_ID, name) VALUES(14, 'BD1');

CREATE TABLE disease (
  disease_ID BIGINT IDENTITY (1,1) NOT NULL,
  disease VARCHAR(30) NOT NULL,
  PRIMARY KEY(disease_ID)
)


CREATE TABLE caller (
  caller_ID BIGINT IDENTITY (1,1) NOT NULL,
  callername VARCHAR(150) NULL,
  caller_phonenumber VARCHAR(60) NULL,
  PRIMARY KEY(caller_ID)
)


CREATE TABLE tmptransports (
  transportNr BIGINT NOT NULL,
  location_ID BIGINT NOT NULL,
  actualYear VARCHAR(39) NULL,

  PRIMARY KEY(transportNr,location_ID)
)


CREATE TABLE competences (
  competence_ID BIGINT IDENTITY (1,1) NOT NULL,
  competence VARCHAR(30) NOT NULL,
  PRIMARY KEY(competence_ID)
)


CREATE TABLE location (
  location_ID BIGINT IDENTITY (1,1) NOT NULL,
  phonenumber_ID BIGINT NOT NULL,
  locationname VARCHAR(30) NULL,
  street VARCHAR(30) NULL,
  streetnumber VARCHAR(10) NULL,
  zipcode BIGINT NULL,
  city VARCHAR(30) NULL,
  note TEXT NULL,
  PRIMARY KEY(location_ID),
  --INDEX location_FKIndex1(phonenumber_ID),
  FOREIGN KEY(phonenumber_ID)
    REFERENCES phonenumbers(phonenumber_ID)
      ON DELETE CASCADE
      ON UPDATE CASCADE
)
Create Index location_FKIndex1 ON location (phonenumber_id)


CREATE TABLE dialysis (
  dialysis_ID BIGINT IDENTITY (1,1) NOT NULL,
  firstname VARCHAR(30) NULL,
  lastname VARCHAR(30) NULL,
  location BIGINT NOT NULL,
  plannedStartOfTransport DATETIME NULL,
  plannedTimeAtPatient DATETIME NULL,
  appointmentTimeAtDialysis DATETIME NULL,
  plannedStartForBackTransport DATETIME NULL,
  readyTime DATETIME NULL,
  fromStreet VARCHAR(50) NULL,
  fromCity VARCHAR(50) NULL,
  toStreet VARCHAR(50) NULL,
  toCity VARCHAR(50) NULL,
  insurance VARCHAR(30) NULL,
  stationary BIT NULL,
  kindOfTransport VARCHAR(30) NULL,
  assistant BIT NULL,
  monday BIT NULL,
  tuesday BIT NULL,
  wednesday BIT NULL,
  thursday BIT NULL,
  friday BIT NULL,
  saturday BIT NULL,
  sunday BIT NULL,
  PRIMARY KEY(dialysis_ID),
  FOREIGN KEY(location)
    REFERENCES location(location_ID)
      ON DELETE CASCADE
      ON UPDATE CASCADE
)


CREATE TABLE dialysis_transport (
	dialysis_ID BIGINT NOT NULL,
	PRIMARY KEY(dialysis_ID),
 	transport_date DATETIME NULL,
 	return_date DATETIME NULL,
 	FOREIGN KEY(dialysis_ID)
    	REFERENCES dialysis(dialysis_ID)
		ON DELETE CASCADE
		ON UPDATE CASCADE
)


CREATE TABLE dayinfo (
  date DATETIME NOT NULL,
  username VARCHAR(30) NOT NULL,
  message TEXT NULL,
  PRIMARY KEY(date),
  --INDEX dayinfo_FKIndex1(username),
  FOREIGN KEY(username)
    REFERENCES userlogin(username)
      ON DELETE CASCADE
      ON UPDATE CASCADE
)


CREATE TABLE staffmembers (
  staffmember_ID BIGINT NOT NULL,
  username VARCHAR(30) NOT NULL,
  primaryLocation BIGINT NOT NULL,
  lastname VARCHAR(30) NOT NULL,
  firstname VARCHAR(30) NOT NULL,
  sex BIT NULL,
  birthday VARCHAR(12) NULL,
  email VARCHAR(100) NULL,
  street VARCHAR(50) NULL,
  city VARCHAR(50) NULL,
  PRIMARY KEY(staffmember_ID),
  --INDEX staffmembers_FKIndex1(primaryLocation),
  --INDEX staffmembers_FKIndex2(username),
  FOREIGN KEY(primaryLocation)
    REFERENCES location(location_ID)
      ON DELETE CASCADE
      ON UPDATE CASCADE,
  FOREIGN KEY(username)
    REFERENCES userlogin(username)
      ON DELETE CASCADE
      ON UPDATE CASCADE
)


CREATE TABLE phone_staffmember (
  staffmember_ID BIGINT NOT NULL,
  phonenumber_ID BIGINT NOT NULL,
  PRIMARY KEY(staffmember_ID, phonenumber_ID),
  --INDEX phonenumbers_has_staffmembers_FKIndex1(phonenumber_ID),
  --INDEX phonenumbers_has_staffmembers_FKIndex2(staffmember_ID),
  FOREIGN KEY(phonenumber_ID)
    REFERENCES phonenumbers(phonenumber_ID)
      --ON DELETE CASCADE
      --ON UPDATE CASCADE,
		ON DELETE NO ACTION
		ON UPDATE NO ACTION,
  FOREIGN KEY(staffmember_ID)
    REFERENCES staffmembers(staffmember_ID)
      ON DELETE CASCADE
      ON UPDATE CASCADE
)


CREATE TABLE staffmember_competence (
  staffmember_ID BIGINT NOT NULL,
  competence_ID BIGINT NOT NULL,
  PRIMARY KEY(staffmember_ID, competence_ID),
  --INDEX staffmembers_has_competences_FKIndex1(staffmember_ID),
  --INDEX staffmembers_has_competences_FKIndex2(competence_ID),
  FOREIGN KEY(staffmember_ID)
    REFERENCES staffmembers(staffmember_ID)
      ON DELETE CASCADE
      ON UPDATE CASCADE,
  FOREIGN KEY(competence_ID)
    REFERENCES competences(competence_ID)
      ON DELETE CASCADE
      ON UPDATE CASCADE
)


CREATE TABLE transports (
  transport_ID BIGINT IDENTITY (1,1) NOT NULL,
  transportNr BIGINT NOT NULL,
  year VARCHAR(39) NULL,
  planned_location BIGINT NULL,
  caller_ID BIGINT NULL,
  CreatedBy_user VARCHAR(30) NOT NULL,
  note TEXT NULL,
  feedback TEXT NULL,
  creationDate DATETIME NULL,
  departure DATETIME NULL,
  appointment DATETIME NULL,
  appointmentPatient DATETIME NULL,
  disease VARCHAR(30) NULL,
  firstname VARCHAR(30) NULL,
  lastname VARCHAR(30) NULL,
  from_street VARCHAR(50) NULL,
  from_city VARCHAR(50) NULL,
  to_street VARCHAR(50) NULL,
  to_city VARCHAR(50) NULL,
  programstate BIGINT NULL,
  transporttype VARCHAR(30) NULL,
  priority VARCHAR(1) NULL,
  direction BIGINT NULL,
  dateOfTransport DATETIME NULL,
  PRIMARY KEY(transport_ID),
  --INDEX transports_FKIndex1(CreatedBy_user),
  --INDEX transports_FKIndex3(caller_ID),
  --INDEX transports_FKIndex7(planned_location),
  FOREIGN KEY(CreatedBy_user)
    REFERENCES userlogin(username)
      ON DELETE CASCADE
      ON UPDATE CASCADE,
  FOREIGN KEY(caller_ID)
    REFERENCES caller(caller_ID)
      ON DELETE CASCADE
      ON UPDATE CASCADE,
  FOREIGN KEY(planned_location)
    REFERENCES location(location_ID)
      ON DELETE CASCADE
      ON UPDATE CASCADE
)


CREATE TABLE roster (
  roster_ID BIGINT IDENTITY (1,1) NOT NULL,
  entry_createdBy VARCHAR(30) NOT NULL,
  staffmember_ID BIGINT NOT NULL,
  location_ID BIGINT NOT NULL,
  job_ID BIGINT NOT NULL,
  servicetype_ID BIGINT NOT NULL,
  starttime DATETIME NOT NULL,
  endtime DATETIME NOT NULL,
  checkIn DATETIME NULL,
  checkOut DATETIME NULL,
  note TEXT NULL,
  standby BIT NULL,
  PRIMARY KEY(roster_ID),
  --INDEX roster_FKIndex1(staffmember_ID),
  --INDEX roster_FKIndex2(servicetype_ID),
  --INDEX roster_FKIndex3(job_ID),
  --INDEX roster_FKIndex4(location_ID),
  --INDEX roster_FKIndex5(entry_createdBy),
  FOREIGN KEY(staffmember_ID)
    REFERENCES staffmembers(staffmember_ID)
      ON DELETE CASCADE
      ON UPDATE CASCADE,
  FOREIGN KEY(servicetype_ID)
    REFERENCES servicetype(servicetype_ID)
      ON DELETE CASCADE
      ON UPDATE CASCADE,
  FOREIGN KEY(job_ID)
    REFERENCES job(job_ID)
      ON DELETE CASCADE
      ON UPDATE CASCADE,
  FOREIGN KEY(location_ID)
    REFERENCES location(location_ID)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(entry_createdBy)
    REFERENCES userlogin(username)
      --ON DELETE CASCADE
      --ON UPDATE CASCADE
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
)


CREATE TABLE vehicles (
  vehicle_ID VARCHAR(10) NOT NULL,
  medic2_ID BIGINT NULL,
  medic1_ID BIGINT NULL,
  driver_ID BIGINT NULL,
  currentLocation BIGINT NOT NULL,
  phonenumber_ID BIGINT NOT NULL,
  primaryLocation BIGINT NOT NULL,
  note TEXT NULL,
  vehicletype VARCHAR(30) NULL,
  readyForAction BIT NULL,
  outOfOrder BIT NULL,
  transportStatus BIGINT NULL,
  PRIMARY KEY(vehicle_ID),
  --INDEX vehicles_FKIndex1(primaryLocation),
  --INDEX vehicles_FKIndex2(phonenumber_ID),
  --INDEX vehicles_FKIndex3(currentLocation),
  --INDEX vehicles_FKIndex4(driver_ID),
  --INDEX vehicles_FKIndex5(medic1_ID),
  --INDEX vehicles_FKIndex6(medic2_ID),
  FOREIGN KEY(primaryLocation)
    REFERENCES location(location_ID)
      ON DELETE CASCADE
      ON UPDATE CASCADE,
  FOREIGN KEY(phonenumber_ID)
    REFERENCES phonenumbers(phonenumber_ID)
      --ON DELETE CASCADE
      --ON UPDATE CASCADE,
		ON DELETE NO ACTION
		ON UPDATE NO ACTION,
  FOREIGN KEY(currentLocation)
    REFERENCES location(location_ID)
      --ON DELETE CASCADE
      --ON UPDATE CASCADE,
		ON DELETE NO ACTION
		ON UPDATE NO ACTION,
  FOREIGN KEY(driver_ID)
    REFERENCES staffmembers(staffmember_ID)
      --ON DELETE CASCADE
      --ON UPDATE CASCADE,
		ON DELETE NO ACTION
		ON UPDATE NO ACTION,
  FOREIGN KEY(medic1_ID)
    REFERENCES staffmembers(staffmember_ID)
      --ON DELETE CASCADE
      --ON UPDATE CASCADE,
		ON DELETE NO ACTION
		ON UPDATE NO ACTION,
  FOREIGN KEY(medic2_ID)
    REFERENCES staffmembers(staffmember_ID)
      --ON DELETE CASCADE
      --ON UPDATE CASCADE
		ON DELETE NO ACTION
		ON UPDATE NO ACTION,
)


CREATE TABLE transportstate (
  transportstate TINYINT NOT NULL,
  transport_ID BIGINT NOT NULL,
  date DATETIME NULL,
  PRIMARY KEY(transportstate, transport_ID),
  --INDEX transportstate_FKIndex1(transport_ID),
  FOREIGN KEY(transport_ID)
    REFERENCES transports(transport_ID)
      ON DELETE CASCADE
      ON UPDATE CASCADE
)


CREATE TABLE assigned_vehicle (
  transport_ID BIGINT NOT NULL,
  vehicle_ID VARCHAR(10) NOT NULL,
  vehicletype VARCHAR(30) NULL,
  driver_ID BIGINT NULL,  
  driver_lastname VARCHAR(30) NULL,
  driver_firstname VARCHAR(30) NULL,
  medic1_ID BIGINT NULL,  
  medic1_lastname VARCHAR(30) NULL,
  medic1_firstname VARCHAR(30) NULL,
  medic2_ID BIGINT NULL,  
  medic2_lastname VARCHAR(30) NULL,
  medic2_firstname VARCHAR(30) NULL, 
  location_ID BIGINT NULL,
  location_name VARCHAR(30) NULL, 
  note TEXT NULL,
  PRIMARY KEY(transport_ID),
  --INDEX assigned_vehicle_FKIndex1(transport_ID),
  FOREIGN KEY(transport_ID)
    REFERENCES transports(transport_ID)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
)


CREATE TABLE transport_selected (
  selected_ID BIGINT NOT NULL,
  transport_ID BIGINT NOT NULL,
  alarmingDateTime DATETIME NULL,
  PRIMARY KEY(selected_ID, transport_ID),
  --INDEX notyfied_has_transports_FKIndex1(selected_ID),
  --INDEX notyfied_has_transports_FKIndex2(transport_ID),
  FOREIGN KEY(selected_ID)
    REFERENCES selected(selected_ID)
      ON DELETE CASCADE
      ON UPDATE CASCADE,
  FOREIGN KEY(transport_ID)
    REFERENCES transports(transport_ID)
      ON DELETE CASCADE
      ON UPDATE CASCADE
)