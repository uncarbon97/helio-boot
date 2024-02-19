-- v1.11.0 - 「后台角色」中，`title`字段更名为「角色名」，`value`字段更名为「角色编码」
ALTER TABLE sys_role
    MODIFY COLUMN title varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名',
    MODIFY COLUMN `value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色编码';

-- v1.11.0 - 「数据字典」中，`value`字段更名为「数据值」
ALTER TABLE sys_data_dict
    MODIFY COLUMN `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据值';

-- v1.11.0 - 扩充「后台用户」数据表，「昵称」字段的长度上限为255
ALTER TABLE sys_user
    MODIFY COLUMN `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '昵称';
