create table acl_user_role
(
	acl_user_id int not null comment 'relate to User.id',
	acl_role_id int not null comment 'relate to Role.id',
	constraint User_Role_ACL_Role_id_fk
		foreign key (acl_role_id) references skylinehitech_sp.acl_role (id),
	constraint User_Role_ACL_User_id_fk
		foreign key (acl_user_id) references skylinehitech_sp.acl_use (id)
)
comment 'user role relations list';