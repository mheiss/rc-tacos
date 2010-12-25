
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

    create table Assignment_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat timestamp,
        history_changedby varchar(255),
        history_createdat timestamp,
        history_createdby varchar(255),
        description varchar(255),
        name varchar(255),
        shortname varchar(255),
        primary key (id, REV)
    );

    create table Car (
        id bigint generated by default as identity (start with 1),
        history_changedat timestamp,
        history_changedby varchar(255) not null,
        history_createdat timestamp,
        history_createdby varchar(255) not null,
        name varchar(255) not null,
        notes varchar(1024),
        outoforder bit,
        registrationdate timestamp,
        todelete bit,
        type varchar(255),
        detail_id bigint,
        location_id bigint not null,
        primary key (id),
        unique (name)
    );

    create table CarCareEntry (
        id bigint generated by default as identity (start with 1),
        history_changedat timestamp,
        history_changedby varchar(255) not null,
        history_createdat timestamp,
        history_createdby varchar(255) not null,
        description varchar(255),
        donefrom varchar(255),
        executeuntil timestamp,
        executedon timestamp,
        responsible varchar(255),
        status varchar(255),
        type varchar(255),
        car_id bigint,
        primary key (id)
    );

    create table CarCareEntry_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat timestamp,
        history_changedby varchar(255),
        history_createdat timestamp,
        history_createdby varchar(255),
        description varchar(255),
        donefrom varchar(255),
        executeuntil timestamp,
        executedon timestamp,
        responsible varchar(255),
        status varchar(255),
        type varchar(255),
        car_id bigint,
        primary key (id, REV)
    );

    create table CarDetail (
        id bigint generated by default as identity (start with 1),
        history_changedat timestamp,
        history_changedby varchar(255) not null,
        history_createdat timestamp,
        history_createdby varchar(255) not null,
        authorizedfrom timestamp,
        authorizeduntil timestamp,
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

    create table CarDetail_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat timestamp,
        history_changedby varchar(255),
        history_createdat timestamp,
        history_createdby varchar(255),
        authorizedfrom timestamp,
        authorizeduntil timestamp,
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
        primary key (id, REV)
    );

    create table Car_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat timestamp,
        history_changedby varchar(255),
        history_createdat timestamp,
        history_createdby varchar(255),
        name varchar(255),
        notes varchar(1024),
        outoforder bit,
        registrationdate timestamp,
        todelete bit,
        type varchar(255),
        detail_id bigint,
        location_id bigint,
        primary key (id, REV)
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

    create table Category_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat timestamp,
        history_changedby varchar(255),
        history_createdat timestamp,
        history_createdby varchar(255),
        description varchar(255),
        name varchar(255),
        primary key (id, REV)
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

    create table Competence_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat timestamp,
        history_changedby varchar(255),
        history_createdat timestamp,
        history_createdby varchar(255),
        description varchar(255),
        name varchar(255),
        shortname varchar(255),
        primary key (id, REV)
    );

    create table Info (
        id bigint generated by default as identity (start with 1),
        history_changedat timestamp,
        history_changedby varchar(255) not null,
        history_createdat timestamp,
        history_createdby varchar(255) not null,
        description varchar(4096),
        displayenddate date not null,
        displaystartdate date not null,
        shortname varchar(255),
        todelete bit,
        category_id bigint not null,
        location_id bigint not null,
        primary key (id)
    );

    create table Info_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat timestamp,
        history_changedby varchar(255),
        history_createdat timestamp,
        history_createdby varchar(255),
        description varchar(4096),
        displayenddate date,
        displaystartdate date,
        shortname varchar(255),
        todelete bit,
        category_id bigint,
        location_id bigint,
        primary key (id, REV)
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

    create table Link_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat timestamp,
        history_changedby varchar(255),
        history_createdat timestamp,
        history_createdby varchar(255),
        description varchar(255),
        name varchar(255),
        primary key (id, REV)
    );

    create table Location (
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

    create table Location_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat timestamp,
        history_changedby varchar(255),
        history_createdat timestamp,
        history_createdby varchar(255),
        name varchar(255),
        shortname varchar(255),
        primary key (id, REV)
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
        password varchar(255) not null,
        superuser bit,
        primary key (id),
        unique (loginname)
    );

    create table Login_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat timestamp,
        history_changedby varchar(255),
        history_createdat timestamp,
        history_createdby varchar(255),
        expireat date,
        locked bit,
        loginname varchar(255),
        password varchar(255),
        superuser bit,
        primary key (id, REV)
    );

    create table Notification (
        id bigint generated by default as identity (start with 1),
        history_changedat timestamp,
        history_changedby varchar(255) not null,
        history_createdat timestamp,
        history_createdby varchar(255) not null,
        entrydate date not null,
        notes varchar(4096),
        primary key (id),
        unique (entrydate)
    );

    create table Notification_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat timestamp,
        history_changedby varchar(255),
        history_createdat timestamp,
        history_createdby varchar(255),
        entrydate date,
        notes varchar(4096),
        primary key (id, REV)
    );

    create table REVINFO (
        REV integer generated by default as identity (start with 1),
        REVTSTMP bigint,
        primary key (REV)
    );

    create table RestoreLogin (
        id bigint generated by default as identity (start with 1),
        history_changedat timestamp,
        history_changedby varchar(255) not null,
        history_createdat timestamp,
        history_createdby varchar(255) not null,
        expireat timestamp,
        token varchar(255) not null,
        username varchar(255) not null,
        primary key (id),
        unique (username)
    );

    create table RestoreLogin_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat timestamp,
        history_changedby varchar(255),
        history_createdat timestamp,
        history_createdby varchar(255),
        expireat timestamp,
        token varchar(255),
        username varchar(255),
        primary key (id, REV)
    );

    create table RosterEntry (
        id bigint generated by default as identity (start with 1),
        history_changedat timestamp,
        history_changedby varchar(255) not null,
        history_createdat timestamp,
        history_createdby varchar(255) not null,
        notes varchar(1024),
        plannedenddatetime timestamp not null,
        plannedstartdatetime timestamp not null,
        realenddatetime timestamp,
        realstartdatetime timestamp,
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

    create table RosterEntry_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat timestamp,
        history_changedby varchar(255),
        history_createdat timestamp,
        history_createdby varchar(255),
        notes varchar(1024),
        plannedenddatetime timestamp,
        plannedstartdatetime timestamp,
        realenddatetime timestamp,
        realstartdatetime timestamp,
        specialservice bit,
        standby bit,
        state varchar(255),
        assignment_id bigint,
        car_id bigint,
        location_id bigint,
        servicetype_id bigint,
        systemuser_id bigint,
        primary key (id, REV)
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

    create table SecuredAction_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat timestamp,
        history_changedby varchar(255),
        history_createdat timestamp,
        history_createdby varchar(255),
        accessexpression varchar(255),
        description varchar(255),
        name varchar(255),
        primary key (id, REV)
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

    create table SecuredResource_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat timestamp,
        history_changedby varchar(255),
        history_createdat timestamp,
        history_createdby varchar(255),
        access varchar(255),
        resource varchar(255),
        primary key (id, REV)
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
        signinout bit,
        primary key (id),
        unique (name)
    );

    create table ServiceType_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat timestamp,
        history_changedby varchar(255),
        history_createdat timestamp,
        history_createdby varchar(255),
        description varchar(255),
        name varchar(255),
        shortname varchar(255),
        signinout bit,
        primary key (id, REV)
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
        notes varchar(1024),
        pnr integer,
        todelete bit,
        location_id bigint not null,
        login_id bigint not null,
        primary key (id),
        unique (pnr),
        unique (login_id),
        unique (address_email)
    );

    create table SystemUser_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat timestamp,
        history_changedby varchar(255),
        history_createdat timestamp,
        history_createdby varchar(255),
        address_city varchar(255),
        address_email varchar(255),
        address_phone varchar(255),
        address_phone2 varchar(255),
        address_street varchar(255),
        address_zipcode varchar(255),
        birthday date,
        firstname varchar(255),
        gender varchar(255),
        lastname varchar(255),
        notes varchar(1024),
        pnr integer,
        todelete bit,
        location_id bigint,
        login_id bigint,
        primary key (id, REV)
    );

    create table SystemUser_Competence (
        SystemUser_id bigint not null,
        competences_id bigint not null
    );

    create table SystemUser_Competence_AUD (
        REV integer not null,
        SystemUser_id bigint not null,
        competences_id bigint not null,
        revtype tinyint,
        primary key (REV, SystemUser_id, competences_id)
    );

    create table SystemUser_UserGroup (
        SystemUser_id bigint not null,
        groups_id bigint not null
    );

    create table SystemUser_UserGroup_AUD (
        REV integer not null,
        SystemUser_id bigint not null,
        groups_id bigint not null,
        revtype tinyint,
        primary key (REV, SystemUser_id, groups_id)
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

    create table UserGroup_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat timestamp,
        history_changedby varchar(255),
        history_createdat timestamp,
        history_createdby varchar(255),
        description varchar(255),
        name varchar(255),
        primary key (id, REV)
    );

    alter table Assignment_AUD 
        add constraint FKDFB4523EDF74E053 
        foreign key (REV) 
        references REVINFO;

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

    alter table CarCareEntry_AUD 
        add constraint FKA6381DEDF74E053 
        foreign key (REV) 
        references REVINFO;

    alter table CarDetail_AUD 
        add constraint FKDD838876DF74E053 
        foreign key (REV) 
        references REVINFO;

    alter table Car_AUD 
        add constraint FK843A3B85DF74E053 
        foreign key (REV) 
        references REVINFO;

    alter table Category_AUD 
        add constraint FK23378FEFDF74E053 
        foreign key (REV) 
        references REVINFO;

    alter table Competence_AUD 
        add constraint FKE7F8853ADF74E053 
        foreign key (REV) 
        references REVINFO;

    alter table Info 
        add constraint FK22D8CE716F1CD8 
        foreign key (location_id) 
        references Location;

    alter table Info 
        add constraint FK22D8CE3DDF2C38 
        foreign key (category_id) 
        references Category;

    alter table Info_AUD 
        add constraint FKE79EF9FDF74E053 
        foreign key (REV) 
        references REVINFO;

    alter table Link_AUD 
        add constraint FK4B0CB4EBDF74E053 
        foreign key (REV) 
        references REVINFO;

    alter table Location_AUD 
        add constraint FK6563F26DF74E053 
        foreign key (REV) 
        references REVINFO;

    alter table Login_AUD 
        add constraint FK10FC609ADF74E053 
        foreign key (REV) 
        references REVINFO;

    alter table Notification_AUD 
        add constraint FK2DD68D5CDF74E053 
        foreign key (REV) 
        references REVINFO;

    alter table RestoreLogin_AUD 
        add constraint FK541ED1ECDF74E053 
        foreign key (REV) 
        references REVINFO;

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

    alter table RosterEntry_AUD 
        add constraint FK3AA002D8DF74E053 
        foreign key (REV) 
        references REVINFO;

    alter table SecuredAction_AUD 
        add constraint FK6DD33494DF74E053 
        foreign key (REV) 
        references REVINFO;

    alter table SecuredResource_AUD 
        add constraint FKC3721F2CDF74E053 
        foreign key (REV) 
        references REVINFO;

    alter table ServiceType_AUD 
        add constraint FKE5A1EDC0DF74E053 
        foreign key (REV) 
        references REVINFO;

    alter table SystemUser 
        add constraint FK9D23FEBAB76029C 
        foreign key (login_id) 
        references Login;

    alter table SystemUser 
        add constraint FK9D23FEBA716F1CD8 
        foreign key (location_id) 
        references Location;

    alter table SystemUser_AUD 
        add constraint FK595E3F8BDF74E053 
        foreign key (REV) 
        references REVINFO;

    alter table SystemUser_Competence 
        add constraint FK446C328E14938758 
        foreign key (SystemUser_id) 
        references SystemUser;

    alter table SystemUser_Competence 
        add constraint FK446C328ED9A3C737 
        foreign key (competences_id) 
        references Competence;

    alter table SystemUser_Competence_AUD 
        add constraint FKF700695FDF74E053 
        foreign key (REV) 
        references REVINFO;

    alter table SystemUser_UserGroup 
        add constraint FK16853A0F3A498367 
        foreign key (groups_id) 
        references UserGroup;

    alter table SystemUser_UserGroup 
        add constraint FK16853A0F14938758 
        foreign key (SystemUser_id) 
        references SystemUser;

    alter table SystemUser_UserGroup_AUD 
        add constraint FK7E57C860DF74E053 
        foreign key (REV) 
        references REVINFO;

    alter table UserGroup_AUD 
        add constraint FK4C4F4525DF74E053 
        foreign key (REV) 
        references REVINFO;
