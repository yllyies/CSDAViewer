/*
DEV / Local use only, disable it in production
*/

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
