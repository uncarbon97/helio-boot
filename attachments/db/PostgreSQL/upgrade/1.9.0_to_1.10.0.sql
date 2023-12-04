-- v1.10.0 - 补充遗漏的数据表默认值
ALTER TABLE "oss_file_info"
    ALTER COLUMN "revision" SET DEFAULT 1,
    ALTER COLUMN "del_flag" SET DEFAULT 0;
