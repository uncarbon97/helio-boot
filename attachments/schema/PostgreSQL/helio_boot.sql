-- ----------------------------
-- Table structure for sys_data_dict
-- ----------------------------
DROP TABLE IF EXISTS "sys_data_dict";
CREATE TABLE "sys_data_dict"
(
    "id"              int8         NOT NULL,
    "tenant_id"       int8,
    "revision"        int8         NOT NULL DEFAULT 1,
    "del_flag"        int2         NOT NULL DEFAULT 0,
    "created_at"      timestamp(6) NOT NULL,
    "created_by"      varchar(255),
    "updated_at"      timestamp(6) NOT NULL,
    "updated_by"      varchar(255),
    "remark"          varchar(255),
    "camel_case_key"  varchar(100) NOT NULL,
    "under_case_key"  varchar(100) NOT NULL,
    "pascal_case_key" varchar(100) NOT NULL,
    "value"           varchar(255) NOT NULL,
    "description"     varchar(255) NOT NULL,
    "unit"            varchar(30),
    "value_range"     varchar(255),
    "alias_key"       varchar(100)
)
;
COMMENT
ON COLUMN "sys_data_dict"."id" IS '主键ID';
COMMENT
ON COLUMN "sys_data_dict"."tenant_id" IS '租户ID';
COMMENT
ON COLUMN "sys_data_dict"."revision" IS '乐观锁';
COMMENT
ON COLUMN "sys_data_dict"."del_flag" IS '逻辑删除标识';
COMMENT
ON COLUMN "sys_data_dict"."created_at" IS '创建时刻';
COMMENT
ON COLUMN "sys_data_dict"."created_by" IS '创建者';
COMMENT
ON COLUMN "sys_data_dict"."updated_at" IS '更新时刻';
COMMENT
ON COLUMN "sys_data_dict"."updated_by" IS '更新者';
COMMENT
ON COLUMN "sys_data_dict"."remark" IS '备注';
COMMENT
ON COLUMN "sys_data_dict"."camel_case_key" IS '驼峰式键名';
COMMENT
ON COLUMN "sys_data_dict"."under_case_key" IS '下划线式键名';
COMMENT
ON COLUMN "sys_data_dict"."pascal_case_key" IS '帕斯卡式键名';
COMMENT
ON COLUMN "sys_data_dict"."value" IS '键值';
COMMENT
ON COLUMN "sys_data_dict"."description" IS '描述';
COMMENT
ON COLUMN "sys_data_dict"."unit" IS '单位';
COMMENT
ON COLUMN "sys_data_dict"."value_range" IS '取值范围';
COMMENT
ON COLUMN "sys_data_dict"."alias_key" IS '别称键名';
COMMENT
ON TABLE "sys_data_dict" IS '数据字典';

