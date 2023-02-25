/*
DEV / Local use only, disable it in production
*/

DROP TABLE IF EXISTS ACL_USER;
create table ACL_USER
(
	id int auto_increment
		primary key,
	name varchar(100) not null,
	email varchar(40) not null,
	password varchar(60) not null,
	status varchar(40)
);
ALTER TABLE cdsa.acl_user COMMENT='用户信息表';
INSERT INTO ACL_USER (id, name, email, password, status) VALUES (1, 'Agilent', 'michael.li2@agilent.com', '123456', '1');
