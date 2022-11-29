-- 添加 creaed_date 列，使用日期格式
ALTER TABLE cdsa.rslt ADD created_date DATETIME NULL;
-- 按照原字符串数据更新新列
UPDATE rslt u1, rslt u2
SET u1.created_date = CAST(replace(replace(u2.created ,"T", " "), "Z", "") AS DATETIME);