-- ----------------------------
-- Records of sys_data_dict
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS "sys_dept";
CREATE TABLE "sys_dept"
(
    "id"         int8         NOT NULL,
    "tenant_id"  int8,
    "revision"   int8         NOT NULL DEFAULT 1,
    "del_flag"   int2         NOT NULL DEFAULT 0,
    "created_at" timestamp(6) NOT NULL,
    "created_by" varchar(255),
    "updated_at" timestamp(6) NOT NULL,
    "updated_by" varchar(255),
    "remark"     varchar(255),
    "title"      varchar(50)  NOT NULL,
    "parent_id"  int8         NOT NULL,
    "sort"       int4         NOT NULL,
    "status"     int4         NOT NULL
)
;
COMMENT
ON COLUMN "sys_dept"."id" IS '主键ID';
COMMENT
ON COLUMN "sys_dept"."tenant_id" IS '租户ID';
COMMENT
ON COLUMN "sys_dept"."revision" IS '乐观锁';
COMMENT
ON COLUMN "sys_dept"."del_flag" IS '逻辑删除标识';
COMMENT
ON COLUMN "sys_dept"."created_at" IS '创建时刻';
COMMENT
ON COLUMN "sys_dept"."created_by" IS '创建者';
COMMENT
ON COLUMN "sys_dept"."updated_at" IS '更新时刻';
COMMENT
ON COLUMN "sys_dept"."updated_by" IS '更新者';
COMMENT
ON COLUMN "sys_dept"."remark" IS '备注';
COMMENT
ON COLUMN "sys_dept"."title" IS '名称';
COMMENT
ON COLUMN "sys_dept"."parent_id" IS '上级ID(根节点设置为0)';
COMMENT
ON COLUMN "sys_dept"."sort" IS '排序';
COMMENT
ON COLUMN "sys_dept"."status" IS '状态(0=禁用 1=启用)';
COMMENT
ON TABLE "sys_dept" IS '部门';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS "sys_log";
CREATE TABLE "sys_log"
(
    "id"         int8         NOT NULL,
    "tenant_id"  int8,
    "revision"   int8         NOT NULL DEFAULT 1,
    "del_flag"   int2         NOT NULL DEFAULT 0,
    "created_at" timestamp(6) NOT NULL,
    "created_by" varchar(255),
    "updated_at" timestamp(6) NOT NULL,
    "updated_by" varchar(255),
    "remark"     varchar(255),
    "user_id"    int8,
    "username"   varchar(50),
    "operation"  varchar(255),
    "method"     varchar(500),
    "params"     text,
    "ip"         varchar(128),
    "status"     int4         NOT NULL
)
;
COMMENT
ON COLUMN "sys_log"."id" IS '主键ID';
COMMENT
ON COLUMN "sys_log"."tenant_id" IS '租户ID';
COMMENT
ON COLUMN "sys_log"."revision" IS '乐观锁';
COMMENT
ON COLUMN "sys_log"."del_flag" IS '逻辑删除标识';
COMMENT
ON COLUMN "sys_log"."created_at" IS '创建时刻';
COMMENT
ON COLUMN "sys_log"."created_by" IS '创建者';
COMMENT
ON COLUMN "sys_log"."updated_at" IS '更新时刻';
COMMENT
ON COLUMN "sys_log"."updated_by" IS '更新者';
COMMENT
ON COLUMN "sys_log"."remark" IS '备注';
COMMENT
ON COLUMN "sys_log"."user_id" IS '用户ID';
COMMENT
ON COLUMN "sys_log"."username" IS '用户账号';
COMMENT
ON COLUMN "sys_log"."operation" IS '操作内容';
COMMENT
ON COLUMN "sys_log"."method" IS '请求方法';
COMMENT
ON COLUMN "sys_log"."params" IS '请求参数';
COMMENT
ON COLUMN "sys_log"."ip" IS 'IP地址';
COMMENT
ON COLUMN "sys_log"."status" IS '状态(参考SysLogStatusEnum)';
COMMENT
ON TABLE "sys_log" IS '后台操作日志';

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS "sys_menu";
CREATE TABLE "sys_menu"
(
    "id"            int8         NOT NULL,
    "tenant_id"     int8,
    "revision"      int8         NOT NULL DEFAULT 1,
    "del_flag"      int2         NOT NULL DEFAULT 0,
    "created_at"    timestamp(6) NOT NULL,
    "created_by"    varchar(255),
    "updated_at"    timestamp(6) NOT NULL,
    "updated_by"    varchar(255),
    "remark"        varchar(255),
    "title"         varchar(50)  NOT NULL,
    "parent_id"     int8         NOT NULL,
    "type"          int4         NOT NULL,
    "permission"    varchar(255) NOT NULL,
    "icon"          varchar(255),
    "sort"          int4,
    "status"        int4         NOT NULL,
    "component"     varchar(255),
    "external_link" varchar(255)
)
;
COMMENT
ON COLUMN "sys_menu"."id" IS '主键ID';
COMMENT
ON COLUMN "sys_menu"."tenant_id" IS '租户ID';
COMMENT
ON COLUMN "sys_menu"."revision" IS '乐观锁';
COMMENT
ON COLUMN "sys_menu"."del_flag" IS '逻辑删除标识';
COMMENT
ON COLUMN "sys_menu"."created_at" IS '创建时刻';
COMMENT
ON COLUMN "sys_menu"."created_by" IS '创建者';
COMMENT
ON COLUMN "sys_menu"."updated_at" IS '更新时刻';
COMMENT
ON COLUMN "sys_menu"."updated_by" IS '更新者';
COMMENT
ON COLUMN "sys_menu"."remark" IS '备注';
COMMENT
ON COLUMN "sys_menu"."title" IS '名称';
COMMENT
ON COLUMN "sys_menu"."parent_id" IS '上级菜单ID(根节点设置为0)';
COMMENT
ON COLUMN "sys_menu"."type" IS '菜单类型(参考MenuTypeEnum)';
COMMENT
ON COLUMN "sys_menu"."permission" IS '权限标识';
COMMENT
ON COLUMN "sys_menu"."icon" IS '图标';
COMMENT
ON COLUMN "sys_menu"."sort" IS '排序';
COMMENT
ON COLUMN "sys_menu"."status" IS '状态(0=禁用 1=启用)';
COMMENT
ON COLUMN "sys_menu"."component" IS '组件';
COMMENT
ON COLUMN "sys_menu"."external_link" IS '外链地址';
COMMENT
ON TABLE "sys_menu" IS '后台菜单';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO "sys_menu"
VALUES (1, 0, 1, 0, '2021-06-01 17:16:48', NULL, '2021-06-02 17:14:16', NULL, NULL, '系统管理', 0, 0, 'Sys',
        'ant-design:setting-outlined', 3, 1, 'LAYOUT', '');
INSERT INTO "sys_menu"
VALUES (2, 0, 1, 0, '2021-06-01 17:13:13', NULL, '2021-07-18 00:10:33', 'admin', NULL, '中控台', 0, 0, 'Dashboard',
        'ant-design:appstore-outlined', 1, 1, 'LAYOUT', '');
