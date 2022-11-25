create table acl_user
(
    id int auto_increment
        primary key,
    title varchar(100) ,
    name varchar(100) not null,
    email varchar(40) not null,
    authType varchar(40) not null,
    password varchar(60) not null,
    SAML varchar(40),
    status varchar(40)
);