# helio-boot

## 项目介绍
`helio-boot`基于 Spring Boot 2.7.x，是一款预置SaaS、RBAC能力的单体项目脚手架，适合初学者学习 JavaWeb 开发的良好实践

JDK compatibility: 1.8 - 17

【[官方文档](https://helio.uncarbon.cc/)】 
【[主要技术栈依赖](https://helio.uncarbon.cc/#/i18n/zh-CN/helio-starters/dependencies)】
【[快速启动步骤](https://helio.uncarbon.cc/#/i18n/zh-CN/helio-boot/quick-start)】
【[前端演示站](https://helio-demo.uncarbon.cc/)】

需要先安装 `MySQL`、`Redis` 等必需中间件

基础支撑构件 [helio-starters](https://github.com/uncarbon97/helio-starters) 已推送至Maven中央仓库，加载时会自动拉取

## 配套后台管理前端模板 && 代码生成器
| 项目名                  | 简介                                                                          | Gitee                                                      | GitHub                                                       |
|----------------------|-----------------------------------------------------------------------------|------------------------------------------------------------|--------------------------------------------------------------|
| helio-generator      | 可一键生成单体or微服务版的前、后端代码，减少无谓的重复劳动                                              | [Gitee](https://gitee.com/uncarbon97/helio-generator)      | [GitHub](https://github.com/uncarbon97/helio-generator)      |
| helio-admin-vue-vben | 基于[Vue vben admin](https://github.com/anncwb/vue-vben-admin) 改造适配的前端框架，开箱即用 | [Gitee](https://gitee.com/uncarbon97/helio-admin-vue-vben) | [GitHub](https://github.com/uncarbon97/helio-admin-vue-vben) |

## 演示项目
| 项目名            | 后端                                                                    | 前端                                                                              | 需导入数据库文件                                                                     |
|----------------|-----------------------------------------------------------------------|---------------------------------------------------------------------------------|------------------------------------------------------------------------------|
| library 图书管理系统 | [Gitee](https://gitee.com/uncarbon97/helio-boot/tree/demo%2Flibrary/) | [Gitee](https://gitee.com/uncarbon97/helio-admin-vue-vben/tree/demo%2Flibrary/) | attachments/db/MySQL/helio_boot.sql<br>attachments/db/demo/library_MySQL.sql |
