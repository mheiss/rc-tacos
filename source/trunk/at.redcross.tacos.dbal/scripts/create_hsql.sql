
    create table Assignment (
        id bigint generated by default as identity (start with 1),
        history_changedat timestamp,
        history_changedby varchar(255) not null,
        history_createdat timestamp,
        history_createdby varchar(255) not null,
        description varchar(255),
        name varchar(255) not null,
        shortname varchar(255),
        primary key (id)
    );

    create table Car (
        id bigint generated by default as identity (start with 1),
        history_changedat timestamp,
        history_changedby varchar(255) not null,
        history_createdat timestamp,
        history_createdby varchar(255) not null,
        name varchar(255) not null,
        notes varchar(255),
        outoforder bit,
        registrationdate timestamp,
        todelete bit,
        type varchar(255),
        location_id bigint,
        primary key (id),
        unique (name)
    );

    create table Category (
        id bigint generated by default as identity (start with 1),
        history_changedat timestamp,
        history_changedby varchar(255) not null,
        history_createdat timestamp,
        history_createdby varchar(255) not null,
        description varchar(255),
        name varchar(255) not null,
        primary key (id)
    );

    create table Competence (
        id bigint generated by default as identity (start with 1),
        history_changedat timestamp,
        history_changedby varchar(255) not null,
        history_createdat timestamp,
        history_createdby varchar(255) not null,
        description varchar(255),
        name varchar(255) not null,
        shortname varchar(255),
        primary key (id),
        unique (name)
    );

    create table District (
        id bigint generated by default as identity (start with 1),
        history_changedat timestamp,
        history_changedby varchar(255) not null,
        history_createdat timestamp,
        history_createdby varchar(255) not null,
        name varchar(255) not null,
        shortname varchar(255),
        primary key (id),
        unique (name)
    );

    create table Info (
        id bigint generated by default as identity (start with 1),
        history_changedat timestamp,
        history_changedby varchar(255) not null,
        history_createdat timestamp,
        history_createdby varchar(255) not null,
        description varchar(255),
        displayenddate date not null,
        displaystartdate date not null,
        shortname varchar(255),
        todelete bit,
        category_id bigint,
        location_id bigint,
        primary key (id)
    );

    create table Link (
        id bigint generated by default as identity (start with 1),
        history_changedat timestamp,
        history_changedby varchar(255) not null,
        history_createdat timestamp,
        history_createdby varchar(255) not null,
        description varchar(255),
        name varchar(255) not null,
        primary key (id),
        unique (name)
    );

    create table Location (
        id bigint generated by default as identity (start with 1),
        history_changedat timestamp,
        history_changedby varchar(255) not null,
        history_createdat timestamp,
        history_createdby varchar(255) not null,
        name varchar(255) not null,
        shortname varchar(255),
        district_id bigint,
        District_Fk bigint,
        primary key (id),
        unique (name)
    );

    create table Login (
        id bigint generated by default as identity (start with 1),
        history_changedat timestamp,
        history_changedby varchar(255) not null,
        history_createdat timestamp,
        history_createdby varchar(255) not null,
        expireat date,
        locked bit,
        loginname varchar(255) not null,
        password varchar(255),
        superuser bit,
        systemuser_id bigint,
        primary key (id),
        unique (loginname)
    );

    create table Notification (
        id bigint generated by default as identity (start with 1),
        history_changedat timestamp,
        history_changedby varchar(255) not null,
        history_createdat timestamp,
        history_createdby varchar(255) not null,
        entrydate date not null,
        notes varchar(255),
        primary key (id),
        unique (entrydate)
    );

    create table RosterEntry (
        id bigint generated by default as identity (start with 1),
        history_changedat timestamp,
        history_changedby varchar(255) not null,
        history_createdat timestamp,
        history_createdby varchar(255) not null,
        notes varchar(255),
        plannedenddate date not null,
        plannedendtime time not null,
        plannedstartdate date not null,
        plannedstarttime time not null,
        realenddate date,
        realendtime time,
        realstartdate date,
        realstarttime time,
        specialservice bit,
        standby bit,
        todelete bit,
        assignment_id bigint,
        car_id bigint,
        location_id bigint,
        servicetype_id bigint,
        systemuser_id bigint,
        primary key (id)
    );

    create table SecuredAction (
        id bigint generated by default as identity (start with 1),
        history_changedat timestamp,
        history_changedby varchar(255) not null,
        history_createdat timestamp,
        history_createdby varchar(255) not null,
        accessexpression varchar(255) not null,
        description varchar(255),
        name varchar(255) not null,
        primary key (id),
        unique (name)
    );

    create table SecuredResource (
        id bigint generated by default as identity (start with 1),
        history_changedat timestamp,
        history_changedby varchar(255) not null,
        history_createdat timestamp,
        history_createdby varchar(255) not null,
        access varchar(255) not null,
        resource varchar(255) not null,
        primary key (id),
        unique (resource)
    );

    create table ServiceType (
        id bigint generated by default as identity (start with 1),
        history_changedat timestamp,
        history_changedby varchar(255) not null,
        history_createdat timestamp,
        history_createdby varchar(255) not null,
        description varchar(255),
        name varchar(255) not null,
        shortname varchar(255),
        primary key (id),
        unique (name)
    );

    create table SystemUser (
        id bigint generated by default as identity (start with 1),
        history_changedat timestamp,
        history_changedby varchar(255) not null,
        history_createdat timestamp,
        history_createdby varchar(255) not null,
        address_city varchar(255),
        address_email varchar(255) not null,
        address_phone varchar(255),
        address_phone2 varchar(255),
        address_street varchar(255),
        address_zipcode varchar(255),
        birthday date,
        firstname varchar(255) not null,
        gender varchar(255) not null,
        lastname varchar(255) not null,
        notes varchar(255),
        pnr integer,
        todelete bit,
        location_id bigint,
        primary key (id),
        unique (pnr),
        unique (address_email)
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
        id bigint generated by default as identity (start with 1),
        history_changedat timestamp,
        history_changedby varchar(255) not null,
        history_createdat timestamp,
        history_createdby varchar(255) not null,
        description varchar(255),
        name varchar(255) not null,
        primary key (id),
        unique (name)
    );

    alter table Car 
        add constraint FK107B4716F1CD8 
        foreign key (location_id) 
        references Location;

    alter table Info 
        add constraint FK22D8CE716F1CD8 
        foreign key (location_id) 
        references Location;

    alter table Info 
        add constraint FK22D8CE3DDF2C38 
        foreign key (category_id) 
        references Category;

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
