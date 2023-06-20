-- v1.7.4 - Uncarbon - 新增错误原因堆栈、用户UA、IP属地字段；表注释更新
ALTER TABLE sys_log
    ADD COLUMN error_stacktrace varchar(3000) NOT NULL DEFAULT '',
    ADD COLUMN user_agent varchar(255) NOT NULL DEFAULT '',
    ADD COLUMN ip_location_region_name varchar(100) NOT NULL DEFAULT '',
    ADD COLUMN ip_location_province_name varchar(100) NOT NULL DEFAULT '',
    ADD COLUMN ip_location_city_name varchar(100) NOT NULL DEFAULT '',
    ADD COLUMN ip_location_district_name varchar(100) NOT NULL DEFAULT '';

COMMENT ON COLUMN sys_log.error_stacktrace IS '错误原因堆栈';
COMMENT ON COLUMN sys_log.user_agent IS '用户UA';
COMMENT ON COLUMN sys_log.ip_location_region_name IS 'IP地址属地-国家或地区名';
COMMENT ON COLUMN sys_log.ip_location_province_name IS 'IP地址属地-省级行政区名';
COMMENT ON COLUMN sys_log.ip_location_city_name IS 'IP地址属地-市级行政区名';
COMMENT ON COLUMN sys_log.ip_location_district_name IS 'IP地址属地-县级行政区名';

COMMENT ON TABLE sys_log IS '系统日志';

-- v1.7.4 - Uncarbon - 订正系统菜单-权限串缺少默认值问题
ALTER TABLE "sys_menu"
    ALTER COLUMN "permission" SET DEFAULT '';
