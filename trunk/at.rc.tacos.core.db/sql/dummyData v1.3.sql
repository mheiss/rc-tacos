-- Citys
INSERT INTO citys(city_ID, cityname, zipcode) VALUES(null, 'Kapfenberg', 8605);
INSERT INTO citys(city_ID, cityname, zipcode) VALUES(null, 'Bruck an der Mur', 8600);
INSERT INTO citys(city_ID, cityname, zipcode) VALUES(null, 'Graz', 8010);

-- Street
INSERT INTO streets(street_ID, streetname) VALUES(null, 'Krottendorf 8/27');
INSERT INTO streets(street_ID, streetname) VALUES(null, 'Wienerstr. 50');
INSERT INTO streets(street_ID, streetname) VALUES(null, 'Grazerstr. 12');

-- competences
INSERT INTO competences(competence_ID, competence) VALUES(1, 'Rettungssani');
INSERT INTO competences(competence_ID, competence) VALUES(2, 'Einsatzfahrer');
INSERT INTO competences(competence_ID, competence) VALUES(3, 'FK 1');
INSERT INTO competences(competence_ID, competence) VALUES(4, 'FK 2');
INSERT INTO competences(competence_ID, competence) VALUES(5, 'FK3');
INSERT INTO competences(competence_ID, competence) VALUES(6, 'Leitstellendisponent');
INSERT INTO competences(competence_ID, competence) VALUES(7, 'Notfallsani');
INSERT INTO competences(competence_ID, competence) VALUES(8, 'KIT');
INSERT INTO competences(competence_ID, competence) VALUES(9, 'SVE');

-- Userlogin
INSERT INTO userlogin(username, pwd, authorization, isloggedin, locked) VALUES('walter', 'pwd', 'Administrator', false, false);
INSERT INTO userlogin(username, pwd, authorization, isloggedin, locked) VALUES('michael', 'pwd', 'Administrator', false, false);
INSERT INTO userlogin(username, pwd, authorization, isloggedin, locked) VALUES('birgit', 'pwd', 'Administrator', false, false);
INSERT INTO userlogin(username, pwd, authorization, isloggedin, locked) VALUES('benutzer', 'pwd', 'User', false, false);
INSERT INTO userlogin(username, pwd, authorization, isloggedin, locked) VALUES('locked', 'pwd', 'User', false, true);

-- Job
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

-- servicetype
INSERT INTO servicetype(servicetype_ID, servicetype, note) VALUES(1, 'Hauptamtlich', 'notes...');
INSERT INTO servicetype(servicetype_ID, servicetype, note) VALUES(2, 'Freiwillig', 'notes...');
INSERT INTO servicetype(servicetype_ID, servicetype, note) VALUES(3, 'Ersatzeinstellung', 'notes...');
INSERT INTO servicetype(servicetype_ID, servicetype, note) VALUES(4, 'Zivildiener', 'notes...');
INSERT INTO servicetype(servicetype_ID, servicetype, note) VALUES(5, 'Sonstiges', 'notes...');

-- phonenumbers
INSERT INTO phonenumbers(phonenumber_ID, phonenumber) VALUES(null, '0699-11321018');
INSERT INTO phonenumbers(phonenumber_ID, phonenumber) VALUES(null, '0699-10342965');
INSERT INTO phonenumbers(phonenumber_ID, phonenumber) VALUES(null, '0664-12345678');

-- locations
INSERT INTO location(location_ID, locationname, street, streetnumber, zipcode, city, note, phonenumber_ID)
VALUES(1, 'Bezirk: Bruck - Kapfenberg', 'Bruckerstr.', '144', 8600, 'Bruck', 'hier ist die Leitstelle', 3);
INSERT INTO location(location_ID, locationname, street, streetnumber, zipcode, city, note, phonenumber_ID)
VALUES(2, 'Bruck an der Mur', 'Wienerstr.', '144', 8605, 'Kapfenberg', 'hier starten die Kapfenberger', 2);
INSERT INTO location(location_ID, locationname, street, streetnumber, zipcode, city, note, phonenumber_ID)
VALUES(3, 'Kapfenberg', 'Wienerstr.', '144', 8605, 'Kapfenberg', 'hier starten die Kapfenberger', 2);
INSERT INTO location(location_ID, locationname, street, streetnumber, zipcode, city, note, phonenumber_ID)
VALUES(4, 'Thörl', 'Wienerstr.', '144', 8605, 'Kapfenberg', 'hier starten die Kapfenberger', 2);
INSERT INTO location(location_ID, locationname, street, streetnumber, zipcode, city, note, phonenumber_ID)
VALUES(5, 'Turnau', 'Wienerstr.', '144', 8605, 'Kapfenberg', 'hier starten die Kapfenberger', 2);
INSERT INTO location(location_ID, locationname, street, streetnumber, zipcode, city, note, phonenumber_ID)
VALUES(6, 'Breitenau', 'Wienerstr.', '144', 8605, 'Kapfenberg', 'hier starten die Kapfenberger', 2);
INSERT INTO location(location_ID, locationname, street, streetnumber, zipcode, city, note, phonenumber_ID)
VALUES(7, 'St. Martin', 'Wienerstr.', '144', 8605, 'Kapfenberg', 'hier starten die Kapfenberger', 2);

