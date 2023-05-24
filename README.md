# ptu-blog

#### 介绍

课程设计，一个简单的博客系统

#### 软件架构

springboot+Vue，前后端分离的项目


#### 安装教程

- 1.使用git克隆项目或者下载压缩包

```bash
git clone https://gitee.com/aaabanana/ptu-blog.git
```

- 2.创建数据库blog并运行项目目录下/document/sql的sql文件

```sql
CREATE DATABASE blog character set utf8;
```

- 3.在项目目录/blog/src/main/resources/下，修改application.yml文件中的数据库密码

```yaml
server:
  port: 7777

system:
  cross-origin: http://localhost:8080 # 前端的端口号

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/blog?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8
    username: root
    password: # 你的数据库密码
  servlet:
    multipart:
      max-file-size: 2MB
      max-request-size: 5MB
  redis:
    # 地址
    host: localhost
    # 端口，默认为6379
    port: 6379

mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      logic-delete-field: delFlag
      logic-delete-value: 1
      logic-not-delete-value: 0
      id-type: auto


```

- 4.加载完maven配置后，先运行redis，再在项目目录/blog/src/main/java/com/my/blog下，运行BlogApplication.java（启动后端）
- 5.进入项目目录/ptu-blog-vue，输入命令

```bash
npm install # 下载前端的依赖
npm run dev # 运行
```

即可启动前端

#### 使用说明

1.  xxxx
2.  xxxx
3.  xxxx

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request