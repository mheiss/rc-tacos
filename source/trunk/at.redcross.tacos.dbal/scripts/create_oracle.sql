
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

    create table Assignment_AUD (
        id number(19,0) not null,
        REV number(10,0) not null,
        revtype number(3,0),
        history_changedat timestamp,
        history_changedby varchar2(255 char),
        history_createdat timestamp,
        history_createdby varchar2(255 char),
        description varchar2(255 char),
        name varchar2(255 char),
        shortname varchar2(255 char),
        primary key (id, REV)
    );

    create table Car (
        id number(19,0) not null,
        history_changedat timestamp,
        history_changedby varchar2(255 char) not null,
        history_createdat timestamp,
        history_createdby varchar2(255 char) not null,
        inspectiondate timestamp,
        licencenumber varchar2(255 char),
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
        executedon timestamp,
        status varchar2(255 char),
        type varchar2(255 char),
        car_id number(19,0),
        primary key (id)
    );

    create table CarCareEntry_AUD (
        id number(19,0) not null,
        REV number(10,0) not null,
        revtype number(3,0),
        history_changedat timestamp,
        history_changedby varchar2(255 char),
        history_createdat timestamp,
        history_createdby varchar2(255 char),
        description varchar2(255 char),
        donefrom varchar2(255 char),
        executedon timestamp,
        status varchar2(255 char),
        type varchar2(255 char),
        car_id number(19,0),
        primary key (id, REV)
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

    create table CarDetail_AUD (
        id number(19,0) not null,
        REV number(10,0) not null,
        revtype number(3,0),
        history_changedat timestamp,
        history_changedby varchar2(255 char),
        history_createdat timestamp,
        history_createdby varchar2(255 char),
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
        primary key (id, REV)
    );

    create table Car_AUD (
        id number(19,0) not null,
        REV number(10,0) not null,
        revtype number(3,0),
        history_changedat timestamp,
        history_changedby varchar2(255 char),
        history_createdat timestamp,
        history_createdby varchar2(255 char),
        inspectiondate timestamp,
        licencenumber varchar2(255 char),
        name varchar2(255 char),
        notes varchar2(1024 char),
        outoforder number(1,0),
        registrationdate timestamp,
        todelete number(1,0),
        type varchar2(255 char),
        detail_id number(19,0),
        location_id number(19,0),
        primary key (id, REV)
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

    create table Category_AUD (
        id number(19,0) not null,
        REV number(10,0) not null,
        revtype number(3,0),
        history_changedat timestamp,
        history_changedby varchar2(255 char),
        history_createdat timestamp,
        history_createdby varchar2(255 char),
        description varchar2(255 char),
        name varchar2(255 char),
        primary key (id, REV)
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

    create table Competence_AUD (
        id number(19,0) not null,
        REV number(10,0) not null,
        revtype number(3,0),
        history_changedat timestamp,
        history_changedby varchar2(255 char),
        history_createdat timestamp,
        history_createdby varchar2(255 char),
        description varchar2(255 char),
        name varchar2(255 char),
        shortname varchar2(255 char),
        primary key (id, REV)
    );

    create table Equipment (
        id number(19,0) not null,
        history_changedat timestamp,
        history_changedby varchar2(255 char) not null,
        history_createdat timestamp,
        history_createdby varchar2(255 char) not null,
        actualnumber number(10,0),
        expirationdate timestamp,
        inventorynumber number(10,0),
        lastinventorydate timestamp,
        name varchar2(255 char) not null,
        notes varchar2(1024 char),
        theoreticalnumber number(10,0),
        todelete number(1,0),
        type varchar2(255 char),
        primary key (id)
    );

    create table Equipment_AUD (
        id number(19,0) not null,
        REV number(10,0) not null,
        revtype number(3,0),
        history_changedat timestamp,
        history_changedby varchar2(255 char),
        history_createdat timestamp,
        history_createdby varchar2(255 char),
        actualnumber number(10,0),
        expirationdate timestamp,
        inventorynumber number(10,0),
        lastinventorydate timestamp,
        name varchar2(255 char),
        notes varchar2(1024 char),
        theoreticalnumber number(10,0),
        todelete number(1,0),
        type varchar2(255 char),
        primary key (id, REV)
    );

    create table FilterRule (
        id number(19,0) not null,
        history_changedat timestamp,
        history_changedby varchar2(255 char) not null,
        history_createdat timestamp,
        history_createdby varchar2(255 char) not null,
        description varchar2(1024 char),
        descriptiontemplate varchar2(1024 char),
        name varchar2(255 char),
        primary key (id)
    );

    create table FilterRuleParam (
        id number(19,0) not null,
        history_changedat timestamp,
        history_changedby varchar2(255 char) not null,
        history_createdat timestamp,
        history_createdby varchar2(255 char) not null,
        description varchar2(1024 char),
        name varchar2(255 char),
        value varchar2(1024 char),
        primary key (id)
    );

    create table FilterRuleParam_AUD (
        id number(19,0) not null,
        REV number(10,0) not null,
        revtype number(3,0),
        history_changedat timestamp,
        history_changedby varchar2(255 char),
        history_createdat timestamp,
        history_createdby varchar2(255 char),
        description varchar2(1024 char),
        name varchar2(255 char),
        value varchar2(1024 char),
        primary key (id, REV)
    );

    create table FilterRule_AUD (
        id number(19,0) not null,
        REV number(10,0) not null,
        revtype number(3,0),
        history_changedat timestamp,
        history_changedby varchar2(255 char),
        history_createdat timestamp,
        history_createdby varchar2(255 char),
        description varchar2(1024 char),
        descriptiontemplate varchar2(1024 char),
        name varchar2(255 char),
        primary key (id, REV)
    );

    create table FilterRule_FilterRuleParam (
        FilterRule_id number(19,0) not null,
        params_id number(19,0) not null
    );

    create table FilterRule_FilterRuleParam_AUD (
        REV number(10,0) not null,
        FilterRule_id number(19,0) not null,
        params_id number(19,0) not null,
        revtype number(3,0),
        primary key (REV, FilterRule_id, params_id)
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

    create table Info_AUD (
        id number(19,0) not null,
        REV number(10,0) not null,
        revtype number(3,0),
        history_changedat timestamp,
        history_changedby varchar2(255 char),
        history_createdat timestamp,
        history_createdby varchar2(255 char),
        description long,
        displayenddate date,
        displaystartdate date,
        shortname varchar2(255 char),
        todelete number(1,0),
        category_id number(19,0),
        location_id number(19,0),
        primary key (id, REV)
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

    create table Link_AUD (
        id number(19,0) not null,
        REV number(10,0) not null,
        revtype number(3,0),
        history_changedat timestamp,
        history_changedby varchar2(255 char),
        history_createdat timestamp,
        history_createdby varchar2(255 char),
        description varchar2(255 char),
        name varchar2(255 char),
        primary key (id, REV)
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

    create table Location_AUD (
        id number(19,0) not null,
        REV number(10,0) not null,
        revtype number(3,0),
        history_changedat timestamp,
        history_changedby varchar2(255 char),
        history_createdat timestamp,
        history_createdby varchar2(255 char),
        name varchar2(255 char),
        shortname varchar2(255 char),
        primary key (id, REV)
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

    create table Login_AUD (
        id number(19,0) not null,
        REV number(10,0) not null,
        revtype number(3,0),
        history_changedat timestamp,
        history_changedby varchar2(255 char),
        history_createdat timestamp,
        history_createdby varchar2(255 char),
        expireat date,
        locked number(1,0),
        loginname varchar2(255 char),
        password varchar2(255 char),
        superuser number(1,0),
        primary key (id, REV)
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

    create table Notification_AUD (
        id number(19,0) not null,
        REV number(10,0) not null,
        revtype number(3,0),
        history_changedat timestamp,
        history_changedby varchar2(255 char),
        history_createdat timestamp,
        history_createdby varchar2(255 char),
        entrydate date,
        notes long,
        primary key (id, REV)
    );

    create table REVINFO (
        REV number(10,0) not null,
        REVTSTMP number(19,0),
        primary key (REV)
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

    create table RestoreLogin_AUD (
        id number(19,0) not null,
        REV number(10,0) not null,
        revtype number(3,0),
        history_changedat timestamp,
        history_changedby varchar2(255 char),
        history_createdat timestamp,
        history_createdby varchar2(255 char),
        expireat timestamp,
        token varchar2(255 char),
        username varchar2(255 char),
        primary key (id, REV)
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

    create table RosterEntry_AUD (
        id number(19,0) not null,
        REV number(10,0) not null,
        revtype number(3,0),
        history_changedat timestamp,
        history_changedby varchar2(255 char),
        history_createdat timestamp,
        history_createdby varchar2(255 char),
        notes varchar2(1024 char),
        plannedenddatetime timestamp,
        plannedstartdatetime timestamp,
        realenddatetime timestamp,
        realstartdatetime timestamp,
        specialservice number(1,0),
        standby number(1,0),
        state varchar2(255 char),
        assignment_id number(19,0),
        car_id number(19,0),
        location_id number(19,0),
        servicetype_id number(19,0),
        systemuser_id number(19,0),
        primary key (id, REV)
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

    create table SecuredAction_AUD (
        id number(19,0) not null,
        REV number(10,0) not null,
        revtype number(3,0),
        history_changedat timestamp,
        history_changedby varchar2(255 char),
        history_createdat timestamp,
        history_createdby varchar2(255 char),
        accessexpression varchar2(255 char),
        description varchar2(255 char),
        name varchar2(255 char),
        primary key (id, REV)
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

    create table SecuredResource_AUD (
        id number(19,0) not null,
        REV number(10,0) not null,
        revtype number(3,0),
        history_changedat timestamp,
        history_changedby varchar2(255 char),
        history_createdat timestamp,
        history_createdby varchar2(255 char),
        access varchar2(255 char),
        resource varchar2(255 char),
        primary key (id, REV)
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
        signinout number(1,0),
        primary key (id)
    );

    create table ServiceType_AUD (
        id number(19,0) not null,
        REV number(10,0) not null,
        revtype number(3,0),
        history_changedat timestamp,
        history_changedby varchar2(255 char),
        history_createdat timestamp,
        history_createdby varchar2(255 char),
        description varchar2(255 char),
        name varchar2(255 char),
        shortname varchar2(255 char),
        signinout number(1,0),
        primary key (id, REV)
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

    create table SystemUser_AUD (
        id number(19,0) not null,
        REV number(10,0) not null,
        revtype number(3,0),
        history_changedat timestamp,
        history_changedby varchar2(255 char),
        history_createdat timestamp,
        history_createdby varchar2(255 char),
        address_city varchar2(255 char),
        address_email varchar2(255 char),
        address_phone varchar2(255 char),
        address_phone2 varchar2(255 char),
        address_street varchar2(255 char),
        address_zipcode varchar2(255 char),
        birthday date,
        firstname varchar2(255 char),
        gender varchar2(255 char),
        lastname varchar2(255 char),
        notes varchar2(1024 char),
        pnr number(10,0),
        todelete number(1,0),
        location_id number(19,0),
        login_id number(19,0),
        primary key (id, REV)
    );

    create table SystemUser_Competence (
        SystemUser_id number(19,0) not null,
        competences_id number(19,0) not null
    );

    create table SystemUser_Competence_AUD (
        REV number(10,0) not null,
        SystemUser_id number(19,0) not null,
        competences_id number(19,0) not null,
        revtype number(3,0),
        primary key (REV, SystemUser_id, competences_id)
    );

    create table SystemUser_UserGroup (
        SystemUser_id number(19,0) not null,
        groups_id number(19,0) not null
    );

    create table SystemUser_UserGroup_AUD (
        REV number(10,0) not null,
        SystemUser_id number(19,0) not null,
        groups_id number(19,0) not null,
        revtype number(3,0),
        primary key (REV, SystemUser_id, groups_id)
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

    create table UserGroup_AUD (
        id number(19,0) not null,
        REV number(10,0) not null,
        revtype number(3,0),
        history_changedat timestamp,
        history_changedby varchar2(255 char),
        history_createdat timestamp,
        history_createdby varchar2(255 char),
        description varchar2(255 char),
        name varchar2(255 char),
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

    alter table Equipment_AUD 
        add constraint FKA749CC3FDF74E053 
        foreign key (REV) 
        references REVINFO;

    alter table FilterRuleParam_AUD 
        add constraint FK2A0E6B8ADF74E053 
        foreign key (REV) 
        references REVINFO;

    alter table FilterRule_AUD 
        add constraint FKE5C8EA85DF74E053 
        foreign key (REV) 
        references REVINFO;

    alter table FilterRule_FilterRuleParam 
        add constraint FKD034A6AE61A5C7CF 
        foreign key (params_id) 
        references FilterRuleParam;

    alter table FilterRule_FilterRuleParam 
        add constraint FKD034A6AE5425CD18 
        foreign key (FilterRule_id) 
        references FilterRule;

    alter table FilterRule_FilterRuleParam_AUD 
        add constraint FKC331CD7FDF74E053 
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

    create sequence hibernate_sequence;
