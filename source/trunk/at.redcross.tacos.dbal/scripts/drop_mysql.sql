
    alter table Assignment_AUD 
        drop 
        foreign key FKDFB4523EDF74E053;

    alter table Car 
        drop 
        foreign key FK107B4716F1CD8;

    alter table Car 
        drop 
        foreign key FK107B44CC82590;

    alter table CarCareEntry 
        drop 
        foreign key FK9FF5628D1D301F3C;

    alter table CarCareEntry_AUD 
        drop 
        foreign key FKA6381DEDF74E053;

    alter table CarDetail_AUD 
        drop 
        foreign key FKDD838876DF74E053;

    alter table Car_AUD 
        drop 
        foreign key FK843A3B85DF74E053;

    alter table Category_AUD 
        drop 
        foreign key FK23378FEFDF74E053;

    alter table Competence_AUD 
        drop 
        foreign key FKE7F8853ADF74E053;

    alter table Equipment_AUD 
        drop 
        foreign key FKA749CC3FDF74E053;

    alter table FilterRuleParam_AUD 
        drop 
        foreign key FK2A0E6B8ADF74E053;

    alter table FilterRule_AUD 
        drop 
        foreign key FKE5C8EA85DF74E053;

    alter table FilterRule_FilterRuleParam 
        drop 
        foreign key FKD034A6AE61A5C7CF;

    alter table FilterRule_FilterRuleParam 
        drop 
        foreign key FKD034A6AE5425CD18;

    alter table FilterRule_FilterRuleParam_AUD 
        drop 
        foreign key FKC331CD7FDF74E053;

    alter table Info 
        drop 
        foreign key FK22D8CE716F1CD8;

    alter table Info 
        drop 
        foreign key FK22D8CE3DDF2C38;

    alter table Info_AUD 
        drop 
        foreign key FKE79EF9FDF74E053;

    alter table Link_AUD 
        drop 
        foreign key FK4B0CB4EBDF74E053;

    alter table Location_AUD 
        drop 
        foreign key FK6563F26DF74E053;

    alter table Login_AUD 
        drop 
        foreign key FK10FC609ADF74E053;

    alter table Notification_AUD 
        drop 
        foreign key FK2DD68D5CDF74E053;

    alter table RestoreLogin_AUD 
        drop 
        foreign key FK541ED1ECDF74E053;

    alter table RosterEntry 
        drop 
        foreign key FKEF3D7087716F1CD8;

    alter table RosterEntry 
        drop 
        foreign key FKEF3D70878AB8CE58;

    alter table RosterEntry 
        drop 
        foreign key FKEF3D708714938758;

    alter table RosterEntry 
        drop 
        foreign key FKEF3D7087C62A077C;

    alter table RosterEntry 
        drop 
        foreign key FKEF3D70871D301F3C;

    alter table RosterEntry_AUD 
        drop 
        foreign key FK3AA002D8DF74E053;

    alter table SecuredAction_AUD 
        drop 
        foreign key FK6DD33494DF74E053;

    alter table SecuredResource_AUD 
        drop 
        foreign key FKC3721F2CDF74E053;

    alter table ServiceType_AUD 
        drop 
        foreign key FKE5A1EDC0DF74E053;

    alter table SystemUser 
        drop 
        foreign key FK9D23FEBAB76029C;

    alter table SystemUser 
        drop 
        foreign key FK9D23FEBA716F1CD8;

    alter table SystemUser_AUD 
        drop 
        foreign key FK595E3F8BDF74E053;

    alter table SystemUser_Competence 
        drop 
        foreign key FK446C328E14938758;

    alter table SystemUser_Competence 
        drop 
        foreign key FK446C328ED9A3C737;

    alter table SystemUser_Competence_AUD 
        drop 
        foreign key FKF700695FDF74E053;

    alter table SystemUser_UserGroup 
        drop 
        foreign key FK16853A0F3A498367;

    alter table SystemUser_UserGroup 
        drop 
        foreign key FK16853A0F14938758;

    alter table SystemUser_UserGroup_AUD 
        drop 
        foreign key FK7E57C860DF74E053;

    alter table UserGroup_AUD 
        drop 
        foreign key FK4C4F4525DF74E053;

    drop table if exists Assignment;

    drop table if exists Assignment_AUD;

    drop table if exists Car;

    drop table if exists CarCareEntry;

    drop table if exists CarCareEntry_AUD;

    drop table if exists CarDetail;

    drop table if exists CarDetail_AUD;

    drop table if exists Car_AUD;

    drop table if exists Category;

    drop table if exists Category_AUD;

    drop table if exists Competence;

    drop table if exists Competence_AUD;

    drop table if exists Equipment;

    drop table if exists Equipment_AUD;

    drop table if exists FilterRule;

    drop table if exists FilterRuleParam;

    drop table if exists FilterRuleParam_AUD;

    drop table if exists FilterRule_AUD;

    drop table if exists FilterRule_FilterRuleParam;

    drop table if exists FilterRule_FilterRuleParam_AUD;

    drop table if exists Info;

    drop table if exists Info_AUD;

    drop table if exists Link;

    drop table if exists Link_AUD;

    drop table if exists Location;

    drop table if exists Location_AUD;

    drop table if exists Login;

    drop table if exists Login_AUD;

    drop table if exists Notification;

    drop table if exists Notification_AUD;

    drop table if exists REVINFO;

    drop table if exists RestoreLogin;

    drop table if exists RestoreLogin_AUD;

    drop table if exists RosterEntry;

    drop table if exists RosterEntry_AUD;

    drop table if exists SecuredAction;

    drop table if exists SecuredAction_AUD;

    drop table if exists SecuredResource;

    drop table if exists SecuredResource_AUD;

    drop table if exists ServiceType;

    drop table if exists ServiceType_AUD;

    drop table if exists SystemUser;

    drop table if exists SystemUser_AUD;

    drop table if exists SystemUser_Competence;

    drop table if exists SystemUser_Competence_AUD;

    drop table if exists SystemUser_UserGroup;

    drop table if exists SystemUser_UserGroup_AUD;

    drop table if exists UserGroup;

    drop table if exists UserGroup_AUD;
