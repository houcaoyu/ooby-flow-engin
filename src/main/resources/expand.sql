alter table ACT_ID_GROUP add column if not exists PARENT_ VARCHAR(64);

create table OOBY_ID_POST (
    USER_ID_ varchar(64),
    GROUP_ID_ varchar(64),
    POST_ID_ varchar(64),
    primary key (USER_ID_, GROUP_ID_)
);

alter table OOBY_ID_POST
    add constraint OOBY_FK_POST_GROUP
    foreign key (GROUP_ID_)
    references ACT_ID_GROUP;
    
alter table OOBY_ID_POST
    add constraint OOBY_FK_POST_POST
    foreign key (GROUP_ID_)
    references ACT_ID_GROUP;
    
alter table OOBY_ID_POST
    add constraint OOBY_FK_POST_USER
    foreign key (USER_ID_)
    references ACT_ID_USER;