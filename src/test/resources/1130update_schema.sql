-- 添加 uploaded_date 列，使用日期格式
ALTER TABLE cdsa.dx ADD uploaded_date DATETIME NULL;
-- 按照原字符串数据更新新列
update dx set uploaded_date = str_to_date(replace(replace(uploaded ,"T", " "), "Z", ""),'%Y-%m-%d %H:%i:%s.%f');