-- 添加 creaed_date 列，使用日期格式
ALTER TABLE cdsa.rslt ADD created_date DATETIME NULL;
-- 按照原字符串数据更新新列
update  rslt set created_date = str_to_date(replace(replace(created ,"T", " "), "Z", ""),'%Y-%m-%d %H:%i:%s.%f');