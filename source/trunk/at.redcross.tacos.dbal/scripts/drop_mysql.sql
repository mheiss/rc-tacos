
    alter table Car 
        drop 
        foreign key FK107B4716F1CD8;

    alter table Info 
        drop 
        foreign key FK22D8CE716F1CD8;

    alter table Info 
        drop 
        foreign key FK22D8CE3DDF2C38;

    alter table Location 
        drop 
        foreign key FK752A03D54004E1E2;

    alter table Location 
        drop 
        foreign key FK752A03D54004E238;

    alter table Login 
        drop 
        foreign key FK462FF4914938758;

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

    alter table SystemUser 
        drop 
        foreign key FK9D23FEBA716F1CD8;

    alter table SystemUser_Competence 
        drop 
        foreign key FK446C328E14938758;

    alter table SystemUser_Competence 
        drop 
        foreign key FK446C328ED9A3C737;

    alter table SystemUser_UserGroup 
        drop 
        foreign key FK16853A0F3A498367;

    alter table SystemUser_UserGroup 
        drop 
        foreign key FK16853A0F14938758;

    drop table if exists Assignment;

    drop table if exists Car;

    drop table if exists Category;

    drop table if exists Competence;

    drop table if exists District;

    drop table if exists Info;

    drop table if exists Link;

    drop table if exists Location;

    drop table if exists Login;

    drop table if exists Notification;

    drop table if exists RosterEntry;

    drop table if exists SecuredAction;

    drop table if exists SecuredResource;

    drop table if exists ServiceType;

    drop table if exists SystemUser;

    drop table if exists SystemUser_Competence;

    drop table if exists SystemUser_UserGroup;

    drop table if exists UserGroup;
