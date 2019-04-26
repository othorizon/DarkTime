# maven项目增加编译版本号 buildnumber-maven-plugin

buildnumber-maven-plugin 这个maven插件可以在编译项目时将git的版本号、时间戳等信息存储到pom中，  
然后可以在代码接口中去读取这些值来方便的查阅代码的部署版本与时间。  

[buildnumber-maven-plugin](http://www.mojohaus.org/buildnumber-maven-plugin/usage.html)  

```pom
<!-- pom配置 -->
  <build>
    <plugins>
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>buildnumber-maven-plugin</artifactId>
            <version>1.4</version>
            <executions>
                <execution>
                    <phase>validate</phase>
                    <goals>
                        <goal>create</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <items>
                    <item>timestamp</item>
                    <item>buildNumber</item>
                </items>
            </configuration>
        </plugin>
    </plugins>
  </build>
```

```yml
# application.yml配置 yml格式读取maven属性使用@@来表示，在properties格式中用${project.versio}
projectBuildVersionInfo:
  version: @project.version@
  buildTimestamp: @timestamp@
  scmVersion: @buildNumber@
```

```java
@RestController
@RequestMapping("/health")
public class ServerHealthController {

    @Autowired
    private VersionInfo versionInfo;

    @GetMapping(value = "/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("version")
    public VersionInfo buildVersion() {
        return versionInfo;
    }
    @Data
    @Component
    @ConfigurationProperties(prefix = "project-build-version-info")
    public static class VersionInfo {
        private String version;
        private String buildTimestamp;
        private String scmVersion;
    }
}
```