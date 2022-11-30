-- 添加 uploaded_date 列，使用日期格式
ALTER TABLE cdsa.dx ADD uploaded_date DATETIME NULL;
-- 按照原字符串数据更新新列
UPDATE dx u1, dx u2 SET u1.uploaded_date = CAST(replace(replace(u2.uploaded ,"T", " "), "Z", "") AS DATETIME) where u2.uploaded is not null;