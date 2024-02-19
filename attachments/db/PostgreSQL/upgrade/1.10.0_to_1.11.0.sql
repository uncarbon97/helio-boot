-- v1.11.0 - 「后台角色」中，`title`字段更名为「角色名」，`value`字段更名为「角色编码」
COMMENT ON COLUMN "sys_role"."title" IS '角色名';
COMMENT ON COLUMN "sys_role"."value" IS '角色编码';

-- v1.11.0 - 「数据字典」中，`value`字段更名为「数据值」
COMMENT ON COLUMN "sys_data_dict"."value" IS '数据值';

-- v1.11.0 - 扩充「后台用户」数据表，「昵称」字段的长度上限为255
ALTER TABLE "sys_user"
    ALTER COLUMN "nickname" TYPE varchar(255);
