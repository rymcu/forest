create database forest default character set utf8mb4 collate utf8mb4_unicode_ci;

use forest;

create table forest_article
(
    id                      bigint auto_increment comment '主键'
        primary key,
    article_title           varchar(128)     null comment '文章标题',
    article_thumbnail_url   varchar(128)     null comment '文章缩略图',
    article_author_id       bigint           null comment '文章作者id',
    article_type            char default '0' null comment '文章类型',
    article_tags            varchar(128)     null comment '文章标签',
    article_view_count      int  default 1   null comment '浏览总数',
    article_preview_content varchar(256)     null comment '预览内容',
    article_comment_count   int  default 0   null comment '评论总数',
    article_permalink       varchar(128)     null comment '文章永久链接',
    article_link            varchar(32)      null comment '站内链接',
    created_time            datetime         null comment '创建时间',
    updated_time            datetime         null comment '更新时间',
    article_perfect         char default '0' null comment '0:非优选1：优选',
    article_status          char default '0' null comment '文章状态',
    article_thumbs_up_count int  default 0   null comment '点赞总数',
    article_sponsor_count   int  default 0   null comment '赞赏总数'
)
    comment ' ' collate = utf8mb4_unicode_ci;

create table forest_article_content
(
    id_article           bigint   not null comment '主键',
    article_content      text     null comment '文章内容原文',
    article_content_html text     null comment '文章内容Html',
    created_time         datetime null comment '创建时间',
    updated_time         datetime null comment '更新时间'
)
    comment ' ' collate = utf8mb4_unicode_ci;

create index forest_article_content_id_article_index
    on forest_article_content (id_article);

create table forest_article_thumbs_up
(
    id             bigint auto_increment comment '主键'
        primary key,
    id_article     bigint   null comment '文章表主键',
    id_user        bigint   null comment '用户表主键',
    thumbs_up_time datetime null comment '点赞时间'
)
    comment '文章点赞表 ';

create table forest_bank
(
    id               bigint auto_increment comment '主键'
        primary key,
    bank_name        varchar(64)  null comment '银行名称',
    bank_owner       bigint       null comment '银行负责人',
    bank_description varchar(512) null comment '银行描述',
    created_by       bigint       null comment '创建人',
    created_time     datetime     null comment '创建时间'
)
    comment '银行表 ';

create table forest_bank_account
(
    id              bigint auto_increment comment '主键'
        primary key,
    id_bank         bigint           null comment '所属银行',
    bank_account    varchar(32)      null comment '银行账户',
    account_balance decimal(32, 8)   null comment '账户余额',
    account_owner   bigint           null comment '账户所有者',
    created_time    datetime         null comment '创建时间',
    account_type    char default '0' null comment '0: 普通账户 1: 银行账户'
)
    comment '银行账户表 ';

create table forest_comment
(
    id                          bigint auto_increment comment '主键'
        primary key,
    comment_content             text             null comment '评论内容',
    comment_author_id           bigint           null comment '作者 id',
    comment_article_id          bigint           null comment '文章 id',
    comment_sharp_url           varchar(256)     null comment '锚点 url',
    comment_original_comment_id bigint           null comment '父评论 id',
    comment_status              char default '0' null comment '状态',
    comment_ip                  varchar(128)     null comment '评论 IP',
    comment_ua                  varchar(128)     null comment 'User-Agent',
    comment_anonymous           char             null comment '0：公开回帖，1：匿名回帖',
    comment_reply_count         int              null comment '回帖计数',
    comment_visible             char             null comment '0：所有人可见，1：仅楼主和自己可见',
    created_time                datetime         null comment '创建时间'
)
    comment '评论表 ' collate = utf8mb4_unicode_ci;

create table forest_currency_issue
(
    id           bigint auto_increment comment '主键'
        primary key,
    issue_value  decimal(32, 8) null comment '发行数额',
    created_by   bigint         null comment '发行人',
    created_time datetime       null comment '发行时间'
)
    comment '货币发行表 ';

create table forest_currency_rule
(
    id               bigint auto_increment comment '主键'
        primary key,
    rule_name        varchar(128)     null comment '规则名称',
    rule_sign        varchar(64)      null comment '规则标志(与枚举变量对应)',
    rule_description varchar(1024)    null comment '规则描述',
    money            decimal(32, 8)   null comment '金额',
    award_status     char default '0' null comment '奖励(0)/消耗(1)状态',
    maximum_money    decimal(32, 8)   null comment '上限金额',
    repeat_days      int  default 0   null comment '重复(0: 不重复,单位:天)',
    status           char default '0' null comment '状态'
)
    comment '货币规则表 ';

create table forest_follow
(
    id             bigint auto_increment comment '主键'
        primary key,
    follower_id    bigint null comment '关注者 id',
    following_id   bigint null comment '关注数据 id',
    following_type char   null comment '0：用户，1：标签，2：帖子收藏，3：帖子关注'
)
    comment '关注表 ' collate = utf8mb4_unicode_ci;

