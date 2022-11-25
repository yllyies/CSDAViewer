/*
DEV / Local use only, disable it in production
*/
DROP TABLE IF EXISTS ACL_ROLE;
create table ACL_ROLE
(
	id int auto_increment
		primary key,
	name varchar(100) not null,
	level int not null,
	authority varchar(40) null,
	status varchar(40) null
);
INSERT INTO ACL_ROLE (id, name, level, authority, status) VALUES (1, 'ADMIN', 3, 'ROLE_ADMIN', '1');
INSERT INTO ACL_ROLE (id, name, level, authority, status) VALUES (2, 'EMPLOYEE', 2, 'ROLE_EMPLOYEE', '1');
INSERT INTO ACL_ROLE (id, name, level, authority, status) VALUES (3, 'USER', 1, 'ROLE_USER', '1');

DROP TABLE IF EXISTS ACL_USER;
create table ACL_USER
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
INSERT INTO ACL_USER (id, title, name, email, authType, password, SAML, status) VALUES (1, 'Manager', 'Michael', 'Michaelli423@hotmail.com', 'Manager', '123', null, '1');
INSERT INTO ACL_USER (id, title, name, email, authType, password, SAML, status) VALUES (2, 'Employee', 'Billy', 'billy@hotmail.com', 'Employee', '123', null, '1');

create table ACL_USER_ROLE
(
	acl_user_id int,
	acl_role_id int
);
INSERT INTO ACL_USER_ROLE (acl_user_id, acl_role_id) VALUES (1, 1);
INSERT INTO ACL_USER_ROLE (acl_user_id, acl_role_id) VALUES (2, 2);