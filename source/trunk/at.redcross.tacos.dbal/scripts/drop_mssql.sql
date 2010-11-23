
    alter table Assignment_AUD 
        drop constraint FKDFB4523E8277D80A;

    alter table Car 
        drop constraint FK107B4716F1CD8;

    alter table Car 
        drop constraint FK107B44CC82590;

    alter table CarCareEntry 
        drop constraint FK9FF5628D1D301F3C;

    alter table CarCareEntry_AUD 
        drop constraint FKA6381DE8277D80A;

    alter table CarDetail_AUD 
        drop constraint FKDD8388768277D80A;

    alter table Car_AUD 
        drop constraint FK843A3B858277D80A;

    alter table Category_AUD 
        drop constraint FK23378FEF8277D80A;

    alter table Competence_AUD 
        drop constraint FKE7F8853A8277D80A;

    alter table Info 
        drop constraint FK22D8CE716F1CD8;

    alter table Info 
        drop constraint FK22D8CE3DDF2C38;

    alter table Info_AUD 
        drop constraint FKE79EF9F8277D80A;

    alter table Link_AUD 
        drop constraint FK4B0CB4EB8277D80A;

    alter table Location_AUD 
        drop constraint FK6563F268277D80A;

    alter table Login_AUD 
        drop constraint FK10FC609A8277D80A;

    alter table Notification_AUD 
        drop constraint FK2DD68D5C8277D80A;

    alter table RestoreLogin_AUD 
        drop constraint FK541ED1EC8277D80A;

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
        drop constraint FK3AA002D88277D80A;

    alter table SecuredAction_AUD 
        drop constraint FK6DD334948277D80A;

    alter table SecuredResource_AUD 
        drop constraint FKC3721F2C8277D80A;

    alter table ServiceType_AUD 
        drop constraint FKE5A1EDC08277D80A;

    alter table SystemUser 
        drop constraint FK9D23FEBAB76029C;

    alter table SystemUser 
        drop constraint FK9D23FEBA716F1CD8;

    alter table SystemUser_AUD 
        drop constraint FK595E3F8B8277D80A;

    alter table SystemUser_Competence 
        drop constraint FK446C328E14938758;

    alter table SystemUser_Competence 
        drop constraint FK446C328ED9A3C737;

    alter table SystemUser_Competence_AUD 
        drop constraint FKF700695F8277D80A;

    alter table SystemUser_UserGroup 
        drop constraint FK16853A0F3A498367;

    alter table SystemUser_UserGroup 
        drop constraint FK16853A0F14938758;

    alter table SystemUser_UserGroup_AUD 
        drop constraint FK7E57C8608277D80A;

    alter table UserGroup_AUD 
        drop constraint FK4C4F45258277D80A;

    drop table Assignment;

    drop table Assignment_AUD;

    drop table Car;

    drop table CarCareEntry;

    drop table CarCareEntry_AUD;

    drop table CarDetail;

    drop table CarDetail_AUD;

    drop table Car_AUD;

    drop table Category;

    drop table Category_AUD;

    drop table Competence;

    drop table Competence_AUD;

    drop table Info;

    drop table Info_AUD;

    drop table Link;

    drop table Link_AUD;

    drop table Location;

    drop table Location_AUD;

    drop table Login;

    drop table Login_AUD;

    drop table Notification;

    drop table Notification_AUD;

    drop table RestoreLogin;

    drop table RestoreLogin_AUD;

    drop table RevisionInfo;

    drop table RosterEntry;

    drop table RosterEntry_AUD;

    drop table SecuredAction;

    drop table SecuredAction_AUD;

    drop table SecuredResource;

    drop table SecuredResource_AUD;

    drop table ServiceType;

    drop table ServiceType_AUD;

    drop table SystemUser;

    drop table SystemUser_AUD;

    drop table SystemUser_Competence;

    drop table SystemUser_Competence_AUD;

    drop table SystemUser_UserGroup;

    drop table SystemUser_UserGroup_AUD;

    drop table UserGroup;

    drop table UserGroup_AUD;
