# 非Spring Boot Web项目 注册节点到Eureka Server并提供服务

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
 在web.xml中添加监听器配置
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
 `eureka.name` 是服务在服务注册中心的页面中展示的名字（Application一列显示的名字）
 `eureka.vipAddress` 是调用方调用时的服务名字，即FeignClient注解位置填写的名字`@FeignClient(name = "MVC-SERVICE")`
 fegin调用使用的是`eureka.vipAddress`，而在spring cloud中的网关服务，即zuul服务，则使用的是`eureka.name`来访问，所以两个尽可能一致
