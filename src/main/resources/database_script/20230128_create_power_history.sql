create table power_history
(
    id varchar(100) primary key,
    instrument_name varchar(100) ,
    ip varchar(100) not null,
    token varchar(100) not null,
    power DOUBLE(6, 2) not null,
    created_date DATETIME null
) comment '仪器信息表，用于记录第三方仪器的ip、token，功率信息';

-- ALTER TABLE cdsa.power_history CHANGE instrument_id instrument_name varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL;
