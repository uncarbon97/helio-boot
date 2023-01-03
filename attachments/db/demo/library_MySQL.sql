SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 建表: tb_book
-- ----------------------------
DROP TABLE IF EXISTS `tb_book`;
CREATE TABLE `tb_book`
(
    `id`                 bigint(20) NOT NULL COMMENT '主键ID',
    `tenant_id`          bigint(20) NULL DEFAULT NULL COMMENT '租户ID',
    `revision`           bigint(20) NOT NULL DEFAULT 1 COMMENT '乐观锁',
    `del_flag`           tinyint(4) UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除标识',
    `created_at`         datetime(0) NOT NULL COMMENT '创建时刻',
    `created_by`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
    `updated_at`         datetime(0) NOT NULL COMMENT '更新时刻',
    `updated_by`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
    `title`              varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '书籍名',
    `isbn`               varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT 'ISBN号',
    `publisher`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '出版社',
    `author`             varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '作者名',
    `book_classified_id` bigint(20) NOT NULL COMMENT '所属书籍类别ID',
    `unit_price`         decimal(10, 2)                                                NOT NULL COMMENT '单价',
    `status`             tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态',
    `cover_img_url`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '封面图片URL',
    `description`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '描述',
    `total_quantity`     int(11) NOT NULL DEFAULT 0 COMMENT '总数量',
    `borrowed_quantity`  int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '被借阅数量',
    `damaged_quantity`   int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '被损坏数量',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX                `idx_book_isbn`(`isbn`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '书籍' ROW_FORMAT = Dynamic;

-- ----------------------------
-- 数据: tb_book
-- ----------------------------
INSERT INTO `tb_book`
VALUES (138652613116792832, NULL, 1, 0, '2022-08-03 14:35:59', 'admin', '2022-08-23 14:54:27', 'admin',
        '平凡的世界：全三册', '9787530221396', '北京十月文艺出版社', '路遥', 137851704950493184, 98.00, 1,
        '//img12.360buyimg.com/n1/jfs/t1/185002/33/14180/99841/60efaf53Ed0b8fe0c/b7db5ad1e7067cc1.jpg',
        '《平凡的世界（全三部）》是路遥的长篇代表作，曾获第三届茅盾文学奖，并入选“新中国70年70部长篇小说典藏”，得到各界名家的一致推荐。\n\n《平凡的世界（全三部）》是一段平凡却热血、温暖又动人的成长故事，小说以孙少平与孙少安两兄弟为主角，讲述他们在生活中面临重重困难与挑战，却依然无畏前行的人生历程。《平凡的世界》深刻展示了普通人在大时代中走过的平凡却不平庸的道路，细腻书写亲情、爱情和友情，饱含真善美和昂扬向上的力量，今天读来依然让人充满共鸣，更给人以激励。《平凡的世界》也是一部跨时代的经典，在反映时代的同时超_越时代，散发出经久不衰的魅力。',
        10, 1, 0);
INSERT INTO `tb_book`
VALUES (138931161622745088, NULL, 1, 0, '2022-08-04 09:02:50', 'admin', '2022-08-23 15:12:38', 'admin',
        '三体（1）地球往事', '9787536692930', '重庆出版社', '刘慈欣', 137851704950493184, 12.20, 1,
        '//img12.360buyimg.com//n0/jfs/t1/111047/8/3031/632157/5ea54c63Ece345dce/062178f1f70139e6.jpg',
        '《三体》讲的是在文化大革命如火如荼进行的同时，军方探寻外星文明的绝秘计划“红岸工程”取得了突破性进展。但在按下发射键的那一刻，历经劫难的叶文洁没有意识到，她彻底改变了人类的命运。地球文明向宇宙发出的头一声啼鸣，以太阳为中心，以光速向宇宙深处飞驰……\\n\\n四光年外，“三体文明”正苦苦挣扎——三颗无规则运行的太阳主导下的百余次毁灭与重生逼迫他们逃离母星。而恰在此时，他们接收到了地球发来的信息。\\n\\n在运用超技术锁死地球人的基础科学之后，三体人庞大的宇宙舰队开始向地球进发……人类的末日悄然来临。',
        10, 0, 1);
INSERT INTO `tb_book`
VALUES (138960565354729472, NULL, 1, 0, '2022-08-04 10:59:41', 'admin', '2022-08-04 10:59:41', 'admin',
        '时间从来不语，却回答了所有问题', '9787550043657', '百花洲文艺出版社', '季羡林', 137949673372618752, 55.00, 1,
        '//img10.360buyimg.com/n1/jfs/t1/80087/2/18645/136037/62bd05bcE8b173d9e/ae325bddbbec2a65.jpg',
        '这本《时间从来不语，却回答了所有问题》分为5个部分，40篇文章，分别从对时间流逝的坦然、对人生意义的思考，对社会现象的看法，对真实自我的表达，对人间真情的热爱，传递出季羡林先生的人生观和价值观。\\n\\n季羡林先生是当代当之无愧的文学巨擘，在世事沉浮中始终坚守自己的精神世界，顺境不骄，逆境不惧。与大师共语，品味人生百味，这些文章涉及季老生活的方方面面，是他一生经历的缩影，经过岁月的沉淀和发酵，诠释出大师的百年智慧。季羡林对人生的参悟和豁达的处事态度，或许我们还难以抵达，但我们仍能不停追寻。',
        11, 0, 0);
INSERT INTO `tb_book`
VALUES (138960840178110464, NULL, 1, 0, '2022-08-04 11:00:46', 'admin', '2022-08-23 15:10:52', 'admin', '心安即是归处',
        '9787554615430', '古吴轩出版社', '季羡林', 137949673372618752, 26.00, 1,
        '//img11.360buyimg.com/n1/jfs/t1/124564/33/2321/44592/5ec4e742E1ac8fcbe/9d953109e5374410.jpg',
        '季羡林经历过人生的大苦大悲，生命的跌宕起伏。然而先生的一生，不争不辩，不怨不艾，满怀天真，执着自己的执着，安于当下。是世界上仅有的精通于吐火罗文的几位学者之一，同时又用质朴的文字向世人传达一个理念——心安即是生命的归处。\\n\\n本书旨在阐释先生的生命智慧，从谈人生的意义到分别谈读书、处世、行走、当下、孤独、生死等跟大家密切相关的生命话题。高山仰止，景行行止。愿我们能了悟人间万相的本真，拥有应对世事的智慧。万事安然于心，从容而行。',
        11, 1, 0);
INSERT INTO `tb_book`
VALUES (138965433805017088, NULL, 1, 0, '2022-08-04 11:19:01', 'admin', '2022-09-23 15:10:32', 'admin', '历史学是什么',
        '9787301258996', '北京大学出版社', '葛剑雄，周筱贇', 137950299498319872, 32.00, 1,
        '//img14.360buyimg.com//n0/jfs/t2374/249/434080646/277103/78e35b8/560b3b56N77efcca1.jpg',
        '历史不仅是指过去的事实本身，更是指人们对过去事实的有意识、有选择的记录。而对于历史的专门性研究，就是历史学，它不仅包括历史本身，还应该包括在历史事实的基础上研究和总结历史发展的规律，以及总结研究历史的方法和理论。《历史学是什么》一书作者围绕“历史”“历史学”“中国历史”等核心词汇高屋建瓴，结合普通读者熟悉的多个案例深入浅出地呈现了一个学科的精彩面貌。',
        20, 0, 0);
INSERT INTO `tb_book`
VALUES (138965680723693568, NULL, 1, 0, '2022-08-04 11:20:00', 'admin', '2022-08-04 11:20:00', 'admin',
        '万古江河：中国历史文化的转折与开展', '9787556116904', '湖南人民出版社', '许倬云', 137950299498319872, 53.00, 1,
        '//img13.360buyimg.com//n0/jfs/t13078/256/1211285573/514847/fa5b728c/5a1d048fN58c003e9.jpg',
        '在世界文明存在与变化的剧烈大潮之中，中国如何自处，如何看待自身的历史与文化？中国从何处来，中国文化从何处来，又要向何处去？\\n\\n这本《万古江河》即心怀中国文化的著名史家许倬云先生交出的一份答卷，也是为今天中国人撰写的历史，为中国文化的成长发展作的一部传记。随着历史的进展，中国文化的内容与中国文化占有的空间不断发生变化：从考古发现所见的中国文化的发端，到“中原”的出现，中原变成了中国，“中原的中国”慢慢扩张变成“中国的中国”，然后超*中国之外，慢慢将四邻吸收进来，通过文化上的交往以及势力范围的扩大，变成了“东亚的中国”，然后在亚洲范围之内扮演一个重要的角色，是“亚洲的中国”，再经过百多年颠簸与蹒跚进入世界，成为“世界的中国”。在对这各个超*王朝、政权的长时段文化圈的叙述中，中国文化和生长在中国这片土地上的人才是真正的主角，所以许先生围绕国家体制与时代特色，思想、宗教与文化变迁，农业、手工业与经济网络，民族、文化的融合与互动，中国人的日常生活，生活方式与生活资源，民间社会与信仰世界，文化、科学技术的进步转变，对外关系及与他者文明的比较互动等一系列主题展开。在在讲述着中国文化和中国人多彩鲜活的成长故事。\\n\\n更可注意者，每一个阶段，中国都要面对别的族群及其缔造的文化，经过不断的接触与交流，或迎或拒，终于改变了“自己”，也改变了邻居族群的文化，甚至“自己”还与“别人”融合为一个新的“自己”。',
        12, 0, 0);
INSERT INTO `tb_book`
VALUES (138966023842926592, NULL, 1, 0, '2022-08-04 11:21:22', 'admin', '2022-08-04 11:21:22', 'admin', '亲密关系',
        '9787115390578', '人民邮电出版社', '罗兰·米勒', 137950336269783040, 88.00, 1,
        '//img10.360buyimg.com//n0/jfs/t1447/126/856470644/203409/d6f351d1/55af2c11Nc81edc4a.jpg',
        '亲密关系与泛泛之交有什么区别；大丈夫与小女子真的般配吗；吸引力的秘密是什么；男人与女人真的是不同的动物吗；同性恋真的是由基因决定的吗；单亲家庭的孩子长大后更容易离婚吗……什么是爱情？由什么构成？能持续多久？两性在发生一夜情及选择终身伴侣上有什么差异？爱情和性欲是由不同的脑区控制吗？亲密关系美满的秘诀是什么？有什么方法能让婚姻持续一生？米勒教授在《亲密关系（第6版）》中回答了这些问题，尤其澄清了通俗心理学所宣扬的经验之谈，甚至某些错误观点。\\n\\n《亲密关系（第6版）》汲取了社会心理学、沟通研究、家庭研究、认知心理学、发展心理学、演化心理学、社会学、传播学及家政学等学科的全新成果，研究实践和理论建构并重，学术标准与大众兴趣兼备。全书结构清晰、逻辑严密、语言生动、启发思考，既通俗易懂，读来轻松愉快，又科学专业，崇尚实证精神。本书遵循由浅入深、由一般到特殊的认知规律，论述了亲密关系的基础、活动形态、类型、矛盾和修复等内容，读完本书，你将对人际吸引、爱情、婚姻、承诺、友谊、激情、沟通、性爱、依恋、择偶、嫉妒、出轨、家暴等亲密关系的方方面面有全新的认识。\\n\\n亲密关系是人类经验的核心，处理得好能给人带来极大的快乐，处理得不好则会造成重大创伤，因此科学地认识亲密关系，攸关我们每个人的幸福。本书既适合研究亲密关系的专业人士，能给他们带来启发与灵感，也适合每个想爱情甜蜜、婚姻长久、人生幸福的普通读者。',
        12, 0, 0);
INSERT INTO `tb_book`
VALUES (138966356619005952, NULL, 1, 0, '2022-08-04 11:22:41', 'admin', '2022-08-04 11:22:41', 'admin', '非暴力沟通',
        '9787522200514', '华夏出版社有限公司', '马歇尔·卢森堡', 137950336269783040, 36.00, 1,
        '//img14.360buyimg.com/n1/jfs/t1/139036/19/26294/133276/61c98ea4Ec59a6e31/ef49b6c1fb158f87.jpg',
        '马歇尔·卢森堡博士发现了一种沟通方式，依照它来谈话和聆听，能使人们情意相通，和谐相处，这就是“非暴力沟通”。做为一个遵纪守法的好人，也许我们从来没有把谈话和“暴力”扯上关系。不过如果稍微留意一下现实生活中的谈话方式，并且用心体会各种谈话方式给我们的不同感受，我们一定会发现，有些话确实伤人！言语上的指责、嘲讽、否定、说教以及任意打断、拒不回应、随意出口的评价和结论给我们带来的情感和精神上的创伤甚至比肉体的伤害更加令人痛苦。\\n\\n这些无心或有意的语言暴力让人与人变得冷漠、隔膜、敌视。非暴力沟通能够：疗愈内心深处的隐秘伤痛；超越个人心智和情感的局限性；突破那些引发愤怒、沮丧、焦虑等负面情绪的思维方式；用不带伤害的方式化解人际间的冲突；学会建立和谐的生命体验。',
        13, 0, 0);
INSERT INTO `tb_book`
VALUES (138966738514579456, NULL, 1, 0, '2022-08-04 11:24:12', 'admin', '2022-08-04 11:24:12', 'admin',
        'Python编程 从入门到实践', '9787115546081', '人民邮电出版社', '埃里克·马瑟斯', 137950490779553792, 55.00, 1,
        '//img14.360buyimg.com//n0/jfs/t1/108659/4/24757/78634/62e5d6a5E65f82a22/18d72d71131f68eb.jpg',
        '本书是针对所有层次Python读者而作的Python入门书。全书分两部分：第一部分介绍用Python编程所必须了解的基本概念，包括强大的Python库和工具，以及列表、字典、if语句、类、文件与异常、代码测试等内容；第二部分将理论付诸实践，讲解如何开发三个项目，包括简单的2D游戏、利用数据生成交互式的信息图以及创建和定制简单的Web应用，并帮助读者解决常见编程问题和困惑。第2版进行了全面修订，简化了Python安装流程，新增了f字符串、get()方法等内容，并且在项目中使用了Plotly库以及新版本的Django和Bootstrap，等等。',
        14, 0, 0);
INSERT INTO `tb_book`
VALUES (138966983906529280, NULL, 1, 0, '2022-08-04 11:25:11', 'admin', '2022-08-04 11:25:11', 'admin', 'SQL必知必会',
        '9787115539168', '人民邮电出版社', '本·福达', 137950490779553792, 33.00, 1,
        '//img11.360buyimg.com//n0/jfs/t1/7722/18/18011/70503/62b1aa60E2fc1a89f/c46bf1ff93582c60.jpg',
        'SQL是使用最广泛的数据库语言，绝大多数重要的 DBMS 支持 SQL。本书由浅入深地讲解了SQL的基本概念和语法，涉及数据的排序、过滤和分组，以及表、视图、联结、子查询、游标、存储过程和触发器等内容，实例丰富，便于查阅。新版对书中的案例进行了全面的更新，并增加了章后挑战题，便于读者巩固所学知识。',
        14, 0, 0);
INSERT INTO `tb_book`
VALUES (138967268284534784, NULL, 1, 0, '2022-08-04 11:26:19', 'admin', '2022-08-04 11:26:19', 'admin', '中国建筑常识',
        '9787545543315', '天地出版社', '林徽因', 137950530642219008, 48.00, 1,
        '//img14.360buyimg.com/n1/jfs/t1/27887/20/7594/88154/5c6e0b10Eb95aa8e8/3f046f31339b4312.jpg',
        '本书精心选取林徽因关于建筑学方面的文章，包括中国古代建筑传统及其历史发展阶段的详细论述，以及外出考察古建筑的调查报告。既可以看做资深建筑学家的学术文章，也可以当成著名散文家有关建筑的优美篇章。书中亦附有专业图表，图文并茂，入之深而出之浅，既可作为建筑研究者的参考书，亦可成为普通读者的建筑学入门读物。',
        15, 0, 0);
INSERT INTO `tb_book`
VALUES (138967536627716096, NULL, 1, 0, '2022-08-04 11:27:23', 'admin', '2022-08-23 17:10:29', 'admin',
        '建筑工程施工做法一本就会', '9787122259981', '化学工业出版社', '筑·匠', 137950530642219008, 38.00, 1,
        '//img10.360buyimg.com/n1/jfs/t1/68/17/8134/514303/5ba45052Ef1d51a83/d95692f2319808cb.jpg',
        '《建筑工程施工做法一本就会》主要介绍了土建工程中各分部分项工程的施工步骤、施工做法、容易出现的问题、质量检验等内容，主要内容包括地基与基坑施工、桩基础施工、地下结构防水施工、脚手架及垂直运输施工、混凝土结构施工、砌体施工、屋面施工、装饰装修施工以及季节性施工等内容。全书内容的编写主要针对刚入行的施工员和技术员，以贴近现场的实用知识和现场图片，将建筑工程施工现场技术讲述明白，达到快速上手、参考施工的目的。在介绍基本施工技术的同时，辅以丰富的现场经验总结和指导，让读者能够更为全面地了解现场施工技术。\\n\\n《建筑工程施工做法一本就会》内容简明实用，图文并茂，实用性和实际操作性较强，可作为建筑工程技术人员和管理人员的参考用书，也可作为土建类相关专业大中专院校师生的参考教材。',
        15, 0, 0);

-- ----------------------------
-- 建表: tb_book_borrowing
-- ----------------------------
DROP TABLE IF EXISTS `tb_book_borrowing`;
CREATE TABLE `tb_book_borrowing`
(
    `id`                  bigint(20) NOT NULL COMMENT '主键ID',
    `tenant_id`           bigint(20) NULL DEFAULT NULL COMMENT '租户ID',
    `revision`            bigint(20) NOT NULL DEFAULT 1 COMMENT '乐观锁',
    `del_flag`            tinyint(4) UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除标识',
    `created_at`          datetime(0) NOT NULL COMMENT '创建时刻',
    `created_by`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
    `updated_at`          datetime(0) NOT NULL COMMENT '更新时刻',
    `updated_by`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
    `member_id`           bigint(20) NOT NULL COMMENT '会员ID',
    `member_username`     varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '会员学号/工号',
    `member_real_name`    varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '会员真实姓名',
    `book_id`             bigint(20) NOT NULL COMMENT '书籍ID',
    `book_title`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '书籍名',
    `book_isbn`           varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '书籍ISBN号',
    `status`              tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态',
    `quantity`            int(11) NOT NULL COMMENT '借阅数量',
    `borrow_at`           datetime(0) NOT NULL COMMENT '借阅时间',
    `borrow_duration`     int(11) NOT NULL COMMENT '借阅时长(天)',
    `appointed_return_at` date                                                          NOT NULL COMMENT '约定归还时间',
    `actual_return_at`    datetime(0) NULL DEFAULT NULL COMMENT '实际归还时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '书籍借阅记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- 数据: tb_book_borrowing
-- ----------------------------
INSERT INTO `tb_book_borrowing`
VALUES (142405472396255232, NULL, 1, 0, '2022-08-13 23:08:30', 'admin', '2022-08-23 14:54:27', 'admin',
        140765533426520064, 'A2021103101', '张三', 138652613116792832, '平凡的世界：全三册', '9787530221396', 2, 1,
        '2022-08-13 00:00:00', 5, '2022-08-18', '2022-08-23 14:54:27');
INSERT INTO `tb_book_borrowing`
VALUES (145908833737338880, NULL, 1, 0, '2022-08-23 15:09:37', 'admin', '2022-08-23 15:10:52', 'admin',
        140837825649152000, 'B2019151902', '李四', 138960840178110464, '心安即是归处', '9787554615430', 1, 1,
        '2022-08-23 15:09:34', 22, '2022-09-14', '2022-08-23 15:10:52');

-- ----------------------------
-- 建表: tb_book_classified
-- ----------------------------
DROP TABLE IF EXISTS `tb_book_classified`;
CREATE TABLE `tb_book_classified`
(
    `id`          bigint(20) NOT NULL COMMENT '主键ID',
    `tenant_id`   bigint(20) NULL DEFAULT NULL COMMENT '租户ID',
    `revision`    bigint(20) NOT NULL DEFAULT 1 COMMENT '乐观锁',
    `del_flag`    tinyint(4) UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除标识',
    `created_at`  datetime(0) NOT NULL COMMENT '创建时刻',
    `created_by`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
    `updated_at`  datetime(0) NOT NULL COMMENT '更新时刻',
    `updated_by`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
    `title`       varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
    `status`      tinyint(4) UNSIGNED NOT NULL DEFAULT 0 COMMENT '状态',
    `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
    `parent_id`   bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '父类别ID',
    `sort`        int(11) NOT NULL DEFAULT 1 COMMENT '排序',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '书籍类别' ROW_FORMAT = Dynamic;

-- ----------------------------
-- 数据: tb_book_classified
-- ----------------------------
INSERT INTO `tb_book_classified`
VALUES (137851647203315712, NULL, 1, 0, '2022-08-01 09:33:14', 'admin', '2022-08-01 09:35:15', 'admin', '文学', 1, NULL,
        0, 1);
INSERT INTO `tb_book_classified`
VALUES (137851704950493184, NULL, 1, 0, '2022-08-01 09:33:28', 'admin', '2022-08-01 09:33:28', 'admin', '小说', 1, NULL,
        137851647203315712, 1);
INSERT INTO `tb_book_classified`
VALUES (137949673372618752, NULL, 1, 0, '2022-08-01 16:02:45', 'admin', '2022-08-01 16:02:45', 'admin', '散文随笔', 1,
        NULL, 137851647203315712, 2);
INSERT INTO `tb_book_classified`
VALUES (137950171332972544, NULL, 1, 0, '2022-08-01 16:04:44', 'admin', '2022-08-01 16:04:44', 'admin', '人文社科', 1,
        NULL, 0, 2);
INSERT INTO `tb_book_classified`
VALUES (137950299498319872, NULL, 1, 0, '2022-08-01 16:05:14', 'admin', '2022-08-01 16:05:14', 'admin', '历史', 1, NULL,
        137950171332972544, 1);
INSERT INTO `tb_book_classified`
VALUES (137950336269783040, NULL, 1, 0, '2022-08-01 16:05:23', 'admin', '2022-08-01 16:05:23', 'admin', '心理学', 1,
        NULL, 137950171332972544, 2);
INSERT INTO `tb_book_classified`
VALUES (137950392779640832, NULL, 1, 0, '2022-08-01 16:05:37', 'admin', '2022-08-01 16:05:37', 'admin', '科学技术', 1,
        NULL, 0, 3);
INSERT INTO `tb_book_classified`
VALUES (137950490779553792, NULL, 1, 0, '2022-08-01 16:06:00', 'admin', '2022-08-01 16:06:00', 'admin',
        '计算机与互联网', 1, NULL, 137950392779640832, 1);
INSERT INTO `tb_book_classified`
VALUES (137950530642219008, NULL, 1, 0, '2022-08-01 16:06:10', 'admin', '2022-08-01 16:06:10', 'admin', '建筑', 1, NULL,
        137950392779640832, 2);

-- ----------------------------
-- 建表: tb_book_damage
-- ----------------------------
DROP TABLE IF EXISTS `tb_book_damage`;
CREATE TABLE `tb_book_damage`
(
    `id`         bigint(20) NOT NULL COMMENT '主键ID',
    `tenant_id`  bigint(20) NULL DEFAULT NULL COMMENT '租户ID',
    `revision`   bigint(20) NOT NULL DEFAULT 1 COMMENT '乐观锁',
    `del_flag`   tinyint(4) UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除标识',
    `created_at` datetime(0) NOT NULL COMMENT '创建时刻',
    `created_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
    `updated_at` datetime(0) NOT NULL COMMENT '更新时刻',
    `updated_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
    `book_id`    bigint(20) NOT NULL COMMENT '书籍ID',
    `book_title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '书籍名',
    `quantity`   int(11) NOT NULL COMMENT '报损数量',
    `remark`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '书籍损坏记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- 数据: tb_book_damage
-- ----------------------------
INSERT INTO `tb_book_damage`
VALUES (139316096451842048, NULL, 1, 1, '2022-08-05 10:32:26', 'admin', '2022-08-05 16:49:53', 'admin',
        138960840178110464, '心安即是归处', 1, NULL);
INSERT INTO `tb_book_damage`
VALUES (139411586279378944, NULL, 1, 1, '2022-08-05 16:51:52', 'admin', '2022-08-05 17:09:00', 'admin',
        138960840178110464, '心安即是归处', 3, NULL);
INSERT INTO `tb_book_damage`
VALUES (139413716276973568, NULL, 1, 1, '2022-08-05 17:00:20', 'admin', '2022-08-05 17:06:04', 'admin',
        138652613116792832, '平凡的世界：全三册', 1, NULL);
INSERT INTO `tb_book_damage`
VALUES (145909592868614144, NULL, 1, 0, '2022-08-23 15:12:38', 'admin', '2022-08-23 15:12:38', 'admin',
        138931161622745088, '三体（1）地球往事', 1, '被咖啡污染');

-- ----------------------------
-- 建表: tb_member
-- ----------------------------
DROP TABLE IF EXISTS `tb_member`;
CREATE TABLE `tb_member`
(
    `id`         bigint(20) NOT NULL COMMENT '主键ID',
    `tenant_id`  bigint(20) NULL DEFAULT NULL COMMENT '租户ID',
    `revision`   bigint(20) NOT NULL DEFAULT 1 COMMENT '乐观锁',
    `del_flag`   tinyint(4) UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除标识',
    `created_at` datetime(0) NOT NULL COMMENT '创建时刻',
    `created_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
    `updated_at` datetime(0) NOT NULL COMMENT '更新时刻',
    `updated_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
    `username`   varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '学号/工号',
    `real_name`  varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '真实姓名',
    `status`     tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态',
    `gender`     tinyint(4) NOT NULL DEFAULT 0 COMMENT '性别',
    `phone_no`   varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员' ROW_FORMAT = Dynamic;

-- ----------------------------
-- 数据: tb_member
-- ----------------------------
INSERT INTO `tb_member`
VALUES (140765533426520064, NULL, 1, 0, '2022-08-09 10:31:58', 'admin', '2022-08-09 15:13:44', 'admin', 'A2021103101',
        '张三', 1, 1, '17078789898');
INSERT INTO `tb_member`
VALUES (140837825649152000, NULL, 1, 0, '2022-08-09 15:19:14', 'admin', '2022-08-09 15:19:14', 'admin', 'B2019151902',
        '李四', 1, 1, '17078789899');

SET
FOREIGN_KEY_CHECKS = 1;

-- 对应的系统菜单
INSERT INTO `sys_menu` (`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                        `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                        `external_link`)
VALUES (136148735326523392, NULL, 1, 0, '2022-07-27 16:46:28', 'admin', '2022-08-09 17:34:45', 'admin', '书籍管理', 0,
        0, 'Book0', 'ant-design:book-twotone', 4, 1, NULL, '');
INSERT INTO `sys_menu` (`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                        `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                        `external_link`)
VALUES (145857402217000960, NULL, 1, 0, '2022-08-23 11:45:15', 'admin', '2022-08-23 11:45:15', 'admin', '确认归还',
        1556931243017101312, 2, 'BookBorrowing:return', 'ant-design:retweet-outlined', 5, 1, NULL, '');
INSERT INTO `sys_menu` (`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                        `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                        `external_link`)
VALUES (1551820366894981120, NULL, 1, 0, '2022-07-27 16:45:21', 'helio-generator', '2022-08-01 17:11:10', 'admin',
        '书籍类别管理', 136148735326523392, 1, 'BookClassified', 'ant-design:flag-outlined', 1, 1,
        '/library/BookClassified/index', '');
INSERT INTO `sys_menu` (`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                        `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                        `external_link`)
VALUES (1551820366894981121, NULL, 1, 0, '2022-07-27 16:45:21', 'helio-generator', '2022-07-27 16:45:21',
        'helio-generator', '查询', 1551820366894981120, 2, 'BookClassified:retrieve', NULL, 1, 1, NULL, '');
INSERT INTO `sys_menu` (`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                        `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                        `external_link`)
VALUES (1551820366894981122, NULL, 1, 0, '2022-07-27 16:45:21', 'helio-generator', '2022-07-27 16:45:21',
        'helio-generator', '新增', 1551820366894981120, 2, 'BookClassified:create', NULL, 2, 1, NULL, '');
INSERT INTO `sys_menu` (`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                        `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                        `external_link`)
VALUES (1551820366894981123, NULL, 1, 0, '2022-07-27 16:45:21', 'helio-generator', '2022-07-27 16:45:21',
        'helio-generator', '删除', 1551820366894981120, 2, 'BookClassified:delete', NULL, 3, 1, NULL, '');
INSERT INTO `sys_menu` (`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                        `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                        `external_link`)
VALUES (1551820366894981124, NULL, 1, 0, '2022-07-27 16:45:21', 'helio-generator', '2022-07-27 16:45:21',
        'helio-generator', '编辑', 1551820366894981120, 2, 'BookClassified:update', NULL, 4, 1, NULL, '');
INSERT INTO `sys_menu` (`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                        `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                        `external_link`)
VALUES (1554017489413595136, NULL, 1, 0, '2022-08-01 16:53:03', 'helio-generator', '2022-08-01 17:11:05', 'admin',
        '书籍管理', 136148735326523392, 1, 'Book', 'ant-design:flag-outlined', 2, 1, '/library/Book/index', '');
INSERT INTO `sys_menu` (`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                        `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                        `external_link`)
VALUES (1554017489413595137, NULL, 1, 0, '2022-08-01 16:53:03', 'helio-generator', '2022-08-01 16:53:03',
        'helio-generator', '查询', 1554017489413595136, 2, 'Book:retrieve', NULL, 1, 1, NULL, '');
INSERT INTO `sys_menu` (`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                        `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                        `external_link`)
VALUES (1554017489413595138, NULL, 1, 0, '2022-08-01 16:53:03', 'helio-generator', '2022-08-01 16:53:03',
        'helio-generator', '新增', 1554017489413595136, 2, 'Book:create', NULL, 2, 1, NULL, '');
INSERT INTO `sys_menu` (`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                        `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                        `external_link`)
VALUES (1554017489413595139, NULL, 1, 0, '2022-08-01 16:53:03', 'helio-generator', '2022-08-01 16:53:03',
        'helio-generator', '删除', 1554017489413595136, 2, 'Book:delete', NULL, 3, 1, NULL, '');
INSERT INTO `sys_menu` (`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                        `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                        `external_link`)
VALUES (1554017489413595140, NULL, 1, 0, '2022-08-01 16:53:03', 'helio-generator', '2022-08-01 16:53:03',
        'helio-generator', '编辑', 1554017489413595136, 2, 'Book:update', NULL, 4, 1, NULL, '');
INSERT INTO `sys_menu` (`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                        `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                        `external_link`)
VALUES (1555085532763906048, NULL, 1, 0, '2022-08-04 15:13:27', 'helio-generator', '2022-08-04 16:51:45', 'admin',
        '书籍损坏记录', 136148735326523392, 1, 'BookDamage', 'ant-design:flag-outlined', 3, 1,
        '/library/BookDamage/index', '');
INSERT INTO `sys_menu` (`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                        `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                        `external_link`)
VALUES (1555085532763906049, NULL, 1, 0, '2022-08-04 15:13:27', 'helio-generator', '2022-08-04 15:13:27',
        'helio-generator', '查询', 1555085532763906048, 2, 'BookDamage:retrieve', NULL, 1, 1, NULL, '');
INSERT INTO `sys_menu` (`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                        `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                        `external_link`)
VALUES (1555085532763906050, NULL, 1, 0, '2022-08-04 15:13:27', 'helio-generator', '2022-08-04 15:13:27',
        'helio-generator', '新增', 1555085532763906048, 2, 'BookDamage:create', NULL, 2, 1, NULL, '');
INSERT INTO `sys_menu` (`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                        `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                        `external_link`)
VALUES (1555085532763906051, NULL, 1, 0, '2022-08-04 15:13:27', 'helio-generator', '2022-08-04 15:13:27',
        'helio-generator', '删除', 1555085532763906048, 2, 'BookDamage:delete', NULL, 3, 1, NULL, '');
INSERT INTO `sys_menu` (`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                        `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                        `external_link`)
VALUES (1555085532763906052, NULL, 1, 0, '2022-08-04 15:13:27', 'helio-generator', '2022-08-05 16:48:44', 'admin',
        '编辑', 1555085532763906048, 2, 'BookDamage:update', NULL, 4, 0, NULL, '');
INSERT INTO `sys_menu` (`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                        `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                        `external_link`)
VALUES (1555792865521426432, NULL, 1, 0, '2022-08-06 13:55:41', 'helio-generator', '2022-08-09 17:34:55', 'admin',
        '会员管理', 0, 1, 'Member', 'ant-design:flag-outlined', 3, 1, '/library/Member/index', '');
INSERT INTO `sys_menu` (`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                        `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                        `external_link`)
VALUES (1555792865521426433, NULL, 1, 0, '2022-08-06 13:55:41', 'helio-generator', '2022-08-06 13:55:41',
        'helio-generator', '查询', 1555792865521426432, 2, 'Member:retrieve', NULL, 1, 1, NULL, '');
INSERT INTO `sys_menu` (`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                        `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                        `external_link`)
VALUES (1555792865521426434, NULL, 1, 0, '2022-08-06 13:55:41', 'helio-generator', '2022-08-06 13:55:41',
        'helio-generator', '新增', 1555792865521426432, 2, 'Member:create', NULL, 2, 1, NULL, '');
INSERT INTO `sys_menu` (`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                        `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                        `external_link`)
VALUES (1555792865521426435, NULL, 1, 0, '2022-08-06 13:55:41', 'helio-generator', '2022-08-06 13:55:41',
        'helio-generator', '删除', 1555792865521426432, 2, 'Member:delete', NULL, 3, 1, NULL, '');
INSERT INTO `sys_menu` (`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                        `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                        `external_link`)
VALUES (1555792865521426436, NULL, 1, 0, '2022-08-06 13:55:41', 'helio-generator', '2022-08-06 13:55:41',
        'helio-generator', '编辑', 1555792865521426432, 2, 'Member:update', NULL, 4, 1, NULL, '');
INSERT INTO `sys_menu` (`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                        `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                        `external_link`)
VALUES (1556931243017101312, NULL, 1, 0, '2022-08-09 17:17:04', 'helio-generator', '2022-08-09 17:34:28', 'admin',
        '书籍借阅记录', 136148735326523392, 1, 'BookBorrowing', 'ant-design:flag-outlined', 4, 1,
        '/library/BookBorrowing/index', '');
INSERT INTO `sys_menu` (`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                        `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                        `external_link`)
VALUES (1556931243017101313, NULL, 1, 0, '2022-08-09 17:17:04', 'helio-generator', '2022-08-09 17:17:04',
        'helio-generator', '查询', 1556931243017101312, 2, 'BookBorrowing:retrieve', NULL, 1, 1, NULL, '');
INSERT INTO `sys_menu` (`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                        `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                        `external_link`)
VALUES (1556931243017101314, NULL, 1, 0, '2022-08-09 17:17:04', 'helio-generator', '2022-08-09 17:34:08', 'admin',
        '新增', 1556931243017101312, 2, 'BookBorrowing:create', NULL, 2, 1, NULL, '');
INSERT INTO `sys_menu` (`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                        `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                        `external_link`)
VALUES (1556931243017101315, NULL, 1, 0, '2022-08-09 17:17:04', 'helio-generator', '2022-08-09 17:17:04',
        'helio-generator', '删除', 1556931243017101312, 2, 'BookBorrowing:delete', NULL, 3, 1, NULL, '');
INSERT INTO `sys_menu` (`id`, `tenant_id`, `revision`, `del_flag`, `created_at`, `created_by`, `updated_at`,
                        `updated_by`, `title`, `parent_id`, `type`, `permission`, `icon`, `sort`, `status`, `component`,
                        `external_link`)
VALUES (1556931243017101316, NULL, 1, 0, '2022-08-09 17:17:04', 'helio-generator', '2022-08-09 17:34:14', 'admin',
        '编辑', 1556931243017101312, 2, 'BookBorrowing:update', NULL, 4, 0, NULL, '');
