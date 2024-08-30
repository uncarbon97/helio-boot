-- 建表 oss_file_info
DROP TABLE IF EXISTS "oss_file_info";
CREATE TABLE "oss_file_info"
(
    "id"                int8         NOT NULL,
    "tenant_id"         int8,
    "revision"          int8         NOT NULL,
    "del_flag"          int2         NOT NULL,
    "created_at"        timestamp(6) NOT NULL,
    "created_by"        varchar(255),
    "updated_at"        timestamp(6) NOT NULL,
    "updated_by"        varchar(255),
    "storage_platform"  varchar(50)  NOT NULL,
    "storage_base_path" varchar(255) NOT NULL,
    "storage_path"      varchar(512) NOT NULL,
    "storage_filename"  varchar(255) NOT NULL,
    "original_filename" varchar(255) NOT NULL,
    "extend_name"       varchar(16)  NOT NULL,
    "file_size"         int8         NOT NULL,
    "md5"               varchar(32)  NOT NULL,
    "classified"        varchar(50),
    "direct_url"        varchar(512)
)
;
COMMENT
ON COLUMN "oss_file_info"."id" IS '主键ID';
COMMENT
ON COLUMN "oss_file_info"."tenant_id" IS '租户ID';
COMMENT
ON COLUMN "oss_file_info"."revision" IS '乐观锁';
COMMENT
ON COLUMN "oss_file_info"."del_flag" IS '逻辑删除标识';
COMMENT
ON COLUMN "oss_file_info"."created_at" IS '创建时刻';
COMMENT
ON COLUMN "oss_file_info"."created_by" IS '创建者';
COMMENT
ON COLUMN "oss_file_info"."updated_at" IS '更新时刻';
COMMENT
ON COLUMN "oss_file_info"."updated_by" IS '更新者';
COMMENT
ON COLUMN "oss_file_info"."storage_platform" IS '存储平台';
COMMENT
ON COLUMN "oss_file_info"."storage_base_path" IS '基础存储路径';
COMMENT
ON COLUMN "oss_file_info"."storage_path" IS '存储路径';
COMMENT
ON COLUMN "oss_file_info"."storage_filename" IS '存储文件名';
COMMENT
ON COLUMN "oss_file_info"."original_filename" IS '原始文件名';
COMMENT
ON COLUMN "oss_file_info"."extend_name" IS '扩展名';
COMMENT
ON COLUMN "oss_file_info"."file_size" IS '文件大小';
COMMENT
ON COLUMN "oss_file_info"."md5" IS 'MD5';
COMMENT
ON COLUMN "oss_file_info"."classified" IS '文件类别';
COMMENT
ON COLUMN "oss_file_info"."direct_url" IS '对象存储直链';
COMMENT
ON TABLE "oss_file_info" IS '上传文件信息';

-- 主键
ALTER TABLE "oss_file_info"
    ADD CONSTRAINT "oss_file_info_pkey" PRIMARY KEY ("id");

-- 增加对应后台管理菜单
INSERT INTO sys_menu (id, tenant_id, revision, del_flag, created_at, created_by, updated_at,
                      updated_by, title, parent_id, type, permission, icon, sort, status, component,
                      external_link)
VALUES (20220922152710, NULL, 1, 0, '2022-12-25 23:09:30', 'admin', '2022-12-26 22:02:31', 'admin', '文件管理', 0, 0,
        'Oss', 'ant-design:file-outlined', 4, 1, NULL, '');
INSERT INTO sys_menu (id, tenant_id, revision, del_flag, created_at, created_by, updated_at,
                      updated_by, title, parent_id, type, permission, icon, sort, status, component,
                      external_link)
VALUES (20220922152714, NULL, 1, 0, '2022-09-22 15:27:14', 'helio-generator', '2022-12-26 22:03:14', 'admin',
        '上传文件信息管理', 20220922152710, 1, 'OssFileInfo', 'ant-design:save-twotone', 1, 1, '/oss/OssFileInfo/index',
        '');
INSERT INTO sys_menu (id, tenant_id, revision, del_flag, created_at, created_by, updated_at,
                      updated_by, title, parent_id, type, permission, icon, sort, status, component,
                      external_link)
VALUES (20220922152715, NULL, 1, 0, '2022-09-22 15:27:14', 'helio-generator', '2022-12-26 22:01:58', 'admin', '查询',
        20220922152714, 2, 'OssFileInfo:retrieve', NULL, 1, 1, NULL, '');
INSERT INTO sys_menu (id, tenant_id, revision, del_flag, created_at, created_by, updated_at,
                      updated_by, title, parent_id, type, permission, icon, sort, status, component,
                      external_link)
VALUES (20220922152718, NULL, 1, 0, '2022-09-22 15:27:14', 'helio-generator', '2022-12-26 22:02:09', 'admin', '删除',
        20220922152714, 2, 'OssFileInfo:delete', NULL, 2, 1, NULL, '');
