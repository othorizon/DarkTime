# spring 最佳实践

## 概要

- 如何配置拦截器：interceptor、filter、@RestControllerAdvice
- bean的初始化： InitializingBean接口、@conditionXXX 注解
- 如何获取applicationContext上下文： ApplicationContextAware
- 枚举的优雅使用： 1、如何把枚举作为接口的交互参数：@JsonCreator、@JsonValue ,要注意fastjson和spring采用的jackson 对注解的支持 2、valueOfByXX
- 缓存的优雅使用： @cacheable 、CaffeineCacheManager
- 配置文件配置时间属性：java.time.Duration
- 正确的报错方式，spring的国际化： <https://www.jianshu.com/p/4d5f16f6ab82>

第三方工具的使用

- RestTempalte的优雅使用：工具类的封装、header的注入
- 借助Mybatis-Plus实现零SQL开发
- 借助MapStruct实现po、bo、vo等对象之间的转换
- 健康接口，版本检查： buildnumber-maven-plugin
- Jenkins的常用配置方式

### 在项目中的实践

- 项目必备的工具包：apache-common系列、gson 等
- 如何划分项目、划分package
- 借助@RestControllerAdvice实现全局统一的response返回，方法直接通过抛异常来返回
- 更好的实现项目的初始化相关操作
