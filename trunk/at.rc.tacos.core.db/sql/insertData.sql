-- Insert data scrip, version 1.5

-- Sercive types
INSERT INTO servicetype(servicetype_ID, servicetype) VALUES(1, 'Hauptamtlich');
INSERT INTO servicetype(servicetype_ID, servicetype) VALUES(2, 'Freiwillig');
INSERT INTO servicetype(servicetype_ID, servicetype) VALUES(3, 'Ersatzeinstellung');
INSERT INTO servicetype(servicetype_ID, servicetype) VALUES(4, 'Zivildiener');
INSERT INTO servicetype(servicetype_ID, servicetype) VALUES(5, 'Sonstiges');

-- Available jobs
INSERT INTO job(job_ID, jobname) VALUES(1, 'Fahrer');
INSERT INTO job(job_ID, jobname) VALUES(2, 'Sanit�ter');
INSERT INTO job(job_ID, jobname) VALUES(3, 'Notfallsanit�ter');
INSERT INTO job(job_ID, jobname) VALUES(4, 'Rettungssanit�ter');
INSERT INTO job(job_ID, jobname) VALUES(5, 'Notarzt');
INSERT INTO job(job_ID, jobname) VALUES(6, 'Leitstellendisponent');
INSERT INTO job(job_ID, jobname) VALUES(7, 'Dienststellenf�hrender');
INSERT INTO job(job_ID, jobname) VALUES(8, 'Inspektionsdienst');
INSERT INTO job(job_ID, jobname) VALUES(9, 'BKTW-Fahrer');
INSERT INTO job(job_ID, jobname) VALUES(10, 'Journaldienst');
INSERT INTO job(job_ID, jobname) VALUES(11, 'Volont�r');
INSERT INTO job(job_ID, jobname) VALUES(12, 'Sonstiges');

-- Mobile phones
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(1, '0699-11321018', 'BM01');
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(2, '0699-10342965', 'BM02');
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(3, '0664-12345678', 'privat');
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(4, '0361-12345678', 'privat');

-- logins
INSERT INTO userlogin(username, pwd, authorization, isloggedin, locked) VALUES('walter', 'pwd', 'Administrator', false, false);
INSERT INTO userlogin(username, pwd, authorization, isloggedin, locked) VALUES('michael', 'pwd', 'Administrator', false, false);
INSERT INTO userlogin(username, pwd, authorization, isloggedin, locked) VALUES('birgit', 'pwd', 'Administrator', false, false);
INSERT INTO userlogin(username, pwd, authorization, isloggedin, locked) VALUES('benutzer', 'pwd', 'User', false, false);
INSERT INTO userlogin(username, pwd, authorization, isloggedin, locked) VALUES('locked', 'pwd', 'User', false, true);
INSERT INTO userlogin(username, pwd, authorization, isloggedin, locked) VALUES('user3', 'P@ssw0rd', 'User', false, false);

-- notifiers
INSERT INTO selected(selected_ID, name) VALUES(1, 'Notarzt');
INSERT INTO selected(selected_ID, name) VALUES(2, 'Exekutive');
INSERT INTO selected(selected_ID, name) VALUES(3, 'Feuerwehr');
INSERT INTO selected(selected_ID, name) VALUES(4, 'Bergrettung');
INSERT INTO selected(selected_ID, name) VALUES(5, 'Dienstf�hrender');
INSERT INTO selected(selected_ID, name) VALUES(6, 'Bezirksrettungskommandant');
INSERT INTO selected(selected_ID, name) VALUES(7, 'BD1');
INSERT INTO selected(selected_ID, name) VALUES(8, 'BD2');
INSERT INTO selected(selected_ID, name) VALUES(9, 'assistant');
INSERT INTO selected(selected_ID, name) VALUES(10, 'return_transport');
INSERT INTO selected(selected_ID, name) VALUES(11, 'longDistance');

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
INSERT INTO competences(competence_ID, competence) VALUES(7, 'Notfallsani');
INSERT INTO competences(competence_ID, competence) VALUES(8, 'KIT');
INSERT INTO competences(competence_ID, competence) VALUES(9, 'SVE');

