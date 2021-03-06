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
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(2, '0664-9615263', 'BM02');
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(3, '0664-8218004', 'BM03');
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(4, '0664-8218005', 'BM04');
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(5, '0664-8218006', 'BM05');
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(6, '0664-8218028', 'BM06');
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(7, '0664-2516087', 'BR07');
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(8, '0664-9615263', 'BM08');
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(9, '0664-8218009', 'BM09');
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(10, '0664-8218011', 'BM10');
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(11, '0664-9615264', 'BM16');
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(12, '0664-9615251', 'KA02');
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(13, '0664-9615252', 'KA03');
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(14, '0664-9615253', 'KA04');
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(15, '0664-9615254', 'KA05');
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(16, '0664-9615255', 'KA06');
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(17, '0664-9615256', 'KA07');
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(18, '0664-9615257', 'KA08');
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(19, '0664-9615258', 'MA14');
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(20, '0664-9615259', 'MA15');
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(21, '0664-9615260', 'TH16');
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(22, '0664-9615261', 'TH17');
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(23, '0664-9615262', 'TU18');

INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(24, '0664-12345678', 'privat');
INSERT INTO phonenumbers(phonenumber_ID, phonenumber, phonename) VALUES(25, '0361-12345678', 'privat');

-- logins
INSERT INTO userlogin(username, pwd, authorization, isloggedin, locked) VALUES('w.lohmann', '8Y+di6ovoMtYViqHtCZzOFPgpOk=', 'Administrator', false, false);
INSERT INTO userlogin(username, pwd, authorization, isloggedin, locked) VALUES('m.hei�', 'F7nhxkWIx/pkGbTSncH0QmJ5ugE=', 'Administrator', false, false);
INSERT INTO userlogin(username, pwd, authorization, isloggedin, locked) VALUES('u.nechansky', '6pbBYrrwVSdvHLczVHDBkYoWVS0=', 'Administrator', false, false);
INSERT INTO userlogin(username, pwd, authorization, isloggedin, locked) VALUES('user3', 'Ib0S3Bg/dA7nbye3jrOcitlyp1c=', 'Benutzer', false, false);
INSERT INTO userlogin(username, pwd, authorization, isloggedin, locked) VALUES('w.gehard', '8Y+di6ovoMtYViqHtCZzOFPgpOk=', 'Administrator', false, false);
INSERT INTO userlogin(username, pwd, authorization, isloggedin, locked) VALUES('i.koeppel', '4Rt6vTdoF95SAZe5jwRqtj6bZZ4=', 'Administrator', false, false);
INSERT INTO userlogin(username, pwd, authorization, isloggedin, locked) VALUES('m.schunko', 'VGaVR6Il/yDLqLdaStylQO7yWFg=', 'Administrator', false, false);
INSERT INTO userlogin(username, pwd, authorization, isloggedin, locked) VALUES('h.maier', 'BwYCWyu87B7Y1kgi9OzNljFJONA=', 'Administrator', false, false);
-- diseases
INSERT INTO disease(disease_ID, disease) VALUES(1, 'Knochenbruch');
INSERT INTO disease(disease_ID, disease) VALUES(2, 'Kreislauf');
INSERT INTO disease(disease_ID, disease) VALUES(3, 'Neuronale Beschwerden');

-- competences
INSERT INTO competences(competence_ID, competence) VALUES(1, 'Rettungssanit�ter');
INSERT INTO competences(competence_ID, competence) VALUES(2, 'Einsatzfahrer');
INSERT INTO competences(competence_ID, competence) VALUES(3, 'FK 1');
INSERT INTO competences(competence_ID, competence) VALUES(4, 'FK 2');
INSERT INTO competences(competence_ID, competence) VALUES(5, 'FK 3');
INSERT INTO competences(competence_ID, competence) VALUES(6, 'Leitstellendisponent');
INSERT INTO competences(competence_ID, competence) VALUES(7, 'Notfallsanit�ter');
INSERT INTO competences(competence_ID, competence) VALUES(8, 'KIT');
INSERT INTO competences(competence_ID, competence) VALUES(9, 'SVE');