create table forest_notification
(
    id           bigint auto_increment comment '主键'
        primary key,
    id_user      bigint           null comment '用户id',
    data_type    char             null comment '数据类型',
    data_id      bigint           null comment '数据id',
    has_read     char default '0' null comment '是否已读',
    data_summary varchar(256)     null comment '数据摘要',
    created_time datetime         null comment '创建时间'
)
    comment '通知表 ' collate = utf8mb4_unicode_ci;

create table forest_portfolio
(
    id                         bigint auto_increment comment '主键'
        primary key,
    portfolio_head_img_url     varchar(500)  null comment '作品集头像',
    portfolio_title            varchar(32)   null comment '作品集名称',
    portfolio_author_id        bigint        null comment '作品集作者',
    portfolio_description      varchar(1024) null comment '作品集介绍',
    created_time               datetime      null comment '创建时间',
    updated_time               datetime      null comment '更新时间',
    portfolio_description_html varchar(1024) null comment ' 作品集介绍HTML'
)
    comment '作品集表' collate = utf8mb4_unicode_ci;

create table forest_portfolio_article
(
    id           bigint auto_increment comment '主键'
        primary key,
    id_portfolio bigint null comment '作品集表主键',
    id_article   bigint null comment '文章表主键',
    sort_no      int    null comment '排序号'
)
    comment '作品集与文章关系表' collate = utf8mb4_unicode_ci;

create table forest_role
(
    id           bigint auto_increment comment '主键'
        primary key,
    name         varchar(32)         null comment '名称',
    input_code   varchar(32)         null comment '拼音码',
    status       char    default '0' null comment '状态',
    created_time datetime            null comment '创建时间',
    updated_time datetime            null comment '更新时间',
    weights      tinyint default 0   null comment '权重,数值越小权限越大;0:无权限'
)
    comment ' ' collate = utf8mb4_unicode_ci;

create table forest_sponsor
(
    id                bigint auto_increment comment '主键'
        primary key,
    data_type         char           null comment '数据类型',
    data_id           bigint         null comment '数据主键',
    sponsor           bigint         null comment '赞赏人',
    sponsorship_time  datetime       null comment '赞赏日期',
    sponsorship_money decimal(32, 8) null comment '赞赏金额'
)
    comment '赞赏表 ';

create table forest_tag
(
    id                   bigint auto_increment comment '主键'
        primary key,
    tag_title            varchar(32)      null comment '标签名',
    tag_icon_path        varchar(512)     null comment '标签图标',
    tag_uri              varchar(128)     null comment '标签uri',
    tag_description      text             null comment '描述',
    tag_view_count       int  default 0   null comment '浏览量',
    tag_article_count    int  default 0   null comment '关联文章总数',
    tag_ad               char             null comment '标签广告',
    tag_show_side_ad     char             null comment '是否显示全站侧边栏广告',
    created_time         datetime         null comment '创建时间',
    updated_time         datetime         null comment '更新时间',
    tag_status           char default '0' null comment '标签状态',
    tag_reservation      char default '0' null comment '保留标签',
    tag_description_html text             null
)
    comment '标签表 ' collate = utf8mb4_unicode_ci;

create table forest_tag_article
(
    id                    bigint auto_increment comment '主键'
        primary key,
    id_tag                bigint        null comment '标签 id',
    id_article            varchar(32)   null comment '帖子 id',
    article_comment_count int default 0 null comment '帖子评论计数 0',
    article_perfect       int default 0 null comment '0:非优选1：优选 0',
    created_time          datetime      null comment '创建时间',
    updated_time          datetime      null comment '更新时间'
)
    comment '标签 - 帖子关联表 ' collate = utf8mb4_unicode_ci;

create index forest_tag_article_id_tag_index
    on forest_tag_article (id_tag);

create table forest_topic
(
    id                     bigint auto_increment comment '主键'
        primary key,
    topic_title            varchar(32)      null comment '专题标题',
    topic_uri              varchar(32)      null comment '专题路径',
    topic_description      text             null comment '专题描述',
    topic_type             varchar(32)      null comment '专题类型',
    topic_sort             int  default 10  null comment '专题序号 10',
    topic_icon_path        varchar(128)     null comment '专题图片路径',
    topic_nva              char default '0' null comment '0：作为导航1：不作为导航 0',
    topic_tag_count        int  default 0   null comment '专题下标签总数 0',
    topic_status           char default '0' null comment '0：正常1：禁用 0',
    created_time           datetime         null comment '创建时间',
    updated_time           datetime         null comment '更新时间',
    topic_description_html text             null comment '专题描述 Html'
)
    comment '主题表' collate = utf8mb4_unicode_ci;

create table forest_topic_tag
(
    id           bigint auto_increment comment '主键'
        primary key,
    id_topic     bigint   null comment '专题id',
    id_tag       bigint   null comment '标签id',
    created_time datetime null comment '创建时间',
    updated_time datetime null comment '更新时间'
)
    comment '专题- 标签关联表 ' collate = utf8mb4_unicode_ci;