--locations
INSERT INTO location(location_ID, locationname, street, streetnumber, zipcode, city, note, phonenumber_ID)
VALUES(1, 'Bezirk: Bruck - Kapfenberg', 'Bruckerstr.', '144', 8600, 'Bruck', 'hier ist die Leitstelle', 3);
INSERT INTO location(location_ID, locationname, street, streetnumber, zipcode, city, note, phonenumber_ID)
VALUES(2, 'Bruck an der Mur', 'Wienerstr.', '144', 8605, 'Kapfenberg', 'hier starten die Kapfenberger', 2);
INSERT INTO location(location_ID, locationname, street, streetnumber, zipcode, city, note, phonenumber_ID)
VALUES(3, 'Kapfenberg', 'Wienerstr.', '144', 8605, 'Kapfenberg', 'hier starten die Kapfenberger', 2);
INSERT INTO location(location_ID, locationname, street, streetnumber, zipcode, city, note, phonenumber_ID)
VALUES(4, 'Th�rl', 'Wienerstr.', '144', 8605, 'Kapfenberg', 'hier starten die Kapfenberger', 2);
INSERT INTO location(location_ID, locationname, street, streetnumber, zipcode, city, note, phonenumber_ID)
VALUES(5, 'Turnau', 'Wienerstr.', '144', 8605, 'Kapfenberg', 'hier starten die Kapfenberger', 2);
INSERT INTO location(location_ID, locationname, street, streetnumber, zipcode, city, note, phonenumber_ID)
VALUES(6, 'Breitenau', 'Wienerstr.', '144', 8605, 'Kapfenberg', 'hier starten die Kapfenberger', 2);
INSERT INTO location(location_ID, locationname, street, streetnumber, zipcode, city, note, phonenumber_ID)
VALUES(7, 'St. Martin', 'Wienerstr.', '144', 8605, 'Kapfenberg', 'hier starten die Kapfenberger', 2);

-- staff members
INSERT INTO staffmembers(staffmember_ID, primaryLocation, firstname, lastname, sex, birthday, email, street, city, username)
VALUES(50100001, 1, 'Walter', 'Lohmann', true, '1983-10-17', 'walter.lohmann.itm05@fh-joanneum.at', 'Krottendorf 8/27', 'Kapfenberg', 'walter');
INSERT INTO staffmembers(staffmember_ID, primaryLocation, firstname, lastname, sex, birthday, email, street, city, username)
VALUES(50100002, 2, 'Michael', 'Heiss', true, '1984-12-02', 'michael.heiss.itm05@fh-joanneum.at', 'Krottendorf 8/27', 'Kapfenberg', 'michael');
INSERT INTO staffmembers(staffmember_ID, primaryLocation, firstname, lastname, sex, birthday, email, street, city, username)
VALUES(50100003, 3, 'Birgit', 'Thek', false, '1975-01-01', 'birgit.thek.itm05@fh-joanneum.at', 'Bruckerstr. 1', 'Bruck an der Mur', 'birgit');
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

--create dummy transports
INSERT INTO transports(transport_ID, transportNr, direction, caller_ID, note, createdBy_user, priority, feedback, creationDate, departure, appointment, appointmentPatient, transporttype, disease, firstname, lastname, planned_location, from_street, from_city, to_street, to_city, programstate, dateOfTransport)
VALUES(null, 20080000001, 1, 1, 'note ...', 'walter', 'A', 'Fraktur des rechten Kn�chels', '20071124215400', '20071124215400', '20071124215400', '20071124215400', 'liegend', 'erkrankung ...', 'Sepp', 'Maier', 1, 'Krottendorf 8/27', 'Kapfenberg', 'LKH', 'Graz', 1, '20071124215400');
INSERT INTO transports(transport_ID, transportNr, direction, caller_ID, note, createdBy_user, priority, feedback, creationDate, departure, appointment, appointmentPatient, transporttype, disease, firstname, lastname, planned_location, from_street, from_city, to_street, to_city, programstate, dateOfTransport)
VALUES(null, 20080000002, 1, 1, 'note ...', 'birgit', 'C', 'hin halt', '20080103215400', '20080103215400', '20080103215400', '20080103215400', 'Tragstuhl', 'erkrankung ...', 'Sonja', 'M�ller', 1, 'Wienerstr. 54', 'Kapfenberg', 'BKH', 'Bruck', 2, '20071124215400');
INSERT INTO transports(transport_ID, transportNr, direction, caller_ID, note, createdBy_user, priority, feedback, creationDate, departure, appointment, appointmentPatient, transporttype, disease, firstname, lastname, planned_location, from_street, from_city, to_street, to_city, programstate, dateOfTransport) 
VALUES(null, 1, 1, 1, 'note ...', 'walter', 'E', 'hin halt', '20080103215400', '20080103215400', '20080103215400', '20080103215400', 'mobil', 'erkrankung ...', 'Storno', '...', 1, 'Wienerstr. 54', 'Kapfenberg', 'BKH', 'Bruck', 2, '20071124215400');
INSERT INTO transports(transport_ID, transportNr, direction, caller_ID, note, createdBy_user, priority, feedback, creationDate, departure, appointment, appointmentPatient, transporttype, disease, firstname, lastname, planned_location, from_street, from_city, to_street, to_city, programstate, dateOfTransport) 
VALUES(null, 2, 1, 1, 'note ...', 'walter', 'B', 'hin halt', '20080103215400', '20080103215400', '20080103215400', '20080103215400', 'liegend', 'erkrankung ...', 'Leerfahrt', '...', 1, 'Wienerstr. 54', 'Kapfenberg', 'BKH', 'Bruck', 2, '20071124215400');