-- locations
INSERT INTO location(location_ID, locationname, street, streetnumber, zipcode, city, note, phonenumber_ID)
VALUES(1, 'Bezirk: Bruck - Kapfenberg', 'Bruckerstr.', '144', 8600, 'Bruck', 'BE', 3);
INSERT INTO location(location_ID, locationname, street, streetnumber, zipcode, city, note, phonenumber_ID)
VALUES(2, 'Bruck an der Mur', 'Wienerstr.', '144', 8605, 'Kapfenberg', 'BM', 2);
INSERT INTO location(location_ID, locationname, street, streetnumber, zipcode, city, note, phonenumber_ID)
VALUES(3, 'Kapfenberg', 'Wienerstr.', '144', 8605, 'Kapfenberg', 'KA', 2);
INSERT INTO location(location_ID, locationname, street, streetnumber, zipcode, city, note, phonenumber_ID)
VALUES(4, 'Th�rl', 'Wienerstr.', '144', 8605, 'Kapfenberg', 'TH', 2);
INSERT INTO location(location_ID, locationname, street, streetnumber, zipcode, city, note, phonenumber_ID)
VALUES(5, 'Turnau', 'Wienerstr.', '144', 8605, 'Kapfenberg', 'TU', 2);
INSERT INTO location(location_ID, locationname, street, streetnumber, zipcode, city, note, phonenumber_ID)
VALUES(6, 'Breitenau', 'Wienerstr.', '144', 8605, 'Kapfenberg', 'BR', 2);
INSERT INTO location(location_ID, locationname, street, streetnumber, zipcode, city, note, phonenumber_ID)
VALUES(7, 'St. Marein', 'Wienerstr.', '144', 8605, 'Kapfenberg', 'MA', 2);

-- staff members
INSERT INTO staffmembers(staffmember_ID, primaryLocation, firstname, lastname, sex, birthday, email, street, city, username)
VALUES(50100001, 1, 'Walter', 'Lohmann', true, '17-10-1983', 'walter.lohmann.itm05@fh-joanneum.at', 'Krottendorf 8/27', 'Kapfenberg', 'w.lohmann');
INSERT INTO staffmembers(staffmember_ID, primaryLocation, firstname, lastname, sex, birthday, email, street, city, username)
VALUES(50100002, 2, 'Michael', 'Heiss', true, '02-12-1984', 'michael.heiss.itm05@fh-joanneum.at', 'Krottendorf 8/27', 'Kapfenberg', 'm.hei�');
INSERT INTO staffmembers(staffmember_ID, primaryLocation, firstname, lastname, sex, birthday, email, street, city, username)
VALUES(50100003, 3, 'Ulrich', 'Nechansky', true, '01-01-1975', 'ulrichandre.nechansky.itm05@fh-joanneum.at', 'Bruckerstr. 1', 'Bruck an der Mur', 'u.nechansky');
INSERT INTO staffmembers(staffmember_ID, primaryLocation, firstname, lastname, sex, birthday, email, street, city, username) 
VALUES(50100004, 4, 'User', 'Three', false, '10-02-1980', 'user3@fh-joanneum.at', 'Bruckerstr. 1', 'Bruck an der Mur', 'user3');
INSERT INTO staffmembers(staffmember_ID, primaryLocation, firstname, lastname, sex, birthday, email, street, city, username)
VALUES(50100005, 1, 'Walter', 'Gehard', true, '17-10-1955', 'walter.gehard@st.roteskreuz.at', 'Schinitz', 'Kapfenberg', 'w.gehard');
INSERT INTO staffmembers(staffmember_ID, primaryLocation, firstname, lastname, sex, birthday, email, street, city, username)
VALUES(50100006, 1, 'Isabella', 'K�ppel', false, '17-10-1955', 'isabella.koeppel@st.roteskreuz.at', 'Schinitz', 'Kapfenberg', 'i.koeppel');
INSERT INTO staffmembers(staffmember_ID, primaryLocation, firstname, lastname, sex, birthday, email, street, city, username)
VALUES(50100007, 1, 'Martin', 'Schunko', true, '17-10-1955', 'martin.schunko@st.roteskreuz.at', 'Schinitz', 'Kapfenberg', 'm.schunko');
INSERT INTO staffmembers(staffmember_ID, primaryLocation, firstname, lastname, sex, birthday, email, street, city, username)
VALUES(50100008, 2, 'Helmut', 'Maier', true, '17-10-1955', 'helmut.maier@st.roteskreuz.at', 'Oberdorferstr.', 'Bruck an der Mur', 'h.maier');

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

-- create dummy caller
INSERT INTO caller(caller_ID, callername, caller_phonenumber)
VALUES(1, 'Herr Maier', '0342 - 12345');

-- create dummy transports
INSERT INTO transports(transport_ID, transportNr, direction, caller_ID, note, createdBy_user, priority, feedback, creationDate, departure, appointment, appointmentPatient, transporttype, disease, firstname, lastname, planned_location, from_street, from_city, to_street, to_city, programstate, dateOfTransport)
VALUES(null, 0, 1, 1, 'anote ...', 'w.lohm', 'B', 'kein NEF erforderlich', '20080103215400', '20071124215400', '20071124215400', '20071124215400', 'gehend', 'v.a. Schlaganfall..', 'Sepp', 'Maier', 4, 'Krottendorf 827', 'Kapfenberg', 'LKH', 'Graz', 0, '20080215215400');

