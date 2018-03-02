# spring cloud

## 参考资料

### spring cloud 优质教程集合

- [springboot学习资料汇总 - 纯洁的微笑博客](http://www.ityouknow.com/springboot/2015/12/30/springboot-collect.html)
- [springcloud学习资料汇总-纯洁的微笑博客](http://www.ityouknow.com/springcloud/2016/12/30/springcloud-collect.html)
- [spring-boot-纯洁的微笑博客](http://www.ityouknow.com/spring-boot.html)
- [Spring Cloud 简单教程 持续更新中 - 搜云库](https://segmentfault.com/a/1190000012648038)
- [史上最简单的 SpringCloud 教程](http://blog.csdn.net/forezp/article/details/70148833)

### 其他资料

- [非Spring Boot Web项目 注册节点到Eureka Server并提供服务 - CSDN博客](http://blog.csdn.net/songmaolin_csdn/article/details/77880324)
- [使用spring-boot-admin对spring-boot服务进行监控](http://www.ityouknow.com/springboot/2018/02/11/spring-boot-admin.html)
- [为你的spring cloud微服务添加宕机邮件通知/服务报警](http://blog.csdn.net/rickiyeat/article/details/73228713)
- [从零开始，轻松搞定SpringCloud微服务系列 - 千万之路刚开始 - 博客园](http://www.cnblogs.com/hyhnet/p/7998751.html)
- [项目改造接入Spring Cloud流程](http://blog.csdn.net/xinluke/article/details/68064599)
- [微服务架构的基础框架选择：Spring Cloud还是Dubbo？ - CSDN博客](http://blog.csdn.net/kobejayandy/article/details/52078275)

#### fegin http请求工具，Feign使得 Java HTTP 客户端编写更方便

- [Feign真正正确的使用方法](https://www.jianshu.com/p/3d597e9d2d67)
- [Feign基础教程 - CSDN博客](http://blog.csdn.net/u010862794/article/details/73649616)

## 学习笔记

### 监控

`Spring Boot Admin` 是一个管理和监控Spring Boot 应用程序的开源软件。每个应用都认为是一个客户端，通过HTTP或者使用 Eureka注册到admin server中进行展示，Spring Boot Admin UI部分使用AngularJs将数据展示在前端。[使用spring-boot-admin对spring-boot服务进行监控](http://www.ityouknow.com/springboot/2018/02/11/spring-boot-admin.html)

### 网关服务

提供一个对外的统一api接口，使外部的服务可以通过正常的http请求来访问注册到服务中心的服务
请求方式：`{网关的host}:{port}/{注册到服务中心的application-name 小写}/REQUEST PATH` eg:`http://koyou.top:8020/customer-service/getNeoHello`

### spring mvc 注册到spring cloud中

[非Spring Boot Web项目 注册节点到Eureka Server并提供服务 - CSDN博客](http://blog.csdn.net/songmaolin_csdn/article/details/77880324)