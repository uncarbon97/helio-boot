-- 新增'头像URL'字段
ALTER TABLE sys_user ADD COLUMN avatar_url varchar(255) NULL COMMENT '头像URL';
