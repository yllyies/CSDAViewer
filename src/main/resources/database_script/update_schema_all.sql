-- JPA规范要求，字段大小写必须转为下划线分割
ALTER TABLE cdsa.rslt CHANGE IsSqxRslt is_sqx_rslt bit(1) NULL COMMENT '是否序列结果集，可能是跟测试无关。';

-- 添加 creaed_date 列，使用日期格式
ALTER TABLE cdsa.rslt ADD created_date DATETIME NULL;
-- 按照原字符串数据更新新列
update  rslt set created_date = str_to_date(replace(replace(created ,"T", " "), "Z", ""),'%Y-%m-%d %H:%i:%s.%f');

-- 添加 uploaded_date 列，使用日期格式
ALTER TABLE cdsa.dx ADD uploaded_date DATETIME NULL;
-- 按照原字符串数据更新新列
update dx set uploaded_date = str_to_date(replace(replace(uploaded ,"T", " "), "Z", ""),'%Y-%m-%d %H:%i:%s.%f');