create index forest_topic_tag_id_topic_index
    on forest_topic_tag (id_topic);

create table forest_transaction_record
(
    id                bigint auto_increment comment '交易主键'
        primary key,
    transaction_no    varchar(32)      null comment '交易流水号',
    funds             varchar(32)      null comment '款项',
    form_bank_account varchar(32)      null comment '交易发起方',
    to_bank_account   varchar(32)      null comment '交易收款方',
    money             decimal(32, 8)   null comment '交易金额',
    transaction_type  char default '0' null comment '交易类型',
    transaction_time  datetime         null comment '交易时间'
)
    comment '交易记录表 ';

create table forest_user
(
    id              bigint auto_increment comment '用户ID'
        primary key,
    account         varchar(32)      null comment '账号',
    password        varchar(64)      not null comment '密码',
    nickname        varchar(128)     null comment '昵称',
    real_name       varchar(32)      null comment '真实姓名',
    sex             char default '0' null comment '性别',
    avatar_type     char default '0' null comment '头像类型',
    avatar_url      varchar(512)     null comment '头像路径',
    email           varchar(64)      null comment '邮箱',
    phone           varchar(11)      null comment '电话',
    status          char default '0' null comment '状态',
    created_time    datetime         null comment '创建时间',
    updated_time    datetime         null comment '更新时间',
    last_login_time datetime         null comment '最后登录时间',
    signature       varchar(128)     null comment '签名'
)
    comment ' ' collate = utf8mb4_unicode_ci;

create table forest_user_extend
(
    id_user bigint       not null comment '用户表主键',
    github  varchar(64)  null comment 'github',
    weibo   varchar(32)  null comment '微博',
    weixin  varchar(32)  null comment '微信',
    qq      varchar(32)  null comment 'qq',
    blog    varchar(500) null comment '博客'
)
    comment '用户扩展表 ';

create table forest_user_role
(
    id_user      bigint   not null comment '用户表主键',
    id_role      bigint   not null comment '角色表主键',
    created_time datetime null comment '创建时间'
)
    comment ' ' collate = utf8mb4_unicode_ci;

create table forest_user_tag
(
    id           bigint auto_increment comment '主键'
        primary key,
    id_user      bigint      null comment '用户 id',
    id_tag       varchar(32) null comment '标签 id',
    type         char        null comment '0：创建者，1：帖子使用，2：用户自评标签',
    created_time datetime    null comment '创建时间',
    updated_time datetime    null comment '更新时间'
)
    comment '用户 - 标签关联表 ' collate = utf8mb4_unicode_ci;

create table forest_visit
(
    id                bigint auto_increment comment '主键'
        primary key,
    visit_url         varchar(256) null comment '浏览链接',
    visit_ip          varchar(128) null comment 'IP',
    visit_ua          varchar(512) null comment 'User-Agent',
    visit_city        varchar(32)  null comment '城市',
    visit_device_id   varchar(256) null comment '设备唯一标识',
    visit_user_id     bigint       null comment '浏览者 id',
    visit_referer_url varchar(256) null comment '上游链接',
    created_time      datetime     null comment '创建时间',
    expired_time      datetime     null comment '过期时间'
)
    comment '浏览表' collate = utf8mb4_unicode_ci;

create table forest_lucene_user_dic
(
    id  int auto_increment comment '字典编号',
    dic char(32) null comment '字典',
    constraint forest_lucene_user_dic_id_uindex
        unique (id)
)
    comment '用户扩展字典';

alter table forest_lucene_user_dic
    add primary key (id);

insert into forest.forest_role (id, name, input_code, status, created_time, updated_time, weights) values (1, '管理员', 'admin', '0', '2019-11-16 04:22:45', '2019-11-16 04:22:45', 1);
insert into forest.forest_role (id, name, input_code, status, created_time, updated_time, weights) values (2, '社区管理员', 'blog_admin', '0', '2019-12-05 03:10:05', '2019-12-05 17:11:35', 2);
insert into forest.forest_role (id, name, input_code, status, created_time, updated_time, weights) values (3, '作者', 'zz', '0', '2020-03-12 15:07:27', '2020-03-12 15:07:27', 3);
insert into forest.forest_role (id, name, input_code, status, created_time, updated_time, weights) values (4, '普通用户', 'user', '0', '2019-12-05 03:10:59', '2020-03-12 15:13:49', 4);

insert into forest.forest_user (id, account, password, nickname, real_name, sex, avatar_type, avatar_url, email, phone, status, created_time, updated_time, last_login_time, signature) values (1, 'admin', '8ce2dd866238958ac4f07870766813cdaa39a9b83a8c75e26aa50f23', 'admin', 'admin', '0', '0', null, null, null, '0', '2021-01-25 18:21:51', '2021-01-25 18:21:54', null, null);

insert into forest.forest_user_role (id_user, id_role, created_time) values (1, 1, '2021-01-25 18:22:12');
