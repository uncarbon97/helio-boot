-- 重构数据字典，拆分为`sys_data_dict_classified`（数据字典分类）和`sys_data_dict_item`（数据字典项），并重新设计字段
CREATE TABLE "sys_data_dict_classified" (
  "id" int8 NOT NULL,
  "tenant_id" int8,
  "revision" int8 NOT NULL DEFAULT 1,
  "del_flag" int2 NOT NULL DEFAULT 0,
  "created_at" timestamp(6) NOT NULL,
  "created_by" varchar(255) COLLATE "pg_catalog"."default",
  "updated_at" timestamp(6) NOT NULL,
  "updated_by" varchar(255) COLLATE "pg_catalog"."default",
  "code" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "status" int2 NOT NULL DEFAULT 1,
  "description" varchar(255) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying
);
COMMENT ON COLUMN "sys_data_dict_classified"."id" IS '主键ID';
COMMENT ON COLUMN "sys_data_dict_classified"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "sys_data_dict_classified"."revision" IS '乐观锁';
COMMENT ON COLUMN "sys_data_dict_classified"."del_flag" IS '逻辑删除标识';
COMMENT ON COLUMN "sys_data_dict_classified"."created_at" IS '创建时刻';
COMMENT ON COLUMN "sys_data_dict_classified"."created_by" IS '创建者';
COMMENT ON COLUMN "sys_data_dict_classified"."updated_at" IS '更新时刻';
COMMENT ON COLUMN "sys_data_dict_classified"."updated_by" IS '更新者';
COMMENT ON COLUMN "sys_data_dict_classified"."code" IS '分类编码';
COMMENT ON COLUMN "sys_data_dict_classified"."name" IS '分类名称';
COMMENT ON COLUMN "sys_data_dict_classified"."status" IS '状态';
COMMENT ON COLUMN "sys_data_dict_classified"."description" IS '分类描述';
COMMENT ON TABLE "sys_data_dict_classified" IS '数据字典分类';
ALTER TABLE "sys_data_dict_classified" ADD PRIMARY KEY ("id");

CREATE TABLE "sys_data_dict_item" (
  "id" int8 NOT NULL,
  "tenant_id" int8,
  "revision" int8 NOT NULL DEFAULT 1,
  "del_flag" int2 NOT NULL DEFAULT 0,
  "created_at" timestamp(6) NOT NULL,
  "created_by" varchar(255) COLLATE "pg_catalog"."default",
  "updated_at" timestamp(6) NOT NULL,
  "updated_by" varchar(255) COLLATE "pg_catalog"."default",
  "classified_id" int8 NOT NULL,
  "code" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "label" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "value" varchar(4096) COLLATE "pg_catalog"."default" NOT NULL,
  "status" int2 NOT NULL DEFAULT 1,
  "sort" int4 NOT NULL DEFAULT 1,
  "description" varchar(255) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying
);
COMMENT ON COLUMN "sys_data_dict_item"."id" IS '主键ID';
COMMENT ON COLUMN "sys_data_dict_item"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "sys_data_dict_item"."revision" IS '乐观锁';
COMMENT ON COLUMN "sys_data_dict_item"."del_flag" IS '逻辑删除标识';
COMMENT ON COLUMN "sys_data_dict_item"."created_at" IS '创建时刻';
COMMENT ON COLUMN "sys_data_dict_item"."created_by" IS '创建者';
COMMENT ON COLUMN "sys_data_dict_item"."updated_at" IS '更新时刻';
COMMENT ON COLUMN "sys_data_dict_item"."updated_by" IS '更新者';
COMMENT ON COLUMN "sys_data_dict_item"."classified_id" IS '所属分类ID';
COMMENT ON COLUMN "sys_data_dict_item"."code" IS '字典项编码';
COMMENT ON COLUMN "sys_data_dict_item"."label" IS '字典项标签';
COMMENT ON COLUMN "sys_data_dict_item"."value" IS '字典项值';
COMMENT ON COLUMN "sys_data_dict_item"."status" IS '状态';
COMMENT ON COLUMN "sys_data_dict_item"."sort" IS '排序';
COMMENT ON COLUMN "sys_data_dict_item"."description" IS '描述';
COMMENT ON TABLE "sys_data_dict_item" IS '数据字典项';
ALTER TABLE "sys_data_dict_item" ADD PRIMARY KEY ("id");