--create dummy roster entries
INSERT INTO roster(roster_ID, location_ID, staffmember_ID, servicetype_ID, job_ID, starttime, endtime, checkIn, checkOut, note, standby, entry_createdBy)
VALUES(null, 1, 50100001, 3, 2, '20071215070000', '20071215190000', null, null, 'Die erste fahrt in der Steiermark!', false, 'walter');
INSERT INTO roster(roster_ID, location_ID, staffmember_ID, servicetype_ID, job_ID, starttime, endtime, checkIn, checkOut, note, standby, entry_createdBy)
VALUES(null, 1, 50100002, 3, 3, '20071215070000', '20071215190000', null, null, 'Marien goes Sozial!', false, 'michael');
INSERT INTO roster(roster_ID, location_ID, staffmember_ID, servicetype_ID, job_ID, starttime, endtime, checkIn, checkOut, note, standby, entry_createdBy)
VALUES(null, 2, 50100003, 1, 1, '20071215190000', '20071216070000', null, null, 'kommt nicht aus den Federn!', true, 'birgit');

--create vehicles
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder)
VALUES('BM01', 50100001, 50100002, null, 2, 'RTW(1)', 1, 1, 'notizen zum Fahrzeug', true, false);
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder) 
VALUES('KBG03', 50100003, 50100002, 50100001, 1, 'KTW', 2, 1, 'notizen zum Fahrzeug', true, false);
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder) 
VALUES('BM02', null, null, null, 2, 'RTW(1)', 3, 1, 'notizen zum Fahrzeug', false, true);

--transport stati
INSERT INTO transportstate(transportstate, transport_ID, date) VALUES(1, 1, 20080125220000);
INSERT INTO transportstate(transportstate, transport_ID, date) VALUES(2, 1, 20080125223000);
INSERT INTO transportstate(transportstate, transport_ID, date) VALUES(3, 1, 20080125230000);
INSERT INTO transportstate(transportstate, transport_ID, date) VALUES(5, 2, 20080123140000);

--assign vehicles to transport
INSERT INTO assigned_vehicle(transport_ID, vehicle_ID, driver_ID, medic1_ID, medic2_ID, locationname, note, vehicletype)
VALUES(1, 'BM01', 50100001, 50100002, null, 'BM', 'bla bla...', 'RTW(1)');
INSERT INTO assigned_vehicle(transport_ID, vehicle_ID, driver_ID, medic1_ID, medic2_ID, locationname, note, vehicletype) 
VALUES(2, 'BM02', 50100003, 50100002, null, 'BM', 'bla bla...', 'RTW(2)');

--transports
INSERT INTO transport_selected(transport_ID, selected_ID) VALUES(1, 1);
INSERT INTO transport_selected(transport_ID, selected_ID) VALUES(1, 2);
INSERT INTO transport_selected(transport_ID, selected_ID) VALUES(1, 5);
INSERT INTO transport_selected(transport_ID, selected_ID) VALUES(2, 2);
INSERT INTO transport_selected(transport_ID, selected_ID) VALUES(2, 7);