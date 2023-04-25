# forest 食用手册

感谢使用 forest，以下是本项目的开发手册，你将了解到如何从零开始进行开发环境的搭建。运行本项目需要用到Mysql和Redis，你可以使用自己配置好的
Mysql 和 Redis，也可以使用本项目提供的`docker`的方式快速搭建 Mysql 和 Redis 环境。

本项目提供了两种方式，你可以一步一步按照教程来配置，也可以
直接使用`docker`快速运行本项目。

## 获取最新代码到本地

```shell
git clone "https://github.com/rymcu/forest"
```

## 开发环境搭建

你可以使用自己配置好的 Mysql 和 Redis，也可以使用本项目提供的`docker`的方式快速搭建 Mysql 和 Redis 环境。

### 方式1

你需要自己配置好下面的环境，下面是本项目所使用的基础开发环境，在运行项目前，请确保你已经配置好基础开发环境。

- IDE: `JetBrains IntelliJ IDEA UItimate`
- `Java 8`
- `Mysql v8.0.29`
- `Redis v7.0.8`

### 方式2

该方式使用 Docker 搭建 Mysql 和 Redis 的开发环境

- 确保你已经安装了 `Docker`和`Docker-compose`
- 在 `docker\dev` 目录下执行 `docker-compose up` 可初始化 `redis` 和 `mysql` 环境
- 如需修改相关配置，请编辑 `docker\dev` 目录下的 `docker-compose.yml` 文件

### 初始化数据库

在配置好 Mysql 和 Redis 的环境之后，在运行项目前，你还需要初始化好数据库中数据。通过执行 `src/main/resources/static`
目录下的 `forest.sql` 文件进行数据库初始化操作。

![forest-sql](https://static.rymcu.com/article/1650261394563.png)

### 配置文件说明

在初始化好数据库之后，你需要在`application.yml`中进行基本信息的配置，具体分为如下几个部分。

#### 数据库配置

```yaml
datasource:
  url: jdbc:mysql://localhost:3306/forest?characterEncoding=UTF-8&autoReconnect=true&useSSL=false&serverTimezone=Asia/Shanghai&allowMultiQueries=true
  username: root
  password: # 数据库密码
  driver-class-name: com.mysql.cj.jdbc.Driver
```

- `characterEncoding` 指定处理字符的解码和编码的格式
- `serverTimezone` 指定时区
- `allowMultiQueries` 允许多行 `sql` 一起执行

#### redis 配置

```yaml
redis:
  host: 127.0.0.1
  port: 6379
  password: # redis 密码
  database: 1
  timeout: 3000
  jedis:
    pool:
      max-active: 8
      max-wait: 1
      max-idle: 500
      min-idle: 0
```

#### 邮箱服务配置

```yaml
mail:
  host: smtp.163.com # 网站发送邮件邮箱服务 host
  port: 465
  username: # 邮箱
  password: # 密码
```

用户注册及找回密码时使用,本项目使用的是网易邮箱,其他邮箱可根据官方教程配置。

#### 系统资源路径配置

```yaml
resource:
  domain: http://yourdomain.com # 网站域名，本地测试时填写前端项目访问地址即可
  file-path: http://yourdomain.com # 上传文件前缀域名，本地测试时填写前端项目访问地址即可
  pic-path: /yoursrc/xx/nebula/static # 上传文件存储地址，本地测试时填写前端项目路径下的 static 目录即可
```

#### 百度相关配置（可选择）

```yaml
baidu:
  data:
    site: https://yourdomain.com # 百度搜索(SEO)绑定网站域名
    token: xxxx
  ai:
    appId: xxx # 百度AI-文字识别 应用 appId
    appKey: xxxx # 百度AI-文字识别 应用 appKey
    secretKey: xxxx # 百度AI-文字识别 应用 secretKey
```

### 运行项目

在你完成了前面的操作之后，就可以直接运行`ForestApplication.java`启动项目了，至此，如果后端项目已经成功启动，则可以切换到
[nebula](https://github.com/rymcu/nebula)运行 前端项目了。

## 常见问题

**Q: 找不到数据库配置，Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource
could be configured.**

A: 检查是否配置了资源文件目录

![1636569760e18471dd2c74bedac5756e5fff537df.png](https://static.rymcu.com/article/1650261657433.png)

**Q: Caused by: java.lang.IllegalArgumentException: Failed to decrypt.
Caused by: java.lang.IllegalArgumentException: String length must be a multiple of four.**

A: 这种情况一般是发生在你使用`application-dev.yml`
启动项目的时候，里面在druid初始化的时候你需要修改一下相关的参数，具体如下:

```yaml
spring:
  datasource:
    druid:
      # 关闭密码加密
      connection-properties: config.decrypt=false;config.decrypt.key=${publicKey}
```