-- 重构数据字典，拆分为`sys_data_dict_classified`（数据字典分类）和`sys_data_dict_item`（数据字典项），并重新设计字段
CREATE TABLE `sys_data_dict_classified`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `tenant_id` bigint(20) NULL DEFAULT NULL COMMENT '租户ID',
  `revision` bigint(20) NOT NULL DEFAULT 1 COMMENT '乐观锁',
  `del_flag` tinyint(4) UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除标识',
  `created_at` datetime NOT NULL COMMENT '创建时刻',
  `created_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `updated_at` datetime NOT NULL COMMENT '更新时刻',
  `updated_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类编码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名称',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分类描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据字典分类' ROW_FORMAT = Dynamic;

CREATE TABLE `sys_data_dict_item`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `tenant_id` bigint(20) NULL DEFAULT NULL COMMENT '租户ID',
  `revision` bigint(20) NOT NULL DEFAULT 1 COMMENT '乐观锁',
  `del_flag` tinyint(4) UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除标识',
  `created_at` datetime NOT NULL COMMENT '创建时刻',
  `created_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `updated_at` datetime NOT NULL COMMENT '更新时刻',
  `updated_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `classified_id` bigint(20) NOT NULL COMMENT '所属分类ID',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典项编码',
  `label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典项标签',
  `value` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典项值',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态',
  `sort` int(11) NOT NULL DEFAULT 1 COMMENT '排序',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据字典项' ROW_FORMAT = Dynamic;
