<div align="center"> <a href="https://github.com/Rabbit-Mind/UniAuthPlatform-Backend"> <img alt="VbenAdmin Logo" width="215" src="https://user.imyrs.net/d/lzb/RabbitMind/logo.png?sign=jBMCR7E9C9HNZ_Dj-8mHNIizxe28pTjkVtkQMyEZ_kU=:0"> </a> <br> <br>

[![license](https://img.shields.io/github/license/Rabbit-Mind/UniAuthPlatform-Backend)](LICENSE)

<h1>Rabbit Mind Universal Authentication Platform Backend</h1>
</div>

[![Author](https://img.shields.io/badge/Author-Ramid-orange)](https://github.com/RamidLab)
![JDK Version](https://img.shields.io/badge/JAVA-JDK8+-red.svg)
[![Spring Boot](https://img.shields.io/maven-central/v/org.springframework.boot/spring-boot-dependencies.svg?label=Spring%20Boot&logo=Spring)](https://search.maven.org/artifact/org.springframework.boot/spring-boot-dependencies)
[![Spring Cloud](https://img.shields.io/maven-central/v/org.springframework.cloud/spring-cloud-dependencies.svg?label=Spring%20Cloud&logo=Spring)](https://search.maven.org/artifact/org.springframework.cloud/spring-cloud-dependencies) 
[![Spring Cloud Alibaba](https://img.shields.io/maven-central/v/com.alibaba.cloud/spring-cloud-alibaba-dependencies.svg?label=Spring%20Cloud%20Alibaba&logo=Spring)](https://search.maven.org/artifact/com.alibaba.cloud/spring-cloud-alibaba-dependencies)

**English** | [中文](./README.zh-CN.md)

## Introduction

The backend of Rabbit Mind Universal Authentication is a high-performance distributed solution developed based on Spring Boot and Spring Cloud frameworks, aiming to provide a universal and extensible authentication and permission control module. The backend is based on modular design, supports multi-tenant architecture, integrates mainstream authentication and authorization mechanisms (such as JWT-based authentication and RBAC permission management), and provides efficient user management, role allocation, and resource access control capabilities.

## Note

The project is achieved by re-opening wemirr-platform. For details, please refer to the original author's [official website](https://docs.battcn.com/).

## Feature

- **Elegant layout**: Provides multiple themes and navigation modes, which can be freely combined.
- **Full-featured**: Fully supports SAAS, multi-tenancy and RBAC.
- **WebSocket** message push: Real-time distributed message push is achieved through Redis.
- **Dynamic gateway**: Supports the opening and closing of dynamic routing configuration through Redis and Nacos.
- **Performance**: In a 2MB network environment, the response time is usually between 10-150 milliseconds, and the slowest does not exceed 300 milliseconds.

## Features

- **Elegant layout**: Provides multiple themes and navigation modes, which can be freely combined.
- **Full-featured**: Fully supports SAAS, multi-tenancy and RBAC.
- **WebSocket** message push: Real-time distributed message push through Redis.
- **Dynamic gateway**: Supports the opening and closing of dynamic routing configuration through Redis and Nacos.
- **Performance**: In a 2MB network environment, the response time is usually between 10-150 milliseconds, and the slowest does not exceed 300 milliseconds.

## Architecture

- **Technology stack**: Spring Cloud Alibaba 2023, Spring Cloud 2023, Nacos, Sentinel, Mybatis-Plus, multi-tenant, Sa-Token, Redis, MySQL, RabbitMQ

- **Communication**: WebSocket, Redis (distributed message push)

- **Distributed tasks**: snail-job for task scheduling

- **Log and link tracking**: skywalking integrated for link tracking (other link tracking solutions are optional)

- **Dynamic gateway management**: support dynamic routing management configured with Redis and Nacos

## Preview

> Platform account 0000 Account admin Password 123456

> Tenant account 8888 Account admin Password 123456

## Documentation

## Installation and use

- Get the project code

```bash
git clone https://github.com/Rabbit-Mind/UniAuthPlatform-Backend.git
```

- Install project dependencies

```bash
cd UniAuthPlatform-Backend

mvn install ramid-ua-platform-dependencies

mvn install ramid-ua-platform-framework
```

- Install environment dependencies (Docker installation is provided below)

Basic services such as Redis, MySQL, Nacos and RabbitMQ can be installed using the following Docker commands:

```bash
docker pull redis:latest
docker run -itd --name redis -p 6379:6379 redis

docker pull mysql:latest
docker run -itd --name mysql-test -p 3306:3306 -e MYSQL_ROOT_PASSWORD={{Set your own password}} mysql

docker pull nacos/nacos-server
docker run --name nacos -itd -p 8848:8848 -p 9848:9848 -p 9849:9849 --restart=always -e MODE=standalone nacos/nacos-server

docker pull docker.io/macintoshplus/rabbitmq-management
docker run -d -p 5671:5671 -p 5672:5672 -p 15672:15672 rabbitmq_image_id
```

Optional services (not required)

If you need to use Sentinel traffic protection and Skywalking (link tracking), you can choose to deploy these services:

```bash
docker pull elasticsearch:7.9.3
docker pull apache/skywalking-oap-server:8.5.0-es7
docker pull apache/skywalking-ui:8.5.0 docker network create ramid docker run --name elasticsearch --net ramid -p 9200:9200 -d -e "discovery.type=single-node" elasticsearch:7.9.3 docker run --name oap --net ramid -p 1234:1234 -p 12800:12800 -p 11800:11800 -d -e SW_STORAGE=elasticsearch7 -e SW_STORAGE_ES_CLUSTER_NODES=elasticsearch:9200 apache/skywalking-oap-server:8.5.0-es7 docker run --name oap-ui --net ramid -p 10086:8080 -d -e TZ=Asia/Shanghai -e SW_OAP_ADDRESS=oap:12800 apache/skywalking-ui:8.5.0
```

If you want to use Skywalking, please add Skywalking Agent in the JVM parameters:

```bash
VmOption -javaagent:/path/to/skywalking-agent.jar
```

Example:

```bash
nohup java -javaagent:/path/to/skywalking-agent.jar -Dskywalking.agent.service_name=ramid-ua-platform -Dskywalking.collector.backend_service=127.0.0.1:11800 -jar ramid-ua-platform.jar -d > logs/start.log &

```

- Run

- Package

## Changelog

[CHANGELOG](https://github.com/Rabbit-Mind/UniAuthPlatform-Backend/releases)

## How to contribute

You are very welcome to join us! [Raise an Issue](https://github.com/Rabbit-Mind/UniAuthPlatform-Backend/issues/new/choose) or submit a Pull Request.

**Pull Request:**

1. Fork the code!

2. Create your own branch: `git checkout -b feature/xxxx`

3. Commit your changes: `git commit -am 'feat(function): add xxxxx'`

4. Push your branch: `git push origin feature/xxxx`

5. Submit `pull request`

## Git contribution submission specification

- Refer to [vue](https://github.com/vuejs/vue/blob/dev/.github/COMMIT_CONVENTION.md) specification ([Angular](https://github.com/conventional-changelog/conventional-changelog/tree/master/packages/conventional-changelog-angular))

- `feat` Add new features
- `fix` Fix problems/bugs
- `style` Code style related without affecting the running results
- `perf` Optimize/performance improvement
- `refactor` Refactor
- `revert` Undo changes
- `test` Test related
- `docs` Documents/comments
- `chore` Dependency updates/Scaffolding configuration changes, etc.
- `ci` Continuous integration
- `types` Type definition file changes
- `wip` Under development

## Maintainer

[@Ramid](https://github.com/RamidLab)

## Contribution

## Communication