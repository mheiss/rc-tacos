-- Insert data scrip, version 1.5 lastChanged 20.02.2008
-- last changed:
-- moved the transport states to the createTable script. 

-- Sercive types
INSERT INTO servicetype(servicetype_ID, servicetype) VALUES(1, 'Hauptamtlich');
INSERT INTO servicetype(servicetype_ID, servicetype) VALUES(2, 'Freiwillig');
INSERT INTO servicetype(servicetype_ID, servicetype) VALUES(3, 'Ersatzeinstellung');
INSERT INTO servicetype(servicetype_ID, servicetype) VALUES(4, 'Zivildiener');
INSERT INTO servicetype(servicetype_ID, servicetype) VALUES(5, 'Sonstiges');

-- Available jobs
INSERT INTO job(job_ID, jobname) VALUES(1, 'Fahrer');
INSERT INTO job(job_ID, jobname) VALUES(2, 'Sanitäter');
INSERT INTO job(job_ID, jobname) VALUES(3, 'Notfallsanitäter');
INSERT INTO job(job_ID, jobname) VALUES(4, 'Rettungssanitäter');
INSERT INTO job(job_ID, jobname) VALUES(5, 'Notarzt');
INSERT INTO job(job_ID, jobname) VALUES(6, 'Leitstellendisponent');
INSERT INTO job(job_ID, jobname) VALUES(7, 'Dienststellenführender');
INSERT INTO job(job_ID, jobname) VALUES(8, 'Inspektionsdienst');
INSERT INTO job(job_ID, jobname) VALUES(9, 'BKTW-Fahrer');
INSERT INTO job(job_ID, jobname) VALUES(10, 'Journaldienst');
INSERT INTO job(job_ID, jobname) VALUES(11, 'Volontär');
INSERT INTO job(job_ID, jobname) VALUES(12, 'Sonstiges');

-- Mobile phones
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(1, '0699-11321018', 'BM01');
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(2, '0699-10342965', 'BM02');
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(3, '0664-12345678', 'privat');
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(4, '0361-12345678', 'privat');

-- logins
INSERT INTO userlogin(username, pwd, authorization, isloggedin, locked) VALUES('w.lohm', 'pwd', 'Administrator', false, false);
INSERT INTO userlogin(username, pwd, authorization, isloggedin, locked) VALUES('m.heiß', 'pwd', 'Administrator', false, false);
INSERT INTO userlogin(username, pwd, authorization, isloggedin, locked) VALUES('u.nech', 'pwd', 'Administrator', false, false);
INSERT INTO userlogin(username, pwd, authorization, isloggedin, locked) VALUES('user3', 'P@ssw0rd', 'Benutzer', false, false);

--diseases
INSERT INTO disease(disease_ID, disease) VALUES(1, 'Knochenbruch');
INSERT INTO disease(disease_ID, disease) VALUES(2, 'Kreislauf');
INSERT INTO disease(disease_ID, disease) VALUES(3, 'Neuronale Beschwerden');

--competences
INSERT INTO competences(competence_ID, competence) VALUES(1, 'Rettungssani');
INSERT INTO competences(competence_ID, competence) VALUES(2, 'Einsatzfahrer');
INSERT INTO competences(competence_ID, competence) VALUES(3, 'FK 1');
INSERT INTO competences(competence_ID, competence) VALUES(4, 'FK 2');
INSERT INTO competences(competence_ID, competence) VALUES(5, 'FK 3');
INSERT INTO competences(competence_ID, competence) VALUES(6, 'Leitstellendisponent');
INSERT INTO competences(competence_ID, competence) VALUES(7, 'Notfallsanitäter');
INSERT INTO competences(competence_ID, competence) VALUES(8, 'KIT');
INSERT INTO competences(competence_ID, competence) VALUES(9, 'SVE');