INSERT INTO roster(roster_ID, location_ID, staffmember_ID, servicetype_ID, job_ID, starttime, endtime, checkIn, checkOut, note, standby, entry_createdBy)
VALUES(null, 1, 50100001, 3, 2, '20071215070000', '20081215190000', null, null, 'Die erste fahrt in der Steiermark!', false, 'w.lohmann');
INSERT INTO roster(roster_ID, location_ID, staffmember_ID, servicetype_ID, job_ID, starttime, endtime, checkIn, checkOut, note, standby, entry_createdBy)
VALUES(null, 1, 50100002, 3, 3, '20071215070000', '20081215190000', null, null, 'Marien goes Sozial!', true, 'm.hei�');
INSERT INTO roster(roster_ID, location_ID, staffmember_ID, servicetype_ID, job_ID, starttime, endtime, checkIn, checkOut, note, standby, entry_createdBy)
VALUES(null, 2, 50100003, 1, 1, '20071215190000', '20081216070000', null, null, 'kommt nicht aus den Federn!', true, 'u.nechansky');

-- create vehicles
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder, transportStatus)
VALUES('Bm02', null, null, null, 2, 'RTW(1)', 2, 2, 'notizen zum Fahrzeug', false, false, 0);
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder, transportStatus)
VALUES('Bm03', null, null, null, 2, 'RTW(2)', 2, 2, null, false, false, 0);
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder, transportStatus)
VALUES('Bm04', null, null, null, 2, 'KTW(2)', 2, 2, null, false, false, 0);
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder, transportStatus)
VALUES('Bm05', null, null, null, 2, 'RTW(2)', 2, 2, null, false, false, 0);
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder, transportStatus)
VALUES('Bm06', null, null, null, 2, 'RTW(2)', 2, 2, null, false, false, 0);
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder, transportStatus)
VALUES('Br07', null, null, null, 2, 'RTW(1)', 6, 6, null, false, false, 0);
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder, transportStatus)
VALUES('Bm08', null, null, null, 2, 'BKTW(2)', 2, 2, null, false, false, 0);
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder, transportStatus)
VALUES('Bm09', null, null, null, 2, 'KTW(2)', 2, 2, null, false, false, 0);
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder, transportStatus)
VALUES('Bm10', null, null, null, 2, 'PKW(0)', 2, 2, null, false, false, 0);
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder, transportStatus)
VALUES('Bm14', null, null, null, 2, 'LKW(0)', 1, 1, null, false, false, 0);
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder, transportStatus)
VALUES('Bm15', null, null, null, 2, 'SFZ(0)', 1, 1, null, false, false, 0);
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder, transportStatus)
VALUES('Bm16', null, null, null, 2, 'BKTW(2)', 2, 2, null, false, false, 0);

INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder, transportStatus)
VALUES('Ka02', null, null, null, 2, 'BKTW(1)', 3, 3, null, false, false, 0);
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder, transportStatus)
VALUES('Ka03', null, null, null, 2, 'PKW(0)', 3, 3, null, false, false, 0);
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder, transportStatus)
VALUES('Ka04', null, null, null, 2, 'RTW(2)', 3, 3, null, false, false, 0);
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder, transportStatus)
VALUES('Ka05', null, null, null, 2, 'RTW(2)', 3, 3, null, false, false, 0);
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder, transportStatus)
VALUES('Ka06', null, null, null, 2, 'KTW(2)', 3, 3, null, false, false, 0);
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder, transportStatus)
VALUES('Ka07', null, null, null, 2, 'RTW(1)', 3, 3, null, false, false, 0);
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder, transportStatus)
VALUES('Ka08', null, null, null, 2, 'PKW(0)', 3, 3, null, false, false, 0);
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder, transportStatus)
VALUES('Ka19', null, null, null, 2, 'SZF(0)', 1, 1, null, false, false, 0);
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder, transportStatus)
VALUES('Th16', null, null, null, 2, 'RTW(2)', 4, 4, null, false, false, 0);
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder, transportStatus)
VALUES('Th17', null, null, null, 2, 'PKW(0)', 4, 4, null, false, false, 0);
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder, transportStatus)
VALUES('Tu18', null, null, null, 2, 'RTW(2)', 5, 5, null, false, false, 0);
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder, transportStatus) 
VALUES('Ma14', null, null, null, 1, 'RTW(2)', 7, 7, null, false, false, 0);
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder, transportStatus)
VALUES('Ma15', null, null, null, 2, 'RTW(2)', 7, 7, null, false, false, 0);
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder, transportStatus) 
VALUES('NEF', null, null, null, 2, 'NEF(0)', 1, 1, null, false, false, 0);
INSERT INTO vehicles(vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder, transportStatus) 
VALUES('KDO', null, null, null, 2, 'KDO(0)', 1, 1, null, false, false, 0);