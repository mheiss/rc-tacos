----------------------
-- Migration script --
----------------------

----------------------------------------------------------------
-- Changes from version 1 to version 2
----------------------------------------------------------------
	alter table Car add inspectiondate datetime null;
	alter table Car add licencenumber varchar(255) null;
	alter table Car_AUD add inspectiondate datetime null;
	alter table Car_AUD add licencenumber varchar(255) null;

	alter table CarCareEntry drop column executeuntil;
	alter table CarCareEntry drop column responsible;
	alter table CarCareEntry_AUD drop column executeuntil;
	alter table CarCareEntry_AUD drop column responsible;

---------------------------------------------------------------
-- Changes from version 2 to version 3
----------------------------------------------------------------
   create table FilterRule (
        id numeric(19,0) identity not null,
        history_changedat datetime null,
        history_changedby varchar(255) not null,
        history_createdat datetime null,
        history_createdby varchar(255) not null,
        description varchar(1024) null,
        descriptiontemplate varchar(1024) null,
        name varchar(255) null,
        primary key (id)
    );

    create table FilterRuleParam (
        id numeric(19,0) identity not null,
        history_changedat datetime null,
        history_changedby varchar(255) not null,
        history_createdat datetime null,
        history_createdby varchar(255) not null,
        description varchar(1024) null,
        name varchar(255) null,
        value varchar(1024) null,
        primary key (id)
    );

    create table FilterRuleParam_AUD (
        id numeric(19,0) not null,
        REV int not null,
        revtype tinyint null,
        history_changedat datetime null,
        history_changedby varchar(255) null,
        history_createdat datetime null,
        history_createdby varchar(255) null,
        description varchar(1024) null,
        name varchar(255) null,
        value varchar(1024) null,
        primary key (id, REV)
    );

    create table FilterRule_AUD (
        id numeric(19,0) not null,
        REV int not null,
        revtype tinyint null,
        history_changedat datetime null,
        history_changedby varchar(255) null,
        history_createdat datetime null,
        history_createdby varchar(255) null,
        description varchar(1024) null,
        descriptiontemplate varchar(1024) null,
        name varchar(255) null,
        primary key (id, REV)
    );

    create table FilterRule_FilterRuleParam (
        FilterRule_id numeric(19,0) not null,
        params_id numeric(19,0) not null
    );

    create table FilterRule_FilterRuleParam_AUD (
        REV int not null,
        FilterRule_id numeric(19,0) not null,
        params_id numeric(19,0) not null,
        revtype tinyint null,
        primary key (REV, FilterRule_id, params_id)
    );
    
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