--locations
INSERT INTO location(location_ID, locationname, street, streetnumber, zipcode, city, note, phonenumber_ID)
VALUES(1, 'Bezirk: Bruck - Kapfenberg', 'Bruckerstr.', '144', 8600, 'Bruck', 'BE', 3);
INSERT INTO location(location_ID, locationname, street, streetnumber, zipcode, city, note, phonenumber_ID)
VALUES(2, 'Bruck an der Mur', 'Wienerstr.', '144', 8605, 'Kapfenberg', 'BM', 2);
INSERT INTO location(location_ID, locationname, street, streetnumber, zipcode, city, note, phonenumber_ID)
VALUES(3, 'Kapfenberg', 'Wienerstr.', '144', 8605, 'Kapfenberg', 'KA', 2);
INSERT INTO location(location_ID, locationname, street, streetnumber, zipcode, city, note, phonenumber_ID)
VALUES(4, 'Thörl', 'Wienerstr.', '144', 8605, 'Kapfenberg', 'TH', 2);
INSERT INTO location(location_ID, locationname, street, streetnumber, zipcode, city, note, phonenumber_ID)
VALUES(5, 'Turnau', 'Wienerstr.', '144', 8605, 'Kapfenberg', 'TU', 2);
INSERT INTO location(location_ID, locationname, street, streetnumber, zipcode, city, note, phonenumber_ID)
VALUES(6, 'Breitenau', 'Wienerstr.', '144', 8605, 'Kapfenberg', 'BR', 2);
INSERT INTO location(location_ID, locationname, street, streetnumber, zipcode, city, note, phonenumber_ID)
VALUES(7, 'St. Marein', 'Wienerstr.', '144', 8605, 'Kapfenberg', 'MA', 2);

-- staff members
INSERT INTO staffmembers(staffmember_ID, primaryLocation, firstname, lastname, sex, birthday, email, street, city, username)
VALUES(50100001, 1, 'Walter', 'Lohmann', true, '1983-10-17', 'walter.lohmann.itm05@fh-joanneum.at', 'Krottendorf 8/27', 'Kapfenberg', 'w.lohm');
INSERT INTO staffmembers(staffmember_ID, primaryLocation, firstname, lastname, sex, birthday, email, street, city, username)
VALUES(50100002, 2, 'Michael', 'Heiss', true, '1984-12-02', 'michael.heiss.itm05@fh-joanneum.at', 'Krottendorf 8/27', 'Kapfenberg', 'm.heiß');
INSERT INTO staffmembers(staffmember_ID, primaryLocation, firstname, lastname, sex, birthday, email, street, city, username)
VALUES(50100003, 3, 'Ulrich', 'Nechansky', false, '1975-01-01', 'ulrichandre.nechansky.itm05@fh-joanneum.at', 'Bruckerstr. 1', 'Bruck an der Mur', 'u.nech');
INSERT INTO staffmembers(staffmember_ID, primaryLocation, firstname, lastname, sex, birthday, email, street, city, username) 
VALUES(50100004, 3, 'User', 'Three', false, '1980-02-10', 'user3@fh-joanneum.at', 'Bruckerstr. 1', 'Bruck an der Mur', 'user3');

-- assign mobile phones to staff members
INSERT INTO phone_staffmember(staffmember_ID, phonenumber_ID) VALUES(50100001, 1);
INSERT INTO phone_staffmember(staffmember_ID, phonenumber_ID) VALUES(50100002, 2);
INSERT INTO phone_staffmember(staffmember_ID, phonenumber_ID) VALUES(50100003, 3);
INSERT INTO phone_staffmember(staffmember_ID, phonenumber_ID) VALUES(50100003, 4);

-- assign the competences to staff members
INSERT INTO staffmember_competence(staffmember_ID, competence_ID) VALUES(50100001,1);
INSERT INTO staffmember_competence(staffmember_ID, competence_ID) VALUES(50100003,3);
INSERT INTO staffmember_competence(staffmember_ID, competence_ID) VALUES(50100003,5);
INSERT INTO staffmember_competence(staffmember_ID, competence_ID) VALUES(50100002,6);
INSERT INTO staffmember_competence(staffmember_ID, competence_ID) VALUES(50100002,1);
INSERT INTO staffmember_competence(staffmember_ID, competence_ID) VALUES(50100002,2);