INSERT INTO "sys_menu"
VALUES (3, 0, 1, 0, '2021-06-01 17:14:22', NULL, '2021-06-02 11:24:38', NULL, NULL, '分析页(默认页必须分配, 否则进不去后台)', 2, 1,
        'Dashboard:analysis', 'ant-design:fund-outlined', 1, 1, '/dashboard/analysis/index', '');
INSERT INTO "sys_menu"
VALUES (4, 0, 1, 0, '2021-06-01 17:14:47', NULL, '2021-06-02 11:32:23', NULL, NULL, '工作台', 2, 1, 'Dashboard:workbench',
        'ant-design:database-outlined', 2, 1, '/dashboard/workbench/index', '');
INSERT INTO "sys_menu"
VALUES (5, 0, 1, 0, '2021-06-01 17:15:38', NULL, '2021-07-15 23:56:43', 'admin', NULL, '关于', 0, 1, 'About',
        'ant-design:eye-outlined', 2, 1, '/sys/vben/about/index', '');
INSERT INTO "sys_menu"
VALUES (6, 0, 1, 0, '2021-06-02 16:06:58', NULL, '2021-07-17 23:55:52', 'admin', NULL, '学(mo)习(yu)', 2, 3, '',
        'ant-design:zhihu-outlined', 3, 1, 'https://www.zhihu.com/', 'https://www.zhihu.com/');
INSERT INTO "sys_menu"
VALUES (7, 0, 1, 0, '2021-07-17 23:32:15', NULL, '2021-07-17 23:32:15', NULL, NULL, '部门管理', 1, 1, 'SysDept',
        'ant-design:flag-outlined', 1, 1, '/sys/SysDept/index', '');
