
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
        notes varchar(255),
        outoforder bit,
        todelete bit,
        location_id bigint,
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

    create table District (
        id bigint not null auto_increment,
        history_changedat datetime,
        history_changedby varchar(255) not null,
        history_createdat datetime,
        history_createdby varchar(255) not null,
        name varchar(255) not null unique,
        shortname varchar(255),
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
        district_id bigint,
        District_Fk bigint,
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
        password varchar(255),
        systemuser_id bigint,
        primary key (id)
    );

    create table RosterEntry (
        id bigint not null auto_increment,
        history_changedat datetime,
        history_changedby varchar(255) not null,
        history_createdat datetime,
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
        notes varchar(255),
        pnr integer unique,
        todelete bit,
        location_id bigint,
        primary key (id)
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

    alter table Location 
        add index FK752A03D54004E1E2 (District_Fk), 
        add constraint FK752A03D54004E1E2 
        foreign key (District_Fk) 
        references District (id);

    alter table Location 
        add index FK752A03D54004E238 (district_id), 
        add constraint FK752A03D54004E238 
        foreign key (district_id) 
        references District (id);

    alter table Login 
        add index FK462FF4914938758 (systemuser_id), 
        add constraint FK462FF4914938758 
        foreign key (systemuser_id) 
        references SystemUser (id);

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
