# helio-boot

## 项目介绍

基于 Spring Boot 3.2.x，是一款预置SaaS、RBAC能力的单体项目脚手架，适合初学者学习 JavaWeb 开发的良好实践  
单Maven模块，定位为快速开发脚手架  
多模块版请移步至[helio-boot-modular](https://github.com/uncarbon97/helio-boot-modular)项目

JDK compatibility: 17 - 21

【[前端演示站](https://helio-demo.uncarbon.cc/)】
【[官方文档](https://helio.uncarbon.cc/)】 
【[主要技术栈依赖](https://helio.uncarbon.cc/#/i18n/zh-CN/helio-starters/dependencies)】
【[快速启动步骤](https://helio.uncarbon.cc/#/i18n/zh-CN/helio-boot/quick-start)】
【[更新记录](https://helio.uncarbon.cc/#/i18n/zh-CN/appendix/change-log)】
【[编码良好实践](https://helio.uncarbon.cc/#/i18n/zh-CN/experience/good-practices)】

需要先安装 `MySQL`、`Redis` 等必需中间件

基础支撑构件 [helio-starters](https://github.com/uncarbon97/helio-starters) 已推送至Maven中央仓库，加载时会自动拉取

## 配套后台管理前端模板 & 代码生成器
| 项目名                  | 简介                                                                          | Gitee                                                      | GitHub                                                       |
|----------------------|-----------------------------------------------------------------------------|------------------------------------------------------------|--------------------------------------------------------------|
| helio-generator      | 可一键生成单体or微服务版的前、后端代码，减少无谓的重复劳动                                              | [Gitee](https://gitee.com/uncarbon97/helio-generator)      | [GitHub](https://github.com/uncarbon97/helio-generator)      |
| helio-admin-vue-vben | 基于[Vue vben admin](https://github.com/anncwb/vue-vben-admin) 改造适配的前端框架，开箱即用 | [Gitee](https://gitee.com/uncarbon97/helio-admin-vue-vben) | [GitHub](https://github.com/uncarbon97/helio-admin-vue-vben) |

## 源码包结构
```
├─attachments                  附件
│  └─db                         数据库变更脚本
│      ├─MySQL
│      │  └─upgrade             Helio升级时MySQL变更脚本
│      └─PostgreSQL
│         └─upgrade             Helio升级时PostgreSQL变更脚本
└─src
    ├─main                      主要代码
    │  ├─java
    │  │  └─cc
    │  │      └─uncarbon
    │  │          ├─aspect       自定义切面
    │  │          ├─config       配置类
    │  │          ├─helper       助手类
    │  │          ├─interceptor  Web拦截器
    │  │          └─module       业务代码模块
    │  │              ├─adminapi     admin-api，用于后台管理的HTTP-API
    │  │              │  ├─constant  常量
    │  │              │  ├─model     抽象模型
    │  │              │  ├─util      静态工具类
    │  │              │  └─web       即：Controller
    │  │              │      ├─auth    登录登出接口
    │  │              │      ├─common  常用通用接口
    │  │              │      ├─oss     对象存储接口
    │  │              │      └─sys     预置系统管理接口（如：后台用户、后台角色、后台菜单等）
    │  │              ├─appapi       app-api，用于C端的HTTP-API【只是一个骨架，并没有业务实现】
    │  │              │  ├─constant  常量
    │  │              │  └─web       即：Controller
    │  │              ├─oss          对象存储服务
    │  │              │  ├─config    配置类
    │  │              │  ├─constant  常量
    │  │              │  ├─entity    实体
    │  │              │  ├─enums     枚举
    │  │              │  ├─facade    门面
    │  │              │  │  └─impl   门面实现类
    │  │              │  ├─mapper    Mybatis Mapper
    │  │              │  ├─model     抽象模型
    │  │              │  │  ├─request   用于请求的
    │  │              │  │  └─response  用于响应的
    │  │              │  └─service  服务类
    │  │              └─sys          预置系统管理服务
    │  │                  ├─annotation 自定义注解
    │  │                  ├─constant   常量
    │  │                  ├─entity     实体
    │  │                  ├─enums      枚举
    │  │                  ├─extension  自定义扩展
    │  │                  │  └─impl    自定义扩展实现类
    │  │                  ├─facade     门面
    │  │                  │  └─impl    门面实现类
    │  │                  ├─mapper     Mybatis Mapper
    │  │                  │  └─xml     Mybatis Mapper XML
    │  │                  ├─model      抽象模型
    │  │                  │  ├─request  用于请求的
    │  │                  │  └─response 用于响应的
    │  │                  ├─service    服务类
    │  │                  └─util       静态工具类
    │  └─resources                资源，包含符合Spring Boot标准的YAML配置文件、Logback配置文件等
    │      └─i18n                 国际化文案定义
```