-- staffmembers
INSERT INTO staffmembers(staffmember_ID, primaryLocation, firstname, lastname, sex, birthday, email, street, city, username)
VALUES(111, 1, 'Walter', 'Lohmann', true, '1983-10-17', 'walter.lohmann.itm05@fh-joanneum.at', 'Krottendorf 8/27', 'Kapfenberg', 'walter');
INSERT INTO staffmembers(staffmember_ID, primaryLocation, firstname, lastname, sex, birthday, email, street, city, username)
VALUES(112, 1, 'Michael', 'Heiss', true, '1984-12-02', 'michael.heiss.itm05@fh-joanneum.at', 'Krottendorf 8/27', 'Kapfenberg', 'michael');
INSERT INTO staffmembers(staffmember_ID, primaryLocation, firstname, lastname, sex, birthday, email, street, city, username)
VALUES(113, 1, 'Birgit', 'Thek', false, '1975-01-01', 'birgit.thek.itm05@fh-joanneum.at', 'Bruckerstr. 1', 'Bruck an der Mur', 'birgit');

-- staffmember-competence
INSERT INTO staffmember_competence(staffmember_ID, competence_ID) VALUES(111,1);
INSERT INTO staffmember_competence(staffmember_ID, competence_ID) VALUES(111,3);
INSERT INTO staffmember_competence(staffmember_ID, competence_ID) VALUES(111,5);
INSERT INTO staffmember_competence(staffmember_ID, competence_ID) VALUES(112,6);

-- phone_staffmember
INSERT INTO phone_staffmember(staffmember_ID, phonenumber_ID) VALUES(111,1);
INSERT INTO phone_staffmember(staffmember_ID, phonenumber_ID) VALUES(112,2);
INSERT INTO phone_staffmember(staffmember_ID, phonenumber_ID) VALUES(113,3);

-- roster (Datetime Format YYYYMMDDHHMMSS)
INSERT INTO roster(roster_ID, location_ID, staffmember_ID, servicetype_ID, job_ID, starttime, endtime, checkIn, checkOut, note, standby)
VALUES(null, 1, 111, 3, 2, '20071215070000', '20071215190000', null, null, 'Die erste fahrt in der Steiermark!', false);
INSERT INTO roster(roster_ID, location_ID, staffmember_ID, servicetype_ID, job_ID, starttime, endtime, checkIn, checkOut, note, standby)
VALUES(null, 1, 112, 3, 3, '20071215070000', '20071215190000', null, null, 'Marien goes Sozial!', false);
INSERT INTO roster(roster_ID, location_ID, staffmember_ID, servicetype_ID, job_ID, starttime, endtime, checkIn, checkOut, note, standby)
VALUES(null, 2, 113, 1, 1, '20071215190000', '20071216070000', null, null, 'kommt nicht aus den Federn!', true);

-- vehicletype
INSERT INTO vehicletype(vehicletype_ID, note) VALUES('RTW(1)', '1 Tragstuhl');
INSERT INTO vehicletype(vehicletype_ID, note) VALUES('RTW(2)', '2 Tragstühle');
INSERT INTO vehicletype(vehicletype_ID, note) VALUES('KTW', '...');
INSERT INTO vehicletype(vehicletype_ID, note) VALUES('NEF', '...');
INSERT INTO vehicletype(vehicletype_ID, note) VALUES('BKTW', '...');
INSERT INTO vehicletype(vehicletype_ID, note) VALUES('KDO', '...');

-- vehiclestates
INSERT INTO vehiclestate(vehiclestate_ID, vehiclestate) VALUES(1, 'Einstatzbereit');
INSERT INTO vehiclestate(vehiclestate_ID, vehiclestate) VALUES(2, 'Besetzt');
INSERT INTO vehiclestate(vehiclestate_ID, vehiclestate) VALUES(3, 'Außer Dienst');
INSERT INTO vehiclestate(vehiclestate_ID, vehiclestate) VALUES(4, 'Unbesetzt');

