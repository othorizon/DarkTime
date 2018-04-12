# spring boot 读取配置文件

[四种读取properties文件的方式](https://www.imooc.com/article/18252)

## 通过@ConfigurationProperties方式

```java 读取配置文件
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "bill")
public class BillActivitiConfig {

    @Getter
    @Setter
    private static RoleId roleId;

    @Getter
    @Setter
    private static Email email;

    @Getter
    @Setter
    public static class RoleId {
        private String cal;
        private String convert;
        private String push;
    }

    @Getter
    @Setter
    public static class Email {
        private String receiver;
        private String cc;
    }
}
```

```properties application-dev.properties
# bill
# 这里注意java文件中属性名是驼峰大小写，写到配置文件应该使用 ‘-’ 分割的全小写
bill.role-id.cal=21
bill.role-id.convert=22
bill.role-id.push=23
#notify email
bill.email.receiver=zhanghao6@kingsoft.com
bill.email.cc=GUOMEINA@kingsoft.com
```

ConfigurationProperties 注解需要添加依赖

```xml pom.xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>
```