-- create dummy dialyse patients
INSERT INTO dialysis(dialysis_ID, firstname, lastname, location, plannedStartOfTransport, plannedTimeAtPatient, appointmentTimeAtDialysis, plannedStartForBackTransport, readyTime, fromStreet, fromCity, toStreet, toCity, insurance, stationary, kindOfTransport, assistant, monday, tuesday, wednesday, thursday, friday, saturday, sunday) VALUES(1, 'max', 'muster', 1, '08:20', '09:00', '10:00', '14:00', '15:00', 'fromStreet1', 'fromCity1', 'toStreet1', 'toCity1', 'keine', false, 'liegend', false, true, false, false, true, false, true, true);
INSERT INTO dialysis(dialysis_ID, firstname, lastname, location, plannedStartOfTransport, plannedTimeAtPatient, appointmentTimeAtDialysis, plannedStartForBackTransport, readyTime, fromStreet, fromCity, toStreet, toCity, insurance, stationary, kindOfTransport, assistant, monday, tuesday, wednesday, thursday, friday, saturday, sunday) VALUES(2, 'frau', 'musterin', 1, '12:30', '13:00', '14:00', '15:00', '18:00', 'fromStreet2', 'fromCity2', 'toStreet2', 'toCity2', 'keine', false, 'liegend', false, false, false, true, true, false, false, true);
-- create the entries int the dialysis transport table
INSERT INTO dialysis_transport(dialysis_ID,transport_date,return_date) values(1,null,null);
INSERT INTO dialysis_transport(dialysis_ID,transport_date,return_date) values(2,null,null);

--create dummy caller
INSERT INTO caller(caller_ID, callername, caller_phonenumber)
VALUES(1, 'Herr Maier', '0342 - 12345');

--create dummy transports
INSERT INTO transports(transport_ID, transportNr, direction, caller_ID, note, createdBy_user, priority, feedback, creationDate, departure, appointment, appointmentPatient, transporttype, disease, firstname, lastname, planned_location, from_street, from_city, to_street, to_city, programstate, dateOfTransport)
VALUES(null, 0, 1, 1, 'note ...', 'w.lohm', 'B', 'kein NEF erforderlich', '20080103215400', '20071124215400', '20071124215400', '20071124215400', 'liegend', 'v.a. Schlaganfall..', 'Sepp', 'Maier', 4, 'Krottendorf 827', 'Kapfenberg', 'LKH', 'Graz', 0, '20080215215400');
INSERT INTO transports(transport_ID, transportNr, direction, caller_ID, note, createdBy_user, priority, feedback, creationDate, departure, appointment, appointmentPatient, transporttype, disease, firstname, lastname, planned_location, from_street, from_city, to_street, to_city, programstate, dateOfTransport)
VALUES(null, 0, 2, 1, 'note ...', 'u.nech', 'C', 'wie vermutet oder so', '20080103215400', '20080103215400', '20080103215400', '20080103215400', 'Tragsessel', 'v.a. Herzinfarkt', 'Sonja', 'Müller', 2, 'Wienerstr. 54', 'Kapfenberg', 'BKH', 'Bruck', 0, '20080215215400');
INSERT INTO transports(transport_ID, transportNr, direction, caller_ID, note, createdBy_user, priority, feedback, creationDate, departure, appointment, appointmentPatient, transporttype, disease, firstname, lastname, planned_location, from_street, from_city, to_street, to_city, programstate, dateOfTransport) 
VALUES(null, 0, 3, 1, 'note ...', 'w.lohm', 'E', 'Polizei nachfordern', '20080103215400', '20080103215400', '20080103215400', '20080103215400', 'gehend', 'v.a. Epianfall........', 'Hans', 'Hofer', 3, 'Wienerstr. 54', 'Kapfenberg', 'BKH', 'Bruck', 0, '20080215215400');
INSERT INTO transports(transport_ID, transportNr, direction, caller_ID, note, createdBy_user, priority, feedback, creationDate, departure, appointment, appointmentPatient, transporttype, disease, firstname, lastname, planned_location, from_street, from_city, to_street, to_city, programstate, dateOfTransport) 
VALUES(null, 0, 4, 1, 'note ...', 'm.heiß', 'B', 'drei Leichtverletzte', '20080103215400', '20080103215400', '20080103215400', '20080103215400', 'liegend', 'Lumbago ............', 'Max', 'Huber', 1, 'Wienerstr. 54', 'Kapfenberg', 'BKH', 'Bruck', 0, '20080215215400');

