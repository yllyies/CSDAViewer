create table acl_role
(
    id int auto_increment
        primary key,
    name varchar(100) not null,
    level int not null,
    authority varchar(40) null,
    status varchar(40) null
);

