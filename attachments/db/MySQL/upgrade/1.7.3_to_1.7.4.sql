-- v1.7.4 - Uncarbon - 新增错误原因堆栈、用户UA、IP属地字段；表注释更新
ALTER TABLE sys_log
    ADD COLUMN error_stacktrace varchar(3000) NULL COMMENT '错误原因堆栈' AFTER status,
    ADD COLUMN user_agent varchar(255) NULL COMMENT '用户UA' AFTER error_stacktrace,
    ADD COLUMN ip_location_region_name varchar(100) NULL COMMENT 'IP地址属地-国家或地区名' AFTER user_agent,
    ADD COLUMN ip_location_province_name varchar(100) NULL COMMENT 'IP地址属地-省级行政区名' AFTER ip_location_region_name,
    ADD COLUMN ip_location_city_name varchar(100) NULL COMMENT 'IP地址属地-市级行政区名' AFTER ip_location_province_name,
    ADD COLUMN ip_location_district_name varchar(100) NULL COMMENT 'IP地址属地-县级行政区名' AFTER ip_location_city_name;

ALTER TABLE sys_log COMMENT = '系统日志';
