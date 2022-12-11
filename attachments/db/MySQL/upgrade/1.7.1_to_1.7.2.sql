SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- 建表 oss_file_info
DROP TABLE IF EXISTS `oss_file_info`;
CREATE TABLE `oss_file_info`
(
    `id`                bigint(20) NOT NULL COMMENT '主键ID',
    `tenant_id`         bigint(20) NULL DEFAULT NULL COMMENT '租户ID',
    `revision`          bigint(20) NOT NULL DEFAULT 1 COMMENT '乐观锁',
    `del_flag`          tinyint(4) UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除标识',
    `created_at`        datetime(0) NOT NULL COMMENT '创建时刻',
    `created_by`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
    `updated_at`        datetime(0) NOT NULL COMMENT '更新时刻',
    `updated_by`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
    `storage_platform`  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '存储平台',
    `storage_base_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '基础存储路径',
    `storage_path`      varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '存储路径',
    `storage_filename`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '存储文件名',
    `original_filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '原始文件名',
    `extend_name`       varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '扩展名',
    `file_size`         bigint(20) NOT NULL COMMENT '文件大小',
    `md5`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT 'MD5',
    `classified`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件类别',
    `direct_url`        varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '对象存储直链',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '上传文件信息' ROW_FORMAT = Dynamic;

SET
FOREIGN_KEY_CHECKS = 1;

-- 增加对应后台管理菜单
INSERT INTO `sys_menu`(`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                       `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                       `external_link`)
VALUES (20220922152714, NULL, 1, 0, '2022-09-22 15:27:14', 'helio-generator', '2022-09-22 15:27:14',
        'helio-generator', '上传文件信息管理', 0, 1, 'OssFileInfo', 'ant-design:flag-outlined', 100, 1,
        '/oss/OssFileInfo/index', '');
INSERT INTO `sys_menu`(`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                       `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                       `external_link`)
VALUES (20220922152715, NULL, 1, 0, '2022-09-22 15:27:14', 'helio-generator', '2022-09-22 15:27:14',
        'helio-generator', '查询', 1572848462892752896, 2, 'OssFileInfo:retrieve', NULL, 1, 1, NULL, '');
INSERT INTO `sys_menu`(`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                       `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                       `external_link`)
VALUES (20220922152716, NULL, 1, 1, '2022-09-22 15:27:14', 'helio-generator', '2022-09-22 15:35:48', 'admin',
        '新增', 1572848462892752896, 2, 'OssFileInfo:create', NULL, 2, 1, NULL, '');
INSERT INTO `sys_menu`(`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                       `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                       `external_link`)
VALUES (20220922152717, NULL, 1, 1, '2022-09-22 15:27:14', 'helio-generator', '2022-09-22 15:35:55', 'admin',
        '编辑', 1572848462892752896, 2, 'OssFileInfo:update', NULL, 4, 1, NULL, '');
INSERT INTO `sys_menu`(`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                       `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                       `external_link`)
VALUES (20220922152718, NULL, 1, 0, '2022-09-22 15:27:14', 'helio-generator', '2022-09-22 15:36:03', 'admin',
        '删除', 1572848462892752896, 2, 'OssFileInfo:delete', NULL, 2, 1, NULL, '');
