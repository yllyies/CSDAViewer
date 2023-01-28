create table power_history
(
    id decimal(19,0) auto_increment
        primary key,
    instrument_id varchar(100) ,
    ip varchar(100) not null,
    token varchar(100) not null,
    power DOUBLE(6, 2) not null,
    created_date DATETIME null
) comment '仪器信息记录表';