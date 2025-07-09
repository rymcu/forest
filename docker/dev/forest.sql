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
) comment '文章表 ' collate = utf8mb4_unicode_ci;

INSERT INTO forest.forest_article (id, article_title, article_thumbnail_url, article_author_id, article_type,
                                   article_tags, article_view_count, article_preview_content, article_comment_count,
                                   article_permalink, article_link, created_time, updated_time, article_perfect,
                                   article_status, article_thumbs_up_count, article_sponsor_count)
VALUES (1, '给新人的一封信', null, 1, '0', '公告,新手信', 3275,
        '您好，欢迎来到 RYMCU 社区，RYMCU 是一个嵌入式知识学习交流平台。RY 取自”容易”的首字母，寓意为让电子设计变得 so easy。新手的疑问初学者都有很多疑问，在这里对这些疑问进行一一解答。我英语不好，可以学习编程吗？对于初学者来说，英语不是主要的障碍，国内有着充足的中文教程。但在接下来的学习过程中，需要阅读大量的英文文档，所以还是需要有一些英语基础和理解学习能力，配合翻译工具（如百度',
        0, 'http://localhost:3000/article/1', '/article/1', '2020-01-03 01:27:25', '2022-09-26 15:33:03', '0', '0', 7,
        3);

create table forest_article_content
(
    id_article           bigint   not null comment '主键',
    article_content      text     null comment '文章内容原文',
    article_content_html text     null comment '文章内容Html',
    created_time         datetime null comment '创建时间',
    updated_time         datetime null comment '更新时间'
) comment ' ' collate = utf8mb4_unicode_ci;

create index forest_article_content_id_article_index
    on forest_article_content (id_article);

INSERT INTO forest.forest_article_content (id_article, article_content, article_content_html, created_time,
                                           updated_time)
