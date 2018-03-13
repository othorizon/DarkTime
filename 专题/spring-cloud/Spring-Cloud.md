# Spring Cloud 整理汇总

按照目前已学习的资料做了简单整理

[TOC]

## 概述

cloud的微服务是采用Rest协议，dubbo采用的则是RPC协议

## 框架

**[Eureka](#eureka)**：
服务中心

**[Config](#config)**：
配置中心

**[Zuul](#zuul)**：
网关服务，外部应用访问微服务内部的API网关

**[Bus](#bus)**：
消息总线，AMQP的方式(目前支持Kafka和RabbitMQ)实现夸应用的spring 事件发布与监听
配置中心配置变更后通知所有客户端更新配置就是借助消息总线实现的。

**[Admin](#admin)**:
监控服务，提供了监控界面来统一监控所有服务的状态，有报警功能。
`Spring Boot Actuator`提供了对单个Spring Boot的监控,而Admin则可以统一监控所有服务

## 细说

### 创建Cloud项目

父POM配置

```xml

```

### Eureka

[springcloud(二)：注册中心Eureka](http://www.ityouknow.com/springcloud/2017/05/10/springcloud-eureka.html)
依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-eureka-server</artifactId>
</dependency>
```

### Config

依赖

```xnk

```

### Zuul

### Bus

### Admin
