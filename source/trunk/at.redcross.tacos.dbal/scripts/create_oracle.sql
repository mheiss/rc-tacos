
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
        notes varchar2(1024 char),
        outoforder number(1,0),
        registrationdate timestamp,
        todelete number(1,0),
        type varchar2(255 char),
        detail_id number(19,0),
        location_id number(19,0) not null,
        primary key (id)
    );

    create table CarCareEntry (
        id number(19,0) not null,
        history_changedat timestamp,
        history_changedby varchar2(255 char) not null,
        history_createdat timestamp,
        history_createdby varchar2(255 char) not null,
        description varchar2(255 char),
        donefrom varchar2(255 char),
        executeuntil timestamp,
        executedon timestamp,
        responsible varchar2(255 char),
        status varchar2(255 char),
        type varchar2(255 char),
        car_id number(19,0),
        primary key (id)
    );

    create table CarDetail (
        id number(19,0) not null,
        history_changedat timestamp,
        history_changedby varchar2(255 char) not null,
        history_createdat timestamp,
        history_createdby varchar2(255 char) not null,
        authorizedfrom timestamp,
        authorizeduntil timestamp,
        buildup varchar2(255 char),
        carenotes varchar2(255 char),
        code varchar2(255 char),
        color varchar2(255 char),
        companyname varchar2(255 char),
        cylindercapacity number(10,0),
        enginepower number(10,0),
        enginepoweratmotorspeed number(10,0),
        identificationnumber varchar2(255 char),
        kindofdrive varchar2(255 char),
        limitspeed number(10,0),
        make varchar2(255 char),
        maxallowedaxleload1 number(10,0),
        maxallowedaxleload2 number(10,0),
        maxallowedaxleload3 number(10,0),
        maxallowedbuffload number(10,0),
        maxallowedcarryingcapicity number(10,0),
        maxallowedcoupledload number(10,0),
        maxallowedtotalweight number(10,0),
        motortype varchar2(255 char),
        netweight number(10,0),
        o2 number(10,0),
        purposeofuse varchar2(255 char),
        seats number(10,0),
        standings number(10,0),
        sticker varchar2(255 char),
        techvalidtotalvol number(10,0),
        tradename varchar2(255 char),
        typeofvehicle varchar2(255 char),
        variant varchar2(255 char),
        wheeldimensions varchar2(255 char),
        primary key (id)
    );

    create table Category (
        id number(19,0) not null,
        history_changedat timestamp,
        history_changedby varchar2(255 char) not null,
        history_createdat timestamp,
        history_createdby varchar2(255 char) not null,
        description varchar2(255 char),
        name varchar2(255 char) not null,
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

    create table Info (
        id number(19,0) not null,
        history_changedat timestamp,
        history_changedby varchar2(255 char) not null,
        history_createdat timestamp,
        history_createdby varchar2(255 char) not null,
        description long,
        displayenddate date not null,
        displaystartdate date not null,
        shortname varchar2(255 char),
        todelete number(1,0),
        category_id number(19,0) not null,
        location_id number(19,0) not null,
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
        password varchar2(255 char) not null,
        superuser number(1,0),
        primary key (id)
    );

    create table Notification (
        id number(19,0) not null,
        history_changedat timestamp,
        history_changedby varchar2(255 char) not null,
        history_createdat timestamp,
        history_createdby varchar2(255 char) not null,
        entrydate date not null unique,
        notes long,
        primary key (id)
    );

    create table RestoreLogin (
        id number(19,0) not null,
        history_changedat timestamp,
        history_changedby varchar2(255 char) not null,
        history_createdat timestamp,
        history_createdby varchar2(255 char) not null,
        expireat timestamp,
        token varchar2(255 char) not null,
        username varchar2(255 char) not null unique,
        primary key (id)
    );

    create table RevisionInfo (
        id number(10,0) not null,
        timestamp number(19,0) not null,
        username varchar2(255 char),
        primary key (id)
    );

    create table RosterEntry (
        id number(19,0) not null,
        history_changedat timestamp,
        history_changedby varchar2(255 char) not null,
        history_createdat timestamp,
        history_createdby varchar2(255 char) not null,
        notes varchar2(1024 char),
        plannedenddatetime timestamp not null,
        plannedstartdatetime timestamp not null,
        realenddatetime timestamp,
        realstartdatetime timestamp,
        specialservice number(1,0),
        standby number(1,0),
        state varchar2(255 char) not null,
        assignment_id number(19,0) not null,
        car_id number(19,0),
        location_id number(19,0) not null,
        servicetype_id number(19,0) not null,
        systemuser_id number(19,0) not null,
        primary key (id)
    );

    create table SecuredAction (
        id number(19,0) not null,
        history_changedat timestamp,
        history_changedby varchar2(255 char) not null,
        history_createdat timestamp,
        history_createdby varchar2(255 char) not null,
        accessexpression varchar2(255 char) not null,
        description varchar2(255 char),
        name varchar2(255 char) not null unique,
        primary key (id)
    );

    create table SecuredResource (
        id number(19,0) not null,
        history_changedat timestamp,
        history_changedby varchar2(255 char) not null,
        history_createdat timestamp,
        history_createdby varchar2(255 char) not null,
        access varchar2(255 char) not null,
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
        notes varchar2(1024 char),
        pnr number(10,0) unique,
        todelete number(1,0),
        location_id number(19,0) not null,
        login_id number(19,0) not null,
        primary key (id),
        unique (login_id)
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

    alter table Car 
        add constraint FK107B44CC82590 
        foreign key (detail_id) 
        references CarDetail;

    alter table CarCareEntry 
        add constraint FK9FF5628D1D301F3C 
        foreign key (car_id) 
        references Car;

    alter table Info 
        add constraint FK22D8CE716F1CD8 
        foreign key (location_id) 
        references Location;

    alter table Info 
        add constraint FK22D8CE3DDF2C38 
        foreign key (category_id) 
        references Category;

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
        add constraint FK9D23FEBAB76029C 
        foreign key (login_id) 
        references Login;

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
