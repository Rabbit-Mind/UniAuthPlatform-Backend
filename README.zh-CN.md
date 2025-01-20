<div align="center"> <a href="https://github.com/Rabbit-Mind/UniAuthBackend"> <img alt="Rabbit Mind Logo" width="215" src="https://user.imyrs.net/d/lzb/RabbitMind/logo.png?sign=jBMCR7E9C9HNZ_Dj-8mHNIizxe28pTjkVtkQMyEZ_kU=:0"> </a> <br> <br>

[![license](https://img.shields.io/github/license/Rabbit-Mind/UniAuthPlatform-Backend)](LICENSE)

<h1>Rabbit Mind 通用认证平台后端</h1>
</div>

[![作者](https://img.shields.io/badge/作者-Ramid-orange)](https://github.com/RamidLab)
![JDK Version](https://img.shields.io/badge/JAVA-JDK8+-red.svg)
[![Spring Boot](https://img.shields.io/maven-central/v/org.springframework.boot/spring-boot-dependencies.svg?label=Spring%20Boot&logo=Spring)](https://search.maven.org/artifact/org.springframework.boot/spring-boot-dependencies)
[![Spring Cloud](https://img.shields.io/maven-central/v/org.springframework.cloud/spring-cloud-dependencies.svg?label=Spring%20Cloud&logo=Spring)](https://search.maven.org/artifact/org.springframework.cloud/spring-cloud-dependencies)
[![Spring Cloud Alibaba](https://img.shields.io/maven-central/v/com.alibaba.cloud/spring-cloud-alibaba-dependencies.svg?label=Spring%20Cloud%20Alibaba&logo=Spring)](https://search.maven.org/artifact/com.alibaba.cloud/spring-cloud-alibaba-dependencies)


**中文** | [English](./README.md)

## 简介

Rabbit Mind 通用认证的后端是基于 Spring Boot 和 Spring Cloud 框架开发的高性能分布式解决方案，旨在提供一个通用、可扩展的认证与权限控制模块。该后端以模块化设计为核心，支持多租户架构，集成了主流的认证与授权机制（如基于 JWT 的认证和 RBAC 权限管理），并提供了高效的用户管理、角色分配和资源访问控制能力。

## 备注

项目通过对 wemirr-platform 进行二开，详情可查看原作者[官网](https://docs.battcn.com/)。

## 特性

- **优雅的布局**：提供多套主题和导览模式，可以自由组合。
- **功能齐全**：完全支持 SAAS、多租户和 RBAC。
- **WebSocket** 消息推送：通过 Redis 实现实时分布式消息推送。
- **动态网关**：支持通过 Redis 和 Nacos 配置动态路由的开启与关闭。
- **性能**：在 2MB 网络环境下，响应时间通常在 10-150 毫秒之间，最慢不超过 300 毫秒。

## 架构

- **技术栈**：Spring Cloud Alibaba 2023，Spring Cloud 2023，Nacos，Sentinel，Mybatis-Plus，多租户，Sa-Token，Redis，MySQL，RabbitMQ
- **通讯**：WebSocket，Redis（分布式消息推送）
- **分布式任务**：snail-job 用于任务调度
- **日志与链路追踪**：集成 skywalking 用于链路追踪（其他链路追踪方案可选）
- **动态网关管理**：支持 Redis 和 Nacos 配置的动态路由管理

## 预览

> 平台账号 0000 账号 admin 密码 123456

> 租户账号 8888 账号 admin 密码 123456

## 文档

## 安装使用

- 获取项目代码

```bash
git clone https://github.com/Rabbit-Mind/UniAuthPlatform-Backend.git
```

- 安装项目依赖

```bash
cd UniAuthPlatform-Backend

mvn install ramid-ua-platform-dependencies

mvn install ramid-ua-platform-framework
```

- 安装环境依赖（以下提供 Docker 方式安装）

基础服务如 Redis、MySQL、Nacos 和 RabbitMQ 可以通过以下 Docker 命令来安装：

```bash
docker pull redis:latest
docker run -itd --name redis -p 6379:6379 redis

docker pull mysql:latest
docker run -itd --name mysql-test -p 3306:3306 -e MYSQL_ROOT_PASSWORD={{自己设定密码}} mysql

docker pull nacos/nacos-server
docker run --name nacos -itd -p 8848:8848 -p 9848:9848 -p 9849:9849 --restart=always -e MODE=standalone nacos/nacos-server

docker pull docker.io/macintoshplus/rabbitmq-management
docker run -d -p 5671:5671 -p 5672:5672 -p 15672:15672 rabbitmq_image_id
```

可选服务（非必须）

如果需要使用 Sentinel 流量保护和 Skywalking（链路追踪），可以选择部署这些服务：

```bash
docker pull elasticsearch:7.9.3
docker pull apache/skywalking-oap-server:8.5.0-es7
docker pull apache/skywalking-ui:8.5.0

docker network create ramid
docker run --name elasticsearch --net ramid -p 9200:9200 -d -e "discovery.type=single-node" elasticsearch:7.9.3
docker run --name oap --net ramid -p 1234:1234 -p 12800:12800 -p 11800:11800 -d -e SW_STORAGE=elasticsearch7 -e SW_STORAGE_ES_CLUSTER_NODES=elasticsearch:9200 apache/skywalking-oap-server:8.5.0-es7
docker run --name oap-ui --net ramid -p 10086:8080 -d -e TZ=Asia/Shanghai -e SW_OAP_ADDRESS=oap:12800 apache/skywalking-ui:8.5.0
```

如果要使用 Skywalking，请在 JVM 参数中添加 Skywalking Agent：

```bash
VmOption -javaagent:/path/to/skywalking-agent.jar
```

例：

```bash
nohup java -javaagent:/path/to/skywalking-agent.jar -Dskywalking.agent.service_name=ramid-ua-platform -Dskywalking.collector.backend_service=127.0.0.1:11800 -jar ramid-ua-platform.jar -d > logs/start.log &

```

- 运行

- 打包

## 更新日志

[CHANGELOG](https://github.com/Rabbit-Mind/UniAuthPlatform-Backend/releases)

## 如何贡献

非常欢迎你的加入！[提一个 Issue](https://github.com/Rabbit-Mind/UniAuthPlatform-Backend/issues/new/choose) 或者提交一个 Pull Request。

**Pull Request:**

1. Fork 代码!
2. 创建自己的分支: `git checkout -b feature/xxxx`
3. 提交你的修改: `git commit -am 'feat(function): add xxxxx'`
4. 推送您的分支: `git push origin feature/xxxx`
5. 提交`pull request`

## Git 贡献提交规范

- 参考 [vue](https://github.com/vuejs/vue/blob/dev/.github/COMMIT_CONVENTION.md) 规范 ([Angular](https://github.com/conventional-changelog/conventional-changelog/tree/master/packages/conventional-changelog-angular))

  - `feat` 增加新功能
  - `fix` 修复问题/BUG
  - `style` 代码风格相关无影响运行结果的
  - `perf` 优化/性能提升
  - `refactor` 重构
  - `revert` 撤销修改
  - `test` 测试相关
  - `docs` 文档/注释
  - `chore` 依赖更新/脚手架配置修改等
  - `ci` 持续集成
  - `types` 类型定义文件更改
  - `wip` 开发中

## 维护者

[@Ramid](https://github.com/RamidLab)

## 贡献

## 通讯
