# forest
![forest](src/main/resources/static/logo_size.jpg)

下一代的知识社区系统,为未来而建

[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Frymcu%2Fforest.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2Frymcu%2Fforest?ref=badge_shield)

## 💡 简介

forest（[ˈfôrəst]，n.森林）是一款现代化的知识社区项目，使用 SpringBoot + Shiro + MyBatis + JWT + Redis 实现。

与 [nebula](https://github.com/rymcu/nebula) （[ˈnebyələ]，n.星云）一起食用，让我们一起探索知识社区的未来。

## 💡 Tips
- 在 `docker\dev` 目录下执行 `docker-compose up` 可初始化 `redis` 和 `mysql` 环境，更多使用教程请阅读 [forest 食用手册](UserManual.md)

## ⚡ 动机

在 2019 年的某一天,受到 [Hugh](https://rymcu.com/user/RYMCU-J) 的邀请, 构建一个开源嵌入式知识学习交流平台。因此就有了 forest 这个项目。 forest
在很多方面受到了 [Symphony](https://github.com/88250/symphony) 的启发,并尝试着在 [Symphony](https://github.com/88250/symphony)
和 [B3log 思想](https://ld246.com/article/1546941897596) 的基础上进一步探索。

## ✨ 特性

- 内容编辑器
  - Markdown（GFM）
  - emoji
  - 上传文件
    - 图片
    - 文件
    - 单独渲染 MP3 文件
    - 单独渲染视频文件
  - 剪切板处理
    - 粘贴内容处理为 Markdown
    - 粘贴图片自动重新上传
  - 数学公式（LaTeX）、流程图支持
  - 工具栏
    - 表情
    - 粗体
    - 斜体
    - 引用
    - 无序列表
    - 有序列表
    - 链接
    - 上传
    - 预览
    - 全屏 
  - 编辑模式
    - 传统的 Markdown 分屏编辑预览
    - 保留 Markdown 标记符的即时渲染
    - 类富文本编辑器的所见即所得
- 注册
  - 用户名
  - 邮箱
  - 验证码
- 登录
  - 账户(用户名/邮箱)
  - 密码
  - 忘记密码
    - 邮箱
    - 邮箱验证
- 发帖
  - 帖子类型
    - 普通帖子
  - 标题
  - 正文
    - 内容编辑器
  - 标签
    - 使用已有（选择、自动完成）或创建
    - 默认“待分类”
  - 发布后 
    - 可更新
    - 可删除
- 回帖
  - 内容编辑器
  - 回复（回复针对回帖）
- 货币
  - 货币规则
- 浏览贴子
  - 编辑自己的帖子
  - 发布时间/浏览数/标签
  - 分享
    - 微信
    - 分享链接（带用户标识）

## 报告缺陷

> 社区系统可能存在一些潜在的缺陷，大家如果有空的话可以帮助我们一起解决。

如果你在使用社区时发现了如下类型的问题，请回帖进行反馈，并附上 bug 截图以及操作步骤：

* **功能性缺陷**：例如发布文章失败、创建作品集失败等
* **安全性漏洞**：例如 XSS/CSRF、盗用用户信息等

## 功能建议

欢迎对社区提出功能特性方面的建议，我们一起讨论，如果有可能我们会尽快实现。

在提功能建议前可以先看一下 [计划表](https://rymcu.com/article/29) ，避免重复提议

## 鸣谢
- 感谢以下开发者对 Forest 作出的贡献：

<a href="https://github.com/rymcu/forest/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=rymcu/forest&max=1000" />
</a>

- 感谢 `JetBrains` 对本项目的帮助,为作者提供了开源许可版 `JetBrains` 全家桶

![JetBrains](src/main/resources/static/jb_beam.svg)


## ⭐ Star 历史

[![Stargazers over time](https://starchart.cc/rymcu/forest.svg)](https://starchart.cc/rymcu/forest)

## License
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Frymcu%2Fforest.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2Frymcu%2Fforest?ref=badge_large)


