# forest 食用手册

感谢使用 forest，以下是本项目的开发手册，你将了解到如何从零开始进行开发环境的搭建。

## 获取最新代码到本地

```shell
git clone "https://github.com/rymcu/forest"
```

## 开发环境搭建
> 本项目在以下开发环境下开发，其他环境下可能会出现兼容性问题，建议使用以下环境进行开发。
- IDE: `JetBrains IntelliJ IDEA UItimate`
- `Java 8(8u101 +)`
- `MySQL v7.x +`
- `Redis v7.x +`

### 手动搭建 MySQL 和 Redis 环境
> 手动安装 MySQL 时，需初始化数据库, sql 文件创建的用户密码都是 admin。

在配置好 MySQL 和 Redis 的环境之后，在运行项目前，你还需要初始化好数据库中数据。通过执行 `src/main/resources/static`
目录下的 `forest.sql` 文件进行数据库初始化操作。

![forest-sql](https://static.rymcu.com/article/1650261394563.png)


### 使用 docker 快速搭建 MySQL 和 Redis 环境
> 使用 docker 快速搭建 MySQL 和 Redis 环境时，请修改 `docker\dev` 目录下的 `docker-compose.yml` 文件中的默认密码。

- 确保你已经安装了 `Docker`和`Docker-compose`
- 在 `docker\dev` 目录下执行 `docker-compose up` 可初始化 `redis` 和 `mysql` 环境
- 如需修改相关配置，请编辑 `docker\dev` 目录下的 `docker-compose.yml` 文件

### 配置文件说明

在初始化好数据库之后，你需要在`application.yml`中进行相关信息的配置，具体分为如下几个部分。

#### 数据库配置

```yaml
datasource:
  url: jdbc:mysql://localhost:3306/forest?characterEncoding=UTF-8&autoReconnect=true&useSSL=false&serverTimezone=Asia/Shanghai
  username: root
  password: # 数据库密码
  driver-class-name: com.mysql.cj.jdbc.Driver
```

- `characterEncoding` 指定处理字符的解码和编码的格式
- `serverTimezone` 指定时区

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

### 运行项目

在你完成了前面的操作之后，就可以直接运行`ForestApplication.java`启动项目了，至此，如果后端项目已经成功启动，则可以切换到 
[nebula](https://github.com/rymcu/nebula) 运行前端项目了。

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
