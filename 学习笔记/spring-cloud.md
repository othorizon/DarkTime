# spring cloud

## 参考资料

### spring cloud 优质教程集合

- [springboot学习资料汇总 - 纯洁的微笑博客](http://www.ityouknow.com/springboot/2015/12/30/springboot-collect.html)
- [springcloud学习资料汇总-纯洁的微笑博客](http://www.ityouknow.com/springcloud/2016/12/30/springcloud-collect.html)
- [spring-boot-纯洁的微笑博客](http://www.ityouknow.com/spring-boot.html)
- [Spring Cloud 简单教程 持续更新中 - 搜云库](https://segmentfault.com/a/1190000012648038)
- [史上最简单的 SpringCloud 教程](http://blog.csdn.net/forezp/article/details/70148833)

### 其他资料

- [Spring Cloud Eureka 常用配置及说明](https://www.cnblogs.com/li3807/p/7282492.html)
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

### 配置中心

可以使用消息总线，即借助mq来分发通知配置文件的修改。
也可以不使用总线，则每个client需要手动触发`/refresh`事件来重新获取配置

#### 不使用消息总线

[springcloud(七)：配置中心svn示例和refresh - 纯洁的微笑博客](http://www.ityouknow.com/springcloud/2017/05/23/springcloud-config-svn-refresh.html)
1.客户端添加`spring-boot-starter-actuator`依赖,该包具有一套完整的监控，其中包括一个`/refresh`功能
2.需要给加载变量的类上面加载`@RefreshScope`,在客户端执行`/refresh`(**POST请求**)的时候就会更新此类下面的变量值
3.添加配置`management.security.enabled=false`关闭安全认证
4.配置变更后，客户端以post请求的方式来访问`http://localhost:8002/refresh`(`curl -X POST http://localhost:8002/refresh`)

#### 使用消息总线

[springcloud(九)：配置中心和消息总线（配置中心终结版）](http://www.ityouknow.com/springcloud/2017/05/26/springcloud-config-eureka-bus.html)

### 坑点

#### eureka集群中replicas显示为unavailable

[spring cloud  unavailable-replicas - CSDN博客](http://blog.csdn.net/u012470019/article/details/77973156)

启动服务时虽然采用  --spring.profiles.active = peerl 指定了配置文件，但还是会从 application.properties 中取值，将application.properties的

``` preperties
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

注释掉或者 在 application-peer1.properties 与 application-peer2.properties 中显示 指定 这两个属性值为true即可。

#### eureka列表显示ip

[eureka的Instances status列表显示ip](http://breezylee.iteye.com/blog/2393447)
服务提供者配置

```properties
eureka.instance.prefer-ip-address=true
eureka.instance.instance-id=${spring.cloud.client.ipAddress}:${server.port}
```

## 非Boot项目 注册到spring cloud（eureka server） 提供服务
[非Spring Boot Web项目 注册节点到Eureka Server并提供服务 - CSDN博客](http://blog.csdn.net/qq_32193151/article/details/72559783)
[非Spring Boot Web项目 注册节点到Eureka Server并提供服务 -evernote](https://app.yinxiang.com/shard/s9/nl/679699/e427b4d2-4c42-4afe-bb59-e9af56776147/)
[非Spring Boot Web项目 注册节点到Eureka Server并提供服务 - CSDN博客](http://blog.csdn.net/songmaolin_csdn/article/details/77880324)

step1:
 引用 eureka client的包,**要注意这里的jar包版本要与Spring Boot项目依赖的eureka-client jar包版本一致 不然可能不兼容**
 ```xml
 <dependency>
    <groupId>com.netflix.eureka</groupId>
    <artifactId>eureka-client</artifactId>
    <version>1.4.12</version>
</dependency>
 ```

step2:
 写监听代码注册到eureka server
 ```java
import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryManager;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by Kowalski on 2017/5/18
 * Updated by Kowalski on 2017/5/18
 */
@Slf4j
public class EurekaInitAndRegisterListener implements ServletContextListener {

    /**
     * * Notification that the web application initialization
     * * process is starting.
     * * All ServletContextListeners are notified of context
     * * initialization before any filter or servlet in the web
     * * application is initialized.
     *
     * @param sce
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        registerWithEureka();
    }

    public void registerWithEureka() {
        /**加载本地配置文件 根据配置初始化这台 Eureka Application Service 并且注册到 Eureka Server*/
        DiscoveryManager.getInstance().initComponent(
                new MyInstanceConfig(),
                new DefaultEurekaClientConfig());

        /**本台 Application Service 已启动，准备好侍服网络请求*/
        ApplicationInfoManager.getInstance().setInstanceStatus(
                InstanceInfo.InstanceStatus.UP);

        log.info("o2o eureka Application Service initing and registering");

        /**Application Service 的 Eureka Server 初始化以及注册是异步的，需要一段时间 此处等待初始化及注册成功 可去除*/
    // private static final DynamicPropertyFactory configInstance = DynamicPropertyFactory
    //         .getInstance();
    // String vipAddress = configInstance.getStringProperty(
    //         "eureka.vipAddress", "o2o").get();
    // InstanceInfo nextServerInfo = null;
    // while (nextServerInfo == null) {
    //     try {
    //         nextServerInfo = DiscoveryManager.getInstance()
    //                 .getDiscoveryClient()
    //                 .getNextServerFromEureka(vipAddress, false);
    //     } catch (Throwable e) {
    //         log.info("Waiting for service to register with eureka..");
    //         try {
    //             Thread.sleep(10000);
    //         } catch (InterruptedException e1) {
    //             e1.printStackTrace();
    //         }
    //     }
    // }
    // log.info("Service started and ready to process requests..");
    }

    /**
     * * Notification that the servlet context is about to be shut down.
     * * All servlets and filters have been destroy()ed before any
     * * ServletContextListeners are notified of context
     * * destruction.
     *
     * @param sce
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DiscoveryManager.getInstance().shutdownComponent();
    }
}
 ```

step3:
 重写配置文件。项目会报找不到eureka Server的hostName的错，在boot中有个preferIpAdress的配置，配置后会默认使用ip访问而不是主机名hostName，在原生的eureka 中写preferIpAdress配置不会被解析读取，因此我们仿照boot的该配置做法：
 ```java
import com.netflix.appinfo.MyDataCenterInstanceConfig;
import java.net.InetAddress;
import java.net.UnknownHostException;
 public class MyInstanceConfig extends MyDataCenterInstanceConfig{
    @Override
    public String getHostName(boolean refresh) {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return super.getHostName(refresh);
        }
    }

    /**
     * 实例id是显示在服务中心列表页的status字段，就是boot项目中的
     * `eureka.instance.instance-id=${spring.cloud.client.ipAddress}:${server.port}`这个配置，
     * 因为boot项目里可以获取ip地址来显示。所以这里采用重写方法的方式来实现
     * @return
     */
    @Override
    public String getInstanceId() {
        try {
            return InetAddress.getLocalHost().getHostAddress()+":"+getNonSecurePort();
        } catch (UnknownHostException e) {
            return "Unknown";
        }
    }
}
 ```

step4:
 在web.xml中添加监听器配置
  ```xml
    <listener>
    <description>eureka监听器</description>
    <listener-class>top.kou.mvcservice.service.EurekaInitAndRegisterListener</listener-class>
  </listener>
  ```

step5:
 在配置目录下添加config.properties文件（默认读取的文件名）添加配置
 ```properties
 #部署应用程序的区域
 # - 对于AWS指定一个AWS区域
 #  - 对于其他数据中心，指定一个指示该区域的任意字符串。
 # 这里主要指定美国东部D
 eureka.region=default

 #服务指定应用名，这里指的是eureka服务本身（相当于boot中的app.name）
 eureka.name=MVC-SERVICE

 #客户识别此服务的虚拟主机名，调用方使用该名字调用服务（相当于boot中的serviceId）
 eureka.vipAddress=MVC-SERVICE

 #服务将被识别并将提供请求的端口（web服务部署的tomcat端口）
 eureka.port=8810

 #设置为false，因为该配置适用于eureka服务器本身的eureka客户端。
 #在eureka服务器中运行的eureka客户端需要连接到其他区域中的服务器。
 #对于其他应用程序，不应设置（默认为true），以实现更好的基于区域的负载平衡。
 eureka.preferSameZone=true

 #如果要使用基于DNS的查找来确定其他eureka服务器（请参见下面的示例），请更改此选项
 eureka.shouldUseDns=false
 eureka.us-east-1.availabilityZones=default
 #由于shouldUseDns为false，因此我们使用以下属性来明确指定到eureka服务器的路由（eureka Server地址）
 eureka.serviceUrl.default=http://localhost:7003/eureka/
 ```
 特别注意：
 `eureka.name` 是服务在服务注册中心的页面中展示的名字（Application一列显示的名字）
 `eureka.vipAddress` 是调用方调用时的服务名字，即FeignClient注解位置填写的名字`@FeignClient(name = "MVC-SERVICE")`
 fegin调用使用的是`eureka.vipAddress`，而在spring cloud中的网关服务，即zuul服务，则使用的是`eureka.name`来访问，所以两个尽可能一致，或者注释掉`eureka.vipAddress`，这样会读取`eureka.name`的值