INSERT INTO "sys_menu"
VALUES (8, 0, 1, 0, '2021-07-17 23:32:15', NULL, '2021-07-17 23:32:15', NULL, NULL, '查询', 7, 2, 'SysDept:retrieve',
        NULL, 1, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (9, 0, 1, 0, '2021-07-17 23:32:15', NULL, '2021-07-17 23:32:15', NULL, NULL, '新增', 7, 2, 'SysDept:create', NULL,
        2, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (10, 0, 1, 0, '2021-07-17 23:32:15', NULL, '2021-07-17 23:32:15', NULL, NULL, '删除', 7, 2, 'SysDept:delete', NULL,
        3, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (11, 0, 1, 0, '2021-07-17 23:32:16', NULL, '2021-07-17 23:32:16', NULL, NULL, '编辑', 7, 2, 'SysDept:update', NULL,
        4, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (12, 0, 1, 0, '2021-07-17 23:32:16', NULL, '2021-07-17 23:32:16', NULL, NULL, '角色管理', 1, 1, 'SysRole',
        'ant-design:usergroup-add-outlined', 2, 1, '/sys/SysRole/index', '');
INSERT INTO "sys_menu"
VALUES (13, 0, 1, 0, '2021-07-17 23:32:16', NULL, '2021-07-17 23:32:16', NULL, NULL, '查询', 12, 2, 'SysRole:retrieve',
        NULL, 1, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (14, 0, 1, 0, '2021-07-17 23:32:16', NULL, '2021-07-17 23:32:16', NULL, NULL, '新增', 12, 2, 'SysRole:create',
        NULL, 2, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (15, 0, 1, 0, '2021-07-17 23:32:16', NULL, '2021-07-17 23:32:16', NULL, NULL, '删除', 12, 2, 'SysRole:delete',
        NULL, 3, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (16, 0, 1, 0, '2021-07-17 23:32:16', NULL, '2021-07-17 23:32:16', NULL, NULL, '编辑', 12, 2, 'SysRole:update',
        NULL, 4, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (17, 0, 1, 0, '2021-07-17 23:32:17', NULL, '2021-07-17 23:32:17', NULL, NULL, '后台用户', 1, 1, 'SysUser',
        'ant-design:user-outlined', 3, 1, '/sys/SysUser/index', '');
INSERT INTO "sys_menu"
VALUES (18, 0, 1, 0, '2021-07-17 23:32:17', NULL, '2021-07-17 23:32:17', NULL, NULL, '查询', 17, 2, 'SysUser:retrieve',
        NULL, 1, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (19, 0, 1, 0, '2021-07-17 23:32:17', NULL, '2021-07-17 23:32:17', NULL, NULL, '新增', 17, 2, 'SysUser:create',
        NULL, 2, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (20, 0, 1, 0, '2021-07-17 23:32:17', NULL, '2021-07-17 23:32:17', NULL, NULL, '删除', 17, 2, 'SysUser:delete',
        NULL, 3, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (21, 0, 1, 0, '2021-07-17 23:32:17', NULL, '2021-07-17 23:32:17', NULL, NULL, '编辑', 17, 2, 'SysUser:update',
        NULL, 4, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (22, 0, 1, 0, '2021-07-17 23:32:18', NULL, '2021-07-17 23:32:18', NULL, NULL, '菜单管理', 1, 1, 'SysMenu',
        'ant-design:align-left-outlined', 4, 1, '/sys/SysMenu/index', '');
INSERT INTO "sys_menu"
VALUES (23, 0, 1, 0, '2021-07-17 23:32:18', NULL, '2021-07-17 23:32:18', NULL, NULL, '查询', 22, 2, 'SysMenu:retrieve',
        NULL, 1, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (24, 0, 1, 0, '2021-07-17 23:32:18', NULL, '2021-07-17 23:32:18', NULL, NULL, '新增', 22, 2, 'SysMenu:create',
        NULL, 2, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (25, 0, 1, 0, '2021-07-17 23:32:18', NULL, '2021-07-17 23:32:18', NULL, NULL, '删除', 22, 2, 'SysMenu:delete',
        NULL, 3, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (26, 0, 1, 0, '2021-07-17 23:32:18', NULL, '2021-07-17 23:32:18', NULL, NULL, '编辑', 22, 2, 'SysMenu:update',
        NULL, 4, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (27, 0, 1, 0, '2021-07-17 23:32:18', NULL, '2021-07-17 23:32:18', NULL, NULL, '参数管理', 1, 1, 'SysParam',
        'ant-design:ant-design-outlined', 5, 1, '/sys/SysParam/index', '');
INSERT INTO "sys_menu"
VALUES (28, 0, 1, 0, '2021-07-17 23:32:19', NULL, '2021-07-17 23:32:19', NULL, NULL, '查询', 27, 2, 'SysParam:retrieve',
        NULL, 1, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (29, 0, 1, 0, '2021-07-17 23:32:19', NULL, '2021-07-17 23:32:19', NULL, NULL, '新增', 27, 2, 'SysParam:create',
        NULL, 2, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (30, 0, 1, 0, '2021-07-17 23:32:19', NULL, '2021-07-17 23:32:19', NULL, NULL, '删除', 27, 2, 'SysParam:delete',
        NULL, 3, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (31, 0, 1, 0, '2021-07-17 23:32:19', NULL, '2021-07-17 23:32:19', NULL, NULL, '编辑', 27, 2, 'SysParam:update',
        NULL, 4, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (32, 0, 1, 0, '2021-07-17 23:32:19', NULL, '2021-07-17 23:32:19', NULL, NULL, '租户管理', 1, 1, 'SysTenant',
        'ant-design:aliyun-outlined', 6, 1, '/sys/SysTenant/index', '');
INSERT INTO "sys_menu"
VALUES (33, 0, 1, 0, '2021-07-17 23:32:19', NULL, '2021-07-17 23:32:19', NULL, NULL, '查询', 32, 2, 'SysTenant:retrieve',
        NULL, 1, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (34, 0, 1, 0, '2021-07-17 23:32:20', NULL, '2021-07-17 23:32:20', NULL, NULL, '新增', 32, 2, 'SysTenant:create',
        NULL, 2, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (35, 0, 1, 0, '2021-07-17 23:32:20', NULL, '2021-07-17 23:32:20', NULL, NULL, '删除', 32, 2, 'SysTenant:delete',
        NULL, 3, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (36, 0, 1, 0, '2021-07-17 23:32:20', NULL, '2021-07-17 23:32:20', NULL, NULL, '编辑', 32, 2, 'SysTenant:update',
        NULL, 4, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (37, 0, 1, 0, '2021-07-17 23:32:20', NULL, '2021-07-17 23:32:20', NULL, NULL, '数据字典', 1, 1, 'SysDataDict',
        'ant-design:field-string-outlined', 7, 1, '/sys/SysDataDict/index', '');
INSERT INTO "sys_menu"
VALUES (38, 0, 1, 0, '2021-07-17 23:32:20', NULL, '2021-07-17 23:32:20', NULL, NULL, '查询', 37, 2,
        'SysDataDict:retrieve', NULL, 1, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (39, 0, 1, 0, '2021-07-17 23:32:21', NULL, '2021-07-17 23:32:21', NULL, NULL, '新增', 37, 2, 'SysDataDict:create',
        NULL, 2, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (40, 0, 1, 0, '2021-07-17 23:32:21', NULL, '2021-07-17 23:32:21', NULL, NULL, '删除', 37, 2, 'SysDataDict:delete',
        NULL, 3, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (41, 0, 1, 0, '2021-07-17 23:32:21', NULL, '2021-07-17 23:32:21', NULL, NULL, '编辑', 37, 2, 'SysDataDict:update',
        NULL, 4, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (42, 0, 1, 0, '2021-07-17 23:32:21', NULL, '2021-07-17 23:32:21', NULL, NULL, '系统日志', 1, 1, 'SysLog',
        'ant-design:edit-twotone', 8, 1, '/sys/SysLog/index', '');
INSERT INTO "sys_menu"
VALUES (43, 0, 1, 0, '2021-07-17 23:32:21', NULL, '2021-07-17 23:32:21', NULL, NULL, '查询', 42, 2, 'SysLog:retrieve',
        NULL, 1, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (44, 0, 1, 0, '2021-07-17 23:37:26', 'admin', '2021-07-17 23:38:28', 'admin', NULL, '修改当前用户密码', 1, 1, '',
        'ant-design:compass-outlined', 9, 1, '/sys/SysUser/change-password/index', '');
INSERT INTO "sys_menu"
VALUES (45, 0, 1, 0, '2021-07-17 23:39:41', 'admin', '2021-07-17 23:39:41', 'admin', NULL, '重置某用户密码', 17, 2,
        'SysUser:resetPassword', 'ant-design:redo-outlined', 5, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (46, 0, 1, 0, '2021-07-17 23:40:26', 'admin', '2021-07-17 23:40:26', 'admin', NULL, '绑定用户与角色关联关系', 17, 2,
        'SysUser:bindRoles', 'ant-design:share-alt-outlined', 6, 1, NULL, '');
INSERT INTO "sys_menu"
VALUES (47, 0, 1, 0, '2021-07-17 23:40:47', 'admin', '2021-07-17 23:41:15', 'admin', NULL, '踢某用户下线', 17, 2,
        'SysUser:kickOut', 'ant-design:disconnect-outlined', 7, 1, 'LAYOUT', '');

-- ----------------------------
-- Table structure for sys_param
-- ----------------------------
DROP TABLE IF EXISTS "sys_param";
CREATE TABLE "sys_param"
(
    "id"          int8         NOT NULL,
    "tenant_id"   int8,
    "revision"    int8         NOT NULL DEFAULT 1,
    "del_flag"    int2         NOT NULL DEFAULT 0,
    "created_at"  timestamp(6) NOT NULL,
    "created_by"  varchar(255),
    "updated_at"  timestamp(6) NOT NULL,
    "updated_by"  varchar(255),
    "remark"      varchar(255),
    "name"        varchar(50)  NOT NULL,
    "value"       varchar(255) NOT NULL,
    "description" varchar(255) NOT NULL
)
;
COMMENT
ON COLUMN "sys_param"."id" IS '主键ID';
COMMENT
ON COLUMN "sys_param"."tenant_id" IS '租户ID';
COMMENT
ON COLUMN "sys_param"."revision" IS '乐观锁';
COMMENT
ON COLUMN "sys_param"."del_flag" IS '逻辑删除标识';
COMMENT
ON COLUMN "sys_param"."created_at" IS '创建时刻';
COMMENT
ON COLUMN "sys_param"."created_by" IS '创建者';
COMMENT
ON COLUMN "sys_param"."updated_at" IS '更新时刻';
COMMENT
ON COLUMN "sys_param"."updated_by" IS '更新者';
COMMENT
ON COLUMN "sys_param"."remark" IS '备注';
COMMENT
ON COLUMN "sys_param"."name" IS '键名';
COMMENT
ON COLUMN "sys_param"."value" IS '键值';
COMMENT
ON COLUMN "sys_param"."description" IS '描述';
COMMENT
ON TABLE "sys_param" IS '系统参数';

-- ----------------------------
-- Records of sys_param
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS "sys_role";
CREATE TABLE "sys_role"
(
    "id"         int8         NOT NULL,
    "tenant_id"  int8,
    "revision"   int8         NOT NULL DEFAULT 1,
    "del_flag"   int2         NOT NULL DEFAULT 0,
    "created_at" timestamp(6) NOT NULL,
    "created_by" varchar(255),
    "updated_at" timestamp(6) NOT NULL,
    "updated_by" varchar(255),
    "remark"     varchar(255),
    "title"      varchar(50)  NOT NULL,
    "value"      varchar(100) NOT NULL
)
;
COMMENT
ON COLUMN "sys_role"."id" IS '主键ID';
COMMENT
ON COLUMN "sys_role"."tenant_id" IS '租户ID';
COMMENT
ON COLUMN "sys_role"."revision" IS '乐观锁';
COMMENT
ON COLUMN "sys_role"."del_flag" IS '逻辑删除标识';
COMMENT
ON COLUMN "sys_role"."created_at" IS '创建时刻';
COMMENT
ON COLUMN "sys_role"."created_by" IS '创建者';
COMMENT
ON COLUMN "sys_role"."updated_at" IS '更新时刻';
COMMENT
ON COLUMN "sys_role"."updated_by" IS '更新者';
COMMENT
ON COLUMN "sys_role"."remark" IS '备注';
COMMENT
ON COLUMN "sys_role"."title" IS '名称';
COMMENT
ON COLUMN "sys_role"."value" IS '值';
COMMENT
ON TABLE "sys_role" IS '后台角色';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO "sys_role"
VALUES (1, 0, 1, 0, '2021-04-17 02:34:30', NULL, '2021-06-07 16:22:28', 'admin', NULL, '超级管理员(代码中固定拥有所有菜单的权限)',
        'SuperAdmin');

-- ----------------------------
-- Table structure for sys_role_menu_relation
-- ----------------------------
DROP TABLE IF EXISTS "sys_role_menu_relation";
CREATE TABLE "sys_role_menu_relation"
(
    "id"         int8         NOT NULL,
    "tenant_id"  int8,
    "revision"   int8         NOT NULL DEFAULT 1,
    "del_flag"   int2         NOT NULL DEFAULT 0,
    "created_at" timestamp(6) NOT NULL,
    "created_by" varchar(255),
    "updated_at" timestamp(6) NOT NULL,
    "updated_by" varchar(255),
    "remark"     varchar(255),
    "role_id"    int8         NOT NULL,
    "menu_id"    int8         NOT NULL
)
;
COMMENT
ON COLUMN "sys_role_menu_relation"."id" IS '主键ID';
COMMENT
ON COLUMN "sys_role_menu_relation"."tenant_id" IS '租户ID';
COMMENT
ON COLUMN "sys_role_menu_relation"."revision" IS '乐观锁';
COMMENT
ON COLUMN "sys_role_menu_relation"."del_flag" IS '逻辑删除标识';
COMMENT
ON COLUMN "sys_role_menu_relation"."created_at" IS '创建时刻';
COMMENT
ON COLUMN "sys_role_menu_relation"."created_by" IS '创建者';
COMMENT
ON COLUMN "sys_role_menu_relation"."updated_at" IS '更新时刻';
COMMENT
ON COLUMN "sys_role_menu_relation"."updated_by" IS '更新者';
COMMENT
ON COLUMN "sys_role_menu_relation"."remark" IS '备注';
COMMENT
ON COLUMN "sys_role_menu_relation"."role_id" IS '角色ID';
COMMENT
ON COLUMN "sys_role_menu_relation"."menu_id" IS '菜单ID';
COMMENT
ON TABLE "sys_role_menu_relation" IS '后台角色-可见菜单关联';

-- ----------------------------
-- Records of sys_role_menu_relation
-- ----------------------------

-- ----------------------------
-- Table structure for sys_tenant
-- ----------------------------
DROP TABLE IF EXISTS "sys_tenant";
CREATE TABLE "sys_tenant"
(
    "id"                   int8         NOT NULL,
    "revision"             int8         NOT NULL DEFAULT 1,
    "del_flag"             int2         NOT NULL DEFAULT 0,
    "created_at"           timestamp(6) NOT NULL,
    "created_by"           varchar(255),
    "updated_at"           timestamp(6) NOT NULL,
    "updated_by"           varchar(255),
    "remark"               varchar(255),
    "tenant_name"          varchar(50)  NOT NULL,
    "tenant_id"            int8,
    "status"               int4         NOT NULL,
    "tenant_admin_user_id" int8
)
;
COMMENT
ON COLUMN "sys_tenant"."id" IS '主键ID';
COMMENT
ON COLUMN "sys_tenant"."revision" IS '乐观锁';
COMMENT
ON COLUMN "sys_tenant"."del_flag" IS '逻辑删除标识';
COMMENT
ON COLUMN "sys_tenant"."created_at" IS '创建时刻';
COMMENT
ON COLUMN "sys_tenant"."created_by" IS '创建者';
COMMENT
ON COLUMN "sys_tenant"."updated_at" IS '更新时刻';
COMMENT
ON COLUMN "sys_tenant"."updated_by" IS '更新者';
COMMENT
ON COLUMN "sys_tenant"."remark" IS '备注';
COMMENT
ON COLUMN "sys_tenant"."tenant_name" IS '租户名';
COMMENT
ON COLUMN "sys_tenant"."tenant_id" IS '租户ID';
COMMENT
ON COLUMN "sys_tenant"."status" IS '状态(0=禁用 1=启用)';
COMMENT
ON COLUMN "sys_tenant"."tenant_admin_user_id" IS '租户管理员用户ID';
COMMENT
ON TABLE "sys_tenant" IS '系统租户';

-- ----------------------------
-- Records of sys_tenant
-- ----------------------------
INSERT INTO "sys_tenant"
VALUES (1, 1, 0, '2021-05-11 16:06:47', '', '2021-06-10 16:29:32', 'admin', '可无视SQL拦截器', '超级租户', 0, 1, 1);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS "sys_user";
CREATE TABLE "sys_user"
(
    "id"            int8         NOT NULL,
    "tenant_id"     int8,
    "revision"      int8         NOT NULL DEFAULT 1,
    "del_flag"      int2         NOT NULL DEFAULT 0,
    "created_at"    timestamp(6) NOT NULL,
    "created_by"    varchar(255),
    "updated_at"    timestamp(6) NOT NULL,
    "updated_by"    varchar(255),
    "remark"        varchar(255),
    "pin"           varchar(20)  NOT NULL,
    "pwd"           varchar(255) NOT NULL,
    "salt"          varchar(64)  NOT NULL,
    "nickname"      varchar(20)  NOT NULL,
    "status"        int4         NOT NULL,
    "gender"        int4,
    "email"         varchar(255),
    "phone_no"      varchar(20),
    "last_login_at" timestamp(6)
)
;
COMMENT
ON COLUMN "sys_user"."id" IS '主键ID';
COMMENT
ON COLUMN "sys_user"."tenant_id" IS '租户ID';
COMMENT
ON COLUMN "sys_user"."revision" IS '乐观锁';
COMMENT
ON COLUMN "sys_user"."del_flag" IS '逻辑删除标识';
COMMENT
ON COLUMN "sys_user"."created_at" IS '创建时刻';
COMMENT
ON COLUMN "sys_user"."created_by" IS '创建者';
COMMENT
ON COLUMN "sys_user"."updated_at" IS '更新时刻';
COMMENT
ON COLUMN "sys_user"."updated_by" IS '更新者';
COMMENT
ON COLUMN "sys_user"."remark" IS '备注';
COMMENT
ON COLUMN "sys_user"."pin" IS '账号';
COMMENT
ON COLUMN "sys_user"."pwd" IS '密码';
COMMENT
ON COLUMN "sys_user"."salt" IS '盐';
COMMENT
ON COLUMN "sys_user"."nickname" IS '昵称';
COMMENT
ON COLUMN "sys_user"."status" IS '状态(0=禁用 1=启用)';
COMMENT
ON COLUMN "sys_user"."gender" IS '性别';
COMMENT
ON COLUMN "sys_user"."email" IS '邮箱';
COMMENT
ON COLUMN "sys_user"."phone_no" IS '手机号';
COMMENT
ON COLUMN "sys_user"."last_login_at" IS '最后登录时刻';
COMMENT
ON TABLE "sys_user" IS '后台用户';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO "sys_user"
VALUES (1, 0, 1, 0, '2021-04-06 09:56:02', NULL, '2021-06-28 16:37:07', 'admin', '', 'admin',
        'b7f6692251833658a56ff2d09e9835b91384d203721bf27a544586d3c9a379ff', '6a8e9339-dfd1-4cfe-80cb-30ca9fc3d81e',
        '超级管理员', 1, 0, 'admin@example.com', '17874585544', '2021-07-18 00:32:08');

-- ----------------------------
-- Table structure for sys_user_dept_relation
-- ----------------------------
DROP TABLE IF EXISTS "sys_user_dept_relation";
CREATE TABLE "sys_user_dept_relation"
(
    "id"         int8         NOT NULL,
    "tenant_id"  int8,
    "revision"   int8         NOT NULL DEFAULT 1,
    "del_flag"   int2         NOT NULL DEFAULT 0,
    "created_at" timestamp(6) NOT NULL,
    "created_by" varchar(255),
    "updated_at" timestamp(6) NOT NULL,
    "updated_by" varchar(255),
    "remark"     varchar(255),
    "user_id"    int8         NOT NULL,
    "dept_id"    int8         NOT NULL
)
;
COMMENT
ON COLUMN "sys_user_dept_relation"."id" IS '主键ID';
COMMENT
ON COLUMN "sys_user_dept_relation"."tenant_id" IS '租户ID';
COMMENT
ON COLUMN "sys_user_dept_relation"."revision" IS '乐观锁';
COMMENT
ON COLUMN "sys_user_dept_relation"."del_flag" IS '逻辑删除标识';
COMMENT
ON COLUMN "sys_user_dept_relation"."created_at" IS '创建时刻';
COMMENT
ON COLUMN "sys_user_dept_relation"."created_by" IS '创建者';
COMMENT
ON COLUMN "sys_user_dept_relation"."updated_at" IS '更新时刻';
COMMENT
ON COLUMN "sys_user_dept_relation"."updated_by" IS '更新者';
COMMENT
ON COLUMN "sys_user_dept_relation"."remark" IS '备注';
COMMENT
ON COLUMN "sys_user_dept_relation"."user_id" IS '用户ID';
COMMENT
ON COLUMN "sys_user_dept_relation"."dept_id" IS '部门ID';
COMMENT
ON TABLE "sys_user_dept_relation" IS '后台用户-部门关联';

-- ----------------------------
-- Records of sys_user_dept_relation
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user_role_relation
-- ----------------------------
DROP TABLE IF EXISTS "sys_user_role_relation";
CREATE TABLE "sys_user_role_relation"
(
    "id"         int8         NOT NULL,
    "tenant_id"  int8,
    "revision"   int8         NOT NULL DEFAULT 1,
    "del_flag"   int2         NOT NULL DEFAULT 0,
    "created_at" timestamp(6) NOT NULL,
    "created_by" varchar(255),
    "updated_at" timestamp(6) NOT NULL,
    "updated_by" varchar(255),
    "remark"     varchar(255),
    "user_id"    int8         NOT NULL,
    "role_id"    int8         NOT NULL
)
;
COMMENT
ON COLUMN "sys_user_role_relation"."id" IS '主键ID';
COMMENT
ON COLUMN "sys_user_role_relation"."tenant_id" IS '租户ID';
COMMENT
ON COLUMN "sys_user_role_relation"."revision" IS '乐观锁';
COMMENT
ON COLUMN "sys_user_role_relation"."del_flag" IS '逻辑删除标识';
COMMENT
ON COLUMN "sys_user_role_relation"."created_at" IS '创建时刻';
COMMENT
ON COLUMN "sys_user_role_relation"."created_by" IS '创建者';
COMMENT
ON COLUMN "sys_user_role_relation"."updated_at" IS '更新时刻';
COMMENT
ON COLUMN "sys_user_role_relation"."updated_by" IS '更新者';
COMMENT
ON COLUMN "sys_user_role_relation"."remark" IS '备注';
COMMENT
ON COLUMN "sys_user_role_relation"."user_id" IS '用户ID';
COMMENT
ON COLUMN "sys_user_role_relation"."role_id" IS '角色ID';
COMMENT
ON TABLE "sys_user_role_relation" IS '后台用户-角色关联';

-- ----------------------------
-- Records of sys_user_role_relation
-- ----------------------------
INSERT INTO "sys_user_role_relation"
VALUES (1, 0, 1, 0, '2021-01-01 18:22:12', NULL, '2021-01-01 18:22:12', NULL, NULL, 1, 1);

-- ----------------------------
-- Table structure for 数据表模板
-- ----------------------------
DROP TABLE IF EXISTS "数据表模板";
CREATE TABLE "数据表模板"
(
    "id"         int8         NOT NULL,
    "tenant_id"  int8,
    "revision"   int8         NOT NULL DEFAULT 1,
    "del_flag"   int2         NOT NULL DEFAULT 0,
    "created_at" timestamp(6) NOT NULL,
    "created_by" varchar(255),
    "updated_at" timestamp(6) NOT NULL,
    "updated_by" varchar(255),
    "remark"     varchar(255)
)
;
COMMENT
ON COLUMN "数据表模板"."id" IS '主键ID';
COMMENT
ON COLUMN "数据表模板"."tenant_id" IS '租户ID';
COMMENT
ON COLUMN "数据表模板"."revision" IS '乐观锁';
COMMENT
ON COLUMN "数据表模板"."del_flag" IS '逻辑删除标识';
COMMENT
ON COLUMN "数据表模板"."created_at" IS '创建时刻';
COMMENT
ON COLUMN "数据表模板"."created_by" IS '创建者';
COMMENT
ON COLUMN "数据表模板"."updated_at" IS '更新时刻';
COMMENT
ON COLUMN "数据表模板"."updated_by" IS '更新者';
COMMENT
ON COLUMN "数据表模板"."remark" IS '备注';
COMMENT
ON TABLE "数据表模板" IS '数据表注释';

-- ----------------------------
-- Records of 数据表模板
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table sys_data_dict
-- ----------------------------
ALTER TABLE "sys_data_dict"
    ADD CONSTRAINT "sys_data_dict_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_dept
-- ----------------------------
ALTER TABLE "sys_dept"
    ADD CONSTRAINT "sys_dept_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_log
-- ----------------------------
ALTER TABLE "sys_log"
    ADD CONSTRAINT "sys_log_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_menu
-- ----------------------------
ALTER TABLE "sys_menu"
    ADD CONSTRAINT "sys_menu_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_param
-- ----------------------------
ALTER TABLE "sys_param"
    ADD CONSTRAINT "sys_param_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_role
-- ----------------------------
ALTER TABLE "sys_role"
    ADD CONSTRAINT "sys_role_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_role_menu_relation
-- ----------------------------
ALTER TABLE "sys_role_menu_relation"
    ADD CONSTRAINT "sys_role_menu_relation_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_tenant
-- ----------------------------
ALTER TABLE "sys_tenant"
    ADD CONSTRAINT "sys_tenant_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_user
-- ----------------------------
ALTER TABLE "sys_user"
    ADD CONSTRAINT "sys_user_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_user_dept_relation
-- ----------------------------
ALTER TABLE "sys_user_dept_relation"
    ADD CONSTRAINT "sys_user_dept_relation_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_user_role_relation
-- ----------------------------
ALTER TABLE "sys_user_role_relation"
    ADD CONSTRAINT "sys_user_role_relation_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table 数据表模板
-- ----------------------------
ALTER TABLE "数据表模板"
    ADD CONSTRAINT "数据表模板_pkey" PRIMARY KEY ("id");
