# spring配置文件

## 参考

[spring context:component-scan 使用说明(转)](https://www.cnblogs.com/youngjoy/p/3817471.html)
[**为啥Spring和Spring MVC包扫描要分开？**](http://labreeze.iteye.com/blog/2359957)
[Springmvc controller 层 @Transactional 不起作用](http://blog.csdn.net/qq_36776347/article/details/77224468)

## 知识点

spring mvc是对spring的扩展，在spring的基础上增加了对`<servlet>`的更多的支持

####  关于Spring事务\<tx:annotation-driven/\>的理解
参考： [关于Spring事务\<tx:annotation-driven/\>的理解](http://blog.csdn.net/catoop/article/details/50067785)

注解前缀：
  在使用SpringMvc的时候，配置文件中我们经常看到 annotation-driven 这样的注解，其含义就是支持注解，一般根据前缀 tx、mvc 等也能很直白的理解出来分别的作用。<tx:annotation-driven/> 就是支持事务注解的（@Transactional） 、<mvc:annotation-driven> 就是支持mvc注解的，说白了就是使Controller中可以使用MVC的各种注解。

事物配置位置说明：
>\<tx:annotation-driven/\> only looks for @Transactional on beans in the same application context it is defined in. This means that, if you put \<tx:annotation-driven/\> in a WebApplicationContext for a DispatcherServlet, it only checks for @Transactional beans in your controllers, and not your services. 

意思就是：**\<tx:annoation-driven/\>只会查找和它在相同的应用上下文（spirng的上下文 和 spring mvc的上下文就是两个不同的上下文）**中定义的bean上面的@Transactional注解，如果你把它放在Dispatcher的应用上下文中，它只检查控制器（Controller）上的@Transactional注解，而不是你services上的@Transactional注解。
    所以，可以确定的是我们是可以在Controller上使用事务注解的，但是我们不推荐这样做（本人也从来没有这样做过），这里只是为了说明spring对\<tx:annotation-driven/\>的使用。

补充说明：
如果我们在spring的配置文件中

#### 加载顺序
``` xml
<context-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>classpath:spring-context.xml,classpath*:datasource-*.xml</param-value>
	</context-param>

	<listener>
		<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
	</listener>

	<servlet>
		<servlet-name>spring</servlet-name>
		<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
		<init-param>
			<param-name>contextConfigLocation</param-name>
			<param-value>classpath:spring-servlet.xml</param-value>
		</init-param>
		<load-on-startup>1</load-on-startup>
	</servlet>
	<servlet-mapping>
		<servlet-name>spring</servlet-name>
		<url-pattern>/</url-pattern>
	</servlet-mapping> 
```
**在这个配置文件中，contextConfigLocation先加载，DispatcherServlet后加载**






#### [SpringMVC Controller配置和事务问题](https://segmentfault.com/q/1010000003791904)

*原理：Spring 是父容器， Spring MVC是子容器， 子容器可以访问父容器的bean,父容器不能访问子容器的bean*
参考：[**为啥Spring和Spring MVC包扫描要分开？**](http://labreeze.iteye.com/blog/2359957)


**配置如下：**  

dispatcherServlet配置 spring-mvc.xml(spring-mvc 配置）
mvc是子容器
mvc配置加载controller，并排出其他的，注意要使用`use-default-filters="false"`不去使用默认扫描

``` xml
<!-- 使用Annotation自动注册Bean,只扫描@Controller -->
    <context:component-scan base-package="com.mxmht.xxx" use-default-filters="false"><!-- base-package 如果多个，用“,”分隔 -->
        <context:include-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
    </context:component-scan>
```

contextConfigLocation 配置spring-context.xml(spring 配置）
spring是主/父容器
`exclude-filter`会在默认扫描(use-default-filters不指定会使用默认的扫描)的注解中过滤掉指定的注解

``` xml
    <!-- 使用Annotation自动注册Bean，解决事物失效问题：在主容器中不扫描@Controller注解，在SpringMvc中只扫描@Controller注解。  -->
    <context:component-scan base-package="com.mxmht.xxx"><!-- base-package 如果多个，用“,”分隔 -->
        <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
    </context:component-scan>
```
    
这个就相当于base-package="com.mxmht.xxx" 楼上的com.xxx


#### [Springmvc controller 层 @Transactional 不起作用](http://blog.csdn.net/qq_36776347/article/details/77224468)

> 将Spring的配置controller的扫描关掉了，让Spring-MVC自己去扫描自己的controller.
@transanction解释器的部分，将@transanction解释器在Spring-mvc中开启，这样就ok了。  
       自己在想了一下实际上就是Spring与Spring mvc配置文件解析的是分开的，你在applicationContext.xml里面开启了@transanction解释器，就会在applicationContext.xml配置的扫描包的时候把扫描到的@transanction这样的注解开启事务，然后Spring-mvc.xml也有扫描而且没有开启解释器就把，有事务功能的controller替换为没有事务功能的controller，@Transactional 就不起作用


## application.properties文件配置

### SpringBoot配置属性之DataSource

[SpringBoot配置属性之DataSource - xixicat - SegmentFault](https://segmentfault.com/a/1190000004316491)

```properties
datasource.boss.url=jdbc:mysql://10.0.0.0:9800/boss
datasource.boss.username=root
datasource.boss.password=123456
datasource.boss.driver-class-name=com.mysql.jdbc.Driver
datasource.boss.max-idle=10
datasource.boss.max-wait=10000
datasource.boss.min-idle=5
datasource.boss.initial-size=5

#指定获取连接时连接校验的sql查询语句.
datasource.boss.validation-query=SELECT 1

#当从连接池借用连接时，是否测试该连接.
datasource.boss.test-on-borrow=true

#当连接空闲时，是否执行连接测试.
datasource.boss.test-while-idle=true

#指定空闲连接检查、废弃连接清理、空闲连接池大小调整之间的操作时间间隔
datasource.boss.time-between-eviction-runs-millis=60000
```