-- vehicles
INSERT INTO vehicles(vehicle_ID, phonenumber_ID, vehicletype_ID, primaryLocation, note)
VALUES('BM01', 3, 'RTW(1)', 1, 'notizen zum Fahrzeug');
INSERT INTO vehicles(vehicle_ID, phonenumber_ID, vehicletype_ID, primaryLocation, note)
VALUES('BM15', 2, 'NEF', 1, 'notizen zum Fahrzeug');
INSERT INTO vehicles(vehicle_ID, phonenumber_ID, vehicletype_ID, primaryLocation, note)
VALUES('KB01', 1, 'KTW', 2, 'notizen zum Fahrzeug');

-- vehicles_vehiclestate
INSERT INTO vehicles_vehiclestate(vehicle_ID, vehiclestate_ID, date) VALUES('BM01', 1, '20071125145600');
INSERT INTO vehicles_vehiclestate(vehicle_ID, vehiclestate_ID, date) VALUES('BM15', 3, '20071125124500');

-- staffmember_vehicle
INSERT INTO staffmember_vehicle(staffmember_vehicle_ID, vehicle_ID, driver_ID, medic1_ID, medic2_ID, currentLocation)
VALUES(null, 'BM15', 111, 112, 113, 1);

-- directions
INSERT INTO directions(direction_ID, direction) VALUES(1, 'Graz');
INSERT INTO directions(direction_ID, direction) VALUES(2, 'Kapfenberg');
INSERT INTO directions(direction_ID, direction) VALUES(3, 'Leoben');
INSERT INTO directions(direction_ID, direction) VALUES(4, 'Mürzzuschlag');
INSERT INTO directions(direction_ID, direction) VALUES(5, 'Wien');

-- disease
INSERT INTO disease(disease_ID, disease) VALUES(null, 'Knochenbruch');
INSERT INTO disease(disease_ID, disease) VALUES(null, 'Kreislauf');
INSERT INTO disease(disease_ID, disease) VALUES(null, 'Neuronale Beschwerden');

-- notyfied
INSERT INTO notyfied(notyfied_ID, name) VALUES(1, 'Notarzt');
INSERT INTO notyfied(notyfied_ID, name) VALUES(2, 'Exekutive');
INSERT INTO notyfied(notyfied_ID, name) VALUES(3, 'Feuerwehr');
INSERT INTO notyfied(notyfied_ID, name) VALUES(4, 'Bergrettung');
INSERT INTO notyfied(notyfied_ID, name) VALUES(5, 'Dienstführender');
INSERT INTO notyfied(notyfied_ID, name) VALUES(6, 'Bezirksrettungskommandant');
INSERT INTO notyfied(notyfied_ID, name) VALUES(7, 'BD1');
INSERT INTO notyfied(notyfied_ID, name) VALUES(8, 'BD2');

-- priority
INSERT INTO priority(priority_ID, priorityname) VALUES (1, 'NEF');
INSERT INTO priority(priority_ID, priorityname) VALUES (2, 'Einsatzfahrt');
INSERT INTO priority(priority_ID, priorityname) VALUES (3, 'Transport');
INSERT INTO priority(priority_ID, priorityname) VALUES (4, 'Rücktransport');
INSERT INTO priority(priority_ID, priorityname) VALUES (5, 'Heimtransport');
INSERT INTO priority(priority_ID, priorityname) VALUES (6, 'Sonstiges');
INSERT INTO priority(priority_ID, priorityname) VALUES (7, 'NEF extern');

-- documentation
INSERT INTO documentation(PK, date, note, username) VALUES(null, '20071124215400', 'docu text ...', 'walter');

-- caller
INSERT INTO caller(caller_ID, callername, caller_phonenumber)
VALUES(null, 'Herr Maier', '0342 - 12345');

-- transporttype
INSERT INTO transporttype(transporttype_ID, transporttype) VALUES(1, 'liegend');
INSERT INTO transporttype(transporttype_ID, transporttype) VALUES(2, 'Tragstuhl');
INSERT INTO transporttype(transporttype_ID, transporttype) VALUES(3, 'mobil');

-- transports
INSERT INTO transports(transport_ID, direction_ID, caller_ID, note, createdBy_user, priority_ID, feedback, creationDate, departure, appointment, appointmentPatient, assistant, staffmember_vehicle_ID, transporttype_ID, transportstate, disease, firstname, lastname, planned_location, return_transport, from_street, from_city, to_street, to_city)
VALUES('2007-BM-000001', 1, 1, 'note ...', 'walter', 1, 'Fraktur des rechten Knöchels', '20071124215400', '20071124215400', '20071124215400', '20071124215400', false, 1, 1, 2, 'erkrankung ...', 'Sepp', 'Maier', 1, 1, 'Krottendorf 8/27', 'Kapfenberg', 'LKH', 'Graz');

-- transport_notyfied
INSERT INTO transport_notyfied(transport_ID, notyfied_ID) VALUES('2007-BM-000001', 1);
INSERT INTO transport_notyfied(transport_ID, notyfied_ID) VALUES('2007-BM-000001', 2);