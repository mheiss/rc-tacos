
    alter table Assignment_AUD 
        drop constraint FKDFB4523EDF74E053;

    alter table Car 
        drop constraint FK107B4716F1CD8;

    alter table Car 
        drop constraint FK107B44CC82590;

    alter table CarCareEntry 
        drop constraint FK9FF5628D1D301F3C;

    alter table CarCareEntry_AUD 
        drop constraint FKA6381DEDF74E053;

    alter table CarDetail_AUD 
        drop constraint FKDD838876DF74E053;

    alter table Car_AUD 
        drop constraint FK843A3B85DF74E053;

    alter table Category_AUD 
        drop constraint FK23378FEFDF74E053;

    alter table Competence_AUD 
        drop constraint FKE7F8853ADF74E053;

    alter table Equipment_AUD 
        drop constraint FKA749CC3FDF74E053;

    alter table Info 
        drop constraint FK22D8CE716F1CD8;

    alter table Info 
        drop constraint FK22D8CE3DDF2C38;

    alter table Info_AUD 
        drop constraint FKE79EF9FDF74E053;

    alter table Link_AUD 
        drop constraint FK4B0CB4EBDF74E053;

    alter table Location_AUD 
        drop constraint FK6563F26DF74E053;

    alter table Login_AUD 
        drop constraint FK10FC609ADF74E053;

    alter table Notification_AUD 
        drop constraint FK2DD68D5CDF74E053;

    alter table RestoreLogin_AUD 
        drop constraint FK541ED1ECDF74E053;

    alter table RosterEntry 
        drop constraint FKEF3D7087716F1CD8;

    alter table RosterEntry 
        drop constraint FKEF3D70878AB8CE58;

    alter table RosterEntry 
        drop constraint FKEF3D708714938758;

    alter table RosterEntry 
        drop constraint FKEF3D7087C62A077C;

    alter table RosterEntry 
        drop constraint FKEF3D70871D301F3C;

    alter table RosterEntry_AUD 
        drop constraint FK3AA002D8DF74E053;

    alter table SecuredAction_AUD 
        drop constraint FK6DD33494DF74E053;

    alter table SecuredResource_AUD 
        drop constraint FKC3721F2CDF74E053;

    alter table ServiceType_AUD 
        drop constraint FKE5A1EDC0DF74E053;

    alter table SystemUser 
        drop constraint FK9D23FEBAB76029C;

    alter table SystemUser 
        drop constraint FK9D23FEBA716F1CD8;

    alter table SystemUser_AUD 
        drop constraint FK595E3F8BDF74E053;

    alter table SystemUser_Competence 
        drop constraint FK446C328E14938758;

    alter table SystemUser_Competence 
        drop constraint FK446C328ED9A3C737;

    alter table SystemUser_Competence_AUD 
        drop constraint FKF700695FDF74E053;

    alter table SystemUser_UserGroup 
        drop constraint FK16853A0F3A498367;

    alter table SystemUser_UserGroup 
        drop constraint FK16853A0F14938758;

    alter table SystemUser_UserGroup_AUD 
        drop constraint FK7E57C860DF74E053;

    alter table UserGroup_AUD 
        drop constraint FK4C4F4525DF74E053;

    drop table Assignment if exists;

    drop table Assignment_AUD if exists;

    drop table Car if exists;

    drop table CarCareEntry if exists;

    drop table CarCareEntry_AUD if exists;

    drop table CarDetail if exists;

    drop table CarDetail_AUD if exists;

    drop table Car_AUD if exists;

    drop table Category if exists;

    drop table Category_AUD if exists;

    drop table Competence if exists;

    drop table Competence_AUD if exists;

    drop table Equipment if exists;

    drop table Equipment_AUD if exists;

    drop table Info if exists;

    drop table Info_AUD if exists;

    drop table Link if exists;

    drop table Link_AUD if exists;

    drop table Location if exists;

    drop table Location_AUD if exists;

    drop table Login if exists;

    drop table Login_AUD if exists;

    drop table Notification if exists;

    drop table Notification_AUD if exists;

    drop table REVINFO if exists;

    drop table RestoreLogin if exists;

    drop table RestoreLogin_AUD if exists;

    drop table RosterEntry if exists;

    drop table RosterEntry_AUD if exists;

    drop table SecuredAction if exists;

    drop table SecuredAction_AUD if exists;

    drop table SecuredResource if exists;

    drop table SecuredResource_AUD if exists;

    drop table ServiceType if exists;

    drop table ServiceType_AUD if exists;

    drop table SystemUser if exists;

    drop table SystemUser_AUD if exists;

    drop table SystemUser_Competence if exists;

    drop table SystemUser_Competence_AUD if exists;

    drop table SystemUser_UserGroup if exists;

    drop table SystemUser_UserGroup_AUD if exists;

    drop table UserGroup if exists;

    drop table UserGroup_AUD if exists;
