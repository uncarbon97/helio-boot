-- 新增'头像URL'字段
ALTER TABLE sys_user ADD COLUMN avatar_url varchar(255);

COMMENT ON COLUMN sys_user.avatar_url IS '头像URL';
