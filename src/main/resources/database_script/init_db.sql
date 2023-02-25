-- JPA规范要求，字段大小写必须转为下划线分割
ALTER TABLE cdsa.rslt CHANGE IsSqxRslt is_sqx_rslt bit(1) NULL COMMENT '是否序列结果集，可能是跟测试无关。';
-- 添加 creaed_date 列，使用日期格式
ALTER TABLE cdsa.rslt ADD created_date DATETIME NULL;
-- 按照原字符串数据更新新列
update  rslt set created_date = str_to_date(replace(replace(created ,"T", " "), "Z", ""),'%Y-%m-%d %H:%i:%s.%f');
-- 添加 uploaded_date 列，使用日期格式
ALTER TABLE cdsa.dx ADD uploaded_date DATETIME NULL;
-- 按照原字符串数据更新新列
update cdsa.dx set uploaded_date = str_to_date(replace(replace(uploaded ,"T", " "), "Z", ""),'%Y-%m-%d %H:%i:%s.%f');

--添加用户表
DROP TABLE IF EXISTS cdsa.acl_user;
create table cdsa.acl_user
(
	id int auto_increment
		primary key,
	name varchar(100) not null,
	email varchar(40) not null,
	password varchar(60) not null,
	status varchar(40)
) comment '用户信息表，内置登录CDS Assistant 用户信息';
INSERT INTO cdsa.acl_user (id, name, email, password, status) VALUES (1, 'Agilent', 'michael.li2@agilent.com', '123456', '1');

-- 创建仪器功率记录表
create table cdsa.power_history
(
    id varchar(100) primary key,
    instrument_name varchar(100) ,
    ip varchar(100) not null,
    token varchar(100) not null,
    power DOUBLE(6, 2) not null,
    created_date DATETIME null
) comment '仪器信息表，用于记录第三方仪器的ip、token，功率信息';