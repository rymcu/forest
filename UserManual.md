# forest 食用手册
感谢使用 forest，以下是本项目的开发手册
## 开发环境搭建
### ide
本项目使用 `JetBrains IntelliJ IDEA UItimate` 作为编辑器进行开发
### java 环境
本项目在 `java se 8` 环境下进行开发
### 数据库
- `redis`
- `mysql`
## 其他
- 本项目使用了 `Lombok`，所以你还需在你的编辑器上安装 `Lombok` 插件
- 本项目使用 `maven` 作为依赖管理工具

## 初始化数据库

![forest-sql](https://static.rymcu.com/article/1650261394563.png)

执行 `resources/static` 目录下的 `forest.sql` 文件进行数据库初始化操作

## 配置文件说明

### 数据库配置
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
### redis 配置
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
### 邮箱服务配置
```yaml
mail:
  host: smtp.163.com # 网站发送邮件邮箱服务 host
  port: 465
  username: # 邮箱
  password: # 密码
```

用户注册及找回密码时使用,本项目使用的是网易邮箱,其他邮箱可根据官方教程配置

### 系统资源路径配置
```yaml
resource:
  domain: http://yourdomain.com # 网站域名，本地测试时填写前端项目访问地址即可
  file-path: http://yourdomain.com # 上传文件前缀域名，本地测试时填写前端项目访问地址即可
  pic-path: /yoursrc/xx/nebula/static # 上传文件存储地址，本地测试时填写前端项目路径下的 static 目录即可
```
### 百度相关配置
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

## 常见问题
**Q: 找不到数据库配置，Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.**

A: 检查是否配置了资源文件目录

![1636569760e18471dd2c74bedac5756e5fff537df.png](https://static.rymcu.com/article/1650261657433.png)