VALUES (1, '您好，欢迎来到 RYMCU 社区，RYMCU 是一个嵌入式知识学习交流平台。RY 取自”容易”的首字母，寓意为让电子设计变得 so easy。

## 新手的疑问

初学者都有很多疑问，在这里对这些疑问进行一一解答。

- 我英语不好，可以学习编程吗？
  对于初学者来说，英语不是主要的障碍，国内有着充足的中文教程。但在接下来的学习过程中，需要阅读大量的英文文档，所以还是需要有一些英语基础和理解学习能力，配合翻译工具（如百度翻译）进行理解。
- 我数学不好，可以学习编程吗？
  对于初学者来说，有必要掌握数学逻辑思维和解决问题的思路，这些能力都在数学学习中得到锻炼，想必学习编程的人数学成绩肯定不错。初学者不需要多高的数学知识水平，但在未来的学习过程中需要更高级的数学知识，应随时做好接受学习新知识的准备。
- 我想学习编程，大佬可以教教我吗？
  一般我是拒绝的，我认为学习是互相促进的过程，而不是单方面的输出，并且我也有很多事情要做。不仅是我，绝大多数人都会拒绝。
- 学习编程是使用 IDE 好还是 Notepad 好？
  最近看到有人在争论这个问题，使用 IDE 是新手的不二选择。
- 好吧，我自学编程，有问题可以问大佬吗？
  可以，但是我拒绝回答书中的基础问题和可以通过搜索引擎解决的问题。
- 学习编程是看书好还是看视频好？
  萝卜青菜，各有所爱，关键是看哪种方式能让你更好理解和学习。我个人是喜爱书本，可以随时查阅资料，非常方便。
- 我学习了很久，但没有成效，我是不是没有天赋？
  我个人觉得对于入门的学习来说，天赋对于学习的影响微乎其微，如果你的学习效率低下，考虑是不是以下原因：

  - 单纯的努力不足，三天打鱼两天晒网。如果不能改正，不如考虑干点别的。
  - 数学逻辑思维和解决问题的能力不足。这个可以学习一些简单易懂的教程，看看视频等，慢慢锻炼，没有任何捷径。
  - 学习方法不对，主要是练得少。只翻书和看视频是没有用的，必须配合大量的练习。个人推荐的方法是：
    - 看完书以后把书上给出的例题再敲一遍，不是照着书上写。
    - 把课后习题都给做了。
    - 做几个自己感兴趣的项目。
    - 对于自己不懂的问题，先看看书，再百度谷歌，最后才询问他人。

## 提问的方法

当你遇到**使用搜索引擎、查阅相关文档、进行 Debug**（如果没有做过上述操作的话，请立刻去做）也无法解决的问题的时候，你可能会向别人求助。现在就来讲讲如何正确提问。

当你进行提问时，请保证你准确提供了以下信息：

- 准确的描述你的需求和实际问题情况。
- 准确的描述你所在的平台的信息。例如：
  - 开发板型号
  - 运行程序( IDE 等)名称及其版本
  - Windows/Linux/MacOS 任一平台及其版本
  - 依赖的类库及其版本
- 提供你的源代码，将源代码包括开发环境完整上传至源码托管平台（如 Github）。
- 提供你的完整日志、异常输出。

如果你在社区提问，请在你的标题也简略的包含问题描述和平台信息。例如 `stm32f103x 开发板` `win10` 运行串口通信程序时，中文显示乱码

如果你想学习更多关于提问的方法、技巧、礼仪，看看[提问的智慧](https://rymcu.com/article/80)会给予你许多帮助。

## 自学的方法

- 每当学习到新知识的时候应该及时的练习和实践
- 多看看开发文档，每次你都能获得新的收获
- 多看看别人的源代码，很多问题都能得到解决
- 搜索引擎是一个好东西
- 写学习笔记和博客是记录知识的好方式，但不是死记知识点
- 好的提问方式才能获得正确答案
- 合理的规划学习时间，而不是三天打鱼两天晒网

## C 语言基础教程

- [C 语言中文教程](https://doc.yonyoucloud.com/doc/wiki/project/c/c-intro.html)
- [C语言小白变怪兽](http://c.biancheng.net/c/)

## 单片机基础教程

- [51 单片机入门教程(Keil4 版)](https://rymcu.com/portfolio/42)
- [STM32 独家入门秘籍](https://rymcu.com/portfolio/11)
- [51 单片机入门教程(VS Code 版)](https://rymcu.com/portfolio/41)

## 其他教程

- [markdown 教程](https://rymcu.com/guides/markdown)
- [社区编辑器使用教程](https://rymcu.com/guides/vditor)

## 推荐书籍

- 《C 程序设计语言( 第 2 版 ) 》 —— [美] 布莱恩·W.克尼汉（Brian W.Kernighan），[美] 丹尼斯·M.里奇（Dennis M.Ritchie） 著
- 《软技能: 代码之外的生存指南》—— [美] 约翰 Z.森梅兹（John Z.Sonmez） 著
- 《大教堂与集市》—— [美] Eric S Raymond 著
- 《黑客与画家》—— [美] Paul Graham 著

## 愿景

> 关于更多的信息请阅读 [《RYMCU 白皮书》](https://rymcu.com/article/115)

我们致力于构建一个即严谨又活泼、专业又不失有趣的开源嵌入式知识平台。在这里我们可以畅所欲言、以平等、自由的身份获取和分享知识。在这里共同学习、交流、进步、成长。

## 行为准则

> 详细行为准则请参考 [参与者公约](https://rymcu.com/article/20)

无论问题简单与否，欢迎大家积极留言、评论、交流。对他人多一些理解和包容，帮助他人解决问题和自我提升是我们的终极目标。
欢迎您发表原创文章、分享独到见解、作出有价值的评论。所有原创内容著作权均归作者本人所有。所发表内容不得侵犯企业或个人的合法权益，包括但不限于涉及个人隐私、造谣与诽谤、商业侵权。

## 其他

### 微信公众号

![qrcodeforgh245b3234e782258.jpg](https://static.rymcu.com/article/1642081054095.jpg)

### github

[RYMCU](https://github.com/rymcu)

### gitee

[RYMCU 社区](https://gitee.com/rymcu-community)

', '<p>您好，欢迎来到 RYMCU 社区，RYMCU 是一个嵌入式知识学习交流平台。RY 取自”容易”的首字母，寓意为让电子设计变得 so easy。</p>
<h2 id="新手的疑问">新手的疑问</h2>
<p>初学者都有很多疑问，在这里对这些疑问进行一一解答。</p>
<ul>
<li>
<p>我英语不好，可以学习编程吗？<br />
对于初学者来说，英语不是主要的障碍，国内有着充足的中文教程。但在接下来的学习过程中，需要阅读大量的英文文档，所以还是需要有一些英语基础和理解学习能力，配合翻译工具（如百度翻译）进行理解。</p>
</li>
<li>
<p>我数学不好，可以学习编程吗？<br />
对于初学者来说，有必要掌握数学逻辑思维和解决问题的思路，这些能力都在数学学习中得到锻炼，想必学习编程的人数学成绩肯定不错。初学者不需要多高的数学知识水平，但在未来的学习过程中需要更高级的数学知识，应随时做好接受学习新知识的准备。</p>
</li>
<li>
<p>我想学习编程，大佬可以教教我吗？<br />
一般我是拒绝的，我认为学习是互相促进的过程，而不是单方面的输出，并且我也有很多事情要做。不仅是我，绝大多数人都会拒绝。</p>
</li>
<li>
<p>学习编程是使用 IDE 好还是 Notepad 好？<br />
最近看到有人在争论这个问题，使用 IDE 是新手的不二选择。</p>
</li>
<li>
<p>好吧，我自学编程，有问题可以问大佬吗？<br />
可以，但是我拒绝回答书中的基础问题和可以通过搜索引擎解决的问题。</p>
</li>
<li>
<p>学习编程是看书好还是看视频好？<br />
萝卜青菜，各有所爱，关键是看哪种方式能让你更好理解和学习。我个人是喜爱书本，可以随时查阅资料，非常方便。</p>
</li>
<li>
<p>我学习了很久，但没有成效，我是不是没有天赋？<br />
我个人觉得对于入门的学习来说，天赋对于学习的影响微乎其微，如果你的学习效率低下，考虑是不是以下原因：</p>
<ul>
<li>单纯的努力不足，三天打鱼两天晒网。如果不能改正，不如考虑干点别的。</li>
<li>数学逻辑思维和解决问题的能力不足。这个可以学习一些简单易懂的教程，看看视频等，慢慢锻炼，没有任何捷径。</li>
<li>学习方法不对，主要是练得少。只翻书和看视频是没有用的，必须配合大量的练习。个人推荐的方法是：
<ul>
<li>看完书以后把书上给出的例题再敲一遍，不是照着书上写。</li>
<li>把课后习题都给做了。</li>
<li>做几个自己感兴趣的项目。</li>
<li>对于自己不懂的问题，先看看书，再百度谷歌，最后才询问他人。</li>
</ul>
</li>
</ul>
</li>
</ul>
<h2 id="提问的方法">提问的方法</h2>
<p>当你遇到<strong>使用搜索引擎、查阅相关文档、进行 Debug</strong>（如果没有做过上述操作的话，请立刻去做）也无法解决的问题的时候，你可能会向别人求助。现在就来讲讲如何正确提问。</p>
<p>当你进行提问时，请保证你准确提供了以下信息：</p>
<ul>
<li>准确的描述你的需求和实际问题情况。</li>
<li>准确的描述你所在的平台的信息。例如：
<ul>
<li>开发板型号</li>
<li>运行程序( IDE 等)名称及其版本</li>
<li>Windows/Linux/MacOS 任一平台及其版本</li>
<li>依赖的类库及其版本</li>
</ul>
</li>
<li>提供你的源代码，将源代码包括开发环境完整上传至源码托管平台（如 Github）。</li>
<li>提供你的完整日志、异常输出。</li>
</ul>
<p>如果你在社区提问，请在你的标题也简略的包含问题描述和平台信息。例如 <code>stm32f103x 开发板</code> <code>win10</code> 运行串口通信程序时，中文显示乱码</p>
<p>如果你想学习更多关于提问的方法、技巧、礼仪，看看<a href="https://rymcu.com/article/80">提问的智慧</a>会给予你许多帮助。</p>
<h2 id="自学的方法">自学的方法</h2>
<ul>
<li>每当学习到新知识的时候应该及时的练习和实践</li>
<li>多看看开发文档，每次你都能获得新的收获</li>
<li>多看看别人的源代码，很多问题都能得到解决</li>
<li>搜索引擎是一个好东西</li>
<li>写学习笔记和博客是记录知识的好方式，但不是死记知识点</li>
<li>好的提问方式才能获得正确答案</li>
<li>合理的规划学习时间，而不是三天打鱼两天晒网</li>
</ul>
<h2 id="C-语言基础教程">C 语言基础教程</h2>
<ul>
<li><a href="https://doc.yonyoucloud.com/doc/wiki/project/c/c-intro.html">C 语言中文教程</a></li>
<li><a href="http://c.biancheng.net/c/">C 语言小白变怪兽</a></li>
</ul>
<h2 id="单片机基础教程">单片机基础教程</h2>
<ul>
<li><a href="https://rymcu.com/portfolio/42">51 单片机入门教程(Keil4 版)</a></li>
<li><a href="https://rymcu.com/portfolio/11">STM32 独家入门秘籍</a></li>
<li><a href="https://rymcu.com/portfolio/41">51 单片机入门教程(VS Code 版)</a></li>
</ul>
<h2 id="其他教程">其他教程</h2>
<ul>
<li><a href="https://rymcu.com/guides/markdown">markdown 教程</a></li>
<li><a href="https://rymcu.com/guides/vditor">社区编辑器使用教程</a></li>
</ul>
<h2 id="推荐书籍">推荐书籍</h2>
<ul>
<li>《C 程序设计语言( 第 2 版 ) 》 —— [美] 布莱恩·W.克尼汉（Brian W.Kernighan），[美] 丹尼斯·M.里奇（Dennis M.Ritchie） 著</li>
<li>《软技能: 代码之外的生存指南》—— [美] 约翰 Z.森梅兹（John Z.Sonmez） 著</li>
<li>《大教堂与集市》—— [美] Eric S Raymond 著</li>
<li>《黑客与画家》—— [美] Paul Graham 著</li>
</ul>
<h2 id="愿景">愿景</h2>
<blockquote>
<p>关于更多的信息请阅读 <a href="https://rymcu.com/article/115">《RYMCU 白皮书》</a></p>
</blockquote>
<p>我们致力于构建一个即严谨又活泼、专业又不失有趣的开源嵌入式知识平台。在这里我们可以畅所欲言、以平等、自由的身份获取和分享知识。在这里共同学习、交流、进步、成长。</p>
<h2 id="行为准则">行为准则</h2>
<blockquote>
<p>详细行为准则请参考 <a href="https://rymcu.com/article/20">参与者公约</a></p>
</blockquote>
<p>无论问题简单与否，欢迎大家积极留言、评论、交流。对他人多一些理解和包容，帮助他人解决问题和自我提升是我们的终极目标。<br />
欢迎您发表原创文章、分享独到见解、作出有价值的评论。所有原创内容著作权均归作者本人所有。所发表内容不得侵犯企业或个人的合法权益，包括但不限于涉及个人隐私、造谣与诽谤、商业侵权。</p>
<h2 id="其他">其他</h2>
<h3 id="微信公众号">微信公众号</h3>
<p><img src="https://static.rymcu.com/article/1642081054095.jpg" alt="qrcodeforgh245b3234e782258.jpg" /></p>
<h3 id="github">github</h3>
<p><a href="https://github.com/rymcu">RYMCU</a></p>
<h3 id="gitee">gitee</h3>
<p><a href="https://gitee.com/rymcu-community">RYMCU 社区</a></p>
', '2020-01-03 15:27:25', '2022-09-26 15:33:02');


create table forest_article_thumbs_up
(
    id             bigint auto_increment comment '主键'
        primary key,
    id_article     bigint   null comment '文章表主键',
    id_user        bigint   null comment '用户表主键',
    thumbs_up_time datetime null comment '点赞时间'
) comment '文章点赞表 ' collate = utf8mb4_unicode_ci;

create table forest_bank
(
    id               bigint auto_increment comment '主键'
        primary key,
    bank_name        varchar(64)  null comment '银行名称',
    bank_owner       bigint       null comment '银行负责人',
    bank_description varchar(512) null comment '银行描述',
    created_by       bigint       null comment '创建人',
    created_time     datetime     null comment '创建时间'
) comment '银行表 ' collate = utf8mb4_unicode_ci;

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
) comment '银行账户表 ' collate = utf8mb4_unicode_ci;

create unique index forest_bank_account_pk
    on forest_bank_account (account_owner, bank_account);

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
    comment_ua                  varchar(512)     null comment 'User-Agent',
    comment_anonymous           char             null comment '0：公开回帖，1：匿名回帖',
    comment_reply_count         int              null comment '回帖计数',
    comment_visible             char             null comment '0：所有人可见，1：仅楼主和自己可见',
    created_time                datetime         null comment '创建时间'
) comment '评论表 ' collate = utf8mb4_unicode_ci;

create table forest_currency_issue
(
    id           bigint auto_increment comment '主键'
        primary key,
    issue_value  decimal(32, 8) null comment '发行数额',
    created_by   bigint         null comment '发行人',
    created_time datetime       null comment '发行时间'
) comment '货币发行表 ' collate = utf8mb4_unicode_ci;

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
) comment '货币规则表 ' collate = utf8mb4_unicode_ci;

create table forest_follow
(
    id             bigint auto_increment comment '主键'
        primary key,
    follower_id    bigint null comment '关注者 id',
    following_id   bigint null comment '关注数据 id',
    following_type char   null comment '0：用户，1：标签，2：帖子收藏，3：帖子关注'
) comment '关注表 ' collate = utf8mb4_unicode_ci;

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
) comment '通知表 ' collate = utf8mb4_unicode_ci;

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
) comment '作品集表' collate = utf8mb4_unicode_ci;

create table forest_portfolio_article
(
    id           bigint auto_increment comment '主键'
        primary key,
    id_portfolio bigint null comment '作品集表主键',
    id_article   bigint null comment '文章表主键',
    sort_no      int    null comment '排序号'
) comment '作品集与文章关系表' collate = utf8mb4_unicode_ci;

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
) comment ' ' collate = utf8mb4_unicode_ci;

create table forest_sponsor
(
    id                bigint auto_increment comment '主键'
        primary key,
    data_type         char           null comment '数据类型',
    data_id           bigint         null comment '数据主键',
    sponsor           bigint         null comment '赞赏人',
    sponsorship_time  datetime       null comment '赞赏日期',
    sponsorship_money decimal(32, 8) null comment '赞赏金额'
) comment '赞赏表 ' collate = utf8mb4_unicode_ci;

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
) comment '标签表 ' collate = utf8mb4_unicode_ci;

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
) comment '标签 - 帖子关联表 ' collate = utf8mb4_unicode_ci;

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
) comment '主题表' collate = utf8mb4_unicode_ci;

create table forest_topic_tag
(
    id           bigint auto_increment comment '主键'
        primary key,
    id_topic     bigint   null comment '专题id',
    id_tag       bigint   null comment '标签id',
    created_time datetime null comment '创建时间',
    updated_time datetime null comment '更新时间'
) comment '专题- 标签关联表 ' collate = utf8mb4_unicode_ci;

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
) comment '交易记录表 ' collate = utf8mb4_unicode_ci;

create table forest_user
(
    id               bigint auto_increment comment '用户ID'
        primary key,
    account          varchar(32)      null comment '账号',
    password         varchar(64)      not null comment '密码',
    nickname         varchar(128)     null comment '昵称',
    real_name        varchar(32)      null comment '真实姓名',
    sex              char default '0' null comment '性别',
    avatar_type      char default '0' null comment '头像类型',
    avatar_url       varchar(512)     null comment '头像路径',
    email            varchar(64)      null comment '邮箱',
    phone            varchar(11)      null comment '电话',
    status           char default '0' null comment '状态',
    created_time     datetime         null comment '创建时间',
    updated_time     datetime         null comment '更新时间',
    last_login_time  datetime         null comment '最后登录时间',
    signature        varchar(128)     null comment '签名',
    last_online_time datetime         null comment '最后在线时间',
    bg_img_url       varchar(512)     null comment '背景图片'
) comment '用户表 ' collate = utf8mb4_unicode_ci;

create table forest_user_extend
(
    id_user bigint       not null comment '用户表主键',
    github  varchar(64)  null comment 'github',
    weibo   varchar(32)  null comment '微博',
    weixin  varchar(32)  null comment '微信',
    qq      varchar(32)  null comment 'qq',
    blog    varchar(500) null comment '博客'
) comment '用户扩展表 ' collate = utf8mb4_unicode_ci;

create table forest_user_role
(
    id_user      bigint   not null comment '用户表主键',
    id_role      bigint   not null comment '角色表主键',
    created_time datetime null comment '创建时间'
) comment '用户权限表 ' collate = utf8mb4_unicode_ci;

create table forest_user_tag
(
    id           bigint auto_increment comment '主键'
        primary key,
    id_user      bigint      null comment '用户 id',
    id_tag       varchar(32) null comment '标签 id',
    type         char        null comment '0：创建者，1：帖子使用，2：用户自评标签',
    created_time datetime    null comment '创建时间',
    updated_time datetime    null comment '更新时间'
) comment '用户 - 标签关联表 ' collate = utf8mb4_unicode_ci;

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
) comment '浏览表' collate = utf8mb4_unicode_ci;

create table forest_lucene_user_dic
(
    id  int auto_increment comment '字典编号',
    dic char(32) null comment '字典',
    constraint forest_lucene_user_dic_id_uindex
        unique (id)
) comment '用户扩展字典' collate = utf8mb4_unicode_ci;

alter table forest_lucene_user_dic
    add primary key (id);

insert into forest.forest_role (id, name, input_code, status, created_time, updated_time, weights)
values (1, '管理员', 'admin', '0', '2019-11-16 04:22:45', '2019-11-16 04:22:45', 1);
insert into forest.forest_role (id, name, input_code, status, created_time, updated_time, weights)
values (2, '社区管理员', 'blog_admin', '0', '2019-12-05 03:10:05', '2019-12-05 17:11:35', 2);
insert into forest.forest_role (id, name, input_code, status, created_time, updated_time, weights)
values (3, '作者', 'zz', '0', '2020-03-12 15:07:27', '2020-03-12 15:07:27', 3);
insert into forest.forest_role (id, name, input_code, status, created_time, updated_time, weights)
values (4, '普通用户', 'user', '0', '2019-12-05 03:10:59', '2020-03-12 15:13:49', 4);

insert into forest.forest_user (id, account, password, nickname, real_name, sex, avatar_type, avatar_url, email, phone,
                                status, created_time, updated_time, last_login_time, signature)
values (1, 'admin', '8ce2dd866238958ac4f07870766813cdaa39a9b83a8c75e26aa50f23', 'admin', 'admin', '0', '0', null,
        'admin@rymcu.com',
        null, '0', '2021-01-25 18:21:51', '2021-01-25 18:21:54', '2021-01-25 18:21:54', null);
insert into forest.forest_user (id, account, password, nickname, real_name, sex, avatar_type, avatar_url, email, phone,
                                status, created_time, updated_time, last_login_time, signature)
values (2, 'testUser', '8ce2dd866238958ac4f07870766813cdaa39a9b83a8c75e26aa50f23', 'testUser', 'testUser', '0', '0',
        null, 'testUser@rymcu.com',
        null, '0', '2021-01-25 18:21:51', '2021-01-25 18:21:54', '2021-01-25 18:21:54', null);
INSERT INTO `forest`.`forest_user` (`id`, `account`, `password`, `nickname`, `real_name`, `sex`, `avatar_type`,
                                    `avatar_url`, `email`, `phone`, `status`, `created_time`, `updated_time`,
                                    `last_login_time`, `signature`, `last_online_time`, `bg_img_url`)
VALUES (65001, 'testUser1', '8ce2dd866238958ac4f07870766813cdaa39a9b83a8c75e26aa50f23', 'testUser', 'testUser1', '0',
        '0', NULL, 'testUser1@rymcu.com', NULL, '0', '2021-01-25 18:21:51', '2021-01-25 18:21:54',
        '2021-01-25 18:21:54',
        NULL, NULL, NULL);

insert into forest.forest_user_role (id_user, id_role, created_time)
values (1, 1, '2021-01-25 18:22:12');

create table forest_file
(
    id           int unsigned auto_increment comment 'id' primary key,
    md5_value    varchar(40)  not null comment '文件md5值',
    file_path    varchar(255) not null comment '文件上传路径',
    file_url     varchar(255) not null comment '网络访问路径',
    created_time datetime     null comment '创建时间',
    updated_time datetime     null comment '更新时间',
    created_by   int          null comment '创建人',
    file_size    int          null comment '文件大小',
    file_type    varchar(10)  null comment '文件类型'
) comment '文件上传记录表' collate = utf8mb4_unicode_ci;

create index index_md5_value_created_by
    on forest_file (md5_value, created_by);

create index index_created_by
    on forest_file (created_by);

create index index_md5_value
    on forest_file (md5_value);

create table forest_login_record
(
    id              bigint auto_increment comment '主键'
        primary key,
    id_user         bigint       not null comment '用户表主键',
    login_ip        varchar(128) null comment '登录设备IP',
    login_ua        varchar(512) null comment '登录设备UA',
    login_city      varchar(128) null comment '登录设备所在城市',
    login_os        varchar(64)  null comment '登录设备操作系统',
    login_browser   varchar(64)  null comment '登录设备浏览器',
    created_time    datetime     null comment '登录时间',
    login_device_id varchar(512) null comment '登录设备/浏览器指纹',
    constraint forest_login_record_id_uindex
        unique (id)
) comment '登录记录表' collate = utf8mb4_unicode_ci;

create table forest_product
(
    id                  int auto_increment comment '主键'
        primary key,
    product_title       varchar(100)       null comment '产品名',
    product_price       int     default 0  null comment '单价(单位:分)',
    product_img_url     varchar(100)       null comment '产品主图',
    product_description varchar(200)       null comment '产品描述',
    weights             tinyint default 50 null comment '权重,数值越小权限越大;0:无权限',
    created_time        datetime           null comment '创建时间',
    updated_time        datetime           null comment '更新时间',
    tags                varchar(64)        null comment '标签集合',
    status              tinyint default 0  not null comment '状态',
    constraint forest_product_id_uindex
        unique (id)
)
    comment '产品表';

create table forest_product_content
(
    id_product           int      null comment '产品表主键',
    product_content      text     null comment '产品详情原文',
    product_content_html text     null comment '产品详情 Html',
    created_time         datetime null comment '创建时间',
    updated_time         datetime null comment '更新时间'
)
    comment '产品详情表';

INSERT INTO forest.forest_product (id, product_title, product_price, product_img_url, product_description, weights,
                                   created_time, updated_time, tags, status)
VALUES (1, 'Nebula Pi 51', 2000000, 'https://static.rymcu.com/article/1648960741563.jpg',
        'Nebula Pi 51 是社区独家设计的一款一机双芯、资源丰富的 51 单片机入门级开发板, 也是社区的第一款产品。', 20,
        '2022-06-13 22:35:33', '2022-06-13 22:35:33', '51 单片机', 1);
INSERT INTO forest.forest_product (id, product_title, product_price, product_img_url, product_description, weights,
                                   created_time, updated_time, tags, status)
VALUES (2, 'ESP32-S3-DevKitC-1', 398000, 'https://static.rymcu.com/article/1706094905846.png',
        'ESP32-S3-DevKitC-1 是社区在乐鑫官方设计方案上进行升级后推出的一款基于 ESP32-S3-WROOM-1/1U 或 ESP32-S3-WROOM-2/2U 模组的入门级开发板。',
        60, '2023-05-14 08:01:12', '2023-05-14 08:01:16', 'ESP32,ESP32S3', 1);
INSERT INTO forest.forest_product (id, product_title, product_price, product_img_url, product_description, weights,
                                   created_time, updated_time, tags, status)
VALUES (3, 'RYDAPLink', 2000000, 'https://static.rymcu.com/article/1706081416691.png',
        'RYDAPLink 是社区独家设计的一款集 下载、调试、串口、3.3V/5V 供电、串口 ISP 功能于一身的 DAPLink 下载器。', 80,
        '2024-01-24 19:28:43', '2024-01-24 19:28:43', '开源,DAPLink', 1);
INSERT INTO forest.forest_product (id, product_title, product_price, product_img_url, product_description, weights,
                                   created_time, updated_time, tags, status)
VALUES (4, 'ESP32-DevKitC', 398000, 'https://static.rymcu.com/article/1706922435052.png',
        'ESP32-DevKitC 是社区在乐鑫官方设计方案上进行升级后推出的一款基于 ESP32 模组的入门级开发板。', 40,
        '2024-02-03 09:10:49', '2024-02-03 09:10:49', '开源,ESP32', 1);
INSERT INTO forest.forest_product (id, product_title, product_price, product_img_url, product_description, weights,
                                   created_time, updated_time, tags, status)
VALUES (5, 'ESP32-C3-DevKitM-1', 398000, 'https://static.rymcu.com/article/1706922639664.png',
        'ESP32-C3-DevKitM-1 是社区在乐鑫官方设计方案上进行升级后推出的一款基于 ESP32-C3-MINI-1 模组的入门级开发板。', 50,
        '2024-02-03 09:10:49', '2024-02-03 09:10:49', 'ESP32,ESP32C3', 1);

INSERT INTO forest.forest_product_content (id_product, product_content, product_content_html, created_time, updated_time) VALUES (1, '![nebula pi](https://static.rymcu.com/article/1640531590770)

Nebula-Pi 开发板平台

## 1.1主板结构及布局

![](https://static.rymcu.com/article/1640531590844)

图1.1 Nebula-Pi 单片机开发平台

## 1.2主板元件说明

从图1.1可以看出， Nebula-Pi 开发板平台资源丰富，不仅涵盖了 51 单片机所有内部资源，还扩展了大量的外设，单片机的各项功能均可以在平台上得到验证。我们以顺时针的顺序从**①**到**⑳**，分别介绍主要模块的功能。

| 序号 | 元器件 | 功能介绍 |
| --- | --- | --- |
| 1 | 迷你 USB 接口 | 给开发板供电，以及计算机与开发板通信 |
| 2 | 单片机跳线帽 | 开发板上有两块独立的 51 单片机，可以通过这个跳线进行切换，选择你需要使用的单片机。 |
| 3 | 电源开关 | 开发板电源开关 |
| 4 |  51 单片机 STC89C52RC | 这套教程的主角， 51 单片机，选用 STC 公司的 STC89C52RC 型号进行讲解 |
| 5 | 液晶显示器跳线帽 | 液晶显示器的跳线，可以选择 OLED 或者 LCD |
| 6 | 主板复位按钮 | 复位按钮，相当于电脑的重启按键 |
| 7 | 数字温度传感器 | 温度传感器，可以测量环境温度 |
| 8 | 红外接收头 | 接收红外遥控信号专用 |
| 9 | 液晶显示器接口 | 预留的液晶显示器 1602/12864 等的接口 |
| 10 | 数码管 | 4 位数码管，可以同时显示 4 个数字等 |
| 11 | 蜂鸣器 | 相当于开发板的小喇叭，可以发出"滴滴"等声音 |
| 12 | 光敏&热敏电阻 | 两种类型的电阻，分别可以用来测量光强度和温度 |
| 13 | 步进电机接口 | 预留给电机的接口 |
| 14 | 8 个 LED 灯 | 8 个 LED 小灯，可实现指示灯，流水灯等效果 |
| 15 | 增强型 51 单片机 STC12 | 开发板上的另外一块 51 单片机，比主角功能更强大，第一块用来学习，这一块用来做项目，学习、实践两不误 |
| 16 | 2.4G 无线模块接口 | 为 2.4G 无线通信模块预留的接口，无线通信距离可以达到 1-2Km，大大扩展了开发板的功能 |
| 17 | 3 个独立按键 | 3 个按键，可以当做开发板的输入设备，相当于迷你版键盘 |
| 18 | 继电器接口 | 开发板上集成了继电器，这个接口预留给用户接线用的，可以用来控制 220V 设备的开关。因此，可以通过开发板來控制各种类型的家用电器 |
| 19 | 电源端子 | 预留了 5V 和 3.3V 的电源端子，可以用来给其他设备供电 |
| 20 | WiFi 模块接口 | WiFi 模块接口，接上 WiFi 模块，开发板可以轻松上网冲浪 |

表 1-1 主板元器件说明

', '<p><img src="https://static.rymcu.com/article/1640531590770" alt="nebula pi" /></p>
<p>Nebula-Pi 开发板平台</p>
<h2 id="1-1主板结构及布局">1.1 主板结构及布局</h2>
<p><img src="https://static.rymcu.com/article/1640531590844" alt="" /></p>
<p>图 1.1 Nebula-Pi 单片机开发平台</p>
<h2 id="1-2主板元件说明">1.2 主板元件说明</h2>
<p>从图 1.1 可以看出， Nebula-Pi 开发板平台资源丰富，不仅涵盖了 51 单片机所有内部资源，还扩展了大量的外设，单片机的各项功能均可以在平台上得到验证。我们以顺时针的顺序从 <strong>①</strong> 到 <strong>⑳</strong>，分别介绍主要模块的功能。</p>
<table>
<thead>
<tr>
<th>序号</th>
<th>元器件</th>
<th>功能介绍</th>
</tr>
</thead>
<tbody>
<tr>
<td>1</td>
<td>迷你 USB 接口</td>
<td>给开发板供电，以及计算机与开发板通信</td>
</tr>
<tr>
<td>2</td>
<td>单片机跳线帽</td>
<td>开发板上有两块独立的 51 单片机，可以通过这个跳线进行切换，选择你需要使用的单片机。</td>
</tr>
<tr>
<td>3</td>
<td>电源开关</td>
<td>开发板电源开关</td>
</tr>
<tr>
<td>4</td>
<td>51 单片机 STC89C52RC</td>
<td>这套教程的主角， 51 单片机，选用 STC 公司的 STC89C52RC 型号进行讲解</td>
</tr>
<tr>
<td>5</td>
<td>液晶显示器跳线帽</td>
<td>液晶显示器的跳线，可以选择 OLED 或者 LCD</td>
</tr>
<tr>
<td>6</td>
<td>主板复位按钮</td>
<td>复位按钮，相当于电脑的重启按键</td>
</tr>
<tr>
<td>7</td>
<td>数字温度传感器</td>
<td>温度传感器，可以测量环境温度</td>
</tr>
<tr>
<td>8</td>
<td>红外接收头</td>
<td>接收红外遥控信号专用</td>
</tr>
<tr>
<td>9</td>
<td>液晶显示器接口</td>
<td>预留的液晶显示器 1602/12864 等的接口</td>
</tr>
<tr>
<td>10</td>
<td>数码管</td>
<td>4 位数码管，可以同时显示 4 个数字等</td>
</tr>
<tr>
<td>11</td>
<td>蜂鸣器</td>
<td>相当于开发板的小喇叭，可以发出&quot;滴滴&quot;等声音</td>
</tr>
<tr>
<td>12</td>
<td>光敏&amp;热敏电阻</td>
<td>两种类型的电阻，分别可以用来测量光强度和温度</td>
</tr>
<tr>
<td>13</td>
<td>步进电机接口</td>
<td>预留给电机的接口</td>
</tr>
<tr>
<td>14</td>
<td>8 个 LED 灯</td>
<td>8 个 LED 小灯，可实现指示灯，流水灯等效果</td>
</tr>
<tr>
<td>15</td>
<td>增强型 51 单片机 STC12</td>
<td>开发板上的另外一块 51 单片机，比主角功能更强大，第一块用来学习，这一块用来做项目，学习、实践两不误</td>
</tr>
<tr>
<td>16</td>
<td>2.4G 无线模块接口</td>
<td>为 2.4G 无线通信模块预留的接口，无线通信距离可以达到 1-2Km，大大扩展了开发板的功能</td>
</tr>
<tr>
<td>17</td>
<td>3 个独立按键</td>
<td>3 个按键，可以当做开发板的输入设备，相当于迷你版键盘</td>
</tr>
<tr>
<td>18</td>
<td>继电器接口</td>
<td>开发板上集成了继电器，这个接口预留给用户接线用的，可以用来控制 220V 设备的开关。因此，可以通过开发板來控制各种类型的家用电器</td>
</tr>
<tr>
<td>19</td>
<td>电源端子</td>
<td>预留了 5V 和 3.3V 的电源端子，可以用来给其他设备供电</td>
</tr>
<tr>
<td>20</td>
<td>WiFi 模块接口</td>
<td>WiFi 模块接口，接上 WiFi 模块，开发板可以轻松上网冲浪</td>
</tr>
</tbody>
</table>
<p>表 1-1 主板元器件说明</p>
', '2022-06-13 22:35:34', '2022-06-13 22:35:34');
INSERT INTO forest.forest_product_content (id_product, product_content, product_content_html, created_time, updated_time) VALUES (2, 'ESP32-S3-DevKitC-1 —— 是社区在乐鑫官方设计方案上进行升级后推出的一款基于 ESP32S3 模组的入门级开发板。

> 获取地址: [GitHub](https://github.com/rymcu/ESP32-Open) | [Gitee](https://gitee.com/rymcu/ESP32-Open) | [RYMCU](https://rymcu.com/product/4)

![](https://static.rymcu.com/article/1706094905846.png)

## 功能介绍

ESP32-S3-DevKitC-1 开发板的主要组件、接口及控制方式见下。

| 主要组件                               | 介绍                     |
|------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| ESP32-S3-WROOM-1/1U/2              | ESP32-S3-WROOM-1、ESP32-S3-WROOM-1U 和 ESP32-S3-WROOM-2 是通用型 Wi-Fi + 低功耗蓝牙 MCU 模组，具有丰富的外设接口、强大的神经网络运算能力和信号处理能力，专为人工智能和 AIoT 市场打造。ESP32-S3-WROOM-1 和 ESP32-S3-WROOM-2 采用 PCB 板载天线，ESP32-S3-WROOM-1U 采用连接器连接外部天线。 |
| 5 V to 3.3 V LDO（5 V 转 3.3 V LDO）  | 电源转换器，输入 5 V，输出 3.3 V。                                                                                                                                                                                        |
| Pin Headers（排针）                    | 所有可用 GPIO 管脚（除 flash 的 SPI 总线）均已引出至开发板的排针。请查看 排针 获取更多信息。                                                                                                                                                      |
| USB-to-UART Port（USB 转 UART 接口）    | USB Type-C 接口，可用作开发板的供电接口，可烧录固件至芯片，也可作为通信接口，通过板载 USB 转 UART 桥接器与芯片通信。                                                                                                                                          |
| Boot Button（Boot 键）                | 下载按键。按住 Boot 键的同时按一下 Reset 键进入“固件下载”模式，通过串口下载固件。                                                                                                                                                              |
| Reset Button（Reset 键）              | 复位按键。                                                                                                                                                                                                         |
| USB Port（USB 接口）                   | ESP32-S3 USB OTG 接口，支持全速 USB 1.1 标准。ESP32-S3 USB 接口可用作开发板的供电接口，可烧录固件至芯片，可通过 USB 协议与芯片通信，也可用于 JTAG 调试。                                                                                                    |
| USB-to-UART Bridge（USB 转 UART 桥接器） | 单芯片 USB 至 UART 桥接器，可提供高达 3 Mbps 的传输速率。                                                                                                                                                                        |
| RGB LED                            | 可寻址 RGB 发光二极管，由 GPIO38 驱动。                                                                                                                                                                                    |
| 3.3 V Power On LED（3.3 V 电源指示灯）    | 开发板连接 USB 电源后，该指示灯亮起。                                                                                                                                                                                         |

> 在板载 ESP32-S3-WROOM-1/1U 模组系列（使用 8 线 SPI flash/PSRAM）的开发板和板载 ESP32-S3-WROOM-2 模组系列的开发板中，管脚 GPIO35、GPIO36 和 GPIO37 已用于内部 ESP32-S3 芯片与 SPI flash/PSRAM 之间的通信，外部不可使用。

## 排针

下表列出了开发板两侧排针（J1 和 J3）的名称和功能，排针名称如图 ESP32-S3-DevKitC-1（板载 ESP32-S3-WROOM-1） 中所示。

### J1

| 序号 | 名称  | 类型 [^1]  | 功能                                                                 |
|----|-----|-------|--------------------------------------------------------------------|
| 1  | 3V3 | P     | 3.3 V 电源                                                           |
| 2  | 3V3 | P     | 3.3 V 电源                                                           |
| 3  | RST | I     | EN                                                                 |
| 4  | 4   | I/O/T | RTC_GPIO4, GPIO4, TOUCH4, ADC1_CH3                                 |
| 5  | 5   | I/O/T | RTC_GPIO5, GPIO5, TOUCH5, ADC1_CH4                                 |
| 6  | 6   | I/O/T | RTC_GPIO6, GPIO6, TOUCH6, ADC1_CH5                                 |
| 7  | 7   | I/O/T | RTC_GPIO7, GPIO7, TOUCH7, ADC1_CH6                                 |
| 8  | 15  | I/O/T | RTC_GPIO15, GPIO15, U0RTS, ADC2_CH4, XTAL_32K_P                    |
| 9  | 16  | I/O/T | RTC_GPIO16, GPIO16, U0CTS, ADC2_CH5, XTAL_32K_N                    |
| 10 | 17  | I/O/T | RTC_GPIO17, GPIO17, U1TXD, ADC2_CH6                                |
| 11 | 18  | I/O/T | RTC_GPIO18, GPIO18, U1RXD, ADC2_CH7, CLK_OUT3                      |
| 12 | 8   | I/O/T | RTC_GPIO8, GPIO8, TOUCH8, ADC1_CH7, SUBSPICS1                      |
| 13 | 3   | I/O/T | RTC_GPIO3, GPIO3, TOUCH3, ADC1_CH2                                 |
| 14 | 46  | I/O/T | GPIO46                                                             |
| 15 | 9   | I/O/T | RTC_GPIO9, GPIO9, TOUCH9, ADC1_CH8, FSPIHD, SUBSPIHD               |
| 16 | 10  | I/O/T | RTC_GPIO10, GPIO10, TOUCH10, ADC1_CH9, FSPICS0, FSPIIO4, SUBSPICS0 |
| 17 | 11  | I/O/T | RTC_GPIO11, GPIO11, TOUCH11, ADC2_CH0, FSPID, FSPIIO5, SUBSPID     |
| 18 | 12  | I/O/T | RTC_GPIO12, GPIO12, TOUCH12, ADC2_CH1, FSPICLK, FSPIIO6, SUBSPICLK |
| 19 | 13  | I/O/T | RTC_GPIO13, GPIO13, TOUCH13, ADC2_CH2, FSPIQ, FSPIIO7, SUBSPIQ     |
| 20 | 14  | I/O/T | RTC_GPIO14, GPIO14, TOUCH14, ADC2_CH3, FSPIWP, FSPIDQS, SUBSPIWP   |
| 21 | 5V  | P     | 5 V 电源                                                             |
| 22 | G   | G     | 接地                                                                 |

### J3

| 序号 | 名称 | 类型    | 功能                                                    |
|----|----|-------|-------------------------------------------------------|
| 1  | G  | G     | 接地                                                    |
| 2  | TX | I/O/T | U0TXD, GPIO43, CLK_OUT1                               |
| 3  | RX | I/O/T | U0RXD, GPIO44, CLK_OUT2                               |
| 4  | 1  | I/O/T | RTC_GPIO1, GPIO1, TOUCH1, ADC1_CH0                    |
| 5  | 2  | I/O/T | RTC_GPIO2, GPIO2, TOUCH2, ADC1_CH1                    |
| 6  | 42 | I/O/T | MTMS, GPIO42                                          |
| 7  | 41 | I/O/T | MTDI, GPIO41, CLK_OUT1                                |
| 8  | 40 | I/O/T | MTDO, GPIO40, CLK_OUT2                                |
| 9  | 39 | I/O/T | MTCK, GPIO39, CLK_OUT3, SUBSPICS1                     |
| 10 | 38 | I/O/T | GPIO38, FSPIWP, SUBSPIWP, RGB LED                     |
| 11 | 37 | I/O/T | SPIDQS, GPIO37, FSPIQ, SUBSPIQ                        |
| 12 | 36 | I/O/T | SPIIO7, GPIO36, FSPICLK, SUBSPICLK                    |
| 13 | 35 | I/O/T | SPIIO6, GPIO35, FSPID, SUBSPID                        |
| 14 | 0  | I/O/T | RTC_GPIO0, GPIO0                                      |
| 15 | 45 | I/O/T | GPIO45                                                |
| 16 | 48 | I/O/T | GPIO48, SPICLK_N, SUBSPICLK_N_DIFF                    |
| 17 | 47 | I/O/T | GPIO47, SPICLK_P, SUBSPICLK_P_DIFF                    |
| 18 | 21 | I/O/T | RTC_GPIO21, GPIO21                                    |
| 19 | 20 | I/O/T | RTC_GPIO20, GPIO20, U1CTS, ADC2_CH9, CLK_OUT1, USB_D+ |
| 20 | 19 | I/O/T | RTC_GPIO19, GPIO19, U1RTS, ADC2_CH8, CLK_OUT2, USB_D- |
| 21 | G  | G     | 接地                                                    |
| 22 | G  | G     | 接地                                                    |

## 管脚布局

![ESP32S3DevKitC1pinlayoutv1.1.jpg](https://static.rymcu.com/article/1708056462709.jpg)

## 相关文档

- [电路原理图](https://static.rymcu.com/article/1686045860275.pdf)

[^1]: P：电源；I：输入；O：输出；T：可设置为高阻。

', '<p>ESP32-S3-DevKitC-1 —— 是社区在乐鑫官方设计方案上进行升级后推出的一款基于 ESP32S3 模组的入门级开发板。</p>
<blockquote>
<p>获取地址: <a href="https://github.com/rymcu/ESP32-Open">GitHub</a> | <a href="https://gitee.com/rymcu/ESP32-Open">Gitee</a> | <a href="https://rymcu.com/product/4">RYMCU</a></p>
</blockquote>
<p><img src="https://static.rymcu.com/article/1706094905846.png" alt="" /></p>
<h2 id="功能介绍">功能介绍</h2>
<p>ESP32-S3-DevKitC-1 开发板的主要组件、接口及控制方式见下。</p>
<table>
<thead>
<tr>
<th>主要组件</th>
<th>介绍</th>
</tr>
</thead>
<tbody>
<tr>
<td>ESP32-S3-WROOM-1/1U/2</td>
<td>ESP32-S3-WROOM-1、ESP32-S3-WROOM-1U 和 ESP32-S3-WROOM-2 是通用型 Wi-Fi + 低功耗蓝牙 MCU 模组，具有丰富的外设接口、强大的神经网络运算能力和信号处理能力，专为人工智能和 AIoT 市场打造。ESP32-S3-WROOM-1 和 ESP32-S3-WROOM-2 采用 PCB 板载天线，ESP32-S3-WROOM-1U 采用连接器连接外部天线。</td>
</tr>
<tr>
<td>5 V to 3.3 V LDO（5 V 转 3.3 V LDO）</td>
<td>电源转换器，输入 5 V，输出 3.3 V。</td>
</tr>
<tr>
<td>Pin Headers（排针）</td>
<td>所有可用 GPIO 管脚（除 flash 的 SPI 总线）均已引出至开发板的排针。请查看 排针 获取更多信息。</td>
</tr>
<tr>
<td>USB-to-UART Port（USB 转 UART 接口）</td>
<td>USB Type-C 接口，可用作开发板的供电接口，可烧录固件至芯片，也可作为通信接口，通过板载 USB 转 UART 桥接器与芯片通信。</td>
</tr>
<tr>
<td>Boot Button（Boot 键）</td>
<td>下载按键。按住 Boot 键的同时按一下 Reset 键进入“固件下载”模式，通过串口下载固件。</td>
</tr>
<tr>
<td>Reset Button（Reset 键）</td>
<td>复位按键。</td>
</tr>
<tr>
<td>USB Port（USB 接口）</td>
<td>ESP32-S3 USB OTG 接口，支持全速 USB 1.1 标准。ESP32-S3 USB 接口可用作开发板的供电接口，可烧录固件至芯片，可通过 USB 协议与芯片通信，也可用于 JTAG 调试。</td>
</tr>
<tr>
<td>USB-to-UART Bridge（USB 转 UART 桥接器）</td>
<td>单芯片 USB 至 UART 桥接器，可提供高达 3 Mbps 的传输速率。</td>
</tr>
<tr>
<td>RGB LED</td>
<td>可寻址 RGB 发光二极管，由 GPIO38 驱动。</td>
</tr>
<tr>
<td>3.3 V Power On LED（3.3 V 电源指示灯）</td>
<td>开发板连接 USB 电源后，该指示灯亮起。</td>
</tr>
</tbody>
</table>
<blockquote>
<p>在板载 ESP32-S3-WROOM-1/1U 模组系列（使用 8 线 SPI flash/PSRAM）的开发板和板载 ESP32-S3-WROOM-2 模组系列的开发板中，管脚 GPIO35、GPIO36 和 GPIO37 已用于内部 ESP32-S3 芯片与 SPI flash/PSRAM 之间的通信，外部不可使用。</p>
</blockquote>
<h2 id="排针">排针</h2>
<p>下表列出了开发板两侧排针（J1 和 J3）的名称和功能，排针名称如图 ESP32-S3-DevKitC-1（板载 ESP32-S3-WROOM-1） 中所示。</p>
<h3 id="J1">J1</h3>
<table>
<thead>
<tr>
<th>序号</th>
<th>名称</th>
<th>类型 <sup class="footnotes-ref" id="footnotes-ref-1"><a href="#footnotes-def-1">1</a></sup></th>
<th>功能</th>
</tr>
</thead>
<tbody>
<tr>
<td>1</td>
<td>3V3</td>
<td>P</td>
<td>3.3 V 电源</td>
</tr>
<tr>
<td>2</td>
<td>3V3</td>
<td>P</td>
<td>3.3 V 电源</td>
</tr>
<tr>
<td>3</td>
<td>RST</td>
<td>I</td>
<td>EN</td>
</tr>
<tr>
<td>4</td>
<td>4</td>
<td>I/O/T</td>
<td>RTC_GPIO4, GPIO4, TOUCH4, ADC1_CH3</td>
</tr>
<tr>
<td>5</td>
<td>5</td>
<td>I/O/T</td>
<td>RTC_GPIO5, GPIO5, TOUCH5, ADC1_CH4</td>
</tr>
<tr>
<td>6</td>
<td>6</td>
<td>I/O/T</td>
<td>RTC_GPIO6, GPIO6, TOUCH6, ADC1_CH5</td>
</tr>
<tr>
<td>7</td>
<td>7</td>
<td>I/O/T</td>
<td>RTC_GPIO7, GPIO7, TOUCH7, ADC1_CH6</td>
</tr>
<tr>
<td>8</td>
<td>15</td>
<td>I/O/T</td>
<td>RTC_GPIO15, GPIO15, U0RTS, ADC2_CH4, XTAL_32K_P</td>
</tr>
<tr>
<td>9</td>
<td>16</td>
<td>I/O/T</td>
<td>RTC_GPIO16, GPIO16, U0CTS, ADC2_CH5, XTAL_32K_N</td>
</tr>
<tr>
<td>10</td>
<td>17</td>
<td>I/O/T</td>
<td>RTC_GPIO17, GPIO17, U1TXD, ADC2_CH6</td>
</tr>
<tr>
<td>11</td>
<td>18</td>
<td>I/O/T</td>
<td>RTC_GPIO18, GPIO18, U1RXD, ADC2_CH7, CLK_OUT3</td>
</tr>
<tr>
<td>12</td>
<td>8</td>
<td>I/O/T</td>
<td>RTC_GPIO8, GPIO8, TOUCH8, ADC1_CH7, SUBSPICS1</td>
</tr>
<tr>
<td>13</td>
<td>3</td>
<td>I/O/T</td>
<td>RTC_GPIO3, GPIO3, TOUCH3, ADC1_CH2</td>
</tr>
<tr>
<td>14</td>
<td>46</td>
<td>I/O/T</td>
<td>GPIO46</td>
</tr>
<tr>
<td>15</td>
<td>9</td>
<td>I/O/T</td>
<td>RTC_GPIO9, GPIO9, TOUCH9, ADC1_CH8, FSPIHD, SUBSPIHD</td>
</tr>
<tr>
<td>16</td>
<td>10</td>
<td>I/O/T</td>
<td>RTC_GPIO10, GPIO10, TOUCH10, ADC1_CH9, FSPICS0, FSPIIO4, SUBSPICS0</td>
</tr>
<tr>
<td>17</td>
<td>11</td>
<td>I/O/T</td>
<td>RTC_GPIO11, GPIO11, TOUCH11, ADC2_CH0, FSPID, FSPIIO5, SUBSPID</td>
</tr>
<tr>
<td>18</td>
<td>12</td>
<td>I/O/T</td>
<td>RTC_GPIO12, GPIO12, TOUCH12, ADC2_CH1, FSPICLK, FSPIIO6, SUBSPICLK</td>
</tr>
<tr>
<td>19</td>
<td>13</td>
<td>I/O/T</td>
<td>RTC_GPIO13, GPIO13, TOUCH13, ADC2_CH2, FSPIQ, FSPIIO7, SUBSPIQ</td>
</tr>
<tr>
<td>20</td>
<td>14</td>
<td>I/O/T</td>
<td>RTC_GPIO14, GPIO14, TOUCH14, ADC2_CH3, FSPIWP, FSPIDQS, SUBSPIWP</td>
</tr>
<tr>
<td>21</td>
<td>5V</td>
<td>P</td>
<td>5 V 电源</td>
</tr>
<tr>
<td>22</td>
<td>G</td>
<td>G</td>
<td>接地</td>
</tr>
</tbody>
</table>
<h3 id="J3">J3</h3>
<table>
<thead>
<tr>
<th>序号</th>
<th>名称</th>
<th>类型</th>
<th>功能</th>
</tr>
</thead>
<tbody>
<tr>
<td>1</td>
<td>G</td>
<td>G</td>
<td>接地</td>
</tr>
<tr>
<td>2</td>
<td>TX</td>
<td>I/O/T</td>
<td>U0TXD, GPIO43, CLK_OUT1</td>
</tr>
<tr>
<td>3</td>
<td>RX</td>
<td>I/O/T</td>
<td>U0RXD, GPIO44, CLK_OUT2</td>
</tr>
<tr>
<td>4</td>
<td>1</td>
<td>I/O/T</td>
<td>RTC_GPIO1, GPIO1, TOUCH1, ADC1_CH0</td>
</tr>
<tr>
<td>5</td>
<td>2</td>
<td>I/O/T</td>
<td>RTC_GPIO2, GPIO2, TOUCH2, ADC1_CH1</td>
</tr>
<tr>
<td>6</td>
<td>42</td>
<td>I/O/T</td>
<td>MTMS, GPIO42</td>
</tr>
<tr>
<td>7</td>
<td>41</td>
<td>I/O/T</td>
<td>MTDI, GPIO41, CLK_OUT1</td>
</tr>
<tr>
<td>8</td>
<td>40</td>
<td>I/O/T</td>
<td>MTDO, GPIO40, CLK_OUT2</td>
</tr>
<tr>
<td>9</td>
<td>39</td>
<td>I/O/T</td>
<td>MTCK, GPIO39, CLK_OUT3, SUBSPICS1</td>
</tr>
<tr>
<td>10</td>
<td>38</td>
<td>I/O/T</td>
<td>GPIO38, FSPIWP, SUBSPIWP, RGB LED</td>
</tr>
<tr>
<td>11</td>
<td>37</td>
<td>I/O/T</td>
<td>SPIDQS, GPIO37, FSPIQ, SUBSPIQ</td>
</tr>
<tr>
<td>12</td>
<td>36</td>
<td>I/O/T</td>
<td>SPIIO7, GPIO36, FSPICLK, SUBSPICLK</td>
</tr>
<tr>
<td>13</td>
<td>35</td>
<td>I/O/T</td>
<td>SPIIO6, GPIO35, FSPID, SUBSPID</td>
</tr>
<tr>
<td>14</td>
<td>0</td>
<td>I/O/T</td>
<td>RTC_GPIO0, GPIO0</td>
</tr>
<tr>
<td>15</td>
<td>45</td>
<td>I/O/T</td>
<td>GPIO45</td>
</tr>
<tr>
<td>16</td>
<td>48</td>
<td>I/O/T</td>
<td>GPIO48, SPICLK_N, SUBSPICLK_N_DIFF</td>
</tr>
<tr>
<td>17</td>
<td>47</td>
<td>I/O/T</td>
<td>GPIO47, SPICLK_P, SUBSPICLK_P_DIFF</td>
</tr>
<tr>
<td>18</td>
<td>21</td>
<td>I/O/T</td>
<td>RTC_GPIO21, GPIO21</td>
</tr>
<tr>
<td>19</td>
<td>20</td>
<td>I/O/T</td>
<td>RTC_GPIO20, GPIO20, U1CTS, ADC2_CH9, CLK_OUT1, USB_D+</td>
</tr>
<tr>
<td>20</td>
<td>19</td>
<td>I/O/T</td>
<td>RTC_GPIO19, GPIO19, U1RTS, ADC2_CH8, CLK_OUT2, USB_D-</td>
</tr>
<tr>
<td>21</td>
<td>G</td>
<td>G</td>
<td>接地</td>
</tr>
<tr>
<td>22</td>
<td>G</td>
<td>G</td>
<td>接地</td>
</tr>
</tbody>
</table>
<h2 id="管脚布局">管脚布局</h2>
<p><img src="https://static.rymcu.com/article/1708056462709.jpg" alt="ESP32S3DevKitC1pinlayoutv1.1.jpg" /></p>
<h2 id="相关文档">相关文档</h2>
<ul>
<li><a href="https://static.rymcu.com/article/1686045860275.pdf">电路原理图</a></li>
</ul>
<div class="footnotes-defs-div"><hr class="footnotes-defs-hr" />
<ol class="footnotes-defs-ol"><li id="footnotes-def-1"><p>P：电源；I：输入；O：输出；T：可设置为高阻。 <a href="#footnotes-ref-1" class="vditor-footnotes__goto-ref">↩</a></p>
</li>
</ol></div>', '2023-05-14 08:14:23', '2023-05-14 08:14:27');
INSERT INTO forest.forest_product_content (id_product, product_content, product_content_html, created_time, updated_time) VALUES (3, 'RYDAPLink —— 集`下载`、`调试`、`串口`、`3.3V/5V 供电`、`串口 ISP` 功能于一身的 DAPLink 下载器, 由 RYMCU 社区 ( https://rymcu.com ) 倾情打造。

> 获取地址:    [GitHub](https://github.com/rymcu/RYDAPLink) | [Gitee](https://gitee.com/rymcu/RYDAPLink) | [RYMCU](https://rymcu.com/article/21)

## 背景

在开发 STM32 等基于 ARM 内核的单片机时，几乎所有人都会遇到同一个问题。[那就是选择一款什么样的下载调试器呢？](https://rymcu.com/article/22) 市面上有各式各样的下载调试器可供我们选择，我觉得选择一款最合适自己的，才是重要的。常用的下载器包括 J-Link ，ST-Link ，J-Link0B ，CMSIS-DAP ，DAPLink 等。

当接触到 DAPLink 这个方案后，我们根据官方开源的原理图，设计了一版下载调试并且开始了一段时间的试用。实物非常的小巧，尺寸为：50mm x 50mm ，实物长下面这样了。

![RYDAPLink.png](https://static.rymcu.com/article/1706081416691.png)

同时，我们在源码的基础上进行了一些改进，实现了串口 ISP 功能。另外，原来方案的基础上，增加了板载自恢复保险丝，保证即使短路也不会烧坏主板，这样心里踏实多了。项目经过了长时间的使用测试，固件非常稳定。五合一功能：下载、调试、串口、3.3V/5V 供电，串口 ISP 。

本文基于 ARMmebed 官方开源代码打造了一款 DAPLink 下载调试器，并做了些许改进，无论初学与否，跟随下面教程，你也可以打造属于你自己的 DAPLink!

## RYDAPLink 项目简介

### 功能介绍

这是一款 ARM 官方开源的仿真器，可以实现全系列 Cortex-M0/M3/M4/M7 内核芯片的程序下载和调试。特性如下：

* 官方开源，无版权限制，稳定不丢失固件
* SWD 接口，全系列 Cortex-M0/M3/M4/M7 下载和调试（ HID ）
* 自带 USB 虚拟串口，方便程序调试（ CDC ）
* 拖拽下载功能，模拟 U 盘，将 Hex 或 bin 格式文件拖拽或拷贝至 U 盘完成下载(MSC)
* 串口下载程序，改进官方程序实现（串口 ISP ）
* 输出 5.0V 电源，可供电目标电路
* 输出 3.3V 电源，可供电目标器件
* 板载自恢复保险丝，短路自保护
* Win10 即插即用，无需驱动

', '<p>RYDAPLink —— 集 <code>下载</code>、<code>调试</code>、<code>串口</code>、<code>3.3V/5V 供电</code>、<code>串口 ISP</code> 功能于一身的 DAPLink 下载器, 由 RYMCU 社区 ( https://rymcu.com ) 倾情打造。</p>
<blockquote>
<p>获取地址:    <a href="https://github.com/rymcu/RYDAPLink">GitHub</a> | <a href="https://gitee.com/rymcu/RYDAPLink">Gitee</a> | <a href="https://rymcu.com/article/21">RYMCU</a></p>
</blockquote>
<h2 id="背景">背景</h2>
<p>在开发 STM32 等基于 ARM 内核的单片机时，几乎所有人都会遇到同一个问题。<a href="https://rymcu.com/article/22">那就是选择一款什么样的下载调试器呢？</a> 市面上有各式各样的下载调试器可供我们选择，我觉得选择一款最合适自己的，才是重要的。常用的下载器包括 J-Link ，ST-Link ，J-Link0B ，CMSIS-DAP ，DAPLink 等。</p>
<p>当接触到 DAPLink 这个方案后，我们根据官方开源的原理图，设计了一版下载调试并且开始了一段时间的试用。实物非常的小巧，尺寸为：50mm x 50mm ，实物长下面这样了。</p>
<p><img src="https://static.rymcu.com/article/1706081416691.png" alt="RYDAPLink.png" /></p>
<p>同时，我们在源码的基础上进行了一些改进，实现了串口 ISP 功能。另外，原来方案的基础上，增加了板载自恢复保险丝，保证即使短路也不会烧坏主板，这样心里踏实多了。项目经过了长时间的使用测试，固件非常稳定。五合一功能：下载、调试、串口、3.3V/5V 供电，串口 ISP 。</p>
<p>本文基于 ARMmebed 官方开源代码打造了一款 DAPLink 下载调试器，并做了些许改进，无论初学与否，跟随下面教程，你也可以打造属于你自己的 DAPLink!</p>
<h2 id="RYDAPLink-项目简介">RYDAPLink 项目简介</h2>
<h3 id="功能介绍">功能介绍</h3>
<p>这是一款 ARM 官方开源的仿真器，可以实现全系列 Cortex-M0/M3/M4/M7 内核芯片的程序下载和调试。特性如下：</p>
<ul>
<li>官方开源，无版权限制，稳定不丢失固件</li>
<li>SWD 接口，全系列 Cortex-M0/M3/M4/M7 下载和调试（ HID ）</li>
<li>自带 USB 虚拟串口，方便程序调试（ CDC ）</li>
<li>拖拽下载功能，模拟 U 盘，将 Hex 或 bin 格式文件拖拽或拷贝至 U 盘完成下载(MSC)</li>
<li>串口下载程序，改进官方程序实现（串口 ISP ）</li>
<li>输出 5.0V 电源，可供电目标电路</li>
<li>输出 3.3V 电源，可供电目标器件</li>
<li>板载自恢复保险丝，短路自保护</li>
<li>Win10 即插即用，无需驱动</li>
</ul>
', '2024-01-24 19:31:43', '2024-01-24 19:31:46');
INSERT INTO forest.forest_product_content (id_product, product_content, product_content_html, created_time, updated_time) VALUES (4, 'ESP32-DevKitC —— 是社区在乐鑫官方设计方案上进行升级后推出的一款基于 ESP32 模组的入门级开发板。

> 获取地址: [GitHub](https://github.com/rymcu/ESP32-Open) | [Gitee](https://gitee.com/rymcu/ESP32-Open) | [RYMCU](https://rymcu.com/product/4/#相关文档)

![](https://static.rymcu.com/article/1706922435052.png)

## 功能介绍

ESP32-DevKitC V4 开发板的主要组件、接口及控制方式见下。

| 主要组件            | 基本介绍                                                                      |
|-----------------|---------------------------------------------------------------------------|
| ESP32-WROOM-32  | 基于 ESP32 的模组。更多详情，请见 [《ESP32-WROOM-32 技术规格书》](https://espressif.com/sites/default/files/documentation/esp32-wroom-32_datasheet_cn.pdf)。                          |
| EN              | 复位按键。                                                                     |
| Boot            | 下载按键。按下 Boot 键并保持，同时按一下 EN 键（此时不要松开 Boot 键）进入“固件下载”模式，通过串口下载固件。           |
| USB-to-UART 桥接器 | 单芯片 USB-UART 桥接器，可提供高达 3 Mbps 的传输速率。                                      |
| USB Type-C 接口   | USB 接口，可用作电路板的供电电源，或连接 PC 和 ESP32-WROOM-32 模组的通信接口。                       |
| 5V Power On LED | 开发板通电后（USB 或外部 5 V），该指示灯将亮起。更多信息，请见 相关文档 中的原理图。                           |
| I/O             | 板上模组的绝大部分管脚均已引出至开发板的排针。用户可以对 ESP32 进行编程，实现 PWM、ADC、DAC、I2C、I2S、SPI 等多种功能。 |

## 排针

下表列出了开发板两侧排针（J1 和 J3）的名称和功能，排针名称如图 ESP32-DevKitC V4（板载 ESP32-WROOM-32） 中所示。

### J1

| 编号 | 名称   | 类型[^1] | 功能                                      |
|----|------|--------|-----------------------------------------|
| 1  | 3V3  | P      | 3.3 V 电源                                |
| 2  | EN   | I      | CHIP_PU, Reset                          |
| 3  | VP   | I      | GPIO36, ADC1_CH0, S_VP                  |
| 4  | VN   | I      | GPIO39, ADC1_CH3, S_VN                  |
| 5  | IO34 | I      | GPIO34, ADC1_CH6, VDET_1                |
| 6  | IO35 | I      | GPIO35, ADC1_CH7, VDET_2                |
| 7  | IO32 | I/O    | GPIO32, ADC1_CH4, TOUCH_CH9, XTAL_32K_P |
| 8  | IO33 | I/O    | GPIO33, ADC1_CH5, TOUCH_CH8, XTAL_32K_N |
| 9  | IO25 | I/O    | GPIO25, ADC1_CH8, DAC_1                 |
| 10 | IO26 | I/O    | GPIO26, ADC2_CH9, DAC_2                 |
| 11 | IO27 | I/O    | GPIO27, ADC2_CH7, TOUCH_CH7             |
| 12 | IO14 | I/O    | GPIO14, ADC2_CH6, TOUCH_CH6, MTMS       |
| 13 | IO12 | I/O    | GPIO12, ADC2_CH5, TOUCH_CH5, MTDI       |
| 14 | GND  | G      | 接地                                      |
| 15 | IO13 | I/O    | GPIO13, ADC2_CH4, TOUCH_CH4, MTCK       |
| 16 | D2   | I/O    | GPIO9, D2 [^2]                          |
| 17 | D3   | I/O    | GPIO10, D3 [^2]                         |
| 18 | CMD  | I/O    | GPIO11, CMD [^2]                        |
| 19 | 5V   | P      | 5 V 电源                                  |

### J3

| 编号 | 名称   | 类型 [^1] | 功能                                |
|----|------|---------|-----------------------------------|
| 1  | GND  | G       | 接地                                |
| 2  | IO23 | I/O     | GPIO23                            |
| 3  | IO22 | I/O     | GPIO22                            |
| 4  | TX   | I/O     | GPIO1, U0TXD                      |
| 5  | RX   | I/O     | GPIO3, U0RXD                      |
| 6  | IO21 | I/O     | GPIO21                            |
| 7  | GND  | G       | 接地                                |
| 8  | IO19 | I/O     | GPIO19                            |
| 9  | IO18 | I/O     | GPIO18                            |
| 10 | IO5  | I/O     | GPIO5                             |
| 11 | IO17 | I/O     | GPIO17 [^3]                       |
| 12 | IO16 | I/O     | GPIO16 [^3]                       |
| 13 | IO4  | I/O     | GPIO4, ADC2_CH0, TOUCH_CH0        |
| 14 | IO0  | I/O     | GPIO0, ADC2_CH1, TOUCH_CH1, Boot  |
| 15 | IO2  | I/O     | GPIO2, ADC2_CH2, TOUCH_CH2        |
| 16 | IO15 | I/O     | GPIO15, ADC2_CH3, TOUCH_CH3, MTDO |
| 17 | D1   | I/O     | GPIO8, D1 [^2]                    |
| 18 | D0   | I/O     | GPIO7, D0 [^2]                    |
| 19 | CLK  | I/O     | GPIO6, CLK [^2]                   |

[^1]: P：电源；I：输入；O：输出。

[^2]: 管脚 D0、D1、D2、D3、CMD 和 CLK 用于 ESP32 芯片与 SPI flash 间的内部通信，集中分布在开发板两侧靠近 USB 端口的位置。通常而言，这些管脚最好不连，否则可能影响 SPI flash/SPI RAM 的工作。

[^3]: 管脚 GPIO16 和 GPIO17 仅适用于板载 ESP32-WROOM 系列和 ESP32-SOLO-1 的开发板，板载 ESP32-WROVER 系列开发板的管脚 GPIO16 和 GPIO17 保留内部使用。

## 管脚布局

![](https://static.rymcu.com/article/1706927881092.png)

## 相关文档

- [电路原理图](https://static.rymcu.com/article/1686043415890.pdf)
- [PCB 源文件](https://static.rymcu.com/article/1691599744950.epro)


', '<p>ESP32-DevKitC —— 是社区在乐鑫官方设计方案上进行升级后推出的一款基于 ESP32 模组的入门级开发板。</p>
<blockquote>
<p>获取地址: <a href="https://github.com/rymcu/ESP32-Open">GitHub</a> | <a href="https://gitee.com/rymcu/ESP32-Open">Gitee</a> | <a href="https://rymcu.com/product/4/#相关文档">RYMCU</a></p>
</blockquote>
<p><img src="https://static.rymcu.com/article/1706922435052.png" alt="" /></p>
<h2 id="功能介绍">功能介绍</h2>
<p>ESP32-DevKitC V4 开发板的主要组件、接口及控制方式见下。</p>
<table>
<thead>
<tr>
<th>主要组件</th>
<th>基本介绍</th>
</tr>
</thead>
<tbody>
<tr>
<td>ESP32-WROOM-32</td>
<td>基于 ESP32 的模组。更多详情，请见 <a href="https://espressif.com/sites/default/files/documentation/esp32-wroom-32_datasheet_cn.pdf">《ESP32-WROOM-32 技术规格书》</a>。</td>
</tr>
<tr>
<td>EN</td>
<td>复位按键。</td>
</tr>
<tr>
<td>Boot</td>
<td>下载按键。按下 Boot 键并保持，同时按一下 EN 键（此时不要松开 Boot 键）进入“固件下载”模式，通过串口下载固件。</td>
</tr>
<tr>
<td>USB-to-UART 桥接器</td>
<td>单芯片 USB-UART 桥接器，可提供高达 3 Mbps 的传输速率。</td>
</tr>
<tr>
<td>USB Type-C 接口</td>
<td>USB 接口，可用作电路板的供电电源，或连接 PC 和 ESP32-WROOM-32 模组的通信接口。</td>
</tr>
<tr>
<td>5V Power On LED</td>
<td>开发板通电后（USB 或外部 5 V），该指示灯将亮起。更多信息，请见 相关文档 中的原理图。</td>
</tr>
<tr>
<td>I/O</td>
<td>板上模组的绝大部分管脚均已引出至开发板的排针。用户可以对 ESP32 进行编程，实现 PWM、ADC、DAC、I2C、I2S、SPI 等多种功能。</td>
</tr>
</tbody>
</table>
<h2 id="排针">排针</h2>
<p>下表列出了开发板两侧排针（J1 和 J3）的名称和功能，排针名称如图 ESP32-DevKitC V4（板载 ESP32-WROOM-32） 中所示。</p>
<h3 id="J1">J1</h3>
<table>
<thead>
<tr>
<th>编号</th>
<th>名称</th>
<th>类型<sup class="footnotes-ref" id="footnotes-ref-1"><a href="#footnotes-def-1">1</a></sup></th>
<th>功能</th>
</tr>
</thead>
<tbody>
<tr>
<td>1</td>
<td>3V3</td>
<td>P</td>
<td>3.3 V 电源</td>
</tr>
<tr>
<td>2</td>
<td>EN</td>
<td>I</td>
<td>CHIP_PU, Reset</td>
</tr>
<tr>
<td>3</td>
<td>VP</td>
<td>I</td>
<td>GPIO36, ADC1_CH0, S_VP</td>
</tr>
<tr>
<td>4</td>
<td>VN</td>
<td>I</td>
<td>GPIO39, ADC1_CH3, S_VN</td>
</tr>
<tr>
<td>5</td>
<td>IO34</td>
<td>I</td>
<td>GPIO34, ADC1_CH6, VDET_1</td>
</tr>
<tr>
<td>6</td>
<td>IO35</td>
<td>I</td>
<td>GPIO35, ADC1_CH7, VDET_2</td>
</tr>
<tr>
<td>7</td>
<td>IO32</td>
<td>I/O</td>
<td>GPIO32, ADC1_CH4, TOUCH_CH9, XTAL_32K_P</td>
</tr>
<tr>
<td>8</td>
<td>IO33</td>
<td>I/O</td>
<td>GPIO33, ADC1_CH5, TOUCH_CH8, XTAL_32K_N</td>
</tr>
<tr>
<td>9</td>
<td>IO25</td>
<td>I/O</td>
<td>GPIO25, ADC1_CH8, DAC_1</td>
</tr>
<tr>
<td>10</td>
<td>IO26</td>
<td>I/O</td>
<td>GPIO26, ADC2_CH9, DAC_2</td>
</tr>
<tr>
<td>11</td>
<td>IO27</td>
<td>I/O</td>
<td>GPIO27, ADC2_CH7, TOUCH_CH7</td>
</tr>
<tr>
<td>12</td>
<td>IO14</td>
<td>I/O</td>
<td>GPIO14, ADC2_CH6, TOUCH_CH6, MTMS</td>
</tr>
<tr>
<td>13</td>
<td>IO12</td>
<td>I/O</td>
<td>GPIO12, ADC2_CH5, TOUCH_CH5, MTDI</td>
</tr>
<tr>
<td>14</td>
<td>GND</td>
<td>G</td>
<td>接地</td>
</tr>
<tr>
<td>15</td>
<td>IO13</td>
<td>I/O</td>
<td>GPIO13, ADC2_CH4, TOUCH_CH4, MTCK</td>
</tr>
<tr>
<td>16</td>
<td>D2</td>
<td>I/O</td>
<td>GPIO9, D2 <sup class="footnotes-ref" id="footnotes-ref-2"><a href="#footnotes-def-2">2</a></sup></td>
</tr>
<tr>
<td>17</td>
<td>D3</td>
<td>I/O</td>
<td>GPIO10, D3 <sup class="footnotes-ref" id="footnotes-ref-2:2"><a href="#footnotes-def-2">2</a></sup></td>
</tr>
<tr>
<td>18</td>
<td>CMD</td>
<td>I/O</td>
<td>GPIO11, CMD <sup class="footnotes-ref" id="footnotes-ref-2:3"><a href="#footnotes-def-2">2</a></sup></td>
</tr>
<tr>
<td>19</td>
<td>5V</td>
<td>P</td>
<td>5 V 电源</td>
</tr>
</tbody>
</table>
<h3 id="J3">J3</h3>
<table>
<thead>
<tr>
<th>编号</th>
<th>名称</th>
<th>类型 <sup class="footnotes-ref" id="footnotes-ref-1:2"><a href="#footnotes-def-1">1</a></sup></th>
<th>功能</th>
</tr>
</thead>
<tbody>
<tr>
<td>1</td>
<td>GND</td>
<td>G</td>
<td>接地</td>
</tr>
<tr>
<td>2</td>
<td>IO23</td>
<td>I/O</td>
<td>GPIO23</td>
</tr>
<tr>
<td>3</td>
<td>IO22</td>
<td>I/O</td>
<td>GPIO22</td>
</tr>
<tr>
<td>4</td>
<td>TX</td>
<td>I/O</td>
<td>GPIO1, U0TXD</td>
</tr>
<tr>
<td>5</td>
<td>RX</td>
<td>I/O</td>
<td>GPIO3, U0RXD</td>
</tr>
<tr>
<td>6</td>
<td>IO21</td>
<td>I/O</td>
<td>GPIO21</td>
</tr>
<tr>
<td>7</td>
<td>GND</td>
<td>G</td>
<td>接地</td>
</tr>
<tr>
<td>8</td>
<td>IO19</td>
<td>I/O</td>
<td>GPIO19</td>
</tr>
<tr>
<td>9</td>
<td>IO18</td>
<td>I/O</td>
<td>GPIO18</td>
</tr>
<tr>
<td>10</td>
<td>IO5</td>
<td>I/O</td>
<td>GPIO5</td>
</tr>
<tr>
<td>11</td>
<td>IO17</td>
<td>I/O</td>
<td>GPIO17 <sup class="footnotes-ref" id="footnotes-ref-3"><a href="#footnotes-def-3">3</a></sup></td>
</tr>
<tr>
<td>12</td>
<td>IO16</td>
<td>I/O</td>
<td>GPIO16 <sup class="footnotes-ref" id="footnotes-ref-3:2"><a href="#footnotes-def-3">3</a></sup></td>
</tr>
<tr>
<td>13</td>
<td>IO4</td>
<td>I/O</td>
<td>GPIO4, ADC2_CH0, TOUCH_CH0</td>
</tr>
<tr>
<td>14</td>
<td>IO0</td>
<td>I/O</td>
<td>GPIO0, ADC2_CH1, TOUCH_CH1, Boot</td>
</tr>
<tr>
<td>15</td>
<td>IO2</td>
<td>I/O</td>
<td>GPIO2, ADC2_CH2, TOUCH_CH2</td>
</tr>
<tr>
<td>16</td>
<td>IO15</td>
<td>I/O</td>
<td>GPIO15, ADC2_CH3, TOUCH_CH3, MTDO</td>
</tr>
<tr>
<td>17</td>
<td>D1</td>
<td>I/O</td>
<td>GPIO8, D1 <sup class="footnotes-ref" id="footnotes-ref-2:4"><a href="#footnotes-def-2">2</a></sup></td>
</tr>
<tr>
<td>18</td>
<td>D0</td>
<td>I/O</td>
<td>GPIO7, D0 <sup class="footnotes-ref" id="footnotes-ref-2:5"><a href="#footnotes-def-2">2</a></sup></td>
</tr>
<tr>
<td>19</td>
<td>CLK</td>
<td>I/O</td>
<td>GPIO6, CLK <sup class="footnotes-ref" id="footnotes-ref-2:6"><a href="#footnotes-def-2">2</a></sup></td>
</tr>
</tbody>
</table>
<h2 id="管脚布局">管脚布局</h2>
<p><img src="https://static.rymcu.com/article/1706927881092.png" alt="" /></p>
<h2 id="相关文档">相关文档</h2>
<ul>
<li><a href="https://static.rymcu.com/article/1686043415890.pdf">电路原理图</a></li>
<li><a href="https://static.rymcu.com/article/1691599744950.epro">PCB 源文件</a></li>
</ul>
<div class="footnotes-defs-div"><hr class="footnotes-defs-hr" />
<ol class="footnotes-defs-ol"><li id="footnotes-def-1"><p>P：电源；I：输入；O：输出。 <a href="#footnotes-ref-1" class="vditor-footnotes__goto-ref">↩</a> <a href="#footnotes-ref-1:2" class="vditor-footnotes__goto-ref">↩</a></p>
</li>
<li id="footnotes-def-2"><p>管脚 D0、D1、D2、D3、CMD 和 CLK 用于 ESP32 芯片与 SPI flash 间的内部通信，集中分布在开发板两侧靠近 USB 端口的位置。通常而言，这些管脚最好不连，否则可能影响 SPI flash/SPI RAM 的工作。 <a href="#footnotes-ref-2" class="vditor-footnotes__goto-ref">↩</a> <a href="#footnotes-ref-2:2" class="vditor-footnotes__goto-ref">↩</a> <a href="#footnotes-ref-2:3" class="vditor-footnotes__goto-ref">↩</a> <a href="#footnotes-ref-2:4" class="vditor-footnotes__goto-ref">↩</a> <a href="#footnotes-ref-2:5" class="vditor-footnotes__goto-ref">↩</a> <a href="#footnotes-ref-2:6" class="vditor-footnotes__goto-ref">↩</a></p>
</li>
<li id="footnotes-def-3"><p>管脚 GPIO16 和 GPIO17 仅适用于板载 ESP32-WROOM 系列和 ESP32-SOLO-1 的开发板，板载 ESP32-WROVER 系列开发板的管脚 GPIO16 和 GPIO17 保留内部使用。 <a href="#footnotes-ref-3" class="vditor-footnotes__goto-ref">↩</a> <a href="#footnotes-ref-3:2" class="vditor-footnotes__goto-ref">↩</a></p>
</li>
</ol></div>', '2024-02-03 10:43:43', '2024-02-03 10:43:45');
INSERT INTO forest.forest_product_content (id_product, product_content, product_content_html, created_time, updated_time) VALUES (5, 'ESP32-C3-DevKitM-1 —— 是社区在乐鑫官方设计方案上进行升级后推出的一款基于 ESP32C3 模组的入门级开发板。

> 获取地址: [GitHub](https://github.com/rymcu/ESP32-Open) | [Gitee](https://gitee.com/rymcu/ESP32-Open) | [RYMCU](https://rymcu.com/product/4)

![](https://static.rymcu.com/article/1706922639664.png)

## 功能介绍

ESP32-C3-DevKitM-1 开发板的主要组件、接口及控制方式见下。

| 主要组件                               | 介绍                                                                                                                                          |
|------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------|
| ESP32-C3-MINI-1                    | ESP32-C3-MINI-1 是一款通用型 Wi-Fi 和低功耗蓝牙双模模组，采用 PCB 板载天线。该款模组集成配置 4 MB 嵌入式 flash 的 ESP32-C3FN4 芯片。由于 flash 直接封装在芯片中，ESP32-C3-MINI-1 模组具有更小的封装尺寸。 |
| 5 V to 3.3 V LDO（5 V 转 3.3 V LDO）  | 电源转换器，输入 5 V，输出 3.3 V。                                                                                                                      |
| 5 V Power On LED（5 V 电源指示灯）        | 开发板连接 USB 电源后，该指示灯亮起。                                                                                                                       |
| Pin Headers（排针）                    | 所有可用 GPIO 管脚（除 flash 的 SPI 总线）均已引出至开发板的排针。请查看 排针 获取更多信息。                                                                                    |
| Boot Button（Boot 键）                | 下载按键。按住 Boot 键的同时按一下 Reset 键进入“固件下载”模式，通过串口下载固件。                                                                                            |
| Micro-USB Port（Micro-USB 接口）       | USB 接口。可用作开发板的供电电源或 PC 和 ESP32-C3FN4 芯片的通信接口。                                                                                               |
| Reset Button（Reset 键）              | 复位按键。                                                                                                                                       |
| USB-to-UART Bridge（USB 至 UART 桥接器） | 单芯片 USB 至 UART 桥接器，可提供高达 3 Mbps 的传输速率。                                                                                                      |
| RGB LED                            | 可寻址 RGB 发光二极管，由 GPIO8 驱动。                                                                                                                   |

## 排针

下表列出了开发板两侧排针（J1 和 J3）的名称和功能，排针名称如图 ESP32-C3-DevKitM-1 中所示。

### J1

| 序号 | 名称   | 类型 [^1]  | 功能                          |
|----|------|-------|-----------------------------|
| 1  | GND  | G     | 接地                          |
| 2  | 3V3  | P     | 3.3 V 电源                    |
| 3  | 3V3  | P     | 3.3 V 电源                    |
| 4  | IO2  | I/O/T | GPIO2 2 , ADC1_CH2, FSPIQ   |
| 5  | IO3  | I/O/T | GPIO3, ADC1_CH3             |
| 6  | GND  | G     | 接地                          |
| 7  | RST  | I     | CHIP_PU                     |
| 8  | GND  | G     | 接地                          |
| 9  | IO0  | I/O/T | GPIO0, ADC1_CH0, XTAL_32K_P |
| 10 | IO1  | I/O/T | GPIO1, ADC1_CH1, XTAL_32K_N |
| 11 | IO10 | I/O/T | GPIO10, FSPICS0             |
| 12 | GND  | G     | 接地                          |
| 13 | 5V   | P     | 5 V 电源                      |
| 14 | 5V   | P     | 5 V 电源                      |
| 15 | GND  | G     | 接地                          |

### J3

| 序号 | 名称   | 类型 [^1]  | 功能                            |
|----|------|-------|-------------------------------|
| 1  | GND  | G     | 接地                            |
| 2  | TX   | I/O/T | GPIO21, U0TXD                 |
| 3  | RX   | I/O/T | GPIO20, U0RXD                 |
| 4  | GND  | G     | 接地                            |
| 5  | IO9  | I/O/T | GPIO9 [^2]                       |
| 6  | IO8  | I/O/T | GPIO8 [^2], RGB LED              |
| 7  | GND  | G     | 接地                            |
| 8  | IO7  | I/O/T | GPIO7, FSPID, MTDO            |
| 9  | IO6  | I/O/T | GPIO6, FSPICLK, MTCK          |
| 10 | IO5  | I/O/T | GPIO5, ADC2_CH0, FSPIWP, MTDI |
| 11 | IO4  | I/O/T | GPIO4, ADC1_CH4, FSPIHD, MTMS |
| 12 | GND  | G     | 接地                            |
| 13 | IO18 | I/O/T | GPIO18                        |
| 14 | IO19 | I/O/T | GPIO19                        |
| 15 | GND  | G     | 接地                            |

## 管脚布局

![esp32c3devkitm1v1pinout.png](https://static.rymcu.com/article/1708083360388.png)

## 相关文档

- [电路原理图](https://static.rymcu.com/article/1686045790604.pdf)

[^1]: P：电源；I：输入；O：输出；T：可设置为高阻。

[^2]: GPIO2、GPIO8、GPIO9 为 ESP32-C3FN4 芯片的 Strapping 管脚。在芯片上电和系统复位过程中，Strapping 管脚根据管脚的二进制电压值控制芯片功能。Strapping 管脚的具体描述和应用，请参考 [ESP32-C3 技术规格书](https://www.espressif.com/sites/default/files/documentation/esp32-c3_datasheet_cn.pdf) 的 Strapping 管脚章节。

', '<p>ESP32-C3-DevKitM-1 —— 是社区在乐鑫官方设计方案上进行升级后推出的一款基于 ESP32C3 模组的入门级开发板。</p>
<blockquote>
<p>获取地址: <a href="https://github.com/rymcu/ESP32-Open">GitHub</a> | <a href="https://gitee.com/rymcu/ESP32-Open">Gitee</a> | <a href="https://rymcu.com/product/4">RYMCU</a></p>
</blockquote>
<p><img src="https://static.rymcu.com/article/1706922639664.png" alt="" /></p>
<h2 id="功能介绍">功能介绍</h2>
<p>ESP32-C3-DevKitM-1 开发板的主要组件、接口及控制方式见下。</p>
<table>
<thead>
<tr>
<th>主要组件</th>
<th>介绍</th>
</tr>
</thead>
<tbody>
<tr>
<td>ESP32-C3-MINI-1</td>
<td>ESP32-C3-MINI-1 是一款通用型 Wi-Fi 和低功耗蓝牙双模模组，采用 PCB 板载天线。该款模组集成配置 4 MB 嵌入式 flash 的 ESP32-C3FN4 芯片。由于 flash 直接封装在芯片中，ESP32-C3-MINI-1 模组具有更小的封装尺寸。</td>
</tr>
<tr>
<td>5 V to 3.3 V LDO（5 V 转 3.3 V LDO）</td>
<td>电源转换器，输入 5 V，输出 3.3 V。</td>
</tr>
<tr>
<td>5 V Power On LED（5 V 电源指示灯）</td>
<td>开发板连接 USB 电源后，该指示灯亮起。</td>
</tr>
<tr>
<td>Pin Headers（排针）</td>
<td>所有可用 GPIO 管脚（除 flash 的 SPI 总线）均已引出至开发板的排针。请查看 排针 获取更多信息。</td>
</tr>
<tr>
<td>Boot Button（Boot 键）</td>
<td>下载按键。按住 Boot 键的同时按一下 Reset 键进入“固件下载”模式，通过串口下载固件。</td>
</tr>
<tr>
<td>Micro-USB Port（Micro-USB 接口）</td>
<td>USB 接口。可用作开发板的供电电源或 PC 和 ESP32-C3FN4 芯片的通信接口。</td>
</tr>
<tr>
<td>Reset Button（Reset 键）</td>
<td>复位按键。</td>
</tr>
<tr>
<td>USB-to-UART Bridge（USB 至 UART 桥接器）</td>
<td>单芯片 USB 至 UART 桥接器，可提供高达 3 Mbps 的传输速率。</td>
</tr>
<tr>
<td>RGB LED</td>
<td>可寻址 RGB 发光二极管，由 GPIO8 驱动。</td>
</tr>
</tbody>
</table>
<h2 id="排针">排针</h2>
<p>下表列出了开发板两侧排针（J1 和 J3）的名称和功能，排针名称如图 ESP32-C3-DevKitM-1 中所示。</p>
<h3 id="J1">J1</h3>
<table>
<thead>
<tr>
<th>序号</th>
<th>名称</th>
<th>类型 <sup class="footnotes-ref" id="footnotes-ref-1"><a href="#footnotes-def-1">1</a></sup></th>
<th>功能</th>
</tr>
</thead>
<tbody>
<tr>
<td>1</td>
<td>GND</td>
<td>G</td>
<td>接地</td>
</tr>
<tr>
<td>2</td>
<td>3V3</td>
<td>P</td>
<td>3.3 V 电源</td>
</tr>
<tr>
<td>3</td>
<td>3V3</td>
<td>P</td>
<td>3.3 V 电源</td>
</tr>
<tr>
<td>4</td>
<td>IO2</td>
<td>I/O/T</td>
<td>GPIO2 2 , ADC1_CH2, FSPIQ</td>
</tr>
<tr>
<td>5</td>
<td>IO3</td>
<td>I/O/T</td>
<td>GPIO3, ADC1_CH3</td>
</tr>
<tr>
<td>6</td>
<td>GND</td>
<td>G</td>
<td>接地</td>
</tr>
<tr>
<td>7</td>
<td>RST</td>
<td>I</td>
<td>CHIP_PU</td>
</tr>
<tr>
<td>8</td>
<td>GND</td>
<td>G</td>
<td>接地</td>
</tr>
<tr>
<td>9</td>
<td>IO0</td>
<td>I/O/T</td>
<td>GPIO0, ADC1_CH0, XTAL_32K_P</td>
</tr>
<tr>
<td>10</td>
<td>IO1</td>
<td>I/O/T</td>
<td>GPIO1, ADC1_CH1, XTAL_32K_N</td>
</tr>
<tr>
<td>11</td>
<td>IO10</td>
<td>I/O/T</td>
<td>GPIO10, FSPICS0</td>
</tr>
<tr>
<td>12</td>
<td>GND</td>
<td>G</td>
<td>接地</td>
</tr>
<tr>
<td>13</td>
<td>5V</td>
<td>P</td>
<td>5 V 电源</td>
</tr>
<tr>
<td>14</td>
<td>5V</td>
<td>P</td>
<td>5 V 电源</td>
</tr>
<tr>
<td>15</td>
<td>GND</td>
<td>G</td>
<td>接地</td>
</tr>
</tbody>
</table>
<h3 id="J3">J3</h3>
<table>
<thead>
<tr>
<th>序号</th>
<th>名称</th>
<th>类型 <sup class="footnotes-ref" id="footnotes-ref-1:2"><a href="#footnotes-def-1">1</a></sup></th>
<th>功能</th>
</tr>
</thead>
<tbody>
<tr>
<td>1</td>
<td>GND</td>
<td>G</td>
<td>接地</td>
</tr>
<tr>
<td>2</td>
<td>TX</td>
<td>I/O/T</td>
<td>GPIO21, U0TXD</td>
</tr>
<tr>
<td>3</td>
<td>RX</td>
<td>I/O/T</td>
<td>GPIO20, U0RXD</td>
</tr>
<tr>
<td>4</td>
<td>GND</td>
<td>G</td>
<td>接地</td>
</tr>
<tr>
<td>5</td>
<td>IO9</td>
<td>I/O/T</td>
<td>GPIO9 <sup class="footnotes-ref" id="footnotes-ref-2"><a href="#footnotes-def-2">2</a></sup></td>
</tr>
<tr>
<td>6</td>
<td>IO8</td>
<td>I/O/T</td>
<td>GPIO8 <sup class="footnotes-ref" id="footnotes-ref-2:2"><a href="#footnotes-def-2">2</a></sup>, RGB LED</td>
</tr>
<tr>
<td>7</td>
<td>GND</td>
<td>G</td>
<td>接地</td>
</tr>
<tr>
<td>8</td>
<td>IO7</td>
<td>I/O/T</td>
<td>GPIO7, FSPID, MTDO</td>
</tr>
<tr>
<td>9</td>
<td>IO6</td>
<td>I/O/T</td>
<td>GPIO6, FSPICLK, MTCK</td>
</tr>
<tr>
<td>10</td>
<td>IO5</td>
<td>I/O/T</td>
<td>GPIO5, ADC2_CH0, FSPIWP, MTDI</td>
</tr>
<tr>
<td>11</td>
<td>IO4</td>
<td>I/O/T</td>
<td>GPIO4, ADC1_CH4, FSPIHD, MTMS</td>
</tr>
<tr>
<td>12</td>
<td>GND</td>
<td>G</td>
<td>接地</td>
</tr>
<tr>
<td>13</td>
<td>IO18</td>
<td>I/O/T</td>
<td>GPIO18</td>
</tr>
<tr>
<td>14</td>
<td>IO19</td>
<td>I/O/T</td>
<td>GPIO19</td>
</tr>
<tr>
<td>15</td>
<td>GND</td>
<td>G</td>
<td>接地</td>
</tr>
</tbody>
</table>
<h2 id="管脚布局">管脚布局</h2>
<p><img src="https://static.rymcu.com/article/1708083360388.png" alt="esp32c3devkitm1v1pinout.png" /></p>
<h2 id="相关文档">相关文档</h2>
<ul>
<li><a href="https://static.rymcu.com/article/1686045790604.pdf">电路原理图</a></li>
</ul>
<div class="footnotes-defs-div"><hr class="footnotes-defs-hr" />
<ol class="footnotes-defs-ol"><li id="footnotes-def-1"><p>P：电源；I：输入；O：输出；T：可设置为高阻。 <a href="#footnotes-ref-1" class="vditor-footnotes__goto-ref">↩</a> <a href="#footnotes-ref-1:2" class="vditor-footnotes__goto-ref">↩</a></p>
</li>
<li id="footnotes-def-2"><p>GPIO2、GPIO8、GPIO9 为 ESP32-C3FN4 芯片的 Strapping 管脚。在芯片上电和系统复位过程中，Strapping 管脚根据管脚的二进制电压值控制芯片功能。Strapping 管脚的具体描述和应用，请参考 <a href="https://www.espressif.com/sites/default/files/documentation/esp32-c3_datasheet_cn.pdf">ESP32-C3 技术规格书</a> 的 Strapping 管脚章节。 <a href="#footnotes-ref-2" class="vditor-footnotes__goto-ref">↩</a> <a href="#footnotes-ref-2:2" class="vditor-footnotes__goto-ref">↩</a></p>
</li>
</ol></div>', '2024-02-03 10:54:25', '2024-02-03 10:54:27');



INSERT INTO forest.forest_bank (id, bank_name, bank_owner, bank_description, created_by, created_time)
VALUES (1, '社区中央银行', 1, '社区中央银行', 1, '2020-11-26 21:24:19');
INSERT INTO forest.forest_bank (id, bank_name, bank_owner, bank_description, created_by, created_time)
VALUES (2, '社区发展与改革银行', 1, '社区发展与改革银行', 1, '2020-11-26 21:31:27');


INSERT INTO forest.forest_bank_account (id, id_bank, bank_account, account_balance, account_owner, created_time,
                                        account_type)
VALUES (2, 1, '100000002', 1207980.00000000, 2, '2020-11-26 21:37:18', '1');
INSERT INTO forest.forest_bank_account (id, id_bank, bank_account, account_balance, account_owner, created_time,
                                        account_type)
VALUES (1, 1, '100000001', 997500000.00000000, 1, '2020-11-26 21:36:21', '1');
INSERT INTO `forest`.`forest_bank_account` (`id`, `id_bank`, `bank_account`, `account_balance`, `account_owner`,
                                            `created_time`, `account_type`)
VALUES (3, 1, '100000061', 100.00000000, 65001, '2020-11-26 21:37:18', '0');
INSERT INTO `forest`.`forest_bank_account` (`id`, `id_bank`, `bank_account`, `account_balance`, `account_owner`,
                                            `created_time`, `account_type`)
VALUES (4, 1, '100000063', 100.00000000, 65003, '2020-11-26 21:37:18', '0');
