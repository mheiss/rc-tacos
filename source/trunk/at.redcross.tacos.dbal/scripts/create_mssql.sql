
    create table Assignment (
        id numeric(19,0) identity not null,
        history_changedat datetime null,
        history_changedby varchar(255) not null,
        history_createdat datetime null,
        history_createdby varchar(255) not null,
        description varchar(255) null,
        name varchar(255) not null,
        shortname varchar(255) null,
        primary key (id)
    );

    create table Car (
        id numeric(19,0) identity not null,
        history_changedat datetime null,
        history_changedby varchar(255) not null,
        history_createdat datetime null,
        history_createdby varchar(255) not null,
        name varchar(255) not null unique,
        notes varchar(1024) null,
        outoforder tinyint null,
        registrationdate datetime null,
        todelete tinyint null,
        type varchar(255) null,
        detail_id numeric(19,0) null,
        location_id numeric(19,0) not null,
        primary key (id)
    );

    create table CarCareEntry (
        id numeric(19,0) identity not null,
        history_changedat datetime null,
        history_changedby varchar(255) not null,
        history_createdat datetime null,
        history_createdby varchar(255) not null,
        description varchar(255) null,
        donefrom varchar(255) null,
        executeuntil datetime null,
        executedon datetime null,
        responsible varchar(255) null,
        status varchar(255) null,
        type varchar(255) null,
        car_id numeric(19,0) null,
        primary key (id)
    );

    create table CarDetail (
        id numeric(19,0) identity not null,
        history_changedat datetime null,
        history_changedby varchar(255) not null,
        history_createdat datetime null,
        history_createdby varchar(255) not null,
        authorizedfrom datetime null,
        authorizeduntil datetime null,
        buildup varchar(255) null,
        carenotes varchar(255) null,
        code varchar(255) null,
        color varchar(255) null,
        companyname varchar(255) null,
        cylindercapacity int null,
        enginepower int null,
        enginepoweratmotorspeed int null,
        identificationnumber varchar(255) null,
        kindofdrive varchar(255) null,
        limitspeed int null,
        make varchar(255) null,
        maxallowedaxleload1 int null,
        maxallowedaxleload2 int null,
        maxallowedaxleload3 int null,
        maxallowedbuffload int null,
        maxallowedcarryingcapicity int null,
        maxallowedcoupledload int null,
        maxallowedtotalweight int null,
        motortype varchar(255) null,
        netweight int null,
        o2 int null,
        purposeofuse varchar(255) null,
        seats int null,
        standings int null,
        sticker varchar(255) null,
        techvalidtotalvol int null,
        tradename varchar(255) null,
        typeofvehicle varchar(255) null,
        variant varchar(255) null,
        wheeldimensions varchar(255) null,
        primary key (id)
    );

    create table Category (
        id numeric(19,0) identity not null,
        history_changedat datetime null,
        history_changedby varchar(255) not null,
        history_createdat datetime null,
        history_createdby varchar(255) not null,
        description varchar(255) null,
        name varchar(255) not null,
        primary key (id)
    );

    create table Competence (
        id numeric(19,0) identity not null,
        history_changedat datetime null,
        history_changedby varchar(255) not null,
        history_createdat datetime null,
        history_createdby varchar(255) not null,
        description varchar(255) null,
        name varchar(255) not null unique,
        shortname varchar(255) null,
        primary key (id)
    );

    create table Info (
        id numeric(19,0) identity not null,
        history_changedat datetime null,
        history_changedby varchar(255) not null,
        history_createdat datetime null,
        history_createdby varchar(255) not null,
        description varchar(4096) null,
        displayenddate datetime not null,
        displaystartdate datetime not null,
        shortname varchar(255) null,
        todelete tinyint null,
        category_id numeric(19,0) not null,
        location_id numeric(19,0) not null,
        primary key (id)
    );

    create table Link (
        id numeric(19,0) identity not null,
        history_changedat datetime null,
        history_changedby varchar(255) not null,
        history_createdat datetime null,
        history_createdby varchar(255) not null,
        description varchar(255) null,
        name varchar(255) not null unique,
        primary key (id)
    );

    create table Location (
        id numeric(19,0) identity not null,
        history_changedat datetime null,
        history_changedby varchar(255) not null,
        history_createdat datetime null,
        history_createdby varchar(255) not null,
        name varchar(255) not null unique,
        shortname varchar(255) null,
        primary key (id)
    );

    create table Login (
        id numeric(19,0) identity not null,
        history_changedat datetime null,
        history_changedby varchar(255) not null,
        history_createdat datetime null,
        history_createdby varchar(255) not null,
        expireat datetime null,
        locked tinyint null,
        loginname varchar(255) not null unique,
        password varchar(255) not null,
        superuser tinyint null,
        primary key (id)
    );

    create table Notification (
        id numeric(19,0) identity not null,
        history_changedat datetime null,
        history_changedby varchar(255) not null,
        history_createdat datetime null,
        history_createdby varchar(255) not null,
        entrydate datetime not null unique,
        notes varchar(4096) null,
        primary key (id)
    );

    create table RestoreLogin (
        id numeric(19,0) identity not null,
        history_changedat datetime null,
        history_changedby varchar(255) not null,
        history_createdat datetime null,
        history_createdby varchar(255) not null,
        expireat datetime null,
        token varchar(255) not null,
        username varchar(255) not null unique,
        primary key (id)
    );

    create table RevisionInfo (
        id int identity not null,
        timestamp numeric(19,0) not null,
        username varchar(255) null,
        primary key (id)
    );

    create table RosterEntry (
        id numeric(19,0) identity not null,
        history_changedat datetime null,
        history_changedby varchar(255) not null,
        history_createdat datetime null,
        history_createdby varchar(255) not null,
        notes varchar(1024) null,
        plannedenddatetime datetime not null,
        plannedstartdatetime datetime not null,
        realenddatetime datetime null,
        realstartdatetime datetime null,
        specialservice tinyint null,
        standby tinyint null,
        state varchar(255) not null,
        assignment_id numeric(19,0) not null,
        car_id numeric(19,0) null,
        location_id numeric(19,0) not null,
        servicetype_id numeric(19,0) not null,
        systemuser_id numeric(19,0) not null,
        primary key (id)
    );

    create table SecuredAction (
        id numeric(19,0) identity not null,
        history_changedat datetime null,
        history_changedby varchar(255) not null,
        history_createdat datetime null,
        history_createdby varchar(255) not null,
        accessexpression varchar(255) not null,
        description varchar(255) null,
        name varchar(255) not null unique,
        primary key (id)
    );

    create table SecuredResource (
        id numeric(19,0) identity not null,
        history_changedat datetime null,
        history_changedby varchar(255) not null,
        history_createdat datetime null,
        history_createdby varchar(255) not null,
        access varchar(255) not null,
        resource varchar(255) not null unique,
        primary key (id)
    );

    create table ServiceType (
        id numeric(19,0) identity not null,
        history_changedat datetime null,
        history_changedby varchar(255) not null,
        history_createdat datetime null,
        history_createdby varchar(255) not null,
        description varchar(255) null,
        name varchar(255) not null unique,
        shortname varchar(255) null,
        primary key (id)
    );

    create table SystemUser (
        id numeric(19,0) identity not null,
        history_changedat datetime null,
        history_changedby varchar(255) not null,
        history_createdat datetime null,
        history_createdby varchar(255) not null,
        address_city varchar(255) null,
        address_email varchar(255) not null unique,
        address_phone varchar(255) null,
        address_phone2 varchar(255) null,
        address_street varchar(255) null,
        address_zipcode varchar(255) null,
        birthday datetime null,
        firstname varchar(255) not null,
        gender varchar(255) not null,
        lastname varchar(255) not null,
        notes varchar(1024) null,
        pnr int null unique,
        todelete tinyint null,
        location_id numeric(19,0) not null,
        login_id numeric(19,0) not null,
        primary key (id),
        unique (login_id)
    );

    create table SystemUser_Competence (
        SystemUser_id numeric(19,0) not null,
        competences_id numeric(19,0) not null
    );

    create table SystemUser_UserGroup (
        SystemUser_id numeric(19,0) not null,
        groups_id numeric(19,0) not null
    );

    create table UserGroup (
        id numeric(19,0) identity not null,
        history_changedat datetime null,
        history_changedby varchar(255) not null,
        history_createdat datetime null,
        history_createdby varchar(255) not null,
        description varchar(255) null,
        name varchar(255) not null unique,
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
