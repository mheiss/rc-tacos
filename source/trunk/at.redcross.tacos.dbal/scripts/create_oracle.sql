
    create table Assignment (
        id number(19,0) not null,
        history_changedat timestamp,
        history_changedby varchar2(255 char) not null,
        history_createdat timestamp,
        history_createdby varchar2(255 char) not null,
        description varchar2(255 char),
        name varchar2(255 char) not null,
        shortname varchar2(255 char),
        primary key (id)
    );

    create table Car (
        id number(19,0) not null,
        history_changedat timestamp,
        history_changedby varchar2(255 char) not null,
        history_createdat timestamp,
        history_createdby varchar2(255 char) not null,
        name varchar2(255 char) not null unique,
        notes varchar2(255 char),
        outoforder number(1,0),
        todelete number(1,0),
        location_id number(19,0),
        primary key (id)
    );

    create table Competence (
        id number(19,0) not null,
        history_changedat timestamp,
        history_changedby varchar2(255 char) not null,
        history_createdat timestamp,
        history_createdby varchar2(255 char) not null,
        description varchar2(255 char),
        name varchar2(255 char) not null unique,
        shortname varchar2(255 char),
        primary key (id)
    );

    create table District (
        id number(19,0) not null,
        history_changedat timestamp,
        history_changedby varchar2(255 char) not null,
        history_createdat timestamp,
        history_createdby varchar2(255 char) not null,
        name varchar2(255 char) not null unique,
        shortname varchar2(255 char),
        primary key (id)
    );

    create table Link (
        id number(19,0) not null,
        history_changedat timestamp,
        history_changedby varchar2(255 char) not null,
        history_createdat timestamp,
        history_createdby varchar2(255 char) not null,
        description varchar2(255 char),
        name varchar2(255 char) not null unique,
        primary key (id)
    );

    create table Location (
        id number(19,0) not null,
        history_changedat timestamp,
        history_changedby varchar2(255 char) not null,
        history_createdat timestamp,
        history_createdby varchar2(255 char) not null,
        name varchar2(255 char) not null unique,
        shortname varchar2(255 char),
        district_id number(19,0),
        District_Fk number(19,0),
        primary key (id)
    );

    create table Login (
        id number(19,0) not null,
        history_changedat timestamp,
        history_changedby varchar2(255 char) not null,
        history_createdat timestamp,
        history_createdby varchar2(255 char) not null,
        expireat date,
        locked number(1,0),
        loginname varchar2(255 char) not null unique,
        password varchar2(255 char),
        superuser number(1,0),
        systemuser_id number(19,0),
        primary key (id)
    );

    create table RosterEntry (
        id number(19,0) not null,
        history_changedat timestamp,
        history_changedby varchar2(255 char) not null,
        history_createdat timestamp,
        history_createdby varchar2(255 char) not null,
        notes varchar2(255 char),
        plannedenddate date not null,
        plannedendtime date not null,
        plannedstartdate date not null,
        plannedstarttime date not null,
        realenddate date,
        realendtime date,
        realstartdate date,
        realstarttime date,
        specialservice number(1,0),
        standby number(1,0),
        todelete number(1,0),
        assignment_id number(19,0),
        car_id number(19,0),
        location_id number(19,0),
        servicetype_id number(19,0),
        systemuser_id number(19,0),
        primary key (id)
    );

    create table SecuredAction (
        id number(19,0) not null,
        history_changedat timestamp,
        history_changedby varchar2(255 char) not null,
        history_createdat timestamp,
        history_createdby varchar2(255 char) not null,
        access varchar2(255 char) not null,
        actionexpression varchar2(255 char) not null unique,
        primary key (id)
    );

    create table SecuredResource (
        id number(19,0) not null,
        history_changedat timestamp,
        history_changedby varchar2(255 char) not null,
        history_createdat timestamp,
        history_createdby varchar2(255 char) not null,
        access varchar2(255 char) not null,
        expression number(1,0) not null,
        resource varchar2(255 char) not null unique,
        primary key (id)
    );

    create table ServiceType (
        id number(19,0) not null,
        history_changedat timestamp,
        history_changedby varchar2(255 char) not null,
        history_createdat timestamp,
        history_createdby varchar2(255 char) not null,
        description varchar2(255 char),
        name varchar2(255 char) not null unique,
        shortname varchar2(255 char),
        primary key (id)
    );

    create table SystemUser (
        id number(19,0) not null,
        history_changedat timestamp,
        history_changedby varchar2(255 char) not null,
        history_createdat timestamp,
        history_createdby varchar2(255 char) not null,
        address_city varchar2(255 char),
        address_email varchar2(255 char) not null unique,
        address_phone varchar2(255 char),
        address_phone2 varchar2(255 char),
        address_street varchar2(255 char),
        address_zipcode varchar2(255 char),
        birthday date,
        firstname varchar2(255 char) not null,
        gender varchar2(255 char) not null,
        lastname varchar2(255 char) not null,
        notes varchar2(255 char),
        pnr number(10,0) unique,
        todelete number(1,0),
        location_id number(19,0),
        primary key (id)
    );

    create table SystemUser_Competence (
        SystemUser_id number(19,0) not null,
        competences_id number(19,0) not null
    );

    create table SystemUser_UserGroup (
        SystemUser_id number(19,0) not null,
        groups_id number(19,0) not null
    );

    create table UserGroup (
        id number(19,0) not null,
        history_changedat timestamp,
        history_changedby varchar2(255 char) not null,
        history_createdat timestamp,
        history_createdby varchar2(255 char) not null,
        description varchar2(255 char),
        name varchar2(255 char) not null unique,
        primary key (id)
    );

    alter table Car 
        add constraint FK107B4716F1CD8 
        foreign key (location_id) 
        references Location;

    alter table Location 
        add constraint FK752A03D54004E1E2 
        foreign key (District_Fk) 
        references District;

    alter table Location 
        add constraint FK752A03D54004E238 
        foreign key (district_id) 
        references District;

    alter table Login 
        add constraint FK462FF4914938758 
        foreign key (systemuser_id) 
        references SystemUser;

    alter table RosterEntry 
        add constraint FKEF3D7087716F1CD8 
        foreign key (location_id) 
        references Location;

    alter table RosterEntry 
        add constraint FKEF3D70878AB8CE58 
        foreign key (assignment_id) 
        references Assignment;

    alter table RosterEntry 
        add constraint FKEF3D708714938758 
        foreign key (systemuser_id) 
        references SystemUser;

    alter table RosterEntry 
        add constraint FKEF3D7087C62A077C 
        foreign key (servicetype_id) 
        references ServiceType;

    alter table RosterEntry 
        add constraint FKEF3D70871D301F3C 
        foreign key (car_id) 
        references Car;

    alter table SystemUser 
        add constraint FK9D23FEBA716F1CD8 
        foreign key (location_id) 
        references Location;

    alter table SystemUser_Competence 
        add constraint FK446C328E14938758 
        foreign key (SystemUser_id) 
        references SystemUser;

    alter table SystemUser_Competence 
        add constraint FK446C328ED9A3C737 
        foreign key (competences_id) 
        references Competence;

    alter table SystemUser_UserGroup 
        add constraint FK16853A0F3A498367 
        foreign key (groups_id) 
        references UserGroup;

    alter table SystemUser_UserGroup 
        add constraint FK16853A0F14938758 
        foreign key (SystemUser_id) 
        references SystemUser;

    create sequence hibernate_sequence;
