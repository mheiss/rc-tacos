
    create table Assignment (
        id bigint not null auto_increment,
        history_changedat datetime,
        history_changedby varchar(255) not null,
        history_createdat datetime,
        history_createdby varchar(255) not null,
        description varchar(255),
        name varchar(255) not null,
        shortname varchar(255),
        primary key (id)
    );

    create table Car (
        id bigint not null auto_increment,
        history_changedat datetime,
        history_changedby varchar(255) not null,
        history_createdat datetime,
        history_createdby varchar(255) not null,
        name varchar(255) not null unique,
        notes longtext,
        outoforder bit,
        registrationdate datetime,
        todelete bit,
        type varchar(255),
        detail_id bigint,
        location_id bigint not null,
        primary key (id)
    );

    create table CarCareEntry (
        id bigint not null auto_increment,
        history_changedat datetime,
        history_changedby varchar(255) not null,
        history_createdat datetime,
        history_createdby varchar(255) not null,
        description varchar(255),
        donefrom varchar(255),
        executeuntil datetime,
        executedon datetime,
        responsible varchar(255),
        status varchar(255),
        type varchar(255),
        car_id bigint,
        primary key (id)
    );

    create table CarDetail (
        id bigint not null auto_increment,
        history_changedat datetime,
        history_changedby varchar(255) not null,
        history_createdat datetime,
        history_createdby varchar(255) not null,
        authorizedfrom datetime,
        authorizeduntil datetime,
        buildup varchar(255),
        carenotes varchar(255),
        code varchar(255),
        color varchar(255),
        companyname varchar(255),
        cylindercapacity integer,
        enginepower integer,
        enginepoweratmotorspeed integer,
        identificationnumber varchar(255),
        kindofdrive varchar(255),
        limitspeed integer,
        make varchar(255),
        maxallowedaxleload1 integer,
        maxallowedaxleload2 integer,
        maxallowedaxleload3 integer,
        maxallowedbuffload integer,
        maxallowedcarryingcapicity integer,
        maxallowedcoupledload integer,
        maxallowedtotalweight integer,
        motortype varchar(255),
        netweight integer,
        o2 integer,
        purposeofuse varchar(255),
        seats integer,
        standings integer,
        sticker varchar(255),
        techvalidtotalvol integer,
        tradename varchar(255),
        typeofvehicle varchar(255),
        variant varchar(255),
        wheeldimensions varchar(255),
        primary key (id)
    );

    create table Category (
        id bigint not null auto_increment,
        history_changedat datetime,
        history_changedby varchar(255) not null,
        history_createdat datetime,
        history_createdby varchar(255) not null,
        description varchar(255),
        name varchar(255) not null,
        primary key (id)
    );

    create table Competence (
        id bigint not null auto_increment,
        history_changedat datetime,
        history_changedby varchar(255) not null,
        history_createdat datetime,
        history_createdby varchar(255) not null,
        description varchar(255),
        name varchar(255) not null unique,
        shortname varchar(255),
        primary key (id)
    );

    create table Info (
        id bigint not null auto_increment,
        history_changedat datetime,
        history_changedby varchar(255) not null,
        history_createdat datetime,
        history_createdby varchar(255) not null,
        description longtext,
        displayenddate date not null,
        displaystartdate date not null,
        shortname varchar(255),
        todelete bit,
        category_id bigint not null,
        location_id bigint not null,
        primary key (id)
    );

    create table Link (
        id bigint not null auto_increment,
        history_changedat datetime,
        history_changedby varchar(255) not null,
        history_createdat datetime,
        history_createdby varchar(255) not null,
        description varchar(255),
        name varchar(255) not null unique,
        primary key (id)
    );

    create table Location (
        id bigint not null auto_increment,
        history_changedat datetime,
        history_changedby varchar(255) not null,
        history_createdat datetime,
        history_createdby varchar(255) not null,
        name varchar(255) not null unique,
        shortname varchar(255),
        primary key (id)
    );

    create table Login (
        id bigint not null auto_increment,
        history_changedat datetime,
        history_changedby varchar(255) not null,
        history_createdat datetime,
        history_createdby varchar(255) not null,
        expireat date,
        locked bit,
        loginname varchar(255) not null unique,
        password varchar(255) not null,
        superuser bit,
        primary key (id)
    );

    create table Notification (
        id bigint not null auto_increment,
        history_changedat datetime,
        history_changedby varchar(255) not null,
        history_createdat datetime,
        history_createdby varchar(255) not null,
        entrydate date not null unique,
        notes longtext,
        primary key (id)
    );

    create table RestoreLogin (
        id bigint not null auto_increment,
        history_changedat datetime,
        history_changedby varchar(255) not null,
        history_createdat datetime,
        history_createdby varchar(255) not null,
        expireat datetime,
        token varchar(255) not null,
        username varchar(255) not null unique,
        primary key (id)
    );

    create table RevisionInfo (
        id integer not null auto_increment,
        timestamp bigint not null,
        username varchar(255),
        primary key (id)
    );

    create table RosterEntry (
        id bigint not null auto_increment,
        history_changedat datetime,
        history_changedby varchar(255) not null,
        history_createdat datetime,
        history_createdby varchar(255) not null,
        notes longtext,
        plannedenddatetime datetime not null,
        plannedstartdatetime datetime not null,
        realenddatetime datetime,
        realstartdatetime datetime,
        specialservice bit,
        standby bit,
        state varchar(255) not null,
        assignment_id bigint not null,
        car_id bigint,
        location_id bigint not null,
        servicetype_id bigint not null,
        systemuser_id bigint not null,
        primary key (id)
    );

    create table SecuredAction (
        id bigint not null auto_increment,
        history_changedat datetime,
        history_changedby varchar(255) not null,
        history_createdat datetime,
        history_createdby varchar(255) not null,
        accessexpression varchar(255) not null,
        description varchar(255),
        name varchar(255) not null unique,
        primary key (id)
    );

    create table SecuredResource (
        id bigint not null auto_increment,
        history_changedat datetime,
        history_changedby varchar(255) not null,
        history_createdat datetime,
        history_createdby varchar(255) not null,
        access varchar(255) not null,
        resource varchar(255) not null unique,
        primary key (id)
    );

    create table ServiceType (
        id bigint not null auto_increment,
        history_changedat datetime,
        history_changedby varchar(255) not null,
        history_createdat datetime,
        history_createdby varchar(255) not null,
        description varchar(255),
        name varchar(255) not null unique,
        shortname varchar(255),
        primary key (id)
    );

    create table SystemUser (
        id bigint not null auto_increment,
        history_changedat datetime,
        history_changedby varchar(255) not null,
        history_createdat datetime,
        history_createdby varchar(255) not null,
        address_city varchar(255),
        address_email varchar(255) not null unique,
        address_phone varchar(255),
        address_phone2 varchar(255),
        address_street varchar(255),
        address_zipcode varchar(255),
        birthday date,
        firstname varchar(255) not null,
        gender varchar(255) not null,
        lastname varchar(255) not null,
        notes longtext,
        pnr integer unique,
        todelete bit,
        location_id bigint not null,
        login_id bigint not null,
        primary key (id),
        unique (login_id)
    );

    create table SystemUser_Competence (
        SystemUser_id bigint not null,
        competences_id bigint not null
    );

    create table SystemUser_UserGroup (
        SystemUser_id bigint not null,
        groups_id bigint not null
    );

    create table UserGroup (
        id bigint not null auto_increment,
        history_changedat datetime,
        history_changedby varchar(255) not null,
        history_createdat datetime,
        history_createdby varchar(255) not null,
        description varchar(255),
        name varchar(255) not null unique,
        primary key (id)
    );

    alter table Car 
        add index FK107B4716F1CD8 (location_id), 
        add constraint FK107B4716F1CD8 
        foreign key (location_id) 
        references Location (id);

    alter table Car 
        add index FK107B44CC82590 (detail_id), 
        add constraint FK107B44CC82590 
        foreign key (detail_id) 
        references CarDetail (id);

    alter table CarCareEntry 
        add index FK9FF5628D1D301F3C (car_id), 
        add constraint FK9FF5628D1D301F3C 
        foreign key (car_id) 
        references Car (id);

    alter table Info 
        add index FK22D8CE716F1CD8 (location_id), 
        add constraint FK22D8CE716F1CD8 
        foreign key (location_id) 
        references Location (id);

    alter table Info 
        add index FK22D8CE3DDF2C38 (category_id), 
        add constraint FK22D8CE3DDF2C38 
        foreign key (category_id) 
        references Category (id);

    alter table RosterEntry 
        add index FKEF3D7087716F1CD8 (location_id), 
        add constraint FKEF3D7087716F1CD8 
        foreign key (location_id) 
        references Location (id);

    alter table RosterEntry 
        add index FKEF3D70878AB8CE58 (assignment_id), 
        add constraint FKEF3D70878AB8CE58 
        foreign key (assignment_id) 
        references Assignment (id);

    alter table RosterEntry 
        add index FKEF3D708714938758 (systemuser_id), 
        add constraint FKEF3D708714938758 
        foreign key (systemuser_id) 
        references SystemUser (id);

    alter table RosterEntry 
        add index FKEF3D7087C62A077C (servicetype_id), 
        add constraint FKEF3D7087C62A077C 
        foreign key (servicetype_id) 
        references ServiceType (id);

    alter table RosterEntry 
        add index FKEF3D70871D301F3C (car_id), 
        add constraint FKEF3D70871D301F3C 
        foreign key (car_id) 
        references Car (id);

    alter table SystemUser 
        add index FK9D23FEBAB76029C (login_id), 
        add constraint FK9D23FEBAB76029C 
        foreign key (login_id) 
        references Login (id);

    alter table SystemUser 
        add index FK9D23FEBA716F1CD8 (location_id), 
        add constraint FK9D23FEBA716F1CD8 
        foreign key (location_id) 
        references Location (id);

    alter table SystemUser_Competence 
        add index FK446C328E14938758 (SystemUser_id), 
        add constraint FK446C328E14938758 
        foreign key (SystemUser_id) 
        references SystemUser (id);

    alter table SystemUser_Competence 
        add index FK446C328ED9A3C737 (competences_id), 
        add constraint FK446C328ED9A3C737 
        foreign key (competences_id) 
        references Competence (id);

    alter table SystemUser_UserGroup 
        add index FK16853A0F3A498367 (groups_id), 
        add constraint FK16853A0F3A498367 
        foreign key (groups_id) 
        references UserGroup (id);

    alter table SystemUser_UserGroup 
        add index FK16853A0F14938758 (SystemUser_id), 
        add constraint FK16853A0F14938758 
        foreign key (SystemUser_id) 
        references SystemUser (id);