--create dummy roster entries
INSERT INTO roster(roster_ID, location_ID, staffmember_ID, servicetype_ID, job_ID, starttime, endtime, checkIn, checkOut, note, standby, entry_createdBy)
VALUES(null, 1, 50100001, 3, 2, '20071215070000', '20081215190000', null, null, 'Die erste fahrt in der Steiermark!', false, 'w.lohm');
INSERT INTO roster(roster_ID, location_ID, staffmember_ID, servicetype_ID, job_ID, starttime, endtime, checkIn, checkOut, note, standby, entry_createdBy)
VALUES(null, 1, 50100002, 3, 3, '20071215070000', '20081215190000', null, null, 'Marien goes Sozial!', true, 'm.heiß');
INSERT INTO roster(roster_ID, location_ID, staffmember_ID, servicetype_ID, job_ID, starttime, endtime, checkIn, checkOut, note, standby, entry_createdBy)
VALUES(null, 2, 50100003, 1, 1, '20071215190000', '20081216070000', null, null, 'kommt nicht aus den Federn!', true, 'u.nech');

--create vehicles
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder, transportStatus)
VALUES('BM01', 50100001, 50100002, null, 2, 'RTW(1)', 2, 2, 'notizen zum Fahrzeug', true, false, 10);
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder, transportStatus) 
VALUES('KA03', 50100003, 50100002, 50100001, 1, 'KTW', 3, 3, 'notizen zum Fahrzeug', true, false, 20);
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder, transportStatus) 
VALUES('MA02', null, null, null, 2, 'RTW(1)', 7, 7, 'notizen zum Fahrzeug', false, false, 30);

--transport stati
INSERT INTO transportstate(transportstate, transport_ID, date) VALUES(1, 1, 20080125220000);
INSERT INTO transportstate(transportstate, transport_ID, date) VALUES(2, 1, 20080125223000);
INSERT INTO transportstate(transportstate, transport_ID, date) VALUES(3, 1, 20080125230000);
INSERT INTO transportstate(transportstate, transport_ID, date) VALUES(5, 2, 20080123140000);

--assign vehicles to transport
INSERT INTO assigned_vehicle(transport_ID, vehicle_ID, driver_ID, medic1_ID, medic2_ID, location_ID, note, vehicletype)
VALUES(1, 'BM01', 50100001, 50100002, null, 1, 'bla bla...', 'RTW(1)');
INSERT INTO assigned_vehicle(transport_ID, vehicle_ID, driver_ID, medic1_ID, medic2_ID, location_ID, note, vehicletype) 
VALUES(2, 'BM02', 50100003, 50100002, null, 1, 'bla bla...', 'RTW(2)');

--transports
INSERT INTO transport_selected(transport_ID, selected_ID) VALUES(1, 1);
INSERT INTO transport_selected(transport_ID, selected_ID) VALUES(1, 2);
INSERT INTO transport_selected(transport_ID, selected_ID) VALUES(1, 5);
INSERT INTO transport_selected(transport_ID, selected_ID) VALUES(2, 2);
INSERT INTO transport_selected(transport_ID, selected_ID) VALUES(2, 7);