
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

    create table Assignment_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat datetime,
        history_changedby varchar(255),
        history_createdat datetime,
        history_createdby varchar(255),
        description varchar(255),
        name varchar(255),
        shortname varchar(255),
        primary key (id, REV)
    );

    create table Car (
        id bigint not null auto_increment,
        history_changedat datetime,
        history_changedby varchar(255) not null,
        history_createdat datetime,
        history_createdby varchar(255) not null,
        inspectiondate datetime,
        licencenumber varchar(255),
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
        executedon datetime,
        status varchar(255),
        type varchar(255),
        car_id bigint,
        primary key (id)
    );

    create table CarCareEntry_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat datetime,
        history_changedby varchar(255),
        history_createdat datetime,
        history_createdby varchar(255),
        description varchar(255),
        donefrom varchar(255),
        executedon datetime,
        status varchar(255),
        type varchar(255),
        car_id bigint,
        primary key (id, REV)
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

    create table CarDetail_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat datetime,
        history_changedby varchar(255),
        history_createdat datetime,
        history_createdby varchar(255),
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
        primary key (id, REV)
    );

    create table Car_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat datetime,
        history_changedby varchar(255),
        history_createdat datetime,
        history_createdby varchar(255),
        inspectiondate datetime,
        licencenumber varchar(255),
        name varchar(255),
        notes longtext,
        outoforder bit,
        registrationdate datetime,
        todelete bit,
        type varchar(255),
        detail_id bigint,
        location_id bigint,
        primary key (id, REV)
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

    create table Category_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat datetime,
        history_changedby varchar(255),
        history_createdat datetime,
        history_createdby varchar(255),
        description varchar(255),
        name varchar(255),
        primary key (id, REV)
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

    create table Competence_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat datetime,
        history_changedby varchar(255),
        history_createdat datetime,
        history_createdby varchar(255),
        description varchar(255),
        name varchar(255),
        shortname varchar(255),
        primary key (id, REV)
    );

    create table Equipment (
        id bigint not null auto_increment,
        history_changedat datetime,
        history_changedby varchar(255) not null,
        history_createdat datetime,
        history_createdby varchar(255) not null,
        actualnumber integer,
        expirationdate datetime,
        inventorynumber integer,
        lastinventorydate datetime,
        name varchar(255) not null,
        notes longtext,
        theoreticalnumber integer,
        todelete bit,
        type varchar(255),
        primary key (id)
    );

    create table Equipment_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat datetime,
        history_changedby varchar(255),
        history_createdat datetime,
        history_createdby varchar(255),
        actualnumber integer,
        expirationdate datetime,
        inventorynumber integer,
        lastinventorydate datetime,
        name varchar(255),
        notes longtext,
        theoreticalnumber integer,
        todelete bit,
        type varchar(255),
        primary key (id, REV)
    );

    create table FilterRule (
        id bigint not null auto_increment,
        history_changedat datetime,
        history_changedby varchar(255) not null,
        history_createdat datetime,
        history_createdby varchar(255) not null,
        description longtext,
        descriptiontemplate longtext,
        name varchar(255),
        primary key (id)
    );

    create table FilterRuleParam (
        id bigint not null auto_increment,
        history_changedat datetime,
        history_changedby varchar(255) not null,
        history_createdat datetime,
        history_createdby varchar(255) not null,
        description longtext,
        name varchar(255),
        value longtext,
        primary key (id)
    );

    create table FilterRuleParam_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat datetime,
        history_changedby varchar(255),
        history_createdat datetime,
        history_createdby varchar(255),
        description longtext,
        name varchar(255),
        value longtext,
        primary key (id, REV)
    );

    create table FilterRule_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat datetime,
        history_changedby varchar(255),
        history_createdat datetime,
        history_createdby varchar(255),
        description longtext,
        descriptiontemplate longtext,
        name varchar(255),
        primary key (id, REV)
    );

    create table FilterRule_FilterRuleParam (
        FilterRule_id bigint not null,
        params_id bigint not null
    );

    create table FilterRule_FilterRuleParam_AUD (
        REV integer not null,
        FilterRule_id bigint not null,
        params_id bigint not null,
        revtype tinyint,
        primary key (REV, FilterRule_id, params_id)
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

    create table Info_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat datetime,
        history_changedby varchar(255),
        history_createdat datetime,
        history_createdby varchar(255),
        description longtext,
        displayenddate date,
        displaystartdate date,
        shortname varchar(255),
        todelete bit,
        category_id bigint,
        location_id bigint,
        primary key (id, REV)
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

    create table Link_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat datetime,
        history_changedby varchar(255),
        history_createdat datetime,
        history_createdby varchar(255),
        description varchar(255),
        name varchar(255),
        primary key (id, REV)
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

    create table Location_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat datetime,
        history_changedby varchar(255),
        history_createdat datetime,
        history_createdby varchar(255),
        name varchar(255),
        shortname varchar(255),
        primary key (id, REV)
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

    create table Login_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat datetime,
        history_changedby varchar(255),
        history_createdat datetime,
        history_createdby varchar(255),
        expireat date,
        locked bit,
        loginname varchar(255),
        password varchar(255),
        superuser bit,
        primary key (id, REV)
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

    create table Notification_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat datetime,
        history_changedby varchar(255),
        history_createdat datetime,
        history_createdby varchar(255),
        entrydate date,
        notes longtext,
        primary key (id, REV)
    );

    create table REVINFO (
        REV integer not null auto_increment,
        REVTSTMP bigint,
        primary key (REV)
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

    create table RestoreLogin_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat datetime,
        history_changedby varchar(255),
        history_createdat datetime,
        history_createdby varchar(255),
        expireat datetime,
        token varchar(255),
        username varchar(255),
        primary key (id, REV)
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

    create table RosterEntry_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat datetime,
        history_changedby varchar(255),
        history_createdat datetime,
        history_createdby varchar(255),
        notes longtext,
        plannedenddatetime datetime,
        plannedstartdatetime datetime,
        realenddatetime datetime,
        realstartdatetime datetime,
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

    create table SecuredAction_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat datetime,
        history_changedby varchar(255),
        history_createdat datetime,
        history_createdby varchar(255),
        accessexpression varchar(255),
        description varchar(255),
        name varchar(255),
        primary key (id, REV)
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

    create table SecuredResource_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat datetime,
        history_changedby varchar(255),
        history_createdat datetime,
        history_createdby varchar(255),
        access varchar(255),
        resource varchar(255),
        primary key (id, REV)
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
        signinout bit,
        primary key (id)
    );

    create table ServiceType_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat datetime,
        history_changedby varchar(255),
        history_createdat datetime,
        history_createdby varchar(255),
        description varchar(255),
        name varchar(255),
        shortname varchar(255),
        signinout bit,
        primary key (id, REV)
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

    create table SystemUser_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat datetime,
        history_changedby varchar(255),
        history_createdat datetime,
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
        notes longtext,
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
        id bigint not null auto_increment,
        history_changedat datetime,
        history_changedby varchar(255) not null,
        history_createdat datetime,
        history_createdby varchar(255) not null,
        description varchar(255),
        name varchar(255) not null unique,
        primary key (id)
    );

    create table UserGroup_AUD (
        id bigint not null,
        REV integer not null,
        revtype tinyint,
        history_changedat datetime,
        history_changedby varchar(255),
        history_createdat datetime,
        history_createdby varchar(255),
        description varchar(255),
        name varchar(255),
        primary key (id, REV)
    );

    alter table Assignment_AUD 
        add index FKDFB4523EDF74E053 (REV), 
        add constraint FKDFB4523EDF74E053 
        foreign key (REV) 
        references REVINFO (REV);

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

    alter table CarCareEntry_AUD 
        add index FKA6381DEDF74E053 (REV), 
        add constraint FKA6381DEDF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table CarDetail_AUD 
        add index FKDD838876DF74E053 (REV), 
        add constraint FKDD838876DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table Car_AUD 
        add index FK843A3B85DF74E053 (REV), 
        add constraint FK843A3B85DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table Category_AUD 
        add index FK23378FEFDF74E053 (REV), 
        add constraint FK23378FEFDF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table Competence_AUD 
        add index FKE7F8853ADF74E053 (REV), 
        add constraint FKE7F8853ADF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table Equipment_AUD 
        add index FKA749CC3FDF74E053 (REV), 
        add constraint FKA749CC3FDF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table FilterRuleParam_AUD 
        add index FK2A0E6B8ADF74E053 (REV), 
        add constraint FK2A0E6B8ADF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table FilterRule_AUD 
        add index FKE5C8EA85DF74E053 (REV), 
        add constraint FKE5C8EA85DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table FilterRule_FilterRuleParam 
        add index FKD034A6AE61A5C7CF (params_id), 
        add constraint FKD034A6AE61A5C7CF 
        foreign key (params_id) 
        references FilterRuleParam (id);

    alter table FilterRule_FilterRuleParam 
        add index FKD034A6AE5425CD18 (FilterRule_id), 
        add constraint FKD034A6AE5425CD18 
        foreign key (FilterRule_id) 
        references FilterRule (id);

    alter table FilterRule_FilterRuleParam_AUD 
        add index FKC331CD7FDF74E053 (REV), 
        add constraint FKC331CD7FDF74E053 
        foreign key (REV) 
        references REVINFO (REV);

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

    alter table Info_AUD 
        add index FKE79EF9FDF74E053 (REV), 
        add constraint FKE79EF9FDF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table Link_AUD 
        add index FK4B0CB4EBDF74E053 (REV), 
        add constraint FK4B0CB4EBDF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table Location_AUD 
        add index FK6563F26DF74E053 (REV), 
        add constraint FK6563F26DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table Login_AUD 
        add index FK10FC609ADF74E053 (REV), 
        add constraint FK10FC609ADF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table Notification_AUD 
        add index FK2DD68D5CDF74E053 (REV), 
        add constraint FK2DD68D5CDF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table RestoreLogin_AUD 
        add index FK541ED1ECDF74E053 (REV), 
        add constraint FK541ED1ECDF74E053 
        foreign key (REV) 
        references REVINFO (REV);

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

    alter table RosterEntry_AUD 
        add index FK3AA002D8DF74E053 (REV), 
        add constraint FK3AA002D8DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table SecuredAction_AUD 
        add index FK6DD33494DF74E053 (REV), 
        add constraint FK6DD33494DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table SecuredResource_AUD 
        add index FKC3721F2CDF74E053 (REV), 
        add constraint FKC3721F2CDF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table ServiceType_AUD 
        add index FKE5A1EDC0DF74E053 (REV), 
        add constraint FKE5A1EDC0DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

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

    alter table SystemUser_AUD 
        add index FK595E3F8BDF74E053 (REV), 
        add constraint FK595E3F8BDF74E053 
        foreign key (REV) 
        references REVINFO (REV);

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

    alter table SystemUser_Competence_AUD 
        add index FKF700695FDF74E053 (REV), 
        add constraint FKF700695FDF74E053 
        foreign key (REV) 
        references REVINFO (REV);

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

    alter table SystemUser_UserGroup_AUD 
        add index FK7E57C860DF74E053 (REV), 
        add constraint FK7E57C860DF74E053 
        foreign key (REV) 
        references REVINFO (REV);

    alter table UserGroup_AUD 
        add index FK4C4F4525DF74E053 (REV), 
        add constraint FK4C4F4525DF74E053 
        foreign key (REV) 
        references REVINFO (REV);
