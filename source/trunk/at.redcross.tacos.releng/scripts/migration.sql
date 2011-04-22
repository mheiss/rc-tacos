----------------------
-- Migration script --
----------------------

-- Changes from version 1 to version 2
alter table Car add inspectiondate datetime null;
alter table Car add licencenumber varchar(255) null;
alter table Car_AUD add inspectiondate datetime null;
alter table Car_AUD add licencenumber varchar(255) null;

alter table CarCareEntry drop column executeuntil;
alter table CarCareEntry drop column responsible;
alter table CarCareEntry_AUD drop column executeuntil;
alter table CarCareEntry_AUD drop column responsible;

