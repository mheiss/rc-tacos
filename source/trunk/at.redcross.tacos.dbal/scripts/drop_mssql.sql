
    alter table Car 
        drop constraint FK107B4716F1CD8;

    alter table Location 
        drop constraint FK752A03D54004E1E2;

    alter table Location 
        drop constraint FK752A03D54004E238;

    alter table Login 
        drop constraint FK462FF4914938758;

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

    alter table SystemUser 
        drop constraint FK9D23FEBA716F1CD8;

    alter table SystemUser_Competence 
        drop constraint FK446C328E14938758;

    alter table SystemUser_Competence 
        drop constraint FK446C328ED9A3C737;

    alter table SystemUser_UserGroup 
        drop constraint FK16853A0F3A498367;

    alter table SystemUser_UserGroup 
        drop constraint FK16853A0F14938758;

    drop table Assignment;

    drop table Car;

    drop table Competence;

    drop table District;

    drop table Link;

    drop table Location;

    drop table Login;

    drop table RosterEntry;

    drop table ServiceType;

    drop table SystemUser;

    drop table SystemUser_Competence;

    drop table SystemUser_UserGroup;

    drop table UserGroup;
