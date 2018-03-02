### 11
[测试类写法](https://stackoverflow.com/questions/42998911/spring-boot-jpa-test-unable-to-find-a-springbootconfiguration-when-doing-a)

### 修改日志工具为slf4j
排除原日志依赖,并引入log4j的包

``` xml
<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-logging</artifactId>
                </exclusion>
            </exclusions>
</dependency>
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-log4j</artifactId>
            <version>1.3.8.RELEASE</version>
</dependency>
```
### 数据源配置

``` java
@Configuration
@MapperScan(basePackages ={"com.rule.converter.dao.boss"},sqlSessionFactoryRef = "bossSqlSessionFactory")
public class BossDataSourceConfig {
    @Primary
    @Bean(name = "bossDataSource")
    @ConfigurationProperties(prefix = "datasource.boss")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "bossTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("bossDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Primary
    @Bean(name = "bossSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("bossDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:com/rule/converter/dao/boss/**/*.xml"));
//        factoryBean.setTypeAliasesPackage("com.dc.es.report.pojo.main");
        return factoryBean.getObject();
    }
}
```


### jenkins配置

启动脚本

``` bash
#!/bin/sh -ex
BUILD_ID=tomcat8-ruleconvert

APP_HOME=/usr/local/boot-ruleconvert-9530

echo branch:$branch

mvn clean package -Dmaven.test.skip=true -P${profile} -U


ssh 10.0.0.0 "pkill -9 -f ${APP_HOME}" || echo noProcess

ssh 10.0.0.0 "rm -f ${APP_HOME}/convert.jar"

scp target/convert.jar 10.0.0.0:$APP_HOME/convert.jar

ssh 10.0.0.0 "sh -l ${APP_HOME}/restart.sh"
#下面这个方案行不通无论是否使用nohup都会在ssh断开后结束程序
#ssh 10.0.0.0 "java -jar ${APP_HOME}/convert.jar --server.port=9530 > /dev/null 2>&1 &"

```

服务器上的restart.sh

``` bash
#!/bin/sh

APP_HOME=/usr/local/boot-ruleconvert-9530
pkill -9 -f $APP_HOME/convert.jar || echo noProcess
#这里必须要cd到app目录，因为jenkins的ssh没有指定目录的情况下，执行命令会在/home/{jenkins-user} 目录下，所以如果日志配置了相对路径，则会打印到home/user目录下
cd $APP_HOME
#nohup确保不会中断
nohup java -jar $APP_HOME/convert.jar --server.port=9530  > /dev/null 2>&1 &
```

```bash
#!/bin/sh

APP_NAME=$1
APP_HOME=$2
PORT=$3

echo $APP_NAME
echo $APP_HOME

pkill -9 -f $APP_HOME/$APP_NAME.jar || echo noProcess
cd $APP_HOME
#nohup确保不会中断
nohup java -jar $APP_NAME.jar --server.port=$PORT --logging.file=logs/$APP_NAME.log  >/dev/null 2>&1 &
```

